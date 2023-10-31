package src;
import java.util.ArrayList;

public class S_AES {

    //s盒与逆s盒
    int[][] s_box = {{9, 4, 10, 11}, {13, 1, 8, 5}, {6, 2, 0, 3}, {12, 14, 15, 7}};
    int[][] Is_box = {{10, 5, 9, 11}, {1, 7, 8, 15}, {6, 0, 2, 3}, {12, 4, 13, 14}};
    //轮常量
    int [] RConstant1 = {1,0,0,0,0,0,0,0};
    int [] RConstant2 = {0,0,1,1,0,0,0,0};
    //半字节表
    int [] MC_table = {1,4,4,1};
    int [] IMC_table = {9,2,2,9};

    //16位数组转换为4位数组
    public int[] key_to_matrix(int[] key_portion) {
        int [] res = new int[4];
        res[0] = key_portion[0]*8 + key_portion[1]*4+ key_portion[2]*2 + key_portion[3];
        res[2] = key_portion[4]*8 + key_portion[5]*4+ key_portion[6]*2 + key_portion[7];
        res[1] = key_portion[8]*8 + key_portion[9]*4+ key_portion[10]*2 + key_portion[11];
        res[3] = key_portion[12]*8 + key_portion[13]*4+ key_portion[14]*2 + key_portion[15];

        return res;
    }


    //将十进制数字转化为4位二进制数
    public int[] to_binary(int DecimalNumber) {
        StringBuilder binaryString = new StringBuilder(Integer.toBinaryString(DecimalNumber));
        while (binaryString.length() < 4) {//高位补0
            binaryString.insert(0, "0");
        }
        int[] binaryArray = new int[4];
        for (int i = 0; i < 4; i++) {
            binaryArray[i] = Character.getNumericValue(binaryString.charAt(i));
        }
        return binaryArray;
    }

    //将四位矩阵转化为16位二进制数
    public int[] matrix_to_binary(int[] matrix) {
        int [] binaryArray = new int[16];
        int [] temp1 = to_binary(matrix[0]);
        int [] temp2 = to_binary(matrix[1]);
        int [] temp3 = to_binary(matrix[2]);
        int [] temp4 = to_binary(matrix[3]);
        System.arraycopy(temp1, 0, binaryArray, 0, 4);
        System.arraycopy(temp3, 0, binaryArray, 4, 4);
        System.arraycopy(temp2, 0, binaryArray, 8, 4);
        System.arraycopy(temp4, 0, binaryArray, 12, 4);
        return binaryArray;
    }

    //数组各数位异或运算
    public static int[] bitXOR(int[] array1, int[] array2) {
        if (array1.length != array2.length) {//检查异或的数组长度是否相同
            throw new IllegalArgumentException("长度不同");
        }
        int[] resultArray = new int[array1.length];
        for (int i = 0; i < array1.length; i++) {
            resultArray[i] = array1[i] ^ array2[i];
        }
        return resultArray;
    }

    //轮密钥加
    public int [] Round_Key(int [] plaintext, int [] key1, int [] key2){
        int [] key_portion = new int[16];
        System.arraycopy(key1, 0, key_portion, 0, 8);
        System.arraycopy(key2, 0, key_portion, 8, 8);

        if (plaintext.length==16) return bitXOR(plaintext, key_portion);
        else return bitXOR(plaintext, key_to_matrix(key_portion));
    }


    //半字节和逆半字节替代
    public int[] Nibble_Substitution(int[] input, int[][] s_box) {
        int [] res = new int[4];
        for (int i = 0; i < 4; i++) {
            res[i] =  nibble_sub(input[i], s_box);
        }
        return  res;
    }
    public int nibble_sub(int k, int [][] s_box) {
        int [] input = to_binary(k);
        int x = input[0]*2+input[1];    //输入的4位二进制数，两位为行两位为列
        int y = input[2]*2+input[3];

        return s_box[x][y];
    }

