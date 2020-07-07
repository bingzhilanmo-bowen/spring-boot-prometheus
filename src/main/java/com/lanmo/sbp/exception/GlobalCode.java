package com.lanmo.sbp.exception;

public enum  GlobalCode implements Code {
    SERVER_ERROR(500010001),
    NOT_FOUND_ERROR(404010002),
    RPC_ERROR(600010003),
    SESSION_EXPIRE(401010004),
    BAD_REQUEST_ERROR(400010005);

    GlobalCode(Integer code){
        this.code = code;
    }

    private Integer code;


    public Integer getCode() {
        return this.code;
    }

}
