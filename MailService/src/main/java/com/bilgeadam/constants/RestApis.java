package com.bilgeadam.constants;

public class RestApis {
    public static final String DOMAIN = "http://localhost:9090";


    private static final String VERSION = "/v1";
    private static final String API = "/api";
    private static final String DEVELOPER = "/dev";
    private static final String TEST = "/test";
    private static final String PROD = "/prod";

    private static final String ROOT = VERSION + DEVELOPER;

    public static final String MAIL = ROOT+ "/mail";

    public static final String SEND_MAIL = "/send-mail";

}
