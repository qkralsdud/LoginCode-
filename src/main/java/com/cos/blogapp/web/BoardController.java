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
<<<<<<< HEAD
import com.cos.blogapp.service.BoardService;
=======
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.service.BoardService;
import com.cos.blogapp.service.CommentService;
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
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
	private final  BoardService boardservice;
<<<<<<< HEAD
	private final HttpSession session;
	
	@PostMapping("/board/{boardId}/comment")
	public String commentSave(@PathVariable int boardId, CommentSaveReqDto dto) {

		// 1. DTO로 데이터 받기

		// 2. Comment객체 만들기(빈객체 생성)

		// 3. Comment 객체에 값 추가하기 id:X, content : DTO값, user:세션값, board:boardId로 findById
		User principal = (User) session.getAttribute("principal");

		boardservice.댓글등록(boardId, dto, principal);
		return "redirect:/board/"+boardId;
	}

=======
	private final CommentService commentService;
	private final HttpSession session;
	
	@PostMapping("/board/{boardId}/comment")
	public String commentSave(@PathVariable int boardId, CommentSaveReqDto dto) {		
		User principal = (User) session.getAttribute("principal");
		
		if(principal == null) {
			throw new MyNotFoundException("인증 실패");
		}
		
		commentService.댓글등록(boardId, dto, principal);
		return "redirect:/board/"+boardId;
	}
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
	
	@PutMapping("/board/{id}")
	public @ResponseBody CMRespDto<String> update(@PathVariable int id, @Valid @RequestBody BoardSaveReqDto dto, BindingResult bindingResult) {
		
		//인증
		User principal = (User) session.getAttribute("principal");
		if(principal == null) {
			throw new MyAsyncNotFoundException("인증 실패");
		}
		
		//유효성
		if(bindingResult.hasErrors()) {			
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			//return Script.back(errorMap.toString());
			throw new MyAsyncNotFoundException(errorMap.toString());
		}
<<<<<<< HEAD
			
=======
		
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
		boardservice.게시글수정(id, principal, dto);
		
		return new CMRespDto<>(1, "업데이트 성공", null);
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String boardupdateForm(@PathVariable int id, Model model) {
<<<<<<< HEAD
		// 게시글 정보를 가지고 가야함
		
		model.addAttribute("boardEntity",  boardservice.게시글수정페이지이동(id));
=======
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
		
		model.addAttribute("boardEntity", boardservice.게시글수정페이지이동(id));
	
		return "board/updateForm";
	}
	
	
	@DeleteMapping("/board/{id}")
	public @ResponseBody CMRespDto<String> deleteById(@PathVariable int id) {
		
		// 인증이 된 사람만 함수 접근 가능!! (로그인 된 사람)
		User principal = (User) session.getAttribute("principal");
		if(principal == null) {
			throw new MyAsyncNotFoundException("인증이 안됨");
		}
<<<<<<< HEAD
		
		// 권한이 있는 사람만 함수 접근 가능(principal.id == {id})
=======
		 
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
		boardservice.게시글삭제(id, principal);
		return new CMRespDto<String>(1, "성공", null);
	}
	
	//쿼리스트링, 패스var -> 디비 where에 걸리는 친구들
	//1. 컨트롤러 선정 2. Http Method 선정 3. 받을 데이터 있는디(body, 쿼리스트링, 패스var)
	// 4. 디비에 접근을 해야되면 Model 접근하기 or Model에 접근할 필요가 없다.
	@GetMapping("/board/{id}")
	public String detail(@PathVariable int id, Model model) {
<<<<<<< HEAD
		// select * from board where id = :id
		
		//1. orElse board값을 리턴 없을때 ()안 값을 리턴
//		Board boardEntity = boardRepository.findById(id)
//				.orElse(null);
		
		// 2. orElseThrow
		
		model.addAttribute("boardEntity", boardservice.게시글상세보기(id));
		return "board/detail";
=======
		// Board 객체에 존재하는것 (Board O, User O,  List<Comment> X)
		model.addAttribute("boardEntity", boardservice.게시글상세보기(id));
		return "board/detail"; // viewResolver
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
	}
	
	@PostMapping("/board")
	public @ResponseBody String save(@Valid BoardSaveReqDto dto, BindingResult bindingResult) {
		
		// 공통 로직 시작---------------------------
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
		// 공통 로직 끝---------------------------
		
		//핵심 로직시작---------------------------
		boardservice.게시글등록(dto, principal);
		//핵심 로직끝---------------------------
		
<<<<<<< HEAD
		boardservice.게시글등록(dto, principal);
=======
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
		return Script.href("/", "글쓰기 완료");
	}
	
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	
	@GetMapping( "/board")
<<<<<<< HEAD
	public String home(Model model, int page) {//Model == request.setAttribute	
		// 영속화된 오브젝트

		model.addAttribute("boardsEntity", boardservice.게시글목록보기(page));
		//System.out.println(boardsEntity.get(0).getUser().getUsername());
=======
	public String home(Model model, int page) {//Model == request.setAttribute

		model.addAttribute("boardsEntity", boardservice.게시글목록보기(page));
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
		return "board/list";
	}
}



















