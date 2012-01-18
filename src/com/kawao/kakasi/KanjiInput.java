/*
 * $Id: KanjiInput.java,v 1.5 2003/01/01 08:18:44 kawao Exp $
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

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.Writer;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

/**
 * An object of this class supplies inputs characters for the conversion.
 * 
 * @see Kakasi#getInput()
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.5 $ $Date: 2003/01/01 08:18:44 $
 */
public class KanjiInput {

    private final StringBuffer buffer = new StringBuffer();
    private int nextIndex;

    private Reader reader;

    private boolean spaceEatMode;

    private final char[] oneCharacter = new char[1];

    /**
     * The constructor is not public.
     */
    KanjiInput() {
    }

    /**
     * Sets input as the specified string.
     *
     * @param string  the string object for input.
     */
    public void setInputString(String string) {
        setReader(new StringReader(string));
    }

    /**
     * Sets the reader object.
     *
     * @param newReader  new reader object.
     */
    public synchronized void setReader(Reader newReader) {
        reader = newReader;
        buffer.setLength(0);
    }

    /**
     * Sets the space eat mode property value. The default value is false.
     *
     * @param newMode  if true skip whitespace characters within jukugo.
     *                        
     */
    public void setSpaceEatMode(boolean newMode) {
        spaceEatMode = newMode;
    }

    /**
     * Gets the space eat mode property value.
     */
    public boolean isSpaceEatMode() {
        return spaceEatMode;
    }

    /**
     * Gets the input character. 
     *
     * @return  The character read, or -1 if the end of the stream has been
     *           reached
     * @exception  IOException  If an I/O error occurs
     */
    synchronized int get() throws IOException {
        if (reader == null) {
            setReader(new BufferedReader(new InputStreamReader(System.in)));
        }
        if (buffer.length() == 0) {
            int ch = reader.read();
            if (ch < 0) {
                return -1;
            }
            buffer.append((char)ch);
        }
        nextIndex = 1;
        return buffer.charAt(0);
    }

    /**
     * Gets more input character.
     * 
     * @return The character read, or -1 if the end of the word or stream has
     *         been reached.
     * @exception  IOException  If an I/O error occurs
     */
    synchronized int more() throws IOException {
        return more(oneCharacter) > 0 ? oneCharacter[0] : -1;
    }

    /**
     * Gets more input characters. 
     * 
     * @param chars  destination buffer. 
     * @return  The number of characters.
     * @exception  IOException  If an I/O error occurs
     */
    synchronized int more(char[] chars) throws IOException {
        int bufferLength = buffer.length();
        int resultLength = 0;
        for (; resultLength < chars.length; nextIndex++) {
            if (bufferLength <= nextIndex) {
                int ch = reader.read();
                if (ch < 0) {
                    break;
                }
                buffer.append((char)ch);
                ++bufferLength;
            }
            char ch = buffer.charAt(nextIndex);
            if (Character.isWhitespace(ch)) {
                if (!isSpaceEatMode()) {
                    break;
                }
            } else {
                chars[resultLength++] = ch;
            }
        }
        return resultLength;
    }

    /**
     * Consumes the specified length of input characters.
     *
     * @param length  the length of characters to consume.
     */
    synchronized void consume(int length) {
        if (isSpaceEatMode()) {
            int start = 0;
            int end = length;
            for (int index = 1; index < end; index++) {
                char ch = buffer.charAt(index);
                if (Character.isWhitespace(ch)) {
                    buffer.setCharAt(start++, ch);
                    end++;
                }
            }
            buffer.delete(start, end);
        } else {
            buffer.delete(0, length);
        }
        nextIndex = 0;
    }

    /**
     * Creates a Writer object that supplies inputs for this object.
     */
    Writer createConnectedWriter() {
        reader = new NullReader();
        return new ConnectedWriter();
    }

    /**
     * Nothing can be read from object of this class.
     */
    private static class NullReader extends Reader {

        public int read(char cbuf[], int off, int len) {
            return -1;
        }

        public void close() {
        }

    }

    /**
     * Writer that supplis inputs to the KanjiInput object.
     */
    private class ConnectedWriter extends Writer {
        
        public void write(char cbuf[], int off, int len) {
            synchronized (KanjiInput.this) {
                buffer.append(cbuf, off, len);
            }
        }

        public void flush() {
        }

        public void close() {
        }

    }

}
