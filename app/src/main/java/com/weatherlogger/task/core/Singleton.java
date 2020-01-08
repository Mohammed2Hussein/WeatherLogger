package com.weatherlogger.task.core;

public class Singleton {
    private static Singleton instance;
    private Cache cache;

    private Singleton(){
        cache = new Cache();
    }

    public static Singleton getInstance() {
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    public Cache getCache() {
        return cache;
    }
}
