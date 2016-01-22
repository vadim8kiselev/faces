package com.kiselev.faces.beans;

import com.kiselev.faces.dao.UserDAO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name = "profileBean")
@ViewScoped
public class ProfileBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public ProfileBean() {
    }

    /* get profile */
    public String getUsernameById(Object userId) {
        try {
            Long id = new Long((String) userId);
            String username = UserDAO.getUsername(id);

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
}
