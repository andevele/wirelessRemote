package cn.feeyan.www.wirelessremote;

import android.util.SparseArray;

/**
 * 给定字符串，转化成16进制，再用字符串形式输出
 * 
 */
public class HexToReverse {
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	// 转化十六进制编码为字符串
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/*
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" –> byte[]{0x2B, 0×44, 0xEF,
	 * 0xD9}
	 */
	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < tmp.length / 2; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/*
	 * 合并单个字节，比如9和e，合并成9e
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	public static String printHexString(byte[] b) {
		// System.out.print(hint);
		String hexString = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString = hexString + hex;
			// System.out.print(hex.toUpperCase() + " ");
		}
		// System.out.println("");
		return hexString;

	}

	public static byte[] getHexByte(String beforeReversed) {
		String st = HexToReverse.toHexString(beforeReversed);
		byte[] finalBytes = HexToReverse.HexString2Bytes(st);
		return finalBytes;
	}

	/**
	 * 给定字符串，先转化成16进制形式，再对16进制取反，得到取反后的16进制字符串并返回
	 * 
	 * @param beforeReversed
	 * @return
	 */
	public static byte[] getReversedHex(String beforeReversed) {
		// 建立16进制字符数组，任何字符包括特殊字符的16进制Ascii码值都可用0~f组成
		char[] charHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		// Map<Integer, Character> hexMap = new HashMap<Integer, Character>();
		SparseArray<Character> hexSparseArray = new SparseArray<Character>();
		for (int i = 0; i < 16; i++) {
			hexSparseArray.put(i, charHex[i]);
		}
		// 给定字符串，得到其16进制字符串形式，比如给定字符串ab,其16进制字符串形式为6162
		String st = HexToReverse.toHexString(beforeReversed);
		// 用来保存取反后的字符16进制，比如，16进制6162取反后为9e9d
		char[] reversedChar = new char[st.length()];
		// 查询给定字符串的每个字符16进制，并保存到字符数组reversedChar中
		for (int i = 0; i < st.length(); i++) {
			for (int j = 0; j < 16; j++) {
				if (charHex[j] == st.charAt(i)) {
					/*
					 * System.out.println("--------charHex[" + j + "]: " +
					 * charHex[j] + " and reverse : " + hexSparseArray.get(15 -
					 * j));
					 */
					reversedChar[i] = hexSparseArray.get(15 - j);
				}
			}
		}
		// 把字符数组转化成字符串，即把取反后的16进制字符数组转化成字符串形式
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < reversedChar.length; i++) {
			sb.append(reversedChar[i]);
		}
		// System.out.println("--------reversedString is: " + sb.toString());

		// 把16进制字符串分割成16进制形式，存放在byte数组中
		byte[] finalBytes = HexToReverse.HexString2Bytes(sb.toString());
		return finalBytes;
	}

	// 给定取反后的16进制字符串，转换成取反前的16进制
	public static String getHexFromReversedHex(String reversedHex) {
		char[] charHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		SparseArray<Character> hexSparseArray = new SparseArray<Character>();
		for (int i = 0; i < 16; i++) {
			hexSparseArray.put(i, charHex[i]);
		}

		String st = reversedHex;
		char[] reversedChar = new char[st.length()];
		for (int i = 0; i < st.length(); i++) {
			for (int j = 0; j < 16; j++) {
				if (charHex[j] == st.charAt(i)) {
					/*
					 * System.out.println("--------charHex[" + j + "]: " +
					 * charHex[j] + " and reverse : " + hexSparseArray.get(15 -
					 * j));
					 */
					reversedChar[i] = hexSparseArray.get(15 - j);
				}
			}
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < reversedChar.length; i++) {
			sb.append(reversedChar[i]);
		}
		return toStringHex(sb.toString());
	}

}
