package com.idealizer.review_x.domain.game.entities.enums;

public enum GameCategory {
    UNKNOWN(-1),
    MAIN_GAME(0),
    DLC_ADDON(1),
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
    PACK(13),
    UPDATE(14);


    private final int igdbCategory;

    GameCategory(int igdbCategory) {
        this.igdbCategory = igdbCategory;
    }

    public static GameCategory fromIgdbCategory(int c) {
        for (GameCategory category : values()) {
            if (category.igdbCategory == c) {
                return category;
            }
        }
        return UNKNOWN;
    }
}
