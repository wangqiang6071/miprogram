package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/8/5 09:39
 */
@Data
@ApiModel("试题分页搜索参数")
public class MiniSubjectPageParam {

    //分页所用
    private String subjectName;

    private int subjectType;

    private Integer pageType=1;//1 列表 2导出

    private Integer pageIndex=1;

    private Integer pageSize=10;


    //单个更新题目所用
    private String subjectNo;
    //单个添加题目所用
    private String rightAnswer;
    //单个添加题目所用
    private String subjectChoice;
}
