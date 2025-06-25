/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.managedBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milan.entities.Contact;
import com.milan.sessionBeans.ContactSessionBean;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
     * Retrieves representation of an instance of
     * com.milan.managedBean.GenericResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAll")
    public Response getAllData() throws JsonProcessingException {
        List<Contact> contact = new ArrayList<>();
        contact = sessionBean.fetchAllContact();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(contact);
        return Response.ok(json, MediaType.APPLICATION_JSON).build(); 
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getData/{id}")
    public Response getName(@PathParam("id") Long id) throws JsonProcessingException {
        Contact contact = new Contact();
        contact = sessionBean.findContact(id);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(contact);

        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getName/{name}")
    public Response getByName(@PathParam("name") String name) throws JsonProcessingException {
        List<Contact> contact = new ArrayList<>();
        contact = sessionBean.getByname(name);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(contact);

        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAddress/{address}")
    public Response getByAddress(@PathParam("address") String address) throws JsonProcessingException{
        List<Contact> contact = new ArrayList<>();
        contact = sessionBean.getByaddress(address);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(contact);
        
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
        
    }
    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("addData")
    public String addContact(String data) throws JsonProcessingException{
        Contact c = new Contact();
        ObjectMapper mapper = new ObjectMapper();
        c = mapper.readValue(data,Contact.class);
        sessionBean.saveContact(c.getName(), c.getAddress(), c.getContactNumber());
        return "Data added successfully";
    }
    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateData/{id}")
    public String addinfo(@PathParam("id")Long id ,String data) throws JsonProcessingException{
        Contact c = new Contact();
        ObjectMapper mapper = new ObjectMapper();
        c = mapper.readValue(data, Contact.class);
        sessionBean.updateContact(id, c.getName(), c.getAddress(), c.getContactNumber());
 
        
        return "Data Updated Sucessfully";
        
        
        
    }
    
    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/DeleteData/{id}")
    public String deleteinfo(@PathParam("id")Long id ,String data) throws JsonProcessingException{

        sessionBean.deleteContact(id);
 
        
        return "Data Deleted Sucessfully";
        
        
        
    }
    

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param content representation for the resource
     */
}
