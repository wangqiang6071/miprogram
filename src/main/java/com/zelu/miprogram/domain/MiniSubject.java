package com.zelu.miprogram.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zelu.miprogram.excelmethod.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel("试题")
@TableName("mini_subject")
public class MiniSubject extends Model<MiniSubject> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Excel(name = "题目编号", cellType = Excel.ColumnType.STRING)
    private String subjectNo;

    @Excel(name = "企业编号", cellType = Excel.ColumnType.STRING)
    private String bussinesNo;

    @Excel(name = "题目内容", cellType = Excel.ColumnType.STRING)
    private String subjectName;

    @Excel(name = "题目类型", cellType = Excel.ColumnType.STRING)
    private int subjectType;

    @Excel(name = "题目正确答案", cellType = Excel.ColumnType.STRING)
    private String rightAnswer;

    @Excel(name = "题目备选答案", cellType = Excel.ColumnType.STRING)
    private String subjectChoice;

    private Date createTime;

    //表外数据
    @TableField(exist = false)
    private String userChoose;
}