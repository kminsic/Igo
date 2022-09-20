package com.wak.igo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private Error error;

    public static <T> ResponseDto<T> success(T data){
        return new ResponseDto<>(true, data, null );
        //응답이 성공적으로 되면 success true 메세지와, 해당 데이터, 에러 없음 표현
    }
    public static <T> ResponseDto<T> fail(String code, String message){
        return new ResponseDto<>(false, null, new Error(code, message));
        //응답 실패시 false 메세지와 데이터(실패 했으니) null 해당 에러 출력
    }

    @Getter
    @AllArgsConstructor
    static class Error{
        private String code;
        private String message;
    } //에러를 출력하기 위해 class 설정
}