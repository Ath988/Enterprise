package com.bilgeadam.constant;

public class RestApis {


    private static final String DEV = "/dev";
    private static final String VERSION = "/v1";
    public static final String MICROSERVICE = "/finance";
    private static final String ROOT = DEV + VERSION + MICROSERVICE;

//*********************************************************************************

    public static final String ACCOUNT = ROOT + "/account";
    public static final String TRANSACTION = ROOT + "/transaction";
    public static final String TAX_RECORD = ROOT + "/tax-record";
    public static final String PAYMENT = ROOT + "/payment";
    public static final String INVOICE = ROOT + "/invoice";
    public static final String FINANCIAL_REPORT = ROOT + "/financial-report";

//*********************************************************************************

    public static final String SAVE_ACCOUNT = "/add-account";
    public static final String DELETE_ACCOUNT ="/delete-account";
    public static final String UPDATE_ACCOUNT =  "/update-account";
    public static final String GET_ALL_ACCOUNTS = "/get-all-accounts";
    public static final String GET_ACCOUNT_BY_ID ="/get-account-by-id";
    public static final String GET_ACCOUNT_BY_ACCOUNT_NUMBER =  "/get-account-by-account-number";
    public static final String GET_ACCOUNT_BY_COMPANY_NAME = "/get-account-by-company-name";

//************************************************************************************

    public static final String SAVE_INVOICE =  "/add-invoice";
    public static final String DELETE_INVOICE =  "/delete-invoice";
    public static final String UPDATE_INVOICE= "/update-invoice";
    public static final String GET_ALL_INVOICES =  "/get-all-invoices";
    public static final String GET_INVOICE_BY_ID = "/get-invoice-by-id";
    public static final String FIND_BY_ALL_INVOICE_DATE = "/find-by-id-and-invoiceDate";

//************************************************************************************

    public static final String SAVE_PAYMENT = "/add-payment";
    public static final String DELETE_PAYMENT =  "/delete-payment";
    public static final String UPDATE_PAYMENT=  "/update-payment";
    public static final String GET_ALL_PAYMENTS =  "/get-all-payments";
    public static final String GET_PAYMENT_BY_ID =  "/get-payment-by-id";
    public static final String GET_PAYMENT_BY_ACCOUNT_ID = "/get-payment-by-account-id";
    public static final String GET_PAYMENT_BY_INVOICE_ID ="/get-payment-by-invoice-id";
    public static final String GET_PAYMENT_BY_STATUS_IS_PAID =  "/get-payment-by-status-is-paid";

//***********************************************************************************

    public static final String SAVE_TAX_RECORD = "/add-tax-record";
    public static final String DELETE_TAX_RECORD =  "/delete-tax-record";
    public static final String UPDATE_TAX_RECORD=  "/update-tax-record";
    public static final String GET_ALL_TAX_RECORD =  "/get-all-tax-records";
    public static final String GET_TAX_RECORD_BY_ID =  "/get-tax-record-by-id";
    public static final String GET_TAX_RECORD_BY_ACCOUNT_ID =  "/get-tax-record-by-account-id";

//***********************************************************************************

    public static final String SAVE_TRANSACTION =  "/add-transaction";
    public static final String DELETE_TRANSACTION =  "/delete-transaction";
    public static final String UPDATE_TRANSACTION=   "/update-transaction";
    public static final String GET_ALL_TRANSACTIONS = "/get-all-transactions";
    public static final String GET_TRANSACTION_BY_ID = "/get-transaction-by-id";
    public static final String GET_TRANSACTION_BY_ACCOUNT_ID ="/get-transaction-by-account-id";
    public static final String GET_TRANSACTION_BETWEEN_START_DATE_AND_END_DATE =  "/get-transaction-between-start-date-and-end-date";
    public static final String GET_TRANSACTION_BY_TYPE =  "/get-transaction-by-type";
    public static final String GET_TOTAL_INCOME = "/get-total-income";
    public static final String GET_TOTAL_EXPENSE = "/get-total-expense";
    public static final String GET_NET_PROFIT = "/get-net-profit";
    public static final String GET_TRANSACTION_BY_CATEGORY =  "/get-transaction-by-category";

//************************************************************************************

    public static final String SAVE_FINANCIAL_REPORT =  "/add-financial-report";
    public static final String DELETE_FINANCIAL_REPORT =  "/delete-financial-report";
    public static final String UPDATE_FINANCIAL_REPORT=   "/update-financial-report";
    public static final String GET_ALL_FINANCIAL_REPORT =  "/get-all-financial-report";
    public static final String GET_FINANCIAL_REPORT_BY_ID =  "/get-financial-report-by-id";
    public static final String FIND_BY_ACCOUNT_ID_BETWEEN_DATE ="/reports/{accountId}/between";
    public static final String FIND_BY_ACCOUNT_ID ="/reports/{accountId}";
}