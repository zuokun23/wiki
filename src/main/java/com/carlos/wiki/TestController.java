package com.carlos.wiki;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/hello")
    public String Hello(){
        return "hello world";
    }
}
