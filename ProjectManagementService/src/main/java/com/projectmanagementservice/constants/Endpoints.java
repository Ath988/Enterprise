package com.projectmanagementservice.constants;

public class Endpoints
{

    // version
    public static final String VERSION = "/v1";

    //profiles
    public static final String DEV = "/dev";
    public static final String MICROSERVICE = "/project-management";


    public static final String ROOT = DEV + VERSION + MICROSERVICE;

    //controllers

    public static final String PROJECT = "/project";


    //methods

    public static final String SAVE = "/save";
    public static final String DELETE = "/delete";
    public static final String UPDATE = "/update";
    public static final String FIND_ALL = "/find-all";
    public static final String FIND_BY_ID = "/find-by-id";


}
