package com.prgms.springbootrestapi.domain;

import java.util.UUID;

public class BoardGame {

    private final UUID id;
    private String name; // 이름
    private Personnel personnel; // 정원 (최소, 최대, 추천)
    private Category category; // 카테고리
    private int playTime; // 플레이 타임(분)
    private int complexity; // 복잡도 1 - 5
    private String description; // 설명
    private Period period; // 생성 & 수정 시간

    public BoardGame(UUID id, String name, Personnel personnel, Category category, int playTime, int complexity, String description) {
        this.id = id;
        this.name = name;
        this.personnel = personnel;
        this.category = category;
        this.playTime = playTime;
        this.complexity = complexity;
        this.description = description;
    }

    public BoardGame(UUID id, String name, Personnel personnel, Category category, int playTime, int complexity, String description, Period period) {
        this.id = id;
        this.name = name;
        this.personnel = personnel;
        this.category = category;
        this.playTime = playTime;
        this.complexity = complexity;
        this.description = description;
        this.period = period;
    }

    public void updateAll(String name, Personnel personnel, Category category, int playTime, int complexity, String description) {
        this.name = name;
        this.personnel = personnel;
        this.category = category;
        this.playTime = playTime;
        this.complexity = complexity;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public Category getCategory() {
        return category;
    }

    public int getPlayTime() {
        return playTime;
    }

    public int getComplexity() {
        return complexity;
    }

    public String getDescription() {
        return description;
    }

    public Period getPeriod() { return period; }

}
