/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.sessionBeans;

import com.milan.entities.Category;
import com.milan.entities.Orders;
import com.milan.entities.Product;
import com.milan.entities.User;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author milan
 */
@Stateless
@LocalBean
public class UserSessionBean {

    @PersistenceContext(unitName = "web_pu")
    private EntityManager em;
    @Inject
    private HttpServletRequest request;

    public List<Category> fetchAllCategory() {
        String sql = "Select * from Category c where c.del_flg=false";
        Query query = em.createNativeQuery(sql, Category.class);
        return query.getResultList();
    }

    public List<Orders> fetchAllOrder() {
        String sql = "Select * from Orders ";
        Query query = em.createNativeQuery(sql, Orders.class);
        return query.getResultList();
    }

    public List<Orders> fetchAllOrderForCustomer() {
        Long userId = (Long) request.getSession().getAttribute("id");
        String sql = "SELECT * FROM Orders where user_id =" + userId;
        Query query = em.createNativeQuery(sql, Orders.class);

        return query.getResultList();
    }

    public void saveProduct(String name, String imageUrl, String Tag, Double price, String quantity, String description, Integer categoryId) {
        Product product = new Product();
        product.setDescription(description);
        product.setImage(imageUrl);
        product.setName(name);
        product.setPrice(price);
        product.setStatus(true);
        product.setQuantity(String.valueOf(quantity));
        product.setTag(Tag);
        Long categoryIdLong = categoryId.longValue();
        product.setCategoryId(em.find(Category.class, categoryIdLong));
        em.persist(product);
    }

    public void saveCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setDelFlg(false);
        em.persist(category);
    }

    public void editCategory(String name, String Category, Long id) {
        Category c = em.find(Category.class, id);
        c.setName(name);
        c.setDescription(Category);
        em.merge(c);
    }

    public void editProduct(String name, String description, String Quantity, Double price, Long id) {
        Product p = em.find(Product.class, id);
        p.setDescription(description);
        p.setName(name);
        p.setQuantity(Quantity);
        p.setPrice(price);
        em.merge(p);
    }

    public void deleteCategory(Long id) {
        Category c = em.find(Category.class, id);
        c.setDelFlg(true);
        em.merge(c);
    }

    public void deleteProduct(Long id) {
        Product c = em.find(Product.class, id);
        c.setDelFlg(true);
        em.merge(c);
    }

}
