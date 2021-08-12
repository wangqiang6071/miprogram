package com.zelu.miprogram.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniPaperSubject;
import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.Params.MiniPaperSubjectParam;

import java.util.List;


public interface MiniPaperSubjectService extends IService<MiniPaperSubject> {

    public ServerResponse<String> Create_Update_PageSubject(MiniPaperSubjectParam param);

    public ServerResponse<List<MiniSubject>> List_Pager_Subject(String paperNo);
}