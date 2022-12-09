package ru.socialnet.team29.serviceInterface;

import java.security.Principal;
import ru.socialnet.team29.answers.PagePostResponse;
import ru.socialnet.team29.model.PostDto;
import ru.socialnet.team29.payloads.PostPayload;

public interface PostService {

  Boolean addPost(PostPayload postPayload);
  PostDto findPostById(Integer id);
  Boolean updatePost(Integer id, PostPayload postPayload);
  Boolean deletePost(Integer id);
  PagePostResponse getPosts(boolean withFriends, String sort, boolean isDelete, int size, int page);
}

