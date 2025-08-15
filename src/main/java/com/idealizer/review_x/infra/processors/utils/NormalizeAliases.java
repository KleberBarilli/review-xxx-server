package com.idealizer.review_x.infra.processors.utils;

import com.ibm.icu.text.Transliterator;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

public final class NormalizeAliases {
    private NormalizeAliases() {}

    // ICU4J: transforma qualquer script em latino e depois em ASCII
    // Ex.: 汉字 -> Hanzi,  ゼルダ -> Zeruda,  Ведьмак -> Vedmak
    private static final Transliterator TO_ASCII =
            Transliterator.getInstance("Any-Latin; NFD; [:Nonspacing Mark:] Remove; NFC; Lower");

    // Conjuntos de regex pré-compilados (performance)
    private static final Pattern DASHES = Pattern.compile("[\\p{Pd}\\u2212\\u2010-\\u2015\\uFE58\\uFE63\\uFF0D]+");
    private static final Pattern SPACES = Pattern.compile("\\s+");
    private static final Pattern MULTI_HYPHEN = Pattern.compile("-{2,}");
    private static final Pattern TRIM_HYPHEN = Pattern.compile("(^-|-$)");

    // Para Unicode slug: mantemos letras e números de qualquer alfabeto + hífen
    // Substituímos o resto por hífen.
    private static final Pattern NOT_LETTER_NUMBER_OR_HYPHEN = Pattern.compile("[^\\p{L}\\p{N}-]+");

    // Para ASCII slug: depois da transliteração, só mantemos a-z0-9-.
    private static final Pattern NOT_ASCII_SLUG = Pattern.compile("[^a-z0-9-]+");

    /** Slug Unicode: preserva CJK, cirílico, árabe etc. */
    public static String unicodeSlug(String raw) {
        if (raw == null) return null;
        String s = Normalizer.normalize(raw, Normalizer.Form.NFKC)
                .toLowerCase(Locale.ROOT);
        s = DASHES.matcher(s).replaceAll("-");     // normaliza todos os traços em '-'
        s = SPACES.matcher(s).replaceAll("-");     // espaços -> '-'
        s = NOT_LETTER_NUMBER_OR_HYPHEN.matcher(s).replaceAll("-"); // símbolos -> '-'
        s = MULTI_HYPHEN.matcher(s).replaceAll("-");
        s = TRIM_HYPHEN.matcher(s).replaceAll("");
        return s.isEmpty() ? null : Normalizer.normalize(s, Normalizer.Form.NFC);
    }

    /** Slug ASCII: translitera tudo para latino e remove acentos/símbolos. */
    public static String asciiSlug(String raw) {
        if (raw == null) return null;
        String s = Normalizer.normalize(raw, Normalizer.Form.NFKC);
        s = TO_ASCII.transliterate(s);             // romaniza + remove acentos + lowercase
        s = DASHES.matcher(s).replaceAll("-");
        s = SPACES.matcher(s).replaceAll("-");
        s = NOT_ASCII_SLUG.matcher(s).replaceAll("-");
        s = MULTI_HYPHEN.matcher(s).replaceAll("-");
        s = TRIM_HYPHEN.matcher(s).replaceAll("");
        return s.isEmpty() ? null : s;
    }

    /** Retorna candidatos de slug (dedup): [ASCII, Unicode] ou só o que existir. */
    public static List<String> slugCandidates(String raw) {
        LinkedHashSet<String> out = new LinkedHashSet<>();
        String ascii = asciiSlug(raw);
        String uni = unicodeSlug(raw);
        if (ascii != null) out.add(ascii);
        if (uni != null) out.add(uni);
        return new ArrayList<>(out);
    }
}
