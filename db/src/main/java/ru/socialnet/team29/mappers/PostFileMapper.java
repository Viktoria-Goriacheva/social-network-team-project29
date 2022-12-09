package ru.socialnet.team29.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.socialnet.team29.domain.tables.records.Post2tagRecord;
import ru.socialnet.team29.domain.tables.records.PostFileRecord;
import ru.socialnet.team29.model.PostFile;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")

public interface PostFileMapper {
    PostFileRecord postFileToRecord(PostFile postFile);

    PostFile RecordToPostFile(PostFileRecord postFileRecord);
}
