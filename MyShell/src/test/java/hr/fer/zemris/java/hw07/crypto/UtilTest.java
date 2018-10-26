package hr.fer.zemris.java.hw07.crypto;

import static org.junit.Assert.*;

import static hr.fer.zemris.java.hw07.crypto.Util.hextobyte;
import static hr.fer.zemris.java.hw07.crypto.Util.bytetohex;

import org.junit.Test;

public class UtilTest {

	@Test
	public void hextobyteEmptyString() {
		assertArrayEquals(new byte[0], hextobyte(""));
	}

	@Test
	public void bytetohexEmptyByte() {
		assertEquals("", bytetohex(new byte[0]));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void hextobyteOddLengthString() {
		hextobyte("a");
	}
	
	@Test
	public void hextobyteAllBytes() {
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
				byte[] actual = hextobyte(String.format("%c%c", i+'0', j+'0'));
				byte[] expected = new byte[] {(byte)((i << 4)+j)};
				assertArrayEquals(String.format("%c%c",  i+'0', j+'0') + ":" + actual[0], expected, actual);
			}
		}
		
		for (int i = 'a'; i <= 'f'; ++i) {
			for (int j = 0; j < 10; ++j) {
				byte[] actual = hextobyte(String.format("%c%c", i, j+'0'));
				byte[] expected = new byte[] {(byte)((i-'a'+10)*16+j)};
				assertArrayEquals(String.format("%c%c",  i, j+'0') + ":" + actual[0], expected, actual);
			}
		}
		
		for (int i = 'a'; i <= 'f'; ++i) {
			for (int j = 'a'; j <= 'f'; ++j) {
				byte[] actual = hextobyte(String.format("%c%c", i, j));
				byte[] expected = new byte[] {(byte)((i-'a'+10)*16+j-'a'+10)};
				assertArrayEquals(String.format("%c%c",  i, j) + ":" + actual[0], expected, actual);
			}
		}
	}
	
	@Test
	public void hextobyteAllBytesUpperCase() {
		for (int i = 'A'; i <= 'F'; ++i) {
			for (int j = 0; j < 10; ++j) {
				byte[] actual = hextobyte(String.format("%c%c", i, j+'0'));
				byte[] expected = new byte[] {(byte)((i-'A'+10)*16+j)};
				assertArrayEquals(String.format("%c%c",  i, j+'0') + ":" + actual[0], expected, actual);
			}
		}
		
		for (int i = 'A'; i <= 'F'; ++i) {
			for (int j = 'A'; j <= 'F'; ++j) {
				byte[] actual = hextobyte(String.format("%c%c", i, j));
				byte[] expected = new byte[] {(byte)((i-'A'+10)*16+j-'A'+10)};
				assertArrayEquals(String.format("%c%c",  i, j) + ":" + actual[0], expected, actual);
			}
		}
	}
	
	@Test
	public void bytetohexAllHex() {
		char[] tohex = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'a', 'b', 'c', 'd', 'e', 'f'};
		
		for (byte i = -128; i <= 127; ++i) {
			String expected = String.format("%c%c", tohex[(i >> 4) & 15], tohex[i & 15]);
			String actual = bytetohex(new byte[] {i});
			assertEquals(expected, actual);
			
			if (i == 127) break;
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void hextobyteInvalidFirstChar() {
		hextobyte("g0");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void hextobyteInvalidSecondChar() {
		hextobyte("0g");
	}
	
	@Test
	public void testConsistency() {
		byte[] bytes = {-1, 56, 42, 77, -128, 127, 0, 2, 4, 127, 67, 56, -128};
		String hex = new String("ff382a4d807f0002047f433880");
		assertEquals(hex, bytetohex(bytes));
		assertArrayEquals(bytes, hextobyte(bytetohex(bytes)));
		assertEquals(hex, bytetohex(hextobyte(hex)));
	}
	
	@Test
	public void hextobyteTestExample() {
		assertArrayEquals(new byte[] {1, -82, 34}, hextobyte("01aE22"));
	}
	
	@Test
	public void bytetohexTestExample() {
		assertEquals("01ae22", bytetohex(new byte[] {1, -82, 34}));
	}
}











