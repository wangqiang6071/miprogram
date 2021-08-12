package com.zelu.miprogram.domain.Params;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.util.Date;

/**
 * @author wangqiang
 * @Date: 2021/8/5 10:46
 */
@Data
@ApiModel("试卷参数")
public class MiniPaperParam {

    private String pageNo;

    private String paperName;

    private Integer paperLevel;//随机 顺序

    private Integer paperType;//考试 练习

    /**试卷的有效期**/
    private Date startTime;

    /**试卷的有效期**/
    private Date endTime;

    /**试卷考试所用总时长 **/
    private Integer totalTimes;

    /**试卷考试所总分 **/
    private Integer totalSorce;

    /** 试卷范围 **/
    private int paperRange;//7，8


}
