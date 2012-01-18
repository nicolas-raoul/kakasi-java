/*
 * $Id: KanaToRomaConverterImpl.java,v 1.1 2003/03/01 12:52:26 kawao Exp $
 *
 * KAKASI/JAVA
 *  Copyright (C) 2002-2003  KAWAO, Tomoyuki (kawao@kawao.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.kawao.kakasi;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * This class implements conversion methods that converts a Hiranana/Katakana
 * word to Romaji.
 *
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.1 $ $Date: 2003/03/01 12:52:26 $
 */
class KanaToRomaConverterImpl {

    private static class Table {

        final Map table = new HashMap();

        void add(String kana, String romaji) {
            Character key = new Character(kana.charAt(0));
            Entry newEntry = new Entry(kana.substring(1), romaji);

            List list = (List)table.get(key);
            if (list == null) {
                list = new LinkedList();
                list.add(newEntry);
                table.put(key, list);
            } else {
                int newLength = newEntry.getKanaLength();
                ListIterator iterator = list.listIterator();
                while (iterator.hasNext()) {
                    Entry entry = (Entry)iterator.next();
                    if (newLength >= entry.getKanaLength()) {
                        iterator.previous();
                        break;
                    }
                }
                iterator.add(newEntry);
            }
        }

        String get(KanjiInput input) throws IOException {
            int ch = input.get();
            if (ch < 0) {
                return null;
            }
            List list = (List)table.get(new Character((char)ch));
            if (list == null) {
                return null;
            }
            String rest = null;
            int restLength = 0;
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry)iterator.next();
                int length = entry.getKanaLength();
                if (length > 0 && rest == null) {
                    char[] chars = new char[length];
                    restLength = input.more(chars);
                    rest = new String(chars, 0, restLength);
                }
                if (length > restLength) {
                    continue;
                }
                String romaji = entry.getRomajiFor(rest);
                if (romaji != null) {
                    input.consume(length + 1);
                    return romaji;
                }
            }
            return null;
        }

    }

    private static class Entry {

        final String kana;
        final String romaji;

        Entry(String kana, String romaji) {
            this.kana = kana;
            this.romaji = romaji;
        }

        int getKanaLength() {
            return kana.length();
        }

        String getRomajiFor(String string) {
            return
                getKanaLength() == 0 ||
                (string != null && string.startsWith(kana)) ? romaji : null;
        }

    }

    private static Table hiraganaToHepburn;
    private static Table hiraganaToKunrei;
    private static Table katakanaToHepburn;
    private static Table katakanaToKunrei;

    /**
     * Gets the Hiragana to Hepburn type romaji conversion table.
     */
    private static Table getHiraganaToHepburnTable() {
        if (hiraganaToHepburn == null) {
            hiraganaToHepburn = new Table();
            hiraganaToHepburn.add("\u3041", "a");
            hiraganaToHepburn.add("\u3042", "a");
            hiraganaToHepburn.add("\u3043", "i");
            hiraganaToHepburn.add("\u3044", "i");
            hiraganaToHepburn.add("\u3045", "u");
            hiraganaToHepburn.add("\u3046", "u");
            hiraganaToHepburn.add("\u3046\u309b", "vu");
            hiraganaToHepburn.add("\u3046\u309b\u3041", "va");
            hiraganaToHepburn.add("\u3046\u309b\u3043", "vi");
            hiraganaToHepburn.add("\u3046\u309b\u3047", "ve");
            hiraganaToHepburn.add("\u3046\u309b\u3049", "vo");
            hiraganaToHepburn.add("\u3047", "e");
            hiraganaToHepburn.add("\u3048", "e");
            hiraganaToHepburn.add("\u3049", "o");
            hiraganaToHepburn.add("\u304a", "o");
            hiraganaToHepburn.add("\u304b", "ka");
            hiraganaToHepburn.add("\u304c", "ga");
            hiraganaToHepburn.add("\u304d", "ki");
            hiraganaToHepburn.add("\u304d\u3083", "kya");
            hiraganaToHepburn.add("\u304d\u3085", "kyu");
            hiraganaToHepburn.add("\u304d\u3087", "kyo");
            hiraganaToHepburn.add("\u304e", "gi");
            hiraganaToHepburn.add("\u304e\u3083", "gya");
            hiraganaToHepburn.add("\u304e\u3085", "gyu");
            hiraganaToHepburn.add("\u304e\u3087", "gyo");
            hiraganaToHepburn.add("\u304f", "ku");
            hiraganaToHepburn.add("\u3050", "gu");
            hiraganaToHepburn.add("\u3051", "ke");
            hiraganaToHepburn.add("\u3052", "ge");
            hiraganaToHepburn.add("\u3053", "ko");
            hiraganaToHepburn.add("\u3054", "go");
            hiraganaToHepburn.add("\u3055", "sa");
            hiraganaToHepburn.add("\u3056", "za");
            hiraganaToHepburn.add("\u3057", "shi");
            hiraganaToHepburn.add("\u3057\u3083", "sha");
            hiraganaToHepburn.add("\u3057\u3085", "shu");
            hiraganaToHepburn.add("\u3057\u3087", "sho");
            hiraganaToHepburn.add("\u3058", "ji");
            hiraganaToHepburn.add("\u3058\u3083", "ja");
            hiraganaToHepburn.add("\u3058\u3085", "ju");
            hiraganaToHepburn.add("\u3058\u3087", "jo");
            hiraganaToHepburn.add("\u3059", "su");
            hiraganaToHepburn.add("\u305a", "zu");
            hiraganaToHepburn.add("\u305b", "se");
            hiraganaToHepburn.add("\u305c", "ze");
            hiraganaToHepburn.add("\u305d", "so");
            hiraganaToHepburn.add("\u305e", "zo");
            hiraganaToHepburn.add("\u305f", "ta");
            hiraganaToHepburn.add("\u3060", "da");
            hiraganaToHepburn.add("\u3061", "chi");
            hiraganaToHepburn.add("\u3061\u3083", "cha");
            hiraganaToHepburn.add("\u3061\u3085", "chu");
            hiraganaToHepburn.add("\u3061\u3087", "cho");
            hiraganaToHepburn.add("\u3062", "di");
            hiraganaToHepburn.add("\u3062\u3083", "dya");
            hiraganaToHepburn.add("\u3062\u3085", "dyu");
            hiraganaToHepburn.add("\u3062\u3087", "dyo");
            hiraganaToHepburn.add("\u3063", "tsu");
            hiraganaToHepburn.add("\u3063\u3046\u309b", "vvu");
            hiraganaToHepburn.add("\u3063\u3046\u309b\u3041", "vva");
            hiraganaToHepburn.add("\u3063\u3046\u309b\u3043", "vvi");
            hiraganaToHepburn.add("\u3063\u3046\u309b\u3047", "vve");
            hiraganaToHepburn.add("\u3063\u3046\u309b\u3049", "vvo");
            hiraganaToHepburn.add("\u3063\u304b", "kka");
            hiraganaToHepburn.add("\u3063\u304c", "gga");
            hiraganaToHepburn.add("\u3063\u304d", "kki");
            hiraganaToHepburn.add("\u3063\u304d\u3083", "kkya");
            hiraganaToHepburn.add("\u3063\u304d\u3085", "kkyu");
            hiraganaToHepburn.add("\u3063\u304d\u3087", "kkyo");
            hiraganaToHepburn.add("\u3063\u304e", "ggi");
            hiraganaToHepburn.add("\u3063\u304e\u3083", "ggya");
            hiraganaToHepburn.add("\u3063\u304e\u3085", "ggyu");
            hiraganaToHepburn.add("\u3063\u304e\u3087", "ggyo");
            hiraganaToHepburn.add("\u3063\u304f", "kku");
            hiraganaToHepburn.add("\u3063\u3050", "ggu");
            hiraganaToHepburn.add("\u3063\u3051", "kke");
            hiraganaToHepburn.add("\u3063\u3052", "gge");
            hiraganaToHepburn.add("\u3063\u3053", "kko");
            hiraganaToHepburn.add("\u3063\u3054", "ggo");
            hiraganaToHepburn.add("\u3063\u3055", "ssa");
            hiraganaToHepburn.add("\u3063\u3056", "zza");
            hiraganaToHepburn.add("\u3063\u3057", "sshi");
            hiraganaToHepburn.add("\u3063\u3057\u3083", "ssha");
            hiraganaToHepburn.add("\u3063\u3057\u3085", "sshu");
            hiraganaToHepburn.add("\u3063\u3057\u3087", "ssho");
            hiraganaToHepburn.add("\u3063\u3058", "jji");
            hiraganaToHepburn.add("\u3063\u3058\u3083", "jja");
            hiraganaToHepburn.add("\u3063\u3058\u3085", "jju");
            hiraganaToHepburn.add("\u3063\u3058\u3087", "jjo");
            hiraganaToHepburn.add("\u3063\u3059", "ssu");
            hiraganaToHepburn.add("\u3063\u305a", "zzu");
            hiraganaToHepburn.add("\u3063\u305b", "sse");
            hiraganaToHepburn.add("\u3063\u305c", "zze");
            hiraganaToHepburn.add("\u3063\u305d", "sso");
            hiraganaToHepburn.add("\u3063\u305e", "zzo");
            hiraganaToHepburn.add("\u3063\u305f", "tta");
            hiraganaToHepburn.add("\u3063\u3060", "dda");
            hiraganaToHepburn.add("\u3063\u3061", "cchi");
            hiraganaToHepburn.add("\u3063\u3061\u3083", "ccha");
            hiraganaToHepburn.add("\u3063\u3061\u3085", "cchu");
            hiraganaToHepburn.add("\u3063\u3061\u3087", "ccho");
            hiraganaToHepburn.add("\u3063\u3062", "ddi");
            hiraganaToHepburn.add("\u3063\u3062\u3083", "ddya");
            hiraganaToHepburn.add("\u3063\u3062\u3085", "ddyu");
            hiraganaToHepburn.add("\u3063\u3062\u3087", "ddyo");
            hiraganaToHepburn.add("\u3063\u3064", "ttsu");
            hiraganaToHepburn.add("\u3063\u3065", "ddu");
            hiraganaToHepburn.add("\u3063\u3066", "tte");
            hiraganaToHepburn.add("\u3063\u3067", "dde");
            hiraganaToHepburn.add("\u3063\u3068", "tto");
            hiraganaToHepburn.add("\u3063\u3069", "ddo");
            hiraganaToHepburn.add("\u3063\u306f", "hha");
            hiraganaToHepburn.add("\u3063\u3070", "bba");
            hiraganaToHepburn.add("\u3063\u3071", "ppa");
            hiraganaToHepburn.add("\u3063\u3072", "hhi");
            hiraganaToHepburn.add("\u3063\u3072\u3083", "hhya");
            hiraganaToHepburn.add("\u3063\u3072\u3085", "hhyu");
            hiraganaToHepburn.add("\u3063\u3072\u3087", "hhyo");
            hiraganaToHepburn.add("\u3063\u3073", "bbi");
            hiraganaToHepburn.add("\u3063\u3073\u3083", "bbya");
            hiraganaToHepburn.add("\u3063\u3073\u3085", "bbyu");
            hiraganaToHepburn.add("\u3063\u3073\u3087", "bbyo");
            hiraganaToHepburn.add("\u3063\u3074", "ppi");
            hiraganaToHepburn.add("\u3063\u3074\u3083", "ppya");
            hiraganaToHepburn.add("\u3063\u3074\u3085", "ppyu");
            hiraganaToHepburn.add("\u3063\u3074\u3087", "ppyo");
            hiraganaToHepburn.add("\u3063\u3075", "ffu");
            hiraganaToHepburn.add("\u3063\u3075\u3041", "ffa");
            hiraganaToHepburn.add("\u3063\u3075\u3043", "ffi");
            hiraganaToHepburn.add("\u3063\u3075\u3047", "ffe");
            hiraganaToHepburn.add("\u3063\u3075\u3049", "ffo");
            hiraganaToHepburn.add("\u3063\u3076", "bbu");
            hiraganaToHepburn.add("\u3063\u3077", "ppu");
            hiraganaToHepburn.add("\u3063\u3078", "hhe");
            hiraganaToHepburn.add("\u3063\u3079", "bbe");
            hiraganaToHepburn.add("\u3063\u307a", "ppe");
            hiraganaToHepburn.add("\u3063\u307b", "hho");
            hiraganaToHepburn.add("\u3063\u307c", "bbo");
            hiraganaToHepburn.add("\u3063\u307d", "ppo");
            hiraganaToHepburn.add("\u3063\u3084", "yya");
            hiraganaToHepburn.add("\u3063\u3086", "yyu");
            hiraganaToHepburn.add("\u3063\u3088", "yyo");
            hiraganaToHepburn.add("\u3063\u3089", "rra");
            hiraganaToHepburn.add("\u3063\u308a", "rri");
            hiraganaToHepburn.add("\u3063\u308a\u3083", "rrya");
            hiraganaToHepburn.add("\u3063\u308a\u3085", "rryu");
            hiraganaToHepburn.add("\u3063\u308a\u3087", "rryo");
            hiraganaToHepburn.add("\u3063\u308b", "rru");
            hiraganaToHepburn.add("\u3063\u308c", "rre");
            hiraganaToHepburn.add("\u3063\u308d", "rro");
            hiraganaToHepburn.add("\u3064", "tsu");
            hiraganaToHepburn.add("\u3065", "du");
            hiraganaToHepburn.add("\u3066", "te");
            hiraganaToHepburn.add("\u3067", "de");
            hiraganaToHepburn.add("\u3068", "to");
            hiraganaToHepburn.add("\u3069", "do");
            hiraganaToHepburn.add("\u306a", "na");
            hiraganaToHepburn.add("\u306b", "ni");
            hiraganaToHepburn.add("\u306b\u3083", "nya");
            hiraganaToHepburn.add("\u306b\u3085", "nyu");
            hiraganaToHepburn.add("\u306b\u3087", "nyo");
            hiraganaToHepburn.add("\u306c", "nu");
            hiraganaToHepburn.add("\u306d", "ne");
            hiraganaToHepburn.add("\u306e", "no");
            hiraganaToHepburn.add("\u306f", "ha");
            hiraganaToHepburn.add("\u3070", "ba");
            hiraganaToHepburn.add("\u3071", "pa");
            hiraganaToHepburn.add("\u3072", "hi");
            hiraganaToHepburn.add("\u3072\u3083", "hya");
            hiraganaToHepburn.add("\u3072\u3085", "hyu");
            hiraganaToHepburn.add("\u3072\u3087", "hyo");
            hiraganaToHepburn.add("\u3073", "bi");
            hiraganaToHepburn.add("\u3073\u3083", "bya");
            hiraganaToHepburn.add("\u3073\u3085", "byu");
            hiraganaToHepburn.add("\u3073\u3087", "byo");
            hiraganaToHepburn.add("\u3074", "pi");
            hiraganaToHepburn.add("\u3074\u3083", "pya");
            hiraganaToHepburn.add("\u3074\u3085", "pyu");
            hiraganaToHepburn.add("\u3074\u3087", "pyo");
            hiraganaToHepburn.add("\u3075", "fu");
            hiraganaToHepburn.add("\u3075\u3041", "fa");
            hiraganaToHepburn.add("\u3075\u3043", "fi");
            hiraganaToHepburn.add("\u3075\u3047", "fe");
            hiraganaToHepburn.add("\u3075\u3049", "fo");
            hiraganaToHepburn.add("\u3076", "bu");
            hiraganaToHepburn.add("\u3077", "pu");
            hiraganaToHepburn.add("\u3078", "he");
            hiraganaToHepburn.add("\u3079", "be");
            hiraganaToHepburn.add("\u307a", "pe");
            hiraganaToHepburn.add("\u307b", "ho");
            hiraganaToHepburn.add("\u307c", "bo");
            hiraganaToHepburn.add("\u307d", "po");
            hiraganaToHepburn.add("\u307e", "ma");
            hiraganaToHepburn.add("\u307f", "mi");
            hiraganaToHepburn.add("\u307f\u3083", "mya");
            hiraganaToHepburn.add("\u307f\u3085", "myu");
            hiraganaToHepburn.add("\u307f\u3087", "myo");
            hiraganaToHepburn.add("\u3080", "mu");
            hiraganaToHepburn.add("\u3081", "me");
            hiraganaToHepburn.add("\u3082", "mo");
            hiraganaToHepburn.add("\u3083", "ya");
            hiraganaToHepburn.add("\u3084", "ya");
            hiraganaToHepburn.add("\u3085", "yu");
            hiraganaToHepburn.add("\u3086", "yu");
            hiraganaToHepburn.add("\u3087", "yo");
            hiraganaToHepburn.add("\u3088", "yo");
            hiraganaToHepburn.add("\u3089", "ra");
            hiraganaToHepburn.add("\u308a", "ri");
            hiraganaToHepburn.add("\u308a\u3083", "rya");
            hiraganaToHepburn.add("\u308a\u3085", "ryu");
            hiraganaToHepburn.add("\u308a\u3087", "ryo");
            hiraganaToHepburn.add("\u308b", "ru");
            hiraganaToHepburn.add("\u308c", "re");
            hiraganaToHepburn.add("\u308d", "ro");
            hiraganaToHepburn.add("\u308e", "wa");
            hiraganaToHepburn.add("\u308f", "wa");
            hiraganaToHepburn.add("\u3090", "i");
            hiraganaToHepburn.add("\u3091", "e");
            hiraganaToHepburn.add("\u3092", "wo");
            hiraganaToHepburn.add("\u3093", "n");
            hiraganaToHepburn.add("\u3093\u3042", "n'a");
            hiraganaToHepburn.add("\u3093\u3044", "n'i");
            hiraganaToHepburn.add("\u3093\u3046", "n'u");
            hiraganaToHepburn.add("\u3093\u3048", "n'e");
            hiraganaToHepburn.add("\u3093\u304a", "n'o");
        }
        return hiraganaToHepburn;
    }

    /**
     * Gets the Hiragana to Kunrei type romaji convertesion table.
     */
    private static Table getHiraganaToKunreiTable() {
        if (hiraganaToKunrei == null) {
            hiraganaToKunrei = new Table();
            hiraganaToKunrei.add("\u3041", "a");
            hiraganaToKunrei.add("\u3042", "a");
            hiraganaToKunrei.add("\u3043", "i");
            hiraganaToKunrei.add("\u3044", "i");
            hiraganaToKunrei.add("\u3045", "u");
            hiraganaToKunrei.add("\u3046", "u");
            hiraganaToKunrei.add("\u3046\u309b", "vu");
            hiraganaToKunrei.add("\u3046\u309b\u3041", "va");
            hiraganaToKunrei.add("\u3046\u309b\u3043", "vi");
            hiraganaToKunrei.add("\u3046\u309b\u3047", "ve");
            hiraganaToKunrei.add("\u3046\u309b\u3049", "vo");
            hiraganaToKunrei.add("\u3047", "e");
            hiraganaToKunrei.add("\u3048", "e");
            hiraganaToKunrei.add("\u3049", "o");
            hiraganaToKunrei.add("\u304a", "o");
            hiraganaToKunrei.add("\u304b", "ka");
            hiraganaToKunrei.add("\u304c", "ga");
            hiraganaToKunrei.add("\u304d", "ki");
            hiraganaToKunrei.add("\u304d\u3083", "kya");
            hiraganaToKunrei.add("\u304d\u3085", "kyu");
            hiraganaToKunrei.add("\u304d\u3087", "kyo");
            hiraganaToKunrei.add("\u304e", "gi");
            hiraganaToKunrei.add("\u304e\u3083", "gya");
            hiraganaToKunrei.add("\u304e\u3085", "gyu");
            hiraganaToKunrei.add("\u304e\u3087", "gyo");
            hiraganaToKunrei.add("\u304f", "ku");
            hiraganaToKunrei.add("\u3050", "gu");
            hiraganaToKunrei.add("\u3051", "ke");
            hiraganaToKunrei.add("\u3052", "ge");
            hiraganaToKunrei.add("\u3053", "ko");
            hiraganaToKunrei.add("\u3054", "go");
            hiraganaToKunrei.add("\u3055", "sa");
            hiraganaToKunrei.add("\u3056", "za");
            hiraganaToKunrei.add("\u3057", "si");
            hiraganaToKunrei.add("\u3057\u3083", "sya");
            hiraganaToKunrei.add("\u3057\u3085", "syu");
            hiraganaToKunrei.add("\u3057\u3087", "syo");
            hiraganaToKunrei.add("\u3058", "zi");
            hiraganaToKunrei.add("\u3058\u3083", "zya");
            hiraganaToKunrei.add("\u3058\u3085", "zyu");
            hiraganaToKunrei.add("\u3058\u3087", "zyo");
            hiraganaToKunrei.add("\u3059", "su");
            hiraganaToKunrei.add("\u305a", "zu");
            hiraganaToKunrei.add("\u305b", "se");
            hiraganaToKunrei.add("\u305c", "ze");
            hiraganaToKunrei.add("\u305d", "so");
            hiraganaToKunrei.add("\u305e", "zo");
            hiraganaToKunrei.add("\u305f", "ta");
            hiraganaToKunrei.add("\u3060", "da");
            hiraganaToKunrei.add("\u3061", "ti");
            hiraganaToKunrei.add("\u3061\u3083", "tya");
            hiraganaToKunrei.add("\u3061\u3085", "tyu");
            hiraganaToKunrei.add("\u3061\u3087", "tyo");
            hiraganaToKunrei.add("\u3062", "di");
            hiraganaToKunrei.add("\u3062\u3083", "dya");
            hiraganaToKunrei.add("\u3062\u3085", "dyu");
            hiraganaToKunrei.add("\u3062\u3087", "dyo");
            hiraganaToKunrei.add("\u3063", "tu");
            hiraganaToKunrei.add("\u3063\u3046\u309b", "vvu");
            hiraganaToKunrei.add("\u3063\u3046\u309b\u3041", "vva");
            hiraganaToKunrei.add("\u3063\u3046\u309b\u3043", "vvi");
            hiraganaToKunrei.add("\u3063\u3046\u309b\u3047", "vve");
            hiraganaToKunrei.add("\u3063\u3046\u309b\u3049", "vvo");
            hiraganaToKunrei.add("\u3063\u304b", "kka");
            hiraganaToKunrei.add("\u3063\u304c", "gga");
            hiraganaToKunrei.add("\u3063\u304d", "kki");
            hiraganaToKunrei.add("\u3063\u304d\u3083", "kkya");
            hiraganaToKunrei.add("\u3063\u304d\u3085", "kkyu");
            hiraganaToKunrei.add("\u3063\u304d\u3087", "kkyo");
            hiraganaToKunrei.add("\u3063\u304e", "ggi");
            hiraganaToKunrei.add("\u3063\u304e\u3083", "ggya");
            hiraganaToKunrei.add("\u3063\u304e\u3085", "ggyu");
            hiraganaToKunrei.add("\u3063\u304e\u3087", "ggyo");
            hiraganaToKunrei.add("\u3063\u304f", "kku");
            hiraganaToKunrei.add("\u3063\u3050", "ggu");
            hiraganaToKunrei.add("\u3063\u3051", "kke");
            hiraganaToKunrei.add("\u3063\u3052", "gge");
            hiraganaToKunrei.add("\u3063\u3053", "kko");
            hiraganaToKunrei.add("\u3063\u3054", "ggo");
            hiraganaToKunrei.add("\u3063\u3055", "ssa");
            hiraganaToKunrei.add("\u3063\u3056", "zza");
            hiraganaToKunrei.add("\u3063\u3057", "ssi");
            hiraganaToKunrei.add("\u3063\u3057\u3083", "ssya");
            hiraganaToKunrei.add("\u3063\u3057\u3085", "ssyu");
            hiraganaToKunrei.add("\u3063\u3057\u3087", "ssho");
            hiraganaToKunrei.add("\u3063\u3058", "zzi");
            hiraganaToKunrei.add("\u3063\u3058\u3083", "zzya");
            hiraganaToKunrei.add("\u3063\u3058\u3085", "zzyu");
            hiraganaToKunrei.add("\u3063\u3058\u3087", "zzyo");
            hiraganaToKunrei.add("\u3063\u3059", "ssu");
            hiraganaToKunrei.add("\u3063\u305a", "zzu");
            hiraganaToKunrei.add("\u3063\u305b", "sse");
            hiraganaToKunrei.add("\u3063\u305c", "zze");
            hiraganaToKunrei.add("\u3063\u305d", "sso");
            hiraganaToKunrei.add("\u3063\u305e", "zzo");
            hiraganaToKunrei.add("\u3063\u305f", "tta");
            hiraganaToKunrei.add("\u3063\u3060", "dda");
            hiraganaToKunrei.add("\u3063\u3061", "tti");
            hiraganaToKunrei.add("\u3063\u3061\u3083", "ttya");
            hiraganaToKunrei.add("\u3063\u3061\u3085", "ttyu");
            hiraganaToKunrei.add("\u3063\u3061\u3087", "ttyo");
            hiraganaToKunrei.add("\u3063\u3062", "ddi");
            hiraganaToKunrei.add("\u3063\u3062\u3083", "ddya");
            hiraganaToKunrei.add("\u3063\u3062\u3085", "ddyu");
            hiraganaToKunrei.add("\u3063\u3062\u3087", "ddyo");
            hiraganaToKunrei.add("\u3063\u3064", "ttu");
            hiraganaToKunrei.add("\u3063\u3065", "ddu");
            hiraganaToKunrei.add("\u3063\u3066", "tte");
            hiraganaToKunrei.add("\u3063\u3067", "dde");
            hiraganaToKunrei.add("\u3063\u3068", "tto");
            hiraganaToKunrei.add("\u3063\u3069", "ddo");
            hiraganaToKunrei.add("\u3063\u306f", "hha");
            hiraganaToKunrei.add("\u3063\u3070", "bba");
            hiraganaToKunrei.add("\u3063\u3071", "ppa");
            hiraganaToKunrei.add("\u3063\u3072", "hhi");
            hiraganaToKunrei.add("\u3063\u3072\u3083", "hhya");
            hiraganaToKunrei.add("\u3063\u3072\u3085", "hhyu");
            hiraganaToKunrei.add("\u3063\u3072\u3087", "hhyo");
            hiraganaToKunrei.add("\u3063\u3073", "bbi");
            hiraganaToKunrei.add("\u3063\u3073\u3083", "bbya");
            hiraganaToKunrei.add("\u3063\u3073\u3085", "bbyu");
            hiraganaToKunrei.add("\u3063\u3073\u3087", "bbyo");
            hiraganaToKunrei.add("\u3063\u3074", "ppi");
            hiraganaToKunrei.add("\u3063\u3074\u3083", "ppya");
            hiraganaToKunrei.add("\u3063\u3074\u3085", "ppyu");
            hiraganaToKunrei.add("\u3063\u3074\u3087", "ppyo");
            hiraganaToKunrei.add("\u3063\u3075", "hhu");
            hiraganaToKunrei.add("\u3063\u3075\u3041", "ffa");
            hiraganaToKunrei.add("\u3063\u3075\u3043", "ffi");
            hiraganaToKunrei.add("\u3063\u3075\u3047", "ffe");
            hiraganaToKunrei.add("\u3063\u3075\u3049", "ffo");
            hiraganaToKunrei.add("\u3063\u3076", "bbu");
            hiraganaToKunrei.add("\u3063\u3077", "ppu");
            hiraganaToKunrei.add("\u3063\u3078", "hhe");
            hiraganaToKunrei.add("\u3063\u3079", "bbe");
            hiraganaToKunrei.add("\u3063\u307a", "ppe");
            hiraganaToKunrei.add("\u3063\u307b", "hho");
            hiraganaToKunrei.add("\u3063\u307c", "bbo");
            hiraganaToKunrei.add("\u3063\u307d", "ppo");
            hiraganaToKunrei.add("\u3063\u3084", "yya");
            hiraganaToKunrei.add("\u3063\u3086", "yyu");
            hiraganaToKunrei.add("\u3063\u3088", "yyo");
            hiraganaToKunrei.add("\u3063\u3089", "rra");
            hiraganaToKunrei.add("\u3063\u308a", "rri");
            hiraganaToKunrei.add("\u3063\u308a\u3083", "rrya");
            hiraganaToKunrei.add("\u3063\u308a\u3085", "rryu");
            hiraganaToKunrei.add("\u3063\u308a\u3087", "rryo");
            hiraganaToKunrei.add("\u3063\u308b", "rru");
            hiraganaToKunrei.add("\u3063\u308c", "rre");
            hiraganaToKunrei.add("\u3063\u308d", "rro");
            hiraganaToKunrei.add("\u3064", "tu");
            hiraganaToKunrei.add("\u3065", "du");
            hiraganaToKunrei.add("\u3066", "te");
            hiraganaToKunrei.add("\u3067", "de");
            hiraganaToKunrei.add("\u3068", "to");
            hiraganaToKunrei.add("\u3069", "do");
            hiraganaToKunrei.add("\u306a", "na");
            hiraganaToKunrei.add("\u306b", "ni");
            hiraganaToKunrei.add("\u306b\u3083", "nya");
            hiraganaToKunrei.add("\u306b\u3085", "nyu");
            hiraganaToKunrei.add("\u306b\u3087", "nyo");
            hiraganaToKunrei.add("\u306c", "nu");
            hiraganaToKunrei.add("\u306d", "ne");
            hiraganaToKunrei.add("\u306e", "no");
            hiraganaToKunrei.add("\u306f", "ha");
            hiraganaToKunrei.add("\u3070", "ba");
            hiraganaToKunrei.add("\u3071", "pa");
            hiraganaToKunrei.add("\u3072", "hi");
            hiraganaToKunrei.add("\u3072\u3083", "hya");
            hiraganaToKunrei.add("\u3072\u3085", "hyu");
            hiraganaToKunrei.add("\u3072\u3087", "hyo");
            hiraganaToKunrei.add("\u3073", "bi");
            hiraganaToKunrei.add("\u3073\u3083", "bya");
            hiraganaToKunrei.add("\u3073\u3085", "byu");
            hiraganaToKunrei.add("\u3073\u3087", "byo");
            hiraganaToKunrei.add("\u3074", "pi");
            hiraganaToKunrei.add("\u3074\u3083", "pya");
            hiraganaToKunrei.add("\u3074\u3085", "pyu");
            hiraganaToKunrei.add("\u3074\u3087", "pyo");
            hiraganaToKunrei.add("\u3075", "hu");
            hiraganaToKunrei.add("\u3075\u3041", "fa");
            hiraganaToKunrei.add("\u3075\u3043", "fi");
            hiraganaToKunrei.add("\u3075\u3047", "fe");
            hiraganaToKunrei.add("\u3075\u3049", "fo");
            hiraganaToKunrei.add("\u3076", "bu");
            hiraganaToKunrei.add("\u3077", "pu");
            hiraganaToKunrei.add("\u3078", "he");
            hiraganaToKunrei.add("\u3079", "be");
            hiraganaToKunrei.add("\u307a", "pe");
            hiraganaToKunrei.add("\u307b", "ho");
            hiraganaToKunrei.add("\u307c", "bo");
            hiraganaToKunrei.add("\u307d", "po");
            hiraganaToKunrei.add("\u307e", "ma");
            hiraganaToKunrei.add("\u307f", "mi");
            hiraganaToKunrei.add("\u307f\u3083", "mya");
            hiraganaToKunrei.add("\u307f\u3085", "myu");
            hiraganaToKunrei.add("\u307f\u3087", "myo");
            hiraganaToKunrei.add("\u3080", "mu");
            hiraganaToKunrei.add("\u3081", "me");
            hiraganaToKunrei.add("\u3082", "mo");
            hiraganaToKunrei.add("\u3083", "ya");
            hiraganaToKunrei.add("\u3084", "ya");
            hiraganaToKunrei.add("\u3085", "yu");
            hiraganaToKunrei.add("\u3086", "yu");
            hiraganaToKunrei.add("\u3087", "yo");
            hiraganaToKunrei.add("\u3088", "yo");
            hiraganaToKunrei.add("\u3089", "ra");
            hiraganaToKunrei.add("\u308a", "ri");
            hiraganaToKunrei.add("\u308a\u3083", "rya");
            hiraganaToKunrei.add("\u308a\u3085", "ryu");
            hiraganaToKunrei.add("\u308a\u3087", "ryo");
            hiraganaToKunrei.add("\u308b", "ru");
            hiraganaToKunrei.add("\u308c", "re");
            hiraganaToKunrei.add("\u308d", "ro");
            hiraganaToKunrei.add("\u308e", "wa");
            hiraganaToKunrei.add("\u308f", "wa");
            hiraganaToKunrei.add("\u3090", "i");
            hiraganaToKunrei.add("\u3091", "e");
            hiraganaToKunrei.add("\u3092", "wo");
            hiraganaToKunrei.add("\u3093", "n");
            hiraganaToKunrei.add("\u3093\u3042", "n'a");
            hiraganaToKunrei.add("\u3093\u3044", "n'i");
            hiraganaToKunrei.add("\u3093\u3046", "n'u");
            hiraganaToKunrei.add("\u3093\u3048", "n'e");
            hiraganaToKunrei.add("\u3093\u304a", "n'o");
        }
        return hiraganaToKunrei;
    }

    /**
     * Gets the Katakana to Hepburn type romaji conversion table.
     */
    private static Table getKatakanaToHepburnTable() {
        if (katakanaToHepburn == null) {
            katakanaToHepburn = new Table();
            katakanaToHepburn.add("\u30a1", "a");
            katakanaToHepburn.add("\u30a2", "a");
            katakanaToHepburn.add("\u30a3", "i");
            katakanaToHepburn.add("\u30a4", "i");
            katakanaToHepburn.add("\u30a5", "u");
            katakanaToHepburn.add("\u30a6", "u");
            katakanaToHepburn.add("\u30a7", "e");
            katakanaToHepburn.add("\u30a8", "e");
            katakanaToHepburn.add("\u30a9", "o");
            katakanaToHepburn.add("\u30aa", "o");
            katakanaToHepburn.add("\u30ab", "ka");
            katakanaToHepburn.add("\u30ac", "ga");
            katakanaToHepburn.add("\u30ad", "ki");
            katakanaToHepburn.add("\u30ad\u30e3", "kya");
            katakanaToHepburn.add("\u30ad\u30e5", "kyu");
            katakanaToHepburn.add("\u30ad\u30e7", "kyo");
            katakanaToHepburn.add("\u30ae", "gi");
            katakanaToHepburn.add("\u30ae\u30e3", "gya");
            katakanaToHepburn.add("\u30ae\u30e5", "gyu");
            katakanaToHepburn.add("\u30ae\u30e7", "gyo");
            katakanaToHepburn.add("\u30af", "ku");
            katakanaToHepburn.add("\u30b0", "gu");
            katakanaToHepburn.add("\u30b1", "ke");
            katakanaToHepburn.add("\u30b2", "ge");
            katakanaToHepburn.add("\u30b3", "ko");
            katakanaToHepburn.add("\u30b4", "go");
            katakanaToHepburn.add("\u30b5", "sa");
            katakanaToHepburn.add("\u30b6", "za");
            katakanaToHepburn.add("\u30b7", "shi");
            katakanaToHepburn.add("\u30b7\u30e3", "sha");
            katakanaToHepburn.add("\u30b7\u30e5", "shu");
            katakanaToHepburn.add("\u30b7\u30e7", "sho");
            katakanaToHepburn.add("\u30b8", "ji");
            katakanaToHepburn.add("\u30b8\u30e3", "ja");
            katakanaToHepburn.add("\u30b8\u30e5", "ju");
            katakanaToHepburn.add("\u30b8\u30e7", "jo");
            katakanaToHepburn.add("\u30b9", "su");
            katakanaToHepburn.add("\u30ba", "zu");
            katakanaToHepburn.add("\u30bb", "se");
            katakanaToHepburn.add("\u30bc", "ze");
            katakanaToHepburn.add("\u30bd", "so");
            katakanaToHepburn.add("\u30be", "zo");
            katakanaToHepburn.add("\u30bf", "ta");
            katakanaToHepburn.add("\u30c0", "da");
            katakanaToHepburn.add("\u30c1", "chi");
            katakanaToHepburn.add("\u30c1\u30e3", "cha");
            katakanaToHepburn.add("\u30c1\u30e5", "chu");
            katakanaToHepburn.add("\u30c1\u30e7", "cho");
            katakanaToHepburn.add("\u30c2", "di");
            katakanaToHepburn.add("\u30c2\u30e3", "dya");
            katakanaToHepburn.add("\u30c2\u30e5", "dyu");
            katakanaToHepburn.add("\u30c2\u30e7", "dyo");
            katakanaToHepburn.add("\u30c3", "tsu");
            katakanaToHepburn.add("\u30c3\u30ab", "kka");
            katakanaToHepburn.add("\u30c3\u30ac", "gga");
            katakanaToHepburn.add("\u30c3\u30ad", "kki");
            katakanaToHepburn.add("\u30c3\u30ad\u30e3", "kkya");
            katakanaToHepburn.add("\u30c3\u30ad\u30e5", "kkyu");
            katakanaToHepburn.add("\u30c3\u30ad\u30e7", "kkyo");
            katakanaToHepburn.add("\u30c3\u30ae", "ggi");
            katakanaToHepburn.add("\u30c3\u30ae\u30e3", "ggya");
            katakanaToHepburn.add("\u30c3\u30ae\u30e5", "ggyu");
            katakanaToHepburn.add("\u30c3\u30ae\u30e7", "ggyo");
            katakanaToHepburn.add("\u30c3\u30af", "kku");
            katakanaToHepburn.add("\u30c3\u30b0", "ggu");
            katakanaToHepburn.add("\u30c3\u30b1", "kke");
            katakanaToHepburn.add("\u30c3\u30b2", "gge");
            katakanaToHepburn.add("\u30c3\u30b3", "kko");
            katakanaToHepburn.add("\u30c3\u30b4", "ggo");
            katakanaToHepburn.add("\u30c3\u30b5", "ssa");
            katakanaToHepburn.add("\u30c3\u30b6", "zza");
            katakanaToHepburn.add("\u30c3\u30b7", "sshi");
            katakanaToHepburn.add("\u30c3\u30b7\u30e3", "ssha");
            katakanaToHepburn.add("\u30c3\u30b7\u30e5", "sshu");
            katakanaToHepburn.add("\u30c3\u30b7\u30e7", "ssho");
            katakanaToHepburn.add("\u30c3\u30b8", "jji");
            katakanaToHepburn.add("\u30c3\u30b8\u30e3", "jja");
            katakanaToHepburn.add("\u30c3\u30b8\u30e5", "jju");
            katakanaToHepburn.add("\u30c3\u30b8\u30e7", "jjo");
            katakanaToHepburn.add("\u30c3\u30b9", "ssu");
            katakanaToHepburn.add("\u30c3\u30ba", "zzu");
            katakanaToHepburn.add("\u30c3\u30bb", "sse");
            katakanaToHepburn.add("\u30c3\u30bc", "zze");
            katakanaToHepburn.add("\u30c3\u30bd", "sso");
            katakanaToHepburn.add("\u30c3\u30be", "zzo");
            katakanaToHepburn.add("\u30c3\u30bf", "tta");
            katakanaToHepburn.add("\u30c3\u30c0", "dda");
            katakanaToHepburn.add("\u30c3\u30c1", "cchi");
            katakanaToHepburn.add("\u30c3\u30c1\u30e3", "ccha");
            katakanaToHepburn.add("\u30c3\u30c1\u30e5", "cchu");
            katakanaToHepburn.add("\u30c3\u30c1\u30e7", "ccho");
            katakanaToHepburn.add("\u30c3\u30c2", "ddi");
            katakanaToHepburn.add("\u30c3\u30c2\u30e3", "ddya");
            katakanaToHepburn.add("\u30c3\u30c2\u30e5", "ddyu");
            katakanaToHepburn.add("\u30c3\u30c2\u30e7", "ddyo");
            katakanaToHepburn.add("\u30c3\u30c4", "ttsu");
            katakanaToHepburn.add("\u30c3\u30c5", "ddu");
            katakanaToHepburn.add("\u30c3\u30c6", "tte");
            katakanaToHepburn.add("\u30c3\u30c7", "dde");
            katakanaToHepburn.add("\u30c3\u30c8", "tto");
            katakanaToHepburn.add("\u30c3\u30c9", "ddo");
            katakanaToHepburn.add("\u30c3\u30cf", "hha");
            katakanaToHepburn.add("\u30c3\u30d0", "bba");
            katakanaToHepburn.add("\u30c3\u30d1", "ppa");
            katakanaToHepburn.add("\u30c3\u30d2", "hhi");
            katakanaToHepburn.add("\u30c3\u30d2\u30e3", "hhya");
            katakanaToHepburn.add("\u30c3\u30d2\u30e5", "hhyu");
            katakanaToHepburn.add("\u30c3\u30d2\u30e7", "hhyo");
            katakanaToHepburn.add("\u30c3\u30d3", "bbi");
            katakanaToHepburn.add("\u30c3\u30d3\u30e3", "bbya");
            katakanaToHepburn.add("\u30c3\u30d3\u30e5", "bbyu");
            katakanaToHepburn.add("\u30c3\u30d3\u30e7", "bbyo");
            katakanaToHepburn.add("\u30c3\u30d4", "ppi");
            katakanaToHepburn.add("\u30c3\u30d4\u30e3", "ppya");
            katakanaToHepburn.add("\u30c3\u30d4\u30e5", "ppyu");
            katakanaToHepburn.add("\u30c3\u30d4\u30e7", "ppyo");
            katakanaToHepburn.add("\u30c3\u30d5", "ffu");
            katakanaToHepburn.add("\u30c3\u30d5\u30a1", "ffa");
            katakanaToHepburn.add("\u30c3\u30d5\u30a3", "ffi");
            katakanaToHepburn.add("\u30c3\u30d5\u30a7", "ffe");
            katakanaToHepburn.add("\u30c3\u30d5\u30a9", "ffo");
            katakanaToHepburn.add("\u30c3\u30d6", "bbu");
            katakanaToHepburn.add("\u30c3\u30d7", "ppu");
            katakanaToHepburn.add("\u30c3\u30d8", "hhe");
            katakanaToHepburn.add("\u30c3\u30d9", "bbe");
            katakanaToHepburn.add("\u30c3\u30da", "ppe");
            katakanaToHepburn.add("\u30c3\u30db", "hho");
            katakanaToHepburn.add("\u30c3\u30dc", "bbo");
            katakanaToHepburn.add("\u30c3\u30dd", "ppo");
            katakanaToHepburn.add("\u30c3\u30e4", "yya");
            katakanaToHepburn.add("\u30c3\u30e6", "yyu");
            katakanaToHepburn.add("\u30c3\u30e8", "yyo");
            katakanaToHepburn.add("\u30c3\u30e9", "rra");
            katakanaToHepburn.add("\u30c3\u30ea", "rri");
            katakanaToHepburn.add("\u30c3\u30ea\u30e3", "rrya");
            katakanaToHepburn.add("\u30c3\u30ea\u30e5", "rryu");
            katakanaToHepburn.add("\u30c3\u30ea\u30e7", "rryo");
            katakanaToHepburn.add("\u30c3\u30eb", "rru");
            katakanaToHepburn.add("\u30c3\u30ec", "rre");
            katakanaToHepburn.add("\u30c3\u30ed", "rro");
            katakanaToHepburn.add("\u30c3\u30f4", "vvu");
            katakanaToHepburn.add("\u30c3\u30f4\u30a1", "vva");
            katakanaToHepburn.add("\u30c3\u30f4\u30a3", "vvi");
            katakanaToHepburn.add("\u30c3\u30f4\u30a7", "vve");
            katakanaToHepburn.add("\u30c3\u30f4\u30a9", "vvo");
            katakanaToHepburn.add("\u30c4", "tsu");
            katakanaToHepburn.add("\u30c5", "du");
            katakanaToHepburn.add("\u30c6", "te");
            katakanaToHepburn.add("\u30c7", "de");
            katakanaToHepburn.add("\u30c8", "to");
            katakanaToHepburn.add("\u30c9", "do");
            katakanaToHepburn.add("\u30ca", "na");
            katakanaToHepburn.add("\u30cb", "ni");
            katakanaToHepburn.add("\u30cb\u30e3", "nya");
            katakanaToHepburn.add("\u30cb\u30e5", "nyu");
            katakanaToHepburn.add("\u30cb\u30e7", "nyo");
            katakanaToHepburn.add("\u30cc", "nu");
            katakanaToHepburn.add("\u30cd", "ne");
            katakanaToHepburn.add("\u30ce", "no");
            katakanaToHepburn.add("\u30cf", "ha");
            katakanaToHepburn.add("\u30d0", "ba");
            katakanaToHepburn.add("\u30d1", "pa");
            katakanaToHepburn.add("\u30d2", "hi");
            katakanaToHepburn.add("\u30d2\u30e3", "hya");
            katakanaToHepburn.add("\u30d2\u30e5", "hyu");
            katakanaToHepburn.add("\u30d2\u30e7", "hyo");
            katakanaToHepburn.add("\u30d3", "bi");
            katakanaToHepburn.add("\u30d3\u30e3", "bya");
            katakanaToHepburn.add("\u30d3\u30e5", "byu");
            katakanaToHepburn.add("\u30d3\u30e7", "byo");
            katakanaToHepburn.add("\u30d4", "pi");
            katakanaToHepburn.add("\u30d4\u30e3", "pya");
            katakanaToHepburn.add("\u30d4\u30e5", "pyu");
            katakanaToHepburn.add("\u30d4\u30e7", "pyo");
            katakanaToHepburn.add("\u30d5", "fu");
            katakanaToHepburn.add("\u30d5\u30a1", "fa");
            katakanaToHepburn.add("\u30d5\u30a3", "fi");
            katakanaToHepburn.add("\u30d5\u30a7", "fe");
            katakanaToHepburn.add("\u30d5\u30a9", "fo");
            katakanaToHepburn.add("\u30d6", "bu");
            katakanaToHepburn.add("\u30d7", "pu");
            katakanaToHepburn.add("\u30d8", "he");
            katakanaToHepburn.add("\u30d9", "be");
            katakanaToHepburn.add("\u30da", "pe");
            katakanaToHepburn.add("\u30db", "ho");
            katakanaToHepburn.add("\u30dc", "bo");
            katakanaToHepburn.add("\u30dd", "po");
            katakanaToHepburn.add("\u30de", "ma");
            katakanaToHepburn.add("\u30df", "mi");
            katakanaToHepburn.add("\u30df\u30e3", "mya");
            katakanaToHepburn.add("\u30df\u30e5", "myu");
            katakanaToHepburn.add("\u30df\u30e7", "myo");
            katakanaToHepburn.add("\u30e0", "mu");
            katakanaToHepburn.add("\u30e1", "me");
            katakanaToHepburn.add("\u30e2", "mo");
            katakanaToHepburn.add("\u30e3", "ya");
            katakanaToHepburn.add("\u30e4", "ya");
            katakanaToHepburn.add("\u30e5", "yu");
            katakanaToHepburn.add("\u30e6", "yu");
            katakanaToHepburn.add("\u30e7", "yo");
            katakanaToHepburn.add("\u30e8", "yo");
            katakanaToHepburn.add("\u30e9", "ra");
            katakanaToHepburn.add("\u30ea", "ri");
            katakanaToHepburn.add("\u30ea\u30e3", "rya");
            katakanaToHepburn.add("\u30ea\u30e5", "ryu");
            katakanaToHepburn.add("\u30ea\u30e7", "ryo");
            katakanaToHepburn.add("\u30eb", "ru");
            katakanaToHepburn.add("\u30ec", "re");
            katakanaToHepburn.add("\u30ed", "ro");
            katakanaToHepburn.add("\u30ee", "wa");
            katakanaToHepburn.add("\u30ef", "wa");
            katakanaToHepburn.add("\u30f0", "i");
            katakanaToHepburn.add("\u30f1", "e");
            katakanaToHepburn.add("\u30f2", "wo");
            katakanaToHepburn.add("\u30f3", "n");
            katakanaToHepburn.add("\u30f3\u30a2", "n'a");
            katakanaToHepburn.add("\u30f3\u30a4", "n'i");
            katakanaToHepburn.add("\u30f3\u30a6", "n'u");
            katakanaToHepburn.add("\u30f3\u30a8", "n'e");
            katakanaToHepburn.add("\u30f3\u30aa", "n'o");
            katakanaToHepburn.add("\u30f4", "vu");
            katakanaToHepburn.add("\u30f4\u30a1", "va");
            katakanaToHepburn.add("\u30f4\u30a3", "vi");
            katakanaToHepburn.add("\u30f4\u30a7", "ve");
            katakanaToHepburn.add("\u30f4\u30a9", "vo");
            katakanaToHepburn.add("\u30f5", "ka");
            katakanaToHepburn.add("\u30f6", "ke");
            katakanaToHepburn.add("\u30fc", "^");
        }
        return katakanaToHepburn;
    }

    /**
     * Gets the Katakana to Kunrei type romaji conversion table.
     */
    static Table getKatakanaToKunreiTable() {
        if (katakanaToKunrei == null) {
            katakanaToKunrei = new Table();
            katakanaToKunrei.add("\u30a1", "a");
            katakanaToKunrei.add("\u30a2", "a");
            katakanaToKunrei.add("\u30a3", "i");
            katakanaToKunrei.add("\u30a4", "i");
            katakanaToKunrei.add("\u30a5", "u");
            katakanaToKunrei.add("\u30a6", "u");
            katakanaToKunrei.add("\u30a7", "e");
            katakanaToKunrei.add("\u30a8", "e");
            katakanaToKunrei.add("\u30a9", "o");
            katakanaToKunrei.add("\u30aa", "o");
            katakanaToKunrei.add("\u30ab", "ka");
            katakanaToKunrei.add("\u30ac", "ga");
            katakanaToKunrei.add("\u30ad", "ki");
            katakanaToKunrei.add("\u30ad\u30e3", "kya");
            katakanaToKunrei.add("\u30ad\u30e5", "kyu");
            katakanaToKunrei.add("\u30ad\u30e7", "kyo");
            katakanaToKunrei.add("\u30ae", "gi");
            katakanaToKunrei.add("\u30ae\u30e3", "gya");
            katakanaToKunrei.add("\u30ae\u30e5", "gyu");
            katakanaToKunrei.add("\u30ae\u30e7", "gyo");
            katakanaToKunrei.add("\u30af", "ku");
            katakanaToKunrei.add("\u30b0", "gu");
            katakanaToKunrei.add("\u30b1", "ke");
            katakanaToKunrei.add("\u30b2", "ge");
            katakanaToKunrei.add("\u30b3", "ko");
            katakanaToKunrei.add("\u30b4", "go");
            katakanaToKunrei.add("\u30b5", "sa");
            katakanaToKunrei.add("\u30b6", "za");
            katakanaToKunrei.add("\u30b7", "si");
            katakanaToKunrei.add("\u30b7\u30e3", "sya");
            katakanaToKunrei.add("\u30b7\u30e5", "syu");
            katakanaToKunrei.add("\u30b7\u30e7", "syo");
            katakanaToKunrei.add("\u30b8", "zi");
            katakanaToKunrei.add("\u30b8\u30e3", "zya");
            katakanaToKunrei.add("\u30b8\u30e5", "zyu");
            katakanaToKunrei.add("\u30b8\u30e7", "zyo");
            katakanaToKunrei.add("\u30b9", "su");
            katakanaToKunrei.add("\u30ba", "zu");
            katakanaToKunrei.add("\u30bb", "se");
            katakanaToKunrei.add("\u30bc", "ze");
            katakanaToKunrei.add("\u30bd", "so");
            katakanaToKunrei.add("\u30be", "zo");
            katakanaToKunrei.add("\u30bf", "ta");
            katakanaToKunrei.add("\u30c0", "da");
            katakanaToKunrei.add("\u30c1", "ti");
            katakanaToKunrei.add("\u30c1\u30e3", "tya");
            katakanaToKunrei.add("\u30c1\u30e5", "tyu");
            katakanaToKunrei.add("\u30c1\u30e7", "tyo");
            katakanaToKunrei.add("\u30c2", "di");
            katakanaToKunrei.add("\u30c2\u30e3", "dya");
            katakanaToKunrei.add("\u30c2\u30e5", "dyu");
            katakanaToKunrei.add("\u30c2\u30e7", "dyo");
            katakanaToKunrei.add("\u30c3", "tu");
            katakanaToKunrei.add("\u30c3\u30ab", "kka");
            katakanaToKunrei.add("\u30c3\u30ac", "gga");
            katakanaToKunrei.add("\u30c3\u30ad", "kki");
            katakanaToKunrei.add("\u30c3\u30ad\u30e3", "kkya");
            katakanaToKunrei.add("\u30c3\u30ad\u30e5", "kkyu");
            katakanaToKunrei.add("\u30c3\u30ad\u30e7", "kkyo");
            katakanaToKunrei.add("\u30c3\u30ae", "ggi");
            katakanaToKunrei.add("\u30c3\u30ae\u30e3", "ggya");
            katakanaToKunrei.add("\u30c3\u30ae\u30e5", "ggyu");
            katakanaToKunrei.add("\u30c3\u30ae\u30e7", "ggyo");
            katakanaToKunrei.add("\u30c3\u30af", "kku");
            katakanaToKunrei.add("\u30c3\u30b0", "ggu");
            katakanaToKunrei.add("\u30c3\u30b1", "kke");
            katakanaToKunrei.add("\u30c3\u30b2", "gge");
            katakanaToKunrei.add("\u30c3\u30b3", "kko");
            katakanaToKunrei.add("\u30c3\u30b4", "ggo");
            katakanaToKunrei.add("\u30c3\u30b5", "ssa");
            katakanaToKunrei.add("\u30c3\u30b6", "zza");
            katakanaToKunrei.add("\u30c3\u30b7", "ssi");
            katakanaToKunrei.add("\u30c3\u30b7\u30e3", "ssya");
            katakanaToKunrei.add("\u30c3\u30b7\u30e5", "ssyu");
            katakanaToKunrei.add("\u30c3\u30b7\u30e7", "ssho");
            katakanaToKunrei.add("\u30c3\u30b8", "zzi");
            katakanaToKunrei.add("\u30c3\u30b8\u30e3", "zzya");
            katakanaToKunrei.add("\u30c3\u30b8\u30e5", "zzyu");
            katakanaToKunrei.add("\u30c3\u30b8\u30e7", "zzyo");
            katakanaToKunrei.add("\u30c3\u30b9", "ssu");
            katakanaToKunrei.add("\u30c3\u30ba", "zzu");
            katakanaToKunrei.add("\u30c3\u30bb", "sse");
            katakanaToKunrei.add("\u30c3\u30bc", "zze");
            katakanaToKunrei.add("\u30c3\u30bd", "sso");
            katakanaToKunrei.add("\u30c3\u30be", "zzo");
            katakanaToKunrei.add("\u30c3\u30bf", "tta");
            katakanaToKunrei.add("\u30c3\u30c0", "dda");
            katakanaToKunrei.add("\u30c3\u30c1", "tti");
            katakanaToKunrei.add("\u30c3\u30c1\u30e3", "ttya");
            katakanaToKunrei.add("\u30c3\u30c1\u30e5", "ttyu");
            katakanaToKunrei.add("\u30c3\u30c1\u30e7", "ttyo");
            katakanaToKunrei.add("\u30c3\u30c2", "ddi");
            katakanaToKunrei.add("\u30c3\u30c2\u30e3", "ddya");
            katakanaToKunrei.add("\u30c3\u30c2\u30e5", "ddyu");
            katakanaToKunrei.add("\u30c3\u30c2\u30e7", "ddyo");
            katakanaToKunrei.add("\u30c3\u30c4", "ttu");
            katakanaToKunrei.add("\u30c3\u30c5", "ddu");
            katakanaToKunrei.add("\u30c3\u30c6", "tte");
            katakanaToKunrei.add("\u30c3\u30c7", "dde");
            katakanaToKunrei.add("\u30c3\u30c8", "tto");
            katakanaToKunrei.add("\u30c3\u30c9", "ddo");
            katakanaToKunrei.add("\u30c3\u30cf", "hha");
            katakanaToKunrei.add("\u30c3\u30d0", "bba");
            katakanaToKunrei.add("\u30c3\u30d1", "ppa");
            katakanaToKunrei.add("\u30c3\u30d2", "hhi");
            katakanaToKunrei.add("\u30c3\u30d2\u30e3", "hhya");
            katakanaToKunrei.add("\u30c3\u30d2\u30e5", "hhyu");
            katakanaToKunrei.add("\u30c3\u30d2\u30e7", "hhyo");
            katakanaToKunrei.add("\u30c3\u30d3", "bbi");
            katakanaToKunrei.add("\u30c3\u30d3\u30e3", "bbya");
            katakanaToKunrei.add("\u30c3\u30d3\u30e5", "bbyu");
            katakanaToKunrei.add("\u30c3\u30d3\u30e7", "bbyo");
            katakanaToKunrei.add("\u30c3\u30d4", "ppi");
            katakanaToKunrei.add("\u30c3\u30d4\u30e3", "ppya");
            katakanaToKunrei.add("\u30c3\u30d4\u30e5", "ppyu");
            katakanaToKunrei.add("\u30c3\u30d4\u30e7", "ppyo");
            katakanaToKunrei.add("\u30c3\u30d5", "hhu");
            katakanaToKunrei.add("\u30c3\u30d5\u30a1", "ffa");
            katakanaToKunrei.add("\u30c3\u30d5\u30a3", "ffi");
            katakanaToKunrei.add("\u30c3\u30d5\u30a7", "ffe");
            katakanaToKunrei.add("\u30c3\u30d5\u30a9", "ffo");
            katakanaToKunrei.add("\u30c3\u30d6", "bbu");
            katakanaToKunrei.add("\u30c3\u30d7", "ppu");
            katakanaToKunrei.add("\u30c3\u30d8", "hhe");
            katakanaToKunrei.add("\u30c3\u30d9", "bbe");
            katakanaToKunrei.add("\u30c3\u30da", "ppe");
            katakanaToKunrei.add("\u30c3\u30db", "hho");
            katakanaToKunrei.add("\u30c3\u30dc", "bbo");
            katakanaToKunrei.add("\u30c3\u30dd", "ppo");
            katakanaToKunrei.add("\u30c3\u30e4", "yya");
            katakanaToKunrei.add("\u30c3\u30e6", "yyu");
            katakanaToKunrei.add("\u30c3\u30e8", "yyo");
            katakanaToKunrei.add("\u30c3\u30e9", "rra");
            katakanaToKunrei.add("\u30c3\u30ea", "rri");
            katakanaToKunrei.add("\u30c3\u30ea\u30e3", "rrya");
            katakanaToKunrei.add("\u30c3\u30ea\u30e5", "rryu");
            katakanaToKunrei.add("\u30c3\u30ea\u30e7", "rryo");
            katakanaToKunrei.add("\u30c3\u30eb", "rru");
            katakanaToKunrei.add("\u30c3\u30ec", "rre");
            katakanaToKunrei.add("\u30c3\u30ed", "rro");
            katakanaToKunrei.add("\u30c3\u30f4", "vvu");
            katakanaToKunrei.add("\u30c3\u30f4\u30a1", "vva");
            katakanaToKunrei.add("\u30c3\u30f4\u30a3", "vvi");
            katakanaToKunrei.add("\u30c3\u30f4\u30a7", "vve");
            katakanaToKunrei.add("\u30c3\u30f4\u30a9", "vvo");
            katakanaToKunrei.add("\u30c4", "tu");
            katakanaToKunrei.add("\u30c5", "du");
            katakanaToKunrei.add("\u30c6", "te");
            katakanaToKunrei.add("\u30c7", "de");
            katakanaToKunrei.add("\u30c8", "to");
            katakanaToKunrei.add("\u30c9", "do");
            katakanaToKunrei.add("\u30ca", "na");
            katakanaToKunrei.add("\u30cb", "ni");
            katakanaToKunrei.add("\u30cb\u30e3", "nya");
            katakanaToKunrei.add("\u30cb\u30e5", "nyu");
            katakanaToKunrei.add("\u30cb\u30e7", "nyo");
            katakanaToKunrei.add("\u30cc", "nu");
            katakanaToKunrei.add("\u30cd", "ne");
            katakanaToKunrei.add("\u30ce", "no");
            katakanaToKunrei.add("\u30cf", "ha");
            katakanaToKunrei.add("\u30d0", "ba");
            katakanaToKunrei.add("\u30d1", "pa");
            katakanaToKunrei.add("\u30d2", "hi");
            katakanaToKunrei.add("\u30d2\u30e3", "hya");
            katakanaToKunrei.add("\u30d2\u30e5", "hyu");
            katakanaToKunrei.add("\u30d2\u30e7", "hyo");
            katakanaToKunrei.add("\u30d3", "bi");
            katakanaToKunrei.add("\u30d3\u30e3", "bya");
            katakanaToKunrei.add("\u30d3\u30e5", "byu");
            katakanaToKunrei.add("\u30d3\u30e7", "byo");
            katakanaToKunrei.add("\u30d4", "pi");
            katakanaToKunrei.add("\u30d4\u30e3", "pya");
            katakanaToKunrei.add("\u30d4\u30e5", "pyu");
            katakanaToKunrei.add("\u30d4\u30e7", "pyo");
            katakanaToKunrei.add("\u30d5", "hu");
            katakanaToKunrei.add("\u30d5\u30a1", "fa");
            katakanaToKunrei.add("\u30d5\u30a3", "fi");
            katakanaToKunrei.add("\u30d5\u30a7", "fe");
            katakanaToKunrei.add("\u30d5\u30a9", "fo");
            katakanaToKunrei.add("\u30d6", "bu");
            katakanaToKunrei.add("\u30d7", "pu");
            katakanaToKunrei.add("\u30d8", "he");
            katakanaToKunrei.add("\u30d9", "be");
            katakanaToKunrei.add("\u30da", "pe");
            katakanaToKunrei.add("\u30db", "ho");
            katakanaToKunrei.add("\u30dc", "bo");
            katakanaToKunrei.add("\u30dd", "po");
            katakanaToKunrei.add("\u30de", "ma");
            katakanaToKunrei.add("\u30df", "mi");
            katakanaToKunrei.add("\u30df\u30e3", "mya");
            katakanaToKunrei.add("\u30df\u30e5", "myu");
            katakanaToKunrei.add("\u30df\u30e7", "myo");
            katakanaToKunrei.add("\u30e0", "mu");
            katakanaToKunrei.add("\u30e1", "me");
            katakanaToKunrei.add("\u30e2", "mo");
            katakanaToKunrei.add("\u30e3", "ya");
            katakanaToKunrei.add("\u30e4", "ya");
            katakanaToKunrei.add("\u30e5", "yu");
            katakanaToKunrei.add("\u30e6", "yu");
            katakanaToKunrei.add("\u30e7", "yo");
            katakanaToKunrei.add("\u30e8", "yo");
            katakanaToKunrei.add("\u30e9", "ra");
            katakanaToKunrei.add("\u30ea", "ri");
            katakanaToKunrei.add("\u30ea\u30e3", "rya");
            katakanaToKunrei.add("\u30ea\u30e5", "ryu");
            katakanaToKunrei.add("\u30ea\u30e7", "ryo");
            katakanaToKunrei.add("\u30eb", "ru");
            katakanaToKunrei.add("\u30ec", "re");
            katakanaToKunrei.add("\u30ed", "ro");
            katakanaToKunrei.add("\u30ee", "wa");
            katakanaToKunrei.add("\u30ef", "wa");
            katakanaToKunrei.add("\u30f0", "i");
            katakanaToKunrei.add("\u30f1", "e");
            katakanaToKunrei.add("\u30f2", "wo");
            katakanaToKunrei.add("\u30f3", "n");
            katakanaToKunrei.add("\u30f3\u30a2", "n'a");
            katakanaToKunrei.add("\u30f3\u30a4", "n'i");
            katakanaToKunrei.add("\u30f3\u30a6", "n'u");
            katakanaToKunrei.add("\u30f3\u30a8", "n'e");
            katakanaToKunrei.add("\u30f3\u30aa", "n'o");
            katakanaToKunrei.add("\u30f4", "vu");
            katakanaToKunrei.add("\u30f4\u30a1", "va");
            katakanaToKunrei.add("\u30f4\u30a3", "vi");
            katakanaToKunrei.add("\u30f4\u30a7", "ve");
            katakanaToKunrei.add("\u30f4\u30a9", "vo");
            katakanaToKunrei.add("\u30f5", "ka");
            katakanaToKunrei.add("\u30f6", "ke");
            katakanaToKunrei.add("\u30fc", "^");
        }
        return katakanaToKunrei;
    }

    /** Romaji type of Hepburn */
    static final int HEPBURN = 0;

    /** Romaji type of Kunrei */
    static final int KUNREI = 1;

    private int type;
    private boolean capitalizeMode;
    private boolean upperCaseMode;

    private Table hiraganaTable;
    private Table katakanaTable;

    /**
     * Sets the Romaji type property value. The default value is HEPBURN.
     *
     * @param newType  new romaji type.
     * @see #HEPBURN
     * @see #KUNREI
     */
    synchronized void setType(int newType) {
        type = newType;
        hiraganaTable = null;
        katakanaTable = null;
    }

    /**
     * Gets the Romaji type property value.
     *
     * @see #HEPBURN
     * @see #KUNREI
     */
    int getType() {
        return type;
    }

    /**
     * Sets the romaji capitalize mode property. The default value is false.
     *
     * @param newMode  new romaji capitalize mode value.
     */
    synchronized void setCapitalizeMode(boolean newMode) {
        capitalizeMode = newMode;
        hiraganaTable = null;
        katakanaTable = null;
    }

    /**
     * Gets the romaji capitalize mode property value.
     */
    boolean isCapitalizeMode() {
        return capitalizeMode;
    }

    /**
     * Sets the romaji upper case mode property. The default value is false.
     *
     * @param newMode  new romaji upper case mode value.
     */
    synchronized void setUpperCaseMode(boolean newMode) {
        upperCaseMode = newMode;
        hiraganaTable = null;
        katakanaTable = null;
    }

    /**
     * Gets the romaji upper case mode property value.
     */
    boolean isUpperCaseMode() {
        return upperCaseMode;
    }

    /**
     * Converts hiranaga word to romaji.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    synchronized boolean convertHiragana(KanjiInput input, Writer output)
        throws IOException {

        if (hiraganaTable == null) {
            hiraganaTable = type == KUNREI ?
                getHiraganaToKunreiTable() : getHiraganaToHepburnTable();
        }
        return convert(input, output, hiraganaTable);
    }

    /**
     * Converts katakana word to romaji.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    synchronized boolean convertKatakana(KanjiInput input, Writer output)
        throws IOException {

        if (katakanaTable == null) {
            katakanaTable = type == KUNREI ?
                getKatakanaToKunreiTable() : getKatakanaToHepburnTable();
        }
        return convert(input, output, katakanaTable);
    }

    /**
     * Converts hiragana/katakana word to romaji.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @param table the conversion table.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    private boolean convert(KanjiInput input, Writer output, Table table)
        throws IOException {

        String romaji = table.get(input);
        if (romaji == null) {
            return false;
        }
        if (capitalizeMode) {
            output.write(Character.toUpperCase(romaji.charAt(0)));
            romaji = romaji.substring(1);
        }
        if (upperCaseMode) {
            romaji = romaji.toUpperCase();
        }
        output.write(romaji);
        while (true) {
            romaji = table.get(input);
            if (romaji == null) {
                break;
            }
            if (upperCaseMode) {
                romaji = romaji.toUpperCase();
            }
            output.write(romaji);
        }
        return true;
    }

}
