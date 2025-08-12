package com.idealizer.review_x.domain.game.entities.enums;

public enum GameStatus {
    UNKNOWN(-1),
    RELEASED(0),
    ALPHA(2),
    BETA(3),
    EARLY_ACCESS(4),
    OFFLINE(5),
    CANCELLED(6),
    RUMORED(7),
    DELISTED(8);



    private final int igdbStatus;

    GameStatus(int igdbStatus) {
        this.igdbStatus = igdbStatus;
    }

    public static GameStatus fromIgdbStatus(int s) {
        for (GameStatus status : values()) {
            if (status.igdbStatus == s) {
                return status;
            }
        }
        return UNKNOWN;
    }
}
