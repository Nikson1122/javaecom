/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.sessionBeans;

import com.milan.entities.Product;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author milan
 */
@Stateless
@LocalBean
public class ProductSessionBean {
@PersistenceContext(unitName = "web_pu")
    private EntityManager em; 

public List<Product> fetchProduct(){
    String sql="SELECT * FROM `product` p WHERE p.status=1 and p.del_flg=false";
    Query query=em.createNativeQuery(sql, Product.class);
    return query.getResultList();
}
    
}
