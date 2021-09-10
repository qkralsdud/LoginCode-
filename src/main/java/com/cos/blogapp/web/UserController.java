package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.domain.user.UserRepository;
import com.cos.blogapp.util.MyAlgorithm;
import com.cos.blogapp.util.SHA256;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.JoinReqtDto;
import com.cos.blogapp.web.dto.LoginReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {	
	private final UserRepository userRepository;	
	private final HttpSession session;
	
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
		
		String encPassword = SHA256.encrypt(dto.getPassword(), MyAlgorithm.SHA256);
		
		User userEntity = userRepository.mLogin(dto.getUsername(), encPassword);
		
		if(userEntity == null) {
			return Script.back("아이디 혹은 비밀번호를 잘못 입력하였습니다.");
		} else {
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
		
		String encPassword = SHA256.encrypt(dto.getPassword(), MyAlgorithm.SHA256);
		
		dto.setPassword(encPassword);
		userRepository.save(dto.toEntity());		
		return Script.href("/loginForm"); // 리다이렉션(300)
	}
		
}








