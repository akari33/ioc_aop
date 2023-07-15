package com.gn.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IocUtil {
    /**
     * 根据配置名文件加载配置文件
     */
    public static Properties getPropertyByName(String fileName) {
        InputStream is = null;
        Properties properties = null;
        try {
            properties = new Properties();
            is = IocUtil.class.getClassLoader().getResourceAsStream(fileName);
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties;
    }

    /**
     * 首字母转小写
     */
    public static String toLowercaseIndex(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        char[] chars = name.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
