package jenml2html;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digest {

	private static final String SHA1 = "sha-1";
	private static final String MD5 = "md5";

	private static String digest(InputStream input, String algorithm)
			throws IOException {
		byte[] hash = digestRaw(input, algorithm);
		StringBuilder builder = new StringBuilder();
		for (byte b : hash) {
			int v = b & 0xff;
			if (v < 16) {
				builder.append(0);
			}
			builder.append(Integer.toHexString(v));
		}
		return builder.toString().toUpperCase();
	}

	private static byte[] digestRaw(InputStream input, String algorithm)
			throws IOException {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			// sha1和md5都是每512位为一组进行处理的，因此这里使用512
			byte[] bytes = new byte[512];
			int len;
			while ((len = input.read(bytes)) >= 0) {
				if (len > 0) {
					digest.update(bytes, 0, len);
				}
			}
			return digest.digest();

		} catch (NoSuchAlgorithmException e) {
			// Never happen
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] md5Raw(InputStream input) throws IOException {
		return digestRaw(input, MD5);
	}

	public static byte[] sha1Raw(InputStream input) throws IOException {
		return digestRaw(input, SHA1);
	}

	public static String md5(InputStream input) throws IOException {
		return digest(input, MD5);
	}

	public static String sha1(InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	private static byte[] digestRaw(byte[] bytes, int len, String algorithm) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			digest.update(bytes, 0, len);
			return digest.digest();
		} catch (NoSuchAlgorithmException e) {
			// Never happen
			e.printStackTrace();
		}
		return null;
	}

	private static String digest(byte[] bytes, int len, String algorithm) {
		byte[] hash = digestRaw(bytes, len, algorithm);
		StringBuilder builder = new StringBuilder();
		for (byte b : hash) {
			int v = b & 0xff;
			if (v < 16) {
				builder.append(0);
			}
			builder.append(Integer.toHexString(v));
		}
		return builder.toString().toUpperCase();
	}

	public static byte[] md5Raw(byte[] bytes) {
		return digestRaw(bytes, bytes.length, MD5);
	}

	public static byte[] sha1Raw(byte[] bytes) {
		return digestRaw(bytes, bytes.length, SHA1);
	}

	public static byte[] md5Raw(byte[] bytes, int len) {
		return digestRaw(bytes, len, MD5);
	}

	public static byte[] sha1Raw(byte[] bytes, int len) {
		return digestRaw(bytes, len, SHA1);
	}

	public static String md5(byte[] bytes) {
		return digest(bytes, bytes.length, MD5);
	}

	public static String sha1(byte[] bytes) {
		return digest(bytes, bytes.length, SHA1);
	}

	public static String md5(byte[] bytes, int len) {
		return digest(bytes, len, MD5);
	}

	public static String sha1(byte[] bytes, int len) {
		return digest(bytes, len, SHA1);
	}

	public static long md5Long(byte[] bytes, int len) {
		byte[] hash = md5Raw(bytes, len);
		// Big Endian
		long re = 0;
		for (int i = 0; i < 8; i++) {
			re = (re << 8) + hash[i];
		}
		return re;
	}

	public static long md5Long(InputStream input) throws IOException {
		byte[] hash = md5Raw(input);
		// Big Endian
		long re = 0;
		for (int i = 0; i < 8; i++) {
			re = (re << 8) + hash[i];
		}
		return re;
	}

}