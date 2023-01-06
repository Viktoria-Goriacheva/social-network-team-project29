package ru.socialnet.team29.mappers;

import org.mapstruct.Mapper;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.dto.RecommendationFriendsDto;
import ru.socialnet.team29.model.FriendForFront;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendMapper {
    List<FriendForFront> PersonRecordToFriendForFront(List<PersonRecord> personRecords);

    List<RecommendationFriendsDto> PersonRecordsToRecommendationFriendsDto(List<PersonRecord> personRecords);
}
