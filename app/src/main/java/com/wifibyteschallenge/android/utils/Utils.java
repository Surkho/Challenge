package com.wifibyteschallenge.android.utils;

import android.text.TextUtils;

/**
 * Created by juanj on 13/03/2017.
 */

public class Utils {

    public final static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public final static boolean isValidPassword(CharSequence pass){
        return !TextUtils.isEmpty(pass) && pass.length() > 4;
    }
}
