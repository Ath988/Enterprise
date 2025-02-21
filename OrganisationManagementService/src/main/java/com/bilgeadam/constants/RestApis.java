package com.bilgeadam.constants;

public class RestApis {

    private static final String API = "/api";
    private static final String DEV = "/dev";
    private static final String VERSION = "/v1";
    private static final String ROOT = VERSION + DEV;

    public static final String EMPLOYEE = ROOT + "/employee";
    public static final String DEPARTMENT = ROOT + "/department";
    public static final String POSITION = ROOT + "/position";
    public static final String ANNOUNCEMENT = ROOT + "/position";

    public static final String CREATE_COMPANY_MANAGER = "/create-company-manager";
    public static final String CREATE_ANNOUNCEMENT = "/create-announcement";
    public static final String DOREGISTER = "/do-register";
}