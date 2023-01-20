package ru.socialnet.team29.repository;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.Person;
import ru.socialnet.team29.domain.tables.Post2tag;
import ru.socialnet.team29.domain.tables.PostTable;
import ru.socialnet.team29.domain.tables.Tag;
import ru.socialnet.team29.domain.tables.records.PostTableRecord;

@Repository
@RequiredArgsConstructor
public class PostRepository implements CrudRepository<PostTableRecord> {

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
    public PostTableRecord update(PostTableRecord post) {
        return dsl.update(PostTable.POST_TABLE)
                .set(PostTable.POST_TABLE.from(post))
                .where(PostTable.POST_TABLE.ID.eq(post.getId()))
                .returning()
                .fetchOne();
    }

    @Override
    public boolean delete(int id) {
        return dsl.update(PostTable.POST_TABLE)
                .set(PostTable.POST_TABLE.IS_DELETE, true)
                .where(PostTable.POST_TABLE.ID.eq(id))
                .execute() == 1;
    }
    public List<Integer> findPostIdsByAuthorWithPersons() {
        return dsl.selectFrom(PostTable.POST_TABLE)
            .where(PostTable.POST_TABLE.TYPE.eq("POSTED"))
            .and(PostTable.POST_TABLE.IS_BLOCKED.eq(false))
            .and(PostTable.POST_TABLE.IS_DELETE.eq(false))
            .orderBy(PostTable.POST_TABLE.TIME.desc())
            .fetch()
            .getValues(PostTable.POST_TABLE.ID);
    }

    public List<Integer> findPostIdsByAuthor(Integer authorId) {
        return dsl.selectFrom(PostTable.POST_TABLE)
                .where(PostTable.POST_TABLE.AUTHOR_ID.eq(authorId))
                .and(PostTable.POST_TABLE.IS_BLOCKED.eq(false))
                .and(PostTable.POST_TABLE.IS_DELETE.eq(false))
                .orderBy(PostTable.POST_TABLE.TIME.desc())
                .fetch()
                .getValues(PostTable.POST_TABLE.ID);
    }
    public  List<Integer> findPostIdsByTag(String tag) {
        return dsl.select(PostTable.POST_TABLE.ID).from(PostTable.POST_TABLE)
            .innerJoin(Post2tag.POST2TAG)
            .on(Post2tag.POST2TAG.POST_ID.eq(PostTable.POST_TABLE.ID))
            .innerJoin(Tag.TAG).on(Post2tag.POST2TAG.TAG_ID.eq(Tag.TAG.ID))
            .where(Tag.TAG.TAG_.likeIgnoreCase("%" + tag + "%"))
            .and(PostTable.POST_TABLE.TYPE.eq("POSTED"))
            .and(PostTable.POST_TABLE.IS_BLOCKED.eq(false))
            .and(PostTable.POST_TABLE.IS_DELETE.eq(false))
            .fetch().getValues(PostTable.POST_TABLE.ID);
    }

  public List<Integer> search(
        String text,
        OffsetDateTime timeFrom,
        OffsetDateTime timeTo,
        String author){
        return dsl.select(PostTable.POST_TABLE.ID).from(PostTable.POST_TABLE)
            .innerJoin(Person.PERSON)
            .on(Person.PERSON.ID.eq(PostTable.POST_TABLE.AUTHOR_ID))
            .where(PostTable.POST_TABLE.TIME.between(timeFrom,timeTo))
            .and(PostTable.POST_TABLE.TITLE.likeIgnoreCase("%" + text + "%")
                .or(PostTable.POST_TABLE.POST_TEXT.likeIgnoreCase("%" + text + "%")))
            .and(PostTable.POST_TABLE.TYPE.eq("POSTED"))
            .and(PostTable.POST_TABLE.IS_BLOCKED.eq(false))
            .and(PostTable.POST_TABLE.IS_DELETE.eq(false))
            .and(Person.PERSON.FIRST_NAME.likeIgnoreCase("%" + author + "%")
                .or(Person.PERSON.LAST_NAME.likeIgnoreCase("%" + author + "%")))
            .orderBy(PostTable.POST_TABLE.TIME.desc())
            .fetch()
            .getValues(PostTable.POST_TABLE.ID);
    }
}

