package com.bilgeadam.constant;

public class RestApis {
    public static final String DOMAIN = "http://localhost:9090";


    private static final String VERSION = "/v1";
    private static final String API = "/api";
    private static final String DEVELOPER = "/dev";
    private static final String TEST = "/test";
    private static final String PROD = "/prod";

    private static final String ROOT = VERSION + DEVELOPER;

    public static final String USER = ROOT+ "/user";


    public static final String DOLOGIN = "/do-login";
    public static final String DOREGISTER = "/do-register";
    public static final String AUTHMAIL = "/auth-mail";
    public static final String NEW_PASSWORD = "/new-password";
    public static final String FORGOT_PASSWORD_MAIL = "/forgot-password-mail";
}
