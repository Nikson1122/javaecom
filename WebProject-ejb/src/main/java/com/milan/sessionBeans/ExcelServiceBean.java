package com.milan.sessionBeans;

import com.milan.entities.Customer;
import com.milan.entities.Organization;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ExcelServiceBean {

    @PersistenceContext(unitName = "web_pu")
    private EntityManager em;

    // Organization operations
    public Organization createOrganization(Organization organization) {
        em.persist(organization);
        return organization;
    }

    public Organization updateOrganization(Organization organization) {
        return em.merge(organization);
    }

    public void removeOrganization(Long organizationId) {
        Organization org = em.find(Organization.class, organizationId);
        if (org != null) {
            em.remove(org);
        }
    }

    public Organization findOrganization(Long organizationId) {
        return em.find(Organization.class, organizationId);
    }

    public List<Organization> getAllOrganizations() {
        return em.createQuery("SELECT o FROM Organization o", Organization.class)
                .getResultList();
    }

    // Customer operations
//    public Customer createCustomer(Customer customer, Long organizationId) {
//        Organization org = em.find(Organization.class, organizationId);
//        if (org != null) {
//            org.addCustomer(customer);
//            em.persist(customer);
//        }
//        return customer;
//    }
    
    public Customer createCustomer(Customer customer, Long organizationId) {
    if (organizationId != null) {
        Organization org = em.find(Organization.class, organizationId);
        if (org != null) {
            org.addCustomer(customer);
        }
    }
    em.persist(customer);
    return customer;
}

    public Customer updateCustomer(Customer customer) {
        return em.merge(customer);
    }

    public void removeCustomer(Long customerId) {
        Customer cust = em.find(Customer.class, customerId);
        if (cust != null) {
            em.remove(cust);
        }
    }

    public Customer findCustomer(Long customerId) {
        return em.find(Customer.class, customerId);
    }

    public List<Customer> getCustomersByOrganization(Long organizationId) {
        return em.createQuery("SELECT c FROM Customer c WHERE c.organization.id = :orgId", Customer.class)
                .setParameter("orgId", organizationId)
                .getResultList();
    }

    // Relationship management
    public void addCustomerToOrganization(Long customerId, Long organizationId) {
        Customer customer = em.find(Customer.class, customerId);
        Organization org = em.find(Organization.class, organizationId);
        
        if (customer != null && org != null) {
            org.addCustomer(customer);
            em.merge(org);
        }
    }
}