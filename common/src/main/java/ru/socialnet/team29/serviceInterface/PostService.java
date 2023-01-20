package ru.socialnet.team29.serviceInterface;

import org.springframework.http.HttpStatus;
import ru.socialnet.team29.answers.PagePostResponse;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.payloads.PostPayload;

public interface PostService {

  Boolean addPost(PostPayload postPayload);
  PostDto findPostById(Integer id, String email);
  Boolean updatePost(Integer id, PostPayload postPayload);
  Boolean deletePost(Integer id);
  PagePostResponse getPosts(String tags, long dateTo, long dateFrom,
      String author, String text, boolean withFriends, String sort, boolean isDelete,
      int size, Integer accountIds, int page);

  HttpStatus addLikeToPost(Integer id);

  HttpStatus deleteLikeFromPost(Integer id);

  HttpStatus addLikeToPostComment(Integer id, Integer commentId);

  HttpStatus deleteLikeFromPostComment(Integer id, Integer commentId);
}

