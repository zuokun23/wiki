package com.carlos.wiki.controller;

import com.carlos.wiki.req.EbookQueryReq;
import com.carlos.wiki.req.EbookSaveReq;
import com.carlos.wiki.resp.CommonResp;
import com.carlos.wiki.resp.EbookQueryResp;
import com.carlos.wiki.resp.PageResp;
import com.carlos.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq req){
        CommonResp<PageResp> resp = new CommonResp<>();
        PageResp<EbookQueryResp> pageResp = ebookService.list(req);
        resp.setContent(pageResp);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp all(@Valid EbookQueryReq req){
        CommonResp<PageResp> resp = new CommonResp<>();
        PageResp<EbookQueryResp> pageResp = ebookService.list(req);
        resp.setContent(pageResp);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req){
        CommonResp resp = new CommonResp();
        ebookService.save(req);
        return resp;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp resp = new CommonResp();
        ebookService.delete(id);
        return resp;
    }

}
