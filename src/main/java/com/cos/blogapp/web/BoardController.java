package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;
import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.BoardSaveReqDto;
import com.cos.blogapp.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 붙은 변수의 생성자를 만들어준다
@Controller //컴포넌트(스프링) IoC
public class BoardController {
		
	//DI
	// final을 붙으면 무조건 초기화를 해야함
	private final BoardRepository boardRepository;
	private final HttpSession session;
	
	@DeleteMapping("/board/{id}")
	public @ResponseBody CMRespDto<String> deleteById(@PathVariable int id) {
		
		// 인증이 된 사람만 함수 접근 가능!! (로그인 된 사람)
		User principal = (User) session.getAttribute("principal");
		if(principal == null) {
			throw new MyAsyncNotFoundException("인증이 안됨");
		}
		
		// 권한이 있는 사람만 함수 접근 가능(principal.id == {id})
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow( () ->  new MyAsyncNotFoundException(id + "를 찾을 수 없습니다") );
		if(principal.getId() != boardEntity.getUser().getId()) {
			throw new MyAsyncNotFoundException("해당 권한없음");
		}
		
		try {
			boardRepository.deleteById(id); // 오류발생(id가 없으면)			
		} catch (Exception e) {
			throw new MyAsyncNotFoundException(id+"를 찾을 수 없어서 삭제할 수 없어요.");
		}
		
		return new CMRespDto<String>(1, "성공", null);
	}
	
	//쿼리스트링, 패스var -> 디비 where에 걸리는 친구들
	//1. 컨트롤러 선정 2. Http Method 선정 3. 받을 데이터 있는디(body, 쿼리스트링, 패스var)
	// 4. 디비에 접근을 해야되면 Model 접근하기 or Model에 접근할 필요가 없다.
	@GetMapping("/board/{id}")
	public String detail(@PathVariable int id, Model model) {
		// select * from board where id = :id
		
		//1. orElse board값을 리턴 없을때 ()안 값을 리턴
//		Board boardEntity = boardRepository.findById(id)
//				.orElse(null);
		
		// 2. orElseThrow
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow( () ->  new MyNotFoundException(id + "를 찾을 수 없습니다") );			
		
		model.addAttribute("boardEntity", boardEntity);
		return "board/detail";
	}
	
	@PostMapping("/board")
	public @ResponseBody String save(@Valid BoardSaveReqDto dto, BindingResult bindingResult) {
		
		User principal = (User) session.getAttribute("principal");
		
		//인증체크
		if(principal == null) {// 로그인 안됨
			return Script.href("/loginForm", "잘못된 접근입니다.");
		}
		
		if(bindingResult.hasErrors()) {			
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}
		
		boardRepository.save(dto.toEntity(principal));		
		return Script.href("/", "글쓰기 완료");
	}
	
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	
	@GetMapping( "/board")
	public String home(Model model, int page) {//Model == request.setAttribute
		
		PageRequest pageRequest = PageRequest.of(page, 3, 
				Sort.by(Sort.Direction.DESC, "id"));
		
		// 영속화된 오브젝트
		Page<Board> boardsEntity = 
				boardRepository.findAll(pageRequest);
		model.addAttribute("boardsEntity", boardsEntity);
		//System.out.println(boardsEntity.get(0).getUser().getUsername());
		return "board/list";
	}
}



















