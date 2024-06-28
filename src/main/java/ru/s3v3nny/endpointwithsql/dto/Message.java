package ru.s3v3nny.endpointwithsql.dto;

import java.util.List;

public record Message (String token, List<ServiceDto> services) { }
