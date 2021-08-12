package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel("角色")
@TableName("mini_role")
public class MiniRole extends Model<MiniRole> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String roleNo;

    private String roleName;

    private int status;

    private Date createTime;

    private Date updateTime;
}