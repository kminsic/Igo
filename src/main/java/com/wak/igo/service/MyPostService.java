package com.wak.igo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.StringUtils;
import com.wak.igo.domain.Member;
import com.wak.igo.domain.MyPost;
import com.wak.igo.domain.State;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.MyPostRequestDto;
import com.wak.igo.dto.response.MyPostResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.repository.MyPostRepository;
import com.wak.igo.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MyPostService {
    private String imgUrl;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final MyPostRepository myPostRepository;
    private final StateRepository myPostStateRepository;
    private static final Tika tika = new Tika();

    // 일정 추가
    public ResponseDto<?> createSchedule(UserDetailsImpl userDetails, MultipartFile multipartFile, MyPostRequestDto requestDto) throws IOException{
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        System.out.println(multipartFile);
        Member member = userDetails.getMember();

        // s3 이미지 저장
        if (multipartFile.isEmpty()) {
              imgUrl = null;
        } else {
            // 이미지 형식 유효성 검사
            if (!validImgFile(multipartFile)) {
                throw new RuntimeException("올바른 이미지 형식이 아닙니다.");
            }
            imgUrl = imageUrl(multipartFile);
        }

        MyPost mypost = MyPost.builder()
                .title(requestDto.getTitle())
                .member(userDetails.getMember())
                .time(requestDto.getTime())
                .imgUrl(imgUrl)
                .content(requestDto.getContent())
                .build();
        myPostRepository.save(mypost);

        State state = myPostStateRepository.findMyPostStateByMyPost_Id(requestDto.getId()); // 일정 등록 상태 가져오기
        int done = (state==null) ? 0 : 1;

        MyPostResponseDto myPostResponseDto = MyPostResponseDto.builder()
                .id(requestDto.getId())
                .title(requestDto.getTitle())
                .time(requestDto.getTime())
                .imgUrl(imgUrl)
                .done(done)
                .content(requestDto.getContent())
                .createdAt(mypost.getCreatedAt())
                .modifiedAt(mypost.getModifiedAt())
                .build();
        return ResponseDto.success(myPostResponseDto);
    }

    // 일정 수정
    public ResponseDto<?> updateSchedule(UserDetailsImpl userDetails, MultipartFile multipartFile, MyPostRequestDto requestDto) throws IOException {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        MyPost mypost = myPostRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("수정할 일정이 없습니다."));

        String getImage = mypost.getImgUrl(); // mypost에 저장된 이미지 url
        String s3Image = amazonS3.getUrl(bucket, getImage).toString(); // s3에 저장된 이미지 url

        if (StringUtils.isNullOrEmpty(getImage)) {  // mypost에 저장된게 없을 때
            if ( !multipartFile.isEmpty() ) {
                // 이미지 형식 유효성 검사
                if (!validImgFile(multipartFile)) {
                    throw new RuntimeException("올바른 이미지 형식이 아닙니다.");
                }
                imgUrl = imageUrl(multipartFile);
            } else {
                imgUrl = null;
            }}
        else if (getImage.split("com/")[1] == s3Image){ // 변경 사항이 없을 때
            imgUrl = s3Image;}
        else { // mypost에 저장된 이미지가 있을 때
            if ( !multipartFile.isEmpty() ) {
                amazonS3.deleteObject(bucket, getImage.split("com/")[1]);
                imgUrl = imageUrl(multipartFile);
            } else {
                amazonS3.deleteObject(bucket, getImage.split("com/")[1]);
                imgUrl = null;
            }
        }
        mypost.update(requestDto, imgUrl);
        myPostRepository.save(mypost);

        State state = myPostStateRepository.findMyPostStateByMyPost_Id(requestDto.getId()); // 일정 등록 상태 가져오기
        int done = (state==null) ? 0 : 1;

        MyPostResponseDto myPostResponseDto = MyPostResponseDto.builder()
                .id(requestDto.getId())
                .title(requestDto.getTitle())
                .time(requestDto.getTime())
                .imgUrl(imgUrl)
                .done(done)
                .content(requestDto.getContent())
                .createdAt(mypost.getCreatedAt())
                .modifiedAt(mypost.getModifiedAt())
                .build();
        return ResponseDto.success(myPostResponseDto);
    }

    // 일정 삭제 (일정 등록 상태도 같이 삭제)
    public ResponseDto<?> deleteSchedule(UserDetailsImpl userDetails, Long id){
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        MyPost mypost = myPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 일정이 없습니다."));

        if (!StringUtils.isNullOrEmpty(mypost.getImgUrl())) {
            amazonS3.deleteObject(bucket, mypost.getImgUrl().split("com/")[1]); // s3 이미지 삭제
        }
        State state = myPostStateRepository.findMyPostStateByMyPost_Id(mypost.getId()); // 일정 등록 상태가 존재하면 같이 삭제
        if (!(state == null)){
            myPostStateRepository.delete(state);
        }
        myPostRepository.delete(mypost);
        return ResponseDto.success("나의 일정 삭제 완료");
    }

    // 회원 별 일정 목록 가져오기
    public List<?> getSchedule(UserDetailsImpl userDetails){
        List<MyPost> schedules = myPostRepository.findByMember(userDetails.getMember());
        List<MyPostResponseDto> scheduleList = new ArrayList<>();
        for (MyPost schedule : schedules) {
            State state = myPostStateRepository.findMyPostStateByMyPost_Id(schedule.getId()); // 일정 등록 상태 가져오기
            int done = (state==null) ? 0 : 1;
            scheduleList.add(MyPostResponseDto.builder()
                    .id(schedule.getId())
                    .title(schedule.getTitle())
                    .time(schedule.getTime())
                    .imgUrl(schedule.getImgUrl())
                    .done(done)
                    .content(schedule.getContent())
                    .createdAt(schedule.getCreatedAt())
                    .modifiedAt(schedule.getModifiedAt())
                    .build());
        }
        return scheduleList;
    }

    // 회원이 일정 완료 시킬 때 실행
    public ResponseDto<?> doneSchedule(UserDetailsImpl userDetails, Long id) {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        MyPost myPost = myPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));
        State state = State.builder()
                .myPost(myPost)
                .done(1)
                .build();
        myPostStateRepository.save(state);

        MyPostResponseDto myPostResponseDto = MyPostResponseDto.builder()
                .id(myPost.getId())
                .title(myPost.getTitle())
                .time(myPost.getTime())
                .imgUrl(myPost.getImgUrl())
                .done(1)
                .content(myPost.getContent())
                .createdAt(myPost.getCreatedAt())
                .modifiedAt(myPost.getModifiedAt())
                .build();
        return ResponseDto.success(myPostResponseDto);
    }

    // 회원이 완료된 일정을 취소할 때 실행
    public ResponseDto<?> cancelSchedule(UserDetailsImpl userDetails, Long id) {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        MyPost myPost = myPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));

        State state = myPostStateRepository.findMyPostStateByMyPost_Id(myPost.getId());
        myPostStateRepository.delete(state);

        MyPostResponseDto myPostResponseDto = MyPostResponseDto.builder()
                .id(myPost.getId())
                .title(myPost.getTitle())
                .time(myPost.getTime())
                .imgUrl(myPost.getImgUrl())
                .done(0)
                .content(myPost.getContent())
                .createdAt(myPost.getCreatedAt())
                .modifiedAt(myPost.getModifiedAt())
                .build();
        return ResponseDto.success(myPostResponseDto);
    }

    // s3에 이미지 업로드
    public String imageUrl(MultipartFile multipartFile) throws IOException{
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename(); // 파일 이름 중복되지 않게 랜덤한 값으로 업로드

        ObjectMetadata objMeta = new ObjectMetadata(); // ObjectMetadata를 통해 파일 사이즈를 ContentLength로 S3에 알려줌
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta); // S3 api 메소드 인 putObject를 이용해 파일 stream을 열어서 s3에 파일 업로드

        return amazonS3.getUrl(bucket, s3FileName).toString(); // S3에 업로드 된 사진 url 가져오기
    }

    public static boolean validImgFile(MultipartFile multipartFile) {
        try {
            // 업로드를 허용하는 파일 타입
            List<String> ValidTypeList = Arrays.asList("image/jpeg", "image/pjpeg", "image/png", "image/gif", "image/jpg");

            // 입력 받은 파일을 “파일종류/파일포맷” 으로 구분 짓는다
            String mimeType = tika.detect(multipartFile.getInputStream());

            // mimeType이 validTypeList 중 하나라도 만족하면 true 아니면 false
            boolean isValid = ValidTypeList.stream().anyMatch(ValidType -> ValidType.equalsIgnoreCase(mimeType));
            return isValid;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
