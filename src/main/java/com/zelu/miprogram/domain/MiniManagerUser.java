package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @author wangqiang
 * @Date: 2021/8/9 09:55
 */
@Data
@ApiModel("后台管理员用户")
@TableName("mini_manager_user")
public class MiniManagerUser extends Model<MiniManagerUser> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String managerNo;
    private String bussinesNo;
    private String userName;
    private int type;//1超级管理员 2企业管理员
    private String account;
    private String password;
    private String phone;
    private int status;//账号状态:1正常 2禁用
    private Integer sex;
    private String headimgUrl;
    private Date createTime;
}
