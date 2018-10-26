package hr.fer.zemris.java.hw07.crypto;

import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This program allows users to encrypt/decrypt given file using the AES crypto-algorithm
 * and the 128-bit encryption key or calculate and check the SHA-256 file digest.
 * @author ltomic
 *
 */
public class Crypto {

	/**
	 * Name of the command used to check SHA-256 digest
	 */
	private final static String CHECKSHA_COMMAND = "checksha";
	/**
	 * Name of the command used to encrypt a file
	 */
	private final static String ENCRYPT_COMMAND = "encrypt";
	/**
	 * Name of the command used to decrypt a file
	 */
	private final static String DECRYPT_COMMAND = "decrypt";
	/**
	 * List of command names
	 */
	private final static String[] commands = {CHECKSHA_COMMAND, DECRYPT_COMMAND, ENCRYPT_COMMAND};
	
	private final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	
	/**
	 * Digest algorithm used for checking file digest
	 */
	private final static String DIGEST_ALGORITHM = "SHA-256";
	
	/**
	 * Buffer size used when reading/writing from/to file.
	 */
	private final static int DEFAULT_CLUSTER_SIZE = 4096;
	
	private static final Scanner IN = new Scanner(System.in);
	
	/**
	 * Method called at the beginning of the program. As arguments this method
	 * receives one of the commands and arguments for that command.
	 * @param args command and its arguments
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
		if (args.length < 1) {
			System.out.println("Invalid arguments");
			return;
		}
		
		args[0] = args[0].trim();
		try {
			switch (args[0]) {
			case CHECKSHA_COMMAND:
				checkSHA(args);
				break;
			case ENCRYPT_COMMAND:
				encrypt(args);
				break;
			case DECRYPT_COMMAND:
				decrypt(args);
				break;
			default:
				System.out.println("Invalid command");
			}
		} catch (FileNotFoundException ex) {
			System.out.println("Given file could not be found");
		} catch (IOException ex) {
			System.out.println("There was a problem reading the file");
		} catch (IllegalArgumentException ex) {
			System.out.println("Invalid number of arguments given");
		}
	}
	
	private static final String ASK_DIGEST_MESSAGE = 
			"Please provide expected sha-256 digest for ";
	
	/**
	 * Implementation of the command used for checking the SHA-256 digest of the file.
	 * This command receives one argument - path to file to be checked, but length of 
	 * array given to the method should be two - command name and the path to file to be checked.
	 * When command is executed, user is asked to input the expected SHA-256 digest.
	 * @param args command name and path to file to be checked
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static void checkSHA(String args[]) throws IOException, NoSuchAlgorithmException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Invalid number of arguments");
		}
		System.out.print(ASK_DIGEST_MESSAGE + args[1] + ":\n> ");
		
		String expectedDigest = IN.nextLine();
		
		MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
		try (BufferedInputStream reader = new BufferedInputStream(
				new FileInputStream(args[1]))) {
			byte[] buffer = new byte[DEFAULT_CLUSTER_SIZE];
			
			while (true) {
				int readSize = reader.read(buffer);
				if (readSize == -1) break;
				md.update(buffer, 0, readSize);
			}	
		}
		
		String actualDigest = Util.bytetohex(md.digest());
		
		System.out.println("Digesting completed. ");
		if (expectedDigest.toLowerCase().equals(actualDigest)) {
			System.out.println("Digest of " + args[1] + " matches expected digest.");
		} else {
			System.out.println("Digest of " + args[1] + " does not match the expected digest."
					+ " Digest was: " + actualDigest);
		}
	}

	/**
	 * Implementation of the encrypt command. This command receives two arguments -
	 * path to the file to be encrypted and path to the file where the encrypted file
	 * should be stored, but additionally these method receives its name as a first argument.
	 * When command starts executing, user is asked to provide password and initialization
	 * vector used by the encryption algorithm.
	 * @param args array of 3 strings - command name, path to file to be encrypted
	 * 				and path where the encrypted file should be saved 
	 * @throws FileNotFoundException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 */
	public static void encrypt(String args[]) throws FileNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException {
		System.out.println("ENCRYPTION");
		transformFile(args, Cipher.ENCRYPT_MODE);
		System.out.println("Encryption completed. Generated file "
				+ args[2] + " based on file " + args[1]);
	}

