package com.bilgeadam.constant;

public class RestApis {


    private static final String VERSION = "/v1";
    private static final String API = "/api";
    private static final String DEVELOPER = "/dev";
    private static final String TEST = "/test";
    private static final String PROD = "/prod";

    private static final String ROOT = VERSION + DEVELOPER;

    public static final String SUBSCRIPTION = ROOT + "/subscription";
    public static final String PRICING_PLAN = ROOT + "/pricing-plan";
    public static final String SERVICE = ROOT + "/service";
    public static final String IMAGE = ROOT + "/image";
    public static final String FAQ =ROOT + "/faq";
    public static final String ANSWER = ROOT + "/answer";

    public static final String ADD_SUBSCRIPTION = "/add";
    public static final String GET_CURRENT_SUBSCRIPTION = "/current";
    public static final String GET_SUBSCRIPTION_HISTORY = "/history";
    public static final String UPDATE_SUBSCRIPTION = "/update";
    public static final String CANCEL_SUBSCRIPTION = "/cancel";
    public static final String GET_ACTIVE_SUBSCRIPTION = "/active";
    public static final String GET_ALL_PLANS = "/all-plans";
    public static final String UPDATE_PLAN = "/update-plan";
    
    public static final String GET_ALL_SERVICES = "/get-all-services";
    public static final String UPDATE_SERVICE = "/update-service";
 
    public static final String GET_ALL_FAQ = "/get-all-faq";
    public static final String UPDATE_FAQ = "/update-faq";
    public static final String CANCEL_FAQ = "/cancel-faq";
    public static final String CREATE_FAQ = "/create-faq";
    
    public static final String GET_ALL_ANSWERS = "/get-all-answers";
    public static final String UPDATE_ANSWER = "/update-answer";
    public static final String CANCEL_ANSWER = "/cancel-answer";
    public static final String CREATE_ANSWER = "/create-answer";

}