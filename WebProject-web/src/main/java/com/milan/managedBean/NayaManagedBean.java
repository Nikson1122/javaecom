/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.managedBean;

import com.milan.entities.Contact;
import com.milan.sessionBeans.ContactSessionBean;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;


@ManagedBean(name = "nayaManagedBean")
//@SessionScoped

//@Named
@ViewScoped 

public class NayaManagedBean implements Serializable {
    @EJB
    ContactSessionBean sessionBean = new ContactSessionBean();
    private Long id; 
    private String name;
    private String address;
    private String contact;
    private boolean submitted = false;
    private long find;

    public long getFind() {
        return find;
    }

    public void setFind(long find) {
        this.find = find;
    }
    
    
    
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public boolean isSubmitted() { return submitted; }
    public void setSubmitted(boolean submitted) { this.submitted = submitted; }
    
    
  

    private List<Contact> contactsList = new ArrayList<>();
    
    public List<Contact>getContactList(){
        
    return contactsList;

  
    }
    
    public void setContactList(List<Contact> contactsList){
        this.contactsList = contactsList;
    }


//    public void submit() {
//        submitted = true;
//        System.out.println("Submitted: " + name + ", " + address + ", " + contact);
//         
//    }
//if we want to do with session scope
//    public String submit(){
//        submitted = true;
//        System.out.println("Submitted:" + name + "," + address + "," + contact);
//        return "naya?faces-redirect=true";
//        
//        
//    }
//    data passing through url encoding in view seesion
    
//public String submit() throws UnsupportedEncodingException {
//    return "naya.xhtml?faces-redirect=true&includeViewParams=true&name=" + 
//           URLEncoder.encode(name, "UTF-8") + 
//           "&address=" + URLEncoder.encode(address, "UTF-8") +
//           "&contact=" + URLEncoder.encode(contact, "UTF-8");
//}
    
public String submit(){
   ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
   ec.getFlash().put("name", name);
   ec.getFlash().put("address", address);
   ec.getFlash().put("contact", contact);
   if(id == null){
       
   
   sessionBean.saveContact(name, address, contact);
 
   }
   else{
       sessionBean.updateContact(id, name, address, contact);
   }
     return "naya?faces-redirect=true";
}

   public String fetch(){
   this.contactsList = sessionBean.fetchAllContact();
   return "";
}
public String edit(Contact selectedContact){
    id=selectedContact.getId();
    name=selectedContact.getName();
    address=selectedContact.getAddress();
    contact=selectedContact.getContactNumber();
    System.out.println("selectedContact "+selectedContact.getName());
//  sessionBean.updateContact(id, name, address, contact);
     return "new.xhtml?faces-redirect=true"; 
//     sessionBean.updateContact(id, name, address, contact);
}

public String delete(long did){
    sessionBean.deleteContact(did);
     return "naya.xhtml?faces-redirect=true"; 
    
}

public void findData(){
  Contact c =  sessionBean.findContact(find);
  this.name = c.getName();
  this.address = c.getAddress();
  this.contact = c.getContactNumber();
  contactsList = new ArrayList<>();
  contactsList.add(c);
//  ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//   ec.getFlash().put("name", name);
//   ec.getFlash().put("address", address);
//   ec.getFlash().put("contact", contact);
//  return "naya.xhtml?faces-redirect=true";
  
}

public void populateData(){
    this.contactsList = sessionBean.fetchAllContact();
}
    
}
// * @author dell
// */
//
//@ManagedBean
//@SessionScoped
//public class NayaManagedBean {
//    private String name;
//    private String address;
//    private String contact;
// 
//    
// 
//    
//    public String getName(){
//        return name;}
//        
//    public void setName(String name){this.name = name;}
//  
//    
//    public String getAddress(){
//        return address;
//    }
//    
//    public void SetAddress(String address) {this.address = address;}
//    
//   
//    
//    public String getContact(){
//        return contact;
//    }
//    
//    public void SetContact(String contact){this.contact = contact;}
//        
//  
//  public String submit() {
//        
//        System.out.println("Submitted: " + name + ", " + address + ", " + contact);
//
//        return "display";  // Navigate to display.xhtml page
//    }
//}
