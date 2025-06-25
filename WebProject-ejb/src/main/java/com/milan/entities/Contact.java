/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.entities;
import dtos.ContactDto;
import javax.persistence.Column;

import java.io.Serializable;
import java.sql.DriverManager;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

/**
 *
 * @author dell
 */
 @Entity

@Table(name = "contact")
 @SqlResultSetMapping(
  name = "ContactDtoMapping",
    classes = @ConstructorResult(
        targetClass =ContactDto.class,
        columns = {
            @ColumnResult(name = "name", type = String.class),
            @ColumnResult(name = "address", type = String.class)
        }
    )
)
@NamedNativeQuery(
    name =  "Samparka.getContactDto",
    query = "SELECT name, address FROM contact",
    resultSetMapping = "ContactDtoMapping"
)

public class Contact implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    
     private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "contact", length = 20)
    private String contactNumber;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
   
    }

