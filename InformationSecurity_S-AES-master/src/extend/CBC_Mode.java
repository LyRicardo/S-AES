package extend;
import src.S_AES;

/**
 * CBC模式加密解密类
 */
public class CBC_Mode {
    private final S_AES sAES; // AES操作工具类
    private int[] initial_vector; // 初始化向量

    /**
     * 构造方法
     */
    public CBC_Mode() {
        sAES = new S_AES(); // 创建AES操作工具对象
    }

    /**
     * 设置初始化向量
     * @param initial_vector 初始向量
     * @throws IllegalArgumentException 如果初始向量长度不为16
     */
    public void set_initial_vector(int[] initial_vector) {
        if (initial_vector.length == 16) {
            this.initial_vector = initial_vector;
        } else {
            throw new IllegalArgumentException("初始向量长度必须为16位");
        }
    }

    /**
     * 在CBC模式下加密明文
     * @param plaintext 明文
     * @param key 密钥
     * @return 密文
     * @throws IllegalStateException 如果未设置初始化向量
     */
    public int[] encryptInCBC(int[] plaintext, int[] key) {
        if (initial_vector == null) {
            throw new IllegalStateException("初始向量未设置。在加密之前调用set_initial_vector()。");
        }

        int[] ciphertext = new int[plaintext.length];
        int[] PreBlock = initial_vector;

        for (int i = 0; i < plaintext.length; i += 16) {
            int[] CurBlock = new int[16];
            System.arraycopy(plaintext, i, CurBlock, 0, 16);

            // 与上一个密文块或初始向量进行异或操作
            CurBlock = S_AES.bitXOR(CurBlock, PreBlock);

            // 加密当前块
            int[] encryptedBlock = sAES.encrypt(CurBlock, key);

            // 将加密块保存为下一轮的上一个密文块
            PreBlock = encryptedBlock;

            // 将加密块追加到密文中
            System.arraycopy(encryptedBlock, 0, ciphertext, i, 16);
        }

        return ciphertext;
    }

    /**
     * 在CBC模式下解密密文
     * @param ciphertext 密文
     * @param key 密钥
     * @return 明文
     * @throws IllegalStateException 如果未设置初始化向量
     */
    public int[] decryptInCBC(int[] ciphertext, int[] key) {
        if (initial_vector == null) {
            throw new IllegalStateException("初始向量未设置。在解密之前调用set_initial_vector()。");
        }

        int[] plaintext = new int[ciphertext.length];
        int[] previousBlock = initial_vector;

        for (int i = 0; i < ciphertext.length; i += 16) {
            int[] currentBlock = new int[16];
            System.arraycopy(ciphertext, i, currentBlock, 0, 16);

            // 解密当前块
            int[] decryptedBlock = sAES.decrypt(currentBlock, key);

            // 与上一个密文块或初始向量进行异或操作
            decryptedBlock = S_AES.bitXOR(decryptedBlock, previousBlock);

            // 将解密块保存为下一轮的上一个密文块
            previousBlock = currentBlock;

            // 将解密块追加到明文中
            System.arraycopy(decryptedBlock, 0, plaintext, i, 16);
        }

        return plaintext;
    }
}
