package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author wangqiang
 * @Date: 2021/8/5 09:39
 */
@Data
@ApiModel("提交试题答案参数")
public class MiniSubjectParam {
    //单个更新题目所用
    private String subjectNo;
    //单个添加题目所用
    private String rightAnswer;
    //单个添加题目所用
    private String subjectChoice;
}
