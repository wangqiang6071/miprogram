package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
@Data
@ApiModel("用户答题记录")
@TableName("mini_subject_user")
public class MiniSubjectUser extends Model<MiniSubjectUser> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String subjectNo;

    private String userNo;

    private String subjectAnswer;

    private String userAnswer;

    private String detailsUuid;//关联uuid

    private Integer sorce;

    private int status;

    private Date createTime;
}