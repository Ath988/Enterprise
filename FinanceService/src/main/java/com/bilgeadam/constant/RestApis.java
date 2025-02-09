package com.bilgeadam.constant;

public class RestApis {



    private static final String API = "/api";
    private static final String DEV = "/dev";
    private static final String VERSION = "/v1";
    private static final String ROOT = VERSION + DEV;

    public static final String ACCOUNT = ROOT + "/account";
    public static final String TRANSACTION = ROOT + "/transaction";
    public static final String TAX_RECORD = ROOT + "/tax-record";
    public static final String PAYMENT = ROOT + "/payment";
    public static final String INVOICE = ROOT + "/invoice";
    public static final String FINANCIAL_REPORT = ROOT + "/financial-report";


    public static final String SAVE = "/save";
    public static final String DELETE = "/delete";
    public static final String UPDATE = "/update";
    public static final String FIND_ALL = "/findAll";
    public static final String FIND_BY_ID = "/find-by-id";
    public static final String FIND_BY_ACCOUNT_ID ="/find-by-account-id" ;
    public static final String FIND_BY_ACCOUNT_ID_BETWEEN_DATE ="/find-by-id-between-startDate-and-endDate" ;
    public static final String FIND_BY_ALL_INVOICE_DATE ="/find-by-id-and-invoiceDate" ;
}
