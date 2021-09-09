package com.cos.blogapp.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이 붙은 변수의 생성자를 만들어준다
@Controller //컴포넌트(스프링) IoC
public class BoardController {
	// final을 붙으면 무조건 초기화를 해야함
	private final BoardRepository boardRepository;
	
	@GetMapping({"/", "/board"})
	public String home(Model model) {//Model == request.setAttribute
		// 영속화된 오브젝트
		List<Board> boardsEntity = boardRepository.findAll();
		model.addAttribute("boardsEntity", boardsEntity);
		//System.out.println(boardsEntity.get(0).getUser().getUsername());
		return "home";
	}
}



















