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
    public static final String TASK = "/task";
    public static final String USER = "/user";


    //methods

    public static final String SAVE = "/save";
    public static final String DELETE = "/delete";
    public static final String UPDATE = "/update";
    public static final String FIND_ALL = "/find-all";
    public static final String FIND_BY_ID = "/find-by-id";


    public static final String ADD_USER_TO_TASK = "/add-user-to-task";
    public static final String ADD_TASK_TO_PROJECT = "/add-task-to-project";
}
