package com.zelu.miprogram.emus;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangqiang
 * @Date: 2021/8/5 10:33
 */
@Getter
public enum ManagerUserEmus {
        //试卷的类型
        //随机试卷
        Super_Manager_User("超级管理员",1),
        //顺序试卷
        Buessiness_Manager_User("企业管理员",2),

        ;

        Integer code;
        String msg;


    ManagerUserEmus(String msg, Integer code) {
            this.code = code;
            this.msg = msg;
        }

        public static Integer getEnumType(String msg) {
            ManagerUserEmus[] alarmGrades = ManagerUserEmus.values();
            for (ManagerUserEmus alarmGrade : alarmGrades) {
                if (StringUtils.equals(alarmGrade.getMsg(),msg)) {
                    return alarmGrade.getCode();
                }
            }
            return null;
        }
        //根据code取msg
        public static String getEnumType(Integer code) {
            ManagerUserEmus[] alarmGrades = ManagerUserEmus.values();
            for (ManagerUserEmus alarmGrade : alarmGrades) {
                System.out.println("alarmGrade"+alarmGrade);
                if (alarmGrade.getCode()==code) {
                    return alarmGrade.getMsg();
                }
            }
            return null;
        }

    public static void main(String[] args) {

        System.out.println(ManagerUserEmus.getEnumType(1));
    }
}
