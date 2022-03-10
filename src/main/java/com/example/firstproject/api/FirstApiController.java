package com.example.firstproject.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController     //RestAPI용 컨트롤러! Entity 타입이 아니라 JSON을 반환
public class FirstApiController {

    @GetMapping("/api/hello")
    public String hello() {
        return "hello world!";
    }
}
