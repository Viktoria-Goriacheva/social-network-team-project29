package ru.socialnet.team29.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.Post2tag;
import ru.socialnet.team29.domain.tables.Tag;
import ru.socialnet.team29.domain.tables.records.TagRecord;

@Repository
@RequiredArgsConstructor
public class TagRepository {

  private final DSLContext dsl;


  public TagRecord insert(TagRecord tag) {
    return dsl.insertInto(Tag.TAG)
        .set(dsl.newRecord(Tag.TAG, tag))
        .returning()
        .fetchOne()
        .into(TagRecord.class);
  }


  public List<TagRecord> findAll(Condition condition) {
    return dsl.selectFrom(Tag.TAG)
        .where(condition)
        .fetch()
        .into(TagRecord.class);
  }

  public List<String> findAllTagsByPostId(Integer id) {
    return dsl.select()
        .from(Tag.TAG, Post2tag.POST2TAG)
        .where(Tag.TAG.ID.equal(Post2tag.POST2TAG.TAG_ID))
        .and(Post2tag.POST2TAG.POST_ID.eq(id))
        .fetch()
        .getValues(Tag.TAG.TAG_, String.class);
  }

  public Boolean delete(Integer id) {
    return dsl.deleteFrom(Tag.TAG)
        .where(Tag.TAG.ID.eq(id))
        .execute() == 1;
  }

}
