package ru.socialnet.team29.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.domain.tables.records.PostCommentRecord;
import ru.socialnet.team29.domain.tables.records.PostTableRecord;
import ru.socialnet.team29.dto.CommentDto;
import ru.socialnet.team29.model.Person;
import ru.socialnet.team29.model.PostDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CommentMapper {
    CommentDto postCommentRecordToCommentDto(PostCommentRecord postCommentRecord);
    PostCommentRecord CommentDtoToPostCommentRecord(CommentDto commentDto);
}
