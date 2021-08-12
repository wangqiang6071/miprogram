package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
@Data
@ApiModel("权限")
@TableName("mini_permission")
public class MiniPermission extends Model<MiniPermission> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String modelNo;

    private String permissionNo;

    private String permissionName;

    private int status;

    private Date createTime;

    private Date updateTime;
}