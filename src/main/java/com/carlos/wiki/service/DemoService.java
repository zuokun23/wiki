package com.carlos.wiki.service;

import com.carlos.wiki.domain.Demo;
import com.carlos.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DemoService {

    @Resource
    private DemoMapper demoMapper;

    public List<Demo> list(){

        return demoMapper.selectByExample(null);
    }
}
