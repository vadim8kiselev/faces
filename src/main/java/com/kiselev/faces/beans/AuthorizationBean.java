package com.kiselev.faces.beans;

import com.kiselev.faces.dao.UserDAO;
import com.kiselev.faces.dao.entities.UserEntity;

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

    public String signin() {
        UserEntity user = new UserEntity(username, password);

        if (UserDAO.checkAccount(user)) {
            isLogged = true;
            return "profile?faces-redirect=true";
        } else {
            password = null;
            // add message error (Incorrect username or password)
            return null;
        }
    }

    public String signup() {
        if (password.equals(secondPassword)) {
            UserEntity user = new UserEntity(username, password);

            if (!UserDAO.checkUsername(user)) {
                isLogged = true;
                UserDAO.addUser(user);
                return "profile?faces-redirect=true";
            } else {
                username = null;
                password = null;
                secondPassword = null;
                // add message error (Nickname is already taken)
                return null;
            }
        } else {
            // add message error (Passwords aren't match)
            password = null;
            secondPassword = null;
            return null;
        }
    }
}
