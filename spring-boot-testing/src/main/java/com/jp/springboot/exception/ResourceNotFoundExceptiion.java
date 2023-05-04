package com.jp.springboot.exception;

public class ResourceNotFoundExceptiion extends RuntimeException{
    public ResourceNotFoundExceptiion(String message){
        super(message);
    }
    public ResourceNotFoundExceptiion(String message, Throwable cause){
        super(message, cause);
    }
}
