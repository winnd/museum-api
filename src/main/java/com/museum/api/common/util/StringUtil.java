package com.museum.api.common.util;



import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字列の操作や各種変換処理などを行うクラスです。
 *
 * @author ttc
 * @version 0.9
 */

public class StringUtil {

	/**
	 * UTF-8も文字コードを示す文字列.
	 */
	public static final String SYSTEM_CHAR_SET = "UTF-8";

	/**
	 * 半角文字のコードの範囲(UTF-8下限)
	 */
	public static final char ALPHABETLO = 0x0020; // space
	/**
	 * 半角文字のコードの範囲(UTF-8上限)
	 */
	public static final char ALPHABETHI = 0x007e; // ~

	/**
	 * 日本語(句点から半濁点まで)(UTF-8下限)
	 */
	public static final char KANALO_UTF_8 = 0xff61; // ｡ 65377(10)
	/**
	 * 日本語(句点から半濁点まで)(UTF-8上限)
	 */
	public static final char KANAHI_UTF_8 = 0xff9f; // ﾟ 65439(10)

	// SHIFT-JIS,MS932の半角文字のコードの範囲
	// 欧米語は同じ

	/**
	 * 日本語(句点から半濁点まで)(Shift_JIS下限)
	 */
	public static final char KANALO_SJIS = 0x00a0; // ｡ 160(10)
	/**
	 * 日本語(句点から半濁点まで)(Shift_JIS上限)
	 */
	public static final char KANAHI_SJIS = 0x00df; // ﾟ 223(10)

	private static final char UTF8NULLCHAR = '\u0000';

	/**
	 * 第一引数に指定されたStringオブジェクトがnullまたは""の場合は第二引数のStringを返します。
	 * 第一引数に指定されたStringオブジェクトがnullでも""でも無い場合は、第一引数の値をそのまま返します。
	 *
	 * @param from
	 *            nullかどうかの検査対象
	 * @param to
	 *            fromがnullの場合に返す値
	 * @return fromがnullの場合はtoを返す。fromがnull以外の場合はfromを返す。
	 */
	public static String nullTo(String from, String to) {
		if (StringUtil.isNull(from)) {
			return to;
		}
		return from;
	}

	/**
	 *
	 * 文字列をMD5形式で暗号化して返す。<BR>
	 * <br>
	 * 対象文字列がnullのとき、または変換できなかった場合はnullを返す。
	 *
	 * @param aString
	 *            対象文字列
	 * @return 暗号化された文字列
	 */

	public static String encrypt(String aString) {
		String retval = null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(aString.getBytes());

			byte[] digest = md.digest();
			StringBuffer s = new StringBuffer();
			for (int i = 0; i < digest.length; i++) {
				s.append(Integer.toString((digest[i] & 0xf0) >> 4, 16));
				s.append(Integer.toString(digest[i] & 0x0f, 16));
			}
			retval = s.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retval;
	}

	/**
	 * 引数に指定された文字列の1文字目を大文字に、2文字目以降を小文字にして返す。<BR>
	 * <br>
	 * 2文字目以降の、<b>大文字の直前の文字が小文字だった場合は大文字に変換しない</b>。<BR>
	 * 変換対象文字列がnullまたは0文字のときは""を返す。<br>
	 * <br>
	 * &nbsp;&nbsp;&nbsp;例1：selectedRow => SelectedRow<BR>
	 * &nbsp;&nbsp;&nbsp;例2：SelectedRow => SelectedRow<BR>
	 * &nbsp;&nbsp;&nbsp;例3：SELECTEdRow => SelectedRow<BR>
	 * &nbsp;&nbsp;&nbsp;例4：SELECTEDRow => Selectedrow
	 *
	 * @param str
	 *            変換対象文字列
	 * @return 変換後の文字列
	 */

	public static String toRegularCase(String str) {
		if (str == null || str.length() < 1) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		buf.append(str.substring(0, 1).toUpperCase());
		boolean alreadyLower = false;
		CharacterIterator ite = new StringCharacterIterator(str.substring(1, str.length()));
		for (char c = ite.first(); c != CharacterIterator.DONE; c = ite.next()) {
			String s1 = String.valueOf(c);
			String s2 = String.valueOf(c).toLowerCase();

			if (alreadyLower) {
				buf.append(s1);
			} else {
				buf.append(s2);
			}

			alreadyLower = s1.equals(s2);
		}
		return buf.toString();
	}

	/**
	 * 文字列の指定された範囲を切り出して返す。<br>
	 * <br>
	 * 全角文字を2、半角文字を1として、指定された範囲を切り出して返す。<br>
	 * 切り出す開始位置から切り出す終了位置-1までの文字列が、切り出す対象範囲となる。<br>
	 * <br>
	 * 切り出す対象範囲の開始位置または終了位置が全角文字を分割するときは下記の通り補正される。<br>
	 * <UL>
	 * <LI>切り出す対象範囲の開始位置が全角文字を分割する位置になるとき、切り出す対象範囲の開始位置は該当する文字の<b>次の文字に補正</b>
	 * される。
	 * <LI>切り出す対象範囲の終了位置が全角文字を分割する位置になるとき、切り出す対象範囲の終了位置は該当する文字の<b>前の文字に補正</b>
	 * される。
	 * </UL>
	 * <br>
	 * 例： &nbsp;&nbsp;&nbsp;substrigByByte("あいうえお" ,1 ,9)のとき<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;開始位置が"あ"を分割する位置のため、直後の"い"に補正される。<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;終了位置が"お"を分割する位置のため、直前の"え"に補正される。<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;結果、"いうえ"が返される。 <br>
	 * <br>
	 * 対象文字列がnullのときはnullを返す。
	 *
	 * @param inStr
	 *            対象文字列
	 * @param startPos
	 *            byte単位の切り出す開始位置 (この値を含む)
	 * @param endPos
	 *            byte単位の切り出す終了位置 (この値を<b>含まない</b>)
	 * @return 切り取った結果の文字列
	 * @see java.lang.String
	 */

