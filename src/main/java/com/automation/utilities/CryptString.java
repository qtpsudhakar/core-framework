package com.automation.utilities;

import java.io.File;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.JOptionPane;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

/**
 * <h1>CryptString</h1> //
 *
 *  * @version 1.0
 */
public class CryptString {
	static Cipher ecipher;
	static Cipher dcipher;
	static byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3,
			(byte) 0x03 };
	public static HashMap<String, String> appConfig = new HashMap<String, String>();
	public static final String fp = File.separator;
	public static final String runDir = System.getProperty("user.dir");
	public static final String APP_CONFIG_PATH = "resources" + fp + "Config" + fp + "Config.xlsx";
	public static final String CURRENT_DIR = System.getProperty("user.dir");
	public static String sql_appConfig = "SELECT config_Category,config_Name,config_Value from appConfig where configName is not null";
	public static final String DEVICEDETAILS = runDir + fp + "src" + fp + "test" + fp + "resources" + fp + "config" + fp
			+ "Config.xlsx";
	static {
		try {
			File confFile = new File(APP_CONFIG_PATH);
			if (confFile.exists())
				appConfig = read_Xl_prop(runDir + fp + APP_CONFIG_PATH, sql_appConfig);
			else {
				appConfig = read_Xl_prop(runDir + fp + "src" + fp + "test" + fp + APP_CONFIG_PATH, sql_appConfig);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	final static String secretKey = appConfig.get("SecretKey");
	static int iterationCount = 19;

	public static String encrypt(String plainText) {
		String encStr = "";
		try {
			KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
			ecipher = Cipher.getInstance(key.getAlgorithm());
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			String charSet = "UTF-8";
			byte[] in = plainText.getBytes(charSet);
			byte[] out = ecipher.doFinal(in);
			encStr = new String(Base64.getEncoder().encode(out));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encStr;
	}

	public static String decrypt(String encryptedText) {
		String plainStr = "";
		try {
			KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
			dcipher = Cipher.getInstance(key.getAlgorithm());
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			byte[] enc = Base64.getDecoder().decode(encryptedText);
			byte[] utf8 = dcipher.doFinal(enc);
			String charSet = "UTF-8";
			plainStr = new String(utf8, charSet);
		} catch (Exception e) {
			System.err.println("Improper text to decrypt ---> " + encryptedText);
			e.printStackTrace();
		}
		return plainStr;
	}

	public static HashMap<String, String> read_Xl_prop(String filePath, String sqlQuery) {
		Fillo fillo = new Fillo();
		HashMap<String, String> appConfig = new HashMap<>();
		try {
			Connection conn = fillo.getConnection(filePath);
			Recordset rs = conn.executeQuery(sql_appConfig);
			while (rs.next())
				appConfig.put(rs.getField("config_Name"), rs.getField("config_Value"));
			rs.close();
			conn.close();

		} catch (FilloException e) {
			e.printStackTrace();
		}
		return appConfig;
	}
}