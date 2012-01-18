/*
 * $Id: KanjiYomi.java,v 1.5 2003/01/01 08:18:44 kawao Exp $
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

import java.util.Map;
import java.util.HashMap;

/**
 * An object of this class holds a yomi of a kanji.
 * 
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.5 $ $Date: 2003/01/01 08:18:44 $
 */
class KanjiYomi implements Comparable {

    private static final Map okuriganaTable = new HashMap();
    static {
        okuriganaTable.put(new Character('\u3041'), "aiueow");
        okuriganaTable.put(new Character('\u3042'), "aiueow");
        okuriganaTable.put(new Character('\u3043'), "aiueow");
        okuriganaTable.put(new Character('\u3044'), "aiueow");
        okuriganaTable.put(new Character('\u3045'), "aiueow");
        okuriganaTable.put(new Character('\u3046'), "aiueow");
        okuriganaTable.put(new Character('\u3047'), "aiueow");
        okuriganaTable.put(new Character('\u3048'), "aiueow");
        okuriganaTable.put(new Character('\u3049'), "aiueow");
        okuriganaTable.put(new Character('\u304a'), "aiueow");
        okuriganaTable.put(new Character('\u304b'), "k");
        okuriganaTable.put(new Character('\u304d'), "k");
        okuriganaTable.put(new Character('\u304f'), "k");
        okuriganaTable.put(new Character('\u3051'), "k");
        okuriganaTable.put(new Character('\u3053'), "k");
        okuriganaTable.put(new Character('\u304c'), "g");
        okuriganaTable.put(new Character('\u304e'), "g");
        okuriganaTable.put(new Character('\u3050'), "g");
        okuriganaTable.put(new Character('\u3052'), "g");
        okuriganaTable.put(new Character('\u3054'), "g");
        okuriganaTable.put(new Character('\u3055'), "s");
        okuriganaTable.put(new Character('\u3057'), "s");
        okuriganaTable.put(new Character('\u3059'), "s");
        okuriganaTable.put(new Character('\u305b'), "s");
        okuriganaTable.put(new Character('\u305d'), "s");
        okuriganaTable.put(new Character('\u3056'), "zj");
        okuriganaTable.put(new Character('\u3058'), "zj");
        okuriganaTable.put(new Character('\u305a'), "zj");
        okuriganaTable.put(new Character('\u305c'), "zj");
        okuriganaTable.put(new Character('\u305e'), "zj");
        okuriganaTable.put(new Character('\u305f'), "t");
        okuriganaTable.put(new Character('\u3061'), "tc");
        okuriganaTable.put(new Character('\u3063'), "aiueokstchgzjfdbpw");
        okuriganaTable.put(new Character('\u3064'), "t");
        okuriganaTable.put(new Character('\u3066'), "t");
        okuriganaTable.put(new Character('\u3068'), "t");
        okuriganaTable.put(new Character('\u3060'), "d");
        okuriganaTable.put(new Character('\u3062'), "d");
        okuriganaTable.put(new Character('\u3065'), "d");
        okuriganaTable.put(new Character('\u3067'), "d");
        okuriganaTable.put(new Character('\u3069'), "d");
        okuriganaTable.put(new Character('\u306a'), "n");
        okuriganaTable.put(new Character('\u306b'), "n");
        okuriganaTable.put(new Character('\u306c'), "n");
        okuriganaTable.put(new Character('\u306d'), "n");
        okuriganaTable.put(new Character('\u306e'), "n");
        okuriganaTable.put(new Character('\u306f'), "h");
        okuriganaTable.put(new Character('\u3072'), "h");
        okuriganaTable.put(new Character('\u3075'), "hf");
        okuriganaTable.put(new Character('\u3078'), "h");
        okuriganaTable.put(new Character('\u307b'), "h");
        okuriganaTable.put(new Character('\u3070'), "b");
        okuriganaTable.put(new Character('\u3073'), "b");
        okuriganaTable.put(new Character('\u3076'), "b");
        okuriganaTable.put(new Character('\u3079'), "b");
        okuriganaTable.put(new Character('\u307c'), "b");
        okuriganaTable.put(new Character('\u3071'), "p");
        okuriganaTable.put(new Character('\u3074'), "p");
        okuriganaTable.put(new Character('\u3077'), "p");
        okuriganaTable.put(new Character('\u307a'), "p");
        okuriganaTable.put(new Character('\u307d'), "p");
        okuriganaTable.put(new Character('\u307e'), "m");
        okuriganaTable.put(new Character('\u307f'), "m");
        okuriganaTable.put(new Character('\u3080'), "m");
        okuriganaTable.put(new Character('\u3081'), "m");
        okuriganaTable.put(new Character('\u3082'), "m");
        okuriganaTable.put(new Character('\u3083'), "y");
        okuriganaTable.put(new Character('\u3084'), "y");
        okuriganaTable.put(new Character('\u3085'), "y");
        okuriganaTable.put(new Character('\u3086'), "y");
        okuriganaTable.put(new Character('\u3087'), "y");
        okuriganaTable.put(new Character('\u3088'), "y");
        okuriganaTable.put(new Character('\u3089'), "rl");
        okuriganaTable.put(new Character('\u308a'), "rl");
        okuriganaTable.put(new Character('\u308b'), "rl");
        okuriganaTable.put(new Character('\u308c'), "rl");
        okuriganaTable.put(new Character('\u308d'), "rl");
        okuriganaTable.put(new Character('\u308e'), "wiueo");
        okuriganaTable.put(new Character('\u308f'), "wiueo");
        okuriganaTable.put(new Character('\u3090'), "wiueo");
        okuriganaTable.put(new Character('\u3091'), "wiueo");
        okuriganaTable.put(new Character('\u3092'), "w");
        okuriganaTable.put(new Character('\u3093'), "n");
        okuriganaTable.put(new Character('\u30f5'), "k");
        okuriganaTable.put(new Character('\u30f6'), "k");
    }

