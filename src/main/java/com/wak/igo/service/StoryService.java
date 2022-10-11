package com.wak.igo.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.wak.igo.domain.Story;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.StoryRequestDto;
import com.wak.igo.dto.response.MemberResponseDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.dto.response.StoryResponseDto;
import com.wak.igo.repository.MemberRepository;
import com.wak.igo.repository.StoryRepository;
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
public class StoryService {

    private String video;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final StoryRepository storyRepository;
    private final MemberRepository memberRepository;
    private static final Tika tika = new Tika();


    //스토리 조회
    public List<?> getStory(UserDetailsImpl userDetails, MemberResponseDto responseDto){
        List<Story> storys = storyRepository.findByMember(userDetails.getMember());
        List<StoryResponseDto> storyList = new ArrayList<>();
        for (Story story : storys) {
            storyList.add(
                    StoryResponseDto.builder()
                    .id(story.getId())
                    .video(story.getVideo())
                    .createdAt(story.getCreatedAt())
                    .modifiedAt(story.getModifiedAt())
                    .profileImage(story.getMember().getProfileImage())
                    .nickname(story.getMember().getNickname())
                    .build());
        }
        return storyList;
    }
    //스토리 작성
    public ResponseDto<?> createStory(UserDetailsImpl userDetails, MultipartFile multipartFile,StoryRequestDto requestDto) throws IOException  {
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        System.out.println(multipartFile);
        if (multipartFile.isEmpty()) {
            video = null;
        } else {
            if (!validVideoFile(multipartFile)) {
                throw new RuntimeException("올바른 파일 형식이 아닙니다.");
            }
            video = videoUrl(multipartFile);
        }

        Story story = Story.builder()
                .member(userDetails.getMember())
                .video(video)
                .build();
        storyRepository.save(story);


        return ResponseDto.success(
                StoryResponseDto.builder()
                .id(story.getId())
                .video(story.getVideo())
                .createdAt(story.getCreatedAt())
                .modifiedAt(story.getModifiedAt())
                .profileImage(story.getMember().getProfileImage())
                .nickname(story.getMember().getNickname())
                .build());
    }

    public String videoUrl(MultipartFile multipartFile) throws IOException{
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename(); // 파일 이름 중복되지 않게 랜덤한 값으로 업로드

        ObjectMetadata objMeta = new ObjectMetadata(); // ObjectMetadata를 통해 파일 사이즈를 ContentLength로 S3에 알려줌
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta); // S3 api 메소드 인 putObject를 이용해 파일 stream을 열어서 s3에 파일 업로드

        return amazonS3.getUrl(bucket, s3FileName).toString(); // S3에 업로드 된 사진 url 가져오기
    }

    public static boolean validVideoFile(MultipartFile multipartFile) {
        try {
            // 업로드를 허용하는 파일 타입
            List<String> ValidTypeList = Arrays.asList("video/quicktime","video/mp4", "video/ogg", "video/mpeg4-generic","video/webm","mp4","avi" ,"mpeg","ogv","webm","3gp","3g2");
            //,"avi" ,"mpeg","ogv","webm","3gp","3g2","image/jpeg", "image/pjpeg", "image/png", "image/gif", "image/jpg",

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

