package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @author wangqiang
 * @Date: 2021/8/6 15:12
 */
@Data
@ApiModel("企业分页和新建参数")
public class MiniBussiesNameParam {

    private String bussiesNo;

    private String bussiesName;

    private String contentUser;

    private String phone;

    /**生效日期**/
    private Date startTime;
    /**失效日期**/
    private Date endTime;

    private Integer pageIndex=1;

    private Integer pageSize=10;
}
