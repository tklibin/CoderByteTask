package com.coderbytetask.springdemo.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.coderbytetask.springdemo.model.AuthenticationRequest;
import com.coderbytetask.springdemo.model.AuthenticationResponse;
import com.coderbytetask.springdemo.model.Post;
import com.coderbytetask.springdemo.model.User;
import com.coderbytetask.springdemo.service.MyUserDetailsService;
import com.coderbytetask.springdemo.util.JwtUtil;

@RestController
@RequestMapping("/users")
public class UsersController {

	private static final String USERS_PUBLIC_API_URL = "https://gorest.co.in/public/v2/users";

	@Autowired
	RestTemplate restTemplate ;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	HttpSession session;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@GetMapping
	public List<User> getAllUsers() {
		ResponseEntity<User[]> responseEntity = restTemplate.getForEntity(USERS_PUBLIC_API_URL, User[].class);
		User[] userArray = responseEntity.getBody();
		return Arrays.asList(userArray);
	}

	@GetMapping("/{userId}")
	public User getUserById(@PathVariable String userId) {

		ResponseEntity<User[]> responseEntity = restTemplate.getForEntity(USERS_PUBLIC_API_URL, User[].class);
		User[] userArray = responseEntity.getBody();
		List<User> userList = Arrays.asList(userArray);
		return userList.stream().filter(p -> p.getId().equals(userId)).findFirst().get();

	}
	
	@GetMapping("/{userId}/posts")
	public List<Post> getPostsByUserId(@PathVariable String userId) throws RestClientException, URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth((String)session.getAttribute("token"));
		//ResponseEntity<Post[]> responseEntity = restTemplate.getForEntity("http://localhost:8080/posts/"+userId, Post[].class);
		
		ResponseEntity<Post[]> responseEntity = restTemplate.exchange(RequestEntity.get(new URI("http://localhost:8080/posts/"+userId)).headers(headers).build(), Post[].class);
		Post[] userArray = responseEntity.getBody();
		List<Post> posts = Arrays.asList(userArray);
		return posts;

	}
	
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}


