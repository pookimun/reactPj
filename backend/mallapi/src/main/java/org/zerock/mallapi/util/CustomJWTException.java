package org.zerock.mallapi.util;

public class CustomJWTException  extends RuntimeException{
    // JWT 예외처리용
    public CustomJWTException(String msg){
        super(msg);
    }
}
