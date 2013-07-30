package org.jandroid.cnbeta.client;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class UnicodeUtils {

    /**
     * ����ת��unicode
     * @param chineseString
     * @return
     */
    public static String chinese2Unicode(String chineseString) {
        char[] myBuffer = chineseString.toCharArray();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chineseString.length(); i++) {
            char ch = myBuffer[i];
            if ((int) ch < 10) {
                sb.append("\\u000" + (int) ch);
                continue;
            }
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
            if (ub == Character.UnicodeBlock.BASIC_LATIN) {
                //Ӣ�ļ����ֵ�
                sb.append(myBuffer[i]);
            }
            else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                //ȫ�ǰ���ַ�
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            }
            else {
                //����
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * unicode ת���� ����
     */
    public static String unicode2Chinese(String unicodeString) {
        char aChar;
        int len = unicodeString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = unicodeString.charAt(x++);
            if (aChar == '\\') {
                aChar = unicodeString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = unicodeString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                }
                else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            }
            else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

        /**
         * ����תunicode
         * @param str
         * @return ����unicode����
         */
        public static String  chinaToUnicode(String str)
        {
            String result = null;
            for (int i = 0; i < str.length(); i++)
            {
                int chr1 = (char) str.charAt(i);
                result  += "\\u" + Integer.toHexString(chr1);
            }
            return result;
        }
    
    
    public static String unicode2Chinese2(String str) throws IOException {
        StringReader sr = new StringReader(str);
        UnicodeUnescapeReader uur = new UnicodeUnescapeReader(sr);

        StringBuffer buf = new StringBuffer();
        for(int c = uur.read(); c != -1; c = uur.read())
        {
            buf.append((char)c);
        }
        return buf.toString();
    }
}

/**
 * Reader transforming unicode escape sequences (i.e \u0065) in the provided
 * stream into the corresponding unicode character.
 *
 * @author Emmanuel Bourg
 * @version $Revision: 1300540 $, $Date: 2012-03-14 13:38:43 +0000 (Wed, 14 Mar 2012) $
 */
class UnicodeUnescapeReader extends Reader {
    private final PushbackReader reader;

    /** The buffer used to read unicode escape sequences. */
    private final char[] sequence = new char[5];

    UnicodeUnescapeReader(Reader reader) {
        this.reader = new PushbackReader(reader, sequence.length);
    }

    @Override
    public int read(char[] cbuf, int offset, int length) throws IOException {
        int count = 0;
        for (int i = 0; i < length; i++) {
            int c = reader.read();

            if (c == -1) {
                return count == 0 ? -1 : count;
            }

            if (c == '\\') {
                int len = reader.read(sequence);
                if (len == sequence.length && isUnicodeSequence(sequence)) {
                    // unicode escape found
                    c = Integer.parseInt(new String(sequence, 1, 4), 16);

                } else if (len > 0) {
                    // put the characters back in the stream
                    reader.unread(sequence, 0, len);
                }
            }

            cbuf[offset + i] = (char) c;
            count++;
        }

        return count;
    }

    private boolean isUnicodeSequence(char[] sequence) {
        return 'u' == sequence[0]
                && isHexadecimal(sequence[1])
                && isHexadecimal(sequence[2])
                && isHexadecimal(sequence[3])
                && isHexadecimal(sequence[4]);
    }

    private boolean isHexadecimal(char c) {
        return ('0' <= c && c <= '9') || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }
}
