package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zelu.miprogram.excelmethod.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel("小程序用户")
@TableName("mini_user")
public class MiniUser extends Model<MiniUser> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Excel(name = "用户编号", cellType = Excel.ColumnType.STRING)
    private String userNo;

    @Excel(name = "用户昵称", cellType = Excel.ColumnType.STRING)
    private String nickName;

    @Excel(name = "账号状态", cellType = Excel.ColumnType.NUMERIC)
    private int status;//账号的状态 1可用 2禁用

    @Excel(name = "用户手机号", cellType = Excel.ColumnType.STRING)
    private String phone;

    @Excel(name = "用户OpenId", cellType = Excel.ColumnType.STRING)
    private String openId;

    @Excel(name = "用户性别", cellType = Excel.ColumnType.NUMERIC)
    private int sex;

    @Excel(name = "用户城市", cellType = Excel.ColumnType.STRING)
    private String city;

    @Excel(name = "用户省份", cellType = Excel.ColumnType.STRING)
    private String province;

    @Excel(name = "用户头像", cellType = Excel.ColumnType.STRING)
    private String headimgUrl;

    @Excel(name = "用户积分", cellType = Excel.ColumnType.NUMERIC)
    private Integer integral;

    @Excel(name = "企业编号", cellType = Excel.ColumnType.STRING)
    private String bussinesNo;

    @Excel(name = "账号类型", cellType = Excel.ColumnType.NUMERIC)
    private int type;

    private Date createTime;
}