# S-AES
重庆大学大数据与软件学院-信息安全导论课程实验，经胡海波老师指导，由林钰完成

## 运行结果
1.基础输入
！[](https://lyricardo.oss-cn-chengdu.aliyuncs.com/S-AES/1.png)
! [](https://lyricardo.oss-cn-chengdu.aliyuncs.com/S-AES/2.png)
! [](https://lyricardo.oss-cn-chengdu.aliyuncs.com/S-AES/3.png)
! [](https://lyricardo.oss-cn-chengdu.aliyuncs.com/S-AES/4.png)
2.拓展输入
! [](https://lyricardo.oss-cn-chengdu.aliyuncs.com/S-AES/5.png)
! [](https://lyricardo.oss-cn-chengdu.aliyuncs.com/S-AES/6.png)
3.双重加密
! [](https://lyricardo.oss-cn-chengdu.aliyuncs.com/S-AES/7.png)
4.中间相遇攻击
未实现
5.三重加密
! [](https://lyricardo.oss-cn-chengdu.aliyuncs.com/S-AES/8.png)
6.CBC密码分组链
! [](https://lyricardo.oss-cn-chengdu.aliyuncs.com/S-AES/9.png)
7.组间测试
已和朱清杨、邓怡杰组完成测试并通过

## 开发手册
### 项目详情
S-AES(page):加解密界面
ResultDialog:运行结果界面
GF2_4:GF2^4实现
S-AES(src):加解密；双重、三重加解密；轮密钥加；行位移；列混淆等算法实现
StringBinaryTransfer:二进制字符串转换
Test：双、三重加解密测试
UniTest：CBC密码分组链测试

### S-AES详解（src/S-AES.java)
1.密钥拓展
在S-AES加密的开始，我们首先对初始的16位密钥进行扩展，以产生所需的子密钥。
使用expand_key函数，将初始密钥分为两个8位的部分：W0和W1。
使用函数 g（在代码中是fun_g）对W1进行处理，然后与W0进行异或操作得到W2。
将W2与W1进行异或操作，得到W3。 使用函数g对w3进行处理，然后与w2进行异或操作，得到w4。
将w4与w3进行异或操作，得到w5。

2.初始轮密钥加
使用add_round_key函数，将明文与子密钥w0和w1进行异或操作。

3.第一轮
半字节替代（Byte Substitution）：使用s_box（S盒）进行半字节替代。 行位移（Row Shift）：对状态矩阵进行行位移操作。
列混淆（Mix Columns）：使用MC函数和MC_table进行列混淆操作。
轮密钥加（Add Round Key）：使用子密钥w2和w3。

4.第二轮
半字节替代（Byte Substitution）：使用s_box（S盒）进行半字节替代。
行位移（Row Shift）：对状态矩阵进行行位移操作。 轮密钥加（Add Round Key）：使用子密钥w4和w5。

5.输出
最后的状态矩阵是加密的密文。

## 用户指南
基于《信息安全导论》课程内容——附录D：简化AES实现，用于对SAES（Simplified Advanced Encryption Standard）算法进行加密和解密操作。

加密
进入页面默认为加密模式，在第一行输入明文，第二行输入密钥，点击encode即可加密

解密
点击取消encryption mode的勾，进入解密模式，第一行输密文，第二行输密钥，点击decode即可解密

注意
密钥必须为标准的16位二进制数；明文的标准格式为16位二进制数，拓展模式会转换为ASCII码处理。
