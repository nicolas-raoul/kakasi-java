/*
 * $Id: CompoundConverter.java,v 1.2 2003/01/01 08:18:44 kawao Exp $
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
 * A composite Converter class used to compose two Converter objects
 * into a single converter.
 *
 * @see Kakasi#getInput()
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.2 $ $Date: 2003/01/01 08:18:44 $
 */
class CompoundConverter implements Converter {

    private final Converter front;
    private final Converter back;

    private final KanjiInput pipeInput = new KanjiInput();
    private final Writer pipeOutput = pipeInput.createConnectedWriter();

    /**
     * Constructs a CompondConverter object.
     *
     * @param fromt  the front converter.
     * @param back  the back converter.
     */
    CompoundConverter(Converter front, Converter back) {
        this.front = front;
        this.back = back;
    }

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
        boolean ret = front.convert(input, pipeOutput);
        if (ret) {
            while (pipeInput.get() >= 0) {
                if (!back.convert(pipeInput, output)) {
                    output.write((char)pipeInput.get());
                    pipeInput.consume(1);
                }
            }
        }
        return ret;
    }

}

