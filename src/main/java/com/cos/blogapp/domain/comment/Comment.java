package com.cos.blogapp.domain.comment;

<<<<<<< HEAD
=======
import javax.persistence.Column;
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.cos.blogapp.domain.board.Board;
import com.cos.blogapp.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

<<<<<<< HEAD
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Comment {
	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동증가
	private int id; // PK(자동증가 번호)
	
=======
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; // PK
	
	@Column(nullable = false)
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
	private String content;
	
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;
	
	@JoinColumn(name = "boardId")
	@ManyToOne
	private Board board;
<<<<<<< HEAD
	
=======
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
}














<<<<<<< HEAD










=======
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
