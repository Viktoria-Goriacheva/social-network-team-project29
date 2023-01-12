package ru.socialnet.team29.repository;


import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.socialnet.team29.domain.tables.Person;
import ru.socialnet.team29.domain.tables.records.FriendshipRecord;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.dto.PersonSearchDto;

import java.time.OffsetDateTime;
import java.util.List;

import static org.jooq.impl.DSL.val;


@Repository
public class PersonRepository implements CrudRepository<PersonRecord>{

  private DSLContext dsl;

  @Autowired
  public void setDsl(@Lazy DSLContext dsl) {
    this.dsl = dsl;
  }


  public int insert(PersonRecord personRecord) {
    return dsl.insertInto(Person.PERSON)
            .set(dsl.newRecord(Person.PERSON, personRecord))
            .onDuplicateKeyUpdate()
            .set(dsl.newRecord(Person.PERSON, personRecord))
            .returning()
            .fetchOne()
            .getId();
  }

  @Override
  public PersonRecord findById(int id) {
    return dsl.selectFrom(Person.PERSON)
            .where(Person.PERSON.ID.eq(id))
            .fetchOne();
  }


  public List<PersonRecord> findAll(Condition condition) {
    return dsl.selectFrom(Person.PERSON)
            .where(condition)
            .fetch()
            .into(PersonRecord.class);
  }

  public PersonRecord update(PersonRecord personRecord) {
    return dsl.update(Person.PERSON)
            .set(Person.PERSON.from(personRecord))
            .where(Person.PERSON.ID.eq(personRecord.getId()))
            .returning()
            .fetchOptional()
            .orElseThrow(() -> new DataAccessException("Error updating entity: " + personRecord.getId()));
  }

  @Override
  public boolean delete(int id) {
    return dsl.update(Person.PERSON)
            .set(Person.PERSON.IS_DELETED, true)
            .where(Person.PERSON.ID.eq(id))
            .execute() == 1;
  }

  public PersonRecord findPersonByEmail(String email) {
    return dsl.selectFrom(Person.PERSON)
            .where(Person.PERSON.EMAIL.equalIgnoreCase(email))
            .fetchOne();
  }

  public PersonRecord findPersonByToken(String token) {
    return dsl.selectFrom(Person.PERSON)
            .where(Person.PERSON.TOKEN.equalIgnoreCase(token))
            .fetchOne();
  }

  public String findEmailByPersonId(String id) {
    return dsl.selectFrom(Person.PERSON)
            .where(Person.PERSON.ID.equalIgnoreCase(id))
            .fetchOne()
            .getEmail();
  }

  public List<PersonRecord> findByIdListAndFilter(List<Integer> ids, PersonSearchDto filter) {
    Person person = Person.PERSON;
    return dsl.selectFrom(person)
            .where(
                    person.ID.in(ids),
                    (val(filter.getFirstName()).isNull())
                            .or(person.FIRST_NAME.containsIgnoreCase(filter.getFirstName())),
                    (val(filter.getBirthDateFrom()).isNull())
                            .or(person.BIRTH_DATE.ge(OffsetDateTime.parse(filter.getBirthDateFrom()))),
                    (val(filter.getBirthDateTo()).isNull())
                            .or(person.BIRTH_DATE.le(OffsetDateTime.parse(filter.getBirthDateTo()))),
                    (val(filter.getCity()).isNull())
                            .or(person.CITY.eq(filter.getCity())),
                    (val(filter.getCountry()).isNull())
                            .or(person.COUNTRY.eq(filter.getCountry()))
            )
            .orderBy(person.FIRST_NAME)
            .limit(filter.getSize())
            .offset(filter.getPage() - 1)
            .fetchInto(PersonRecord.class);
  }

  @Transactional(readOnly = true)
  public List<PersonRecord> findByPageableTerm(Pageable pageable) {
    return null;
  }

  public int count() {
    return dsl.fetchCount(Person.PERSON);
  }

}
