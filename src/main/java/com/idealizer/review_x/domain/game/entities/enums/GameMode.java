package com.idealizer.review_x.domain.game.entities.enums;

public enum GameMode { SINGLE_PLAYER(1), MULTIPLAYER(2), CO_OPERATIVE(3), SPLIT_SCREEN(4), MASSIVELY_MULTIPLAYER_ONLINE(5), BATTLE_ROYALE(6), OTHER(-1);


    private final int igdbId;

    GameMode(int igdbId) {
        this.igdbId = igdbId;
    }

    public int getIgdbId() {
        return igdbId;
    }

    public static GameMode fromIgdbId(int id) {
        for (GameMode mode : values()) {
            if (mode.igdbId == id) {
                return mode;
            }
        }
        return OTHER;
    }
}
