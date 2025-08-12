package com.idealizer.review_x.infra.processors.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class TrailerPicker {
    private TrailerPicker() {}

    private static final Pattern OFFICIAL   = Pattern.compile("\\bofficial\\s+trailer\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern LAUNCH     = Pattern.compile("\\blaunch\\s+trailer\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern ANNOUNCE   = Pattern.compile("\\bannounce(?:ment)?\\s+trailer\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern STORY      = Pattern.compile("\\bstory\\s+trailer\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern REVEAL     = Pattern.compile("\\breveal\\s+trailer\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern FULL_EXT   = Pattern.compile("\\b(full|extended)\\s+trailer\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern TRAILER    = Pattern.compile("\\btrailer\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern GAMEPLAY   = Pattern.compile("\\bgameplay\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern WALKTHROUGH= Pattern.compile("\\bwalkthrough\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern DEV_DIARY  = Pattern.compile("\\bdev(?:eloper)?\\s+diary\\b|\\bbehind\\s+the\\s+scenes\\b|\\bdeep\\s+dive\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern COMMENTARY = Pattern.compile("\\bcommentary\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern PREVIEW    = Pattern.compile("\\bpreview\\b|\\bteaser\\b", Pattern.CASE_INSENSITIVE);

    public static <V> String pickBestTrailerUrl(List<V> videos,
                                                Function<V,String> nameFn,
                                                Function<V,String> idFn) {
        String id = pickBestTrailerId(videos, nameFn, idFn);
        return id == null ? null : "https://www.youtube.com/watch?v=" + id;
    }

    public static <V> String pickBestTrailerId(List<V> videos,
                                               Function<V,String> nameFn,
                                               Function<V,String> idFn) {
        if (videos == null || videos.isEmpty()) return null;

        return videos.stream()
                .filter(Objects::nonNull)
                .filter(v -> notBlank(idFn.apply(v)))
                .max(Comparator
                        .comparingInt(v -> score(nameFn.apply((V) v)))
                        .thenComparing(v -> contains(TRAILER, nameFn.apply((V) v)) ? 1 : 0)
                        .thenComparingInt(v -> -safeLen(nameFn.apply((V) v))))
                .map(idFn)
                .orElse(null);
    }

    private static int score(String name) {
        if (name == null || name.isBlank()) return 0;
        int s = 0;
        if (matches(OFFICIAL, name))  s += 1000;
        if (matches(LAUNCH, name))    s += 900;
        if (matches(ANNOUNCE, name))  s += 850;
        if (matches(STORY, name))     s += 820;
        if (matches(REVEAL, name))    s += 800;
        if (matches(FULL_EXT, name))  s += 150;
        if (matches(TRAILER, name))   s += 500;

        if (matches(GAMEPLAY, name))  s -= 350;
        if (matches(WALKTHROUGH, name)) s -= 400;
        if (matches(DEV_DIARY, name)) s -= 450;
        if (matches(COMMENTARY, name)) s -= 300;
        if (matches(PREVIEW, name))   s -= 200;
        return s;
    }

    private static boolean matches(Pattern p, String s) { return s != null && p.matcher(s).find(); }
    private static boolean contains(Pattern p, String s) { return matches(p, s); }
    private static boolean notBlank(String s) { return s != null && !s.isBlank(); }
    private static int safeLen(String s) { return s == null ? 0 : s.length(); }
}