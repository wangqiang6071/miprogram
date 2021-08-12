package com.zelu.miprogram.emus;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangqiang
 * @Date: 2021/4/23 17:05
 */
@Getter
public enum userPermission {
        //可用
        allow("可用",1),
        //禁用
        forbidden("禁用",2),
        //企业用户
        bussiness_user("企业用户",3),
        //游客
        tourist_user("游客用户",4),
    ;

    Integer code;
    String msg;


    userPermission(String msg,Integer code) {
        this.code = code;
        this.msg = msg;
    }

    public static Integer getEnumType(String msg) {
        userPermission[] alarmGrades = userPermission.values();
        for (userPermission alarmGrade : alarmGrades) {
            if (StringUtils.equals(alarmGrade.getMsg(),msg)) {
                return alarmGrade.getCode();
            }
        }
        return null;
    }
    //根据code取msg
    public static String getEnumType(Integer code) {
        userPermission[] alarmGrades = userPermission.values();
        for (userPermission alarmGrade : alarmGrades) {
            if (alarmGrade.getCode()==code) {
                return alarmGrade.getMsg();
            }
        }
        return null;
    }
}
