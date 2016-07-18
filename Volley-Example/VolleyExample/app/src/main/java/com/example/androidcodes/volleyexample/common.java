package com.example.androidcodes.volleyexample;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by peacock on 10/6/16.
 */
public class common {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static String Server_URL = "http://117.239.214.145:8080/GlobalBoard/";
    public static String SIGNUP = "signUp.htm";
    public static String KEY_EMAIL = "email";
    public static String KEY_PASSWORD = "password";
    public static String KEY_DEVICE_ID = "deviceId";
    public static String KEY_USERNAME = "name";
    public static String KEY_PHONE_NUMBER = "phoneNo";
    public static String KEY_LOGIN_TYPE = "loginType";
    public static String SIGNIN = "signIn.htm";
    private static SharedPreferences sharedpref1;

    public static boolean isValidEmail(String target) {

        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    //    public static String getRegId() {
//        return sharedpref1.getString("regId", "");
//    }
    public static boolean ValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
