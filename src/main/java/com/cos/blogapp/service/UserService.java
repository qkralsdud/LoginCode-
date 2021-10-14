package com.cos.blogapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.domain.user.UserRepository;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.util.MyAlgorithm;
import com.cos.blogapp.util.SHA;
import com.cos.blogapp.web.dto.JoinReqtDto;
import com.cos.blogapp.web.dto.LoginReqDto;
import com.cos.blogapp.web.dto.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
<<<<<<< HEAD

=======
	
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
	// 이건 하나의 서비스인가?(principal 값변경, update하고, 세션값 변경(X))	
	@Transactional(rollbackFor = MyAsyncNotFoundException.class)
	public void 회원수정(User principal, UserUpdateDto dto) {
		User userEntity = userRepository.findById(principal.getId())
				.orElseThrow(() -> new MyAsyncNotFoundException("회원정보를 찾을 수 없습니다"));
<<<<<<< HEAD

		userEntity.setEmail(dto.getEmail());			
	}//더티체킹

	public User 로그인(LoginReqDto dto) {
		return  userRepository.mLogin(dto.getUsername(),SHA.encrypt(dto.getPassword(), MyAlgorithm.SHA256));
	}

	@Transactional(rollbackFor = MyNotFoundException.class)
	public void 회원가입(JoinReqtDto dto) {

=======
		
		userEntity.setEmail(dto.getEmail());			
	}//더티체킹
		
	public User 로그인(LoginReqDto dto) {
		return  userRepository.mLogin(dto.getUsername(),SHA.encrypt(dto.getPassword(), MyAlgorithm.SHA256));
	}
	
	@Transactional(rollbackFor = MyNotFoundException.class)
	public void 회원가입(JoinReqtDto dto) {
		
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
		String encPassword = SHA.encrypt(dto.getPassword(), MyAlgorithm.SHA256);		
		dto.setPassword(encPassword);
		userRepository.save(dto.toEntity());		
	}
<<<<<<< HEAD
}
=======
}
































>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
