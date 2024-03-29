package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.service.UserService;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.CMRespDto;
import com.cos.blogapp.web.dto.JoinReqtDto;
import com.cos.blogapp.web.dto.LoginReqDto;
import com.cos.blogapp.web.dto.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {	
	private final UserService userService;
	private final HttpSession session;
	
	@PutMapping("/api/user/{id}")
	public @ResponseBody CMRespDto<String> update(@PathVariable int id, @Valid @RequestBody UserUpdateDto dto, BindingResult bindingResult) {
		
		// 유효성
		if(bindingResult.hasErrors()) {			
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			throw new MyAsyncNotFoundException(errorMap.toString());
		}
		
		// 인증
		User principal = (User) session.getAttribute("principal");

		// 권한
		if(principal.getId() != id) {
			throw new MyAsyncNotFoundException("회원정보를 수정할 권한이 없습니다");
		}
			
		userService.회원수정(principal, dto);
		
		// 핵심로직
		principal.setEmail(dto.getEmail());
		session.setAttribute("principal", principal); // 세션 값 변경

		// 세션 동기화해주는 부분
		return new CMRespDto<>(1, "셩공", null);
	}
	
	@GetMapping("/api/user/{id}")
	public String userInfo() {		
		// 기본은 userRepository.findByid(id) 디비에서 가져와야함.
		
		// 편법은 세션값을 가져올 수도 있다
				
		return "user/updateForm";
	}
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate(); // 세션 무효화
		return "redirect:/";  // 게시글 모록 화면에 데이터가 없음
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	@PostMapping("/login")
	public @ResponseBody String login(@Valid LoginReqDto dto, 
			BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return Script.back(errorMap.toString()); 
		} 
		
		User userEntity = userService.로그인(dto);
		
		if(userEntity == null) {
			return Script.back("아이디 혹은 비밀번호를 잘못 입력하였습니다.");
		} else {
			// 세션 날아가는 조건 : 1. session.invalidate(), 2. 브라우저를 닫으면 날아감
			session.setAttribute("principal", userEntity);
			return Script.href("/", "로그인성공");			
		}
	}
	
	@PostMapping("/join")
	public @ResponseBody String join(@Valid JoinReqtDto dto,
			BindingResult bindingResult) {// 유효성검사시 터진것을 담아준다
				
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}
		userService.회원가입(dto);
		return Script.href("/loginForm"); // 리다이렉션(300)
	}
		
}








