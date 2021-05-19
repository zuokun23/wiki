package com.carlos.wiki.service;

import com.carlos.wiki.domain.Ebook;
import com.carlos.wiki.domain.EbookExample;
import com.carlos.wiki.mapper.EbookMapper;
import com.carlos.wiki.req.EbookQueryReq;
import com.carlos.wiki.req.EbookSaveReq;
import com.carlos.wiki.resp.EbookQueryResp;
import com.carlos.wiki.resp.PageResp;
import com.carlos.wiki.util.CopyUtil;
import com.carlos.wiki.util.SnowFlake;
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

    //@Resource是jdk自带的注解，@Autowired是Spring自带
    @Resource
    private SnowFlake snowFlake;

    /**
     * 返回列表
     * @param req
     * @return
     */
    public PageResp<EbookQueryResp> list(EbookQueryReq req){


        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数: {}", pageInfo.getPages());

        //持久层返回List<Ebook>需要转换成List<EbookResp>再返回controller
        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        //pageResp
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 保存
     * @param req
     */
    public void save(EbookSaveReq req){
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            long id = snowFlake.nextId();
            ebook.setId(id);
            ebookMapper.insert(ebook);
        }else{
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    /**
     * 删除
     * @param id
     */
    public void delete(Long id){
        ebookMapper.deleteByPrimaryKey(id);
    }

}
