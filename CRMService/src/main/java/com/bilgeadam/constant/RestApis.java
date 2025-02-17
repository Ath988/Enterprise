package com.bilgeadam.constant;

public class RestApis {
	private static final String VERSION = "/v1";
	private static final String API = "/api";
	private static final String DEVELOPER = "/dev";
	private static final String TEST = "/test";
	private static final String PROD = "/prod";
	
	private static final String ROOT = VERSION + DEVELOPER;
	
	public static final String CUSTOMER = ROOT + "/customer";
	
	
	public static final String ADD_CUSTOMER = "/add-customer";
	public static final String GET_ALL_CUSTOMERS = "/get-all-customers";
	public static final String GET_CUSTOMER_ID = "/get-customer-id";
	public static final String UPDATE_CUSTOMER = "/update-customer";
	public static final String DELETE_CUSTOMER = "/delete-customer";
	public static final String DELETE_CUSTOMERS = "/delete-customers";
}