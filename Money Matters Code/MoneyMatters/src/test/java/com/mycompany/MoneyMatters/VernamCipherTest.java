package com.mycompany.MoneyMatters;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mycompany.Algorithms.VernamCipher;

public class VernamCipherTest {

    private final String folderName = "VernamKeys";
    private final String email = "test@gmail.com";
    private final File folder = new File(folderName);
    private final File file = new File(folderName + File.separator + email + ".json");
    
    @BeforeEach
    void cleanBefore() throws Exception {
        deleteIfExists(file);
        if (folder.exists()) {
            folder.delete();
        }
    }

    @AfterEach
    void cleanAfter() throws Exception {
        deleteIfExists(file);
        if (folder.exists()) {
            folder.delete();
        }
    }

    private void deleteIfExists(File f) throws Exception {
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    void testConvertPlainTextToBinary() {
        String text = "A";
        String plainText = "1000001";
        String actual = VernamCipher.convertPlainTextToBinary(text);
        assertEquals(plainText, actual);
    }

    @Test
    void testXORfunction() {
        String plainText = "1010101";
        String vernamKey = "1100110";
        String expectedCipherText = "0110011";
        String actualCipherText = VernamCipher.XORfunction(plainText, vernamKey);
        assertEquals(expectedCipherText, actualCipherText);
    }

    @Test
    void writesAndReadsVernamKey() {
        final String vernamKey = "1010101";
        VernamCipher.storeVernamKeysInJSON(email, vernamKey);

        assertTrue(folder.exists());
        assertTrue(file.exists());

        String loadedVernamKey = VernamCipher.loadVernamKey(email);
        assertEquals(vernamKey, loadedVernamKey);
    }
}