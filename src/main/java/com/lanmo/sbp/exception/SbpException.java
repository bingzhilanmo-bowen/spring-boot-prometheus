package com.lanmo.sbp.exception;

public class SbpException extends RuntimeException {
    private Code code;

    private String msg;

    public SbpException(Code code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public Code getCode() {
        return code;
    }

    @Override
    public String getMessage(){
        return msg;
    }
}
