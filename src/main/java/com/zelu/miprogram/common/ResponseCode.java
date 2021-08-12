package com.zelu.miprogram.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
	
	// 枚举方法
	ERROR(-1, "服务端异常"),

	SUCCESS(0, "成功"),
	
	ILLEGAL_ARGUMENT(-11, "ILLEGAL_ARGUMENT"),// 非法参数
	ILLEGAL_String(-2,"字符串长度异常"),


	;

	// 属性值
	private final int code;
	private final String msg;

	// 构造器
	ResponseCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	//根据枚举类的
	public static ResponseCode getEnumType(int code) {
		ResponseCode[] alarmGrades = ResponseCode.values();
        for (ResponseCode alarmGrade : alarmGrades) {
            if (alarmGrade.getCode()==code) {
                return alarmGrade;
            }
        }
        return null;
    }
}
