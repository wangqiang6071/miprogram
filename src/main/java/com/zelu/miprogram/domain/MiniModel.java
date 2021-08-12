package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("系统模块")
@TableName("mini_model")
public class MiniModel extends Model<MiniModel> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String modelNo;

    private String modelName;

    private Date createTime;


}