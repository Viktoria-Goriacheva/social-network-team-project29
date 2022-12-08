package ru.socialnet.team29.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PostComment;
import ru.socialnet.team29.domain.tables.records.PostCommentRecord;
import ru.socialnet.team29.services.DslContextCustom;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

  private final DslContextCustom dslContextCustom;
  private static DSLContext dsl;


  public PostCommentRecord insert(PostCommentRecord comment) {
    initDsl();
    return dsl.insertInto(PostComment.POST_COMMENT)
        .set(dsl.newRecord(PostComment.POST_COMMENT, comment))
        .returning()
        .fetchOne()
        .into(PostCommentRecord.class);
  }


  public List<PostCommentRecord> findAll(Condition condition) {
    initDsl();
    return dsl.selectFrom(PostComment.POST_COMMENT)
        .where(condition)
        .fetch()
        .into(PostCommentRecord.class);
  }

  public Integer getCountCommentsByPostId(Integer id) {
    initDsl();
    return dsl.fetchCount(DSL.selectFrom(PostComment.POST_COMMENT)
        .where(PostComment.POST_COMMENT.POST_ID.eq(id)));
  }

  public Boolean delete(Integer id) {
    initDsl();
    return dsl.deleteFrom(PostComment.POST_COMMENT)
        .where(PostComment.POST_COMMENT.ID.eq(id))
        .execute() == 1;
  }

  private void initDsl() {
    if (dsl == null) {
      dsl = dslContextCustom.initDslContext();
    }
  }
}

