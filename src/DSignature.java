import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class DSignature {
	private int bitLength = 160;
	private SecureRandom random = new SecureRandom();
	private BigInteger gamma;
	private BigInteger p;
	private BigInteger alpha;
	private BigInteger x;
	private BigInteger y;
	private BigInteger msg;
	private BigInteger hash;
	private BigInteger U;
	private BigInteger Z;
	private BigInteger k;
	private BigInteger g;
	private BigInteger S;
	private BigInteger lCheck;
	private BigInteger rCheck;

	public DSignature() { }

	public void print() {
		System.out.println("gamma: " + gamma);
		System.out.println("p: " + p);
		System.out.println("alpha: " + alpha);
		System.out.println("x: " + x);
		System.out.println("y: " + y);
		System.out.println("hash: " + hash);
		System.out.println("U: " + U);
		System.out.println("Z: " + Z);
		System.out.println("k: " + k);
		System.out.println("g: " + g);
		System.out.println("S: " + S);
		System.out.println("lCheck: " + lCheck);
		System.out.println("rCheck: " + rCheck);
	}

	// Генерация простого γ, γ|p-1
	public void generateGamma() {
		gamma = BigInteger.probablePrime(bitLength, random);
	}

	// Вычисление модуля p; p = n*γ+1, n - случ. целое
	public void calculateP() {
		do {
			BigInteger n = new BigInteger(880, random);
			p = gamma.multiply(n).add(BigInteger.ONE);
		} while (!p.isProbablePrime(1));
	}

	// Генерация α - первообразного корня mod p
	public void generateAlpha() {
		BigInteger z;
		do {
			BigInteger b = new BigInteger(p.bitLength()-1, random); // Генерируем b < p-1
			BigInteger gammaShtrih = p.subtract(BigInteger.ONE).divide(gamma); // γ' = (p-1)/γ
			z = b.modPow(gammaShtrih, p); // z = b^γ' mod p
		} while (z.equals(1)); // если z != 1, то α = z, иначе - повтор
		alpha = z;
	}

	// Генерация секретного ключа
	public void generateX() {
		x = new BigInteger(bitLength, random);
	}

	// Вычисление открытого ключа
	public void calculateY() {
		y = alpha.modPow(x, p);
	}

	// Генерация сообщения
	public void generateMsg() { msg = new BigInteger(bitLength, random); }

	// Генерация U < p-1
	public void generateU() {
		U = new BigInteger(p.bitLength()-1, random);
	}

	// Вычисление Z = α^U mod p
	public void calculateZ() {
		Z = alpha.modPow(U, p);
	}

	// Вычисление k = (U / (H - x*Z)) mod γ
	public void calculateK() {
		BigInteger temp = hash.subtract(x.multiply(Z));
		BigInteger tempInv = temp.modInverse(gamma);
		k = U.multiply(tempInv).mod(gamma);
	}

	// Вычисление g = (H - x*Z) mod γ
	public void calculateG() {
		g = hash.subtract(x.multiply(Z)).mod(gamma);
	}

	// Вычисление S = α^g mod p
	public void calculateS() {
		S = alpha.modPow(g, p);
	}

	// Вычисление левой части проверочного сравнения = α^H mod p
	public void calculateLCheck() {
		lCheck = alpha.modPow(hash, p);
	}

	// Вычисление правой части проверочного сравнения = S*y^(S^k mod p) mod p
	public void calculateRCheck() {
		rCheck = S.multiply(y.modPow(S.modPow(k, p), p)).mod(p);
	}

	// MD5 хэш
	public void getMD5(String st) {
		MessageDigest messageDigest = null;
		byte[] digest = new byte[0];
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(st.getBytes());
			digest = messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		hash = new BigInteger(1, digest);
	}

	public void clear() {
		gamma = null;
		p = null;
		alpha = null;
		x = null;
		y = null;
		msg = null;
		hash = null;
		U = null;
		Z = null;
		k = null;
		g = null;
		S = null;
		lCheck = null;
		rCheck = null;
	}

	public BigInteger getGamma() {
		return gamma;
	}

	public BigInteger getP() {
		return p;
	}

	public BigInteger getAlpha() {
		return alpha;
	}

	public BigInteger getX() {
		return x;
	}

	public BigInteger getY() {
		return y;
	}

	public BigInteger getHash() {
		return hash;
	}

	public BigInteger getU() {
		return U;
	}

	public BigInteger getZ() {
		return Z;
	}

	public BigInteger getK() {
		return k;
	}

	public BigInteger getG() {
		return g;
	}

	public BigInteger getS() {
		return S;
	}

	public BigInteger getlCheck() {
		return lCheck;
	}

	public BigInteger getrCheck() {
		return rCheck;
	}

	public BigInteger getMsg() {
		return msg;
	}

	public void setMsg(BigInteger msg) {
		this.msg = msg;
	}

	public void setGamma(BigInteger gamma) {
		this.gamma = gamma;
	}

	public void setP(BigInteger p) {
		this.p = p;
	}

	public void setAlpha(BigInteger alpha) {
		this.alpha = alpha;
	}

	public void setX(BigInteger x) {
		this.x = x;
	}

	public void setY(BigInteger y) {
		this.y = y;
	}

	public void setHash(BigInteger hash) {
		this.hash = hash;
	}

	public void setU(BigInteger u) {
		U = u;
	}

	public void setZ(BigInteger z) {
		Z = z;
	}

	public void setK(BigInteger k) {
		this.k = k;
	}

	public void setG(BigInteger g) {
		this.g = g;
	}

	public void setS(BigInteger s) {
		S = s;
	}

	public void setlCheck(BigInteger lCheck) {
		this.lCheck = lCheck;
	}

	public void setrCheck(BigInteger rCheck) {
		this.rCheck = rCheck;
	}
}
