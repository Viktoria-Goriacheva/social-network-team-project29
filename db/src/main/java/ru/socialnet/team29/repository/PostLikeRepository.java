package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PostLike;
import ru.socialnet.team29.services.DslContextCustom;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository {

  private final DslContextCustom dslContextCustom;
  private static DSLContext dsl;

  public Integer getCountLikeByPostId(Integer id) {
    initDsl();
    return dsl.fetchCount(DSL.selectFrom(PostLike.POST_LIKE)
        .where(PostLike.POST_LIKE.POST_ID.eq(id)));
  }

  public boolean getMyLikeByPostId(Integer postId, Integer personId) {
    initDsl();
    return dsl.fetchExists(DSL.selectFrom(PostLike.POST_LIKE)
        .where(PostLike.POST_LIKE.POST_ID.eq(postId), PostLike.POST_LIKE.PERSON_ID.eq(personId)));
  }

  private void initDsl() {
    if (dsl == null) {
      dsl = dslContextCustom.initDslContext();
    }
  }
}