        //拓展密钥，16位的初始密钥被分为w0，w1两个8位部分,分别是前8位与后8位
    public ArrayList<int[]> expand_key(int[] key) {
        ArrayList<int[]> res = new ArrayList<>(); // 用于存储扩展密钥的列表
        int[] w0 = new int[8]; // 前8位扩展密钥
        int[] w1 = new int[8]; // 后8位扩展密钥
        System.arraycopy(key, 0, w0, 0, 8); // 将初始密钥的前8位复制到w0数组中
        System.arraycopy(key, 8, w1, 0, 8); // 将初始密钥的后8位复制到w1数组中
        int[] w2 = bitXOR(w0, fun_g(w1, RConstant1)); // 计算w2数组，对w0数组和fun_g(w1, RConstant1)数组进行异或运算
        int[] w3 = bitXOR(w2, w1); // 计算w3数组，对w2数组和w1数组进行异或运算
        int[] w4 = bitXOR(w2, fun_g(w3, RConstant2)); // 计算w4数组，对w2数组和fun_g(w3, RConstant2)数组进行异或运算
        int[] w5 = bitXOR(w4, w3); // 计算w5数组，对w4数组和w3数组进行异或运算
        res.add(w0); res.add(w1); res.add(w2); res.add(w3); res.add(w4); res.add(w5); // 将w0、w1、w2、w3、w4、w5数组添加到res列表中

        return res; // 返回扩展密钥的列表
    }


        /**
     * 函数功能：对数组w的前4个元素进行操作，并返回结果
     * @param w 输入数组
     * @param ignoredRCON 忽略的RCON数组
     * @return 返回操作后的结果数组
     */
    int[] fun_g(int[] w, int[] ignoredRCON) {
        int[] N1 = new int[4];  // 存储w数组的前4个元素
        int[] N2 = new int[4];  // 存储w数组的后4个元素
        System.arraycopy(w, 0, N1, 0, 4);  // 将w数组的前4个元素复制到N1数组
        System.arraycopy(w, 4, N2, 0, 4);  // 将w数组的后4个元素复制到N2数组
        int k1 = N1[0] * 8 + N1[1] * 4 + N1[2] * 2 + N1[3];  // 计算k1值
        int k2 = N2[0] * 8 + N2[1] * 4 + N2[2] * 2 + N2[3];  // 计算k2值
        N1 = to_binary(nibble_sub(k1, s_box));  // 将k1与s_box做 nibble_sub 运算后转换为二进制数组
        N2 = to_binary(nibble_sub(k2, s_box));  // 将k2与s_box做 nibble_sub 运算后转换为二进制数组
        int[] combine_N12 = new int[8];  // 创建8个元素的数组combine_N12
        System.arraycopy(N2, 0, combine_N12, 0, 4);  // 将N2数组的元素复制到combine_N12数组的前4个位置
        System.arraycopy(N1, 0, combine_N12, 4, 4);  // 将N1数组的元素复制到combine_N12数组的后4个位置

        return bitXOR(combine_N12, ignoredRCON);  // 将combine_N12数组和ignoredRCON数组进行异或运算后返回结果
    }


    //行位移
    public int[] row_shift(int[] row) {
        int temp = row[2];
        row[2] = row[3];
        row[3] = temp;
        return row;
    }

    // 列混淆和逆列混淆，定义为一个函数
    public int[] MC(int[] temp, int[] key) {
        // 输入temp是列混淆或者逆列混淆的参数矩阵，key是已经处理过的密钥（非初始密钥）
        int s0_0 = GF2_4.add(GF2_4.multiply(temp[0], key[0]), GF2_4.multiply(temp[1], key[2]));
        int s0_1 = GF2_4.add(GF2_4.multiply(temp[0], key[1]), GF2_4.multiply(temp[1], key[3]));
        int s1_0 = GF2_4.add(GF2_4.multiply(temp[2], key[0]), GF2_4.multiply(temp[3], key[2]));
        int s1_1 = GF2_4.add(GF2_4.multiply(temp[2], key[1]), GF2_4.multiply(temp[3], key[3]));

        return new int[] {s0_0, s0_1, s1_0, s1_1};
    }

