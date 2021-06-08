package com.terziev.konstantin.caesarcipher.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CaesarCipherTest {

    private static final String ENCRYPTED_TEXT = "Iyeb locd kxn gscocd bopeqo pbyw kvv dbyelvoc sc sx iyeb cmsoxmo.";
    private static final String DECRYPTED_TEXT = "Your best and wisest refuge from all troubles is in your science.";

    @Test
    public void testEncrypt() {
        String encryptedText = CaesarCipher.encrypt(DECRYPTED_TEXT, 10);

        Assertions.assertEquals(ENCRYPTED_TEXT, encryptedText);
    }

    @Test
    public void testDecrypt() {
        String decryptedText = CaesarCipher.decrypt(ENCRYPTED_TEXT, 36);

        Assertions.assertEquals(DECRYPTED_TEXT, DECRYPTED_TEXT);
    }

}
