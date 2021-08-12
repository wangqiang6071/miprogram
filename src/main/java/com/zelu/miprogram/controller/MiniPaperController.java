package com.zelu.miprogram.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniPaper;
import com.zelu.miprogram.domain.Params.MiniPaperPageParam;
import com.zelu.miprogram.domain.Params.MiniPaperParam;
import com.zelu.miprogram.domain.Params.MiniPaperUpDownParam;
import com.zelu.miprogram.service.MiniPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("paper")
public class MiniPaperController {
    //======================后台接口======================
    @Autowired
    private MiniPaperService paperService;

    //创建试卷
    @ResponseBody
    @PostMapping("create")
    public ServerResponse<String>CreatePage(@RequestBody MiniPaperParam param){
        return paperService.Create_Page(param);
    }

    //试卷批量操作(上架/下架)
    public ServerResponse<String> BetchUpDownPage(@RequestBody MiniPaperUpDownParam param){
        return paperService.Betch_Up_Down_Page(param);
    }

    //删除试卷
    @ResponseBody
    @GetMapping("delete")
    public ServerResponse<String>DeletePage(String pageNo){
        return paperService.Delete_Page(pageNo);
    }

    //修改
    @ResponseBody
    @PostMapping("update")
    public ServerResponse<String>UpdatePage(@RequestBody MiniPaperParam param){
        return paperService.Update_Page(param);
    }

    //列表
    @ResponseBody
    @PostMapping("list")
    public ServerResponse<IPage<MiniPaper>>ListPage(@RequestBody MiniPaperPageParam param){
        return paperService.List_Page(param);
    }
}