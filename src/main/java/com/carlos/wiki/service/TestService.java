package com.carlos.wiki.service;

import com.carlos.wiki.domain.Test;
import com.carlos.wiki.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {

    @Resource
    private TestMapper testMapper;

    public List<Test> list(){

        return testMapper.list();
    }
}
