package com.zelu.miprogram.emus;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangqiang
 * @Date: 2021/8/5 10:33
 */
@Getter
public enum SubjectEmus {
        //题目类型
        Subject_Single_Choice("单选题",1),
        Subject_Multiple_Choice("多选题",2),
        Subject_Fill_In_Blanks("填空题",3),
        Subject_Finshed_Answer("已答",4),
        Subject_NotFinshed_Answer("未答",5),
    ;

        Integer code;
        String msg;


        SubjectEmus(String msg, Integer code) {
            this.code = code;
            this.msg = msg;
        }

        public static Integer getEnumType(String msg) {
            SubjectEmus[] alarmGrades = SubjectEmus.values();
            for (SubjectEmus alarmGrade : alarmGrades) {
                if (StringUtils.equals(alarmGrade.getMsg(),msg)) {
                    return alarmGrade.getCode();
                }
            }
            return null;
        }
        //根据code取msg
        public static String getEnumType(Integer code) {
            SubjectEmus[] alarmGrades = SubjectEmus.values();
            for (SubjectEmus alarmGrade : alarmGrades) {
                if (alarmGrade.getCode()==code) {
                    return alarmGrade.getMsg();
                }
            }
            return null;
        }
}
