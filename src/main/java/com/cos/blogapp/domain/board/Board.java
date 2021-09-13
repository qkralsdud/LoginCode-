package com.cos.blogapp.domain.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.cos.blogapp.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; // PK
	@Column(nullable = false, length = 50)
	private String title;
	
	// Longtext를 만들기
	@Lob
	private String content; 
	
	@JoinColumn(name = "userId")
	// 어차피 한건밖에 없어서
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
}














