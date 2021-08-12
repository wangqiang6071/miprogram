package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/8/11 14:49
 */
@Data
@ApiModel("角色查询对应的管理员用户信息参数")
public class MiniManageUserInforParam {

    private String managerNo;

    private String userName;

    private String account;

    private String businessName;

    private int type;//1超级管理员 2企业管理员

    private String phone;

    private int status;//账号状态:1正常 2禁用

    private Integer sex;

    private String headimgUrl;
}
