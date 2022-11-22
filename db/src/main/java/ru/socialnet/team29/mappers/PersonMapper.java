package ru.socialnet.team29.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.model.Person;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PersonMapper {


    Person PersonRecordToPerson(PersonRecord personRecord);

    PersonRecord PersonToPersonRecord(Person person);


}
