package com.zelu.miprogram.domain.Params;

import com.zelu.miprogram.domain.MiniSubject;
import com.zelu.miprogram.domain.MiniUser;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/8/5 09:39
 */
@Data
@ApiModel("用户提交题目答案参数")
public class MiniSubjectListParam {
    private String paperNo;//试卷编号
    private Integer time=0;//考试所用时间
    private MiniUser user;
    List<MiniSubjectParam>subjects;
    //List<MiniSubject> subjects;
}
