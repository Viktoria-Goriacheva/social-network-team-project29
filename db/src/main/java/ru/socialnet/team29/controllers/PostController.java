package ru.socialnet.team29.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.dto.PostLikeDto;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.services.PostLikeService;
import ru.socialnet.team29.services.PostService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

  private final PostService postService;
  private final PostLikeService postLikeService;

  @GetMapping("/posts")
  public List<PostDto> getPostsById(@RequestParam String email , Integer accountIds, String tags, long dateTo, long dateFrom, String author, String text) {
    log.info("Получили запрос на выдачу всех постов по id автора {}", accountIds);
    return postService.getPosts(email, accountIds, tags, dateTo, dateFrom, author, text);
  }

  @PostMapping("/post")
  public Boolean addPost(@RequestBody PostDto postDto) {
    log.info("Добавляем в базу новый пост authorId={} title={}", postDto.getAuthorId(), postDto.getTitle());
    return postService.addNewPost(postDto);
  }

  @GetMapping("/post")
  public PostDto findPostById(@RequestParam Integer id, @RequestParam String email) {
    log.info("Запрос поста Id={}", id);
    return postService.getPostById(id, email);
  }

  @PutMapping("/post")
  public boolean updatePost(@RequestBody PostDto postDto){
    log.info("Обновляем пост Id={} title={}", postDto.getId(), postDto.getTitle());
    return postService.updatePost(postDto);
  }

  @DeleteMapping("/post")
  public Boolean deletePost(@RequestParam Integer id) {
    log.info("Удаляем пост Id={}", id);
    return postService.deletePost(id);
  }

  @PostMapping(value = "/post/like")
  Boolean addLikeToPost(@RequestBody PostLikeDto postLikeDto) {
    log.info("Получили запрос от core - добавить лайк к посту id=" + postLikeDto.getPostId());
    return postLikeService.addLikeToPost(postLikeDto);
  }

  @DeleteMapping(value = "/post/like")
  Boolean deleteLikeFromPost(@RequestBody PostLikeDto postLikeDto) {
    log.info("Получили запрос от core - убрать лайк с поста id=" + postLikeDto.getPostId());
    return postLikeService.deleteLikeFromPost(postLikeDto);
  }

  @PostMapping(value = "/post/comment/like")
  Boolean addLikeToPostComment(@RequestBody PostLikeDto postLikeDto) {
    log.info("Получили запрос от core - добавить лайк к комменту (id=" + postLikeDto.getCommentId() + ") поста id=" + postLikeDto.getPostId());
    return postLikeService.addLikeToPostComment(postLikeDto);
  }

  @DeleteMapping(value = "/post/comment/like")
  Boolean deleteLikeFromPostComment(@RequestBody PostLikeDto postLikeDto) {
    log.info("Получили запрос от core - убрать лайк с коммента (id=" + postLikeDto.getCommentId() + ") поста id=" + postLikeDto.getPostId());
    return postLikeService.deleteLikeFromPostComment(postLikeDto);
  }
}
