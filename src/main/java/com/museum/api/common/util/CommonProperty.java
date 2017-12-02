package com.museum.api.common.util;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 *
 * @author JLD
 *
 * @version 1.0
 */

public final class CommonProperty {

	private static Logger log = Logger.getLogger(CommonProperty.class);
	/**
	 * 自インスタンス。
	 */
	private static CommonProperty inst = null;

	/**
	 * 起動オプションのキー。
	 */
	private static final String SYSTEMPROP_CONFIGFILE = "config.configfile";

	/**
	 * CLASSPATHから取得可能なファイル名称。
	 */
	private static final String DEFAULT_CONFIGFILE = "common.properties";

	/**
	 * システム共通プロパティファイルから業務プロパティファイルを呼び出す プロパティのプレフィックス。
	 */
	private static final String PREFIX_APP_CONFIG = "config.filename.";

	/**
	 * システム変数のプロパティ。
	 */
	private Properties properties;

	/**
	 * デフォルトコンストラクタ。
	 *
	 * @exception IOException
	 *                configファイル読み込み時のI/O例外
	 */
	private CommonProperty() throws IOException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		if (cl == null) {
			cl = getClass().getClassLoader();
		}
		// 初期処理
		Properties props = new Properties();
		setProperties(props);
		String filename = System.getProperty(SYSTEMPROP_CONFIGFILE);
		// configファイルの読み込み。
		InputStream is;
		if (filename == null) {
			// CLASSPATHに存在するファイルから読み込む。
			is = cl.getResourceAsStream(DEFAULT_CONFIGFILE);
		} else {
			// VM起動時のオプションによってローカルファイルから読み込む。
			is = openStream(filename);
		}
		if (is == null) {
			throw new IOException(filename + " not exists.");
		}
		// configファイルからプロパティオブジェクトに展開する。
		try {
			props.load(is);
		} finally {
			if (is != null) {
				is.close();
			}
		}
		// メインのconfigから業務単位のconfigを呼び出す。
		Enumeration<?> enm = props.propertyNames();
		while (enm.hasMoreElements()) {
			Object obj = enm.nextElement();
			if (obj == null || !(obj instanceof String)) {
				continue;
			}
			String key = (String) obj;
			// キーの先頭に"config.filename."がついているものを読み込む。
			if (key.startsWith(CommonProperty.PREFIX_APP_CONFIG)) {
				String val = props.getProperty(key);
				Properties temp = new Properties();
				InputStream iss = cl.getResourceAsStream(val);
				if (iss == null) {
					iss = openStream(val);
				}
				if (iss == null) {
					log.error(val + "は読み込まれませんでした。");
					continue;
				}
				try {
					temp.load(iss);
				} finally {
					iss.close();
				}
				int before = props.size();
				int add = temp.size();
				props.putAll(temp);
				int after = props.size();
				// キーが重複していてもエラーにしない
				if (before + add != after) {
					log.info(val + "で重複するキーが" + (before + add - after)
							+ "個存在しました。");
				}
			}
		}
	}

	/**
	 * システム変数群を取得する。 private なのは、追加・変更・削除されたくないため。
	 * Config.getProperty()内でのみ使用し、プロパティはキーを指定して 取得するのみとなる。
	 *
	 * @return システム変数のプロパティ。
	 */
	private Properties getProperties() {
		return this.properties;
	}

	/**
	 * システム変数群を設定する。 private なのは、追加・変更・削除されたくないため。
	 *
	 * @param properties
	 *            システム変数のプロパティ。
	 */
	private void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * インスタンス取得。
	 *
	 * @return Configクラスのインスタンス
	 * @exception IOException
	 *                configファイル読み込み時のI/O例外
	 */
	private static synchronized CommonProperty getInstance() throws IOException {
		if (inst == null) {
			inst = new CommonProperty();
		}
		return inst;
	}

	/**
	 * ローカルファイルからInputStreamをopenする。
	 *
	 * @param localFilePath
	 *            ローカルファイルのパス
	 * @return ローカルファイルの入力ストリーム
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws IOException
	 *             パスから読み込む際のI/O例外
	 */
	private static InputStream openStream(String localFilePath) {
		File file = new File(localFilePath);
		if (!file.exists()) {
			log.debug(file.getAbsolutePath() + " is not found.");
		}
		try {
			return file.toURI().toURL().openConnection().getInputStream();
		} catch (IOException e) {
			log.debug(file.getAbsolutePath() + " can not read.");
			return null;
		}
	}

	/**
	 * システムで標準とするエンコードを戻す。<br>
	 * 適用順位
	 * <ol = type1>
	 * <li>Configファイル※ (key=user.encoding) で指定したエンコード<br>
	 * ※System.property (key=config.configfile)で取得したプロパティファイル(VM起動時のパラメータ)が<br>
	 * 無い場合はCLASSPTHに存在する"config.properties"ファイル
	 * <li>System.property (key=user.encoding) で指定したエンコード
	 * <li>System.property (key=file.encoding) で指定したエンコード
	 * </ol>
	 *
	 * @return String エンコード
	 */
	public static String getEncode() {
		String encode = CommonProperty.getProperty("user.encoding");
		if (encode == null) {
			encode = System.getProperty("user.encoding");
			if (encode == null) {
				encode = System.getProperty("file.encoding");
			}
		}
		return encode;
	}

	/**
	 * システムで代表使用するプロパティオブジェクトの値を取得する。
	 *
	 * @param key
	 *            キー
	 * @return String value(値)
	 */
	public static String getProperty(String key) {
		CommonProperty config = null;
		try {
			config = CommonProperty.getInstance();
		} catch (IOException e) {
			// 読み込みに失敗してもエラーにしない。
			log.warn("getProperty(\"" + key + "\")", e);
			return null;
		}
		return config.getProperties().getProperty(key);
	}

	/**
	 * システムで代表使用するプロパティオブジェクトの値を取得する。
	 *
	 * @param key
	 *            キー
	 * @param defaultValue
	 *            キーが存在しないときのデフォルト値
	 * @return String value(値)
	 */
	public static String getProperty(String key, String defaultValue) {
		CommonProperty config = null;
		try {
			config = CommonProperty.getInstance();
		} catch (IOException e) {
			// 読み込みに失敗してもエラーにしない。
			log.warn("getProperty(\"" + key + "\",\"" + defaultValue + "\")", e);
			return null;
		}
		return config.getProperties().getProperty(key, defaultValue);
	}

	/**
	 * 初期化処理。 特に初期化処理を行う必要はなく、getPropertyを呼んだ際に初期化されて<BR>
	 * いなければ初期化を行う。そのため、再度初期化を行う場合などに使用する。<br>
	 * 新規にConfigのインスタンスを作成する。
	 *
	 * @exception IOException
	 *                configファイル読み込み時のI/O例外
	 */
	public static synchronized void init() throws IOException {
		inst = new CommonProperty();
	}

}