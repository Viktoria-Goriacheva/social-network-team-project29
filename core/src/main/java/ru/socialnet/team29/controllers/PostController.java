package ru.socialnet.team29.controllers;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.socialnet.team29.answers.PagePostResponse;
import ru.socialnet.team29.serviceInterface.PostService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

  private final PostService postService;

  @GetMapping
  public ResponseEntity<PagePostResponse> getPost(
      @RequestParam(value = "withFriends", required = false, defaultValue = "true") boolean withFriends,
      @RequestParam(value = "sort", required = false, defaultValue = "time,desc") String sort,
      @RequestParam(value = "isDelete", required = false, defaultValue = "false") boolean isDelete,
      @RequestParam(value = "size", required = false, defaultValue = "3") int size,
      @RequestParam(value = "page", defaultValue = "1") int page) {
    return new ResponseEntity(
        postService.getPosts(withFriends, sort, isDelete, size, page),
        HttpStatus.OK);
  }
}
