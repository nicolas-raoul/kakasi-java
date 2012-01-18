/*
 * $Id: KanjiOutput.java,v 1.2 2003/01/01 08:18:44 kawao Exp $
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
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;

/**
 * An object of this class is used as destination of the conversion.
 * 
 * @see Kakasi#getOutput()
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.2 $ $Date: 2003/01/01 08:18:44 $
 */
public class KanjiOutput extends Writer {

    private Writer writer;

    private boolean autoFlushMode = true;

    private boolean splitMode;
    private boolean lastWasSpace;
    private boolean outSeparator;

    /**
     * The constructor is not public.
     */
    KanjiOutput() {
    }

    /**
     * Sets the writer object.
     *
     * @param newWriter  new writer object.
     */
    public synchronized void setWriter(Writer newWriter) {
        writer = newWriter;
        lastWasSpace = true;
    }

    /**
     * Sets the auto flush mode property value. The default value is true.
     *
     * @param newMode  if true the output buffer is flushed when a newline
     *                 character is written.
     *                 
     */
    public void setAutoFlushMode(boolean newMode) {
        autoFlushMode = newMode;
    }

    /**
     * Gets the auto flush mode property value.
     */
    public boolean isAutoFlushMode() {
        return autoFlushMode;
    }

    /**
     * Sets the split mode property value. The default value is false.
     *
     * @param newMode  the new split mode.
     */
    public synchronized void setSplitMode(boolean newMode) {
        splitMode = newMode;
        outSeparator = false;
    }

    /**
     * Gets the split mode property value.
     */
    public boolean isSplitMode() {
        return splitMode;
    }

    /**
     * Puts the separator character if the split mode is true.
     */
    synchronized void putSeparator() {
        if (splitMode) {
            outSeparator = true;
        }
    }

    /**
     * Write a portion of an array of characters.
     *
     * @param  cbuf  Array of characters
     * @param  off   Offset from which to start writing characters
     * @param  len   Number of characters to write
     * @exception  IOException  If an I/O error occurs
     */
    public void write(char cbuf[], int off, int len) throws IOException {
        int max = off + len;
        for (int index = off; index < max; index++) {
            write(cbuf[index]);
        }
    }

    /**
     * Write a single character.
     *
     * @param c  int specifying a character to be written.
     * @exception  IOException  If an I/O error occurs
     */
    public synchronized void write(int c) throws IOException {
        if (writer == null) {
            setWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        }
        if (isSplitMode()) {
            if (Character.isWhitespace((char)c)) {
                lastWasSpace = true;
                outSeparator = false;
            } else {
                if (outSeparator) {
                    outSeparator = false;
                    if (!lastWasSpace) {
                        writer.write(' ');
                    }
                }
                lastWasSpace = false;
            }
        }
        writer.write(c);
        if (c == '\n' && isAutoFlushMode()) {
            flush();
        }
    }

    /**
     * Flush the stream.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public synchronized void flush() throws IOException {
        if (writer != null) {
            writer.flush();
        }
    }

    /**
     * Close the stream.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

}

