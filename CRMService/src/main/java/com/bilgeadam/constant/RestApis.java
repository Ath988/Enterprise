package com.bilgeadam.constant;

public class RestApis {
	private static final String VERSION = "/v1";
	private static final String API = "/api";
	private static final String DEVELOPER = "/dev";
	private static final String TEST = "/test";
	private static final String PROD = "/prod";
	
	private static final String ROOT = VERSION + DEVELOPER;
	
	public static final String CUSTOMER = ROOT + "/customer";
	public static final String CAMPAIGN = ROOT + "/campaign";
	public static final String SALES_OPPORTUNITY = ROOT + "/sales-pportunity";
	public static final String SUPPORT_TICKET = ROOT + "/support-ticket";
	
	
	public static final String ADDCUSTOMER = "/add-customer";
	public static final String ADDCAMPAIGN = "/add-campaign";
	public static final String GETALLCUSTOMERS = ROOT + "/get-all-customers";
	public static final String GETCUSTOMERBYEMAIL = ROOT + "/get-customer-by-email";
	public static final String GETCUSTOMERBYPHONE = ROOT + "/get-customer-by-phone";
	public static final String GETCUSTOMERSBYCOMPANY = ROOT + "/get-customer-by-company";
	public static final String GETCUSTOMERSBYFIRSTNAME = ROOT + "/get-customer-by-first-name";
	public static final String GETCUSTOMERBYFULLNAME = ROOT + "/get-customer-by-full-name";
	public static final String GETCUSTOMERSBYPHONEPREFIX = ROOT + "/get-customer-by-phone-prefix";
	public static final String GETLATESTCUSTOMERS = ROOT + "/get-latest-customers";
	public static final String UPDATECUSTOMERBYEMAIL = ROOT + "/update-customer-by-email";
	public static final String UPDATECUSTOMERBYPHONE = ROOT + "/update-customer-by-phone";
	public static final String DELETECUSTOMERBYEMAIL = ROOT + "/delete-customer-by-email";
	public static final String DELETECUSTOMERBYPHONE = ROOT + "/delete-customer-by-phone";
	
	public static final String GETALLCAMPAIGNS = ROOT + "/get-all-campaigns";
	public static final String GET_CAMPAIGN_BY_ID = ROOT + "/campaign-by-id";
	public static final String UPDATE_CAMPAIGN = ROOT + "/update-campaign";
	public static final String DELETE_CAMPAIGN = ROOT + "/delete-campaign";
	public static final String GET_CAMPAIGN_BY_STATUS = ROOT + "/campaign-by-status";
	public static final String GET_CAMPAIGN_BY_CUSTOMER_ID = ROOT + "/campaign-by-customer-id";
	public static final String GET_CAMPAIGN_STARTING_AFTER = ROOT + "/campaign-starting-after";
	public static final String GET_CAMPAIGN_ENDING_BEFORE = ROOT + "/campaign-ending-before";
	public static final String GET_CAMPAIGN_BY_BUDGET_RANGE = ROOT + "/campaign-by-budget-range";
	
	public static final String ADD_SALES_OPPORTUNITY = ROOT + "/add-sales-opportunity";
	public static final String GET_ALL_SALES_OPPORTUNITIES = ROOT + "/get-all-sales-opportunities";
	public static final String GET_SALES_OPPORTUNITY_BY_ID = ROOT + "/sales-opportunity-by-id";
	public static final String UPDATE_SALES_OPPORTUNITY = ROOT + "/update-sales-opportunity";
	public static final String DELETE_SALES_OPPORTUNITY = ROOT + "/delete-sales-opportunity";
	public static final String GET_SALES_OPPORTUNITIES_BY_CUSTOMER_ID = ROOT + "/sales-opportunities-by-customer-id";
	public static final String GET_SALES_OPPORTUNITIES_BY_CAMPAIGN_ID = ROOT + "/sales-opportunities-by-campaign-id";
	public static final String GET_SALES_OPPORTUNITIES_BY_ESTIMATED_VALUE_RANGE = ROOT + "/sales-opportunities-by-estimated-value-range";
	public static final String GET_SALES_OPPORTUNITIES_BY_STATUS = ROOT + "/sales-opportunities-by-status";
	public static final String GET_SALES_OPPORTUNITIES_BY_CUSTOMER_AND_CAMPAIGN = ROOT + "/sales-opportunities-by-customer-and-campaign";
	
	public static final String ADD_SUPPORT_TICKET  = ROOT + "/add-support-ticket";
	public static final String GET_ALL_SUPPORT_TICKETS = ROOT + "/get-all-support-tickets";
	public static final String GET_SUPPORT_TICKET_BY_ID = ROOT + "/support-ticket-by-id";
	public static final String UPDATE_SUPPORT_TICKET  = ROOT + "/update-support-ticket";
	public static final String DELETE_SUPPORT_TICKET  = ROOT + "/delete-support-ticket";
	public static final String GET_SUPPORT_TICKETS_BY_CUSTOMER_ID = ROOT + "/support-tickets-by-customer-id";
	public static final String GET_SUPPORT_TICKETS_BY_STATUS = ROOT + "/support-tickets-by-status";
	public static final String GET_SUPPORT_TICKETS_BY_CUSTOMER_AND_SUBJECT  = ROOT + "/support-tickets-by-customer-and-subject";
	public static final String GET_LATEST_SUPPORT_TICKETS = ROOT + "/latest-support-tickets";
}