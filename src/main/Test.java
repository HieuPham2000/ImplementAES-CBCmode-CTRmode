package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.ByteBuffer;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * Các hàm mã hóa sử dụng thư viện
 * Dùng kết quả để đối chiếu với code mình tự viết
 */
public class Test {
	
	/*
	 * Mã hóa AES với CBC mode
	 * Sử dụng CBC mode sẵn có của thư viện javax.crypto
	 * Dùng để đối chiếu kết quả với CBC mode do mình tự cài đặt
	 * Bản mã lưu vào file "output_cbc_test.txt"
	 */
	public static void encrypt_CBC_test(String plainText, byte[] keyBytes, byte[] iv) throws Exception {
		byte[] clean = plainText.getBytes();
//		int ivSize = 16;
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] encrypted = cipher.doFinal(clean);

		
//		byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
//		System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
//		System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);

		BufferedWriter writer = new BufferedWriter(new FileWriter("output_cbc_test.txt"));
		writer.write(Utils.bytesToHex(iv));
		writer.write(Utils.bytesToHex(encrypted));
		writer.close();
	}
	
	
	/*
	 * Mã hóa AES với CTR mode
	 * Sử dụng CTR mode sẵn có của thư viện javax.crypto
	 * Dùng để đối chiếu kết quả với CTR mode do mình tự cài đặt
	 * Bản mã lưu vào file "output_ctr_test.txt"
	 */
	public static void encrypt_CTR_test(String plainText, byte[] keyBytes, byte[] iv) throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter("output_ctr_test.txt"));
		byte[] clean = plainText.getBytes();
		
		byte[] nonce_ctr = new byte[16];
		System.arraycopy(iv, 0, nonce_ctr, 0, 8);
		byte[] ctr = ByteBuffer.allocate(8).putInt(0).array();
		System.arraycopy(ctr, 0, nonce_ctr, 8, 8);
		writer.write(Utils.bytesToHex(nonce_ctr));
		
		IvParameterSpec ivParameterSpec = new IvParameterSpec(nonce_ctr);
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

		
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] encrypted = cipher.doFinal(clean);

		
		writer.write(Utils.bytesToHex(encrypted));
		writer.close();
	}
}
