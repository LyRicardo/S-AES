package test;

import src.S_AES;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int [] plaintext1 = new int[]{1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0};
//        int [] key1 = new int[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        int [] key2 = new int[]{1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1};
//        int [] key3 = new int[]{0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0};
//        int [] key48 = new int[]{
//                0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//                1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1,
//                0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0
//        };

        S_AES s_aes = new  S_AES();

//        System.out.println("三重加密结果："+ Arrays.toString(s_aes.triple_encrypt(plaintext1, key48)));
//        System.out.println("第一次加密："+Arrays.toString(s_aes.encrypt(plaintext1, key1)));
//        System.out.println("第二次加密："+Arrays.toString(s_aes.encrypt(s_aes.encrypt(plaintext1, key1), key2)));
//        System.out.println("第三次加密："+Arrays.toString(s_aes.encrypt(s_aes.encrypt(s_aes.encrypt(plaintext1, key1), key2), key3)));
//        System.out.println("解密得："+Arrays.toString(s_aes.triple_decrypt(s_aes.triple_encrypt(plaintext1, key48), key48)));

        int [] key1 = new int[]{0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0};
        int [] key2 = new int[]{1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int [] key32 = new int[]{
                0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
                1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
        };

        System.out.println("两重加密结果："+ Arrays.toString(s_aes.double_encrypt(plaintext1, key32)));
        System.out.println("第一次加密："+Arrays.toString(s_aes.encrypt(plaintext1, key1)));
        System.out.println("第二次加密："+Arrays.toString(s_aes.encrypt(s_aes.encrypt(plaintext1, key1), key2)));
        System.out.println("解密得："+Arrays.toString(s_aes.double_decrypt(s_aes.double_encrypt(plaintext1, key32), key32)));



    }
}
