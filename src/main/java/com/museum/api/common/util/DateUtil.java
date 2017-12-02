package com.museum.api.common.util;



import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * カレンダー情報や日付の計算を行うクラスです。<BR>
 * このクラスを使用するにはパッケージ：jp.co.toyotsu.core.master.calendar.dao内のファイルが必要になります。<BR>
 * またDaoを使用するため、springの設定、datasourceの設定、ibatisのsqlmapのclasspassの設定が必要となります。<BR>
 */
public class DateUtil {

	/** デフォルトの日付書式(yyyyMMdd) */
	public static final String DEFAULT_FORMAT = "yyyyMMdd";
	public static final String DEFAULT_FORMAT2 = "yyyyMMddHHmmss";

	/** スラッシュ付き日付書式(yyyy/MM/dd) */
	public static final String SLASH_FORMAT = "yyyy/MM/dd";

	/** デフォルトの年月書式(yyyyMM) */
	public static final String DEFAULT_YM_FORMAT = "yyyyMM";

	/** スラッシュ付き年月書式(yyyy/MM) */
	public static final String SLASH_YM_FORMAT = "yyyy/MM";

	/** DB2でのDate書式 */
	public static final String DB_DATE_FORMAT = "yyyy-MM-dd";

	/** DB2でのTimestamp書式 */
	public static final String DB_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	/** デフォルトの時間書式(HHmmss) */
	public static final String DEFAULT_TIME_FORMAT = "HHmmss";

	/** コロン付き時間書式(HH:mm:ss) */
	public static final String COLON_TIME_FORMAT = "HH:mm:ss";

	/** デフォルトの時分書式(HHmm) */
	public static final String DEFAULT_MINUTE_FORMAT = "HHmm";

	/** コロン付き時分書式(HH:mm) */
	public static final String COLON_MINUTE_FORMAT = "HH:mm";

	// デフォルトコンストラクタ
	private DateUtil() {
	}

	/**
	 * <p>
	 * 現在日時のタイムスタンプフォーマット文字列を返します
	 * </p>
	 *
	 * @return 現在日時のyyyy-MM-dd HH.mm.ss.SSSフォーマットのタイムスタンプ文字列
	 */
	public static Timestamp getCurrentDate() {

		SimpleDateFormat df = new SimpleDateFormat(DB_TIMESTAMP_FORMAT);
		String time = df.format(new Date());
		Timestamp ts = Timestamp.valueOf(time);
		return ts;
		// formatDate( new Date(), DB_TIMESTAMP_FORMAT );
	}

	/**
	 * 引数で渡された月の第１平日を返します。<BR>
	 * 平日はカレンダーマスタに登録されている情報から判断します。<BR>
	 * 対象のカレンダー情報がカレンダーマスタに無い場合はnullを返します。<BR>
	 *
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param holidayFlg
	 *            休日区分(0:トヨタ(ライン),1:ニューヨーク,2:トヨタ,3:トヨタ(営業),4:銀行,5:豊通)
	 * @return String 第１平日
	 * @deprecated
	 */
	public static String getFirstDay(String year, String month, int holidayFlg) {
		return (String) exec("getFirstDay", new Class[] { String.class, String.class, Integer.TYPE },
				new Object[] { year, month, holidayFlg });
	}

