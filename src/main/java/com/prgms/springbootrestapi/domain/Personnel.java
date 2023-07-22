package com.prgms.springbootrestapi.domain;

public record Personnel(
    int max,
    int min,
    int best
) {
    public Personnel {
        validMin(max, min);
        validBest(max, min, best);
    }
    private void validMin(int max, int min) {
        if (min > max) {  // 최대 인원수보다 크다면
            throw new IllegalArgumentException("최소 인원은 최대 인원보다 클 수 없습니다.");
        }
    }

    private void validBest(int max, int min, int best) {
        if (best < min || best > max) {
            throw new IllegalArgumentException("베스트 인원은 최소 인원보다 크고 최대 인원보다 작아야 합니다.");
        }
    }

}
