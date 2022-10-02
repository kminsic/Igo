package com.wak.igo.controller;

import com.wak.igo.dto.response.ResponseDto;
import com.wak.igo.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    //신고하기
    @PostMapping("/api/report/{id}")
    public ResponseDto<?> insertReport(@PathVariable Long id, HttpServletRequest request) {
        return reportService.insertReport(id, request);
    }
}



