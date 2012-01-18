/*
 * $Id: ItaijiDictionary.java,v 1.3 2003/01/01 08:18:44 kawao Exp $
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

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * This class represents the Itaiji dictionary.
 * 
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.3 $ $Date: 2003/01/01 08:18:44 $
 */
class ItaijiDictionary {

    private static final ItaijiDictionary instance = new ItaijiDictionary();

    /**
     * Returns the ItaijiDictionary instance.
     */
    static ItaijiDictionary getInstance() {
        return instance;
    }

    private final Map table = new HashMap();

    /**
     * Constructs a ItaijiDictionary object.
     */
    private ItaijiDictionary() {
        try {
            initialize();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * loads the dictionary file.
     */
    private void initialize() throws IOException {
        InputStream in;
        String path = System.getProperty("kakasi.itaijiDictionary.path");
        if (path != null) {
            in = new FileInputStream(path);
        } else {
            in = ItaijiDictionary.class.getResourceAsStream(
                "resources/itaijidict");
        }
        String encoding =
            System.getProperty("kakasi.itaijiDictionary.encoding");
        if (encoding == null) {
            encoding = "JISAutoDetect";
        }
        try {
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(in, encoding));
            try {
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    line = line.trim();
                    if (line.length() == 0) {
                        continue;
                    }
                    if (line.length() != 2) {
                        System.err.println("ItaijiDictionary: Ignored line: " +
                                           line);
                    }
                    table.put(new Character(line.charAt(0)),
                              new Character(line.charAt(1)));
                }
            } finally {
                try {
                    reader.close();
                    in = null;
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * If the specified character is registerd in the Itaiji dictionary,
     * returns the value, else returns the specified character.
     *
     * @param ch  the character.
     */
    char get(char ch) {
        Character changed = (Character)table.get(new Character(ch));
        return changed == null ? ch : changed.charValue();
    }

}
