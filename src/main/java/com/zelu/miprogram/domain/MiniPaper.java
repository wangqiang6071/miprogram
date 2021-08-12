package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
@Data
@ApiModel("试卷")
@TableName("mini_paper")
public class MiniPaper extends Model<MiniPaper> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String paperNo;

    private String paperName;

    private int paperLevel;//随机 顺序

    /**试卷考试所用总时长 **/
    private Integer totalTimes;
    /**试卷考试总分 **/
    private Integer totalSorce;

    /**试卷考试所有试题数量 **/
    private Integer totalSubjects;

    /**试卷考试每一题的分值 **/
    private Integer peerSorce;

    /**试卷是否上架 1 上架 2下架**/
    private int status;

    /** 试卷可见范围 **/
    private int paperRange;

    /** 试卷可见企业编号 **/
    private String paperBussno;

    private Date createTime;


}