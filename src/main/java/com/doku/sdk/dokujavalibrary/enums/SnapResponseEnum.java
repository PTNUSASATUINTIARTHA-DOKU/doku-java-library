package com.doku.sdk.dokujavalibrary.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SnapResponseEnum {
    // 200
    SUCCESSFUL("00", "Successful"),

    // 400
    INVALID_FIELD_FORMAT("01", "Invalid Field Format "),
    INVALID_MANDATORY_FIELD("02", "Invalid Mandatory Field "),
    TRANSACTION_ALREADY_SUCCESS("03", "Transaction Already Success"),
    INVALID_PARAMETER("04", "Invalid Parameter"),
    BAD_REQUEST("00", "Bad Request"),

    // 401
    UNAUTHORIZED("00", "Unauthorized. "),
    INVALID_CUSTOMER_TOKEN("02", "Invalid Customer Token"),
    INVALID_ADDITIONAL_INFO_VALUE("01","Invalid Additional Info Data"),

    // 403,
    TRANSACTION_NOT_PERMITTED("15", "Transaction Not Permitted. "),

    // 404
    TRANSACTION_NOT_FOUND("01", "Transaction Not Found"),
    INVALID_ROUTING("02", "Invalid Routing"),
    JOURNEY_NOT_FOUND("07", "Journey Not Found"),
    INVALID_MERCHANT("08", "Invalid Merchant"),
    INVALID_CUSTOMER("11", "Invalid Customer. "),
    INVALID_CARD("11", "Invalid Card"),
    INVALID_AMOUNT("13", "Invalid Amount"),
    INCONSISTENT_REQUEST("18", "Inconsistent Request"),

    // 409
    DUPLICATE_EXTERNAL_ID("00", "Conflict"),
    INVALID_UUID("00", "Conflict"),

    // 500
    GENERAL_ERROR("00", "General Error"),
    INTERNAL_SERVER_ERROR("01", "Internal Server Error"),
    CONNECTION_FAILED("00", "Connection Failed"),
    OTP_SENT_TO_CARDHOLDER("13", "OTP Sent To Cardholder"),

    // 504
    TIMEOUT("00", "Timeout");

    private final String responseCode;
    private final String responseMessage;
}