	// TODO 移行期用、削除予定
	private static Object exec(String name, Class<?>[] clss, Object[] objs) {
		try {
			Class<?> cls = Class.forName("jp.co.toyotsu.core.util.CmbDateUtil");
			Method mthd = cls.getMethod(name, clss);
			return mthd.invoke(null, objs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 引数で渡された月の最終平日を返します。<BR>
	 * 平日はカレンダーマスタに登録されている情報から判断します。<BR>
	 * 対象のカレンダー情報がカレンダーマスタに無い場合はnullを返します。<BR>
	 *
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param holidayFlg
	 *            休日区分(0:トヨタ(ライン),1:ニューヨーク,2:トヨタ,3:トヨタ(営業),4:銀行,5:豊通)
	 * @return Date 最終平日
	 * @deprecated
	 */
	public static String getLastDay(String year, String month, int holidayFlg) {
		return (String) exec("getLastDay", new Class[] { String.class, String.class, Integer.TYPE },
				new Object[] { year, month, holidayFlg });
	}

	/**
	 * 引数で渡された月の平日の日数を取得します。<BR>
	 * 平日はカレンダーマスタに登録されている情報から判断します。<BR>
	 * 対象のカレンダー情報がカレンダーマスタに無い場合は0を返します。<BR>
	 *
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param holidayFlg
	 *            休日区分(0:トヨタ(ライン),1:ニューヨーク,2:トヨタ,3:トヨタ(営業),4:銀行,5:豊通)
	 * @return int 対象月の平日数
	 * @deprecated
	 */
	public static int getWeekDayCount(String year, String month, int holidayFlg) {
		return (Integer) exec("getWeekDayCount", new Class[] { String.class, String.class, Integer.TYPE },
				new Object[] { year, month, holidayFlg });
	}

	/**
	 * 第一引数で渡された日付から第二引数の日数分、平日のみを数えて加算(減算)した日付を返します。<BR>
	 * 平日はカレンダーマスタに登録されている情報から判断します。<BR>
	 * 対象のカレンダー情報がカレンダーマスタに無い場合はnullを返します。<BR>
	 *
	 * @param date
	 *            基点となる日付(形式：yyyyMMdd)
	 * @param dist
	 *            日数（正数を指定すると加算、負数の値を指定すると減算します）
	 * @param holidayFlg
	 *            休日区分(0:トヨタ(ライン),1:ニューヨーク,2:トヨタ,3:トヨタ(営業),4:銀行,5:豊通)
	 * @return 基点日から加減算した日付
	 * @deprecated
	 */
	public static String rollDay(String date, int dist, int holidayFlg) {
		return (String) exec("rollDay", new Class[] { String.class, Integer.TYPE, Integer.TYPE },
				new Object[] { date, dist, holidayFlg });
	}

	/**
	 * 平日判定処理。<br>
	 * 指定された日付が平日であるかを検査する。
	 *
	 * @param date
	 *            基点となる日付(形式：yyyyMMdd)
	 * @param holidayFlg
	 *            休日区分(0:トヨタ(ライン),1:ニューヨーク,2:トヨタ,3:トヨタ(営業),4:銀行,5:豊通)
	 * @return 基点日から加減算した日付
	 * @deprecated
	 */
	public static boolean isWeekDay(String date, int holidayFlg) {
		return (Boolean) exec("isWeekDay", new Class[] { String.class, Integer.TYPE },
				new Object[] { date, holidayFlg });
	}

	/**
	 * 第一引数で渡された年月から第二引数の月数分、加算(減算)した年月を返します。<BR>
	 * 引数の年月に不備があった場合、nullを返します。<BR>
	 *
	 * @param yearMonth
	 *            基点となる年月(形式：yyyyMM)
	 * @param dist
	 *            月数（正数を指定すると加算、負数の値を指定すると減算します）
	 * @return String 基点日から加減算した年月
	 */
	public static String rollYearMonth(String yearMonth, int dist) {

		// 形式判定
		if ((yearMonth == null) || (yearMonth.length() != 6)) {
			return null;
		}

		final String format = "yyyyMM";
		final Date date = DateUtil.parse(yearMonth, format);
		if (date == null) {
			return null;
		}
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, dist);

		return DateUtil.formatDate(cal.getTime(), format);
	}

	/**
	 * 第一引数の月に第二引数の月数を加減算した値を返します。<BR>
	 * 足した結果が12を超えて13になった場合は01、14になった場合は02を返します。<BR>
	 * 引数に負数を指定し、足した結果が0になった場合は12、-1になった場合は11を返します。<BR>
	 * 引数の月が不正な場合、nullを返します。<BR>
	 *
	 * 有効な月の指定は"01"～"12"までの文字列です。
	 *
	 * @param strMonth
	 *            fromDate 基点となる月
	 * @param dist
	 *            月数（正数を指定すると加算、負数の値を指定すると減算します）
	 * @return String 基点日から加減算した月
	 */
	public static String rollMonth(String strMonth, int dist) {

		// 引数判定
		if ((strMonth == null) || (strMonth.trim().length() == 0) || (strMonth.trim().length() > 2)) {
			// nullまたは桁数が0または2桁より大きい場合
			return null;
		}

		final String format = "MM";
		final Date date = DateUtil.parse(strMonth, format);
		if (date == null) {
			return null;
		}
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.roll(Calendar.MONTH, dist);

		return DateUtil.formatDate(cal.getTime(), format);
	}

	/**
	 * 指定された年月日の妥当性チェックを行います。<BR>
	 * 月は1から12の範囲をチェックします。<BR>
	 * 日は1から指定された年月の月末日の範囲をチェックします。<BR>
	 *
	 * @param aYear
	 *            年
	 * @param aMonth
	 *            月
	 * @param aDay
	 *            日
	 * @return boolean true:正常、false:エラー
	 * @deprecated {@link ValidateUtil#chkDate(String, String, String)}
	 */
	public static boolean isValidDate(String aYear, String aMonth, String aDay) {
		return ValidateUtil.chkDate(aYear, aMonth, aDay);
	}

	/**
	 * 指定された年月日の妥当性チェックを行います。<BR>
	 * 有効な日付文字列フォーマットは[yyyyMMdd]または[yyyy/MM/dd]です。<BR>
	 * 月は1から12の範囲をチェックします。<BR>
	 * 日は1から指定された年月の月末日の範囲をチェックします。<BR>
	 *
	 * @param aTarget
	 *            検査対象日付
	 * @return boolean true:正常、false:エラー
	 * @deprecated {@link ValidateUtil#chkDate(String)}
	 */
	public static boolean isValidDate(String aTarget) {
		return ValidateUtil.chkDate(aTarget);
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
	 * @deprecated {@link ValidateUtil#checkDateOrder(String, String, boolean)}
	 */
	public static int checkDateOrder(String baseDate, String date, boolean notEqual) {

		return ValidateUtil.checkDateOrder(baseDate, date, notEqual);
	}

	/**
	 * Dateオブジェクトを生成します。<BR>
	 * 引数に不備があった場合nullを返します。<BR>
	 *
	 * @param y
	 *            年
	 * @param m
	 *            月
	 * @param d
	 *            日
	 * @return Dateオブジェクト
	 */
	public static Date getDate(String y, String m, String d) {
		final String dateValue = y + "/" + m + "/" + d;
		final String format = "y/M/d";
		return DateUtil.parse(dateValue, format);
	}

	/**
	 * Dateオブジェクトを生成します。<BR>
	 *
	 * @param y
	 *            年
	 * @param m
	 *            月
	 * @param d
	 *            日
	 * @return Dateオブジェクト
	 */
	public static Date getDate(int y, int m, int d) {

		final String dateValue = "" + y + "/" + m + "/" + d;
		final String format = "y/M/d";
		return DateUtil.parse(dateValue, format);
	}

	/**
	 * スラッシュ付の日付文字列、またはyyyyMMdd形式の文字列からDateオブジェクトを生成します。<BR>
	 * 引数の文字列が7桁か8桁でない場合やnullであった場合、nullを返します。<BR>
	 *
	 * @param date
	 *            yyyyMMdd形式またはyyyy/MM/dd形式の日付文字列
	 * @return Dateオブジェクト
	 */
	public static Date getDate(String date) {
		if (date == null) {
			return null;
		}
		if (date.indexOf("/") == -1) {
			return DateUtil.parse(date, DEFAULT_FORMAT);
		} else {
			return DateUtil.parse(date, SLASH_FORMAT);
		}
	}

	/**
	 * スラッシュ付の年月文字列、またはyyyyMM形式の文字列からDateオブジェクトを生成します。<BR>
	 * 引数の文字列が5桁か6桁でない場合やnullであった場合、nullを返します。<BR>
	 *
	 * @param date
	 *            yyyyMMdd形式またはyyyy/MM/dd形式の日付文字列
	 * @return Dateオブジェクト
	 */
	public static Date getYearMonth(String date) {
		if (date == null) {
			return null;
		}
		if (date.indexOf("/") == -1) {
			return DateUtil.parse(date, DEFAULT_YM_FORMAT);
		} else {
			return DateUtil.parse(date, SLASH_YM_FORMAT);
		}
	}

	/**
	 * スラッシュ付の日付文字列、またはyyyyMMdd形式の文字列から年、月、日の文字列に分けます。<BR>
	 * 引数の文字列が書式を満たさない場合やnullであった場合、nullを返します。<BR>
	 *
	 * @param date
	 *            yyyyMMdd形式またはyyyy/MM/dd形式の日付文字列
	 * @return String[] 年月日 要素[0]：年、要素[1]：月、要素[2]：日
	 */
	public static String[] divideDate(String date) {
		final Date dt = DateUtil.getDate(date);
		if (dt == null) {
			return null;
		}
		final String value = DateUtil.formatDate(dt, SLASH_FORMAT);
		return value.split("/");
	}

	/**
	 * Dateクラスから型：yyyyMMddの文字列へ型変換する。<BR>
	 *
	 *
	 * @param date
	 *            日付
	 * @return String yyyyMMddの文字列
	 */
	public static String formatDate(Date date) {
		// 既存のソースではSimpleDateFormatのインスタンスは
		// staticで保持していたが、競合が発生した場合を考慮
		// して毎回インスタンス化すべき
		if (date != null) {
			return formatDate(date, null);
		}
		return null;
	}

	/**
	 * Dateクラスから文字列へ型変換する。<BR>
	 *
	 * @param date
	 *            日付
	 * @return String yyyyMMddの文字列
	 */
	public static String formatDate(Date date, String format) {
		if (date == null) {
			return null;
		}
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		if (format != null) {
			simpleDateFormat.applyPattern(format);
		} else {
			simpleDateFormat.applyPattern(DEFAULT_FORMAT);
		}
		simpleDateFormat.setLenient(false); // 妥当な日付以外は許さない設定
		return simpleDateFormat.format(date);
	}

	/**
	 * 日付取得。<br>
	 * 日付文字列から日付を取得します。<br>
	 *
	 * @param dateValue
	 *            日付文字列
	 * @param format
	 *            日付書式。省略(nullに)した場合は、{@link #DEFAULT_FORMAT}が適用されます。
	 * @return 日付オブジェクト nullまたは妥当でない日付の場合はnullを返却します。
	 */
	public static Date parse(String dateValue, String format) {
		if (dateValue == null) {
			return null;
		}
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		if (format != null) {
			simpleDateFormat.applyPattern(format);
		} else {
			simpleDateFormat.applyPattern(DEFAULT_FORMAT);
		}

		simpleDateFormat.setLenient(false); // 妥当な日付以外は許さない設定
		ParsePosition pos = new ParsePosition(0);
		Date dt = simpleDateFormat.parse(dateValue, pos);
		if (pos.getIndex() == dateValue.length()) {
			return dt;
		} else {
			return null;
		}
	}

	/**
	 * 日付文字列の変換処理。<br>
	 * 日付文字列をDBのタイムスタンプ型に変換します。<br>
	 *
	 * @param dateValue
	 *            日付文字列
	 * @param srcFormat
	 *            日付文字列の書式
	 * @return 変換後の文字列 nullまたは妥当でない日付の場合は変換前の文字列を返却します。
	 */
	public static String convertDateToDbTimestamp(String dateValue, String srcFormat) {
		return convertDate(dateValue, srcFormat, DB_TIMESTAMP_FORMAT);
	}

	/**
	 * 日付文字列の変換処理。<br>
	 * 日付文字列をDBのDate型に変換します。<br>
	 *
	 * @param dateValue
	 *            日付文字列
	 * @param srcFormat
	 *            日付文字列の書式
	 * @return 変換後の文字列 nullまたは妥当でない日付の場合は変換前の文字列を返却します。
	 */
	public static String convertDateToDbDate(String dateValue, String srcFormat) {
		return convertDate(dateValue, srcFormat, DB_DATE_FORMAT);
	}

	/**
	 * 日付文字列の変換処理。<br>
	 * 日付文字列を別の書式に変換します。<br>
	 * マイクロ秒は変換元書式が".*SSSSSS$"の場合のみに対応し、入力エラーは考慮しません。
	 * 単純に値が"[\d]{6,6}$"のケースのみ対応し、それ以外は標準javaでの解析とします。
	 * （マイクロ秒が切られて、正常に変換できるケースがあったり、変換エラーになるケースがあります）
	 * また、変換後書式が".*SSSSSS$"の場合のみに対応し、マイクロ秒を追加します。
	 *
	 * @param dateValue
	 *            日付文字列
	 * @param srcFormat
	 *            日付文字列の書式。省略(nullに)した場合はロケール
	 * @param outFormat
	 *            変換後の書式 省略(nullに)した場合はsrcFormatが適用されます。
	 * @return 変換後の文字列 nullまたは妥当でない日付の場合は変換前の文字列を返却します。
	 */
	public static String convertDate(String dateValue, String srcFormat, String outFormat) {
		// 変換する必要の無い場合
		// 変換するものが無い、変換後の書式が同じ
		if (dateValue == null || outFormat == null || outFormat.equals(srcFormat)) {
			return dateValue;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		simpleDateFormat.setLenient(false);
		boolean existsMicroSec = false;
		String microSec = null;
		if (srcFormat != null) {
			// マイクロ秒対応
			existsMicroSec = srcFormat.endsWith("SSSSSS");
			if (existsMicroSec) {
				// .1234 のように3桁より多いとparseエラーになるので<数字6桁の場合のみ>後ろ3桁を切る
				if (dateValue.matches(".*[\\d]{6,6}$")) {
					microSec = dateValue.substring(dateValue.length() - 3);
					dateValue = dateValue.substring(0, dateValue.length() - 3);
				}
			}
			simpleDateFormat.applyPattern(srcFormat);
		}

		ParsePosition pos = new ParsePosition(0);
		Date date = simpleDateFormat.parse(dateValue, pos);
		if (date == null || dateValue.length() != pos.getIndex()) {
			return dateValue;
		}
		// マイクロ秒対応
		boolean existsMicroSecOut = outFormat.endsWith("SSSSSS");
		// Sはミリ病なので、Sの下3桁より前は0で埋められるため、余分な0は埋めないように
		// Sが6桁の場合(正確なフォーマットの場合)のみ3桁にして後でマイクロ秒を追加する
		if (existsMicroSecOut) {
			outFormat = outFormat.replace("SSSSSS", "SSS");
		}

		simpleDateFormat.applyPattern(outFormat);
		String value = simpleDateFormat.format(date);

		// マイクロ秒対応
		if (microSec != null && existsMicroSecOut) {
			value += microSec;
		}
		return value;
	}

	/**
	 * <p>
	 * 年追加メソッド
	 * </p>
	 * <p>
	 * 指定した日付文字列に年を追加します。戻り値は指定したフォーマットに整形されます。
	 * </p>
	 * <p>
	 * 基準日とフォーマットに不整合がある場合、または追加する年が妥当でない場合はNULLを返します。
	 * </p>
	 *
	 * @param date
	 *            日付文字列
	 * @param format
	 *            フォーマット
	 * @param year
	 *            加減算値(年)
	 * @return 日付文字列
	 */
	public static Date addYear(Date date, String format, int year) {
		return addDate(date, format, year, 1);
	}

	/**
	 * <p>
	 * 月追加メソッド
	 * </p>
	 * <p>
	 * 指定した日付文字列に月を追加します。戻り値は指定したフォーマットに整形されます。
	 * </p>
	 * <p>
	 * 基準日とフォーマットに不整合がある場合、または追加する月が妥当でない場合はNULLを返します。
	 * </p>
	 *
	 * @param date
	 *            日付文字列
	 * @param format
	 *            フォーマット
	 * @param month
	 *            加減算値(月)
	 * @return 日付文字列
	 */
	public static Date addMonth(Date date, String format, int month) {
		return addDate(date, format, month, 2);
	}

	/**
	 * <p>
	 * 日追加メソッド
	 * </p>
	 * <p>
	 * 指定した日付文字列に日を追加します。戻り値は指定したフォーマットに整形されます。
	 * </p>
	 * <p>
	 * 基準日とフォーマットに不整合がある場合、または追加する日が妥当でない場合はNULLを返します。
	 * </p>
	 *
	 * @param date
	 *            日付文字列
	 * @param format
	 *            フォーマット
	 * @param day
	 *            加減算値(日)
	 * @return 日付文字列
	 */
	public static Date addDay(Date date, String format, int day) {
		return addDate(date, format, day, 3);
	}

	// 年月日追加メソッド
	private static Date addDate(Date date, String strFormat, int number, int pattern) {

		// 解析
		if (date == null) {
			return null;
		}
		// カレンダーのインスタンスを作成する
		Calendar calendar = Calendar.getInstance();
		calendar.setLenient(false);
		// 日付文字列をカレンダー形に変換
		calendar.setTime(date);
		if (pattern == 1) {
			// 年の和算
			calendar.add(Calendar.YEAR, number);
		} else if (pattern == 2) {
			// 月の和算
			calendar.add(Calendar.MONTH, number);
		} else {
			// 日の和算
			calendar.add(Calendar.DAY_OF_MONTH, number);
		}
		// フォーマットパターンの適用
		date = new Date(calendar.getTimeInMillis());
		// フォーマットパターンの適用
		return date;
	}

	public static Date convertXmlDate(XMLGregorianCalendar date) {
		GregorianCalendar cal = date.toGregorianCalendar();
		return cal.getTime();
	}

	/** 计算年龄 */
	public static int getAge(Date birthDay) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			// throw new IllegalArgumentException(
			// "The birthDay is before Now.It's unbelievable!");
			return -1;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		}

		return age;
	}

	/** 计算年龄 */
	public static String getAgeYmd(Date birthDay, Date nowDate) {
		Calendar calNow = Calendar.getInstance();

		calNow.setTime(nowDate);
		int yearNow = calNow.get(Calendar.YEAR);
		int monthNow = calNow.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = calNow.get(Calendar.DAY_OF_MONTH);

		Calendar cal = Calendar.getInstance();
		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;
		String ymd = "";

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
					if (age != 0) {
						ymd = age + "岁";
					}
					Date date = DateUtil.addYear(birthDay, "", age);
					date = DateUtil.addMonth(date, "", 11);

					ymd += "11个月" + DateUtil.daysBetween(date.getTime(), nowDate.getTime()) + "天";
				} else if (dayOfMonthNow > dayOfMonthBirth) {
					ymd = age + "岁" + (dayOfMonthNow - dayOfMonthBirth) + "天";
				} else {
					ymd = age + "岁";
				}
			} else {
				// monthNow>monthBirth
				age--;
				if (age != 0) {
					ymd = age + "岁";
				}
				// 10.17---11.18
				if (dayOfMonthNow < dayOfMonthBirth) {
					int monthCnt = (12 - (monthBirth - monthNow) - 1);
					Date date = DateUtil.addYear(birthDay, "", age);
					date = DateUtil.addMonth(date, "", monthCnt);

					ymd += monthCnt + "个月" + DateUtil.daysBetween(date.getTime(), nowDate.getTime()) + "天";
				} else if (dayOfMonthNow > dayOfMonthBirth) {
					// 10.17---11.16
					ymd += (12 - (monthBirth - monthNow)) + "个月" + (dayOfMonthNow - dayOfMonthBirth) + "天";
				} else {
					// 10.17---11.17
					ymd += (12 - (monthBirth - monthNow)) + "个月";
				}
			}
		} else {
			if (age != 0) {
				ymd = age + "岁";
			}
			// 10.17---5.16
			if (dayOfMonthNow > dayOfMonthBirth) {
				ymd += (monthNow - monthBirth) + "个月" + (dayOfMonthNow - dayOfMonthBirth) + "天";
			} else if (dayOfMonthNow < dayOfMonthBirth) {
				// 10.17---5.18
				int monthCnt = (monthNow - monthBirth - 1);
				Date date = DateUtil.addYear(birthDay, "", age);
				date = DateUtil.addMonth(date, "", monthCnt);
				if (monthCnt > 0) {
					ymd += monthCnt + "个月" + DateUtil.daysBetween(date.getTime(), nowDate.getTime()) + "天";
				} else {
					ymd += DateUtil.daysBetween(date.getTime(), nowDate.getTime()) + "天";
				}

			} else {
				// 10.17---5.17
				ymd += (monthNow - monthBirth) + "个月";
			}
		}

		return ymd;
	}

