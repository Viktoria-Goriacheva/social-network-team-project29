package ru.socialnet.team29.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorageImageDto {
    private String imagePath;
}
