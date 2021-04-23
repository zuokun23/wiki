package com.carlos.wiki.controller;

import com.carlos.wiki.domain.Test;
import com.carlos.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/hello")
    public String Hello(){
        return "hello world";
    }

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }
}
