package com.kiselev.faces.beans;

import com.kiselev.faces.dao.DAO;
import com.kiselev.faces.dao.entities.ProfileEntity;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;
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

    private void resetData() {
        username = null;
        password = null;
        secondPassword = null;
        inMessage = null;
        upMessage = null;
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

            ProfileEntity user = new ProfileEntity(username, password);

            if ((id = DAO.getId(user)) != null) {
                logged = true;
                resetData();
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
                ProfileEntity user = new ProfileEntity(username, password);

                try {
                    id = DAO.addUser(user);
                    logged = true;
                    resetData();
                    return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
                } catch (PersistenceException e) {
                    upMessage = "This username is already taken";
                    return "/faces/index.xhtml?faces-redirect=true";
                }
            } else {
                upMessage = "Passwords do not match";
                return "/faces/index.xhtml?faces-redirect=true";
            }
        } else {

            if ("".equals(username.trim()) || "".equals(password.trim())) {
                upMessage = "This fields cannot be blank";
                return "/faces/index.xhtml?faces-redirect=true";
            }

            if ("".equals(secondPassword.trim())) {
                upMessage = "Passwords do not match";
                return "/faces/index.xhtml?faces-redirect=true";
            }

            return "/faces/index.xhtml?faces-redirect=true";
        }
    }

    public String homePage() {
        if (logged)
            return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
        else
            return "/faces/signin.xhtml?faces-redirect=true";
    }

    public String logout() {
        logged = false;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        session.removeAttribute("authorizationBean");
        session.invalidate();
        return "/faces/signin.xhtml?faces-redirect=true";
    }
}
