package test;

import extend.CBC_Mode;

import java.util.Arrays;

public class UnitTest {


    public static void main(String[] args) {
        CBC_Mode cbc = new CBC_Mode();

        // 设置初始化向量 16位二进制数
        int[] initial_vector = {1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1};
        cbc.set_initial_vector(initial_vector);

        // 设置密钥  16位二进制数
        int[] key = {1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        // 明文消息 32位二进制数
        int[] plaintext = {1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0};

        // 加密明文
        int[] ciphertext = cbc.encryptInCBC(plaintext, key);

        // 输出加密后的密文
        System.out.println("加密后的密文: " + Arrays.toString(ciphertext));

        // 解密密文
        int[] decryptedText = cbc.decryptInCBC(ciphertext, key);

        // 输出解密后的明文
        System.out.println("解密后的明文: " + Arrays.toString(decryptedText));
    }
}

