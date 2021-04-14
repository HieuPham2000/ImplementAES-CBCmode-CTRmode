package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/*
 * Lớp Utils cung cấp 1 số method tiện ích
 */
public class Utils {
	private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
	/*
	 * Chuyển mảng byte[] thành String hexa
	 */
	public static String bytesToHex(byte[] bytes) {
		byte[] hexChars = new byte[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars, StandardCharsets.UTF_8);
	}

	/*
	 * PKCS#7 Padding trong CBC mode
	 */
	public static byte[] pkcs7_padding(String plainText, int blockSize) throws Exception {
		byte[] plainTextBytes = plainText.getBytes("UTF-8");
		int oldLen = plainTextBytes.length;
		int paddingLength = blockSize - (oldLen % blockSize);
		if (paddingLength == 0) {
			paddingLength = blockSize;
		}

		byte[] res = new byte[oldLen + paddingLength];
		System.arraycopy(plainTextBytes, 0, res, 0, oldLen);
		for (int i = oldLen; i < oldLen + paddingLength; ++i) {
			res[i] = (byte) paddingLength;
		}

		return res;
	}

	/*
	 * Chia bản rõ thành các khối theo blockSize
	 */
	public static byte[][] splitBlock(byte[] msg, int blockSize) {
		int num = msg.length / blockSize;
		
		byte[][] res = new byte[num][blockSize];
		for (int i = 0; i < num; ++i) {
			System.arraycopy(msg, i * blockSize, res[i], 0, blockSize);
		}
		
		return res;
	}

	/*
	 * Xor giữa 2 khối
	 */
	public static byte[] xor(byte[] x, byte[] y) {
		int len = Math.min(x.length, y.length);
		byte[] res = new byte[len];
		for (int i = 0; i < len; ++i) {
			res[i] = (byte) (x[i] ^ y[i]);
		}
		return res;
	}

	
	/*
	 * Sinh mảng byte[] ngẫu nhiên
	 */
	public static byte[] generateRandomBytes(int size) {
		byte[] res = new byte[size];
		SecureRandom random = new SecureRandom();
		random.nextBytes(res);
		return res;
	}
	
	/*
	 * Đọc khóa k từ file "key.txt"
	 */
	public static byte[] getKey() throws IOException {
		File file = new File("key.txt");

		if (file.length() == 0) {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] key = generateRandomBytes(16);
			fos.write(key);
			fos.close();
			return key;
		}
		FileInputStream input = new FileInputStream(file);
		byte[] key = input.readAllBytes();
		input.close();
		return key;

	}
}
