package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PostComment;
import ru.socialnet.team29.domain.tables.records.PostCommentRecord;


import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private DSLContext dsl;

    @Autowired
    public void setDsl(@Lazy DSLContext dsl) {
        this.dsl = dsl;
    }

    public PostCommentRecord insert(PostCommentRecord comment) {
        return dsl.insertInto(PostComment.POST_COMMENT)
                .set(dsl.newRecord(PostComment.POST_COMMENT, comment))
                .onDuplicateKeyUpdate()
                .set(dsl.newRecord(PostComment.POST_COMMENT, comment))
                .returning()
                .fetchOne()
                .into(PostCommentRecord.class);
    }

    @Deprecated
    public List<PostCommentRecord> findAll(Condition condition) {
        return dsl.selectFrom(PostComment.POST_COMMENT)
                .where(condition)
                .fetch()
                .into(PostCommentRecord.class);
    }

    public Integer getCountCommentsByPostId(Integer id) {
        return dsl.fetchCount(DSL.selectFrom(PostComment.POST_COMMENT)
                .where(PostComment.POST_COMMENT.POST_ID.eq(id)));
    }

    public Boolean deleteById(Integer id) {
        return dsl.deleteFrom(PostComment.POST_COMMENT)
                .where(PostComment.POST_COMMENT.ID.eq(id))
                .execute() == 1;
    }

    public PostCommentRecord update(PostCommentRecord postCommentRecord) {
        return dsl.update(PostComment.POST_COMMENT)
                .set(PostComment.POST_COMMENT.from(postCommentRecord))
                .where(PostComment.POST_COMMENT.ID.eq(postCommentRecord.getId()))
                .returning()
                .fetchOne();
//                .fetchOptional()
//                .orElseThrow(() -> new DataAccessException("Error updating entity: " + postCommentRecord.getId()));

    }

    public PostCommentRecord findById(int id) {
        return dsl.selectFrom(PostComment.POST_COMMENT)
                .where(PostComment.POST_COMMENT.ID.eq(id))
                .fetchOne();
    }

    public List<Integer> findCommentByPostId(Integer postId) {
        return dsl.selectFrom(PostComment.POST_COMMENT)
                .where(PostComment.POST_COMMENT.POST_ID.eq(postId))
                .and(PostComment.POST_COMMENT.IS_BLOCKED.eq(false))
                .orderBy(PostComment.POST_COMMENT.TIME.desc())
                .fetch()
                .getValues(PostComment.POST_COMMENT.ID);
    }

    public List<Integer> getCommentIdByPostId(Integer postId) {
        return dsl.selectFrom(PostComment.POST_COMMENT)
                .where(PostComment.POST_COMMENT.POST_ID.eq(postId))
                .and(PostComment.POST_COMMENT.IS_BLOCKED.eq(false))
                .fetch()
                .getValues(PostComment.POST_COMMENT.ID);

    }

    public PostCommentRecord findByCommentId(Integer commentId) {
        return dsl.selectFrom(PostComment.POST_COMMENT)
                .where(PostComment.POST_COMMENT.ID.eq(commentId))
                .fetchOne();
    }
}

