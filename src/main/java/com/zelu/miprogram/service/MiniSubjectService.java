package com.zelu.miprogram.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.Params.MiniSubjectPageParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;


public interface MiniSubjectService extends IService<MiniSubject> {

    //后台接口
    public ServerResponse<String> ImportSubject(MultipartFile file) throws Exception;

    public ServerResponse<IPage<MiniSubject>>ExportSubject(@RequestBody MiniSubjectPageParam param);

    public ServerResponse<String> Add_Subject(MiniSubjectPageParam param);

    public ServerResponse<String> Update_Subject(MiniSubjectPageParam param);

    public ServerResponse<String> Delete_Subject(String subjectNo);
}