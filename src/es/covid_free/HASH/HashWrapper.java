package es.covid_free.HASH;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * clase para aplicar algoritmo de hash a las contraseñas
 * @author covid_free
 *
 */
public final class HashWrapper {

	public static final String ID = "$31$";

	// Algoritmo de hash seleccionado
	private static final String ALGORITMO = "PBKDF2WithHmacSHA512";

	// Tamaño del hash generado
	private static final int SIZE = 128;
	
	// Iteraciones ( 1 << coste)
	private static final int coste = 16;

	// Patrón del hash
	private static final Pattern layout = Pattern.compile("\\$31\\$(\\d\\d?)\\$(.{43})");

	private final SecureRandom random;


	public HashWrapper() {
	    this.random = new SecureRandom();
	}

	/**	Aplica el hash a la contraseña para ser guardada en la BD
	 * 	@param password
	 * 	@return hash de la contraseña
	 * 
	 */
	public String hash(char[] password) {
		byte[] salt = new byte[SIZE / 8];
		random.nextBytes(salt);
		byte[] dk = pbkdf2(password, salt, 1 << coste);
		byte[] hash = new byte[salt.length + dk.length];
		System.arraycopy(salt, 0, hash, 0, salt.length);
		System.arraycopy(dk, 0, hash, salt.length, dk.length);
		Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
		return ID + coste + '$' + enc.encodeToString(hash);
	}
	
	/**	Aplica el hash a la contraseña introducida para compararlo
	 * 	con el guardado en la BD y comprobar si las contraseñas coinciden
	 * 	@param password
	 * 	@return devuelve verdad si coincoden y falso en caso contrario
	 * 
	 */
	public boolean authenticate(char[] password, String token) {
		Matcher m = layout.matcher(token);
		if (!m.matches()) {
			throw new IllegalArgumentException("Invalid token format");
		}
		int iterations = (1 << Integer.parseInt(m.group(1)));
		byte[] hash = Base64.getUrlDecoder().decode(m.group(2));
		byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
		byte[] check = pbkdf2(password, salt, iterations);
		int zero = 0;
		for (int i = 0; i < check.length; ++i) {
			zero |= hash[salt.length + i] ^ check[i];
		}
		return zero == 0;
	}
	
	/**	Aplica el algoritmo pbkdf2 indicado
	 * 	@param password
	 *  @param salt
	 *  @param iterations
	 * 	@return hash de la contraseña
	 * 
	 */
	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
		KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITMO);
			return f.generateSecret(spec).getEncoded();
		}
		catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("Missing algorithm: " + ALGORITMO, ex);
		}
		catch (InvalidKeySpecException ex) {
			throw new IllegalStateException("Invalid SecretKeyFactory", ex);
		}
	}
}


