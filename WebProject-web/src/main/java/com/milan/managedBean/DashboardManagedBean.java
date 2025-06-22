/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.managedBean;

import com.milan.entities.Product;
import com.milan.sessionBeans.LoginRegisterSessionBean;
import com.milan.sessionBeans.ProductSessionBean;
import com.milan.sessionBeans.placeOrderSessionBean;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
/**
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;

 *
 * @author milan
 */
@ViewScoped
@ManagedBean
@Getter
@Setter
public class DashboardManagedBean {
  @EJB
ProductSessionBean productSessionBean;
  @EJB
  LoginRegisterSessionBean loginRegisterSessionBean;
  @EJB
  placeOrderSessionBean placeOrderSessionBean;
  private List<Product> productList;
  private String quantity;
 private String imageFolder;
  
  public void fetchProduct(){
      productList=productSessionBean.fetchProduct();
      imageFolder="images/";
  }
    public String getImageURLFromDB(String imageFilename) {
        return imageFolder  + imageFilename; 
    }
  public void preRenderDashboard(){
        
      if(!loginRegisterSessionBean.isLoggedIn()){
          try {
              FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
          } catch (IOException ex) {
              Logger.getLogger(DashboardManagedBean.class.getName()).log(Level.SEVERE, null, ex);
          }
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"User Not Logged In",null));
      }
      fetchProduct();
  }
      public boolean isAdmin() {
        return loginRegisterSessionBean.isAdmin();
    }
    public String notVisibleToAdmin(){
        if(isAdmin()){
            return "display:none";
        }else{
            return "";
        }
    }
      public String visibleToAdmin(){
        if(isAdmin()){
            return "";
        }else{
            return "display:none";
        }
    }
    public String getContainerHeight(){
        if(isAdmin()){
            return "height: 270px";
        }else{
            return "";
        }
    }
    public void buyItem(Product product){
        if(Integer.valueOf(product.getQuantity())<Integer.valueOf(this.quantity)){
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Sorry! Only "+product.getQuantity()+" items available!",null));
        }else{
            placeOrderSessionBean.placeOrder(loginRegisterSessionBean.getUserId(), product, new Date(), quantity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success","Order Placed!"));
        }
        this.quantity="";
        
    }
  
}
