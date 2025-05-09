package com.bilgeadam.ticketservice.constant;

public class RestApis {


    private static final String VERSION = "/v1";
    private static final String API = "/api";
    private static final String DEVELOPER = "/dev";
    private static final String TEST = "/test";
    private static final String PROD = "/prod";

    private static final String ROOT = VERSION + DEVELOPER;

    public static final String TICKET = ROOT + "/ticket";

    public static final String ADD = "/add";
    public static final String TICKETS_BY_TOKEN =  "/tickets-by-token";
    public static final String ALL_TICKETS = "/all-tickets";
    public static final String RESPOND = "/respond";
    public static final String CANCEL_MY_TICKET = "/cancel-my-ticket";


}
