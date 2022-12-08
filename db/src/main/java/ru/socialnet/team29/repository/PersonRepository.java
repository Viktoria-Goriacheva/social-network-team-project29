package ru.socialnet.team29.repository;


import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.Keys;
import ru.socialnet.team29.domain.tables.Person;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.services.DslContextCustom;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class PersonRepository {
    private final DslContextCustom dslContextCustom;
    private static DSLContext dsl;


    public PersonRecord insert(PersonRecord personRecord) {
        initDsl();
         return dsl.insertInto(Person.PERSON)
                .set(dsl.newRecord(Person.PERSON, personRecord))
                 .onDuplicateKeyUpdate()
                 .set(dsl.newRecord(Person.PERSON, personRecord))
                .returning()
                .fetchOne();
    }

    public List<PersonRecord> findAll(Condition condition) {
        initDsl();
        return dsl.selectFrom(Person.PERSON)
                .where(condition)
                .fetch()
                .into(PersonRecord.class);
    }

    public Boolean delete(Integer id) {
        initDsl();
        return dsl.deleteFrom(Person.PERSON)
                .where(Person.PERSON.ID.eq(id))
                .execute() == 200;
    }

  public Integer findPersonIdByEmail(String email) {
    initDsl();
    return dsl.selectFrom(Person.PERSON)
        .where(Person.PERSON.EMAIL.equalIgnoreCase(email))
        .fetchOne()
        .getId();
  }

    public PersonRecord findPersonByEmail(String email) {
        initDsl();
        return dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.EMAIL.equalIgnoreCase(email))
                .fetchOne();
    }

    private void initDsl() {
        if (dsl == null) {
            dsl = dslContextCustom.initDslContext();
        }
    }


    public boolean insertPerson(ru.socialnet.team29.model.Person person) {
        initDsl();
        return (dsl.insertInto(Person.PERSON)
                .set(dsl.newRecord(Person.PERSON, person))
                .returning()
                .fetchOne()
                .into(ru.socialnet.team29.model.Person.class)) != null;
    }
}