    // 加密函数
        public int [] encrypt(int [] plaintext, int [] key){
        // 先拓展密钥
        ArrayList<int []> expand_keys = expand_key(key);
        // 将明文转化为状态矩阵
        plaintext = key_to_matrix(plaintext);
        // 第一步，轮密钥加
        plaintext = Round_Key(plaintext, expand_keys.get(0), expand_keys.get(1));
        // 第二步，半字节替代
        plaintext = Nibble_Substitution(plaintext,s_box);
        // 第三步，行位移
        plaintext = row_shift(plaintext);
        // 第四步，列混淆
        plaintext = MC(MC_table,plaintext);
        // 第五步，轮密钥加
        plaintext = Round_Key(plaintext, expand_keys.get(2), expand_keys.get(3));
        // 第五步，半字节替代
        plaintext = Nibble_Substitution(plaintext,s_box);
        // 第六步，行位移
        plaintext = row_shift(plaintext);
        // 第七步，轮密钥加
        plaintext = Round_Key(plaintext, expand_keys.get(4), expand_keys.get(5));
        // 将状态矩阵转化为二进制数组返回
        return matrix_to_binary(plaintext);
    }

    // 解密函数
        public int [] decrypt(int [] ciphertext, int [] key){
        // 扩展密钥
        ArrayList<int []> expand_keys = expand_key(key);
        // 将密文转化为状态矩阵
        ciphertext = key_to_matrix(ciphertext);
        // 进行密钥加操作
        ciphertext = Round_Key(ciphertext, expand_keys.get(4), expand_keys.get(5));
        // 进行逆行位移操作
        ciphertext = row_shift(ciphertext);
        // 进行逆半字节替代操作
        ciphertext = Nibble_Substitution(ciphertext,Is_box);
        // 进行密钥加操作
        ciphertext = Round_Key(ciphertext, expand_keys.get(2), expand_keys.get(3));
        // 进行逆列混淆操作
        ciphertext = MC(IMC_table,ciphertext);
        // 进行逆行移位操作
        ciphertext = row_shift(ciphertext);
        // 进行逆半字节替代操作
        ciphertext = Nibble_Substitution(ciphertext,Is_box);
        // 进行密钥加操作
        ciphertext = Round_Key(ciphertext, expand_keys.get(0), expand_keys.get(1));

        // 将矩阵转换为二进制数组返回
        return matrix_to_binary(ciphertext);
    }


        // 输入为字符串时的加密
    public String encrypt(String plaintext, String keys){
        StringBinaryTransfer sb = new StringBinaryTransfer();
        int [] key = sb.intStringToBinary(keys); // 先将字符串转化为16位二进制数的列表
        ArrayList<int []> plaintext_list = sb.SBTransfer(plaintext); // 将字符串转换为16位二进制数的列表
        ArrayList<int []> ciphertext_list = new ArrayList<>();
        for (int [] p: plaintext_list){
            int [] temp = encrypt(p,key); // 对每个明文进行加密
            ciphertext_list.add(temp); // 将加密结果添加到密文列表中
        }

        return sb.BSTransfer(ciphertext_list); // 将密文列表转换为字符串并返回
    }


    //输入为字符串时的解密
    public String decrypt(String ciphertext,String keys){
        StringBinaryTransfer sb = new StringBinaryTransfer();
        int [] key = sb.intStringToBinary(keys); // 同上
        ArrayList<int []> ciphertext_list = sb.SBTransfer(ciphertext); // 同上
        ArrayList<int []> plaintext_list = new ArrayList<>();
        for(int [] c: ciphertext_list){
            int [] temp = decrypt(c,key); // 对每个密文进行解密
            plaintext_list.add(temp); // 将解密结果添加到明文列表
        }
        return sb.BSTransfer(plaintext_list);
    }

