package ru.socialnet.team29.responses.dialog_response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class UnreadCount {
  private Integer count;
  private Integer recipientId;
}

