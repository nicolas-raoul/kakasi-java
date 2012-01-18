/*
 * $Id: HiraganaConverterImpl.java,v 1.2 2003/01/01 08:18:44 kawao Exp $
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
 * This class implements conversion methods that converts a Hiragana word.
 *
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.2 $ $Date: 2003/01/01 08:18:44 $
 */
class HiraganaConverterImpl {

    private static final char WO = '\u3092';

    /**
     * Converts the Hiragana word into the Katakana word.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    boolean toKatakana(KanjiInput input, Writer output) throws IOException {
        if (!isHiragana(input.get())) {
            return false;
        }
        while (true) {
            int ch = input.get();
            if (ch == '\u3046') {	// 'u'
                input.consume(1);
                int ch2 = input.get();
                if (ch2 == '\u309b') {	// voice sound mark
                    input.consume(1);
                    output.write('\u30f4');	// 'vu'
                } else {
                    output.write('\u30a6');
                }
            } else if ((ch >= '\u3041' && ch <= '\u3093') ||
                       ch == '\u309d' || ch == '\u309e') {
                // from small 'a' to 'n' and iteration marks
                input.consume(1);
                output.write((char)(ch + 0x60));
            } else if (isHiragana(ch)) {
                input.consume(1);
                output.write((char)ch);
            } else {
                break;
            }
        }
        return true;
    }

    /**
     * Converts the Hiragana word into the Hiragana word.
     * This method id used for wakachigaki.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    boolean toHiragana(KanjiInput input, Writer output) throws IOException {
        int ch = input.get();
        if (!isHiragana(ch)) {
            return false;
        }
        output.write((char)ch);
        int length = 1;
        if (ch != WO) {
            for (;; length++) {
                ch = input.more();
                if (ch == WO) {
                    break;
                }
                if (!isHiragana(ch)) {
                    break;
                }
                output.write((char)ch);
            }
        }
        input.consume(length);
        return true;
    }

    /**
     * Returns whether the specified character is hiragana.
     */
    private static boolean isHiragana(int ch) {
        if (ch < 0) {
            return false;
        }
        if (ch == '\u30fc') {	// prolonged sound mark
            return true;
        }
        Character.UnicodeBlock block = Character.UnicodeBlock.of((char)ch);
        return block.equals(Character.UnicodeBlock.HIRAGANA);
    }

}
