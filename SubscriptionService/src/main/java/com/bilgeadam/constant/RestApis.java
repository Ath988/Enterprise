package com.bilgeadam.constant;

public class RestApis {
    public static final String DOMAIN = "http://localhost:9100";


    private static final String VERSION = "/v1";
    private static final String API = "/api";
    private static final String DEVELOPER = "/dev";
    private static final String TEST = "/test";
    private static final String PROD = "/prod";

    private static final String ROOT = VERSION + DEVELOPER;

    public static final String SUBSCRIPTION = ROOT + "/subscription";

    public static final String ADD_SUBSCRIPTION = "/add";
    public static final String GET_CURRENT_SUBSCRIPTION = "/get-current";
    public static final String GET_SUBSCRIPTION_HISTORY = "/get-history";
    public static final String UPDATE_SUBSCRIPTION = "/update";
}
