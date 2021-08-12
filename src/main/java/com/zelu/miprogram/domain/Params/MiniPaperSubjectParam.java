package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.util.List;

/**
 * @author wangqiang
 * @Date: 2021/8/5 14:45
 */
@Data
@ApiModel("试卷与试题关系参数")
public class MiniPaperSubjectParam {
    //试卷编号
    private String pageNo;
    private List<String> subjectNo;
}
