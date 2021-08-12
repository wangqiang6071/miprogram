package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
@Data
@ApiModel("管理员用户与角色的关系")
@TableName("mini_user_role")
public class MiniUserRole extends Model<MiniUserRole> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String managerNo;

    private String roleNo;

    private Date createTime;

    private Date updateTime;
}