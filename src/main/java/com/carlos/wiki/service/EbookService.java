package com.carlos.wiki.service;

import com.carlos.wiki.domain.Ebook;
import com.carlos.wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> list(){

        return ebookMapper.selectByExample(null);
    }
}
