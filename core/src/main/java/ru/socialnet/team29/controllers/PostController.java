package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.PagePostResponse;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.payloads.PostPayload;
import ru.socialnet.team29.serviceInterface.PostService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@Slf4j
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

  @PostMapping(value = "")
  ResponseEntity<Boolean> addNewPost(@RequestBody PostPayload postPayload) {
    return new ResponseEntity<>(postService.addPost(postPayload), HttpStatus.CREATED);
  }

  @GetMapping(value = "/{id}")
  ResponseEntity<PostDto> findById(@PathVariable(value = "id") Integer id) {
    return new ResponseEntity<>(postService.findPostById(id), HttpStatus.OK);
  }

  @PutMapping(value = "/{id}")
  ResponseEntity<Boolean> updatePost(@PathVariable(value = "id") Integer id, @RequestBody PostPayload postPayload) {
    return new ResponseEntity<>(postService.updatePost(id, postPayload), HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/{id}")
  ResponseEntity<Boolean> deletePost(@PathVariable(value = "id") Integer id) {
    return new ResponseEntity<>(postService.deletePost(id), HttpStatus.OK);
  }
}
