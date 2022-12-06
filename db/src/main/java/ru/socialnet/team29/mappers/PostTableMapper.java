package ru.socialnet.team29.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import ru.socialnet.team29.domain.tables.records.PostTableRecord;
import ru.socialnet.team29.model.PostDto;

@Mapper(componentModel = "spring")
public interface PostTableMapper {

  PostDto PostTableRecordToPostDto(PostTableRecord postTableRecord);

  PostTableRecord PostDtoToPostTableRecord(PostDto postDto);

  List<PostDto> PostTableRecordToPostDto(List<PostTableRecord> models);

  List<PostTableRecord> PostDtoToPostTableRecord(List<PostDto> dtos);
}

