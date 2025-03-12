package com.bilgeadam.constant;

public class Apis {
	private static final String VERSION = "/v1";
	private static final String DEVELOPER = "/dev";
	private static final String TEST = "/test";
	
	private static final String ROOT = VERSION+DEVELOPER;
	
	public static final String SURVEY=ROOT+"/survey";
	public static final String SURVEY_RESPONSE=ROOT+"/survey-response";
	
	//SURVEY CONTROLLER
	public static final String CREATE_SURVEY="/create-survey";
	public static final String GET_ACTIVE_SURVEYS="/get-active-surveys";
	public static final String GET_SURVEY_DETAILS="/get-survey-details";
	public static final String DELETE_SURVEY="/delete-survey";
	public static final String UPDATE_SURVEY="/update-survey";
	
	//SURVEY RESPONSE CONTROLLER
	public static final String SUBMIT_SURVEY_RESPONSE="/submit-survey-response";
	public static final String GET_USER_RESPONSES="/get-user-responses";
	public static final String GET_RESPONSE_DETAILS="/get-response-details";
	
}