package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @author wangqiang
 * @Date: 2021/8/5 10:46
 */
@Data
@ApiModel("试卷分页参数")
public class MiniPaperPageParam {

    private String paperName;

    private int paperLevel;//随机 顺序

    private Integer pageIndex=1;

    private Integer pageSize=10;

}
