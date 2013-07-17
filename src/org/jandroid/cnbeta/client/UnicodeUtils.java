package org.jandroid.cnbeta.client;

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

}
