package com.kiselev.faces.beans;

import com.kiselev.faces.dao.UserDAO;
import com.kiselev.faces.dao.entities.UserEntity;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@ManagedBean(name = "authorizationBean")
@SessionScoped
public class AuthorizationBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean logged = false;
    private String username;
    private String password;
    private String secondPassword;
    private String inMessage;
    private String upMessage;
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String signin() {
        if (!"".equals(username.trim()) && !"".equals(password.trim())) {
            username = username.trim();
            password = password.trim();

            UserEntity user = new UserEntity(username, password);

            if ((id = UserDAO.getId(user)) != null) {
                logged = true;

                return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
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
                    logged = true;
                    id = UserDAO.addUser(user);
                    return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
                } else {
                    upMessage = "This username is already taken";
                    return "/faces/signup.xhtml?faces-redirect=true";
                }
            } else {
                upMessage = "Passwords do not match";
                return "/faces/signup.xhtml?faces-redirect=true";
            }
        } else {

            if ("".equals(username.trim()) || "".equals(password.trim())) {
                upMessage = "This fields cannot be blank";
                return "/faces/signup.xhtml?faces-redirect=true";
            }

            if ("".equals(secondPassword.trim())) {
                upMessage = "Passwords do not match";
                return "/faces/signup.xhtml?faces-redirect=true";
            }

            return "/faces/signup.xhtml?faces-redirect=true";
        }
    }

    public String homePage() {
        if (logged)
            return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
        else
            return "/faces/index.xhtml?faces-redirect=true";
    }

    public String logout() {
        logged = false;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        session.removeAttribute("authorizationBean");
        session.invalidate();
        return "/faces/logout.xhtml?faces-redirect=true";
    }
}
