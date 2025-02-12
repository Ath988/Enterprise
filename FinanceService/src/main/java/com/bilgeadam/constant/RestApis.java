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
//************************************************************************************

    public static final String SAVE_ACCOUNT =  ROOT + "/add-account";
    public static final String DELETE_ACCOUNT =  ROOT + "/delete-account";
    public static final String UPDATE_ACCOUNT =  ROOT + "/update-account";
    public static final String GET_ALL_ACCOUNTS = ROOT + "/get-all-accounts";
    public static final String GET_ACCOUNT_BY_ID = ROOT + "/get-account-by-id";
    public static final String GET_ACCOUNT_BY_ACCOUNT_NUMBER = ROOT + "/get-account-by-account-number";
    public static final String GET_ACCOUNT_BY_COMPANY_NAME = ROOT + "/get-account-by-company-name";

    //************************************************************************************
    public static final String SAVE_INVOICE =  ROOT + "/add-invoice";
    public static final String DELETE_INVOICE =  ROOT + "/delete-invoice";
    public static final String UPDATE_INVOICE=  ROOT + "/update-invoice";
    public static final String GET_ALL_INVOICES = ROOT + "/get-all-invoices";
    public static final String GET_INVOICE_BY_ID = ROOT + "/get-invoice-by-id";
    public static final String FIND_BY_ALL_INVOICE_DATE = ROOT + "/find-by-id-and-invoiceDate" ;
    //************************************************************************************

    public static final String SAVE_PAYMENT =  ROOT + "/add-payment";
    public static final String DELETE_PAYMENT =  ROOT + "/delete-payment";
    public static final String UPDATE_PAYMENT=  ROOT + "/update-payment";
    public static final String GET_ALL_PAYMENTS = ROOT + "/get-all-payments";
    public static final String GET_PAYMENT_BY_ID = ROOT + "/get-payment-by-id";

    //**public static final String SAVE_TAX_RECORD =  ROOT + "/add-tax-record";
    public static final String DELETE_TAX_RECORD =  ROOT + "/delete-tax-record";
    public static final String UPDATE_TAX_RECORD=  ROOT + "/update-tax-record";
    public static final String GET_ALL_TAX_RECORD = ROOT + "/get-all-tax-records";
    public static final String GET_TAX_RECORD_BY_ID = ROOT + "/get-tax-record-by-id";

    //************************************************************************************
    public static final String SAVE_TRANSACTION =  ROOT + "/add-transaction";
    public static final String DELETE_TRANSACTION =  ROOT + "/delete-transaction";
    public static final String UPDATE_TRANSACTION=  ROOT + "/update-transaction";
    public static final String GET_ALL_TRANSACTIONS = ROOT + "/get-all-transactions";
    public static final String GET_TRANSACTION_BY_ID = ROOT + "/get-transaction-by-id";


    //************************************************************************************
    public static final String SAVE =  ROOT + "/save";
    public static final String DELETE = ROOT +  "/delete";
    public static final String UPDATE = ROOT +  "/update";
    public static final String FIND_ALL =  ROOT + "/findAll";
    public static final String FIND_BY_ID = ROOT +  "/find-by-id";
    public static final String FIND_BY_ACCOUNT_ID = ROOT + "/find-by-account-id" ;
    public static final String FIND_BY_ACCOUNT_ID_BETWEEN_DATE = ROOT + "/find-by-id-between-startDate-and-endDate";

}