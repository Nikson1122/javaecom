/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.managedBean;

import com.milan.sessionBeans.ShedularBean;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author dell
 */
@ManagedBean
@RequestScoped



public class ShedularMangedBean {
    
   @EJB
   private ShedularBean shedularBean;

   public String getLastExecutionTime() {
       return shedularBean.getLastExecutionTime();
   }
}
