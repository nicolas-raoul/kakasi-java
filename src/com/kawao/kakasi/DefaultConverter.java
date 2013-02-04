/*
 * $Id: DefaultConverter.java,v 1.1 2003/01/01 08:18:44 kawao Exp $
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
 * This class is a default implementation of Converter interface.
 * 
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.1 $ $Date: 2003/01/01 08:18:44 $
 */
class DefaultConverter implements Converter {

    /**
     * Converts the string form the specified input object
     * and output the result to the specified writer.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    public boolean convert(KanjiInput input, Writer output)
        throws IOException {
        int ch = input.get();
        if (ch < 0) {
            return false;
        }
        Character.UnicodeBlock pblock = Character.UnicodeBlock.of((char)ch);
        while (true) {
            input.consume(1);
            output.write((char)ch);
            ch = input.get();
            if (ch < 0) {
                break;
            }
            Character.UnicodeBlock block;
            switch (ch) {
            case '\u3005':	// kurikaesi
            case '\u3006':	// shime
            case '\u30f5':	// katakana small ka
            case '\u30f6':	// katakana small ke
                block = Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
                break;
            default:
                block = Character.UnicodeBlock.of((char)ch);
                break;
            }
            if (isJapanese(block) != isJapanese(pblock)) {
            	break;
            }
        }
        return true;
    }
    
    private boolean isJapanese(Character.UnicodeBlock block) {
    	return block.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) || block.equals(Character.UnicodeBlock.HIRAGANA) || block.equals(Character.UnicodeBlock.KATAKANA);
    }

}
