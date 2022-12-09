package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.Post2tag;
import ru.socialnet.team29.domain.tables.Tag;
import ru.socialnet.team29.domain.tables.records.TagRecord;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepository extends AbstractRepository<TagRecord> {

    private final DSLContext dsl;

  public int insert(TagRecord tag) {
    return dsl.insertInto(Tag.TAG)
        .set(dsl.newRecord(Tag.TAG, tag))
        .returning()
        .fetchOne()
        .getId();
  }

  @Override
  public TagRecord findById(int id) {
    return null;
  }

  @Override
  public boolean update(TagRecord tagRecord) {
    return false;
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

  @Override
  public boolean delete(int id) {
    return dsl.deleteFrom(Tag.TAG)
        .where(Tag.TAG.ID.eq(id))
        .execute() == 1;
  }

  public int findByTag(String tag) {
    TagRecord tagRecord = dsl.selectFrom(Tag.TAG)
            .where(Tag.TAG.TAG_.eq(tag))
            .fetchOne();
    if (tagRecord == null)
      return 0;
    return tagRecord.getId();
  }
}
