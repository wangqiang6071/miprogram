package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/8/6 16:47
 */
@Data
@ApiModel("用户分页和新建参数")
public class MiniUserParam {

    private String userNo;

    private String nickName;

    private Integer status;//账号的状态 1可用 2禁用

    private String phone;

    private String openId;

    private Integer sex;

    private String city;

    private String province;

    private String headimgUrl;

    private Integer integral;

    private Integer type;

    private Integer pageType=1;//1 列表 2导出

    private Integer pageIndex=1;

    private Integer pageSize=10;
}
