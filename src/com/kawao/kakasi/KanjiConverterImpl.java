/*
 * $Id: KanjiConverterImpl.java,v 1.2 2003/01/01 08:18:44 kawao Exp $
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
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * This class implements conversion methods that converts a Kanji word.
 *
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.2 $ $Date: 2003/01/01 08:18:44 $
 */
class KanjiConverterImpl {

    private final ItaijiDictionary itaijiDictionary =
        ItaijiDictionary.getInstance();
    private final KanwaDictionary kanwaDictionary;

    private boolean heikiMode;
    private boolean furiganaMode;

    /**
     * Constructs a KanjiConverterImpl object.
     *
     * @param kanwaDictionary  the KanwaDictionary object.
     */
    KanjiConverterImpl(KanwaDictionary kanwaDictionary) {
        this.kanwaDictionary = kanwaDictionary;
    }

    /**
     * Sets the heiki mode property. The default value is false.
     *
     * @param newMode  if true, lists all readings with Kanji convertsion.
     */
    void setHeikiMode(boolean newMode) {
        heikiMode = newMode;
    }

    /**
     * Gets the heiki mode property value.
     */
    boolean isHeikiMode() {
        return heikiMode;
    }

    /**
     * Sets the furigana mode property. The default value is false.
     *
     * @param newMode  new furigana mode value.
     */
    void setFuriganaMode(boolean newMode) {
        furiganaMode = newMode;
    }

    /**
     * Gets the furigana mode property value.
     */
    boolean isFuriganaMode() {
        return furiganaMode;
    }

    /**
     * Converts the Kanji word into the Hiragana word.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    boolean toHiragana(KanjiInput input, Writer output) throws IOException {
        char key = itaijiDictionary.get((char)input.get());
        Iterator iterator = kanwaDictionary.lookup(key);
        if (iterator == null) {
            return false;
        }
        Set yomiSet = new HashSet();
        String rest = null;
        int restLength = 0;
        int resultLength = 0;
        while (iterator.hasNext()) {
            KanjiYomi kanjiYomi = (KanjiYomi)iterator.next();
            int length = kanjiYomi.getLength();
            if (rest == null) {
                char[] chars = new char[length + 1];
                restLength = input.more(chars);
                for (int index = 0; index < restLength; index++) {
                    chars[index] = itaijiDictionary.get(chars[index]);
                }
                rest = new String(chars, 0, restLength);
            }
            if (length < resultLength) {
                break;
            }
            if (length > restLength) {
                continue;
            }
            String yomi = kanjiYomi.getYomiFor(rest);
            if (yomi == null) {
                continue;
            }
            yomiSet.add(yomi);
            resultLength = length;
            if (!isHeikiMode()) {
                break;
            }
        }
        if (yomiSet.isEmpty()) {
            return false;
        }
        char additionalChar = 0;
        if (resultLength > 0  && restLength > resultLength &&
            rest.charAt(resultLength - 1) == '\u3063') {
            char nextCh = rest.charAt(resultLength);
            Character.UnicodeBlock block = Character.UnicodeBlock.of(nextCh);
            if (block.equals(Character.UnicodeBlock.HIRAGANA)) {
                ++resultLength;
                additionalChar = nextCh;
            }
        }
        input.consume(resultLength + 1);
        if (isFuriganaMode()) {
            output.write(key);
            if (resultLength > 0) {
                output.write(rest, 0, resultLength);
            }
            output.write('[');
        }
        if (yomiSet.size() == 1) {
            output.write((String)yomiSet.iterator().next());
            if (additionalChar > 0) {
                output.write(additionalChar);
            }
        } else if (yomiSet.size() > 1) {
            Iterator iter = yomiSet.iterator();
            output.write('{');
            while (true) {
                output.write((String)iter.next());
                if (additionalChar > 0) {
                    output.write(additionalChar);
                }
                if (iter.hasNext()) {
                    output.write('|');
                } else {
                    break;
                }
            }
            output.write('}');
        }
        if (isFuriganaMode()) {
            output.write(']');
        }
        return true;
    }

    /**
     * Converts the Kanji word into the Kanji word.
     * This method id used for wakachigaki.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    boolean toKanji(KanjiInput input, Writer output) throws IOException {
        char key = itaijiDictionary.get((char)input.get());
        Iterator iterator = kanwaDictionary.lookup(key);
        if (iterator == null) {
            return false;
        }
        String rest = null;
        int restLength = 0;
        int resultLength = 0;
        while (iterator.hasNext()) {
            KanjiYomi kanjiYomi = (KanjiYomi)iterator.next();
            int length = kanjiYomi.getLength();
            if (rest == null) {
                char[] chars = new char[length + 1];
                restLength = input.more(chars);
                for (int index = 0; index < restLength; index++) {
                    chars[index] = itaijiDictionary.get(chars[index]);
                }
                rest = new String(chars, 0, restLength);
            }
            if (length < resultLength) {
                break;
            }
            if (length > restLength) {
                continue;
            }
            if (kanjiYomi.getYomiFor(rest) != null) {
                resultLength = length;
                break;
            }
        }
        if (resultLength > 0  && restLength > resultLength &&
            rest.charAt(resultLength - 1) == '\u3063') {
            char nextCh = rest.charAt(resultLength);
            Character.UnicodeBlock block = Character.UnicodeBlock.of(nextCh);
            if (block.equals(Character.UnicodeBlock.HIRAGANA)) {
                ++resultLength;
            }
        }
        input.consume(resultLength + 1);
        output.write(key);
        if (resultLength > 0) {
            output.write(rest, 0, resultLength);
        }
        return true;
    }

}
