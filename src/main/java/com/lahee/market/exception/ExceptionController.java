package com.lahee.market.exception;

import com.lahee.market.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(Status400Exception.class)
    public ResponseEntity<ResponseDto> handleIllegalState(Status400Exception e) {
        ResponseDto response = new ResponseDto();
        response.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Status404Exception.class)
    public ResponseEntity<ResponseDto> handleNotFound(Status404Exception e) {
        ResponseDto response = new ResponseDto();
        response.setMessage(e.getMessage());
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(Status403Exception.class)
    public ResponseEntity<ResponseDto> handleForbidden(WriterNameNotMatchException e) {
        ResponseDto response = new ResponseDto();
        response.setMessage(e.getMessage());
        return ResponseEntity.status(403).body(response);
    }

    //처리되지 못한 예외 사항을 체크하기 위해서 테스트 용도
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> handleOtherError(RuntimeException e) {
        ResponseDto response = new ResponseDto();
        response.setMessage(String.format("예외 처리 하지 않음 : %s ", e.getMessage()));
        return ResponseEntity.status(400).body(response);
    }
}
