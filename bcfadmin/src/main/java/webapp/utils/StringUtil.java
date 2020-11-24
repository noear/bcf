package webapp.utils;


import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static <T> String join(String mark, List<T> list) {
        String res = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                res += list.get(i);
            } else {
                res += (list.get(i) + mark);
            }
        }
        return res;
    }

    //是否是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    //替换手机号
    public static String mobile(String value, String change_value, int start, int end) {
        String startStr = value.substring(0, start);
        String endStr = value.substring(end, value.length());
        return startStr + change_value + endStr;
    }

    //取银行卡号后四位
    public static String card(String value, int start) {
        return value.substring(value.length() - start, value.length());
    }


    /**
     * Ascii转换为字符串
     *
     * @param value
     * @return
     */
    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    ///double格式化
    public static String doubleFormt(double input) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        return df.format(input);
    }

    public static String doubleFormt2(double input) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.###");
        return df.format(input);
    }

    public static boolean idCardNoEquals(String input, String operator) {

        String tmpa = subStringCardNo(input);
        String tmpb = subStringCardNo(operator);
        if (tmpa.equals(tmpb)) {
            return true;
        }
        return false;
    }

    //截取身份证号码
    public static String subStringCardNo(String input) {
        int length = input.length();
        if (length > 8) {
            String head = input.substring(0, 4);
            String end = input.substring(length - 4, length);
            return head + end;
        } else {
            return "";
        }
    }

    //校验身份证号码格式
    public static boolean isIDCard(String idcard) {
        String str = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
        Pattern pattern = Pattern.compile(str);
        return pattern.matcher(idcard).matches() ? true : false;
    }

    public static String genUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String genUkey(int ugroupId, String mobile) {
        return EncryptUtil.md5(ugroupId + "#" + mobile);
    }

}
