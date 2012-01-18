/*
 * $Id: Kakasi.java,v 1.15 2003/03/01 12:52:26 kawao Exp $
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
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.io.IOException;

/**
 * This class is the KAKASI/JAVA main class.
 * 
 * @author  Kawao, Tomoyuki (kawao@kawao.com)
 * @version $Revision: 1.15 $ $Date: 2003/03/01 12:52:26 $
 */
public class Kakasi {

    /** Character Set ID of ASCII */
    public static final String ASCII = "ascii";

    /** Character Set ID of Kanji */
    public static final String KANJI = "kanji";

    /** Character Set ID of Hiragana */
    public static final String HIRAGANA = "hiragana";

    /** Character Set ID of Katakana */
    public static final String KATAKANA = "katakana";

    /** Romaji type of Hepburn */
    public static final int HEPBURN = KanaToRomaConverterImpl.HEPBURN;

    /** Romaji type of Kunrei */
    public static final int KUNREI = KanaToRomaConverterImpl.KUNREI;

    private static final Converter defaultConverter = new DefaultConverter();

    private final KanjiInput input = new KanjiInput();
    private final KanjiOutput output = new KanjiOutput();

    private final KanwaDictionary kanwaDictionary;
    private final KanjiConverterImpl kanjiConverterImpl;
    private final HiraganaConverterImpl hiraganaConverterImpl;
    private final KatakanaConverterImpl katakanaConverterImpl;
    private final KanaToRomaConverterImpl kanaToRomaConverterImpl;

    private Converter kanjiConverter;
    private Converter hiraganaConverter;
    private Converter katakanaConverter;

    private boolean wakachigakiMode;

    /**
     * Constructs a Kakasi object.
     */
    public Kakasi() {
        this(null);
    }

    /**
     * Constructs a Kakasi object with the specified kanwa dictionary.
     *
     * @param kanwaDictionary  the KanwaDictionary object.
     */
    public Kakasi(KanwaDictionary kanwaDictionary) {
        this.kanwaDictionary =
            kanwaDictionary == null ? new KanwaDictionary() : kanwaDictionary;
        kanjiConverterImpl = new KanjiConverterImpl(this.kanwaDictionary);
        hiraganaConverterImpl = new HiraganaConverterImpl();
        katakanaConverterImpl = new KatakanaConverterImpl();
        kanaToRomaConverterImpl = new KanaToRomaConverterImpl();
    }

    /**
     * Prepares the kanji converter.
     *
     * @param  characterSet  the destination character set ID.
     * @see #ASCII
     * @see #KANJI
     * @see #HIRAGANA
     * @see #KATAKANA
     */
    public void setupKanjiConverter(String characterSet) {
        kanjiConverter = characterSet == null ?
            null : createKanjiConverter(characterSet);
    }

