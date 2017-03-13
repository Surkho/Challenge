package com.wifibyteschallenge.android.utils;

import android.text.TextUtils;

/**
 * Created by juanj on 13/03/2017.
 */

public class Utils {

    //ERROR_CODE VALUES
    public static final int ERROR_NETWORK = -24;
    public static final int ERROR_UNKNOW = -999;
    public static final int ERROR_UNAVALAIABLE = -10;

    //FUNCTIONS TO VALIDATE USERNAME AND PASSWORD
    public final static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public final static boolean isValidPassword(CharSequence pass){
        return !TextUtils.isEmpty(pass) && pass.length() > 4;
    }



}
