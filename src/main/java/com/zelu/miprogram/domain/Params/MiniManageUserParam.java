package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/8/9 10:40
 */
@Data
@ApiModel("管理员账号分页和新建参数")
public class MiniManageUserParam {

    private String roleNo;
    private String managerNo;
    private String bussinesNo;
    private String userName;
    private int type;//1超级管理员 2企业管理员
    private String account;
    private String password;
    private String phone;
    private Integer status;//账号状态:1正常 2禁用
    private Integer sex;
    private String headimgUrl;

    private Integer pageIndex=1;

    private Integer pageSize=10;
}
