package com.milan.sessionBeans;

import com.milan.entities.Barchart;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import javax.annotation.security.PermitAll;

@Stateless
@PermitAll
public class SalesServiceBean {

    @PersistenceContext(unitName = "web_pu")
    private EntityManager em;

    public List<Barchart> getAllSales() {
        return em.createQuery("SELECT s FROM Barchart s ORDER BY s.salesDate DESC", Barchart.class)
                .getResultList();
    }
    
   public List<String> getDistinctNames() {
        return em.createQuery("SELECT DISTINCT s.purchasedBy FROM Barchart s", String.class)
                 .getResultList();
    }
}



