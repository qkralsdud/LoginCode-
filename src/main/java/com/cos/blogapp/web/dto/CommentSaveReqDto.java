package com.cos.blogapp.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSaveReqDto {
	@NotBlank
	@Size(min = 1, max = 255)
	private String content;
<<<<<<< HEAD

=======
	
>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
//	public Comment toEntity(User principal, Board boardEntity) {
//		Comment comment = new Comment();
//		comment.setContent(content);
//		comment.setUser(principal);
//		comment.setBoard(boardEntity);
//		return comment;
//	}
<<<<<<< HEAD
}
=======
}

















>>>>>>> 9156bd20945f4dda9bd3f60237d6b98e10b6cb21
