package ru.socialnet.team29;


import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.config.JdbcConnections;
import ru.socialnet.team29.domain.tables.Person;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.services.DslContextCustom;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class PersonRepository {
    private final DslContextCustom dslContextCustom;
    private static DSLContext dsl;


    public PersonRecord insert(PersonRecord person) {
      initDsl();
        return dsl.insertInto(Person.PERSON)
                .set(dsl.newRecord(Person.PERSON, person))
                .returning()
                .fetchOne()
                .into(PersonRecord.class);
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

    public List<PersonRecord> findPersonByEmail(String email) {
        initDsl();
        return dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.E_MAIL.equalIgnoreCase(email))
                .fetch()
                .into(PersonRecord.class);
    }

    private void initDsl() {
        if (dsl == null) {
            dsl = dslContextCustom.initDslContext();
        }
    }


}
