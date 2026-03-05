package com.campus.common.constant;

public final class AuthConstants {

    private AuthConstants() {
    }

    public static final String BEARER_PREFIX = "Bearer ";
    
    public static final String USER_ID_HEADER = "X-User-Id";
    
    public static final String AUTHORIZATION_HEADER = "Authorization";
    
    public static final String CONTENT_TYPE_JSON = "application/json";
    
    public static final String CONTENT_TYPE_JSON_UTF8 = "application/json;charset=UTF-8";
    
    public static final String CHARSET_UTF8 = "UTF-8";
    
    public static final String ACCESS_TOKEN_TYPE = "access";
    
    public static final String REFRESH_TOKEN_TYPE = "refresh";
}