    /**
     * Creates the kanji converter that converts kanji to the specified
     * character set.
     *
     * @param  characterSet  the destination character set ID.
     */
    private Converter createKanjiConverter(String characterSet) {
        if (characterSet.equals(ASCII)) {
            return new CompoundConverter(createKanjiConverter(HIRAGANA),
                                         createHiraganaConverter(ASCII));
        } else if (characterSet.equals(KANJI)) {
            return new Converter() {
                public boolean convert(KanjiInput input, Writer output)
                    throws IOException {
                    return kanjiConverterImpl.toKanji(input, output);
                }
            };
        } else if (characterSet.equals(HIRAGANA)) {
            return new Converter() {
                public boolean convert(KanjiInput input, Writer output)
                    throws IOException {
                    return kanjiConverterImpl.toHiragana(input, output);
                }
            };
        } else if (characterSet.equals(KATAKANA)) {
            return new CompoundConverter(createKanjiConverter(HIRAGANA),
                                         createHiraganaConverter(KATAKANA));
        } else {
            String message =
                "KanjiConverter does not support character set: " +
                characterSet;
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Prepares the hiragana converter.
     *
     * @param  characterSet  the destination character set ID.
     * @see #ASCII
     * @see #HIRAGANA
     * @see #KATAKANA
     */
    public void setupHiraganaConverter(String characterSet) {
        hiraganaConverter = characterSet == null ?
            null : createHiraganaConverter(characterSet);
    }

    /**
     * Creates the hiragana converter that converts hiragana to the specified
     * character set.
     *
     * @param  characterSet  the destination character set ID.
     */
    private Converter createHiraganaConverter(String characterSet) {
        if (characterSet.equals(ASCII)) {
            return new Converter() {
                public boolean convert(KanjiInput input, Writer output)
                    throws IOException {
                    return kanaToRomaConverterImpl.convertHiragana(input,
                                                                   output);
                }
            };
        } else if (characterSet.equals(HIRAGANA)) {
            return new Converter() {
                public boolean convert(KanjiInput input, Writer output)
                    throws IOException {
                    return hiraganaConverterImpl.toHiragana(input, output);
                }
            };
        } else if (characterSet.equals(KATAKANA)) {
            return new Converter() {
                public boolean convert(KanjiInput input, Writer output)
                    throws IOException {
                    return hiraganaConverterImpl.toKatakana(input, output);
                }
            };
        } else {
            String message =
                "HiraganaConverter does not support character set: " +
                characterSet;
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Prepares the katakana converter.
     *
     * @param  characterSet  the destination character set ID.
     * @see #ASCII
     * @see #HIRAGANA
     * @see #KATAKANA
     */
    public void setupKatakanaConverter(String characterSet) {
        katakanaConverter = characterSet == null ?
            null : createKatakanaConverter(characterSet);
    }

    /**
     * Creates the katakana converter that converts katakana to the specified
     * character set.
     *
     * @param  characterSet  the destination character set ID.
     */
    private Converter createKatakanaConverter(String characterSet) {
        if (characterSet.equals(ASCII)) {
            return new Converter() {
                public boolean convert(KanjiInput input, Writer output)
                    throws IOException {
                    return kanaToRomaConverterImpl.convertKatakana(input,
                                                                   output);
                }
            };
        } else  if (characterSet.equals(HIRAGANA)) {
            return new Converter() {
                public boolean convert(KanjiInput input, Writer output)
                    throws IOException {
                    return katakanaConverterImpl.toHiragana(input, output);
                }
            };
        } else if (characterSet.equals(KATAKANA)) {
            return new Converter() {
                public boolean convert(KanjiInput input, Writer output)
                    throws IOException {
                    return katakanaConverterImpl.toKatakana(input, output);
                }
            };
        } else {
            String message =
                "KatakanaConverter does not support character set: " +
                characterSet;
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Gets the Kanwa dictionary object.
     */
    public KanwaDictionary getKanwaDictionary() {
        return kanwaDictionary;
    }

    /**
     * Gets the input object.
     */
    public KanjiInput getInput() {
        return input;
    }

    /**
     * Gets the output object.
     */
    public KanjiOutput getOutput() {
        return output;
    }

    /**
     * Sets the heiki mode property. The default value is false.
     *
     * @param newMode  if true, lists all readings with Kanji convertsion.
     */
    public void setHeikiMode(boolean newMode) {
        kanjiConverterImpl.setHeikiMode(newMode);
    }

    /**
     * Gets the heiki mode property value.
     */
    public boolean isHeikiMode() {
        return kanjiConverterImpl.isHeikiMode();
    }

    /**
     * Sets the furigana mode property. The default value is false.
     *
     * @param newMode  new furigana mode value.
     */
    public void setFuriganaMode(boolean newMode) {
        kanjiConverterImpl.setFuriganaMode(newMode);
    }

    /**
     * Gets the furigana mode property value.
     */
    public boolean isFuriganaMode() {
        return kanjiConverterImpl.isFuriganaMode();
    }

    /**
     * Sets the wakachigaki mode property. The default value is false.
     * 
     * @param newMode  new wakachigaki mode value.
     */
    public void setWakachigakiMode(boolean newMode) {
        if (newMode) {
            output.setSplitMode(true);
            setupKanjiConverter(KANJI);
            setupHiraganaConverter(HIRAGANA);
            setupKatakanaConverter(KATAKANA);
        } else {
            output.setSplitMode(false);
            setupKanjiConverter(null);
            setupHiraganaConverter(null);
            setupKatakanaConverter(null);
        }
        wakachigakiMode = newMode;
    }

    /**
     * Gets the wakachigaki mode property value.
     */
    public boolean isWakachigakiMode() {
        return wakachigakiMode;
    }

    /**
     * Sets the Romaji type property value. The default value is HEPBURN.
     *
     * @param newType  new romaji type.
     * @see #HEPBURN
     * @see #KUNREI
     */
    public void setRomajiType(int newType) {
        kanaToRomaConverterImpl.setType(newType);
    }
    
    /**
     * Gets the Romaji type property value.
     *
     * @see #HEPBURN
     * @see #KUNREI
     */
    public int getRomajiType() {
        return kanaToRomaConverterImpl.getType();
    }

    /**
     * Sets the romaji capitalize mode property. The default value is false.
     *
     * @param newMode  new romaji capitalize mode value.
     */
    public void setRomajiCapitalizeMode(boolean newMode) {
        kanaToRomaConverterImpl.setCapitalizeMode(newMode);
    }

    /**
     * Gets the romaji capitalize mode property value.
     */
    public boolean isRomajiCapitalizeMode() {
        return kanaToRomaConverterImpl.isCapitalizeMode();
    }

    /**
     * Sets the romaji upper case mode property. The default value is false.
     *
     * @param newMode  new romaji upper case mode value.
     */
    public void setRomajiUpperCaseMode(boolean newMode) {
        kanaToRomaConverterImpl.setUpperCaseMode(newMode);
    }

    /**
     * Gets the romaji upper case mode property value.
     */
    public boolean isRomajiUpperCaseMode() {
        return kanaToRomaConverterImpl.isUpperCaseMode();
    }

    /**
     * Processes the specified string.
     *
     * @param string  the input string to process.
     * @return  the result string.
     * @exception  IOException  if an I/O error occurred.
     */
    public synchronized String doString(String string) throws IOException {
        input.setInputString(string);
        StringWriter writer = new StringWriter(string.length() * 2);
        output.setWriter(writer);
        run();
        return writer.toString();
    }

    /**
     * Runs the conversion process.
     *
     * @exception  IOException  if an I/O error occurred.
     */
    public synchronized void run() throws IOException {
        while (true) {
            int ch = input.get();
            if (ch < 0) {
                break;
            }
            Converter converter = null;
            Character.UnicodeBlock block = Character.UnicodeBlock.of((char)ch);
            if (block.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)) {
                converter = kanjiConverter;
            } else if (block.equals(Character.UnicodeBlock.HIRAGANA)) {
                converter = hiraganaConverter;
            } else if (block.equals(Character.UnicodeBlock.KATAKANA)) {
                converter = katakanaConverter;
            }
            if (converter == null) {
                converter = defaultConverter;
            }
            output.putSeparator();
            if (!converter.convert(input, output)) {
                input.consume(1);
                if (wakachigakiMode) {
                    output.write((char)ch);
                }
            }
        }
        output.flush();
    }

    /**
     * Main program of 'kakasi_j'.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) throws Throwable {
        Kakasi kakasi = new Kakasi();
        int index = 0;
        for (; index < args.length; index++) {
            if (args[index].charAt(0) != '-') {
                break;
            }
            int length = args[index].length();
            if (length < 2) {
                usage();
            }
            String characterSet = null;
            String encoding = null;
            String typeString = null;
            switch (args[index].charAt(1)) {
            case 'J':
                if (length < 3) {
                    usage();
                }
                switch (args[index].charAt(2)) {
                case 'H':
                    characterSet = HIRAGANA;
                    break;
                case 'K':
                    characterSet = KATAKANA;
                    break;
                case 'a':
                    characterSet = ASCII;
                    break;
                default:
                    usage();
                }
                kakasi.setupKanjiConverter(characterSet);
                break;
            case 'H':
                if (length < 3) {
                    usage();
                }
                switch (args[index].charAt(2)) {
                case 'K':
                    characterSet = KATAKANA;
                    break;
                case 'a':
                    characterSet = ASCII;
                    break;
                default:
                    usage();
                }
                kakasi.setupHiraganaConverter(characterSet);
                break;
            case 'K':
                if (length < 3) {
                    usage();
                }
                switch (args[index].charAt(2)) {
                case 'H':
                    characterSet = HIRAGANA;
                    break;
                case 'a':
                    characterSet = ASCII;
                    break;
                default:
                    usage();
                }
                kakasi.setupKatakanaConverter(characterSet);
                break;
            case 'i':
                if (length > 2) {
                    encoding = args[index].substring(2);
                } else if (++index < args.length) {
                    encoding = args[index];
                } else {
                    usage();
                }
                kakasi.getInput().setReader(
                    new BufferedReader(
                        new InputStreamReader(System.in, encoding)));
                break;
            case 'o':
                if (length > 2) {
                    encoding = args[index].substring(2);
                } else if (++index < args.length) {
                    encoding = args[index];
                } else {
                    usage();
                }
                kakasi.getOutput().setWriter(
                    new BufferedWriter(
                        new OutputStreamWriter(System.out, encoding)));
                break;
            case 'p':
                kakasi.setHeikiMode(true);
                break;
            case 'f':
                kakasi.setFuriganaMode(true);
                break;
            case 'c':
                kakasi.getInput().setSpaceEatMode(true);
                break;
            case 's':
                kakasi.getOutput().setSplitMode(true);
                break;
            case 'b':
                kakasi.getOutput().setAutoFlushMode(false);
                break;
            case 'r':
                if (length > 2) {
                    typeString = args[index].substring(2);
                } else if (++index < args.length) {
                    typeString = args[index];
                } else {
                    usage();
                }
                if ("hepburn".equalsIgnoreCase(typeString)) {
                    kakasi.setRomajiType(HEPBURN);
                } else if ("kunrei".equalsIgnoreCase(typeString)) {
                    kakasi.setRomajiType(KUNREI);
                } else {
                    usage();
                }
                break;
            case 'C':
                kakasi.setRomajiCapitalizeMode(true);
                break;
            case 'U':
                kakasi.setRomajiUpperCaseMode(true);
                break;
            case 'w':
                kakasi.setWakachigakiMode(true);
                break;
            default:
                usage();
            }
        }
        KanwaDictionary kanwaDictionary = kakasi.getKanwaDictionary();
        for (; index < args.length; index++) {
            kanwaDictionary.load(args[index]);
        }
        kakasi.run();
        kanwaDictionary.close();
    }

    /**
     * Prints usage and exit.
     */
    private static void usage() {
        Package pkg = Package.getPackage("com.kawao.kakasi");
        String version = pkg.getImplementationVersion();
        System.err.println("KAKASI/Java  version " + version);
        System.err.println(
            "Copyright (C) 2002-2003 KAWAO, Tomoyuki. All rights reserved.");
        System.err.println();
        System.err.println(
            "Usage: kakasi_j [-JH | -JK | -Ja] [-HK | -Ha] [-KH | -Ka]");
        System.err.println("\t\t[-i<input-encoding>] [-o<output-encoding>]");
        System.err.println("\t\t[-p] [-f] [-c] [-s] [-b]");
        System.err.println("\t\t[-r{hepburn|kunrei}] [-C | -U] [-w]");
        System.err.println("\t\t[dictionary1 [dictionary2 [,,,]]]");
        System.err.println();
        System.err.println("\tCharacter Set Conversions:");
        System.err.println("\t -JH: kanji to hiragana");
        System.err.println("\t -JK: kanji to katakana");
        System.err.println("\t -Ja: kanji to romaji");
        System.err.println("\t -HK: hiragana to katakana");
        System.err.println("\t -Ha: hiragana to romaji");
        System.err.println("\t -KH: katakana to hiragana");
        System.err.println("\t -Ka: katakana to romaji");
        System.err.println();
        System.err.println("\tOptions:");
        System.err.println("\t -i: input encoding");
        System.err.println("\t -o: output encoding");
        System.err.println("\t -p: list all readings (with -J option)");
        System.err.println("\t -f: furigana mode (with -J option)");
        System.err.println("\t -c: skip whitespace chars within jukugo");
        System.err.println("\t -s: insert separate characters");
        System.err.println(
            "\t -b: output buffer is not flushed when a newline character is written");
        System.err.println("\t -r: romaji conversion system");
        System.err.println("\t -C: romaji Capitalize");
        System.err.println("\t -U: romaji Uppercase");
        System.err.println("\t -w: wakachigaki mode");

        System.err.println();
        System.err.println("Report bugs to <kawao@kawao.com>.");
        System.exit(1);
    }

}
