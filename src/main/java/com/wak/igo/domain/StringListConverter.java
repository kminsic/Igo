package com.wak.igo.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

// AttricuteConverter 인터페이스를 이용하여 list <-> string 으로 변환
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ";";
    @Override
    public String convertToDatabaseColumn(List<String> stringList) { // 엔티티의 데이터를 데이터베이스 컬럼에 저장할 데이터로 변환
        return stringList != null ? String.join(SPLIT_CHAR, stringList) : ""; // List의 문자열들을 ";"를 구분자로 문자열로 반환
    }
    @Override
    public List<String> convertToEntityAttribute(String string) { // DB에서 조회한 컬럼 데이터를 엔티티의 데이터로 변환
        return string != null ? Arrays.asList(string.split(SPLIT_CHAR)) : emptyList(); // 문자열을 ";" 구분자로 분리해 리스트로 반환
    }
}