package ru.socialnet.team29.mappers;

import org.mapstruct.Mapper;
import ru.socialnet.team29.testcase.Post;
import ru.socialnet.team29.testcase.PostDto;

@Mapper(componentModel = "spring")
public interface PostMapper {

  PostDto postToPostDto(Post post);

  Post postDtotoPost(PostDto postDto);
}
