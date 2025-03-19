package com.bilgeadam.constants;

public class RestApis {

    private static final String API = "/api";
    private static final String DEV = "/dev";
    private static final String VERSION = "/v1";
    private static final String ROOT = VERSION + DEV;

    public static final String EMPLOYEE = ROOT + "/employee";
    public static final String DEPARTMENT = ROOT + "/department";
    public static final String POSITION = ROOT + "/position";
    public static final String ANNOUNCEMENT = ROOT + "/announcement";
    public static final String ANNOUNCEMENT_IS_READ = ROOT + "/announcement-is-read";
    public static final String QUESTION = ROOT + "/question";
    public static final String REPLY = ROOT + "/reply";
    public static final String ASSET = ROOT + "/asset";
    public static final String MAINTENANCE = ROOT + "/maintenance";

    public static final String CREATE_COMPANY_MANAGER = "/create-company-manager";
    public static final String CREATE_ANNOUNCEMENT = "/create-announcement";
    public static final String DELETE_ANNOUNCEMENT = "/delete-announcement";
    public static final String GETALLANNOUNCEMENT = "/get-all-announcement";
    public static final String GETREADANNOUNCEMENT = "/get-read-announcement";
    public static final String GETUNREADANNOUNCEMENT = "/get-unread-announcement";
    public static final String GETALLREADERSANNOUNCEMENT = "/get-all-readers-announcement";
    public static final String ISREAD_ANNOUNCEMENT = "/is-read-announcement";

    public static final String DOREGISTER = "/do-register";

    public static final String REQUEST = "/request";
    public static final String SHIFT = "/shift";
    public static final String CREATE = "/create";
    public static final String GETBYID = "/get-by-id";
    public static final String GETALL = "/get-all";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";

    public static final String GET_ALL_QUESTION = "/get-all-question";
    public static final String GET_QUESTION = "/get-question";
    public static final String ADD_QUESTION = "/add-question";

    public static final String ADD_REPLY = "/add-reply";

    public static final String NOTIFICATIONSENDER = "/notificationSender";
    public static final String SEND_MAIL = "/send-mail";
}