package com.zelu.miprogram.emus;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangqiang
 * @Date: 2021/8/5 10:33
 */
@Getter
public enum PaperEmus {
        //试卷的类型
        //随机试卷
        Random_Pager("随机试卷",1),
        //顺序试卷
        Order_Pager("顺序试卷",2),
        //试卷批量操作
        Pager_Up("批量上架",5),
        Pager_Down("批量下架",6),
        //试卷范围
        Pager_Range_Business("企业",7),
        Pager_Range_Tourist("游客",8),

        ;

        Integer code;
        String msg;


    PaperEmus(String msg,Integer code) {
            this.code = code;
            this.msg = msg;
        }

        public static Integer getEnumType(String msg) {
            PaperEmus[] alarmGrades = PaperEmus.values();
            for (PaperEmus alarmGrade : alarmGrades) {
                if (StringUtils.equals(alarmGrade.getMsg(),msg)) {
                    return alarmGrade.getCode();
                }
            }
            return null;
        }
        //根据code取msg
        public static String getEnumType(Integer code) {
            PaperEmus[] alarmGrades = PaperEmus.values();
            for (PaperEmus alarmGrade : alarmGrades) {
                if (alarmGrade.getCode()==code) {
                    return alarmGrade.getMsg();
                }
            }
            return null;
        }
}
