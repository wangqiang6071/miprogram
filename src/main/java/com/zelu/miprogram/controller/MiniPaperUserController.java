package com.zelu.miprogram.controller;

import com.zelu.miprogram.common.ServerResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("paper_user")
public class MiniPaperUserController {
    //===========================小程序接口====================
    //
    public ServerResponse<String> CreatePageSubject(){
        return ServerResponse.createBySuccess("创建成功");
    }
    //删除试卷
    public ServerResponse<String>DeletePageSubject(){
        return ServerResponse.createBySuccess("创建成功");
    }
    //修改
    public ServerResponse<String>UpdatePageSubject(){
        return ServerResponse.createBySuccess("创建成功");
    }
    //列表
    public ServerResponse<String>ListPageSubject(){
        return ServerResponse.createBySuccess("创建成功");
    }
}