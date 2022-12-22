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

    public Boolean delete(Integer id) {
        return dsl.deleteFrom(PostComment.POST_COMMENT)
                .where(PostComment.POST_COMMENT.ID.eq(id))
                .execute() == 1;
    }

}

