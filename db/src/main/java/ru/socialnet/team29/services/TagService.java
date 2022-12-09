package ru.socialnet.team29.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.socialnet.team29.domain.tables.records.TagRecord;
import ru.socialnet.team29.repository.TagRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    public List<String> findAllTagsByPostId(int id) {
        return tagRepository.findAllTagsByPostId(id);
    }


    /**
     * Просто добавляет тэг в базу, без проверки.
     * @param tag имя тега
     * @return идентификатор созданной записи
     */
    public int addNewTag(String tag) {
        TagRecord tagRecord = new TagRecord();
        tagRecord.setTag(tag);
        return tagRepository.insert(tagRecord);
    }

    /**
     * Проверяет, существует ли в базе такой тэг.
     * Если есть, то возвращает его id.
     * Если нет, добавляет тэг в базу и возвращает новый id.
     * @param tag имя тега
     * @return идентификатор созданной или существующей записи
     */
    public int getTagId(String tag){
        int tadId = tagRepository.findByTag(tag);
        return tadId > 0 ? tadId : addNewTag(tag);
    }

}
