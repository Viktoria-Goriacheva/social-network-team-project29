package ru.socialnet.team29.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.answers.PagePostResponse;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.payloads.PostPayload;
import ru.socialnet.team29.serviceInterface.NotificationService;
import ru.socialnet.team29.serviceInterface.PostService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
@Slf4j
public class PostController {

  private final PostService postService;


  @GetMapping
  public ResponseEntity<PagePostResponse> getPost(
      @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
      @RequestParam(value = "dateTo", required = false, defaultValue = "0") long dateTo,
      @RequestParam(value = "dateFrom", required = false, defaultValue = "0") long dateFrom,
      @RequestParam(value = "author", required = false, defaultValue = "") String author,
      @RequestParam(value = "text", required = false, defaultValue = "") String text,
      @RequestParam(value = "withFriends", required = false, defaultValue = "false") boolean withFriends,
      @RequestParam(value = "sort") String sort,
      @RequestParam(value = "isDelete") boolean isDelete,
      @RequestParam(value = "size") int size,
      @RequestParam(value = "accountIds", required = false, defaultValue = "0") Integer accountIds,
      @RequestParam(value = "page", defaultValue = "1") int page) {
    return new ResponseEntity(
        postService.getPosts(tags, dateTo, dateFrom, author, text, withFriends, sort, isDelete, size, accountIds, page),
        HttpStatus.OK);
  }

  @PostMapping(value = "")
  ResponseEntity<Boolean> addNewPost(@RequestBody PostPayload postPayload) {
    return new ResponseEntity<>(postService.addPost(postPayload), HttpStatus.CREATED);
  }

  @GetMapping(value = "/{id}")
  ResponseEntity<PostDto> findById(@PathVariable(value = "id") Integer id) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return new ResponseEntity<>(postService.findPostById(id, email), HttpStatus.OK);
  }

  @PutMapping(value = "/{id}")
  ResponseEntity<Boolean> updatePost(@PathVariable(value = "id") Integer id, @RequestBody PostPayload postPayload) {
    return new ResponseEntity<>(postService.updatePost(id, postPayload), HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/{id}")
  ResponseEntity<Boolean> deletePost(@PathVariable(value = "id") Integer id) {
    return new ResponseEntity<>(postService.deletePost(id), HttpStatus.OK);
  }

  /**
   * Добавление лайка к посту
   */
  @PostMapping(value = "/{id}/like")
  ResponseEntity<Void> addLikeToPost(@PathVariable(value = "id") Integer id) {
    return new ResponseEntity<>(postService.addLikeToPost(id));
  }

  /**
   * Удаление лайка от поста
   */
  @DeleteMapping(value = "/{id}/like")
  ResponseEntity<Void> deleteLikeFromPost(@PathVariable(value = "id") Integer id) {
    return new ResponseEntity<>(postService.deleteLikeFromPost(id));
  }

  /**
   * Добавление лайка к комментарию
   */
  @PostMapping(value = "/{id}/comment/{commentId}/like")
  ResponseEntity<Void> addLikeToPostComment(
          @PathVariable(value = "id") Integer id,
          @PathVariable(value = "commentId") Integer commentId) {
    return new ResponseEntity<>(postService.addLikeToPostComment(id, commentId));
  }

  /**
   * Удаление лайка комментария
   */
  @DeleteMapping(value = "/{id}/comment/{commentId}/like")
  ResponseEntity<Void> deleteLikeFromPostComment(
          @PathVariable(value = "id") Integer id,
          @PathVariable(value = "commentId") Integer commentId) {
    return new ResponseEntity<>(postService.deleteLikeFromPostComment(id, commentId));
  }
}
