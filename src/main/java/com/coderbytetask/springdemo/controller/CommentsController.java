package com.coderbytetask.springdemo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.coderbytetask.springdemo.model.Comment;




@RestController
@RequestMapping("/comments")
public class CommentsController {
	private static final String COMMENTS_PUBLIC_API_URL = "https://gorest.co.in/public/v2/comments";

	@Autowired
	RestTemplate restTemplate ;

	@GetMapping
	public List<Comment> getAllComments() {

		ResponseEntity<Comment[]> responseEntity = restTemplate.getForEntity(COMMENTS_PUBLIC_API_URL, Comment[].class);
		Comment[] commentArray = responseEntity.getBody();
		return Arrays.asList(commentArray);
	}
	
	
	@GetMapping("/{postId}")
	public List<Comment> getCommentsByPostId(@PathVariable String postId) {

		ResponseEntity<Comment[]> responseEntity = restTemplate.getForEntity(COMMENTS_PUBLIC_API_URL, Comment[].class);
		Comment[] postArray = responseEntity.getBody();
		List<Comment> postList = Arrays.asList(postArray);
		return postList.stream().filter(p -> p.getPost_id().equals(postId)).collect(Collectors.toList());

	}
}
