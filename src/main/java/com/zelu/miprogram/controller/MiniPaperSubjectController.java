package com.zelu.miprogram.controller;

import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.Params.MiniPaperSubjectParam;
import com.zelu.miprogram.service.MiniPaperSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("paper_subject")
public class MiniPaperSubjectController {
    //============================后台接口============================
    @Autowired
    private MiniPaperSubjectService paperSubjectService;

    //试题与试卷绑定
    public ServerResponse<String> CreateUpdatePageSubject(@RequestBody MiniPaperSubjectParam param){
        return paperSubjectService.Create_Update_PageSubject(param);
    }
    //查询试卷中的题目
    public ServerResponse<List<MiniSubject>>ListPagerSubject(String paperNo){
        return paperSubjectService.List_Pager_Subject(paperNo);
    }
}