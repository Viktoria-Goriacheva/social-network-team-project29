package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PostLike;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository implements CrudRepository<PostLike> {

  private DSLContext dsl;

  @Autowired
  public void setDsl(@Lazy DSLContext dsl) {
    this.dsl = dsl;
  }
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
  public PostLike update(PostLike postLike) {
    return null;
  }

  @Override
  public boolean delete(int id) {
    return false;
  }
}
