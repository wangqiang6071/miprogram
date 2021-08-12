package com.zelu.miprogram.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.Params.MiniSubjectPageParam;
import com.zelu.miprogram.service.MiniSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("subject")
public class MiniSubjectController {

    //=================后台接口================
    @Autowired
    private MiniSubjectService subjectService;

    //导入
    @PostMapping("import")
    public ServerResponse<String> importExcelSubject(MultipartFile file) throws Exception {
        return subjectService.ImportSubject(file);
    }
    //导出
    @ResponseBody
    @PostMapping("export")
    public ServerResponse<IPage<MiniSubject>>exportExcelSubject(@RequestBody MiniSubjectPageParam param){
        return subjectService.ExportSubject(param);
    }
    //单个增加
    @ResponseBody
    @PostMapping("add")
    public ServerResponse<String>AddSubject(@RequestBody MiniSubjectPageParam param){
        return subjectService.Add_Subject(param);
    }
    //单个修改
    @ResponseBody
    @PostMapping("update")
    public ServerResponse<String>UpdateSubject(@RequestBody MiniSubjectPageParam param){
        return subjectService.Update_Subject(param);
    }
    //单个删除
    @ResponseBody
    @PostMapping("delete")
    public ServerResponse<String>DeleteSubject(String subjectNo){
        return subjectService.Delete_Subject(subjectNo);
    }
}