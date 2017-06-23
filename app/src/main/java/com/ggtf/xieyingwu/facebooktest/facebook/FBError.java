package com.ggtf.xieyingwu.facebooktest.facebook;

/**
 * Created by xieyingwu on 2017/6/21.
 * FaceBook error信息
 */

public class FBError {
    private static final int ERROR_CODE_INVALID_PARAMETER = 100;
    private static final int ERROR_CODE_PERMISSIONS_ERROR = 200;
    public int errorCode;
    public String errorType;
    public String errorMessage;

    public boolean isInvalidParameter() {
        return ERROR_CODE_INVALID_PARAMETER == errorCode;
    }

    public boolean isPermissionsError() {
        return ERROR_CODE_PERMISSIONS_ERROR == errorCode;
    }
}
