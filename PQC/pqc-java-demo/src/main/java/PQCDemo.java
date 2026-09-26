import org.bouncycastle.jcajce.SecretKeyWithEncapsulation;
import org.bouncycastle.jcajce.spec.KEMExtractSpec;
import org.bouncycastle.jcajce.spec.KEMGenerateSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

public class PQCDemo {

    static {
        // Register Bouncy Castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== 1. ML-KEM (Key Encapsulation / FIPS 203) Demo ===");
        demoMLKEM();

        System.out.println("\n=== 2. ML-DSA (Digital Signatures / FIPS 204) Demo ===");
        demoMLDSA();
    }

    /**
     * Demonstrates ML-KEM Key Exchange / Encapsulation (ML-KEM-768)
     */
    public static void demoMLKEM() throws Exception {
        // 1. Receiver generates Key Pair (Public Key + Private Key)
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ML-KEM", "BC");
        kpg.initialize(768); // Options: 512, 768, 1024
        KeyPair receiverKeyPair = kpg.generateKeyPair();

        PublicKey receiverPublicKey = receiverKeyPair.getPublic();
        PrivateKey receiverPrivateKey = receiverKeyPair.getPrivate();

        // 2. Sender Encapsulates: Uses Receiver's Public Key to generate shared secret + ciphertext
        KeyGenerator keyGen = KeyGenerator.getInstance("ML-KEM", "BC");
        keyGen.init(new KEMGenerateSpec(receiverPublicKey, "AES")); // "AES" specifies symmetric output algorithm
        
        SecretKeyWithEncapsulation senderEncapsulation = (SecretKeyWithEncapsulation) keyGen.generateKey();
        byte[] senderSharedSecret = senderEncapsulation.getEncoded();
        byte[] ciphertext = senderEncapsulation.getEncapsulation();

        // 3. Receiver Decapsulates: Uses Receiver's Private Key + Ciphertext to derive shared secret
        KeyGenerator decapsGen = KeyGenerator.getInstance("ML-KEM", "BC");
        decapsGen.init(new KEMExtractSpec(receiverPrivateKey, ciphertext, "AES"));
        
        SecretKeyWithEncapsulation receiverDecapsulation = (SecretKeyWithEncapsulation) decapsGen.generateKey();
        byte[] receiverSharedSecret = receiverDecapsulation.getEncoded();

        // Verify shared secrets match
        boolean match = Arrays.equals(senderSharedSecret, receiverSharedSecret);
        System.out.println("Shared Secret Matched? " + match);
        System.out.println("Ciphertext Size: " + ciphertext.length + " bytes");
        System.out.println("Derived Shared Secret Size: " + senderSharedSecret.length + " bytes");
    }

    /**
     * Demonstrates ML-DSA Digital Signature Generation & Verification (ML-DSA-65)
     */
    public static void demoMLDSA() throws Exception {
        // 1. Key Pair Generation
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ML-DSA", "BC");
        kpg.initialize(65); // Options: 44, 65, 87
        KeyPair keyPair = kpg.generateKeyPair();

        byte[] message = "Lecture Note Verification: Post-Quantum Cryptography is active.".getBytes();

        // 2. Sign the message using Private Key
        Signature signer = Signature.getInstance("ML-DSA", "BC");
        signer.initSign(keyPair.getPrivate());
        signer.update(message);
        byte[] signatureBytes = signer.sign();

        // 3. Verify the signature using Public Key
        Signature verifier = Signature.getInstance("ML-DSA", "BC");
        verifier.initVerify(keyPair.getPublic());
        verifier.update(message);
        boolean isValid = verifier.verify(signatureBytes);

        System.out.println("Signature Valid? " + isValid);
        System.out.println("Signature Length: " + signatureBytes.length + " bytes");
    }
}