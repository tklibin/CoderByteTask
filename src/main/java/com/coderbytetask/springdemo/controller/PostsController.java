package com.coderbytetask.springdemo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.coderbytetask.springdemo.model.Comment;
import com.coderbytetask.springdemo.model.Post;

@RestController
@RequestMapping("/posts")
public class PostsController {

	private static final String POSTS_PUBLIC_API_URL = "https://gorest.co.in/public/v2/posts";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	HttpSession session;

	@GetMapping
	public List<Post> getAllPosts() {
		ResponseEntity<Post[]> responseEntity = restTemplate.getForEntity(POSTS_PUBLIC_API_URL, Post[].class);
		Post[] postArray = responseEntity.getBody();
		return Arrays.asList(postArray);
	}

	@GetMapping("/{userId}")
	public List<Post> getPostsByUserId(@PathVariable String userId) {

		ResponseEntity<Post[]> responseEntity = restTemplate.getForEntity(POSTS_PUBLIC_API_URL, Post[].class);
		Post[] postArray = responseEntity.getBody();
		List<Post> postList = Arrays.asList(postArray);
		return postList.stream().filter(p -> p.getUser_id().equals(userId)).collect(Collectors.toList());
	}

	@GetMapping("/{postId}/comments")
	public List<Comment> getCommentsByPostId(@PathVariable String postId)
			throws RestClientException, URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth((String) session.getAttribute("token"));

		ResponseEntity<Comment[]> responseEntity = restTemplate.exchange(
				RequestEntity.get(new URI("http://localhost:8080/comments/" + postId)).headers(headers).build(),
				Comment[].class);

		Comment[] commentsArray = responseEntity.getBody();


		return Arrays.asList(commentsArray);

	}

}
