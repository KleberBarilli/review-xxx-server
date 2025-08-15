package com.idealizer.review_x.common.helpers;

import com.ibm.icu.text.Transliterator;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

public final class NormalizeAliases {
    private NormalizeAliases() {}

    private static final Transliterator TO_ASCII =
            Transliterator.getInstance("Any-Latin; NFD; [:Nonspacing Mark:] Remove; NFC; Lower");

    private static final Pattern DASHES = Pattern.compile("[\\p{Pd}\\u2212\\u2010-\\u2015\\uFE58\\uFE63\\uFF0D]+");
    private static final Pattern SPACES = Pattern.compile("\\s+");
    private static final Pattern MULTI_HYPHEN = Pattern.compile("-{2,}");
    private static final Pattern TRIM_HYPHEN = Pattern.compile("(^-|-$)");

    private static final Pattern NOT_LETTER_NUMBER_OR_HYPHEN = Pattern.compile("[^\\p{L}\\p{N}-]+");
    private static final Pattern NOT_ASCII_SLUG = Pattern.compile("[^a-z0-9-]+");

    public static String unicodeSlug(String raw) {
        if (raw == null) return null;
        String s = Normalizer.normalize(raw, Normalizer.Form.NFKC)
                .toLowerCase(Locale.ROOT);
        s = DASHES.matcher(s).replaceAll("-");
        s = SPACES.matcher(s).replaceAll("-");
        s = NOT_LETTER_NUMBER_OR_HYPHEN.matcher(s).replaceAll("-");
        s = MULTI_HYPHEN.matcher(s).replaceAll("-");
        s = TRIM_HYPHEN.matcher(s).replaceAll("");
        return s.isEmpty() ? null : Normalizer.normalize(s, Normalizer.Form.NFC);
    }

    public static String asciiSlug(String raw) {
        if (raw == null) return null;
        String s = Normalizer.normalize(raw, Normalizer.Form.NFKC);
        s = TO_ASCII.transliterate(s);
        s = DASHES.matcher(s).replaceAll("-");
        s = SPACES.matcher(s).replaceAll("-");
        s = NOT_ASCII_SLUG.matcher(s).replaceAll("-");
        s = MULTI_HYPHEN.matcher(s).replaceAll("-");
        s = TRIM_HYPHEN.matcher(s).replaceAll("");
        return s.isEmpty() ? null : s;
    }
    public static List<String> slugCandidates(String raw) {
        LinkedHashSet<String> out = new LinkedHashSet<>();
        String ascii = asciiSlug(raw);
        String uni = unicodeSlug(raw);
        if (ascii != null) out.add(ascii);
        if (uni != null) out.add(uni);
        return new ArrayList<>(out);
    }
}
