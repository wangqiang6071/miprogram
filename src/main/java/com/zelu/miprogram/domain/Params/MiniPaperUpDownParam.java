package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/8/5 10:46
 */
@Data
@ApiModel("试卷上架或者下架参数")
public class MiniPaperUpDownParam {

    List<String> pageNo;
    private Integer status;

}
