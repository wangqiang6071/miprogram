package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel("角色与权限的关系")
@TableName("mini_role_permission")
public class MiniRolePermission extends Model<MiniRolePermission> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String roleNo;

    private String permissionNo;

    private Date createTime;

    private Date updateTime;
}