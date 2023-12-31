package com.blog.app.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Comment;
import com.blog.app.entities.Post;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.CommentDto;
import com.blog.app.payloads.PostDto;
import com.blog.app.repositories.CommentRepo;
import com.blog.app.repositories.PostRepo;
import com.blog.app.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "PostId", postId));

		Comment comment = this.dtoToComment(commentDto);
		comment.setPost(post);

		Comment savedComment = this.commentRepo.save(comment);
		CommentDto savedCommentDto = this.commentToDto(savedComment);

		return savedCommentDto;
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "CommentId", commentId));
		this.commentRepo.delete(comment);

	}

	private Comment dtoToComment(CommentDto commentDto) {
		return this.modelMapper.map(commentDto, Comment.class);
	}

	private CommentDto commentToDto(Comment comment) {
		return this.modelMapper.map(comment, CommentDto.class);
	}

}
