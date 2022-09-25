//package com.wak.igo.exception;
//
//import lombok.Getter;
//import org.springframework.http.HttpStatus;
//
//@Getter
//public enum ErrorCode {
//
//    // 401 Unauthorized.
//    UNAUTHORIZED_LOGIN(HttpStatus.UNAUTHORIZED, "401_2", "로그인이 필요합니다.");
//
//    private final HttpStatus httpStatus;
//    private final String errorCode;
//    private final String errorMessage;
//
//    ErrorCode(HttpStatus httpStatus, String errorCode, String errorMessage) {
//        this.httpStatus = httpStatus;
//        this.errorCode = errorCode;
//        this.errorMessage = errorMessage;
//    }
//}