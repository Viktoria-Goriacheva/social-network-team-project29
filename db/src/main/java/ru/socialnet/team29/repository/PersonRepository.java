package ru.socialnet.team29.repository;


import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.socialnet.team29.domain.tables.Person;
import ru.socialnet.team29.domain.tables.records.PersonRecord;
import ru.socialnet.team29.dto.PersonSearchDto;
import ru.socialnet.team29.payloads.AccountSearchFilter;

import java.time.OffsetDateTime;
import java.util.List;

import static org.jooq.impl.DSL.val;


@Repository
public class PersonRepository implements CrudRepository<PersonRecord> {

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
                        person.BIRTH_DATE.isNull().or(
                                person.BIRTH_DATE.between(
                                        OffsetDateTime.parse(filter.getBirthDateFrom()),
                                        OffsetDateTime.parse(filter.getBirthDateTo()))),
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
    public List<PersonRecord> findByPageRequest(PageRequest pageRequest) {
        return dsl.selectFrom(Person.PERSON)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
    }

    public List<PersonRecord> findBySearchFilter(AccountSearchFilter searchFilter) {
        // todo not finished yet
        return null;
    }

    public int count() {
        return dsl.fetchCount(Person.PERSON);
    }

    public List<PersonRecord> findPageByIds(List<Integer> ids, PageRequest pageRequest) {
        return dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.ID.in(ids))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
    }

    public List<PersonRecord> findByIds(List<Integer> ids) {
        return dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.ID.in(ids))
                .fetch();
    }

    public List<Integer> findAllIds() {
        return dsl.selectFrom(Person.PERSON)
                .where(Person.PERSON.IS_DELETED.isNull())
                .or(Person.PERSON.IS_DELETED.isFalse())
                .fetch()
                .getValues(Person.PERSON.ID);
    }

    public List<PersonRecord> findByCondition(Condition condition, PageRequest pageRequest) {
        return dsl.selectFrom(Person.PERSON)
                .where(condition)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
    }

    public long countByCondition(Condition condition) {
        return dsl.selectFrom(Person.PERSON)
                .where(condition)
                .fetch()
                .size();
    }

    public List<Integer> getIdUsersBirthdateToday() {
        String query = "select id from socialnet.person p where (select date_part('day', birth_date)) = {0} and (select date_part('month', birth_date)) = {1}";
        return (List<Integer>) dsl.resultQuery(query, OffsetDateTime.now().getDayOfMonth(), OffsetDateTime.now().getMonthValue()).fetch(0);

    }
}