	public static String substringByByte(String inStr, int startPos, int endPos) {
		if (inStr == null) {
			return null;
		}

		StringBuffer buf = new StringBuffer();

		int intCharLen;
		int bytePos = 0;
		char c = 0;

		// startPosまで読み飛ばし。
		// startPosが全角文字を分割する場合、分割される全角文字を超えた位置まで読み飛ばす
		StringCharacterIterator sci = new StringCharacterIterator(inStr);
		c = sci.first();
		while (c != CharacterIterator.DONE && startPos > bytePos) {
			intCharLen = StringUtil.countLengthByByte(String.valueOf(c));
			bytePos += intCharLen;
			c = sci.next();
		}

		// 読み飛ばされた位置からendPosまで切り取る
		// endPos-1が全角文字を分割する場合、分割される全角文字を含まないようにする
		while (c != CharacterIterator.DONE) {
			intCharLen = StringUtil.countLengthByByte(String.valueOf(c));
			bytePos += intCharLen;
			if (bytePos <= endPos) {
				buf.append(c);
			}

			c = sci.next();
			if (bytePos >= endPos) {
				break;
			}
		}
		return buf.toString();
	}

	/**
	 * 文字列の左側に文字列を連結する。<br>
	 * <br>
	 * 下記の手順で文字列を連結する。<br>
	 * <OL>
	 * <LI>追加する文字列を長さの上限以上になるまでこれを繰り返す。<br>
	 * <LI>元の文字列の先頭に追加文字列で指定された文字列を追加し、長さの上限以上になるまでこれを繰り返す。
	 * <LI>追加後の文字列の長さが長さの上限を超えているときは下記の操作を行う。
	 * <OL type="i">
	 * <LI>追加した文字列の末尾から、(長さの上限-元の文字列のbyte数)分の文字列を追加する文字列にする。
	 * <LI>追加する文字列の末尾が全角文字を分割する位置になるときは、その文字の前までが切り出される。
	 * <LI>追加する文字列に元の文字列を連結する。
	 * </OL>
	 * </OL>
	 * <br>
	 * 例：
	 * <table>
	 * <tr>
	 * <td>元の文字列：</td>
	 * <td>"１２３４５"</td>
	 * </tr>
	 * <tr>
	 * <td>長さの上限：</td>
	 * <td>17</td>
	 * </tr>
	 * <tr>
	 * <td>追加文字列：</td>
	 * <td>"ＡＢ"</td>
	 * </tr>
	 * </table>
	 * のとき、"ＡＢＡ１２３４５"が返される。
	 *
	 * @param inStr
	 *            元の文字列
	 * @param byteLen
	 *            長さの上限(全角文字を2、半角文字を1とした長さ)
	 * @param strPad
	 *            追加文字列
	 * @return 加工後の文字列
	 * @exception 連結する文字列が長さ0またはnullのとき
	 */

	public static String leftPad(String inStr, int byteLen, String strPad) {
		if (inStr == null) {
			return null;
		}
		return pad(inStr, byteLen, strPad, false);
	}

	/**
	 * 文字列の右側に文字列を連結する。<br>
	 * <br>
	 * 下記の手順で文字列を連結する。<br>
	 * <OL>
	 * <LI>元の文字列に追加文字列で指定された文字列を追加し、長さの上限以上になるまでこれを繰り返す。
	 * <LI>追加後の文字列の先頭から、長さの上限分の文字列を切り出す。
	 * </OL>
	 * <br>
	 * 元の文字列が長さの上限を超えているときは文字列の連結は行わず、元の文字列の先頭から長さの上限分の文字列を切り出す。<br>
	 * <br>
	 * 切り出した右端が全角文字を分割する位置になるときは、その文字の前までが切り出される。<br>
	 * <br>
	 * 例：
	 * <table>
	 * <tr>
	 * <td>元の文字列：</td>
	 * <td>"１２３４５"</td>
	 * </tr>
	 * <tr>
	 * <td>長さの上限：</td>
	 * <td>17</td>
	 * </tr>
	 * <tr>
	 * <td>追加文字列：</td>
	 * <td>"ＡＢ"</td>
	 * </tr>
	 * </table>
	 * のとき、"１２３４５ＡＢＡ"が返される。
	 *
	 * @param inStr
	 *            元の文字列
	 * @param byteLen
	 *            長さの上限(全角文字を2、半角文字を1とした長さ)
	 * @param strPad
	 *            追加文字列
	 * @return 加工後の文字列
	 * @exception 連結する文字列が長さ0またはnullのとき
	 */

	public static String rightPad(String inStr, int byteLen, String strPad) {
		if (inStr == null) {
			return null;
		}
		return pad(inStr, byteLen, strPad, true);
	}

	// ///////////////////////////////
	// 文字列の各種カウンタ
	// ///////////////////////////////
	/**
	 * 文字列の長さを求める。<br>
	 * <br>
	 * デフォルトの文字セットでの文字列の長さを求める。<br>
	 * 全角文字を2、半角文字を1として計算する。<br>
	 * <br>
	 * 対象文字列がnullのときは0を返す。
	 *
	 * @param str
	 *            対象文字列
	 * @return 長さ
	 */

