/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.managedBean;

import com.milan.entities.Contact;
import com.milan.sessionBeans.ContactSessionBean;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author dell
 */
@Path("generic")
@RequestScoped
public class GenericResource {

    @EJB
    ContactSessionBean sessionBean = new ContactSessionBean();
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }
    
    /**
     * Retrieves representation of an instance of com.milan.managedBean.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Contact> getXml() {
        //TODO return proper representation object
        return sessionBean.fetchAllContact();
    }

    @GET
    @Path("get/1")
    @Produces(MediaType.APPLICATION_JSON)
    public Contact getContact(){
        Contact c = new Contact();
        c.setAddress("Kathmandu");
        c.setContactNumber("12345");
        c.setName("Nikson");
        return c;
    }
    
    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
 
}
