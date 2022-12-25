package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PostTable;
import ru.socialnet.team29.domain.tables.records.PostTableRecord;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository extends AbstractRepository<PostTableRecord> {

    private DSLContext dsl;

    @Autowired
    public void setDsl(@Lazy DSLContext dsl) {
        this.dsl = dsl;
    }

    public int insert(PostTableRecord post) {
        return dsl.insertInto(PostTable.POST_TABLE)
                .set(dsl.newRecord(PostTable.POST_TABLE, post))
                .returning()
                .fetchOne()
                .getId();
    }

    @Override
    public PostTableRecord findById(int id) {
        return dsl.selectFrom(PostTable.POST_TABLE)
                .where(PostTable.POST_TABLE.ID.eq(id))
                .fetchOne();
    }

    @Override
    public boolean update(PostTableRecord post) {
        return dsl.update(PostTable.POST_TABLE)
                .set(PostTable.POST_TABLE.from(post))
                .where(PostTable.POST_TABLE.ID.eq(post.getId()))
                .execute() == 1;
    }

    @Override
    public boolean delete(int id) {
        return dsl.update(PostTable.POST_TABLE)
                .set(PostTable.POST_TABLE.IS_DELETE, true)
                .where(PostTable.POST_TABLE.ID.eq(id))
                .execute() == 1;
    }

    public List<PostTableRecord> findAllByAuthorId(Integer id) {
        return dsl.selectFrom(PostTable.POST_TABLE)
                .where(PostTable.POST_TABLE.AUTHOR_ID.eq(id))
                .and(PostTable.POST_TABLE.IS_BLOCKED.eq(false))
                .and(PostTable.POST_TABLE.IS_DELETE.eq(false))
                .orderBy(PostTable.POST_TABLE.TIME.desc())
                .fetch();
    }

    public List<Integer> findPostIdsByAuthor(Integer authorId) {
        return dsl.selectFrom(PostTable.POST_TABLE)
                .where(PostTable.POST_TABLE.AUTHOR_ID.eq(authorId))
                .orderBy(PostTable.POST_TABLE.TIME.desc())
                .fetch()
                .getValues(PostTable.POST_TABLE.ID);
    }
}

