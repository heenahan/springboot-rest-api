package com.prgms.springbootrestapi.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PersonnelTest {

    @ParameterizedTest
    @CsvSource(value = { "3,4,3", "4,2,1", "4,2,5" }, delimiter = ',')
    @DisplayName("최소 인원은 최대 인원보다 커서는 안되고 최적 인원은 최소, 최대 인원 사이여야 한다.")
    void invalidPersonnelTest(int max, int min, int best) {
        assertThatThrownBy(() -> {
            new Personnel(max, min, best);
        }).isExactlyInstanceOf(IllegalArgumentException.class);
    }

}