package com.idealizer.review_x.domain.game.entities.enums;

public enum GameGenre {
    POINT_AND_CLICK(2),
    FIGHTING(4),
    SHOOTER(5),
    MUSIC(7),
    PLATFORM(8),
    PUZZLE(9),
    RACING(10),
    REAL_TIME_STRATEGY_RTS(11),
    ROLE_PLAYING_RPG(12),
    SIMULATOR(13),
    SPORT(14),
    STRATEGY(15),
    TURN_BASED_STRATEGY_TBS(16),
    TACTICAL(24),
    HACK_AND_SLASH_BEAT_EM_UP(25),
    QUIZ_TRIVIA(26),
    PINBALL(30),
    ADVENTURE(31),
    INDIE(32),
    ARCADE(33),
    VISUAL_NOVEL(34),
    CARD_AND_BOARD_GAME(35),
    MOBA(36),
    OTHER(-1);

    private final int igdbId;

    GameGenre(int igdbId) {
        this.igdbId = igdbId;
    }

    public static GameGenre fromIgdbId(int id) {
        for (GameGenre genre : values()) {
            if (genre.igdbId == id) {
                return genre;
            }
        }
        return OTHER;
    }
}
