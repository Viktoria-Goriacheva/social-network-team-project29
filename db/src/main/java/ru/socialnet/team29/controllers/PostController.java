package ru.socialnet.team29.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.services.PostService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

  private final PostService postService;

  @GetMapping("/posts")
  public List<PostDto> getPostsById(@RequestParam String email) {
    log.info("Получили запрос на выдачу всех постов по почте автора {}", email);
    return postService.getPostsByAuthorEmail(email);
  }

  @PostMapping("/post")
  public Boolean addPost(@RequestBody PostDto postDto) {
    log.info("Добавляем в базу новый пост authorId={} title={}", postDto.getAuthorId(), postDto.getTitle());
    return postService.addNewPost(postDto);
  }

  @GetMapping("/post")
  public PostDto findPostById(@RequestParam Integer id) {
    log.info("Запрос поста Id={}", id);
    return postService.getPostById(id);
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
}
