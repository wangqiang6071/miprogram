package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel("试卷与题目的关系")
@TableName("mini_paper_subject")
public class MiniPaperSubject extends Model<MiniPaperSubject> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String subjectNo;

    private String paperNo;

    private Date createTime;
}