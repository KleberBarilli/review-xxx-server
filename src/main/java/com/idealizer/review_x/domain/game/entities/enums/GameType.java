package com.idealizer.review_x.domain.game.entities.enums;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public enum GameType {
    UNKNOWN(-1, "Unknown"),

    MAIN_GAME(0, "Main Game"),
    DLC(1, "DLC"),
    EXPANSION(2, "Expansion"),
    BUNDLE(3, "Bundle"),
    STANDALONE_EXPANSION(4, "Standalone Expansion"),
    MOD(5, "Mod"),
    EPISODE(6, "Episode"),
    SEASON(7, "Season"),
    REMAKE(8, "Remake"),
    REMASTER(9, "Remaster"),
    EXPANDED_GAME(10, "Expanded Game"),
    PORT(11, "Port"),
    FORK(12, "Fork"),
    PACK_ADDON(13, "Pack / Addon"),
    UPDATE(14, "Update");

    private final int id;
    private final String label;

    GameType(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int id() { return id; }
    public String label() { return label; }

    private static final Map<Integer, GameType> BY_ID =
            Stream.of(values()).collect(java.util.stream.Collectors.toUnmodifiableMap(
                    GameType::id, Function.identity(), (a, b) -> a));

    private static final Map<String, GameType> BY_LABEL =
            Stream.of(values()).collect(java.util.stream.Collectors.toUnmodifiableMap(
                    c -> c.label.toLowerCase(), Function.identity(), (a, b) -> a));

    public static GameType fromId(int id) {
        return BY_ID.getOrDefault(id, UNKNOWN);
    }

    public static Optional<GameType> fromLabel(String label) {
        if (label == null) return Optional.empty();
        return Optional.ofNullable(BY_LABEL.get(label.toLowerCase()));
    }

    @Override public String toString() { return label; }
}