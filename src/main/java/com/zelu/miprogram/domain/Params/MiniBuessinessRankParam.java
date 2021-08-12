package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/8/6 16:47
 */
@Data
@ApiModel("企业用户排名参数")
public class MiniBuessinessRankParam {

    private String userNo;

    private String nickName;

    private String bussinessName;

    private String headUrl;

    private Integer integral;
}
