package hr.fer.zemris.java.hw07.crypto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class implements helper methods that convert string of hexadecimal values
 * to bytes and vice versa.
 * @author ltomic
 *
 */
public class Util {
	
	/**
	 * Mapping from hexadecimal to group of 4 bits
	 */
	private static Map<Character, Integer> charToHalfByte = new HashMap<>();
	/**
	 * Mapping from group of 4 bits to hexadecimal
	 */
	private static Map<Integer, Character> halfByteToChar = new HashMap<>();
	
	private static int halfByteSize = 4;
	
	static {
		for (int i = 0; i < 10; ++i) {
			Character c = Character.valueOf((char)(i+'0'));
			charToHalfByte.put(c, i);
			halfByteToChar.put(i, c);
		}
		
		for (char i = 'a'; i <= 'f'; ++i) {
			charToHalfByte.put(i, i-'a'+10);
			halfByteToChar.put(i-'a'+10, i);
		}
	}

	/**
	 * Converts a string of hexadecimal values to an array of bytes.
	 * @param keyText hexadecimal value to be converted
	 * @return array of bytes converted from hexadecimal values
	 */
	public static byte[] hextobyte(String keyText) {
		Objects.requireNonNull(keyText, "keyText cannot be null");
		
		int size = keyText.length();
		if (size % 2 != 0) {
			throw new IllegalArgumentException("Provided string should be even-sized. " +
					"Given : " + keyText);
		}
		
		byte[] converted = new byte[size/2];
		keyText = keyText.toLowerCase();
		
		for (int i = 0; i < size; i += 2) {
			converted[i/2] = twoCharactersToByte(keyText.charAt(i), keyText.charAt(i+1));
		}
		
		return converted;
	}
	
	/**
	 * Method that converts two hexadecimal values to a single byte
	 * @param first first hexadecimal value
	 * @param second second hexadecimal value
	 * @return byte that represents two hexadecimal value
	 */
	private static byte twoCharactersToByte(Character first, Character second) {
		if (!charToHalfByte.containsKey(first) || !charToHalfByte.containsKey(second)) {
			throw new IllegalArgumentException(
					"Given characters " + first + second + "are not hex-encoded");
		}
		
		return (byte)((charToHalfByte.get(first) << halfByteSize) + charToHalfByte.get(second));
	}
	
	/**
	 * Method that converts array of bytes to a string of hexadecimal values
	 * @param bytearray array of bytes to be converted
	 * @return string of hexadecimal values converted from given array of bytes
	 */
	public static String bytetohex(byte[] bytearray) {
		Objects.requireNonNull(bytearray, "bytearray cannot be null");
		
		StringBuilder converted = new StringBuilder();
		
		for (int i = 0; i < bytearray.length; ++i) {
			converted.append(halfByteToChar.get((bytearray[i] >> halfByteSize) & 15));
			converted.append(halfByteToChar.get(bytearray[i] & 15));
		}
		
		return converted.toString();
	}
}
