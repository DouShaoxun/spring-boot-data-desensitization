package cn.cruder.sbdd.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 数据脱敏工具类
 *
 * @Author: cruder
 * @Date: 2021/12/09/15:06
 */
public class DataDesensitizationUtil {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1(3|4|5|6|7|8|9)\\d{9}$");

    /**
     * 对字符串进行脱敏操作
     *
     * @param origin          原始字符串
     * @param prefixNoMaskLen 左侧需要保留几位明文字段
     * @param suffixNoMaskLen 右侧需要保留几位明文字段
     * @param maskStr         用于遮罩的字符串, 如'*'
     * @return 脱敏后结果
     */
    public static String desValue(String origin, int prefixNoMaskLen, int suffixNoMaskLen, String maskStr) {
        if (org.apache.commons.lang3.StringUtils.isBlank(origin)) {
            return origin;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = origin.length(); i < n; i++) {
            if (i < prefixNoMaskLen) {
                sb.append(origin.charAt(i));
                continue;
            }
            if (i > (n - suffixNoMaskLen - 1)) {
                sb.append(origin.charAt(i));
                continue;
            }
            sb.append(maskStr);
        }
        return sb.toString();
    }


    /**
     * 【中文姓名】只显示最后一个汉字，其他隐藏为星号，比如：**梦
     *
     * @param fullName 姓名
     * @return 结果
     */
    public static String chineseName(String fullName) {
        return desValue(fullName, 1, 0, "*");
    }


    /**
     * 【身份证号】显示前4位, 后2位，其他隐藏。
     *
     * @param id 身份证号码
     * @return 结果
     */
    public static String idCardNum(String id) {
        return desValue(id, 4, 2, "*");
    }


    /**
     * 【手机号码】前三位，后四位，其他隐藏。
     *
     * @param mobile 手机号码
     * @return 结果
     */
    public static String mobilePhone(String mobile) {
        if (StringUtils.isBlank(mobile) || !MOBILE_PATTERN.matcher(mobile).matches()) {
            return mobile;
        }
        return desValue(mobile, 3, 4, "*");
    }


}
