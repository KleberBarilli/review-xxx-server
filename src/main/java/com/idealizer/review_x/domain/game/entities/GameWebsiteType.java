package com.idealizer.review_x.domain.game.entities;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public enum GameWebsiteType {
    UNKNOWN(0, "Unknown"),

    OFFICIAL_WEBSITE(1, "Official Website"),
    COMMUNITY_WIKI(2, "Community Wiki"),
    WIKIPEDIA(3, "Wikipedia"),
    FACEBOOK(4, "Facebook"),
    TWITTER(5, "Twitter"),
    TWITCH(6, "Twitch"),
    INSTAGRAM(8, "Instagram"),
    YOUTUBE(9, "YouTube"),
    APP_STORE_IPHONE(10, "App Store (iPhone)"),
    APP_STORE_IPAD(11, "App Store (iPad)"),
    GOOGLE_PLAY(12, "Google Play"),
    STEAM(13, "Steam"),
    SUBREDDIT(14, "Subreddit"),
    ITCH(15, "Itch"),
    EPIC_GAMES_STORE(16, "Epic"),
    GOG(17, "GOG"),
    DISCORD(18, "Discord"),
    BLUESKY(19, "Bluesky"),
    XBOX(22, "Xbox"),
    PLAYSTATION(23, "Playstation"),
    NINTENDO(24, "Nintendo"),
    META(25, "Meta");

    private final int id;
    private final String label;

    GameWebsiteType(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int id() { return id; }
    public String label() { return label; }

    private static final Map<Integer, GameWebsiteType> BY_ID =
            Stream.of(values()).collect(java.util.stream.Collectors.toUnmodifiableMap(
                    GameWebsiteType::id, Function.identity(), (a, b) -> a));

    private static final Map<String, GameWebsiteType> BY_LABEL =
            Stream.of(values()).collect(java.util.stream.Collectors.toUnmodifiableMap(
                    e -> e.label.toLowerCase(), Function.identity(), (a, b) -> a));

    public static GameWebsiteType fromId(int id) {
        return BY_ID.getOrDefault(id, UNKNOWN);
    }

    public static Optional<GameWebsiteType> fromLabel(String label) {
        if (label == null) return Optional.empty();
        return Optional.ofNullable(BY_LABEL.get(label.toLowerCase()));
    }



    @Override public String toString() { return label; }
}