package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PostLike;
import ru.socialnet.team29.domain.tables.records.PostLikeRecord;

import static org.jooq.impl.DSL.val;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository implements CrudRepository<PostLikeRecord> {

  private DSLContext dsl;

  @Autowired
  public void setDsl(@Lazy DSLContext dsl) {
    this.dsl = dsl;
  }

  public Integer getCountLikeByPostId(Integer id) {
    return dsl.fetchCount(DSL.selectFrom(PostLike.POST_LIKE)
        .where(
            PostLike.POST_LIKE.POST_ID.eq(id),
            PostLike.POST_LIKE.COMMENT_ID.isNull()
        )
    );
  }

  /**
   * Проверка лайкнула ли персона этот пост
   * @param personId идентификатор персоны
   * @param postId идентификатор поста
   * @return true - если лайк уже есть
   */
  public boolean getMyLikeByPostId(Integer postId, Integer personId) {
    return dsl.fetchExists(DSL.selectFrom(PostLike.POST_LIKE)
        .where(
                PostLike.POST_LIKE.POST_ID.eq(postId),
                PostLike.POST_LIKE.COMMENT_ID.isNull(),
                PostLike.POST_LIKE.PERSON_ID.eq(personId)
        )
    );
  }

  /**
   * Проверка лайкнула ли персона этот коммент поста
   * @param personId идентификатор персоны
   * @param postId идентификатор поста
   * @param commentId идентификатор комментария
   * @return true - если лайк уже есть
   */
  public boolean getMyLikeByPostId(Integer postId, Integer personId, Integer commentId) {
    return dsl.fetchExists(DSL.selectFrom(PostLike.POST_LIKE)
            .where(
                    PostLike.POST_LIKE.POST_ID.eq(postId),
                    PostLike.POST_LIKE.PERSON_ID.eq(personId),
                    PostLike.POST_LIKE.COMMENT_ID.eq(commentId)));
  }


  @Override
  public int insert(PostLikeRecord postLike) {
    return dsl.insertInto(PostLike.POST_LIKE)
            .set(dsl.newRecord(PostLike.POST_LIKE, postLike))
            .returning()
            .fetchOne()
            .getId();
  }

  @Override
  public PostLikeRecord findById(int id) {
    return null;
  }

  @Override
  public PostLikeRecord update(PostLikeRecord postLike) {
    return null;
  }

  @Override
  public boolean delete(int id) {
    return false;
  }

  /**
   * Удалить лайк персоны с поста
   */
  public Integer deleteLikeFromPost(PostLikeRecord postLike) {
    PostLike post = PostLike.POST_LIKE;
    return dsl.deleteFrom(post)
            .where(
                    post.POST_ID.eq(postLike.getPostId()),
                    post.PERSON_ID.eq(postLike.getPersonId()),
                    post.COMMENT_ID.eq(postLike.getCommentId()).or(post.COMMENT_ID.isNull().and(val(postLike.getCommentId()).isNull()))
            )
            .execute();
  }

  public Integer getCountLikeByCommentId(int id) {
    return dsl.fetchCount(DSL.selectFrom(PostLike.POST_LIKE)
            .where(
                    PostLike.POST_LIKE.COMMENT_ID.eq(id)
            )
    );
  }

  public boolean getMyLikeByCommentId(Integer id, Integer personId) {
    return dsl.fetchExists(DSL.selectFrom(PostLike.POST_LIKE)
            .where(
                    PostLike.POST_LIKE.COMMENT_ID.eq(id),
                    PostLike.POST_LIKE.PERSON_ID.eq(personId)
            )
    );
  }
}
