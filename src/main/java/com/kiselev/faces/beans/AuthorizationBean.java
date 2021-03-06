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
    private String urlname;

    private ProfileEntity model;

    private Locale locale = new Locale(System.getProperty("user.language"),
            System.getProperty("user.country"));

    public AuthorizationBean() {

    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isRegistered() {
        return registered;
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

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        this.locale = new Locale(language);
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

    public ProfileEntity getModel() {
        return model;
    }

    public void setModel(ProfileEntity model) {
        this.model = model;
    }

    public String signin() {

        if ((inMessage = Validator
                .fieldsAreNotBlank(locale, username, password)) == null) {

            ProfileEntity user = new ProfileEntity(username, password);

            if ((inMessage = Validator
                    .validationSignInId(locale, id = DAO.getId(user))) == null) {
                this.model = DAO.getProfile(id);

                login();

                String[] localeArgs = model.getLanguage().split("_");
                locale = new Locale(localeArgs[0], localeArgs[1]);

                registered = model.getFirstName() != null &&
                        !model.getFirstName().equals("");

                urlname = model.getUrlName();
                if (registered && !urlname.equals(""))
                    return "/faces/profile.xhtml?faces-redirect=true&urlname=" + urlname;
                else
                    return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
            } else {

                return null;
            }
        } else {
            return null;
        }
    }

    public String signup() {

        if ((upMessage = Validator.fieldsAreNotBlank(locale, username,
                password, secondPassword)) == null) {

            if ((upMessage = Validator.validationUsername(locale, username)) != null) {
                return null;
            }

            if ((upMessage = Validator.validationPassword(locale, password)) != null) {
                return null;
            }

            if ((upMessage = Validator
                    .passwordsAreEquals(locale, password, secondPassword)) == null) {

                ProfileEntity user = new ProfileEntity(username, password);
                user.setLanguage(new Locale(System.getProperty("user.language"),
                        System.getProperty("user.country")).toString());

                if ((upMessage = Validator
                        .validationSignUpId(locale, id = DAO.addUser(user))) == null) {
                    this.model = DAO.getProfile(id);
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

        if ((upMessage = Validator.fieldsAreNotBlank(locale, model.getFirstName(),
                model.getLastName())) == null) {

            if (Validator.fieldsAreNotBlank(locale, model.getPhoto()) == null &&
                    (upMessage = Validator.validationPhoto(locale, model.getPhoto())) != null) {

                model.setPhoto("");
                return null;
            }
            DAO.updateProfile(model);

            registered = true;
            return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
        } else {
            return null;
        }
    }

    public void updateModel() {
        model = DAO.getProfile(id);
    }

    public String homePage() {
        if (logged)
            if (model.getUrlName() != null && !model.getUrlName().trim().equals(""))
                return "/faces/profile.xhtml?faces-redirect=true&urlname=" + model.getUrlName();
            else
                return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
        else
            return "/faces/signin.xhtml?faces-redirect=true";
    }

    public String settings() {
        if (logged)
            return "/faces/settings.xhtml?faces-redirect=true";
        else
            return "/faces/signin.xhtml?faces-redirect=true";
    }

    public String save() {
        String[] localeArgs = model.getLanguage().split("_");
        locale = new Locale(localeArgs[0], localeArgs[1]);

        if ((inMessage = Validator.validationFullName(locale,
                model.getFirstName(),
                model.getLastName())) != null) {
            return null;
        }

        if (!"".equals(model.getUrlName()) && !model.getUrlName().equals(urlname)) {
            if (DAO.isValidUrlName(model.getUrlName())) {
                inMessage = Validator.getMessage(locale, "error_urlname_taken");
                return null;
            }

            if ((inMessage = Validator.validationUrlName(locale, model.getUrlName())) != null) {
                return null;
            }
        }

        DAO.updateProfile(model);
        urlname = model.getUrlName();

        if (!"".equals(urlname))
            return "/faces/profile.xhtml?faces-redirect=true&urlname=" + urlname;
        else
            return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
    }

    public String logout() {
        logged = false;
        model = null;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        session.removeAttribute("authorizationBean");
        session.invalidate();
        return "/faces/signin.xhtml?faces-redirect=true";
    }

    public String delete() {
        logout();
        DAO.deleteProfile(id);
        return "/faces/index.xhtml?faces-redirect=true";
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
