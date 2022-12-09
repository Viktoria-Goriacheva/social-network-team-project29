package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PostLike;
import ru.socialnet.team29.services.DslContextCustom;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository extends AbstractRepository<PostLike> {

  private final DSLContext dsl;

  public Integer getCountLikeByPostId(Integer id) {
    return dsl.fetchCount(DSL.selectFrom(PostLike.POST_LIKE)
        .where(PostLike.POST_LIKE.POST_ID.eq(id)));
  }

  public boolean getMyLikeByPostId(Integer postId, Integer personId) {
    return dsl.fetchExists(DSL.selectFrom(PostLike.POST_LIKE)
        .where(PostLike.POST_LIKE.POST_ID.eq(postId), PostLike.POST_LIKE.PERSON_ID.eq(personId)));
  }


  @Override
  public int insert(PostLike postLike) {
    return 0;
  }

  @Override
  public PostLike findById(int id) {
    return null;
  }

  @Override
  public boolean update(PostLike postLike) {
    return false;
  }

  @Override
  public boolean delete(int id) {
    return false;
  }
}
