/*
 * $Id: KanwaDictionary.java,v 1.6 2003/01/01 08:54:30 kawao Exp $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * This class represents the Kanwa dictionary.
 *
 * @see Kakasi#getKanwaDictionary()
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.6 $ $Date: 2003/01/01 08:54:30 $
 */
public class KanwaDictionary {

    private final Map contentsTable = new HashMap(8192);

    private Map entryTable;
    private Set loadedKyes;

    private RandomAccessFile file;

    /**
     * Reads and adds dictionary entries from the file.
     * The file encoding is "JISAutoDetect".
     * 
     * @param filename  the file name.
     * @exception  IOException  if an error occurred when opening or reading
     *               the file.
     */
    public void load(String filename) throws IOException {
        load(filename, "JISAutoDetect");
    }

    /**
     * Reads and adds dictionary entries from the file with the specified
     * encoding.
     *
     * @param filename  the file name.
     * @param encoding  the file encoding.
     * @exception  IOException  if an error occurred when opening or reading
     *               the file.
     */
    public void load(String filename, String encoding) throws IOException {
        InputStream in = new FileInputStream(filename);
        try {
            Reader reader = new InputStreamReader(in, encoding);
            try {
                load(reader);
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
     * Reads and adds dictionary entries from the reader.
     *
     * @param reader  the reader object.
     * @exception  IOException  if an error occurred when reading from
     *               the reader.
     */
    public void load(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        while (true) {
            String line = in.readLine();
            if (line == null) {
                break;
            }
            int length = line.length();
            if (length == 0) {
                continue;
            }
            Character.UnicodeBlock yomiBlock =
                Character.UnicodeBlock.of(line.charAt(0));
            if (!yomiBlock.equals(Character.UnicodeBlock.HIRAGANA) &&
                !yomiBlock.equals(Character.UnicodeBlock.KATAKANA)) {
                continue;
            }
            StringBuffer yomiBuffer = new StringBuffer();
            yomiBuffer.append(line.charAt(0));
            int index = 1;
            for (; index < length; index++) {
                char ch = line.charAt(index);
                if (" ,\t".indexOf(ch) >= 0) {
                    break;
                }
                yomiBuffer.append(ch);
            }
            if (index >= length) {
                System.err.println("KanwaDictionary: Ignored line: " + line);
                continue;
            }
            char okurigana = '\u0000';
            char yomiLast = yomiBuffer.charAt(index - 1);
            if (yomiLast >= 'a' && yomiLast <= 'z') {
                okurigana = yomiLast;
                yomiBuffer.setLength(index - 1);
            }
            String yomi = yomiBuffer.toString();
            for (++index; index < length; index++) {
                char ch = line.charAt(index);
                if (" ,\t".indexOf(ch) < 0) {
                    break;
                }
            }
            if (index >= length) {
                System.err.println("KanwaDictionary: Ignored line: " + line);
                continue;
            }
            if (line.charAt(index) == '/') {
            SKK_LOOP:
                while (true) {
                    StringBuffer kanji = new StringBuffer();
                    for (++index; index < length; index++) {
                        char ch = line.charAt(index);
                        if (ch == '/') {
                            break;
                        }
                        if (ch == ';') {
                            index = length - 1;
                            break;
                        }
                        if (ch == '[') {
                            break SKK_LOOP;
                        }
                        kanji.append(ch);
                    }
                    if (index >= length) {
                        break;
                    }
                    addItem(kanji.toString(), yomi, okurigana);
                }
            } else {
                StringBuffer kanji = new StringBuffer();
                kanji.append(line.charAt(index));
                for (++index; index < length; index++) {
                    char ch = line.charAt(index);
                    if (" ,\t".indexOf(ch) >= 0) {
                        break;
                    }
                    kanji.append(ch);
                }
                addItem(kanji.toString(), yomi, okurigana);
            }
        }
    }

    /**
     * Adds dictionary entry.
     *
     * @param kanji  the kanji string.
     * @param yomi  the yomi string.
     * @param okurigana  the okurigana character.
     */
    public synchronized void addItem(String kanji,
                                     String yomi,
                                     char okurigana) {
        Character.UnicodeBlock kanjiBlock =
            Character.UnicodeBlock.of(kanji.charAt(0));
        if (!kanjiBlock.equals(
                Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)) {
            //System.err.println("KanwaDictionary: Ignored item:" +
            //                   " kanji=" + kanji + " yomi=" + yomi);
            return;
        }
        int kanjiLength = kanji.length();
        StringBuffer kanjiBuffer = new StringBuffer(kanjiLength);
        for (int index = 0; index < kanjiLength; index++) {
            char ch = kanji.charAt(index);
            //if (ch < '\u0100') {
            //    System.err.println("KanwaDictionary: Ignored item:" +
            //                       " kanji=" + kanji + " yomi=" + yomi);
            //    return;
            //}
            kanjiBuffer.append(ItaijiDictionary.getInstance().get(ch));
        }
        Character key = new Character(kanjiBuffer.charAt(0));
        kanji = kanjiBuffer.substring(1);

        int yomiLength = yomi.length();
        StringBuffer yomiBuffer = new StringBuffer(yomiLength);
        for (int index = 0; index < yomiLength; index++) {
            char ch = yomi.charAt(index);
            Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
            if (!block.equals(Character.UnicodeBlock.HIRAGANA) &&
                !block.equals(Character.UnicodeBlock.KATAKANA)) {
                System.err.println("KanwaDictionary: Ignored item:" +
                                   " kanji=" + kanjiBuffer + " yomi=" + yomi);
                return;
            }
            if ((ch >= '\u30a1' && ch <= '\u30f3') ||
                ch == '\u30fd' || ch == '\u30fe') {
                yomiBuffer.append((char)(ch - 0x60));
            } else if (ch == '\u30f4') {	// 'vu'
                yomiBuffer.append('\u3046');
                yomiBuffer.append('\u309b');
            } else {
                yomiBuffer.append(ch);
            }
        }
        yomi = yomiBuffer.toString();   

        KanjiYomi kanjiYomi = new KanjiYomi(kanji, yomi, okurigana);
        Set list = (Set)contentsTable.get(key);
        if (list == null) {
            list = new TreeSet();
            contentsTable.put(key, list);
        }
        list.add(kanjiYomi);
    }

    /**
     * Looks up the specified character.
     *
     * @param k  the character to look up.
     * @return  the iterator of KanjiYomi obujects.
     * @exception  IOException  if an error occurred when reading kanwa
     *               dictionary file.
     */
    synchronized Iterator lookup(char k) throws IOException {
        if (entryTable == null) {
            initialize();
        }
        Character key = new Character(k);
        Set list = (Set)contentsTable.get(key);
        if (list == null) {
            list = new TreeSet();
            contentsTable.put(key, list);
        }
        if (file != null && !loadedKyes.contains(key)) {
            KanwaEntry entry = (KanwaEntry)entryTable.get(key);
            if (entry != null) {
                file.seek(entry.getOffset());
                int numWords = entry.getNumberOfWords();
                for (int index = 0; index < numWords; index++) {
                    String kanji = file.readUTF();
                    String yomi = file.readUTF();
                    char okurigana = (char)file.readByte();
                    list.add(new KanjiYomi(kanji, yomi, okurigana));
                }
            }
            loadedKyes.add(key);
        }
        return list.iterator();
    }

    /**
     * Initializes this object.
     *
     * @exception  IOException  if an error occurred when reading kanwa
     *               dictionary file.
     */
    private void initialize() throws IOException {
        String path = System.getProperty("kakasi.kanwaDictionary");
        if (path == null) {
            String home = System.getProperty("kakasi.home");
            path = home + "/lib/kanwadict";
        }
        file = new RandomAccessFile(path, "r");
        int numKanji = file.readInt();
        entryTable = new HashMap(numKanji);
        loadedKyes = new HashSet(numKanji);
        for (int index = 0; index < numKanji; index++) {
            Character key = new Character(file.readChar());
            int offset = file.readInt();
            int numWords = file.readShort();
            entryTable.put(key, new KanwaEntry(offset, numWords));
        }
    }

    /**
     * Closes the dictionary file.
     *
     * @exception  IOException  if an error occurred when closing kanwa
     *               dictionary file.
     */
    public synchronized void close() throws IOException {
        if (file != null) {
            file.close();
            file = null;
        }
    }

    /**
     * Called when there are no more references to this object.
     *
     * @exception Throwable  the Exception raised by this method
     */
    public void finalize() throws Throwable {
        close();
        super.finalize();
    }

    /**
     * Saves this Kanwa dictionary to the specified file.
     *
     * @param file  the destination file.
     * @exception  IOException  if an error occurred when writing to the file.
     */
    public synchronized void save(RandomAccessFile file) throws IOException {
        int numKanji = contentsTable.size();
        entryTable = new HashMap(numKanji);
        file.writeInt(numKanji);
        file.seek(4 + 8 * numKanji);
        Iterator keys = contentsTable.keySet().iterator();
        while (keys.hasNext()) {
            int offset = (int)file.getFilePointer();
            Character key = (Character)keys.next();
            Set list = (Set)contentsTable.get(key);
            Iterator kanjiYomis = list.iterator();
            while (kanjiYomis.hasNext()) {
                KanjiYomi kanjiYomi = (KanjiYomi)kanjiYomis.next();
                file.writeUTF(kanjiYomi.getKanji());
                file.writeUTF(kanjiYomi.getYomi());
                file.writeByte(kanjiYomi.getOkurigana());
           }
            KanwaEntry entry = new KanwaEntry(offset, list.size());
            entryTable.put(key, entry);
        }
        file.seek(4);
        keys = entryTable.keySet().iterator();
        while (keys.hasNext()) {
            Character key = (Character)keys.next();
            file.writeChar(key.charValue());
            KanwaEntry entry = (KanwaEntry)entryTable.get(key);
            file.writeInt(entry.getOffset());
            file.writeShort(entry.getNumberOfWords());
        }
    }

    /**
     * Main program of 'mkkanwa_j'.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2 || args[0].equals("-h")) {
            System.err.println("usage: mkkanwa_j kanwadict dict1 [dict2,,,]");
            System.exit(1);
        }
        KanwaDictionary dictionary = new KanwaDictionary();
        for (int index = 1; index < args.length; index++) {
            dictionary.load(args[index]);
        }
        RandomAccessFile file = new RandomAccessFile(args[0], "rw");
        try {
            dictionary.save(file);
        } finally {
            try {
                file.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

}
