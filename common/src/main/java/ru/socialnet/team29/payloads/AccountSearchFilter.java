package ru.socialnet.team29.payloads;

import lombok.Data;

@Data
public class AccountSearchFilter {
    private AccountSearchPayload accountSearchPayload;
    private int pageSize;
    private int pageNumber;
}
