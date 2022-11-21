package org.sydnik.by.framework.utils;

public class Logger {
    public static void info(Class clazz, String message){
        org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(clazz);
        log.info(message);
    }
    public static void trace(Class clazz, String message){
        org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(clazz);
        log.trace(message);
    }
    public static void error(Class clazz, String message){
        org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(clazz);
        log.error(message);
    }
}
