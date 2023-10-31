package src;

public class GF2_4 {

    private static final int FIELD_SIZE = 16; // 定义域的大小为16
    private static final int MODULUS = 0b10011; // 定义域的模数为二进制数0b10011

    // 在GF(2^4)上执行加法操作
    public static int add(int a, int b) {
        return a ^ b;
    }

    // 在GF(2^4)上执行乘法操作
    public static int multiply(int a, int b) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            if ((b & 1) == 1) {
                result ^= a; // 当b的最低位为1时，对a进行异或操作
            }
            boolean carry = (a & 0b1000) == 0b1000; // 判断a的第2和第3位是否均为1
            a <<= 1; // 将a左移一位
            if (carry) {
                a ^= MODULUS; // 如果产生了进位，则对a进行异或操作
            }
            b >>= 1; // 将b右移一位
        }
        return result;
    }

    public static void main(String[] args) {
        int a = 11; // 十进制数11的二进制表示为00001011
        int b = 12; // 十进制数12的二进制表示为00001100

        // 在GF(2^4)上执行加法操作
        int sum = add(a, b);
        System.out.println("加法：" + sum);

        // 在GF(2^4)上执行乘法操作
        int product = multiply(a, b);
        System.out.println("乘法：" + product);
    }

}
