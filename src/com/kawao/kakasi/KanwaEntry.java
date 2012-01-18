/*
 * $Id: KanwaEntry.java,v 1.2 2003/01/01 08:18:44 kawao Exp $
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

/**
 * This class represents the Kanwa dictionary entry.
 * 
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.2 $ $Date: 2003/01/01 08:18:44 $
 */
class KanwaEntry {

    private final int offset;
    private final int numberOfWords;

    /**
     * Constructs a KanwaEntry object.
     *
     * @param offset  the file offset.
     * @param numberOfWords  number of words.
     */
    KanwaEntry(int offset, int numberOfWords) {
        this.offset = offset;
        this.numberOfWords = numberOfWords;
    }

    /**
     * Gets the file offset.
     */
    int getOffset() {
        return offset;
    }

    /**
     * Gets number of words.
     */
    int getNumberOfWords() {
        return numberOfWords;
    }

}

