package com.management.leave.common.util;

import com.management.leave.common.constats.Constants;
import com.management.leave.model.pojo.EmployeeInfo;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

/** some common utils
 * @author zh
 */
public class CommonUtils {
    /**
     * Randomly generate a string of custom content
     *
     * @param length The length of the string that needs to be generated
     * @param s      The character pool from which the string is generated
     * @return String
     */
    public static String getRandomStr(int length, String s) {

        String base = "";

        if (StringUtils.isEmpty(s)) {
            base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        } else {
            base = s;
        }

        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();

    }

    public static EmployeeInfo getLoginInfo(HttpServletRequest request){
        EmployeeInfo loginInfo = (EmployeeInfo) request.getAttribute(Constants.USER_CACHE);
        return loginInfo;
    }


    public static LocalDate dateToLocalDate(Date date) {
        if (date==null){
            return null;
        }
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
}
