package com.idealizer.review_x.infra.processors.utils;

public final class NormalizeSlugs {
    private NormalizeSlugs() {}

    public static String normalize(String raw) {
        if (raw == null) return null;
        String s = java.text.Normalizer.normalize(raw, java.text.Normalizer.Form.NFKD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(java.util.Locale.ROOT)
                .replace('_', '-')
                .replaceAll("\\p{Pd}+", "-")
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("(^-|-$)", "");
        return s;
    }
}