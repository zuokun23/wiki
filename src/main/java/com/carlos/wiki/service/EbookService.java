package com.carlos.wiki.service;

import com.carlos.wiki.domain.Ebook;
import com.carlos.wiki.domain.EbookExample;
import com.carlos.wiki.mapper.EbookMapper;
import com.carlos.wiki.req.EbookReq;
import com.carlos.wiki.resp.EbookResp;
import com.carlos.wiki.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    public List<EbookResp> list(EbookReq req){


        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(1, 3);
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数: {}", pageInfo.getPages());

        //持久层返回List<Ebook>需要转换成List<EbookResp>再返回controller
        List<EbookResp> respList = CopyUtil.copyList(ebookList, EbookResp.class);

        return respList;
    }
}
