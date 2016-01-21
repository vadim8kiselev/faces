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
    private String inMessage;
    private String upMessage;

    public AuthorizationBean() {

    }

    public String getInMessage() {
        return inMessage;
    }

    public void setInMessage(String inMessage) {
        this.inMessage = inMessage;
    }

    public String getUpMessage() {
        return upMessage;
    }

    public void setUpMessage(String upMessage) {
        this.upMessage = upMessage;
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
        if (!"".equals(username.trim()) && !"".equals(password.trim())) {
            username = username.trim();
            password = password.trim();

            UserEntity user = new UserEntity(username, password);

            if (UserDAO.checkAccount(user)) {
                isLogged = true;
                return "profile?faces-redirect=true";
            } else {
                inMessage = "Incorrect username or password";
                return null;
            }
        } else {
            inMessage = "This fields cannot be blank";
            return null;
        }
    }

    public String signup() {
        if (!"".equals(username.trim()) && !"".equals(password.trim())
                && !"".equals(secondPassword.trim())) {
            username = username.trim();
            password = password.trim();
            secondPassword = secondPassword.trim();

            if (password.equals(secondPassword)) {
                UserEntity user = new UserEntity(username, password);

                if (!UserDAO.checkUsername(user)) {
                    isLogged = true;
                    UserDAO.addUser(user);
                    return "profile?faces-redirect=true";
                } else {
                    upMessage = "This username is already taken";
                    return null;
                }
            } else {
                upMessage = "Passwords do not match";
                return null;
            }
        } else {

            if ("".equals(username.trim()) || "".equals(password.trim())) {
                upMessage = "This fields cannot be blank";
                return null;
            }

            if ("".equals(secondPassword.trim())) {
                upMessage = "Passwords do not match";
                return null;
            }

            return null;
        }
    }
}