	/**
	 * 当天的开始时间
	 * 
	 * @return
	 */
	public static Date startOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date = calendar.getTime();
		return date;
	}

	/**
	 * 当天的结束时间
	 * 
	 * @return
	 */
	public static Date endOfTodDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date date = calendar.getTime();
		return date;
	}

	public static int daysBetween(long time1, long time2) {

		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String str = sdf.format(date);
		return str;
	}
	/**
	 * 输入年和月可以的到 这个月的有少天
	 * 
	 * @param date:如 2017-04
	 * @return
	 */
	public static List<Date> getAllTheDateOftheMonth(String date)  {
		StringBuffer buffer = new StringBuffer(date);
		buffer.append("-01");
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd"); 
		Date d;
		try {
			d = sdf.parse(buffer.toString());
			List<Date> list = new ArrayList<Date>();
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.DATE, 1);
			int month = cal.get(Calendar.MONTH)+1;
			while ((cal.get(Calendar.MONTH)+1) == month) {
				list.add(cal.getTime());
				cal.add(Calendar.DATE, 1);
			}
			return list;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void main(String[] aa) {

		System.out.println(DateUtil.parse(DateUtil.convertDateToDbDate("19870928", DateUtil.DEFAULT_FORMAT),
				DateUtil.DB_DATE_FORMAT));
	}
}
