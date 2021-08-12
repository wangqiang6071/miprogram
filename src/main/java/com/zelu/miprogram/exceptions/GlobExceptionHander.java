package com.zelu.miprogram.exceptions;

import com.zelu.miprogram.common.ResponseCode;
import com.zelu.miprogram.common.ServerResponse;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

@ControllerAdvice
public class GlobExceptionHander {
	//请求方式不对1
	@ResponseBody
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ServerResponse<String> handle1(HttpRequestMethodNotSupportedException e) {
		return ServerResponse.createByError(ResponseCode.ERROR, "HTTP请求方式不对");
	}

	//系统sql语法错误2
	@ResponseBody
	@ExceptionHandler(SQLSyntaxErrorException.class)
	public ServerResponse<String> handle2(SQLSyntaxErrorException e) {
		return ServerResponse.createByError(ResponseCode.ERROR, e.getMessage());
	}

	//系统sql语法错误3
	@ResponseBody
	@ExceptionHandler(SQLException.class)
	public ServerResponse<String> handle3(SQLException e) {
		return ServerResponse.createByError(ResponseCode.ERROR, e.getMessage());
	}
	// sql字段重复

	// 字符串不合法
	@ResponseBody
	@ExceptionHandler(StringException.class)
	public ServerResponse<String> handle5(StringException e) {
		return ServerResponse.createByError(ResponseCode.ERROR, e.getMessage());
	}


	//数据库插入数据外键约束
	@ResponseBody
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ServerResponse<String> handle7(SQLIntegrityConstraintViolationException e) {
		return ServerResponse.createByError(ResponseCode.ERROR, e.getMessage());
	}


	//数据库插入数据外键约束
	@ResponseBody
	@ExceptionHandler(UnknownAccountException.class)
	public ServerResponse<String> handle8(UnknownAccountException e) {
		return ServerResponse.createByError(ResponseCode.ERROR, e.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(IncorrectCredentialsException.class)
	public ServerResponse<String> handle9(IncorrectCredentialsException e) {
		return ServerResponse.createByError(ResponseCode.ERROR, e.getMessage());
	}

	@ResponseBody
	@ExceptionHandler(ClassCastException.class)
	public ServerResponse<String> handle9(ClassCastException e) {
		return ServerResponse.createByError(ResponseCode.ERROR, e.getMessage());
	}

}