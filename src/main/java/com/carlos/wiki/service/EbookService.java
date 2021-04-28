package com.carlos.wiki.service;

import com.carlos.wiki.domain.Ebook;
import com.carlos.wiki.domain.EbookExample;
import com.carlos.wiki.mapper.EbookMapper;
import com.carlos.wiki.req.EbookReq;
import com.carlos.wiki.resp.EbookResp;
import com.carlos.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        //持久层返回List<Ebook>需要转换成List<EbookResp>再返回controller
        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);

        return respList;
    }
}
