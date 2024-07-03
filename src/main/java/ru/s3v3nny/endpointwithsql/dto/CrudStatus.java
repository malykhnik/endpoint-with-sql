package ru.s3v3nny.endpointwithsql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrudStatus {
    Boolean create;
    Boolean read;
    Boolean update;
    Boolean delete;
}
