package com.idealizer.review_x.infra.processors.utils;

public final class NormalizeSlugs {
    private NormalizeSlugs() {}

    public static String normalize(String raw) {
        if (raw == null) return null;

        String s = java.text.Normalizer.normalize(raw, java.text.Normalizer.Form.NFKD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(java.util.Locale.ROOT);
        s = s.replaceAll("[\\p{Pd}\\u2212\\u2010-\\u2015\\uFE58\\uFE63\\uFF0D]", "-");

        s = s.replace('_', '-')
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9-]+", "-");

        return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFC);
    }
}