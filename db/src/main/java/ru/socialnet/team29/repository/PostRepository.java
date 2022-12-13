package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PostTable;
import ru.socialnet.team29.domain.tables.records.PostTableRecord;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final DSLContext dsl;

    public PostTableRecord insert(PostTableRecord post) {
        return dsl.insertInto(PostTable.POST_TABLE)
                .set(dsl.newRecord(PostTable.POST_TABLE, post))
                .returning()
                .fetchOne()
                .into(PostTableRecord.class);
    }


    public List<PostTableRecord> findAllById(Integer id) {
        return dsl.selectFrom(PostTable.POST_TABLE)
                .where(PostTable.POST_TABLE.AUTHOR_ID.eq(id))
                .and(PostTable.POST_TABLE.IS_BLOCKED.eq(false))
                .and(PostTable.POST_TABLE.IS_DELETE.eq(false))
                .orderBy(PostTable.POST_TABLE.TIME.desc())
                .fetch();
    }


    public Boolean delete(Integer id) {
        return dsl.deleteFrom(PostTable.POST_TABLE)
                .where(PostTable.POST_TABLE.ID.eq(id))
                .execute() == 1;
    }

}

