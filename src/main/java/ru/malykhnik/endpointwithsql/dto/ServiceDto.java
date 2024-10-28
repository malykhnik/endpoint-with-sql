package ru.malykhnik.endpointwithsql.dto;

public record ServiceDto(String name, String status, CrudStatus crud_status) { }
