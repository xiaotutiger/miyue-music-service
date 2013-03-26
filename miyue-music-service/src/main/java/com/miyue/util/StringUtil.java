package com.miyue.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtil extends org.apache.commons.lang.StringUtils {

	public static final Log logger = LogFactory.getLog(StringUtil.class);
	
	public static String[] invisibleChars = { new String("" + (char) 0x0A),
			new String("" + (char) 0x0D), new String("" + (char) 0x09) };

	/**
	 * 将整数转化为32进制的字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String int2EyptStr(int num) {
		char[] tmp = new char[32];
		int i = 0;
		if (0 == num) {
			tmp[31] = '0';
			i = 1;
		}
		while (0 != num && i < 32) {
			int v = num % 32;
			if (v < 10)
				tmp[31 - i] = (char) (v + '0');
			else
				tmp[31 - i] = (char) (v - 10 + 'A');
			num = num / 32;
			i++;
		}
		return new String(tmp, 32 - i, i);
	}

	/**
	 * 转义单引号,生成SQL时使用。
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeSingleQuotes(String str) {
		if (str == null) {
			return null;
		} else {
			String string = org.apache.commons.lang.StringUtils.replace(str, "'", "''");
			return "'" + string + "'";
		}
	}

	/**
	 * 过滤字符串中的不可见字符
	 * 
	 * @param str
	 * @return
	 */
	public static String filterInvisibleChars(String str) {
		if (org.apache.commons.lang.StringUtils.isEmpty(str)) {
			return null;
		}
		for (String c : invisibleChars) {
			str = str.replaceAll(c, "");
		}
		return str;
	}

	public static void main(String[] args) {
		
/*		String url="http://sns.51mrp.com:8080/sky-social/social/rest/people/100012";
		//url="http://m.51mrp.com";
		try {
			JSONObject job=getJSONObject(url).getJSONObject("entry");
			System.out.println(job.get("nickname"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public static String getArrayString(String[] values, String splt,
			String prex, String append) {
		StringBuffer s = new StringBuffer();
		String exp = null;
		int size = values.length;
		for (int i = 0; i < size; i++) {
			s.append(prex + values[i] + append);
			if (i < size - 1)
				s.append(splt);
		}
		exp = s.toString();
		return exp;
	}

	/**
	 * 对html标记进行转义，例如'< '和 '>'替换成 '&lt;' 和 '&gt;'
	 * 
	 * @param需要转换的字符串
	 * @return 返回转义过的字符串
	 */
	public static String escapeHTMLTags(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
				.replaceAll("\'", "&#039;").replaceAll("\"", "&quot;")
				.replaceAll("\\$", "\\$\\$").replaceAll("\\{", "『").replaceAll(
						"\\}", "』");
	}

	/**
	 * 对String.Replace(regx,repl)中的repl参数转义
	 */
	public static String escapeRepl(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		return input.replaceAll("\\\\", "\\\\\\\\")
				.replaceAll("\\$", "\\\\\\$");
	}

	/**
	 * 把 url 中要传输的等号 转换成 %3D
	 * 
	 * @param input
	 * @return string
	 */
	public static String escapeUrlTags(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		// Use a StringBuffer in lieu of String concatenation -- it is
		// much more efficient this way.
		StringBuffer buf = new StringBuffer(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '=') {
				buf.append("%3D");
			} else if (ch == '\'') {
				buf.append("&#039;");
			} else if (ch == '"') {
				buf.append("&quot;");
			} else
				buf.append(ch);
		}
		return buf.toString();
	}

	/**
	 * 把',"转义成 \',\"
	 * 
	 * @param input
	 * @return String
	 */
	public static String escapeJScriptTags(String input) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (input == null || input.length() == 0) {
			return input;
		}
		// Use a StringBuffer in lieu of String concatenation -- it is
		// much more efficient this way.
		StringBuffer buf = new StringBuffer(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '\'') {
				buf.append("\\'");
			} else if (ch == '"') {
				buf.append("\\\"");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	/**
	 * 中文字符集转换 iso-8859-1
	 */

	public static String parseChinese(String in) {
		String s = null;
		byte temp[];
		if (in == null) {
			return null;
		}
		try {
			temp = in.getBytes("iso-8859-1");
			s = new String(temp);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		return s;
	}

	/**
	 * 中文字符集转换 GBK
	 */

	public static String getChinese(String in) {
		String s = null;
		byte temp[];
		if (in == null) {
			return null;
		}
		try {
			temp = in.getBytes("GBK");
			s = new String(temp);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.toString());
		}
		return s;
	}

	/**
	 * Oracle查询字符窜转换
	 */
	public static String parseOracleQuery(String in) {
		return in.replace('*', '%');
	}

	/**
	 * Oracle查询字符窜转换
	 */
	public static String escapeSQL(String in) {
		if (in == null)
			return null;
		if (in.indexOf("exec") != -1)
			return null;
		return in.replaceAll("'", "''");
	}

	/**
	 * 按分隔符号读出字符串的内容
	 * 
	 * @param strlist
	 *            含有分隔符号的字符串
	 * @param ken
	 *            分隔符号
	 * @return 列表
	 */
	@SuppressWarnings("unchecked")
	public static Vector parseStringToVector(String strlist, String ken) {
		StringTokenizer st = new StringTokenizer(strlist, ken);

		if (strlist == null || strlist.equals("") || st.countTokens() <= 0) {
			return new Vector();
		}

		int size = st.countTokens();
		Vector strv = new Vector();

		for (int i = 0; i < size; i++) {
			String nextstr = st.nextToken();
			if (!nextstr.equals("")) {
				strv.add(nextstr);
			}
		}
		return strv;
	}

	/**
	 * 按分隔符号读出字符串的内容
	 * 
	 * @param strlist
	 *            含有分隔符号的字符串
	 * @param ken
	 *            分隔符号
	 * @return 列表
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList parseStringToArrayList(String strlist, String ken) {
		StringTokenizer st = new StringTokenizer(strlist, ken);

		if (strlist == null || strlist.equals("") || st.countTokens() <= 0) {
			return new ArrayList();
		}

		int size = st.countTokens();
		ArrayList strv = new ArrayList();

		for (int i = 0; i < size; i++) {
			String nextstr = st.nextToken();
			if (!nextstr.equals("")) {
				strv.add(nextstr);
			}
		}
		return strv;
	}

	public static String convertNewlines(String input) {
		char[] chars = input.toCharArray();
		int cur = 0;
		int len = chars.length;
		StringBuffer buf = new StringBuffer(len);
		// Loop through each character lookin for newlines.
		for (int i = 0; i < len; i++) {
			// If we've found a Unix newline, add BR tag.
			if (chars[i] == '\n') {
				buf.append(chars, cur, i - cur).append(StringUtil.BR_TAG);
				cur = i + 1;
			}
			// If we've found a Windows newline, add BR tag.
			else if (chars[i] == '\r' && i < len - 1 && chars[i + 1] == '\n') {
				buf.append(chars, cur, i - cur).append(StringUtil.BR_TAG);
				i++;
				cur = i + 1;
			}
		}
		// Add whatever chars are left to buffer.
		buf.append(chars, cur, len - cur);
		return buf.toString();
	}

	/**
	 * Initialization lock for the whole class. Init's only happen once per
	 * class load so this shouldn't be a bottleneck.
	 */
	public static Object initLock = new Object();
	public static final char[] BR_TAG = "<br/>".toCharArray();

	/**
	 * Used by the hash method.
	 */
	private static MessageDigest digest = null;

	/**
	 * 计算指定字符串的md5消息摘要
	 * 
	 * @paraminput
	 * @return String
	 */
	public synchronized static String hash(String data) {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				System.err.println("Failed to load the MD5 MessageDigest. "
						+ "Jive will be unable to function normally.");
				nsae.printStackTrace();
			}
		}
		// Now, compute hash.
		digest.update(data.getBytes());
		return toHex(digest.digest());
	}

	/**
	 * Turns an array of bytes into a String representing each byte as an
	 * unsigned hex number. <p/> Method by Santeri Paavolainen, Helsinki Finland
	 * 1996<br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	 * Distributed under LGPL.
	 * 
	 * @param hash
	 *            an rray of bytes to convert to a hex-string
	 * @return generated hex string
	 */
	public static String toHex(byte hash[]) {
		StringBuffer buf = new StringBuffer(hash.length * 2);
		int i;

		for (i = 0; i < hash.length; i++) {
			if ((hash[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(hash[i] & 0xff, 16));
		}
		return buf.toString();
	}

	/**
	 * Converts a line of text into an array of lower case words. Words are
	 * delimited by the following characters: , .\r\n:/\+ <p/> In the future,
	 * this method should be changed to use a BreakIterator.wordInstance(). That
	 * class offers much more fexibility.
	 * 
	 * @param text
	 *            a String of text to convert into an array of words
	 * @return text broken up into an array of words.
	 */
	public static String[] toLowerCaseWordArray(String text) {
		StringTokenizer tokens = new StringTokenizer(text, " ,\r\n.:/\\+");
		String[] words = new String[tokens.countTokens()];
		for (int i = 0; i < words.length; i++) {
			words[i] = tokens.nextToken().toLowerCase();
		}
		return words;
	}

	/**
	 * A list of some of the most common words. For searching and indexing, we
	 * often want to filter out these words since they just confuse searches.
	 * The list was not created scientifically so may be incomplete :)
	 */
	private static final String[] commonWords = new String[] { "a", "and",
			"as", "at", "be", "do", "i", "if", "in", "is", "it", "so", "the",
			"to" };
	@SuppressWarnings("unchecked")
	private static Map commonWordsMap = null;

	/**
	 * Returns a new String array with some of the most common English words
	 * removed. The specific words removed are: a, and, as, at, be, do, i, if,
	 * in, is, it, so, the, to
	 */
	@SuppressWarnings("unchecked")
	public static String[] removeCommonWords(String[] words) {
		// See if common words map has been initialized. We don't statically
		// initialize it to save some memory. Even though this a small savings,
		// it adds up with hundreds of classes being loaded.
		if (commonWordsMap == null) {
			synchronized (StringUtil.initLock) {
				if (commonWordsMap == null) {
					commonWordsMap = new HashMap();
					for (int i = 0; i < commonWords.length; i++) {
						commonWordsMap.put(commonWords[i], commonWords[i]);
					}
				}
			}
		}
		// Now, add all words that aren't in the common map to results
		ArrayList results = new ArrayList(words.length);
		for (int i = 0; i < words.length; i++) {
			if (!commonWordsMap.containsKey(words[i])) {
				results.add(words[i]);
			}
		}
		return (String[]) results.toArray(new String[results.size()]);
	}

	/**
	 * 输出转换
	 */
	public static String parseNormal(String in) {
		String s = "";
		if (in != null)
			s = in;
		return s;
	}

	/**
	 * Date转换成字符串
	 * 
	 * @param in
	 * @return
	 */
	public static String parseNormal(Date in) {
		String s = "";
		if (in != null)
			s = in.toString();
		return s;
	}

	/**
	 * double 转换成字符串
	 * 
	 * @param in
	 * @return
	 */
	@SuppressWarnings({ "unused", "static-access" })
	public static String parseNormal(double in) {
		String s;
		int ilen, dlen, index, len;
		char ch[];
		s = Double.toString(in);
		index = s.indexOf('E');
		if (index >= 0) {
			ilen = Integer.parseInt(s.substring(index + 1));
			dlen = index - ilen - 2;
			ch = s.toCharArray();
			s = s.copyValueOf(ch, 0, 1);
			if (ilen < index - 2) {
				s += s.copyValueOf(ch, 2, ilen);
			} else {
				s += s.copyValueOf(ch, 2, index - 2);
				for (int i = 0; i < ilen - index + 2; i++) {
					s += "0";
				}
			}
			if (dlen > 0) {
				s += ".";
				s += s.copyValueOf(ch, 2 + ilen, dlen);
			}
		}
		return s;
	}

	/**
	 * Int 转换成字符串
	 * 
	 * @param in
	 * @return
	 */
	public static String parseNormal(int in) {
		String s = "";
		s = Integer.toString(in);
		return s;
	}

	/**
	 * long 转换成字符串
	 * 
	 * @param in
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String parseNormal(long in) {
		String s = "";
		int ilen, index;
		char ch[];
		s = Long.toString(in);
		index = s.indexOf('E');
		if (index >= 0) {
			ilen = Integer.parseInt(s.substring(index + 1));
			ch = s.toCharArray();
			s = s.copyValueOf(ch, 0, 1);
			if (ilen < index - 2) {
				s += s.copyValueOf(ch, 2, ilen);
			} else {
				s += s.copyValueOf(ch, 2, index - 2);
				for (int i = 0; i < ilen - index + 2; i++) {
					s += "0";
				}
			}
		}
		return s;
	}

	/**
	 * Breaks up words that are longer than <tt>maxCount</tt> with spaces.
	 * 
	 * @param input
	 *            the String to check for long words in.
	 * @return a new String with words broken apart as necessary.
	 */
	public static String createBreaks(String input) {
		char[] chars = input.toCharArray();
		int len = chars.length;
		StringBuffer buf = new StringBuffer(len);
		int count = 0;
		int cur = 0;
		// Loop through each character, looking for words that are too long.
		for (int i = 0; i < len; i++) {
			// If we've found a Unix newline, add BR tag.
			if (chars[i] == '\n') {
				buf.append(chars, cur, i - cur).append(StringUtil.BR_TAG);
				cur = i + 1;
			}
			// If we've found a Windows newline, add BR tag.
			else if (chars[i] == '\r' && i < len - 1 && chars[i + 1] == '\n') {
				buf.append(chars, cur, i - cur).append(StringUtil.BR_TAG);
				i++;
				cur = i + 1;
			}

			count++;
		}
		// Add whatever chars are left to buffer.
		buf.append(chars, cur, len - cur);
		return buf.toString();
	}

	/**
	 * ISO_8859_1编码转换到GBK
	 * 
	 * @param s
	 * @return String
	 */
	public static String UnicodeToChinese(String s) {
		try {
			if (s == null || s.equals(""))
				return "";
			String newstring = null;
			newstring = new String(s.getBytes("ISO_8859_1"), "GBK");
			return newstring;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}

	/**
	 * ISO_8859_1编码转换到指定编码
	 * 
	 * @param s
	 * @param charset
	 * @return
	 */
	public static String UnicodeToChinese(String s, String charset) {
		try {
			if (s == null || s.equals(""))
				return "";
			String newstring = null;
			newstring = new String(s.getBytes("ISO_8859_1"), charset);
			return newstring;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}

	/**
	 * gb2312编码转换到ISO8859_1
	 * 
	 * @param s
	 * @return String
	 */
	public static String ChineseToUnicode(String s) {
		try {
			if (s == null || s.equals(""))
				return "";
			String newstring = null;
			newstring = new String(s.getBytes("gb2312"), "ISO8859_1");
			return newstring;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}

	/**
	 * gb2312编码转换到UTF8
	 * 
	 * @param s
	 * @return String
	 */
	public static String ChineseToUTF8(String s) {
		try {
			if (s == null || s.equals(""))
				return "";
			String newstring = null;
			newstring = new String(s.getBytes("gb2312"), "UTF-8");
			return newstring;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}

	/**
	 * 转换为URLEncoder(UTF-8编码)
	 * 
	 * @param s
	 * @return String
	 */
	public static String encodeUTF8(String s) {
		try {
			if (s == null || s.equals(""))
				return "";
			s = URLEncoder.encode(s, "UTF-8");
			s = s.replaceAll("\\+", "%20");
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return s;
		}
	}

	/**
	 * Replaces all instances of oldString with newString in line with the added
	 * feature that matches of newString in oldString ignore case. The count
	 * paramater is set to the number of replaces performed.
	 * 
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 * @param count
	 *            a value that will be updated with the number of replaces
	 *            performed.
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static String replaceIgnoreCase(String line, String oldString,
			String newString, int[] count) {
		if (line == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			int counter = 0;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}

	/**
	 * 替换字符串
	 * 
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 * @return a String will all instances of oldString replaced by newString
	 */
	public static String replace(String line, String oldString,
			String newString, int[] count) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			int counter = 0;
			counter++;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}

	public static String getArrayString(String[] values, String splt) {
		StringBuffer s = new StringBuffer();
		String exp = null;
		int size = values.length;
		for (int i = 0; i < size; i++) {
			s.append(values[i]);
			if (i < size - 1)
				s.append(splt);
		}
		exp = s.toString();
		return exp;
	}

	/**
	 * 读取指定url的内容
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String getUrlTxt(String url) throws Exception {
		String sLine = "";
		String sText = "";
		java.io.InputStream l_urlStream;
		java.net.URL l_url = new java.net.URL(url);
		java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url
				.openConnection();
		l_connection.connect();
		l_urlStream = l_connection.getInputStream();
		java.io.BufferedReader l_reader = new java.io.BufferedReader(
				new java.io.InputStreamReader(l_urlStream));
		while ((sLine = l_reader.readLine()) != null) {
			sText += sLine;
		}
		return sText;
	}
/*	*//**
	 * 对url获取的内容，返回一个json对象，url内容必须是json字符串
	 * @param jsonUrl
	 * @return json对象
	 * @throws Exception
	 *//*
	public static JSONObject getJSONObject(String jsonUrl)throws Exception{
		return new JSONObject(getUrlTxt(jsonUrl));
	}*/

	public static boolean isNull(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String parseUri(String uri){
		if(uri.contains("?")){
			return uri.substring(1,uri.indexOf('?'));
		}
		return uri.substring(1);
	}
	
	/**
	 * 对指定字符串进行字符集转换
	 * 
	 * @param s
	 * @param srcCharset
	 * @param destCharset
	 * @return
	 */
	public static String convert(String s, String srcCharset, String destCharset) {
		if (s == null)
			return null;
		/*
		 * String ret = s; try { ret = new String(s.getBytes(srcCharset),
		 * destCharset); } catch (Exception e) { ; } return ret;
		 */
		Charset charset = Charset.forName(srcCharset);
		CharsetDecoder decoder = charset.newDecoder();
		ByteBuffer asciiBytes = ByteBuffer.wrap(s.getBytes());
		CharBuffer chars = null;
		try {
			chars = decoder.decode(asciiBytes);
		} catch (Exception e) {
			System.err.println("Error decoding:" + srcCharset);
			return s;
		}

		charset = Charset.forName(destCharset);
		CharsetEncoder encoder = charset.newEncoder();
		ByteBuffer ret = null;
		try {
			ret = encoder.encode(chars);
		} catch (CharacterCodingException e) {
			System.err.println("Error encoding:" + destCharset);
			return s;
		}
		return new String(ret.array());
	}
}
