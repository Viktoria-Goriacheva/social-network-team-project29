package ru.socialnet.team29.repository;


import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.Person;
import ru.socialnet.team29.domain.tables.records.PersonRecord;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class PersonRepository {
    private final DSLContext dsl;


    public PersonRecord insert(PersonRecord personRecord) {
        return dsl.insertInto(Person.PERSON)
                .set(dsl.newRecord(Person.PERSON, personRecord))
                .onDuplicateKeyUpdate()
                .set(dsl.newRecord(Person.PERSON, personRecord))
                .returning()
                .fetchOne();
    }

    public List<PersonRecord> findAll(Condition condition) {
        return dsl.selectFrom(Person.PERSON)
                .where(condition)
                .fetch()
                .into(PersonRecord.class);
    }

    public Boolean delete(Integer id) {
        return dsl.deleteFrom(Person.PERSON)
                .where(Person.PERSON.ID.eq(id))
                .execute() == 200;
    }

    public Integer findPersonIdByEmail(String email) {
        return dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.EMAIL.equalIgnoreCase(email))
                .fetchOne()
                .getId();
    }

    public PersonRecord findPersonByEmail(String email) {
        return dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.EMAIL.equalIgnoreCase(email))
                .fetchOne();
    }

    public boolean insertPerson(ru.socialnet.team29.model.Person person) {
        return (dsl.insertInto(Person.PERSON)
                .set(dsl.newRecord(Person.PERSON, person))
                .returning()
                .fetchOne()
                .into(ru.socialnet.team29.model.Person.class)) != null;
    }
}
