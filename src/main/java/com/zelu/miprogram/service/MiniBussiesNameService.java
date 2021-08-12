package com.zelu.miprogram.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zelu.miprogram.common.ServerResponse;
import com.zelu.miprogram.domain.MiniBussiesName;
import com.zelu.miprogram.domain.MiniPaper;
import com.zelu.miprogram.domain.Params.MiniBussiesNameParam;

import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/8/6 14:08
 */
public interface MiniBussiesNameService extends IService<MiniBussiesName> {
    ServerResponse<String> Create_BussiesName(MiniBussiesNameParam param);

    ServerResponse<String> Update_BussiesName(MiniBussiesNameParam param);

    ServerResponse<String> Delete_BussiesName(String businessNo);

    ServerResponse<IPage<MiniBussiesName>> List_BussiesName(MiniBussiesNameParam param);

    ServerResponse<List<MiniBussiesNameParam>> List_All_BussiesName();
}
