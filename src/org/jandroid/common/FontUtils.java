package org.jandroid.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class FontUtils {

    private static String lastFont = "default";
    private static Typeface lastTypeface;

    public static TTFParser parseFontFile(File file) throws IOException {
        TTFParser parser = new TTFParser();
        parser.parse(file);
        return parser;
    }

    public static Typeface loadTypeface(Activity activity, String fontPath) {
        //默认字体
        if(fontPath == null || fontPath.isEmpty() || fontPath.equals("default")){
            return Typeface.DEFAULT;
        }
        else {
            try {
                if (!fontPath.equals(lastFont)) {
                    Typeface typeface;
                    if (fontPath.contains("/android_asset/")) {
                        typeface = Typeface.createFromAsset(activity.getAssets(), fontPath.substring("file:///android_asset/".length()));
                    }
                    else {
                        typeface = Typeface.createFromFile(new File(fontPath.substring("file://".length())));
                    }
                    lastFont = fontPath;
                    lastTypeface = typeface;

                }
                return lastTypeface;
            }
            catch (Exception e) {
                ToastUtils.showShortToast(activity, "读取字体时发生异常, " + e.toString());
                return null;
            }
        }
    }


    public static void updateFont(final View root, final Typeface typeface) {
        if(typeface == null) return;
        if (root instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup)root).getChildCount(); i++) {
                View v = ((ViewGroup)root).getChildAt(i);
                updateFont(v, typeface);
            }
        }
        else {
            if(root instanceof TextView) {
                if(!typeface.equals(((TextView) root).getTypeface())) {
                    ((TextView) root).setTypeface(typeface);
                }
            }
            else if (root instanceof WebView) {
                //TODO: Has to update font of webview in a separate way by CSS/JS
            }
        }
    }

    public static void updateTextSize(Context theContext, TextView textView, int dimResourceId, int spOffsetSize) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, PixelUtils.pixelsToSp(theContext, theContext.getResources().getDimension(dimResourceId)) + spOffsetSize);
    }

    public static void updateTextSize(Context theContext, WebView webView, int dimResourceId, int spOffsetSize) {
//        webView.getSettings().setDefaultFontSize((int)theContext.getResources().getDimension(dimResourceId) + (int)theContext.getResources().getDisplayMetrics().scaledDensity * spOffsetSize);
//        webView.getSettings().setDefaultFixedFontSize((int)theContext.getResources().getDimension(dimResourceId) + (int)theContext.getResources().getDisplayMetrics().scaledDensity * spOffsetSize);
        webView.getSettings().setTextZoom(100 + (spOffsetSize*10));
    }

    public static void updateText(Context theContext, TextView textView, int dimResourceId, int spOffsetSize, Typeface typeface) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, PixelUtils.pixelsToSp(theContext, theContext.getResources().getDimension(dimResourceId)) + spOffsetSize);
        updateFont(textView, typeface);
    }


    /**
     * TTF Font file parser
     * <p/>
     * <p/>
     * <p/>
     * sample:
     * <p/>
     * <code><pre>
     * <p/>
     *                          File fs = new File("C:\\Windows\\Fonts");
     * <p/>
     *                          File[] files = fs.listFiles(new FilenameFilter() {
     * <p/>
     *                              public boolean accept(File dir, String name) {
     * <p/>
     *                                  if (name.endsWith("ttf")){ return true;}
     * <p/>
     *                                  return false;
     * <p/>
     *                              }
     * <p/>
     *                          });
     * <p/>
     *                          for (File file : files) {
     * <p/>
     *                              TTFParser parser = new TTFParser();
     * <p/>
     *                              parser.parse(file.getAbsolutePath());
     * <p/>
     *                              System.out.println("font name: " + parser.getFontName());
     * <p/>
     *                          }
     * <p/>
     * </pre></code>
     * <p/>
     * <p/>
     * <p/>
     * Copyright: Copyright (c) 12-8-6 下午3:51
     * <p/>
     * <p/>
     * <p/>
     * Version: 1.0
     * <p/>
     * <p/>
     */

    public static class TTFParser {

        public static int COPYRIGHT = 0;

        public static int FAMILY_NAME = 1;

        public static int FONT_SUBFAMILY_NAME = 2;

        public static int UNIQUE_FONT_IDENTIFIER = 3;

        public static int FULL_FONT_NAME = 4;

        public static int VERSION = 5;

        public static int POSTSCRIPT_NAME = 6;

        public static int TRADEMARK = 7;

        public static int MANUFACTURER = 8;

        public static int DESIGNER = 9;

        public static int DESCRIPTION = 10;

        public static int URL_VENDOR = 11;

        public static int URL_DESIGNER = 12;

        public static int LICENSE_DESCRIPTION = 13;

        public static int LICENSE_INFO_URL = 14;


        private Map<Integer, String> fontProperties = new HashMap<Integer, String>();


        /**
         * 获取ttf font name
         *
         * @return
         */

        public String getFontName() {

            if (fontProperties.containsKey(FULL_FONT_NAME)) {

                return fontProperties.get(FULL_FONT_NAME);

            }
            else if (fontProperties.containsKey(FAMILY_NAME)) {

                return fontProperties.get(FAMILY_NAME);

            }
            else {

                return null;

            }

        }


        /**
         * 获取ttf属性
         *
         * @param nameID 属性标记，见静态变量
         * @return 属性值
         */

        public String getFontPropertie(int nameID) {

            if (fontProperties.containsKey(nameID)) {

                return fontProperties.get(nameID);

            }
            else {
                return null;
            }

        }


        /**
         * 获取ttf属性集合
         *
         * @return 属性集合(MAP)
         */


        public Map<Integer, String> getFontProperties() {
            return fontProperties;
        }

        public void parse(File file) throws IOException {
            fontProperties.clear();

            RandomAccessFile f = null;

            try {

                f = new RandomAccessFile(file, "r");

                parseInner(f);

            }
            finally {

                try {

                    f.close();

                }
                catch (Exception e) {

                    //ignore;

                }

            }

        }

        /**
         * 执行解析
         *
         * @param fileName ttf文件名
         * @throws IOException
         */

        public void parse(String fileName) throws IOException {
            parse(new File(fileName));
        }


        private void parseInner(RandomAccessFile randomAccessFile) throws IOException {

            int majorVersion = randomAccessFile.readShort();

            int minorVersion = randomAccessFile.readShort();

            int numOfTables = randomAccessFile.readShort();

            if (majorVersion != 1 || minorVersion != 0) {
                return;
            }


            //jump to TableDirectory struct

            randomAccessFile.seek(12);


            boolean found = false;

            byte[] buff = new byte[4];

            TableDirectory tableDirectory = new TableDirectory();

            for (int i = 0; i < numOfTables; i++) {

                randomAccessFile.read(buff);

                tableDirectory.name = new String(buff);

                tableDirectory.checkSum = randomAccessFile.readInt();

                tableDirectory.offset = randomAccessFile.readInt();

                tableDirectory.length = randomAccessFile.readInt();

                if ("name".equalsIgnoreCase(tableDirectory.name)) {

                    found = true;

                    break;

                }
                else if (tableDirectory.name == null || tableDirectory.name.length() == 0) {

                    break;

                }

            }


            // not found table of name

            if (!found) {
                return;
            }

            randomAccessFile.seek(tableDirectory.offset);

            NameTableHeader nameTableHeader = new NameTableHeader();

            nameTableHeader.fSelector = randomAccessFile.readShort();

            nameTableHeader.nRCount = randomAccessFile.readShort();

            nameTableHeader.storageOffset = randomAccessFile.readShort();

            NameRecord nameRecord = new NameRecord();

            for (int i = 0; i < nameTableHeader.nRCount; i++) {

                nameRecord.platformID = randomAccessFile.readShort();

                nameRecord.encodingID = randomAccessFile.readShort();

                nameRecord.languageID = randomAccessFile.readShort();

                nameRecord.nameID = randomAccessFile.readShort();

                nameRecord.stringLength = randomAccessFile.readShort();

                nameRecord.stringOffset = randomAccessFile.readShort();

                long pos = randomAccessFile.getFilePointer();

                byte[] bf = new byte[nameRecord.stringLength];

                long vpos = tableDirectory.offset + nameRecord.stringOffset + nameTableHeader.storageOffset;

                randomAccessFile.seek(vpos);

                randomAccessFile.read(bf);

                String temp = new String(bf, Charset.forName("utf-16"));

                fontProperties.put(nameRecord.nameID, temp);

                randomAccessFile.seek(pos);

            }

        }


        @Override

        public String toString() {

            return fontProperties.toString();

        }


        private static class TableDirectory {

            String name; //table name

            int checkSum; //Check sum

            int offset; //Offset from beginning of file

            int length; //length of the table in bytes

        }


        private static class NameTableHeader {

            int fSelector; //format selector. Always 0

            int nRCount; //Name Records count

            int storageOffset; //Offset for strings storage,

        }


        private static class NameRecord {

            int platformID;

            int encodingID;

            int languageID;

            int nameID;

            int stringLength;

            int stringOffset; //from start of storage area

        }

    }


    public static void main(String[] args) throws Exception {
        TTFParser parser = new TTFParser();
        parser.parse("assets/fonts/katong.ttf");
        System.out.println("font name: " + parser.getFontName());

    }
}