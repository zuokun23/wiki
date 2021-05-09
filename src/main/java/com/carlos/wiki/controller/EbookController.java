package com.carlos.wiki.controller;

import com.carlos.wiki.req.EbookReq;
import com.carlos.wiki.resp.CommonResp;
import com.carlos.wiki.resp.EbookResp;
import com.carlos.wiki.resp.PageResp;
import com.carlos.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookReq req){
        CommonResp<PageResp> resp = new CommonResp<>();
        PageResp<EbookResp> pageResp = ebookService.list(req);
        resp.setContent(pageResp);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp all(EbookReq req){
        CommonResp<PageResp> resp = new CommonResp<>();
        PageResp<EbookResp> pageResp = ebookService.list(req);
        resp.setContent(pageResp);
        return resp;
    }
}
