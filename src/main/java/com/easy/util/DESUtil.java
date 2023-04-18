package com.easy.util;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class DESUtil {

    private static final SymmetricCrypto des;

    static {
        // sgEsnN6QWq8W7j5H01020304即为密钥明文长24位，
        // 不够则会随机补足24位
        des = new SymmetricCrypto(SymmetricAlgorithm.DES, "plsapalsllsxmis*s@qq112s".getBytes());
    }

    /**
     * des解密
     *
     * @param content 密文
     * @return 解密后的明文
     */
    public static String decrypt(String content) {
        return des.decryptStr(content);
    }

    /**
     * des加密
     *
     * @param content 明文
     * @return 加密后的密文
     */
    public static String encryption(String content) {
        return des.encryptHex(content);
    }


}
