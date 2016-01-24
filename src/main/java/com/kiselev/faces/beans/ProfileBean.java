package com.kiselev.faces.beans;

import com.kiselev.faces.dao.UserDAO;

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

    public ProfileBean() {
    }

    @PostConstruct
    public void init() {
        id = new Long(FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("id"));
    }

    public String getUsername() {
        try {
            username = UserDAO.getUsername(id);

            if (username == null)
                throw new NumberFormatException();

            return username;

        } catch (NumberFormatException error) {
            FacesContext.getCurrentInstance()
                    .getApplication().getNavigationHandler()
                    .handleNavigation(FacesContext.getCurrentInstance(), null,
                            "/faces/error.xhtml?faces-redirect=true");
            return null;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
