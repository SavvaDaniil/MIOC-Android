package com.ds.miocnative.singleton;

public class User {

    private User(){

    }
    private static User instance;

    private User(String JWT) {
        this.JWT = JWT;
    }
    public static User create(){
        if(instance == null){
            instance = new User();
        }
        return instance;
    }
    public static User create(String JWT){
        if(instance == null){
            instance = new User(JWT);
        } else {
            instance.setJWT(JWT);
        }
        return instance;
    }
    public static User get(){
        return instance;
    }
    public static User delete(){
        if(instance != null)instance = null;
        return null;
    }


    private String JWT;

    public String getJWT() {
        return JWT;
    }

    public void setJWT(String JWT) {
        this.JWT = JWT;
    }
}
