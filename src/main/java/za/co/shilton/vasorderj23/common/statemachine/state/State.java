package za.co.shilton.vasorderj23.common.statemachine.state;

public enum State {
    START,
    ORDER_VALIDATION,
    ORDER_VALIDATION_FAILURE,
    QUOTE_VALIDATION,
    QUOTE_VALIDATION_FAILURE,
    LIMITS_VALIDATION,
    LIMIT_VALIDATION_FAILURE,
    PAYMENT,
    PAYMENT_FAILURE,
    PARTNER_ORDER,
    PARTNER_ORDER_FAILURE,
    HISTORY_UPDATE,
    HISTORY_UPDATE_FAILURE,
    NOTIFICATION,
    NOTIFICATION_FAILURE,
    FINISH_ORDER_SUCCESS,
    FINISH_ORDER_FAILURE,
    END,

}


