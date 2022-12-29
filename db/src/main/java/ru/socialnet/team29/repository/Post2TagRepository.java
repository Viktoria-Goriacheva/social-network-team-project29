package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.Post2tag;
import ru.socialnet.team29.domain.tables.records.Post2tagRecord;

@Repository
@RequiredArgsConstructor
public class Post2TagRepository implements CrudRepository<Post2tagRecord> {

    private DSLContext dsl;

    @Autowired
    public void setDsl(@Lazy DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public int insert(Post2tagRecord post2tag) {
        return dsl.insertInto(Post2tag.POST2TAG)
                .set(dsl.newRecord(Post2tag.POST2TAG, post2tag))
                .returning()
                .fetchOne()
                .getId();
    }

    @Override
    public Post2tagRecord findById(int id) {
        return null;
    }

    @Override
    public Post2tagRecord update(Post2tagRecord post2tagRecord) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return dsl.deleteFrom(Post2tag.POST2TAG)
                .where(Post2tag.POST2TAG.ID.eq(id))
                .execute() == 1;
    }

    public int findByRelation(int postId, int tagId) {
        return dsl.selectFrom(Post2tag.POST2TAG)
                .where(Post2tag.POST2TAG.POST_ID.eq(postId))
                .and(Post2tag.POST2TAG.TAG_ID.eq(tagId))
                .fetchOne()
                .getId();
    }
}
