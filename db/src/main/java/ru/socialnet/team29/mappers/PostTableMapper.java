package ru.socialnet.team29.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.socialnet.team29.domain.tables.records.PostTableRecord;
import ru.socialnet.team29.model.PostDto;

@Mapper(componentModel = "spring")
public interface PostTableMapper {

  @Mapping(source = "timechanged", target = "timeChanged")
  PostDto PostTableRecordToPostDto(PostTableRecord postTableRecord);

  @Mapping(source = "postDto.timeChanged", target = "timechanged")
  PostTableRecord PostDtoToPostTableRecord(PostDto postDto);

  List<PostDto> PostTableRecordToPostDto(List<PostTableRecord> models);

  List<PostTableRecord> PostDtoToPostTableRecord(List<PostDto> dtos);
}

