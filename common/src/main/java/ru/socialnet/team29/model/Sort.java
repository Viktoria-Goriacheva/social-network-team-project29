package ru.socialnet.team29.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Sort {

  public String direction;//"DESC" возьмем по-умолчанию
  public String property;//"time" возьмем по-умолчанию
  public boolean ignoreCase;//false(учтем оба варианта)
  public String nullHandling;//"NATIVE" возьмем по-умолчанию
  public boolean ascending;//false(прямая ASC)
  public boolean descending;//true(обратная DESC)
}
