/*
 * $Id: Converter.java,v 1.3 2003/01/01 08:18:44 kawao Exp $
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
 * An object that implements the Converter interface can convert a string.
 * 
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.3 $ $Date: 2003/01/01 08:18:44 $
 */
interface Converter {

    /**
     * Converts the string form the specified input object
     * and output the result to the specified writer.
     *
     * @param input  the input object.
     * @param output  the output writer object.
     * @return  false if no character is converted, otherwise true.
     * @exception  IOException  if an I/O error occurred.
     */
    boolean convert(KanjiInput input, Writer output) throws IOException;

}
