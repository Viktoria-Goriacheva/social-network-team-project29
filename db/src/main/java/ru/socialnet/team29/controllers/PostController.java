package ru.socialnet.team29.controllers;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.services.PostService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

  private final PostService postService;

  @GetMapping("/posts")
  public List<PostDto> getPostsById(@RequestParam String email) {
    log.info("Получили запрос на выдачу всех постов по айди автора");
    return postService.getPosts(email);
  }
}
