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
import ru.socialnet.team29.domain.tables.records.PersonRecord;

import java.util.List;


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

  /**
   * @deprecated Вместо этого метода предлагаются более подходящие методы </br>
   * {@link PersonRepository#isExist(int)},
   * {@link PersonRepository#findByIdList(List)}
   * @param condition условие выборки
   * @return список аккаунтов
   */
  @Deprecated
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

  public Integer findEmailByPersonId(String id) { //todo вероятно должен возвращаться String email
    return dsl.selectFrom(Person.PERSON)
            .where(Person.PERSON.ID.equalIgnoreCase(id))
            .fetchOne()
            .getId();
  }

  /**
   * Экзистенциональная проверка аккаунта по идентификатору
   * @param id идентификатор
   * @return Если существует, то true
   */
  public boolean isExist(int id) {
    return dsl.selectFrom(Person.PERSON)
            .where(Person.PERSON.ID.eq(id))
            .fetchOne() != null;

  }

  /**
   * Получение списка аккаунтов по списку идентификаторов
   * @param ids список id
   * @return список аккаунтов
   */
  public List<PersonRecord> findByIdList(List<Integer> ids) {
    return dsl.selectFrom(Person.PERSON)
            .where(Person.PERSON.ID.in(ids))
            .fetch();
  }

  @Transactional(readOnly = true)
  public List<PersonRecord> findByPageableTerm(Pageable pageable) {
    return null;
  }

  public int count() {
    return dsl.fetchCount(Person.PERSON);
  }

}
