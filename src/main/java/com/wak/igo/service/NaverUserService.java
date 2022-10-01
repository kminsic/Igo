package com.wak.igo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.response.MemberResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.jwt.TokenProvider;
import com.wak.igo.repository.MemberRepository;
import com.wak.igo.dto.request.MemberInfo;
import com.wak.igo.dto.request.TokenDto;
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
public class NaverUserService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    public ResponseDto<?> naverlogin(String code, String state, HttpServletResponse response) throws JsonProcessingException {
        String accesstoken = getAccessToken(code, state); // 인가 코드로 전체 response 요청해서 access token를 받아온다.
        MemberInfo MemberInfo = getMemberInfo(accesstoken); // access token 으로 api 요청해서 회원정보를 받아온다.
        Member naverUser = registerNaverUserIfNeeded(MemberInfo); // DB에 회원이 존재하지 않으면 회원정보를 저장한다(회원가입)
        Authentication authentication = forceLogin(naverUser); // 강제 로그인
        UserDetailsImpl userDetails = naverUsersAuthorizationInput(authentication, response); // 로그인 인증정보로 jwt 토큰 생성, header에 Jwt 토큰 추가.
        MemberResponseDto memberInfo = memberInfo(userDetails); // 회원정보 가져오기
        return ResponseDto.success(memberInfo);
    }

    private String getAccessToken(String code, String state) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
//        body.add("client_id", "DmLVvurxVnPCqlnSp0XZ");      // localhost client_id
        body.add("client_id", "1tmOBpKKBicBaUmPQpaF");        // 프론트엔드 client_id
//        body.add("client_secret", "9fbJI0kZub");            // localhost client_secret
        body.add("client_secret", "ybrSh2bxg2");              // 프론트엔드 client_secret
        body.add("code", code);
        body.add("state", state);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private MemberInfo getMemberInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> MemberInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                MemberInfoRequest,
                String.class
        );

        // 사용자 정보 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String memberId = jsonNode.get("response").get("id").asText();
        String nickname = jsonNode.get("response").get("name").asText();
        log.info("네이버 사용자 정보: " + memberId + ", " + nickname);
        return new MemberInfo(memberId, nickname);
    }

    private Member registerNaverUserIfNeeded(MemberInfo MemberInfo) {
        String naverId = MemberInfo.getMemberId();                      // DB 에 중복된 Kakao Id 가 있는지 확인
        Member naverUser = memberRepository.findBymemberId(naverId)
                .orElse(null);
        // 회원가입
        if (naverUser == null) {


            String nickname = MemberInfo.getNickname();                 // nickname: naver name
            String password = UUID.randomUUID().toString();             // password: random UUID
            String encodedPassword = passwordEncoder.encode(password);  // password 암호화

            naverUser = Member.builder()
                    .nickname(nickname)
                    .password(encodedPassword)
                    .memberId(naverId)
                    .build();
            memberRepository.save(naverUser);
            log.info(nickname + "회원가입이 완료되었습니다.");
        }
        return naverUser;
    }

    private Authentication forceLogin(Member naverUser) {
        UserDetails userDetails = new UserDetailsImpl(naverUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private UserDetailsImpl naverUsersAuthorizationInput(Authentication authentication, HttpServletResponse response) {
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