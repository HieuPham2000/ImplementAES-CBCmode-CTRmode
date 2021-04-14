package main;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
	public static void main(String[] args) throws Exception {
		
		// Lấy khóa từ file/ hoặc tạo mới và lưu vào file nếu chưa có (khóa 16-byte)
		byte[] key = Utils.getKey();
//		System.out.println(Utils.bytesToHex(key));
		
		
		
		
		// Đọc bản rõ vào từ file "sample.txt"
//		String clean = "hello";
		BufferedReader reader = new BufferedReader(new FileReader("sample.txt"));
		String clean = reader.readLine();
		reader.close();
		System.out.println("Plain text: " + clean.length() + " bytes");
		
		
		
		// CBC mode
		long start = System.currentTimeMillis();
		
		// Sinh iv ngẫu nhiên (16-byte)
		byte[] iv16 = Utils.generateRandomBytes(16);
		
//		Test.encrypt_CBC_test(clean, key, iv16);
		Encrypt.encryptAES_CBC(clean, key, iv16);
		
		long end = System.currentTimeMillis();
		System.out.println("CBC mode: " + (end - start) + " ms");
		
		
		
		// CTR mode
		start = System.currentTimeMillis();
		
		// Sinh nonce ngẫu nhiên (8-byte)
		byte[] iv8 = Utils.generateRandomBytes(8);
		
		Encrypt.encryptAES_CTR(clean, key, iv8);
//		Test.encrypt_CTR_test(clean, key, iv8);
		end = System.currentTimeMillis();
		System.out.println("CTR mode: " + (end - start) + " ms");
		
		
		System.out.println("Done!");
	}
}
