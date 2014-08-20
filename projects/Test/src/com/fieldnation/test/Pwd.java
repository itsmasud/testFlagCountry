package com.fieldnation.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.fieldnation.utils.misc;

public class Pwd {
	private static SecureRandom rand = new SecureRandom();
	private static char[] chars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '=', '-', '[', ']', '\\', ';', '\'', ',', '.', '/', '!', '@', '#', '$', '%', '^', '&',
			'*', '(', ')', '_', '+', '{', '}', '|', ':', '"', '<', '>', '?', };

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println(genPwd());
		}
	}

	private static String genPwd() {
		String raw = "";

		for (int i = 0; i < 200; i++) {
			raw += rand.nextLong();
		}

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] data = digest.digest(raw.getBytes());

			String pwd = "";
			for (int i = 0; i < data.length; i++) {
				pwd += chars[Math.abs(data[i]) % chars.length];
			}

			return pwd;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
