package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.ByteBuffer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/*
 * Các hàm mã hóa AES với CBC mode, CTR mode tự viết
 * Có sử dụng hàm mã hóa AES (ECB mode) trong thư viện javax.crypto
 */
public class Encrypt {
	
	// Hàm mã hóa AES, sử dụng thư viện javax.crypo
	public static byte[] encryptAES(byte[] msg, byte[] key) throws Exception {
		
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		
		byte[] encrypted = cipher.doFinal(msg);
		return encrypted;
	}
	
	
	/*
	 * Mã hóa AES với CBC mode
	 * Key 16-byte
	 * IV 16-byte
	 * Dùng PKCS#7 Padding
	 */
	public static void encryptAES_CBC(String x, byte[] key, byte[] iv) throws Exception {
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("output_cbc_mode.txt"));

		// padding
		byte[] paddingMsg = Utils.pkcs7_padding(x, 16);
		// chia bản rõ thành các khối 16-byte
		byte[][] block = Utils.splitBlock(paddingMsg, 16);

		byte[] IV = iv;
		// lưu vào file
		writer.write(Utils.bytesToHex(IV));
		
		for (int i = 0; i < block.length; ++i) {
			byte[] y = encryptAES(Utils.xor(IV, block[i]), key);
			writer.write(Utils.bytesToHex(y));
			IV = y;
		}
		
		writer.close();
	}
	
	
	/*
	 * Mã hóa AES với Counter mode (CTR)
	 * nonce 8-byte kết hợp với ctr 8-byte => nonce || ctr là 16-byte
	 * ctr bắt đầu từ 0, tăng dần
	 */
	public static void encryptAES_CTR(String x, byte[] key, byte[] nonce) throws Exception {
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("output_ctr_mode.txt"));

		byte[] msg = x.getBytes("UTF-8");
		byte[][] block = Utils.splitBlock(msg, 16);
		
		byte[] nonce_ctr = new byte[16];
		System.arraycopy(nonce, 0, nonce_ctr, 0, 8);
		byte[] ctr = ByteBuffer.allocate(8).putInt(0).array();
		System.arraycopy(ctr, 0, nonce_ctr, 8, 8);
		writer.write(Utils.bytesToHex(nonce_ctr));
		
		int i = 0;
		for (i = 0; i < block.length; ++i) {
			ctr = ByteBuffer.allocate(8).putInt(i).array();
			System.arraycopy(ctr, 0, nonce_ctr, 8, 8);
			byte[] tmp = encryptAES(nonce_ctr, key);
			byte[] y = Utils.xor(tmp, block[i]);
			writer.write(Utils.bytesToHex(y));
		}
		
		int lenLastBlock = msg.length % 16;
		if(lenLastBlock !=0 ) {
			byte[] lastBlock = new byte[lenLastBlock];
			System.arraycopy(msg, msg.length - lenLastBlock, lastBlock, 0, lenLastBlock);
			
			ctr = ByteBuffer.allocate(8).putInt(i).array();
			System.arraycopy(ctr, 0, nonce_ctr, 8, 8);
			byte[] tmp = encryptAES(nonce_ctr, key);
			byte[] y = Utils.xor(tmp, lastBlock);
			writer.write(Utils.bytesToHex(y));
		}
		
		writer.close();
	}

}
