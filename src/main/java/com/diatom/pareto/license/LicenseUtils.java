package com.diatom.pareto.license;

import org.jasypt.util.text.BasicTextEncryptor;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class LicenseUtils {

	private static final String KEY = "2a0a083e10ba83c75a4c2181cf7eeef0b3255d8e";
	
	public static boolean isValidKey(File f) {
		try {
			String content = new Scanner(f).useDelimiter("\\Z").next();
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword(KEY);
			String myDecryptedText = textEncryptor.decrypt(content);
			if(myDecryptedText.startsWith("DIATOM-PARETO"))	{
				System.out.println(myDecryptedText);
				return true;
			}
			return false;
		}
		catch(Exception e) {
			System.out.println("Problem with license file. Please contact support@diatom.com for assistance.");
			return false;
		}
	}
	
	public static void createLicenseKey(String licenseText) {
		try {
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword(KEY);
			String myEncryptedText = textEncryptor.encrypt(licenseText);
			PrintWriter out = new PrintWriter(new FileWriter(new File("license.key")));
			out.println(myEncryptedText);
			out.flush();
			out.close();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to encrypt license.key");
		}
	}
	
}