    //双重加密，key为32位密钥，key = key1+key2，先用key1加密，再使用key2加密
    public int [] double_encrypt(int [] plaintext, int [] key){
        int [] key1 = new int[16];
        int [] key2 = new int[16];
        System.arraycopy(key,0,key1,0,16);
        System.arraycopy(key,16,key2,0,16); // 将key的前16位复制到key1，后16位复制到key2
        //第一轮加密
        int [] temp_ciphertext = encrypt(plaintext,key1);
        //第二轮加密
        return encrypt(temp_ciphertext,key2);
    }

    // 双重解密，key为32位密钥，key = key1+key2，先用key2解密，再使用key1解密
        public int [] double_decrypt(int [] ciphertext, int [] key){
        // 将密钥数组分为两个数组
        int [] key1 = new int[16];
        int [] key2 = new int[16];
        System.arraycopy(key,0,key1,0,16);
        System.arraycopy(key,16,key2,0,16);

        // 对密文进行一次解密操作，使用密钥数组的后半部分
        int [] temp_plaintext = decrypt(ciphertext,key2);

        // 对临时明文进行一次解密操作，使用密钥数组的前半部分
        return  decrypt(temp_plaintext,key1);
    }


        //三重加密，key变成48位分组密钥，key = key1+key2+key3，先用key1加密，再使用key2加密，最后使用key3加密
    public int [] triple_encrypt(int [] plaintext, int [] key){
        int [] key1 = new int[16];  //定义长度为16的密钥数组key1
        int [] key2 = new int[16];  //定义长度为16的密钥数组key2
        int [] key3 = new int[16];  //定义长度为16的密钥数组key3
        System.arraycopy(key,0,key1,0,16);  //将key中的前16个元素复制到key1数组中
        System.arraycopy(key,16,key2,0,16);  //将key中的第17到第32个元素复制到key2数组中
        System.arraycopy(key,32,key3,0,16);  //将key中的第33到第48个元素复制到key3数组中
        int [] temp_ciphertext = encrypt(plaintext,key1);  //使用key1对明文进行加密，返回加密结果temp_ciphertext
        int [] temp_temp_ciphertext = encrypt(temp_ciphertext,key2);  //使用key2对temp_ciphertext进行加密，返回加密结果temp_temp_ciphertext
        return encrypt(temp_temp_ciphertext,key3);  //使用key3对temp_temp_ciphertext进行加密，返回最终的加密结果
    }


    /**
 * 这是一个三次解密方法。它接收密文和密钥作为输入，并返回解密后的明文。
 *
 * @param ciphertext 密文，需要进行解密
 * @param key        密钥，用于解密密文
 * @return 解密后的明文
 */
public int[] triple_decrypt(int[] ciphertext, int[] key) {
    // 创建三个16个元素的数组来保存三个密钥的副本
    int[] key1 = new int[16];
    int[] key2 = new int[16];
    int[] key3 = new int[16];

    // 将密钥的前16个元素复制到key1数组中
    System.arraycopy(key, 0, key1, 0, 16);
    // 将密钥的中间16个元素复制到key2数组中
    System.arraycopy(key, 16, key2, 0, 16);
    // 将密钥的后16个元素复制到key3数组中
    System.arraycopy(key, 32, key3, 0, 16);

    // 使用key3解密密文，得到临时明文
    int[] temp_plaintext = decrypt(ciphertext, key3);
    // 使用key2解密临时明文，得到第二次临时明文
    int[] temp_temp_plaintext = decrypt(temp_plaintext, key2);
    // 使用key1解密第二次临时明文，得到解密后的明文
    return decrypt(temp_temp_plaintext, key1);
    }

}
