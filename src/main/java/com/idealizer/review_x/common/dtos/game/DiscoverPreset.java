package com.idealizer.review_x.common.dtos.game;

public enum DiscoverPreset {
    TOP_RATED(-1, null),

    STEAM_24_HOUR_PEAK_PLAYERS(5, 1),
    STEAM_POSITIVE_REVIEWS(6, 1),
    STEAM_NEGATIVE_REVIEWS(7, 1),
    STEAM_TOTAL_REVIEWS(8, 1),
    STEAM_GLOBAL_TOP_SELLERS(9, 1),
    STEAM_MOST_WISHLISTED_UPCOMING(10, 1),

    IGDB_VISITS(1, 121),
    IGDB_WANT_TO_PLAY(2, 121),
    IGDB_PLAYING(3, 121),
    IGDB_PLAYED(4, 121);

    private final int popularityTypeId;
    private final Integer popularitySourceId;

    DiscoverPreset(int popularityTypeId, Integer popularitySourceId) {
        this.popularityTypeId = popularityTypeId;
        this.popularitySourceId = popularitySourceId;
    }

    public int popularityTypeId() { return popularityTypeId; }
    public Integer popularitySourceId() { return popularitySourceId; }

    private static final java.util.Map<Integer, DiscoverPreset> BY_TYPE_ID =
            java.util.Arrays.stream(values())
                    .filter(p -> p.popularityTypeId > 0)
                    .collect(java.util.stream.Collectors.toMap(DiscoverPreset::popularityTypeId, p -> p));

    public static java.util.Optional<DiscoverPreset> fromPopularityTypeId(int typeId) {
        return java.util.Optional.ofNullable(BY_TYPE_ID.get(typeId));
    }
}
