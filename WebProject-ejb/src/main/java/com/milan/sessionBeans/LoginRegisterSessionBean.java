/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.sessionBeans;

import com.milan.entities.User;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author milan
 */
@Stateless
@Local
public class LoginRegisterSessionBean {

    @Inject
    private HttpServletRequest request;

    @PersistenceContext(unitName = "web_pu")
    private EntityManager em;

    public void persist(Object o) {
        em.persist(o);
    }

    public void merge(Object o) {
        em.merge(o);
    }

      public String register(String name, String email, String password,String address,String phoneNumber) {
        if (!isEmailAvailable(email)) {
            return "Email Address is already in use. Please enter another valid Email";
        }
         if (!isNumberAvailable(phoneNumber)) {
            return "Phone Number is already in use. Please enter another valid number";
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setActive(true);
        user.setStatus(true);
        user.setContactNumber(phoneNumber);
        user.setAddress(address);
        user.setUserType("2");
        em.persist(user);
        return null;
    }

    public String login(String email, String password) {
        String sql = "Select * from User u where u.email= :email AND u.password= :password";
        Query query = em.createNativeQuery(sql, User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        try {
            User user = (User) query.getSingleResult();
            HttpSession httpSession = request.getSession();
            httpSession.removeAttribute("user");
            httpSession.setAttribute("user", email);
            httpSession.setAttribute("id", user.getId());
            httpSession.setAttribute("name", user.getName());
            httpSession.setAttribute("userType", user.getUserType().equals("1") ? "Admin" : "User");
            return null;
        } catch (NoResultException e) {
            return "Invalid Credentials!";
        }

    }

    public boolean isNumberAvailable(String number) {
        System.out.println(number);
        String sql = "SELECT count(*) FROM User u WHERE u.contact_number = :number";
        try {
            Number count = (Number) em.createNativeQuery(sql)
                    .setParameter("number", number)
                    .getSingleResult();
            if (count.intValue() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.err.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
       public boolean isEmailAvailable(String email) {
        System.out.println(email);
        String sql = "SELECT count(*) FROM User u WHERE u.email = :email";
        try {
            Number count = (Number) em.createNativeQuery(sql)
                    .setParameter("email", email)
                    .getSingleResult();
            if (count.intValue() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.err.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isAdmin() {
        try{
        String type = request.getSession().getAttribute("userType").toString();
        return type.equals("Admin");
        }catch(Exception e){
            System.out.println("Session is Null");
            return false;
        }
    }

    public void logOut() {
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("id");
        request.getSession().removeAttribute("userType");
        request.getSession().removeAttribute("name");
    }
    public boolean isLoggedIn(){
        try{
        return request.getSession().getAttribute("id").toString()!=null;
        }catch(Exception e){
            return false;
        }
    }
    public String getUserName(){
        try{
        return request.getSession().getAttribute("name").toString();
        }catch(Exception e){
            return "Login";
        }
    }
        public String getUserId(){
        try{
        return request.getSession().getAttribute("id").toString();
        }catch(Exception e){
            return "";
        }
    }
      

}
