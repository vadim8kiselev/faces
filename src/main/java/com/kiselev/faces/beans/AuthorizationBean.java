package com.kiselev.faces.beans;

import com.kiselev.faces.dao.DAO;
import com.kiselev.faces.dao.entities.ProfileEntity;
import com.kiselev.faces.validators.Validator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Locale;

@ManagedBean(name = "authorizationBean")
@SessionScoped
public class AuthorizationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean logged = false;
    private boolean registered = false;

    private String username;
    private String password;
    private String secondPassword;

    private String inMessage;
    private String upMessage;

    private Long id;
    private String firstName;
    private String lastName;
    private String photo;

    private Locale locale = FacesContext.getCurrentInstance()
            .getExternalContext().getRequestLocale();

    public AuthorizationBean() {

    }

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        this.locale = new Locale(language);
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String signin() {
        username = username.trim();
        password = password.trim();

        if ((inMessage = Validator
                .fieldsAreNotBlank(locale, username, password)) == null) {

            ProfileEntity user = new ProfileEntity(username, password);

            if ((inMessage = Validator
                    .validationSignInId(locale, id = DAO.getId(user))) == null) {

                login();
                registered = DAO.isRegistered(id);
                return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String signup() {
        username = username.trim();
        password = password.trim();
        secondPassword = secondPassword.trim();

        if ((upMessage = Validator.fieldsAreNotBlank(locale, username,
                password, secondPassword)) == null) {

            if ((upMessage = Validator.validationUsername(locale, username)) != null) {
                return "/faces/index.xhtml?faces-redirect=true";
            }

            if ((upMessage = Validator.validationPassword(locale, password)) != null) {
                return "/faces/index.xhtml?faces-redirect=true";
            }

            if ((upMessage = Validator
                    .passwordsAreEquals(locale, password, secondPassword)) == null) {

                ProfileEntity user = new ProfileEntity(username, password);

                if ((upMessage = Validator
                        .validationSignUpId(locale, id = DAO.addUser(user))) == null) {

                    login();
                    return "/faces/register.xhtml?faces-redirect=true";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {

            if ((upMessage = Validator
                    .fieldsAreNotBlank(locale, username, password)) != null) {
                return null;
            }

            if ((upMessage = Validator
                    .fieldsAreNotBlank(locale, secondPassword)) != null) {
                return null;
            }

            upMessage = Validator.unhandledError(locale);
            return null;
        }
    }

    public String register() {
        firstName = firstName.trim();
        lastName = lastName.trim();
        photo = photo.trim();

        if ((upMessage = Validator.fieldsAreNotBlank(locale, firstName,
                lastName)) == null) {

            if ((upMessage = Validator
                    .validationFullName(locale, firstName, lastName)) != null) {
                return null;
            }

            if (Validator.fieldsAreNotBlank(locale, photo) == null &&
                    (upMessage = Validator.validationPhoto(locale, photo)) != null) {

                photo = "";
                return null;
            }

            DAO.register(id, firstName, lastName, photo);
            registered = true;
            return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
        } else {
            return null;
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

    private void login() {
        logged = true;
        username = null;
        password = null;
        secondPassword = null;
        inMessage = null;
        upMessage = null;
    }
}
