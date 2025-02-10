package com.bilgeadam.constant;

public class RestApis {
    public static final String DOMAIN = "http://localhost:9090";


    private static final String VERSION = "/v1";
    private static final String API = "/api";
    private static final String DEVELOPER = "/dev";
    private static final String TEST = "/test";
    private static final String PROD = "/prod";

    private static final String ROOT = VERSION + DEVELOPER;

    public static final String AUTH = ROOT+ "/auth";


    public static final String DOLOGIN = "/do-login";
    public static final String DOREGISTER = "/do-register";
    public static final String AUTHMAIL = "/auth-mail";
    public static final String NEW_PASSWORD = "/new-password";
    public static final String FORGOT_PASSWORD_MAIL = "/forgot-password-mail";
    public static final String UPDATEPROFILE = "/update-profile";
    public static final String UPDATEPASSWORD = "/update-password";
    public static final String GETPROFILE = "/user-get-profile";

    public static final String SEND_MAIL = "/send-mail";

    public static final String CREATE_COMPANY_MANAGER = "/create-company-manager";
}
