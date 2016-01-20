package com.kiselev.faces.beans;

import com.kiselev.faces.dao.UserDAO;
import com.kiselev.faces.dao.entities.UserEntity;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
        if (!"".equals(username.trim()) && !"".equals(password.trim())) {
            username = username.trim();
            password = password.trim();

            UserEntity user = new UserEntity(username, password);

            if (UserDAO.checkAccount(user)) {
                isLogged = true;
                return "profile?faces-redirect=true";
            } else {
                password = null;
                FacesContext.getCurrentInstance().addMessage
                        ("authorization-form",
                                new FacesMessage("Incorrect username or " +
                                        "password"));
                return null;
            }
        } else {

            if ("".equals(username.trim())) {
                FacesContext.getCurrentInstance().addMessage
                        ("authorization-form:username",
                                new FacesMessage("Username can't be blank"));
                return null;
            }
            if ("".equals(password.trim())) {
                FacesContext.getCurrentInstance().addMessage
                        ("authorization-form:password",
                                new FacesMessage("Password can't be blank"));
                return null;
            }
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
                    username = null;
                    FacesContext.getCurrentInstance()
                            .addMessage("authorization-form",
                                    new FacesMessage("Nickname is already " +
                                            "taken"));

                    return null;
                }
            } else {
                FacesContext.getCurrentInstance()
                        .addMessage("authorization-form",
                                new FacesMessage("Passwords aren't match"));
                password = null;
                secondPassword = null;
                return null;
            }
        } else {

            if ("".equals(username.trim())) {
                FacesContext.getCurrentInstance().addMessage
                        ("authorization-form:username",
                                new FacesMessage("Username can't be blank"));
                return null;
            }
            if ("".equals(password.trim())) {
                FacesContext.getCurrentInstance().addMessage
                        ("authorization-form:password",
                                new FacesMessage("Password can't be blank"));
                return null;
            }

            if ("".equals(secondPassword.trim())) {
                FacesContext.getCurrentInstance().addMessage
                        ("authorization-form",
                                new FacesMessage("Passwords aren't match"));
                return null;
            }

            return null;
        }
    }
}
