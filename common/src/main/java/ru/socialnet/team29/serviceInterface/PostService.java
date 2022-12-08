package ru.socialnet.team29.serviceInterface;

import java.security.Principal;
import ru.socialnet.team29.answers.PagePostResponse;

public interface PostService {

  PagePostResponse getPosts(boolean withFriends, String sort, boolean isDelete, int size, int page);
}

