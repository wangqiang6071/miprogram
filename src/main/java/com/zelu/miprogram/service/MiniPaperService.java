package com.zelu.miprogram.service;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniPaper;
import com.zelu.miprogram.domain.Params.MiniPaperPageParam;
import com.zelu.miprogram.domain.Params.MiniPaperParam;
import com.zelu.miprogram.domain.Params.MiniPaperUpDownParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MiniPaperService extends IService<MiniPaper> {
    //======================后台接口======================
    public ServerResponse<String> Create_Page(@RequestBody MiniPaperParam param);

    ServerResponse<String> Delete_Page(String pageNo);

    public ServerResponse<String> Betch_Up_Down_Page(@RequestBody MiniPaperUpDownParam param);

    ServerResponse<String> Update_Page(MiniPaperParam param);

    ServerResponse<IPage<MiniPaper>> List_Page(MiniPaperPageParam param);
}