	/**
	 * Implementation of the decrypt command. This command receives two arguments -
	 * path to the file to be decrpyted and path to the file where the decrypted file
	 * should be stored, but additionally these method receives its name as a first argument.
	 * When command starts executing, user is asked to provide password and initialization
	 * vector used by the decryption algorithm.
	 * @param args array of 3 strings - command name, path to file to be decrypted
	 * 				and path where the decrypted file should be saved 
	 * @throws FileNotFoundException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 */
	public static void decrypt(String args[]) throws FileNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException {
		System.out.println("DECRYPTION");
		transformFile(args, Cipher.DECRYPT_MODE);
		System.out.println("Decryption completed. Generated file "
				+ args[2] + " based on file " + args[1]);
	}
	
	/**
	 * Helper method for decrypt and encrypt command. It generates the cipher used
	 * for encryption or decryption and uses it to generate the file. If file should
	 * be encrypted. Depending on the argument provided as cipherMode the given file
	 * is encrpyted or decrypted.
	 * @param args array of 3 strings - command name, path to file to be decrypted
	 * 				and path where the decrypted file should be saved 
	 * @param cipherMode mode in which should cipher operate. 
	 * @throws FileNotFoundException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 */
	private static void transformFile(String[] args, int cipherMode) throws FileNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException {
		if (args.length != 3) {
			throw new IllegalArgumentException("Invalid number of arguments");
		}
		
		Cipher cipher = generateCipher(cipherMode);
		generateFile(cipher, args[1], args[2]);
	}

	/**
	 * Helper method for generating file from the given file using a provided cipher.
	 * @param cipher cipher to be used
	 * @param source path to file from which the new file should be generated
	 * @param target path where the generated file should be saved
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static void generateFile(Cipher cipher, String source, String target) throws FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException {
		BufferedInputStream reader = new BufferedInputStream(
				new FileInputStream(source));
		BufferedOutputStream writer = new BufferedOutputStream(
				new FileOutputStream(target));
		
		byte[] buffer = new byte[4096];
		while (true) {
			int readSize = reader.read(buffer);
			if (readSize == -1) break;
			writer.write(cipher.update(buffer, 0, readSize));
		}
		writer.write(cipher.doFinal());
		
		reader.close();
		writer.close();
	}
	
	private static final String PROVIDE_PASSWORD_MESSAGE = 
			"Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ";
	private static final String PROVIDE_INIT_VECTOR_MESSAGE =
			"Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ";
	
	private static final String DEFAULT_ENCRYPTION_ALGORITHM = "AES";
	private static final String DEFAULT_TRANSFORMATION = "AES/CBC/PKCS5Padding";
	
	/**
	 * Helper method that generates the cipher that uses a AES encryption algorithm.
	 * @param cipherMode mode in which cipher should be operate
	 * @return {@link Cipher} that operates in the provided cipher mode using 
	 * 			AES encryption algorithm
	 */
	private static Cipher generateCipher(int cipherMode) {
		System.out.print(PROVIDE_PASSWORD_MESSAGE);
		String password = IN.nextLine().trim();
		
		if (password.length() != 32) {
			throw new IllegalArgumentException("Password must be a 32 hex-digit string");
		}
		
		System.out.print(PROVIDE_INIT_VECTOR_MESSAGE);
		String initVector = IN.nextLine().trim();
		
		if (initVector.length() != 32) {
			throw new IllegalArgumentException("Initialization vector must be a 32 hex-digit string");
		}
		
		try {
			SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(password), DEFAULT_ENCRYPTION_ALGORITHM);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(initVector));
			Cipher cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION);
			cipher.init(cipherMode, keySpec, paramSpec);
			return cipher;
		} catch (NoSuchAlgorithmException|NoSuchPaddingException|InvalidKeyException|InvalidAlgorithmParameterException ex) {
			throw new IllegalStateException(ex.getMessage() + "Error while generating cipher");
		}
	}
	
}
