package com.wak.igo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.MemberInfo;
import com.wak.igo.dto.request.TokenDto;
import com.wak.igo.dto.response.MemberResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.jwt.TokenProvider;
import com.wak.igo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public ResponseDto<?> logout(UserDetailsImpl userDetails){
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        return tokenProvider.deleteRefreshToken(userDetails.getMember());
    }

    public ResponseDto<?> kakaologin(String code, HttpServletResponse response) throws JsonProcessingException {
        String accessToken = getAccessToken(code);  // 인가 코드로 전체 response 요청해서 access token를 받아온다.
        MemberInfo kakaoUserInfo = getkakaoUserInfo(accessToken);  // access token 으로 api 요청해서 회원정보를 받아온다.
        Member kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo); // DB에 회원이 존재하지 않으면 회원정보를 저장한다(회원가입)
        Authentication authentication = forceLogin(kakaoUser); // 강제 로그인
        UserDetailsImpl userDetails = kakaoUsersAuthorizationInput(authentication, response); // 로그인 인증정보로 jwt 토큰 생성, header에 Jwt 토큰 추가.
        MemberResponseDto memberInfo = memberInfo(userDetails); // 회원정보 가져오기
        return ResponseDto.success(memberInfo);

    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
//        body.add("client_id", "fdb42734830cbb186c8221bf3acdd6c6");        // localhost client_id
        body.add("client_id", "3d365192ea8ab4f32c7f9c1d7c5688e1");          // 프론트엔드 client_id
        body.add("client_secret", "FuvfQecT3uPmfM3wlzF5VxRJU7Iz654F");
//        body.add("redirect_url", "http://localhost:8080/kakao/callback"); // localhost redirect_url
        body.add("redirect_url", "http://localhost:3000/kakao/callback");// 프론트 엔드  redirect_url
//        body.add("redirect_uri", "http://eunjiroh.shop/kakaoloading");     // 프론트엔드 도메인 배포 client_id
//        body.add("redirect_uri", "http://3.88.14.18/kakaoloading");     // 프론트엔드 배포 client_id
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private MemberInfo getkakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // 사용자 정보 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("id").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        log.info("카카오 사용자 정보: " + id + ", " + nickname);
        return new MemberInfo(id, nickname);
    }

    private Member registerKakaoUserIfNeeded(MemberInfo kakaoUserInfo) {

        String kakaoId = kakaoUserInfo.getMemberId();                   // DB 에 중복된 Kakao Id 가 있는지 확인

        Member kakaoUser = memberRepository.findByMemberId(kakaoId)
                .orElse(null);
        // 회원가입
        if (kakaoUser == null) {
            String nickname = kakaoUserInfo.getNickname();              // nickname: kakao nickname
            String password = UUID.randomUUID().toString();             // password: random UUID
            String encodedPassword = passwordEncoder.encode(password);  // password 암호화

            kakaoUser = Member.builder()
                    .nickname(nickname)
                    .password(encodedPassword)
                    .memberId(kakaoId)
                    .build();
            memberRepository.save(kakaoUser);
            log.info(nickname + "회원가입이 완료되었습니다.");
        }
        return kakaoUser;
    }

    private Authentication forceLogin(Member kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private UserDetailsImpl kakaoUsersAuthorizationInput(Authentication authentication, HttpServletResponse response) {
        // response header에 token 추가

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        TokenDto token = tokenProvider.generateTokenDto(userDetails);
        response.addHeader("Authorization", "BEARER" + " " + token.getAccessToken());
        response.addHeader("RefreshToken", token.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", token.getAccessTokenExpiresIn().toString());

        return userDetails;
    }

    private MemberResponseDto memberInfo(UserDetailsImpl userDetails){
        List<String> tag = new ArrayList<>();
        Member member = userDetails.getMember();

        // 가입 후 이미지나 관심사가 null일 때
        List<String> tags = member.getInterested();
        if (tags == null) {
            tag.add("false");
        } else {
            tag = tags;
        }


        String profileImg = (member.getProfileImage() == null) ? "false" : member.getProfileImage();
        MemberResponseDto response = MemberResponseDto.builder()
                .nickname(member.getNickname())
                .profileImage(profileImg)
                .interested(tag)
                .build();
        return response;
    }
}