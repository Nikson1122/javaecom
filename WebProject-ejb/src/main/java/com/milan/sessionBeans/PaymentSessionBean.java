/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.sessionBeans;

import com.milan.entities.Payment;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author dell
 */
@Stateless
public class PaymentSessionBean {
    
    @PersistenceContext(unitName = "web_pu")
    private EntityManager em;
    
    public void savePayment(Payment payment){
        em.persist(payment);
    }
        public List<Payment> getAllPayments() {
        TypedQuery<Payment> query = em.createQuery("SELECT p FROM Payment p ORDER BY p.paymentDate DESC", Payment.class);
        return query.getResultList();
    }

    
    
}
