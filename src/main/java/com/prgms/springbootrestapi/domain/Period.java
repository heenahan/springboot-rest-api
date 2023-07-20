package com.prgms.springbootrestapi.domain;

import java.time.LocalDateTime;

public record Period(
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
