package com.kiselev.faces.beans;

import com.kiselev.faces.dao.DAO;
import com.kiselev.faces.dao.entities.ProfileEntity;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

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

    public AuthorizationBean() {

    }

    private void resetData() {
        username = null;
        password = null;
        secondPassword = null;
        inMessage = null;
        upMessage = null;
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
        if (!"".equals(username.trim()) && !"".equals(password.trim())) {
            username = username.trim();
            password = password.trim();

            ProfileEntity user = new ProfileEntity(username, password);

            if ((id = DAO.getId(user)) != null) {
                logged = true;
                resetData();
                registered = DAO.isRegistered(id);
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
                    return "/faces/register.xhtml?faces-redirect=true";
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

    public String register() {
        if (!"".equals(firstName.trim()) && !"".equals(lastName.trim())) {
            firstName = firstName.trim();
            lastName = lastName.trim();
            if (photo != null && !"".equals(photo.trim())) {
                photo = photo.trim();
                try {
                    checkPhoto();

                } catch (IOException error) {
                    photo = null;
                    upMessage = error.getMessage();
                    return "/faces/register.xhtml?faces-redirect=true";
                }
            }
            DAO.register(id, firstName, lastName, photo);
            registered = true;
            return "/faces/profile.xhtml?faces-redirect=true&id=" + id;
        } else {
            upMessage = "Full name cannot be blank";
            return "/faces/register.xhtml?faces-redirect=true";
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

    private void checkPhoto() throws IOException, IllegalArgumentException {
        if (!photo.matches("^(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;" +
                "]*[-a-zA-Z0-9+&@#/%=~_|]")) {
            throw new MalformedURLException("Invalid url");
        }

        URL url = new URL(photo);
        BufferedImage image;
        try {
            image = ImageIO.read(url);
        } catch (IOException error) {
            throw new IOException("Invalid url");
        }

        if (image == null) {
            throw new IOException("Invalid url");
        }

        if (image.getWidth() > 200 || image.getHeight() > 300) {
            throw new IOException("It must be less than 200x300");
        }
    }
}
