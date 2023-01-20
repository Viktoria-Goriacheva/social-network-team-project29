package ru.socialnet.team29.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.socialnet.team29.domain.tables.records.MessageTableRecord;
import ru.socialnet.team29.responses.dialog_response.MessageDto;

@Mapper(componentModel = "spring")
public interface MessageDtoMapper {

    @Mapping(target = "time", expression = "java(messageTableRecords.getTime().getLong(java.time.temporal.ChronoField.INSTANT_SECONDS))")
    MessageDto MessageTableRecordsToMessageDto(MessageTableRecord messageTableRecords);

}
