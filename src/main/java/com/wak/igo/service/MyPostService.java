package com.wak.igo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.wak.igo.domain.Mypost;
import com.wak.igo.domain.UserDetailsImpl;
import com.wak.igo.dto.request.MyPostRequestDto;
import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.repository.MyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MyPostService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final MyPostRepository myPostRepository;

    public ResponseDto<?> createSchedule(UserDetailsImpl userDetails, MultipartFile multipartFile, MyPostRequestDto requestDto) throws  IOException{
        if (null == userDetails.getAuthorities()) {
            ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }
        Mypost mypost = Mypost.builder()
                .title(requestDto.getTitle())
                .money(requestDto.getMoney())
                .member(userDetails.getMember())
                .time(requestDto.getTime())
                .imgUrl(imageUrl(multipartFile))
                .content(requestDto.getContent())
                .done(0)
                .build();
        myPostRepository.save(mypost);
        return ResponseDto.success("나의 일정 작성 완료");
    }

    public String imageUrl(MultipartFile multipartFile) throws IOException{
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename(); // 파일 이름 중복되지 않게 랜덤한 값으로 업로드

        ObjectMetadata objMeta = new ObjectMetadata(); // ObjectMetadata를 통해 파일 사이즈를 ContentLength로 S3에 알려준다
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta); // S3 api 메소드 인 putObject를 이용해 파일 stream을 열어서 s3에 파일 업로드

        return amazonS3.getUrl(bucket, s3FileName).toString(); // S3에 업로드 된 사진 url을 가져온다
    }
}
