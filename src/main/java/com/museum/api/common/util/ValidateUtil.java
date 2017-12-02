package com.museum.api.common.util;



import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 各種データの検査を行うクラスです。
 *
 * @author ttc
 * @version 0.9
 */
public class ValidateUtil {

	/**
	 * システムで規定されている禁則文字。<br>
	 *
	 * <pre>
	 * !&quot;#$%&amp;'()=&tilde;|\@[+;:]&lt;&gt;,._
	 * </pre>
	 */
	private static final String SYSTEM_FORBIDDEN_CHARS = "!\"#$%&\'()=~|\\@[+;:]<>,._";

	/**
	 * 半角カナの判定ルール
	 */
	private static final String RULE_KANA = "^[ \uFF61-\uFF9F]*$";

	/**
	 * 改行コード
	 */
	private static final String lineSeparator = System.getProperty("line.separator");

	/**
	 * 文字列が半角のアルファベットと数字のみで構成されているかどうかを検査する。<br>
	 * <br>
	 * 検査する文字列のすべての文字について、英数字か英数字以外かの判定を行う。
	 *
	 * @param pstrTarget
	 *            検査する文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>0</td>
	 *         <td>半角の英数字のみで構成されている。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>1</td>
	 *         <td>半角の英数字以外の文字がある。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>-11</td>
	 *         <td>検査する文字列がnullだった。</td>
	 *         </tr>
	 *         </table>
	 */
	public static int chkAlphaNumeric(String pstrTarget) {
		if (pstrTarget == null) {
			return -11;
		}

		if (pstrTarget.matches("^[\\w]*$")) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 文字列内に禁則文字が含まれているかどうかを検査する。<br>
	 * <br>
	 * 禁則文字群の長さが0のときはシステムで規定されている禁則文字群を使用して検査を行う。
	 *
	 * @param pstrTarget
	 *            検査する文字列
	 * @param pstrForbidden
	 *            禁則文字群(省略時(nullまたはサイズ0の文字列))
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>\u0000</td>
	 *         <td>禁則文字群に一致しなかった。または検査する文字列がnullだった。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>上記以外</td>
	 *         <td>最初に一致した文字</td>
	 *         </tr>
	 *         </table>
	 */
	public static char chkForbiddenChar(String pstrTarget, String pstrForbidden) {

		if (pstrForbidden == null || pstrForbidden.length() == 0) {
			pstrForbidden = SYSTEM_FORBIDDEN_CHARS;
		}
		return StringUtil.findChar(pstrTarget, pstrForbidden);
	}

	/**
	 * 文字列内にシステムで規定されている禁則文字が含まれているかどうかを検査する。<br>
	 * <br>
	 * システムで規定されている禁則文字群 <font face="ＭＳ ゴシック"><b>!"#$%&'()=~|\@[+;:]
	 * <>,._</b></font>
	 *
	 * @param pstrTarget
	 *            検査する文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>\u0000</td>
	 *         <td>禁則文字群に一致しなかった。または検査する文字列がnullだった。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>上記以外</td>
	 *         <td>最初に一致した文字</td>
	 *         </tr>
	 *         </table>
	 */
	public static char chkForbiddenChar(String pstrTarget) {
		return chkForbiddenChar(pstrTarget, SYSTEM_FORBIDDEN_CHARS);
	}

	/**
	 * 文字列内に全角文字があるどうかを検査する。<br>
	 * <br>
	 * 検査する文字列のすべての文字について、全角文字か半角文字かの判定を行う。
	 *
	 * @param pstrTarget
	 *            検査する文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>0以上</td>
	 *         <td>最初に見つかった全角文字のインデックス。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>-1</td>
	 *         <td>全角文字はない。(すべて半角文字)</td>
	 *         </tr>
	 *         <tr>
	 *         <td>-11</td>
	 *         <td>検査する文字列がnullだった。</td>
	 *         </tr>
	 *         </table>
	 * @deprecated {@link ValidateUtil#isSingleByteString(String)}
	 */
	public static int chkMultiByteChar(String pstrTarget) {
		if (pstrTarget == null) {
			return -11;
		}

		int intRet = -1; // すべて半角文字をデフォルト値
		CharacterIterator ite = new StringCharacterIterator(pstrTarget);
		for (char c = ite.first(); c != CharacterIterator.DONE; c = ite.next()) {
			if ((c >= StringUtil.ALPHABETLO && c <= StringUtil.ALPHABETHI) // 欧米語の範囲内
					|| (c >= StringUtil.KANALO_UTF_8 && c <= StringUtil.KANAHI_UTF_8)) { // 日本語の範囲内
				;
			} else {
				intRet = pstrTarget.indexOf(c); // 半角文字の範囲にないときのインデックス
				break;
			}
		}
		return intRet;
	}

	/**
	 * 数字列の文字数が最小桁数以上あるかどうかを検査する。<br>
	 * <br>
	 * 整数部の桁数と小数部の桁数を、それぞれ指定された最小桁数と比較する。
	 *
	 * @param pstrTarget
	 *            検査する文字列
	 * @param pintIntLen
	 *            整数部の最小桁数（検査をしないときは「-1」を指定する。整数部の桁数の判定は「最小桁数以上」となる。）
	 * @param pintDecLen
	 *            小数部の最小桁数（検査をしないときは「-1」を指定する。小数部の桁数の判定は「最小桁数以上」となる。）
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td align="right">0</td>
	 *         <td>整数部、小数部ともに最小桁数以上ある。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">1</td>
	 *         <td>整数部の長さは最小桁数未満で、小数部の長さは最小桁数以上である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">2</td>
	 *         <td>整数部の長さは最小桁数以上で、小数部の長さは最小桁数未満である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">3</td>
	 *         <td>整数部、小数部ともに最小桁数未満である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-1</td>
	 *         <td>整数部の最小桁数が-1以上の整数でない。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-2</td>
	 *         <td>小数部の最小桁数が-1以上の整数でない。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-3</td>
	 *         <td>検査する文字列が数値として正しくない。
	 *         <tr>
	 *         <td align="right">-11</td>
	 *         <td>検査する文字列がnullだった。</td>
	 *         </tr>
	 *         </table>
	 */

	public static int chkMinDigits(String pstrTarget, int pintIntLen, int pintDecLen) {
		if (pstrTarget == null) {
			return -11;
		}

		int intNumCheck = chkNumeric(pstrTarget);
		if (intNumCheck < 0) {
			return -3; // 検査する文字列が数値として正しくない。
		}

		if (pintIntLen >= -1) { // 整数部の最大桁数が-1以上の整数でないとき。
			;
		} else {
			return -1;
		}

		if (pintDecLen >= -1) { // 小数部の最大桁数が-1以上の整数でないとき。
			;
		} else {
			return -2;
		}
		// エラーチェックはここまで

		int intRetVal;
		String value = pstrTarget.replaceAll(",", "");

		boolean blnInteger = true;
		if (pintIntLen != -1) {
			blnInteger = value.matches("^[+-]?[0-9]{" + pintIntLen + ",}(\\.[0-9+-]*$|$)"); // 整数部の長さの検査
		}
		boolean blnDecimal = true;
		if (pintDecLen == 0) { // 小数点の最小桁数が0の場合は小数点自体があるといけない
			blnDecimal = value.indexOf(".") != -1;
		} else if (pintDecLen > 0) {
			blnDecimal = value.matches("^[+-]?[0-9]{1,}\\.[0-9]{" + pintDecLen + ",}[+-]?$"); // 小数部の長さの検査
		}

		if (blnInteger) {
			if (blnDecimal) {
				intRetVal = 0; // 整数部、小数部ともに最小桁数以上ある。
			} else {
				intRetVal = 2; // 整数部の長さは最小桁数以上で、小数部の長さは最小桁数未満である。
			}
		} else {
			if (blnDecimal) {
				intRetVal = 1; // 整数部の長さは最小桁数未満で、小数部の長さは最小桁数以上である。
			} else {
				intRetVal = 3; // 整数部、小数部ともに最小桁数未満である。
			}
		}
		return intRetVal;
	}

	/**
	 * 数字列の文字数が最大桁数以下かを検査する。<br>
	 * <br>
	 * 整数部の桁数と小数部の桁数を、それぞれ指定された最大桁数と比較する。
	 *
	 * @param pstrTarget
	 *            検査する文字列
	 * @param pintIntLen
	 *            整数部の最大桁数（検査をしないときは「-1」を指定する。整数部の桁数の判定は「最大桁数以下」となる。）
	 * @param pintDecLen
	 *            小数部の最大桁数（検査をしないときは「-1」を指定する。小数部の桁数の判定は「最大桁数以下」となる。）
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td align="right">0</td>
	 *         <td>整数部、小数部ともに最大桁数以下である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">1</td>
	 *         <td>整数部の長さは最大桁数を超えていて、小数部の長さは最大桁数以下である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">2</td>
	 *         <td>整数部の長さは最大桁数以下で、小数部の長さは最大桁数を超えている。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">3</td>
	 *         <td>整数部、小数部ともに最大桁数を超えている。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-1</td>
	 *         <td>整数部の最大桁数が-1以上の整数でない。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-2</td>
	 *         <td>小数部の最大桁数が-1以上の整数でない。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-3</td>
	 *         <td>検査する文字列が数値として正しくない。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-11</td>
	 *         <td>検査する文字列がnullだった。</td>
	 *         </tr>
	 *         </table>
	 */

	public static int chkMaxDigits(String pstrTarget, int pintIntLen, int pintDecLen) {
		if (pstrTarget == null) {
			return -11;
		}

		int intNumCheck = chkNumeric(pstrTarget);
		if (intNumCheck < 0) {
			return -3; // 検査する文字列が数値として正しくない。
		}

		if (pintIntLen >= -1) { // 整数部の最大桁数が-1以上の整数でないとき。
			;
		} else {
			return -1;
		}

		if (pintDecLen >= -1) { // 小数部の最大桁数が-1以上の整数でないとき。
			;
		} else {
			return -2;
		}
		// エラーチェックはここまで

		int intRetVal;

		String value = pstrTarget.replaceAll(",", "");
		boolean blnInteger = true;
		if (pintIntLen != -1) {
			blnInteger = value.matches("^[+-]?[0-9]{0," + pintIntLen + "}(\\.[0-9]+)?[+-]?$"); // 整数部の長さの検査
		}
		boolean blnDecimal = true;
		if (pintDecLen != -1) {
			blnDecimal = value.matches("^[+-]?[0-9]+(\\.[0-9]{1," + pintDecLen + "})?[+-]?$"); // 小数部の長さの検査
		}

		if (blnInteger) {
			if (blnDecimal) {
				intRetVal = 0; // 整数部、小数部ともに最大桁数以下である。
			} else {
				intRetVal = 2; // 整数部の長さは最大桁数以下で、小数部の長さは最大桁数を超えている。
			}
		} else {
			if (blnDecimal) {
				intRetVal = 1; // 整数部の長さは最大桁数を超えていて、小数部の長さは最大桁数以下である。
			} else {
				intRetVal = 3; // 整数部、小数部ともに最大桁数を超えている。
			}
		}
		return intRetVal;
	}

	/**
	 * 文字列がnullまたはブランクかどうかを検査する。<br>
	 *
	 * @param str
	 *            検査する文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>true</td>
	 *         <td>nullまたはブランクである。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>false</td>
	 *         <td>nullでない、かつブランクでない。</td>
	 *         </tr>
	 *         </table>
	 */
	public static boolean isNullOrBlank(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * オブジェクトがnullかどうかを検査する。<br>
	 * <br>
	 * 検査する変数が下記のいずれかのときもnullとみなす。
	 * <UL>
	 * <LI>文字型で長さが0のとき。
	 * <LI>文字型で半角、全角に関わらずブランク文字だけのとき。
	 * </UL>
	 * ※マルチラインの場合、文前後の空行は削除するが、文中の空行は削除しない。
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
		if (pobjObject == null) {
			return true;
		}
		if (pobjObject instanceof String) {
			String s = (String) pobjObject;
			if (StringUtil.trim(StringUtil.trimMulti(s, false)).trim().length() < 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * オブジェクトがnullかどうかを検査する。<br>
	 * <br>
	 * isNull()での検査に加えて、文字型で数値の0とみなせるとき("0"や"0.0"など)もnullとする。
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
	public static boolean isNullOrZero(Object pobjObject) {
		if (pobjObject == null) {
			return true;
		}
		if (pobjObject instanceof String) {
			String s = (String) pobjObject;
			try {
				String num = StringUtil.trim(s);
				if (num.length() < 1) {
					return true;
				}
				// DoubleでIntegerもカバーできるためDoubleのみで評価
				if (Double.parseDouble(num) == 0.0d) {
					return true;
				}
			} catch (NumberFormatException e) {
				// 数値に変換できない時はNullであにという判定
				return false;
			}
		}
		return false;
	}

	/**
	 * 文字列が数字のみで構成されているかを検査する。<br>
	 * <br>
	 * 指定された文字列が数字のみで構成されているとき、trueを返す。<br>
	 * 符号("+"、"-")があった場合、falseを返す。<br>
	 * 指定された文字列がnullの場合、falseを返す。<br>
	 *
	 * @param pstrString
	 *            検査する文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>true</td>
	 *         <td>数字のみで構成されていた。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>false</td>
	 *         <td>数字以外の文字があった</td>
	 *         </tr>
	 *         </table>
	 */

	public static boolean isNumeric(String pstrString) {
		if (pstrString == null) {
			throw new NullPointerException("");
		}
		return pstrString.matches("^[\\d]*$");
	}

	/**
	 * 文字列がATSGに則したパスワードになっているかを検査する。<br>
	 * <br>
	 * 検査する文字列が下記のすべてを満足するとき、有効と判定する。1つ以上の条件を満足しないとき、無効と判定する。
	 * <UL>
	 * <LI>文字数が6文字以上である。
	 * <LI>複数の文字種で構成されている。（文字種：数字、英大文字、英小文字、記号）
	 * </UL>
	 *
	 * @param pstrTarget
	 *            検査する文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>0</td>
	 *         <td>パスワードとして有効である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>1</td>
	 *         <td>文字数が足りない。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>2</td>
	 *         <td>構成している文字種が足りない。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>-11</td>
	 *         <td>検査する文字列がnullだった。</td>
	 *         </tr>
	 *         </table>
	 */

	public static int chkValidPassword(String pstrTarget) {
		if (pstrTarget == null) {
			return -11;
		}

		if (pstrTarget.length() < 6) {
			return 1;
		}
		// エラーチェックはここまで

		final String SYMBOL_CHARS = "!\"#$%&'()=~|-^\\`{+*}<>?_@[;:],./";
		// 半角の記号 !"#$%&'()=~|-^\`{+*}<>?_@[;:],./
		boolean blnUseLarge = false; // 大文字の使用の有無
		boolean blnUseSmall = false; // 小文字の使用の有無
		boolean blnUseNumeric = false; // 数字の使用の有無
		boolean blnUseSymbol = false; // 記号の使用の有無

		CharacterIterator ite = new StringCharacterIterator(pstrTarget);
		for (char c = ite.first(); c != CharacterIterator.DONE; c = ite.next()) {
			if (c >= '0' && c <= '9') {
				blnUseNumeric = true;
				continue;
			} else if (c >= 'a' && c <= 'z') {
				blnUseSmall = true;
				continue;
			} else if (c >= 'A' && c <= 'Z') {
				blnUseLarge = true;
				continue;
			} else {
				int idx = SYMBOL_CHARS.indexOf(c);
				if (idx >= 0) { // 記号のいずれかに一致したとき
					blnUseSymbol = true;
				}
			}
		}

		int intCnt = 0; // 使用している文字種のカウンタ
		int intRetVal;

		if (blnUseLarge) {
			intCnt++; // 大文字
		}
		if (blnUseSmall) {
			intCnt++; // 小文字
		}
		if (blnUseNumeric) {
			intCnt++; // 数字
		}
		if (blnUseSymbol) {
			intCnt++; // 記号
		}

		if (intCnt >= 2) {
			intRetVal = 0; // 2種類以上使用されていたら、パスワードとして有効
		} else {
			intRetVal = 2; // 1種類以下のとき「文字種が足りない」
		}
		return intRetVal;
	}

	/**
	 * 文字列が正の数、負の数、0のいずれであるか、また整数か実数かを検査する。<br>
	 * <br>
	 * 検査する文字列に対して下記の判定を行う。
	 * <UL>
	 * <LI>正の数、負の数、0の判定を行う。
	 * <table>
	 * <tr>
	 * <td>先頭または末尾の一方に<b>「+」があるか、またはどちらにも符号がないとき</b>、正の数と判定する。</td>
	 * </tr>
	 * <tr>
	 * <td>先頭または末尾の一方に</b>「-」があるとき</b>、負の数と判断する。</td>
	 * </tr>
	 * <tr>
	 * <td>符号を除いた文字列が<b>「0」のみ、または「0」と小数点のみで構成されているとき</b>、0と判断する。</td>
	 * </tr>
	 * </table>
	 * <LI>整数か実数かの判定を行う。
	 * <table>
	 * <tr>
	 * <td><b>小数点が1つだけ</b>あり、小数点が検査する文字列の先頭、末尾以外にあるとき、実数と判定する。</td>
	 * </tr>
	 * <tr>
	 * <td><b>小数点がないとき</b>、整数と判断する。</td>
	 * </tr>
	 * </table>
	 * </UL>
	 *
	 * @param pstrTarget
	 *            検査する文字列
	 * @param pintSignPos
	 *            符号の位置（符号が必要な場合、どこに書かれるか）
	 *            <table>
	 *            <tr>
	 *            <td align="right">1</td>
	 *            <td>先頭にあること</td>
	 *            </tr>
	 *            <tr>
	 *            <td align="right">2</td>
	 *            <td>末尾にあること</td>
	 *            </tr>
	 *            <tr>
	 *            <td align="right">上記以外</td>
	 *            <td>先頭、末尾のいずれでも可</td>
	 *            </tr>
	 *            </table>
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td align="right">0x00</td>
	 *         <td>0である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">0x01</td>
	 *         <td>正の整数である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">0x02</td>
	 *         <td>正の実数である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">0x11</td>
	 *         <td>負の整数である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">0x12</td>
	 *         <td>負の実数である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-1</td>
	 *         <td>検査する文字列が数値として正しくない。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right" valign="top">-2</td>
	 *         <td>符号の位置が正しくない。<br>
	 *         （符号の位置として2（末尾）を指定したが、先頭にあったときなど）</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-3</td>
	 *         <td>先行する0がある。<br>
	 *         <tr>
	 *         <td align="right">-11</td>
	 *         <td>検査する文字列がnullだった。</td>
	 *         </tr>
	 *         </table>
	 */

	public static int chkNumeric(String pstrTarget, int pintSignPos) {
		if (pstrTarget == null) {
			return -11;
		}

		if (pstrTarget.length() <= 0) {
			return -1;
		}

		final String NUMBERS_CHARS = "1234567890.+-,";

		// 数字、符号以外の文字があれば-1を返す。
		CharacterIterator iteSignChk = new StringCharacterIterator(pstrTarget);
		for (char c = iteSignChk.first(); c != CharacterIterator.DONE; c = iteSignChk.next()) {
			if (NUMBERS_CHARS.indexOf(c) <= -1) { // 数字、符合のいずれにも一致しないとき
				return -1;
			}
		}

		int intCheckSign; // 符号の検査結果 (1...先頭に+ 、2...末尾に+ 、
		// -1...先頭に- 、-2...末尾に- 、
		// 0...ナシ 、
		// 5...符号が2つ以上あった、6...符号が先頭、末尾以外にあった)

		intCheckSign = chkSignPos(pstrTarget); // 符号の位置の検査
		if (intCheckSign != 0) { // 符号があったとき
			if (intCheckSign == 6 || intCheckSign == 5) { // 符号が先頭、末尾以外にあった or
															// 符号が2つ以上あった
				return -1;
			} else {
				if (intCheckSign == -11) { // nullのとき
					return -11;
				}
			}

			int intCheckABS = Math.abs(intCheckSign);
			// 先頭と指定されていたが、末尾にあったとき or 末尾と指定されていたが、先頭にあったとき
			if ((pintSignPos == 1 && intCheckABS == 2) || (pintSignPos == 2 && intCheckABS == 1)) {
				return -2;
			}
		}

		String strNumStr = getNumStr(pstrTarget, intCheckSign); // 先頭または末尾の符号を除いた、正味の数値を取り出す。

		// 数字フォーマットの検証
		int intCheckFormat = ValidateUtil.chkDecimalFormat(strNumStr);
		if (intCheckFormat == -1) {
			return -1;
		}

		// 整数か実数かの判定
		boolean blnReal; // 整数、実数の判定結果 (true...実数 、false...整数)
		int intDotPos = strNumStr.indexOf("."); // .の位置
		int intOrgLen = strNumStr.length(); // 正味の数値の長さ

		if (intDotPos >= 0) { // .があったとき
			// 先頭にあったとき or //末尾にあったとき
			if (intDotPos == 0 || intDotPos == intOrgLen - 1) {
				return -1;
			}
			blnReal = true; // 先頭、末尾以外にあったとき、実数
		} else { // .がなかったとき、整数
			blnReal = false;
		}

		// 先行する0の判定
		// NGとなる条件(下記のすべての条件を満たすとき)
		// 1. 1文字目が"0"
		// 2. 2文字以上
		// 3. 2文字目が"."以外のとき
		char str1Char = strNumStr.charAt(0);
		if (str1Char == '0') {
			if (intOrgLen >= 2) {
				str1Char = strNumStr.charAt(1);
				if (str1Char != '.') {
					return -3;
				}
			}
		}

		// 0かどうかの判定と戻り値の組み立て
		boolean blnZero = true;
		CharacterIterator iteNumChk = new StringCharacterIterator(strNumStr);
		for (char c = iteNumChk.first(); c != CharacterIterator.DONE; c = iteNumChk.next()) {
			if (c >= '1' && c <= '9') {
				blnZero = false;
				break;
			}
		}

		int intRetVal;
		if (blnZero) {
			intRetVal = 0x00;
		} else { // 0以外のとき
			if (blnReal) {
				intRetVal = 0x02; // 実数のとき
			} else {
				intRetVal = 0x01; // 整数のとき
			}

			if (intCheckSign <= -1) {
				intRetVal |= 0x10; // -のとき
			}
		}
		return intRetVal;
	}

	/**
	 * 文字列が正の数、負の数、0のいずれであるか、また整数か実数かを検査する。<br>
	 * <br>
	 * 検査する文字列に対して下記の判定を行う。
	 * <UL>
	 * <LI>文字列の先頭の符号で正の数、負の数、0の判定を行う。
	 * <table>
	 * <tr>
	 * <td><b>「+」があるとき</b>、正の数と判定する。</td>
	 * </tr>
	 * <tr>
	 * <td><b>「-」があるとき</b>、負の数と判断する。</td>
	 * </tr>
	 * <tr>
	 * <td><b>符号がないとき</b>、正の数と判定する。</td>
	 * </tr>
	 * <tr>
	 * <td>符号を除いた文字列が<b>「0」のみ、または「0」と小数点のみで構成されているとき</b>、0と判断する。</td>
	 * </tr>
	 * </table>
	 * <LI>整数か実数かの判定を行う。
	 * <table>
	 * <tr>
	 * <td><b>小数点が1つだけ</b>あり、小数点が検査する文字列の先頭、末尾以外にあるとき、実数と判定する。</td>
	 * </tr>
	 * <tr>
	 * <td><b>小数点がないとき</b>、整数と判断する。</td>
	 * </tr>
	 * </table>
	 * </UL>
	 *
	 * @param pstrTarget
	 *            検査する文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td align="right">0x00</td>
	 *         <td>0である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">0x01</td>
	 *         <td>正の整数である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">0x02</td>
	 *         <td>正の実数である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">0x11</td>
	 *         <td>負の整数である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">0x12</td>
	 *         <td>負の実数である。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-1</td>
	 *         <td>検査する文字列が数値として正しくない。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-2</td>
	 *         <td>符号の位置が正しくない。<br>
	 *         <tr>
	 *         <td align="right">-11</td>
	 *         <td>検査する文字列がnullだった。</td>
	 *         </tr>
	 *         </table>
	 */

	public static int chkNumeric(String pstrTarget) {
		return chkNumeric(pstrTarget, 1);
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
	 * @deprecated
	 */
	public static char findChar(String pstrTarget, String pstrMatching) {
		return StringUtil.findChar(pstrTarget, pstrMatching);
	}

	/**
	 * 文字列が全て半角かを検査する。<br>
	 * <br>
	 * <br>
	 * デフォルトの文字セットで文字列の全角と半角の検査を行う。<br>
	 * 文字列がnullのときはfalseを返す。
	 *
	 * @param value
	 *            文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>true</td>
	 *         <td>全て半角だった。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>false</td>
	 *         <td>全角文字があった。</td>
	 *         </tr>
	 *         </table>
	 */

	public static boolean isSingleByteString(String value) {
		return ValidateUtil.isSingleByteString(value, CommonProperty.getEncode());
	}

	/**
	 * 文字列が全て全角かを検査する。<br>
	 * <br>
	 * <br>
	 * デフォルトの文字セットで文字列の全角検査を行う。<br>
	 * 文字列がnull、または長さ0のときはfalseを返す。
	 *
	 * @param value
	 *            文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>true</td>
	 *         <td>全て全角。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>false</td>
	 *         <td>全角文字以外の文字が含まれている。</td>
	 *         </tr>
	 *         </table>
	 */

	public static boolean isZenkaku(String value) {
		return isZenkaku(value, true);
	}

	/**
	 * 文字列が全て全角かを検査する。<br>
	 * <br>
	 * <br>
	 * デフォルトの文字セットで文字列の全角検査を行う。<br>
	 * 文字列がnull、または長さ0のときはfalseを返す。<br>
	 * 引数のlineSeparateFlagがtrueの場合は、文字列中に改行文字が含まれていても入力検査エラーとならないが、
	 * falseの場合は、改行文字が含まれていると入力検査エラーとなる。
	 *
	 *
	 * @param value
	 *            文字列
	 * @param lineSeparateFlag
	 *            改行文字入力許可フラグ（true:改行コード入力可能／false:改行コード入力不可能）
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>true</td>
	 *         <td>全て全角。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>false</td>
	 *         <td>全角文字以外の文字が含まれている。</td>
	 *         </tr>
	 *         </table>
	 */

	public static boolean isZenkaku(String value, boolean lineSeparateFlag) {

		if (value == null) {
			return false;
		}

		if (!lineSeparateFlag && value.indexOf(lineSeparator) >= 0) {
			return false;
		}

		return value.matches("^[　]*[^ -~｡-ﾟ]*[　]*$");
	}

	/**
	 * 文字セットを指定して、文字列が全て半角かを検査する。<br>
	 * <br>
	 * 文字列または文字セットがnullのときはfalseを返す。
	 *
	 * @param value
	 *            文字列
	 * @param enc
	 *            文字セット
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td>true</td>
	 *         <td>全て半角だった。</td>
	 *         </tr>
	 *         <tr>
	 *         <td>false</td>
	 *         <td>全角文字があった。</td>
	 *         </tr>
	 *         </table>
	 */

	public static boolean isSingleByteString(String value, String enc) {
		if (value == null || enc == null) {
			return false;
		}
		// すべて半角であれば、文字数とバイト数が一致する。
		return (StringUtil.countLengthByByte(value, enc) == value.length());
	}

	/**
	 * 数値を表す文字列の、符号を除いた部分を取り出す。<br>
	 * <br>
	 * 元の文字列がnullのときは、""を返す。
	 *
	 * @param pstrString
	 *            元の文字列
	 * @param pintSign
	 *            符号の位置
	 *            <table>
	 *            <tr>
	 *            <td align="right">1</td>
	 *            <td>先頭に+がある。</td>
	 *            </tr>
	 *            <tr>
	 *            <td align="right">2</td>
	 *            <td>末尾に+がある。</td>
	 *            </tr>
	 *            <tr>
	 *            <td align="right">-1</td>
	 *            <td>先頭に-がある。</td>
	 *            </tr>
	 *            <tr>
	 *            <td align="right">-2</td>
	 *            <td>末尾に-がある。</td>
	 *            </tr>
	 *            <tr>
	 *            <td align="right">0</td>
	 *            <td>符号ナシ</td>
	 *            </tr>
	 *            </table>
	 * @return 取り出した数字列
	 */

	private static String getNumStr(String pstrString, int pintSign) {
		String strWorkStr = "";

		if (pstrString != null) {
			int intSign = Math.abs(pintSign);
			if (intSign == 0) {
				strWorkStr = pstrString; // 符号がなかったときはそのまま
			}
			if (intSign == 1) {
				strWorkStr = pstrString.substring(1); // 符号が先頭にあったときは2文字目から最後まで
			}
			if (intSign == 2) {
				int intLen = pstrString.length();
				strWorkStr = pstrString.substring(0, intLen - 1); // 符号が末尾にあったときは1文字目から文字数-1まで
			}
		}
		return strWorkStr;
	}

	/**
	 * 指定された文字列を数値と判断できるかどうかを判定する。 <br>
	 * 判定内容<BR>
	 * 数字、.、,、-以外の文字が含まれているか。(左記以外の文字があるときはエラーとする。)<BR>
	 * 小数点の数および位置<BR>
	 * カンマ区切りの位置
	 *
	 * @param aTarget
	 *            検査する文字列
	 * @return 検査結果
	 *         <table>
	 *         <tr>
	 *         <td align="right">0</td>
	 *         <td>エラーなし</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-1</td>
	 *         <td>エラーあり</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-11</td>
	 *         <td>検査する文字列がnullだった。</td>
	 *         </tr>
	 *         </table>
	 */

	public static int chkDecimalFormat(String aTarget) {
		int intRetVal = 0;
		final int NUM_ERROR = -1;

		if (ValidateUtil.isNull(aTarget)) {
			return -11;
		}

		CharacterIterator ite = new StringCharacterIterator(aTarget);
		int numCnt = 0;
		int cmCnt = 0; // ,の数
		int pdCnt = 0; // .の数

		boolean minusFlg = false;

		for (char c = ite.first(); c != CharacterIterator.DONE; c = ite.next()) {
			if (c >= '0' && c <= '9') {
				if (pdCnt == 0) {
					numCnt++;
				}
			} else {
				int idx = ite.getIndex();
				if (c == '.') {
					// 先頭にピリオド、複数のピリオド、最後にピリオドがあるか？
					if (idx < 1 || pdCnt > 0 || idx == ite.getEndIndex() - 1) {
						intRetVal = NUM_ERROR;
						break;
					}
					pdCnt++;
				} else if (c == ',') {
					// 小数部に,があるとき
					if (pdCnt >= 1) {
						intRetVal = NUM_ERROR;
						break;
					}

					cmCnt++;
					// 先頭にカンマまたは先頭から3桁以上の位置で数字の数が3以外
					if (idx == 0 || (numCnt != 3 && idx > 2)) {
						intRetVal = NUM_ERROR;
						break;
					}
					numCnt = 0;
				} else if (c == '-') {
					minusFlg = true;
					// 先頭以外の位置にマイナス符号
					if (idx != 0) {
						intRetVal = NUM_ERROR;
						break;
					}
				} else {
					// ピリオド、カンマ、マイナス符号以外の文字
					intRetVal = NUM_ERROR;
					break;
				}
			}
		} // end of 'for(char c = ite.first(); c != CharacterIterator.DONE; c =
			// ite.next()){'

		// カンマが1以上あり、且つ最終カンマ後の数字が3以外
		if (cmCnt > 0 && numCnt != 3) {
			intRetVal = NUM_ERROR;
		}
		if (minusFlg && numCnt < 1) {
			intRetVal = NUM_ERROR;
		}
		return intRetVal;
	}

	/**
	 * 数字を意味する文字列内の符号の位置を検査する。<br>
	 * <br>
	 * 文字列内に+、-の符号がどこにあるかを検査する。
	 *
	 * @param pstrString
	 *            検査する文字列
	 * @return 符号の位置
	 *         <table>
	 *         <tr>
	 *         <td align="right">1</td>
	 *         <td>先頭に+がある。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">2</td>
	 *         <td>末尾に+がある。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-1</td>
	 *         <td>先頭に-がある。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-2</td>
	 *         <td>末尾に-がある。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">0</td>
	 *         <td>符号ナシ</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">5</td>
	 *         <td>符号が2つ以上あった。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">6</td>
	 *         <td>符号が先頭、末尾以外にあった。</td>
	 *         </tr>
	 *         <tr>
	 *         <td align="right">-11</td>
	 *         <td>検査する文字列がnullだった。</td>
	 *         </tr>
	 *         </table>
	 */

	private static int chkSignPos(String pstrString) {
		if (pstrString == null) {
			return -11;
		}

		// 符号がいくつあるか数える。
		int intSignCnt = 0; // 符号の数
		CharacterIterator ite = new StringCharacterIterator(pstrString);
		for (char c = ite.first(); c != CharacterIterator.DONE; c = ite.next()) {
			if (c == '+' || c == '-') {
				intSignCnt++;
			}
		}

		if (intSignCnt >= 2) {
			return 5; // 符号が2つ以上あったとき
		}

		int intMinusSignPos = pstrString.indexOf("-"); // -の有無
		int intPlusSignPos = pstrString.indexOf("+"); // +の有無
		int intOrgLen = pstrString.length();
		int intRetVal = 0; // 符号ナシをデフォルト値

		if (intMinusSignPos >= 0) { // -があったとき
			if (intMinusSignPos == 0) { // 先頭にあったとき
				intRetVal = -1;
			} else {
				if (intMinusSignPos == intOrgLen - 1) { // 末尾にあったとき
					intRetVal = -2;
				} else {
					intRetVal = 6; // 先頭、末尾以外にあったとき
				}
			}
		}

		if (intPlusSignPos >= 0) { // +があったとき
			if (intPlusSignPos == 0) { // 先頭にあったとき
				intRetVal = 1;
			} else {
				if (intPlusSignPos == intOrgLen - 1) { // 末尾にあったとき
					intRetVal = 2;
				} else {
					intRetVal = 6; // 先頭、末尾以外にあったとき
				}
			}
		}
		return intRetVal;
	}

	/**
	 * <p>
	 * 半角カナチェック処理。
	 * </p>
	 * <p>
	 * 指定された文字列が半角カナと半角ブランクのみであるかどうかをチェックする。
	 * </p>
	 * <p>
	 * 。
	 * </p>
	 *
	 * @param value
	 *            チェック対象文字列
	 * @return 半角カナおよび半角ブランクのみの場合はtrueを返却し、そうでない場合はfalseを返却。
	 */
	public static boolean isKana(String value) {
		if (value == null) {
			return true;
		}
		return value.matches(ValidateUtil.RULE_KANA);
	}

	/**
	 * 日付チェック。<br>
	 * 年、月、日を別々に指定して日付を検査する。<br>
	 * STRUTS用VALIDATORへの実装対象外。<br>
	 *
	 * @param year
	 *            年を表す文字列
	 * @param month
	 *            月を表す文字列
	 * @param day
	 *            日を表す文字列
	 * @return 妥当な日付であればtrueを返却し、そうでなければfalseを返却する。
	 */
	public static boolean chkDate(String year, String month, String day) {
		// lengthのチェック
		if (year == null || month == null || day == null || year.length() != 4 || month.length() > 2
				|| day.length() > 2) {
			return false;
		}
		String dateValue = year + "/" + month + "/" + day;
		String format = "y/M/d";
		Date date = DateUtil.parse(dateValue, format);
		return date != null;
	}

	/**
	 * 日付チェック。<br>
	 * yyyyMMdd型の日付文字列を検査する。<br>
	 *
	 * @param dateValue
	 *            yyyyMMdd型の日付文字列
	 * @return 妥当な書式および日付であればtrueを返却し、そうでなければfalseを返却する。
	 */
	public static boolean chkDate(String dateValue) {
		// dateValueのlengthチェック
		if (dateValue == null) {
			return false;
		}
		if (dateValue.indexOf("/") != -1) {
			// スラッシュ付きの場合
			String[] dateList = dateValue.split("/");
			if (dateList.length == 3) {
				if (dateList[0].length() != 4 || dateList[1].length() > 2 || dateList[2].length() > 2) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			// スラッシュなしの場合
			if (dateValue.length() != 8) {
				return false;
			}
		}

		Date date = DateUtil.getDate(dateValue);
		return date != null;
	}

	/**
	 * 年月チェック。<br>
	 * yyyyMM型の日付文字列を検査する。<br>
	 *
	 * @param dateValue
	 *            yyyyMMdd型の年月文字列
	 * @return 妥当な書式および年月であればtrueを返却し、そうでなければfalseを返却する。
	 */
	public static boolean chkYearMonth(String dateValue) {

		// dateValueのlengthチェック
		if (dateValue.indexOf("/") != -1) {
			// スラッシュ付きの場合
			String[] dateList = dateValue.split("/");
			if (dateList.length == 2) {
				if (dateList[0].length() != 4 || dateList[1].length() > 2) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			// スラッシュなしの場合
			if (!(4 < dateValue.length() && dateValue.length() <= 6)) {
				return false;
			}
		}

		Date date = DateUtil.getYearMonth(dateValue);
		return date != null;
	}

	/**
	 * 桁数チェック。<br>
	 * 検査対象の文字列を文字数で検査する。<br>
	 * UTF-8環境では全角は必ずしも(というかほとんど)2byteにはならず3byteになる。よって、
	 * よって、バイト数での厳密なチェックは行わない。文字数のチェックとなる。<br>
	 * min と max が双方nullの場合はNullPointerExceptionを投げる
	 *
	 * @param value
	 *            検査対照の文字列(nullを指定した場合は検査対象とないためtrueを返却)
	 * @param min
	 *            最小桁数(nullを指定した場合は最大桁数のチェックだけをして最小桁数はチェックしない)
	 * @param max
	 *            最大桁数(nullを指定した場合は最小桁数のチェックだけをして最大桁数はチェックしない)
	 * @return 指定された桁数以内ならtrue そうでないならfalse
	 */
	public static boolean chkLength(String value, String min, String max) {
		if (value == null) {
			return true;
		}
		if (min == null && max == null) {
			throw new NullPointerException("min and max is null.");
		}
		boolean ret = true;
		if (min != null && !"".equals(min)) {
			if (!min.matches("^[\\d]+$")) {
				throw new IllegalArgumentException("min is not digit.");
			}
			ret &= (Integer.parseInt(min) <= value.length());
		}
		if (max != null && !"".equals(max)) {
			if (!max.matches("^[\\d]+$")) {
				throw new IllegalArgumentException("max is not digit.");
			}
			ret &= (value.length() <= Integer.parseInt(max));
		}
		return ret;
	}

	/**
	 * 引数の文字列の日付形式チェックと第1引数の日付と第2の日付の順序を比較します。<BR>
	 *
	 * @param baseDate
	 *            yyyyMMdd形式またはyyyy/MM/dd形式の日付文字列
	 * @param date
	 *            yyyyMMdd形式またはyyyy/MM/dd形式の日付文字列
	 * @param notEqual
	 *            true：等しい値をエラーとする、false：等しい値を正常とする
	 * @return int 比較結果 第1引数の日付がフォーマットエラーの場合：-1、第2引数の日付がフォーマットエラーの場合：-2、<BR>
	 *         第1、2引数の日付が共にフォーマットエラーの場合：-3、<BR>
	 *         第1引数の日付より第2引数の日付が新しい場合：0、第1引数の日付より第2引数の日付が古い場合：1<BR>
	 */
	public static int checkDateOrder(String baseDate, String date, boolean notEqual) {

		Date dBase = DateUtil.getDate(baseDate);
		Date dDate = DateUtil.getDate(date);

		// null判定
		if ((dBase == null) && (dDate == null)) {
			// 引数両方ともフォーマットエラーの場合
			return -3;
		} else if (dBase == null) {
			// 第1引数のみフォーマットエラーの場合
			return -1;
		} else if (dDate == null) {
			// 第2引数のみフォーマットエラーの場合
			return -2;
		}

		boolean before = dBase.before(dDate); // dBaseがdDateよりも前かどうか？
		// イコール判定
		if (!before && !notEqual) {
			before = dBase.equals(dDate);
		}

		if (before) {
			return 0;
		} else {
			return 1;
		}
	}

	public static int compare_date(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				// System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				// System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static boolean isDate(String DATE1) {

		if (StringUtils.isEmpty(DATE1)) {
			return false;
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			df.parse(DATE1);

		} catch (Exception exception) {
			return false;
		}
		return true;
	}

	public static boolean isTimeLegal(String patternString) {

		Pattern a = Pattern.compile(
				"^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher b = a.matcher(patternString);
		if (b.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}
	
	/**
	 * @param str
	 *            判断key是否有中文
	 * @return
	 */
	public static boolean hasChineseChar(String value) {
		if (value == null) {
			return false;
		}
		boolean temp = false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(value);
		if (m.find()) {
			temp = true;
		}
		return temp;
	}

} // end of 'public class ValidateUtil {'
