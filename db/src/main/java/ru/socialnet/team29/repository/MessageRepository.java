package ru.socialnet.team29.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.socialnet.team29.domain.tables.MessageTable;
import ru.socialnet.team29.domain.tables.records.MessageTableRecord;
import ru.socialnet.team29.mappers.MessageDtoMapper;
import ru.socialnet.team29.responses.dialog_response.MessageDto;
import ru.socialnet.team29.responses.dialog_response.UnreadCount;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MessageRepository {
    private final DSLContext dsl;
    private final MessageDtoMapper messageDtoMapper;

    public MessageTableRecord insert(MessageTableRecord messageTableTableRecord) {
        return dsl.insertInto(MessageTable.MESSAGE_TABLE)
                .set(dsl.newRecord(MessageTable.MESSAGE_TABLE, messageTableTableRecord))
                .onDuplicateKeyUpdate()
                .set(dsl.newRecord(MessageTable.MESSAGE_TABLE, messageTableTableRecord))
                .returning()
                .fetchOne();
    }

    public List<MessageDto> getLastMessage(Long authorId) {
        String sqlQuery =
                "SELECT * FROM socialnet.message_table AS mt " +
                "WHERE mt.time IN (" +
                "   SELECT MAX(max_time) FROM (" +
                "       SELECT MAX(mt2.time) AS max_time, mt2.recipient_id AS recipient " +
                "       FROM socialnet.message_table AS mt2 " +
                "       WHERE mt2.author_id = {0} " +
                "       GROUP BY mt2.recipient_id " +
                "       UNION" +
                "       SELECT MAX(mt3.time) AS max_time, mt3.author_id AS recipient " +
                "       FROM socialnet.message_table AS mt3 " +
                "       WHERE mt3.recipient_id = {0} " +
                "       GROUP BY mt3.author_id " +
                "   ) AS mt4 " +
                "   GROUP BY recipient" +
                ")";
        List<MessageTableRecord> result = dsl.resultQuery(sqlQuery, authorId).fetchInto(MessageTableRecord.class);
        result.forEach(
                messageDto -> {
                    if (messageDto.getRecipientId().equals(authorId)) {
                        messageDto.setRecipientId(messageDto.getAuthorId());
                    }
                }
        );
        return result.stream().map(messageDtoMapper::MessageTableRecordsToMessageDto).collect(Collectors.toList());
    }

    public Map<Long, UnreadCount> getUnreadMessages(Long authorId) {
        MessageTable messageTable = MessageTable.MESSAGE_TABLE;
        return dsl
                .select(count(), messageTable.AUTHOR_ID.as(field("RECIPIENT_ID"))).from(messageTable)
                .where(messageTable.RECIPIENT_ID.eq(authorId))
                .groupBy(messageTable.READ_STATUS, messageTable.AUTHOR_ID)
                .having(messageTable.READ_STATUS.eq("SENT"))
                .fetchMap(field("RECIPIENT_ID", Long.class), UnreadCount.class);
    }

    public List<MessageDto> getFullDialogData(Long id, Long companionId) {
        MessageTable messageTable = MessageTable.MESSAGE_TABLE;
        var result = dsl
                .selectFrom(messageTable)
                .where(messageTable.AUTHOR_ID.eq(id).and(messageTable.RECIPIENT_ID.eq(companionId))
                        .or(messageTable.AUTHOR_ID.eq(companionId).and(messageTable.RECIPIENT_ID.eq(id)))
                )
                .offset(0)
                .limit(20)
                .fetchInto(MessageTableRecord.class);
        return result.stream().map(messageDtoMapper::MessageTableRecordsToMessageDto).collect(Collectors.toList());
    }

    public void setReadAllStatus(Long id, Long companionId) {
        MessageTable messageTable = MessageTable.MESSAGE_TABLE;
        dsl
                .update(messageTable)
                .set(messageTable.READ_STATUS, "READ")
                .where(messageTable.AUTHOR_ID.eq(id).and(messageTable.RECIPIENT_ID.eq(companionId))
                        .or(messageTable.AUTHOR_ID.eq(companionId).and(messageTable.RECIPIENT_ID.eq(id)))
                )
                .execute();
    }
}
