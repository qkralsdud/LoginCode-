package com.cos.blogapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.board.BoardRepository;
import com.cos.blogapp.domain.comment.Comment;
import com.cos.blogapp.domain.comment.CommentRepository;
import com.cos.blogapp.domain.user.User;
import com.cos.blogapp.handler.ex.MyAsyncNotFoundException;
import com.cos.blogapp.handler.ex.MyNotFoundException;
import com.cos.blogapp.web.dto.BoardSaveReqDto;
import com.cos.blogapp.web.dto.CommentSaveReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service 
public class BoardService {

	// 생성자 주입(DI)
	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;

	//트랜잭션 시작
	@Transactional(rollbackFor = MyNotFoundException.class)
	public void 댓글등록(int boardId, CommentSaveReqDto dto, User principal) {

		Board boardEntity = boardRepository.findById(boardId)
				.orElseThrow(() -> new MyNotFoundException("해당 게시글을 찾을 수 없습니다"));

		Comment comment = new Comment();
		comment.setContent(dto.getContent());
		comment.setUser(principal);
		comment.setBoard(boardEntity);

		commentRepository.save(comment);
	} // 트랜잭션 종료

	// 트랜잭션 시작
	@Transactional(rollbackFor = MyAsyncNotFoundException.class)
	public void 게시글수정(int id, User principal, BoardSaveReqDto dto) {

		Board boardEntity = boardRepository.findById(id)
				.orElseThrow( () ->  new MyAsyncNotFoundException("해당 게시글을 찾을 수 없습니다") );

		if(principal.getId() != boardEntity.getUser().getId()) {
			throw new MyAsyncNotFoundException("해당 권한없음");
		}

		if (principal.getId() != boardEntity.getUser().getId()) {
			throw new MyAsyncNotFoundException("해당 권한없음");
		}

		// 영속화된 데이터를 변경하면!!
		boardEntity.setTitle(dto.getTitle());
		boardEntity.setContent(dto.getContent());
	} // 트랜잭션 종료

	public Board 게시글수정페이지이동(int id) {
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow(()-> new MyNotFoundException(id + "변호의 게시글을 찾을 수 없습니다."));

		return boardEntity;		
	} // 트랜잭션 종료, 더티체킹

	@Transactional(rollbackFor = MyAsyncNotFoundException.class)
	public void 게시글삭제(int id, User principal) {
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow( () ->  new MyAsyncNotFoundException(id + "를 찾을 수 없습니다") );

		if(principal.getId() != boardEntity.getUser().getId()) {
			throw new MyAsyncNotFoundException("해당 권한없음");
		}
		if (principal.getId() != boardEntity.getUser().getId()) {
			throw new MyAsyncNotFoundException("해당 권한없음");
		}

		try {
			boardRepository.deleteById(id); // 오류발생(id가 없으면)			
		} catch (Exception e) {
			throw new MyAsyncNotFoundException(id+"를 찾을 수 없어서 삭제할 수 없어요.");
		}
	}

	public Board 게시글상세보기(int id) {
		Board boardEntity = boardRepository.findById(id)
				.orElseThrow( () ->  new MyNotFoundException(id + "를 찾을 수 없습니다") );			

		return boardEntity;		
	}

	@Transactional(rollbackFor = MyNotFoundException.class)
	public void 게시글등록(BoardSaveReqDto dto, User principal) {
		boardRepository.save(dto.toEntity(principal));		
	}


	public Page<Board> 게시글목록보기(int page) {
		PageRequest pageRequest = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "id"));
		Page<Board> boardsEntity = boardRepository.findAll(pageRequest);

		return boardsEntity;
	}


}


