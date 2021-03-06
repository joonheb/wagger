package com.example.afinal.util;

import java.util.regex.Pattern;

public class RegexHelper {
    // 싱글톤 패턴 시작
    private static RegexHelper instance = null;

    public static RegexHelper getInstance() {
        if (instance == null) instance = new RegexHelper();

        return instance;
    }
    public static void freeInstance() {
        instance = null;
    }
    private RegexHelper() {}
    // 싱글톤 패턴 끝

    // 주어진 문자열이 공백이거나 null인지를 검사
    // 공백, null이 아닐 경우, true 리턴
    public boolean isValue(String str) {
        boolean result = false;
        if(str != null) {
            result = !str.trim().equals("");
        }
        return result;
    }
    // 숫자 모양에 대한 형식 검사
    public boolean isNum(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[0-9]*$", str);
        }
        return result;
    }
    // 영문으로만 구성되었는지에 대한 형식 검사
    public boolean isEng(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[a-zA-Z]*$", str);
        }
        return result;
    }
    // 한글로만 구성되었는지에 대한 형식 검사
    public boolean isKor(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[ㄱ-ㅎ가-힣]*$", str);
        }
        return result;
    }
    // 영문과 숫자로만 구성되었는지에 대한 형식 검사
    public boolean isEngNum(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[a-zA-Z0-9]*$", str);
        }
        return result;
    }
    // 한글과 숫자로만 구성되었는지에 대한 형식 검사
    public boolean isKorNum(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[ㄱ-ㅎ가-힣0-9]*$", str);
        }
        return result;
    }
    // 이메일 형식인지에 대한 검사.
    public boolean isEmail(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", str);
        }
        return result;
    }
    // "-"없이 핸드폰번호인지에 대한 형식 검사
    public boolean isCellPhone(String str) {
        boolean result = false;
        if(isValue(str)) {
            result = Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", str);
        }
        return result;
    }
}
