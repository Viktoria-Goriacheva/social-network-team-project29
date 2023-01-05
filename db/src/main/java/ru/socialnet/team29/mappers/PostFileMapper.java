package ru.socialnet.team29.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.socialnet.team29.domain.tables.records.PostFileRecord;
import ru.socialnet.team29.model.PostFile;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")

public interface PostFileMapper {
    @Mapping(source="postFile.imagePath", target="path")
    PostFileRecord postFileToRecord(PostFile postFile);

    @Mapping(source="postFileRecord.path", target="imagePath")
    PostFile RecordToPostFile(PostFileRecord postFileRecord);
}
