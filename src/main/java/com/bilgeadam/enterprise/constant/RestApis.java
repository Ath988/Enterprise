package com.bilgeadam.enterprise.constant;

public class RestApis {
	public static final String DOMAIN = "http://localhost:9090";
	
	
	private static final String VERSION = "/v1";
	private static final String API = "/api";
	private static final String DEVELOPER = "/dev";
	private static final String TEST = "/test";
	private static final String PROD = "/prod";
	
	private static final String ROOT = VERSION + DEVELOPER;
	
	public static final String CHAT = ROOT+ "/chat";
	
	public static final String CREATE_GROUP_CHAT = "/create-group-chat";
	public static final String CREATE_PRIVATE_CHAT = "/create-private-chat";
	public static final String SEND_PRIVATE_MESSAGE = "private/{chatId}/sendMessage";
	public static final String SEND_GROUP_MESSAGE = "/group/{chatId}/sendMessage";
	public static final String GET_USERS_CHAT = "/get-chat-list";
	public static final String ADD_USER_CHAT = "/add-user-chat";
	public static final String DELETE_GROUP_CHAT = "/delete-group-chat";
	public static final String DELETE_MESSAGE = "/delete-message";
	public static final String GET_USERS_IN_CHAT = "/get-users-in-chat";
	public static final String UPDATE_CHAT_DETAILS = "/update-chat-details";
	public static final String GET_CHAT_DETAILS = "/get-chat-details";
	
}