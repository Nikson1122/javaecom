/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.managedBean;

import com.milan.entities.User;
import com.milan.sessionBeans.LoginRegisterSessionBean;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Setter;
import lombok.Getter;

/**
 *
 * @author milan
 */
@Getter
@Setter
@ManagedBean
@ViewScoped
public class LoginRegisterManagedBean implements Serializable {

    @EJB
    private LoginRegisterSessionBean loginRegisterSessionBean;
    private boolean registerMode;
    private String fullName;
    private String email;
    private String password;
    private String rePassword;
    private String address;
    private String phoneNumber;

    public void toggleMode() {
        registerMode = !registerMode;
    }

    public String login() {
        String result = loginRegisterSessionBean.login(email, password);
        if (result == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome User:  " + email, null));
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml?faces-redirect=true");
            } catch (IOException ex) {
                Logger.getLogger(LoginRegisterManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result, null));
            return "";
        }
    }

    public String register() {
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password must be at least 8 characters and include at least one uppercase letter, one lowercase letter, and one digit!", null));
            return "";
        }
        if (!password.equals(rePassword)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password and Re-Password Should Match!", null));
            return "";
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter a valid Email Address", null));
            return "";
        }
        if (!phoneNumber.matches("^(97|98)\\d{8}$")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter a valid Phone Number", null));
            return "";
        }
        String result = loginRegisterSessionBean.register(fullName, email, password, address, phoneNumber);
        if (result != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result, null));
            return "";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User " + email + " created!", null));
            this.registerMode = false;
            return "";
        }
    }

  

    //Getter Setter
    public void setRegisterMode(boolean registerMode) {
        this.registerMode = registerMode;
    }

    public boolean isRegisterMode() {
        return registerMode;
    }

}
