package com.cos.blogapp.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.CMRespDto;

// @ControllerAdvice 1. 익셉션 핸들링  2.@Controller역할까지 한다
@ControllerAdvice
public class GlobalExceptionHandler {
	
	// 일반 요청시
	@ExceptionHandler(value = MyNotFoundException.class)
	public @ResponseBody String error1(MyNotFoundException e) {
		System.out.println(e.getMessage());
		return Script.href("/", e.getMessage());
	}
	
	// fetch요청(데이터를 응답아야 할때)
	@ExceptionHandler(value = MyAsyncNotFoundException.class)
	public @ResponseBody CMRespDto<String> error2(MyAsyncNotFoundException e) {
		System.out.println(e.getMessage());
		return new CMRespDto<String> (-1, e.getMessage(), null);
	}
}
















