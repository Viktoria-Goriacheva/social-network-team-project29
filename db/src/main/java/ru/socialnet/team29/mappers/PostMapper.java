package ru.socialnet.team29.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.socialnet.team29.domain.tables.records.PostTableRecord;
import ru.socialnet.team29.testcase.Post;
import ru.socialnet.team29.testcase.PostDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PostMapper {

  PostDto postToPostDto(Post post);

  Post postDtotoPost(PostDto postDto);
}
