package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.Post2tag;
import ru.socialnet.team29.domain.tables.Tag;
import ru.socialnet.team29.domain.tables.records.Post2tagRecord;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class Post2TagRepository {

    private final DSLContext dsl;


    public Post2tagRecord insert(Post2tagRecord post2tag) {
        return dsl.insertInto(Post2tag.POST2TAG)
                .set(dsl.newRecord(Post2tag.POST2TAG, post2tag))
                .returning()
                .fetchOne()
                .into(Post2tagRecord.class);
    }


    public List<Post2tagRecord> findAll(Condition condition) {
        return dsl.selectFrom(Post2tag.POST2TAG)
                .where(condition)
                .fetch()
                .into(Post2tagRecord.class);
    }


    public Boolean deleteByPostId(Integer id) {
        Integer idTag = dsl.selectFrom(Post2tag.POST2TAG)
                .where(Post2tag.POST2TAG.POST_ID.eq(id).getComment()).fetchOne().getId();
        dsl.deleteFrom(Tag.TAG).where(Tag.TAG.ID.eq(idTag)).execute();

        return dsl.deleteFrom(Post2tag.POST2TAG)
                .where(Post2tag.POST2TAG.POST_ID.eq(id))
                .execute() == 1;
    }


}
