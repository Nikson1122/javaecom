/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.managedBean;



import com.milan.sessionBeans.UserSessionBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author milan
 */
@ManagedBean
@ViewScoped
public class UserManagedBean implements Serializable {
    @EJB
   private UserSessionBean userSessionBean;
   @PostConstruct
   public void init(){
      
   }

}
