package com.kiselev.faces.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name = "authorizationBean")
@SessionScoped
public class AuthorizationBean implements Serializable {

    private static final long serialVersionUID = 1L;
    public boolean isLogged = false;
    private String username;
    private String password;
    private String secondPassword;

    public AuthorizationBean() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecondPassword() {
        return secondPassword;
    }

    public void setSecondPassword(String secondPassword) {
        this.secondPassword = secondPassword;
    }

    public String login() {
        // add database checking
        if (true) {
            isLogged = true;
            return "profile?faces-redirect=true";
        } else {
            return "signin?faces-redirect=true";
        }
    }

    public String signup() {
        // add database checking
        if (true) {
            isLogged = true;
            return "profile?faces-redirect=true";
        } else {
            return "signup?faces-redirect=true";
        }
    }
}
