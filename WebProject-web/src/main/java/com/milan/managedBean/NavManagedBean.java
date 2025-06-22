/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.managedBean;

import com.milan.sessionBeans.LoginRegisterSessionBean;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author milan
 */
@ManagedBean
@ViewScoped
@Getter
@Setter
public class NavManagedBean {

    @EJB
    LoginRegisterSessionBean loginRegisterSessionBean;
    private String user;

    @Inject
    private HttpServletRequest request;

    @PostConstruct
    public void init(){
        user=loginRegisterSessionBean.getUserName();
    }
    public boolean isAdmin() {
        return loginRegisterSessionBean.isAdmin();
    }

    public void logOut() {
                try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
        } catch (IOException ex) {
                    System.out.println("Cant Redirect");
        }
        loginRegisterSessionBean.logOut();

    }
}
