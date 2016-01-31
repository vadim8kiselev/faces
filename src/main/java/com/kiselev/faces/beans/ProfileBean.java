package com.kiselev.faces.beans;

import com.kiselev.faces.dao.DAO;
import com.kiselev.faces.dao.entities.ProfileEntity;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name = "profileBean")
@RequestScoped
public class ProfileBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String fullName;
    private java.util.Date birthday;
    private String hometown;
    private String email;
    private String phone;
    private String photo;

    public ProfileBean() {
    }

    @PostConstruct
    public void init() {
        id = new Long(FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("id"));

        if (!DAO.isValidId(id)) {
            FacesContext.getCurrentInstance()
                    .getApplication().getNavigationHandler()
                    .handleNavigation(FacesContext.getCurrentInstance(), null,
                            "/faces/error.xhtml?faces-redirect=true");
        } else {
            ProfileEntity model = DAO.getProfile(id);
            if (model != null) {
                this.username = model.getUsername();
                this.fullName = model.getFirstName() +
                        ((model.getLastName() == null) ?
                                "" :
                                " " + model.getLastName());
                this.birthday = model.getBirthday();
                this.hometown = model.getHometown();
                this.email = model.getEmail();
                this.phone = model.getPhone();
                this.photo = model.getPhoto();
            } else {
                FacesContext.getCurrentInstance()
                        .getApplication().getNavigationHandler()
                        .handleNavigation(FacesContext.getCurrentInstance(),
                                null,
                                "/faces/error.xhtml?faces-redirect=true");
            }
        }
    }

    public boolean informationIsEmpty() {
        return ((username != null) || (birthday != null) || (hometown !=
                null) ||
                (email != null) || (phone != null));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return fullName;
    }

    public void setFirstName(String firstName) {
        this.fullName = firstName;
    }

    public String getPhoto() {
        return (photo == null) ? "" : photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public java.util.Date getBirthday() {
        return birthday;
    }

    public void setBirthday(java.util.Date birthday) {
        this.birthday = birthday;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
