package ru.socialnet.team29.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.socialnet.team29.domain.tables.records.Post2tagRecord;
import ru.socialnet.team29.model.Post2tag;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface Post2tagMapper {
    Post2tagRecord Post2tagToRecord(Post2tag post2tag);

    Post2tag RecordToPost2tag(Post2tagRecord post2tagRecord);
}