	public static int countLengthByByte(String str) {
		if (str == null) {
			return 0;
		}

		try {
			return StringUtil.countLengthByByte(str, null);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * 文字セットを指定して文字列の長さを求める。<br>
	 * <br>
	 * 指定された文字セットを使用して文字列の長さを求める。<br>
	 * 全角文字を2、半角文字を1として計算する。<br>
	 * <br>
	 * 対象文字列がnullのときは0を返す。
	 *
	 * @param str
	 *            対象文字列
	 * @param pstrEncode
	 *            文字セット
	 * @return 長さ
	 */

	public static int countLengthByByte(String str, String pstrEncode) {
		if (str == null) {
			return 0;
		}

		// 半角カナの範囲の特定
		char chrLow = StringUtil.KANALO_UTF_8;
		char chrHi = StringUtil.KANAHI_UTF_8;

		int intByteLen = 0; // byte数
		CharacterIterator ite = new StringCharacterIterator(str);
		for (char c = ite.first(); c != CharacterIterator.DONE; c = ite.next()) {
			if ((c >= StringUtil.ALPHABETLO && c <= StringUtil.ALPHABETHI) // 欧米語の範囲内
					|| (c >= chrLow && c <= chrHi)) { // 日本語の範囲内
				intByteLen++; // 半角文字の範囲にあったとき
			} else {
				intByteLen += 2; // 半角文字の範囲にないとき
			}
		}
		return intByteLen;
	}

	/**
	 * 文字列を構築するバイト数を求める。<br>
	 * <br>
	 * デフォルトの文字コードでの、対象文字列の構築するバイト数を求める。<br>
	 * <br>
	 * 検査対象の文字列がnullのときは0を返す。
	 *
	 * @param aTarget
	 *            検査対象の文字列
	 * @return バイト数
	 */

	public static int countLengthByBuildByte(String aTarget) {
		return StringUtil.countLengthByBuildByte(aTarget, CommonProperty.getEncode());
	}

	/**
	 * 文字列を構築するバイト数を求める。<br>
	 * <br>
	 * 指定された文字コードでの、対象文字列の構築するバイト数を求める。<br>
	 * 文字コードがUTF-8のとき<b>1～3バイトで1文字を構成する</b>。また、半角カナは3バイトで1文字を構成する。<br>
	 * そのため、全角文字を2バイト、半角文字を1バイトとして算出されるバイト数より大きくなる。<br>
	 * <br>
	 * 検査対象の文字列がnullのときは0を返す。
	 *
	 * @param aTarget
	 *            検査対象の文字列
	 * @param pstrEncode
	 *            文字セット
	 * @return バイト数
	 */

	public static int countLengthByBuildByte(String aTarget, String pstrEncode) {
		if (aTarget == null) {
			return 0;
		}

		try {
			int intRet = 0;
			byte[] ba = aTarget.getBytes(pstrEncode);
			intRet = ba.length;
			return intRet;
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	/**
	 * 文字列の文字数をカウントする。<br>
	 * <br>
	 * 全角文字、半角文字にかかわらず<b>1文字を1</b>としてカウントする。<br>
	 * 検査対象の文字列がnullのときは0を返す。
	 *
	 * @param aTarget
	 *            検査対象の文字列
	 * @return 文字数
	 */

	public static int countLengthByChar(String aTarget) {
		int intRet = 0;

		if (!ValidateUtil.isNull(aTarget)) {
			intRet = aTarget.length();
		}
		return intRet;
	}

	/**
	 * 文字列内の指定された文字を削除する。<br>
	 * <br>
	 * 例：
	 * <table>
	 * <tr>
	 * <td>元の文字列：</td>
	 * <td>"ABCDEABCDE"</td>
	 * </tr>
	 * <tr>
	 * <td>削除する文字：</td>
	 * <td>'C'</td>
	 * </tr>
	 * </table>
	 * と指定された場合、"ABDEABDE"が返される。(Cが削除された状態になる。)<br>
	 * <br>
	 * 元の文字列がnullのときはnullを返す。
	 *
	 * @param data
	 *            元の文字列
	 * @param delChar
	 *            削除する文字
	 * @return 削除後の数字列
	 * @deprecated {@link String#replaceAll(String, String)}
	 *             をreplacementを""にして使用する
	 */

	public static String deleteCharFromString(String data, char delChar) {
		if (data == null) {
			return null;
		}
		return data.replaceAll(new String(new char[] { delChar }), "");
	}

	/**
	 * 文字列内の指定された文字群を削除する。<br>
	 * <br>
	 * 例：
	 * <table>
	 * <tr>
	 * <td>元の文字列：</td>
	 * <td>"ABCDEABCDE"</td>
	 * </tr>
	 * <tr>
	 * <td>削除する文字群：</td>
	 * <td>'C','E'</td>
	 * </tr>
	 * </table>
	 * と指定された場合、"ABDABD"が返される。(CとEが削除された状態になる。)<br>
	 * <br>
	 * 元の文字列がnullのときはnullを返す。
	 *
	 * @param str
	 *            元の文字列
	 * @param delChar
	 *            削除する文字群
	 * @return 削除後の数字列
	 */

	public static String deleteCharFromString(String str, char[] delChar) {
		if (str == null) {
			return null;
		}

		StringBuffer buf = new StringBuffer("");
		CharacterIterator ite = new StringCharacterIterator(str);
		for (char c = ite.first(); c != CharacterIterator.DONE; c = ite.next()) {
			boolean notFound = true;
			// 注目している文字が削除する文字群に一致するかどうかを調べる。
			for (int i = 0; i < delChar.length; i++) {
				if (c == delChar[i]) {
					notFound = false;
					break;
				}
			}
			if (notFound) { // 削除する文字群に一致しなければ追加する。
				buf.append(c);
			}
		}
		return buf.toString();
	}

	// ///////////////////////////////
	// 日付フォーマット変換(各型からDate型へ)
	// ///////////////////////////////
	/**
	 * 指定された数値の年、月、日をDateデータへ変換する。<br>
	 * <br>
	 * 例：
	 * <table>
	 * <tr>
	 * <td>年：</td>
	 * <td>2008</td>
	 * </tr>
	 * <tr>
	 * <td>月：</td>
	 * <td align="right">10</td>
	 * </tr>
	 * <tr>
	 * <td>日：</td>
	 * <td align="right">21</td>
	 * </tr>
	 * </table>
	 * と指定された場合、"20081021"の日付データが返される。<br>
	 * <br>
	 * 年、月、日の何れかに1以下の値がある場合、nullを返す。
	 *
	 * @param y
	 *            年
	 * @param m
	 *            月
	 * @param d
	 *            日
	 * @return 日付データ
	 * @deprecated {@link DateUtil#getDate(int, int, int)}
	 */

	public static Date toDate(int y, int m, int d) {
		return DateUtil.getDate(y, m, d);
	}

	/**
	 * 指定された文字列の年、月、日をDateデータへ変換する。<br>
	 * <br>
	 * 例：
	 * <table>
	 * <tr>
	 * <td>年：</td>
	 * <td>"2008"</td>
	 * </tr>
	 * <tr>
	 * <td>月：</td>
	 * <td>"10"</td>
	 * </tr>
	 * <tr>
	 * <td>日：</td>
	 * <td>"21"</td>
	 * </tr>
	 * </table>
	 * と指定された場合、"20081021"の日付データが返される。<br>
	 * <br>
	 * 年、月、日の何れかに0以下の値、数字以外の文字またはnullのときnullを返す。
	 *
	 * @param aYear
	 *            年
	 * @param aMonth
	 *            月
	 * @param aDay
	 *            日
	 * @return 日付データ
	 * @deprecated {@link DateUtil#getDate(String, String, String)}
	 */

	public static Date toDate(String aYear, String aMonth, String aDay) {
		return DateUtil.getDate(aYear, aMonth, aDay);
	}

	/**
	 * 指定された文字列の年月日をDateデータへ変換する。<br>
	 * <br>
	 * 例：<br>
	 * "20081021"が指定された場合、"20081021"の日付データが返される。<br>
	 * <br>
	 * 下記のいずれかのときはnullを返す。
	 * <OL>
	 * <LI>年月日が8桁でないとき。
	 * <LI>年、月、日のいずれかが数字以外のとき。
	 * <LI>年、月、日のいずれかが1以下の値のとき。
	 * </OL>
	 *
	 * @param aYMD
	 *            年月日
	 * @return 日付データ
	 * @deprecated {@link DateUtil#parse(String, String)}
	 *             {@link DateUtil#getDate(String)}を
	 *             使用するとスラッシュ付きも変換してしまうため、注意する。
	 */

	public static Date toDate(String aYMD) {
		return DateUtil.parse(aYMD, null);
	}

	// ///////////////////////////////
	// 日付フォーマット変換(Date型から各型へ)
	// ///////////////////////////////
	/**
	 * 指定されたDateオブジェクトをyyyyMMddフォーマットの数値に変換する。<br>
	 * <br>
	 * 例：<br>
	 * 2008年10月21日が指定された場合、20081021(8桁の整数)が返される。<br>
	 * <br>
	 * Dateオブジェクトがnullの場合、0を返す。
	 *
	 * @param aDate
	 *            日付
	 * @return yyyyMMddフォーマットの整数
	 */

	public static int intValueFromDate(java.util.Date aDate) {
		String val = DateUtil.formatDate(aDate);
		if (!ValidateUtil.isNull(val)) {
			return Integer.parseInt(val);
		}
		return 0;
	}

	/**
	 * 指定されたDateオブジェクトをyyyyMMddフォーマットの文字列に変換する。<br>
	 * <br>
	 * 例：<br>
	 * 2008年10月21日が指定された場合、"20081021"が返される。<br>
	 * <br>
	 * Dateオブジェクトがnullの場合、nullを返す。
	 *
	 * @param aDate
	 *            日付
	 * @return "yyyyMMdd"フォーマットの文字列
	 * @deprecated {@link DateUtil#formatDate(Date)}
	 */

	public static String strValueFromDate(java.util.Date aDate) {
		return DateUtil.formatDate(aDate);
	}

	/**
	 * 指定されたDateオブジェクトを年、月、日の文字列に分割する。<br>
	 * <br>
	 * 例：<br>
	 * 2008年10月21日が指定された場合、配列の各要素には下記のように入る。
	 * <table>
	 * <tr>
	 * <td>0</td>
	 * <td>"2008"</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>"10"</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>"21"</td>
	 * </tr>
	 * </table>
	 * Dateオブジェクトがnullの場合、配列のすべての要素にnullを返す。
	 *
	 * @param aDate
	 *            日付
	 * @return 年、月、日が入った文字列型の配列
	 */

	public static String[] strValueArrayFromDate(java.util.Date aDate) {
		String[] ret = DateUtil.divideDate(DateUtil.formatDate(aDate, null));
		if (ret == null) {
			return new String[] { null, null, null };
		} else {
			return ret;
		}
	}

	/**
	 * 指定されたDateオブジェクトを年、月、日の数値に分割する。<br>
	 * <br>
	 * 例：<br>
	 * 2008年10月21日が指定された場合、配列の各要素には下記のように入る。
	 * <table>
	 * <tr>
	 * <td>0</td>
	 * <td align="right">2008</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td align="right">10</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td align="right">21</td>
	 * </tr>
	 * </table>
	 * Dateオブジェクトがnullの場合、配列のすべての要素に0を返す。
	 *
	 * @param aDate
	 *            日付
	 * @return 年、月、日が入った数値型の配列
	 */

	public static int[] intValueArrayFromDate(java.util.Date aDate) {
		String[] straYMD = new String[3];
		int[] intaYMD = new int[3];
		straYMD = StringUtil.strValueArrayFromDate(aDate);

		if (straYMD[0] == null) {
			intaYMD[0] = 0;
			intaYMD[1] = 0;
			intaYMD[2] = 0;
		} else {
			intaYMD[0] = Integer.parseInt(straYMD[0]);
			intaYMD[1] = Integer.parseInt(straYMD[1]);
			intaYMD[2] = Integer.parseInt(straYMD[2]);
		}
		return intaYMD;
	}

	// ///////////////////////////////
	// 配列、リスト→文字列変換
	// ///////////////////////////////
	/**
	 * 文字列配列のすべての要素を繋げて文字列にする。<br>
	 * <br>
	 * 文字列配列のすべての内容を","で繋げて1つの文字列にする。<br>
	 * <br>
	 * 文字列配列の要素がnullのときはその箇所には何も出力されない。また、要素数が0のときは""を出力する。
	 *
	 * @param pstrStrings
	 *            連結する文字列の配列
	 * @return 連結した文字列
	 */

	public static String concatString(String[] pstrStrings) {
		return StringUtil.concatString(pstrStrings, ",");
	}

	/**
	 * 項目間の区切り文字を指定して、文字列配列の要素を繋げて文字列にする。<br>
	 * <br>
	 * 文字列配列のすべての内容を指定した文字で繋げて1つの文字列にする。<br>
	 * <br>
	 * 文字列配列の要素がnullのときはその箇所には何も出力されない。また、要素数が0のときは""を出力する。
	 *
	 * @param pstrStrings
	 *            連結する文字列の配列
	 * @param pstrSep
	 *            区切り文字
	 * @return 連結した文字列
	 */

	public static String concatString(String[] pstrStrings, String pstrSep) {
		int intMax = pstrStrings.length; // 要素数

		if (intMax <= 0) { // 要素数が0のとき
			return "";
		}

		StringBuffer buf = new StringBuffer("");
		if (pstrStrings[0] != null) {
			buf.append(pstrStrings[0]);
		}

		for (int i = 1; i < intMax; i++) {
			buf.append(pstrSep);
			if (pstrStrings[i] != null) {
				buf.append(pstrStrings[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * リストのすべての要素を繋げて文字列にする。<br>
	 * <br>
	 * リストのすべての内容を","で繋げて1つの文字列にする。<br>
	 * <br>
	 * リストの要素がnullのときはその箇所には何も出力されない。また、要素数が0のときは""を出力する。
	 *
	 * @param plstList
	 *            連結するリスト
	 * @return 連結した文字列
	 */

	public static String concatList(List<?> plstList) {
		return StringUtil.concatList(plstList, ",");
	}

	/**
	 * 項目間の区切り文字を指定して、リストの要素を繋げて文字列にする。<br>
	 * <br>
	 * リストのすべての内容を指定した文字で繋げて1つの文字列にする。<br>
	 * <br>
	 * リストの要素がnullのときはその箇所には何も出力されない。また、要素数が0のときは""を出力する。
	 *
	 * @param plstList
	 *            連結するリスト
	 * @param pstrSep
	 *            区切り文字
	 * @return 連結した文字列
	 */

	public static String concatList(List<?> plstList, String pstrSep) {
		int intMax = plstList.size(); // 要素数

		if (intMax <= 0) { // 要素数が0のとき
			return "";
		}

		StringBuffer buf = new StringBuffer("");
		String str1Data;

		// 最初の要素の取り出し
		str1Data = (String) getListElement(plstList, 0);
		if (str1Data != null) {
			buf.append(str1Data);
		}

		// 2つめ以降の要素の取り出し
		// 構築した文字列にセパレータを付けてから要素を追加する。
		for (int i = 1; i < intMax; i++) {
			buf.append(pstrSep);
			str1Data = (String) getListElement(plstList, i);
			if (str1Data != null) {
				buf.append(str1Data);
			}
		}
		return buf.toString();
	}

	/**
	 * 文字列を指定された文字で区切り、各要素に分解する。<br>
	 * <br>
	 * 元になる文字列または区切り文字ががnullのときは<b>要素を1つとし、内容はnull</b>にする。
	 *
	 * @param pstrString
	 *            元になる文字列
	 * @param pstrSep
	 *            区切り文字
	 * @return 連結した文字列
	 * @deprecated {@link String#split(String)}をそのまま使う。
	 */

	public static String[] splitStringToArray(String pstrString, String pstrSep) {
		if (pstrString == null || pstrSep == null) {
			return new String[0];
		}
		return pstrString.split("\\Q" + pstrSep + "\\E");
	}

	/**
	 * 文字列を指定された文字で区切り、リストの各要素に格納する。<br>
	 * <br>
	 * 元になる文字列または区切り文字ががnullのときは<b>要素を1つとし、内容はnull</b>にする。
	 *
	 * @param pstrString
	 *            元になる文字列
	 * @param pstrSep
	 *            区切り文字
	 * @return 連結した文字列
	 */

	public static List<String> splitStringToList(String pstrString, String pstrSep) {
		if (pstrString == null || pstrSep == null) {
			return new ArrayList<String>();
		}
		// ArrayListでさらにnewをしないと返したListにaddできないが、
		// そこまでの機能が必要かどうかは・・・
		return new ArrayList<String>(Arrays.asList(pstrString.split(pstrSep)));
	}

	// ///////////////////////////////
	// 文字列の読み取り
	// ///////////////////////////////
	/**
	 * 文字列を読み取る。<br>
	 * <br>
	 * 文字列がnullかどうかを検査して、nullのときは「null時に返す文字列」を返す。<br>
	 * 「null時に返す文字」自体がnullのときは""を返す。<br>
	 * 読み取る文字列がnull以外のときは読み取る文字列をそのまま返す。<br>
	 *
	 * @param pstrString
	 *            読み取る文字列
	 * @param pstrForNull
	 *            null時に返す文字列
	 * @return 返す文字列
	 */

	public static String readString(String pstrString, String pstrForNull) {
		String strReturn; // 返す文字列

		if (pstrString == null) {
			if (pstrForNull == null) { // null時に返す文字列自体がnullのときは""を返す。
				strReturn = "";
			} else { // null以外のときは文字列をそのまま返す。
				strReturn = pstrForNull;
			}
		} else {
			strReturn = pstrString; // 対象文字列がnull以外のとき、対象文字列をそのまま返す。
		}
		return strReturn;
	}

	// ///////////////////////////////
	// 文字列の置換
	// ///////////////////////////////
	/**
	 * 文字列内の任意の文字列を他の文字列に置換する。<br>
	 * <br>
	 * 元の文字列、置換対象の文字列、置換後の文字列のいずれかがnullのときのときはnullを返す。<br>
	 * 元の文字列、置換対象の文字列のいずれかの長さが0のときは元の文字列をそのまま返す。<br>
	 * <br>
	 *
	 * @param pstrString
	 *            元の文字列
	 * @param pstrOldStr
	 *            置換対象の文字列
	 * @param pstrNewStr
	 *            置換後の文字列
	 * @return 置換をかけた数字列
	 * @deprecated {@link String#replaceAll(String, String)}
	 */

	public static String replaceAllString(String pstrString, String pstrOldStr, String pstrNewStr) {
		if (pstrString == null || pstrOldStr == null || pstrNewStr == null) {
			return null;
		}

		if ("".equals(pstrOldStr)) {
			return pstrString;
		}
		return pstrString.replaceAll(pstrOldStr, pstrNewStr);
	}

	/**
	 * 文字列内の指定された文字列を削除する。<br>
	 * <br>
	 * 元の文字列、削除する文字列のいずれかがnullのときのときはnullを返す。<br>
	 * 元の文字列、削除する文字列のいずれかの長さが0のときは元の文字列をそのまま返す。<br>
	 * <br>
	 *
	 * @param pstrString
	 *            元の文字列
	 * @param pstrDelStr
	 *            削除する文字列
	 * @return 削除後の数字列
	 * @deprecated {@link String#replaceAll(String, String)}
	 */

	public static String deleteStringFromString(String pstrString, String pstrDelStr) {
		return replaceAllString(pstrString, pstrDelStr, "");
	}

	/**
	 * オブジェクトがnullかどうかを検査する。<br>
	 * <br>
	 * 検査する変数がnullのとき、trueを返す。<br>
	 * 検査する変数が下記のいずれかのときもnullとみなす。
	 * <UL>
	 * <LI>文字型で長さが0のとき。
	 * <LI>文字型で半角、全角に関わらずブランク文字だけのとき。
	 * </UL>
	 *
	 * @param pobjObject
	 *            検査する変数
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>true</td>
	 *         <td>nullである。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>false</td>
	 *         <td>nullでない。</td>
	 *         </tr>
	 *         </table>
	 */

	public static boolean isNull(Object pobjObject) {
		return ValidateUtil.isNull(pobjObject);
	}

	/**
	 * オブジェクトがnullでないかどうかを検査する。<br>
	 * <br>
	 * 検査する変数がnullでないとき、trueを返す。<br>
	 * 検査する変数が下記のいずれかのときもnullとみなす。
	 * <UL>
	 * <LI>文字型で長さが0のとき。
	 * <LI>文字型で半角、全角に関わらずブランク文字だけのとき。
	 * </UL>
	 *
	 * @param pobjObject
	 *            検査する変数
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>true</td>
	 *         <td>nullである。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>false</td>
	 *         <td>nullでない。</td>
	 *         </tr>
	 *         </table>
	 */

	public static boolean isNotNull(Object pobjObject) {
		return !ValidateUtil.isNull(pobjObject);
	}

	/**
	 *
	 * トリム処理。<br>
	 * 前後の空白文字(\x0000 - \x0020 String#trimに準拠)と全角空白を除去します。<br>
	 *
	 * @param value
	 *            トリム対象の文字列
	 * @return トリム後の文字列
	 */
	public static String trim(String value) {
		if (value == null) {
			return null;
		}
		return value.replaceAll("\\A[\\u0000-\\u0020　]+", "").replaceAll("[\\u0000-\\u0020　]+\\z", "");
	}

	/**
	 *
	 * 左トリム処理。<br>
	 * 対象文字列左側（先頭）の空白文字(\x0000 - \x0020 String#trimに準拠)と全角空白を除去します。<br>
	 *
	 * @param value
	 *            トリム対象の文字列
	 * @return トリム後の文字列
	 */
	public static String ltrim(String value) {
		if (value == null) {
			return null;
		}
		return value.replaceAll("\\A[\\u0000-\\u0020　]+", "");
	}

	/**
	 *
	 * 右トリム処理。<br>
	 * 対象文字列の右側（語尾）の空白文字(\x0000 - \x0020 String#trimに準拠)と全角空白を除去します。<br>
	 *
	 * @param value
	 *            トリム対象の文字列
	 * @return トリム後の文字列
	 */
	public static String rtrim(String value) {
		if (value == null) {
			return null;
		}
		return value.replaceAll("[\\u0000-\\u0020　]+\\z", "");
	}

	/**
	 *
	 * トリム処理(マルチライン対応)。<br>
	 * 前後の空白文字(\x0000 - \x0020 String#trimに準拠)と全角空白を除去します。<br>
	 * ※マルチライン対応で行単位で前後の空白を除去します。 <br>
	 * 文前後にある空行→除去、文中の空行→残す場合
	 * StringUtil.trim(StringUtil.trimMulti(value,false))と使用する。<br>
	 * 文前後にある空行→除去、文中の空行→除去する場合 StringUtil.trimMulti(value,true)と使用する。<br>
	 * 文前後にある空行→残す、文中の空行→残す場合 StringUtil.trimMulti(value,false)と使用する。<br>
	 *
	 * @param value
	 *            トリム対象の文字列
	 * @param removeBlankLine
	 *            trueなら文中の空行を削除する。falseなら文中の空行は削除しない。
	 * @return トリム後の文字列
	 */
	public static String trimMulti(String value, boolean removeBlankLine) {
		if (value == null) {
			return null;
		}
		String excludeLineFeed = "&&[^\\n\\r]";
		if (removeBlankLine) {
			excludeLineFeed = "";
		}
		return value.replaceAll("(?m)^[\\u0000-\\u0020　" + excludeLineFeed + "]+", "")
				.replaceAll("(?m)[\\u0000-\\u0020　" + excludeLineFeed + "]+$", "");
	}

	/**
	 * 00フォーマットの文字列にして返す。<BR>
	 * <BR>
	 * int値を日付のMMフォーマットの月部分用の値にする場合などに使用する。<BR>
	 * <BR>
	 *
	 * @param i
	 *            変換する値
	 * @return 変換後の文字列
	 */

	public static String format00(int i) {
		return new DecimalFormat("00").format(i);
	}

	/**
	 * 第一引数に指定された文字列中に第二引数に指定された文字列に合致する最初の文字を返す。<br>
	 * <br>
	 * 引数のどちらかにnullが指定された場合、0を返す。
	 *
	 * @param pstrTarget
	 *            検査する文字列
	 * @param pstrMatching
	 *            対象文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>\u0000</td>
	 *         <td>対象文字列のいずれにも一致しなかった。検査する文字列または対象文字列がnullだった。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>上記以外</td>
	 *         <td>最初に一致した文字</td>
	 *         </tr>
	 *         </table>
	 */
	public static char findChar(String pstrTarget, String pstrMatching) {
		if (pstrTarget == null || pstrMatching == null) { // パラメータがnullのとき
			return UTF8NULLCHAR;
		}

		String chars = pstrMatching;
		char chrRet = UTF8NULLCHAR;

		CharacterIterator iter = new StringCharacterIterator(pstrTarget);
		for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
			int idx = chars.indexOf(c);
			if (idx >= 0) {
				chrRet = chars.charAt(idx);
				break;
			}
		}
		return chrRet;
	}

	/**
	 * 数値変換処理。<br>
	 * DecimalFormatでの変換を行います。<br>
	 * 小数の丸め方法は {@link java.math.RoundingMode#HALF_EVEN} に従います。
	 * 
	 * @param val
	 *            変換する値
	 * @param srcFormat
	 *            変換元フォーマット。省略(nullに)した場合は"#,##0.###"が適用されます。
	 * @param outFormat
	 *            変換後のフォーマット。省略(nullに)した場合は変換前のフォーマットが適用されます。
	 * @return 変換後の値
	 */
	public static String convertDecimal(String val, String srcFormat, String outFormat) {
		return convertDecimalRnd(val, srcFormat, outFormat, null);
	}

	/**
	 * 数値変換処理。<br>
	 * 丸め方法を指定できる。<br/>
	 *
	 * @param val
	 *            変換対象となる数値
	 * @param srcFormat
	 *            変換元フォーマット。略(nullに)した場合は"#,##0.###"が適用されます。
	 * @param outFormat
	 *            変換後のフォーマット。省略(nullに)した場合は変換前のフォーマットが適用されます。
	 * @param roundingMode
	 *            丸め方法。省略(nullに)した場合は、 {@link RoundingMode#HALF_EVEN} に従います。
	 *            {@link RoundingMode}
	 * @return 変換後の値
	 */
	public static String convertDecimalRnd(String val, String srcFormat, String outFormat, RoundingMode roundingMode) {
		if (val == null) {
			return val;
		}

		Pattern pattern = Pattern.compile("^([+]?)([^+-]*)([+-]?)$");
		Matcher matcher = pattern.matcher(val);
		boolean existTokushuKigo = matcher.matches();

		DecimalFormat decFormat = new DecimalFormat();
		if (srcFormat != null) {
			if (existTokushuKigo) {
				Matcher matcherSrc = pattern.matcher(srcFormat);
				boolean matchSrc = matcherSrc.matches();
				StringBuilder sb = new StringBuilder();
				if (matchSrc && !matcher.group(1).equals(matcherSrc.group(1))) {
					sb.append(matcher.group(1));
				}
				sb.append(srcFormat);
				if (matchSrc && !matcher.group(3).equals(matcherSrc.group(3))) {
					sb.append(matcher.group(3));
				}
				decFormat.applyPattern(sb.toString());
			} else {
				decFormat.applyPattern(srcFormat);
			}
		} else if (existTokushuKigo) {
			decFormat.applyPattern(matcher.group(1) + decFormat.toPattern() + matcher.group(3));
		}
		decFormat.setParseBigDecimal(true);
		ParsePosition pos = new ParsePosition(0);
		Number num = decFormat.parse(val, pos);
		if (num == null || pos.getIndex() != val.length()) {
			return val;
		}
		if (num instanceof BigDecimal && roundingMode != null) {
			int figure = 3;
			if (outFormat != null) {
				int idx = outFormat.indexOf(".");
				if (idx != -1) {
					figure = outFormat.length() - idx - 1;
				} else {
					figure = 0;
				}
			} else if (srcFormat != null) {
				int idx = srcFormat.indexOf(".");
				if (idx != -1) {
					figure = srcFormat.length() - idx;
				} else {
					figure = 0;
				}
			}
			int setPrecision;
			int idx = val.indexOf(".");
			if (idx == -1) {
				setPrecision = val.length() + figure;
			} else {
				setPrecision = idx + figure;
			}
			MathContext mc = null;
			if (roundingMode != null) {
				mc = new MathContext(setPrecision, roundingMode);
			}
			num = ((BigDecimal) num).round(mc);
		}
		if (outFormat != null) {
			if (existTokushuKigo) {
				Matcher matcherOut = pattern.matcher(outFormat);
				boolean matchOut = matcherOut.matches();
				StringBuilder sb = new StringBuilder();
				if (matchOut && !matcher.group(1).equals(matcherOut.group(1))) {
					sb.append(matcher.group(1));
				}
				sb.append(outFormat);
				if (matchOut && !matcher.group(3).equals(matcherOut.group(3))) {
					sb.append(matcher.group(3));
				}
				decFormat.applyPattern(sb.toString());
			} else {
				decFormat.applyPattern(outFormat);
			}
		}
		return decFormat.format(num);
	}

	/**
	 * MS ShiftJISの変換テーブルにあわせる処理.
	 * 
	 * @param str
	 *            IBM Shift-JISを含む文字列
	 * @return MS ShiftJISの変換テーブルに一致する文字列
	 */
	public static String convertShiftJISI2M(String str) {
		if (str == null) {
			return null;
		}
		return convertShiftJISI2M(new StringBuilder(str)).toString();
	}

	/**
	 * MS ShiftJISの変換テーブルにあわせる処理. (StringBufferバージョン)
	 * 
	 * @param buf
	 *            IBM Shift-JISを含む文字列
	 * @return MS ShiftJISの変換テーブルに一致する文字列
	 */
	public static StringBuffer convertShiftJISI2M(StringBuffer buf) {
		if (buf == null) {
			return null;
		}
		StringBuilder b = convertShiftJISI2M(new StringBuilder(buf.toString()));
		buf.delete(0, buf.length());
		buf.append(b.toString());
		return buf;
	}

	/**
	 * MS ShiftJISの変換テーブルにあわせる処理. (StringBuilderバージョン)
	 * 
	 * @param buf
	 *            IBM Shift-JISを含む文字列
	 * @return MS ShiftJISの変換テーブルに一致する文字列
	 */
	public static StringBuilder convertShiftJISI2M(StringBuilder buf) {
		if (buf == null) {
			return null;
		}
		int max = buf.length();
		for (int idx = 0; idx < max; idx++) {
			char c = buf.charAt(idx);
			char cvt = c;
			if (c == '\u2014') {
				cvt = '\u2015';
			} else if (c == '\u301c') {
				cvt = '\uff5e';
			} else if (c == '\u2016') {
				cvt = '\u2225';
			} else if (c == '\u2212') {
				cvt = '\uff0d';
			} else if (c == '\u00a6') {
				cvt = '\uffe4';
			}
			buf.setCharAt(idx, cvt);
		}
		return buf;
	}

	// //////////////////////////////////////////////////////////////////////////////
	private static String pad(String inStr, int byteLen, String strPad, boolean rightPad) {
		if (strPad == null || StringUtil.countLengthByByte(strPad) < 1) {
			throw new IllegalArgumentException("パディング文字列が指定されていません");
		}

		if (inStr == null) {
			inStr = "";
		}

		int inLen = StringUtil.countLengthByByte(inStr); // 元の文字列の長さ
		int targetLen = byteLen - inLen; // 追加する文字列の上限
		int padLen = StringUtil.countLengthByByte(strPad); // 連結する文字列の長さ
		String pad = "";

		StringBuffer buf = new StringBuffer();
		if (inLen > byteLen) { // 元の文字列の長さが、長さの上限を超えているとき。
			if (rightPad) {
				return StringUtil.substringByByte(inStr, 0, byteLen); // 右側に文字を連結しようとしたとき。
			} else {
				return StringUtil.substringByByte(inStr, (targetLen * -1), inLen); // 左側に文字を連結しようとしたとき。
			}
		} else if (inLen < byteLen) { // 元の文字列の長さが、長さの上限未満のとき。
			// 追加する文字列の上限以上になるまで連結文字列を繋ぐ
			for (int i = 0; i < targetLen; i += padLen) {
				buf.append(strPad);
			}
			pad = StringUtil.substringByByte(buf.toString(), 0, targetLen);
		}

		if (rightPad) {
			return inStr + pad;
		} else {
			return pad + inStr;
		}
	}

	public static void main(String[] aa) {
		System.out.println(StringUtil.encrypt("123456"));
		try {
			System.out.println(URLDecoder.decode("%E6%98%A8%E5%A4%A9%E8%BD%AC%E8%AE%A9%E4%BF%A1%E6%81%AF","UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// リストのn番目の要素を取り出す。
	private static Object getListElement(List<?> plstList, int pintIndex) {
		Object objData = plstList.get(pintIndex);
		if (objData == null) {
			return null;
		}
		return objData.toString();
	}
} // end of 'public class StringUtil {'
