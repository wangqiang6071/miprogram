package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("试卷与用户的关系")
@TableName("mini_paper_user")
public class MiniPaperUser extends Model<MiniPaperUser> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String paperNo;

    private String userNo;

    private String detailsUuid;//关联uuid

    private String level;//考试水平 best(优秀) good(及格) not good(不及格)

    private Integer socre;

    private Integer testTime;

    private int paperLevel;//试卷的类型 1随机 2顺序

    private Date createTime;
}