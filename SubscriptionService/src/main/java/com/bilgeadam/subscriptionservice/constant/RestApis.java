package com.bilgeadam.subscriptionservice.constant;

public class RestApis {
    public static final String DOMAIN = "http://localhost:9100";


    private static final String VERSION = "/v1";
    private static final String API = "/api";
    private static final String DEVELOPER = "/dev";
    private static final String TEST = "/test";
    private static final String PROD = "/prod";

    private static final String ROOT = VERSION + DEVELOPER;

    public static final String SUBSCRIPTION = ROOT + "/subscription";
}
