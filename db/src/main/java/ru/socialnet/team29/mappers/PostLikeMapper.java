package ru.socialnet.team29.mappers;

import org.mapstruct.Mapper;
import ru.socialnet.team29.domain.tables.records.PostLikeRecord;
import ru.socialnet.team29.dto.PostLikeDto;

@Mapper(componentModel = "spring")
public interface PostLikeMapper {
    PostLikeRecord PostLikeDtoToPostLikeRecord(PostLikeDto postLikeDto);
}
