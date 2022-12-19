package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.PostFile;
import ru.socialnet.team29.domain.tables.records.PostFileRecord;

@Repository
@RequiredArgsConstructor
public class PostFileRepository implements CrudRepository<PostFileRecord> {

    private DSLContext dsl;

    @Autowired
    public void setDsl(@Lazy DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public int insert(PostFileRecord postFileRecord) {
        return dsl.insertInto(PostFile.POST_FILE)
                .set(dsl.newRecord(PostFile.POST_FILE, postFileRecord))
                .returning()
                .fetchOne()
                .getId();
    }

    @Override
    public PostFileRecord findById(int id) {
        return dsl.selectFrom(PostFile.POST_FILE)
                .where(PostFile.POST_FILE.ID.eq(id))
                .fetchOne();
    }

    @Override
    public PostFileRecord update(PostFileRecord postFileRecord) {
        return dsl.update(PostFile.POST_FILE)
                .set(PostFile.POST_FILE.from(postFileRecord))
                .where(PostFile.POST_FILE.ID.eq(postFileRecord.getId()))
                .returning()
                .fetchOne();
    }


    @Override
    public boolean delete(int id) {
        return dsl.deleteFrom(PostFile.POST_FILE)
                .where(PostFile.POST_FILE.ID.eq(id))
                .execute() == 1;
    }

    /**
     * Ищет в базе запись PostFileRecord, связанную с постом id = ${postId} <br/>
     * Если не найдено, то возвращает запись с нулевыми полями
     *
     * @param postId идентификатор поста
     * @return существующий в базе или пустой экземпляр
     */
    public PostFileRecord findByPostId(int postId) {
        return dsl.selectFrom(PostFile.POST_FILE)
                .where(PostFile.POST_FILE.POST_ID.eq(postId))
                .fetchOptional()
                .orElse(getEmptyPostFileRecord());
    }

    private PostFileRecord getEmptyPostFileRecord() {
        return new PostFileRecord(0, 0, "", "");
    }

}
