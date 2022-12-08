package ru.socialnet.team29.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.Post2tag;
import ru.socialnet.team29.domain.tables.Tag;
import ru.socialnet.team29.domain.tables.records.Post2tagRecord;
import ru.socialnet.team29.services.DslContextCustom;

@Repository
@RequiredArgsConstructor
public class Post2TagRepository {

  private final DslContextCustom dslContextCustom;
  private static DSLContext dsl;


  public Post2tagRecord insert(Post2tagRecord post2tag) {
    initDsl();
    return dsl.insertInto(Post2tag.POST2TAG)
        .set(dsl.newRecord(Post2tag.POST2TAG, post2tag))
        .returning()
        .fetchOne()
        .into(Post2tagRecord.class);
  }


  public List<Post2tagRecord> findAll(Condition condition) {
    initDsl();
    return dsl.selectFrom(Post2tag.POST2TAG)
        .where(condition)
        .fetch()
        .into(Post2tagRecord.class);
  }


  public Boolean deleteByPostId(Integer id) {
    initDsl();
    Integer idTag = dsl.selectFrom(Post2tag.POST2TAG)
        .where(Post2tag.POST2TAG.POST_ID.eq(id).getComment()).fetchOne().getId();
    dsl.deleteFrom(Tag.TAG).where(Tag.TAG.ID.eq(idTag)).execute();

    return dsl.deleteFrom(Post2tag.POST2TAG)
        .where(Post2tag.POST2TAG.POST_ID.eq(id))
        .execute() == 1;
  }

  private void initDsl() {
    if (dsl == null) {
      dsl = dslContextCustom.initDslContext();
    }
  }
}
