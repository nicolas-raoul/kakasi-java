/*
 * $Id: KatakanaConverterImpl.java,v 1.2 2003/01/01 08:18:44 kawao Exp $
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

/**
 * This class implements conversion methods that converts a Katakana word.
 *
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.2 $ $Date: 2003/01/01 08:18:44 $
 */
class KatakanaConverterImpl {

    /**
     * Converts the Katakana word into the Hiragana word.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    boolean toHiragana(KanjiInput input, Writer output) throws IOException {
        if (!isKatakana(input.get())) {
            return false;
        }
        while (true) {
            int ch = input.get();
            if ((ch >= '\u30a1' && ch <= '\u30f3') ||
                ch == '\u30fd' || ch == '\u30fe') {
                // from small 'a' to 'n' and iteration marks
                input.consume(1);
                output.write((char)(ch - 0x60));
            } else if (ch == '\u30f4') {	// 'vu'
                input.consume(1);
                output.write('\u3046');
                output.write('\u309b');
            } else if (isKatakana(ch)) {
                input.consume(1);
                output.write((char)ch);
            } else {
                break;
            }
        }
        return true;
    }

    /**
     * Converts the Katakana word into the Katakana word.
     * This method id used for wakachigaki.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    boolean toKatakana(KanjiInput input, Writer output) throws IOException {
        int ch = input.get();
        if (!isKatakana(ch)) {
            return false;
        }
        output.write((char)ch);
        int length = 1;
        for (;; length++) {
            ch = input.more();
            if (!isKatakana(ch)) {
                break;
            }
            output.write((char)ch);
        }
        input.consume(length);
        return true;
    }

    /**
     * Returns whether the specified character is katakana.
     */
    private static boolean isKatakana(int ch) {
        if (ch < 0) {
            return false;
        }
        if (ch == '\u309b' || ch == '\u309c') { // voice sound mark
            return true;
        }
        Character.UnicodeBlock block = Character.UnicodeBlock.of((char)ch);
        return block.equals(Character.UnicodeBlock.KATAKANA);
    }

}
