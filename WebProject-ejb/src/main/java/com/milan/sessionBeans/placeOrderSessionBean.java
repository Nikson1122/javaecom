/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.sessionBeans;

import com.milan.entities.Orders;
import com.milan.entities.Product;
import com.milan.entities.User;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author milan
 */
@Stateless
@LocalBean
public class placeOrderSessionBean {
@PersistenceContext(unitName = "web_pu")
    private EntityManager em; 

public void persist(Object o){
    em.persist(o);
}
public void placeOrder(String userId,Product product,Date date,String quantity){
            Orders orders=new Orders();
            Product productNew=em.find(Product.class, product.getId());
            productNew.setQuantity(String.valueOf(Integer.valueOf(productNew.getQuantity())-Integer.valueOf(quantity)));
            orders.setUserId(em.find(User.class,Long.valueOf(userId) ));
            orders.setProductId(productNew);
            orders.setOrderDate(date);
            orders.setQuantity(quantity);
            orders.setPrice(Double.valueOf(quantity)*product.getPrice());
            em.persist(orders);
}

}
