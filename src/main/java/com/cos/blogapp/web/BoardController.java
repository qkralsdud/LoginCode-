package com.cos.blogapp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.service.BoardService;
import com.cos.blogapp.service.CommentService;
import com.cos.blogapp.util.Script;
import com.cos.blogapp.web.dto.BoardSaveReqDto;
import com.cos.blogapp.web.dto.CMRespDto;
import com.cos.blogapp.web.dto.CommentSaveReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 붙은 변수의 생성자를 만들어준다
@Controller //컴포넌트(스프링) IoC
public class BoardController {
		
	//DI
	// final을 붙으면 무조건 초기화를 해야함
	private final CommentService commentService;
	private final BoardService boardService;
	private final HttpSession session;
	
	@PostMapping("/api/board/{boardId}/comment")
	public String commentSave(@PathVariable int boardId, CommentSaveReqDto dto) {		
		User principal = (User) session.getAttribute("principal");
				
		commentService.댓글등록(boardId, dto, principal);
		return "redirect:/board/"+boardId;
	}
	
	@PutMapping("/api/board/{id}")
	public @ResponseBody CMRespDto<String> update(@PathVariable int id, @Valid @RequestBody BoardSaveReqDto dto, BindingResult bindingResult) {
				
		//유효성
		if(bindingResult.hasErrors()) {			
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			//return Script.back(errorMap.toString());
			throw new MyAsyncNotFoundException(errorMap.toString());
		}
		
		//인증
		User principal = (User) session.getAttribute("principal");
		
		boardService.게시글수정(id, principal, dto);		
		return new CMRespDto<>(1, "업데이트 성공", null);
	}
	
	@GetMapping("/api/board/{id}/updateForm")
	public String boardupdateForm(@PathVariable int id, Model model) {

		// 게시글 정보를 가지고 가야함		
		model.addAttribute("boardEntity",  boardService.게시글수정페이지이동(id));	
		return "board/updateForm";
	}
	
	
	@DeleteMapping("/api/board/{id}")
	public @ResponseBody CMRespDto<String> deleteById(@PathVariable int id) {
		
		// 인증이 된 사람만 함수 접근 가능!! (로그인 된 사람)
		User principal = (User) session.getAttribute("principal");
		
		boardService.게시글삭제(id, principal);
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
		
		// Board 객체에 존재하는것 (Board O, User O,  List<Comment> X)
		model.addAttribute("boardEntity", boardService.게시글상세보기(id));
		return "board/detail"; // viewResolver
	}
	
	@PostMapping("/api/board")
	public @ResponseBody String save(@Valid BoardSaveReqDto dto, BindingResult bindingResult) {
		
		// 공통 로직 시작---------------------------
		User principal = (User) session.getAttribute("principal");
		
		
		if(bindingResult.hasErrors()) {			
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return Script.back(errorMap.toString());
		}
		// 공통 로직 끝---------------------------
		
		//핵심 로직시작---------------------------
		boardService.게시글등록(dto, principal);
		//핵심 로직끝---------------------------
		
		return Script.href("/", "글쓰기 완료");
	}
	
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	
	@GetMapping( "/board")
	public String home(Model model, int page) {//Model == request.setAttribute	
		// 영속화된 오브젝트

		model.addAttribute("boardsEntity", boardService.게시글목록보기(page));
		//System.out.println(boardsEntity.get(0).getUser().getUsername());
		return "board/list";
	}
}



















