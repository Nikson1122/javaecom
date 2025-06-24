/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.sessionBeans;

import com.milan.entities.Contact;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author dell
 */
@Stateless
@Local
public class ContactSessionBean {

    @PersistenceContext(unitName = "web_pu")
    // Replace with your PU name from persistence.xml
    private EntityManager em;

    private Contact contact = new Contact();

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

//    @Transactional
    public void saveContact(String name, String address, String contactNumber) {
//        contact.setId( id);
        contact.setName(name);
        contact.setContactNumber(contactNumber);
        contact.setAddress(address);

        em.persist(contact);

    }

    public List<Contact> fetchAllContact() {
        String sql = "select * from contact";
        Query query = em.createNativeQuery(sql, Contact.class);
        return query.getResultList();
    }

    public Contact updateContact(Long id, String newName, String newAddress, String newcontactNumber) {
        Contact contact = em.find(Contact.class, id);
        if (contact != null) {
            contact.setName(newName);
            contact.setAddress(newAddress);
            contact.setContactNumber(newcontactNumber);
            em.merge(contact);
        }
        return contact;
    }

    public void deleteContact(Long id) {
        Contact contact = em.find(Contact.class, id);
        if (contact != null) {
            em.remove(contact);
        }

    }

    public Contact findContact(Long id) {
        return em.find(Contact.class, id);
    }

    public List<Contact> getByname(String name) {

        String sql = "SELECT * FROM `contact` WHERE name LIKE '" + name + "%'";
        Query query = em.createNativeQuery(sql, Contact.class);
        return query.getResultList();
    }
    
    public List<Contact> getByaddress( String address){
        String sql = "SELECT * FROM `contact` WHERE  address LIKE'" +address + "%'";
        Query query = em.createNativeQuery(sql, Contact.class);
        return query.getResultList();
    }

}

//public String submit() {
////    saveContact(contact);
////    contact = new Contact();
//    return null;
//}

