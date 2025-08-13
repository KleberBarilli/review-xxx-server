package com.idealizer.review_x.domain.game.entities.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public enum GameType {
    UNKNOWN(-1),

    MAIN_GAME(0),
    DLC(1),
    EXPANSION(2),
    BUNDLE(3),
    STANDALONE_EXPANSION(4),
    MOD(5),
    EPISODE(6),
    SEASON(7),
    REMAKE(8),
    REMASTER(9),
    EXPANDED_GAME(10),
    PORT(11),
    FORK(12),
    PACK_ADDON(13),
    UPDATE(14);

    private final int id;

    GameType(int id) {
        this.id = id;

    }

    public int id() { return id; }

    private static final Map<Integer, GameType> BY_ID =
            Stream.of(values()).collect(java.util.stream.Collectors.toUnmodifiableMap(
                    GameType::id, Function.identity(), (a, b) -> a));



    public static GameType fromId(int id) {
        return BY_ID.getOrDefault(id, UNKNOWN);
    }



}