    private static final Object LOCK = new Object();
    private static long objectConter;
    private final long objectIndex;

    private final String kanji;
    private final String yomi;
    private final char okurigana;
    private final int kanjiLength;
    private final int hashCode;

    /**
     * Constructs a KanjiYomi object.
     *
     * @param kanji  the kanji string.
     * @param yomi  the yomi string.
     * @param okurigana  the okurigana character.
     */
    KanjiYomi(String kanji, String yomi, char okurigana) {
        this.kanji = kanji;
        this.yomi = yomi;
        this.okurigana = okurigana;
        kanjiLength = kanji.length();
        hashCode = kanji.hashCode() ^ yomi.hashCode() ^ (int)okurigana;
        synchronized (LOCK) {
            objectIndex = objectConter++;
        }
    }

    /**
     * Gets the kanji string.
     */
    String getKanji() {
        return kanji;
    }

    /**
     * Gets the yomi string.
     */
    String getYomi() {
        return yomi;
    }

    /**
     * Gets the okurigana character.
     */
    char getOkurigana() {
        return okurigana;
    }

    /**
     * Gets required kanji length.
     */
    int getLength() {
        return kanjiLength + (okurigana > 0 ? 1 : 0);
    }

    /**
     * Gets the yomi string for the specified string.
     *
     * @param target  the string to be checked.
     * @return  the yomi string, or null if the specified string is not match.
     */
    String getYomiFor(String target) {
        if (kanjiLength > 0 && !target.startsWith(kanji)) {
            return null;
        }
        if (okurigana == 0) {
            return yomi;
        }
        try {
            Character ch = new Character(target.charAt(kanjiLength));
            String okuriganaList = (String)okuriganaTable.get(ch);
            return
                okuriganaList == null || okuriganaList.indexOf(okurigana) < 0 ?
                null : yomi + ch;
        } catch (IndexOutOfBoundsException exceprion) {
            return null;
        }
    }

    /**
     * Compares two objects for equality.
     *
     * @param object  the object to compare with.
     * @return  true if the objects are the same; false otherwise.
     */
    public boolean equals(Object object) {
	if (object instanceof KanjiYomi) {
            KanjiYomi kanjiYomi = (KanjiYomi)object;
	    return
                hashCode() == kanjiYomi.hashCode() &&
                kanji.equals(kanjiYomi.kanji) &&
                yomi.equals(kanjiYomi.yomi) &&
                okurigana == kanjiYomi.okurigana;
	}
	return false;
    }

    /**
     * Returns a hash code value for this object.
     */
    public int hashCode() {
        return hashCode;
    }

    /**
     * Compares this objects to another object.
     *
     * @param   o the Object to be compared.
     */
    public int compareTo(Object o) {
        KanjiYomi another = (KanjiYomi)o;
        if (another.kanjiLength == kanjiLength) {
            if (okurigana > 0 && another.okurigana == 0) {
                return -1;
            } else if (okurigana == 0 && another.okurigana > 0) {
                return 1;
            } else {
                return equals(another) ? 0 :
                    objectIndex < another.objectIndex ? -1 : 1;
            }
        } else {
            return another.kanjiLength < kanjiLength ? -1 : 1;
        }
    }

}
