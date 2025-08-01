package com.idealizer.review_x.domain.game.entities;

public enum GamePlatform {
    XBOX_360(12),
    WII_U(41),
    PLAYSTATION_5(167),
    PLAYSTATION_VR2(390),
    PLAYSTATION_VR(165),
    PLAYSTATION_4(48),
    LINUX(3),
    PC_MICROSOFT_WINDOWS(6),
    PLAYSTATION_2(8),
    IOS(39),
    GAME_BOY_ADVANCE(24),
    PLAYSTATION_3(9),
    XBOX(11),
    NINTENDO_DS(20),
    GAME_BOY_COLOR(22),
    PLAYSTATION_VITA(46),
    NINTENDO_ENTERTAINMENT_SYSTEM(18),
    NINTENDO_GAMECUBE(21),
    GAME_BOY(33),
    PLAYSTATION_PORTABLE(38),
    NEW_NINTENDO_3DS(137),
    GOOGLE_STADIA(170),
    NINTENDO_3DS(37),
    XBOX_SERIES_XS(169),
    PLAYSTATION(7),
    ANDROID(34),
    NINTENDO_64(4),
    WII(5),
    MAC(14),
    SUPER_NINTENDO_ENTERTAINMENT_SYSTEM(19),
    XBOX_ONE(49),
    NINTENDO_SWITCH(130),
    OTHER(-1);

    private final int igdbId;

    GamePlatform(int igdbId) {
        this.igdbId = igdbId;
    }

    public int getIgdbId() {
        return igdbId;
    }

    public static GamePlatform fromIgdbId(int id) {
        for (GamePlatform platform : values()) {
            if (platform.igdbId == id) {
                return platform;
            }
        }
        return OTHER;
    }
}
