/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.milan.managedBean;

import com.milan.sessionBeans.UserSessionBean;
import java.io.Serializable;
import java.util.List;
import com.milan.entities.Category;
import com.milan.entities.Orders;
import com.milan.entities.Product;
import com.milan.sessionBeans.ProductSessionBean;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author User
 */
@ManagedBean
@ViewScoped
public class ProductMBean implements Serializable {

    @EJB
    private UserSessionBean userSessionBean;
    @EJB
    private ProductSessionBean productSessionBean;
    private Double price;
    private String imageUrl;
    private String name;
    private String description;
    private Integer quantity;
    private String tag;
    private Integer categoryId;
    private StreamedContent imageLogo;
    private UploadedFile logo;
    private List<Category> categoryList;
    private String categoryName, categoryDescription;
    private Long categoriesId, productId;
    private Boolean editMode = false;
    private List<Orders> orderList, orderListForCustomer;
    private List<Product> productList;

    @PostConstruct
    public void init() {
        categoryList = userSessionBean.fetchAllCategory();
        orderList = userSessionBean.fetchAllOrder();
        orderListForCustomer = userSessionBean.fetchAllOrderForCustomer();
         productList=productSessionBean.fetchProduct();
    }

//    public void handleFileUploadLogo(FileUploadEvent event) {
//        UploadedFile logo = event.getFile();
//        if (logo != null) {
//            try (InputStream input = logo.getInputStream()) {
//                String uploadDir = "C:\\Users\\dell\\Downloads\\WebProject\\WebProject\\WebProject-web\\src\\main\\webapp\\images";
//                
//
//                File uploadDirFile = new File(uploadDir);
//                 if (!uploadDirFile.exists()) {
//                uploadDirFile.mkdirs();  // create folder if not exists
//            }
//
//                String fileName = logo.getFileName();
//                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
//                Files.copy(input, new File(uploadDir + File.separator + uniqueFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
//                imageUrl = uniqueFileName;
//            } catch (IOException e) {
//
//                e.printStackTrace();
//            }
//        } else {
//
//            System.out.println("Uploaded file is null.");
//        }
//    }

    
    public void handleFileUploadLogo(FileUploadEvent event) {
    UploadedFile logo = event.getFile();
    if (logo != null) {
        System.out.println("Uploaded file: " + logo.getFileName()); // Debug log
        try (InputStream input = logo.getInputStream()) {
            String uploadDir = "C:\\Users\\dell\\Downloads\\WebProject\\WebProject\\WebProject-web\\src\\main\\webapp\\images";

            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                boolean created = uploadDirFile.mkdirs();
                System.out.println("Created directory: " + created);
            }

            String fileName = logo.getFileName();
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            File savedFile = new File(uploadDirFile, uniqueFileName);

            Files.copy(input, savedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            imageUrl = uniqueFileName; // Store just the filename

            System.out.println("Image saved to: " + savedFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("Uploaded file is null.");
    }
}


    public String saveProduct() {
        userSessionBean.saveProduct(name, imageUrl, tag, price, String.valueOf(quantity), description, categoryId);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product added Sucessfully", null));
        this.price = null;
        this.description = null;
        this.imageUrl = null;
        this.name = null;
        this.quantity = null;
        this.tag = null;
        return "dashboard.xhtml?faces-redirect=true";
    }

    public void cancelForm() {
        this.price = null;
        this.description = null;
        this.imageUrl = null;
        this.name = null;
        this.quantity = null;
        this.tag = null;
    }

    public void createNew() {
        PrimeFaces.current().executeScript("PF('categoryDialog').show()");
        editMode = false;
    }

    public void editCategoryClick(Category c) {
        editMode = true;
        categoryDescription = c.getDescription();
        categoryName = c.getName();
        categoriesId = c.getId();
        PrimeFaces.current().executeScript("PF('categoryDialog').show()");
    }

    public void deleteCategoryClick(Category c) {
        Long id = c.getId();
        userSessionBean.deleteCategory(id);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category deleted Sucessfully", null));
        this.init();
    }

    public void saveCategory() {
        userSessionBean.saveCategory(categoryName, categoryDescription);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category added Sucessfully", null));
        this.categoryDescription = null;
        this.categoryName = null;
        this.init();
    }

    public void editCategory() {
        userSessionBean.editCategory(categoryName, categoryDescription, categoriesId);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category edited Sucessfully", null));
        this.init();
        this.categoryDescription = null;
        this.categoryName = null;
    }

    public void editProduct() {
        userSessionBean.editProduct(name, description, String.valueOf(quantity), price, productId);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category edited Sucessfully", null));
        this.init();
        this.categoryDescription = null;
        this.categoryName = null;
    }

    public void cancelCategoryForm() {
        PrimeFaces.current().executeScript("PF('categoryDialog').hide()");
        this.categoryDescription = null;
        this.categoryName = null;

    }

    public void editProductClick(Product p) {
        this.productId=p.getId();
        this.name = p.getName();
        this.description = p.getDescription();
        this.price = p.getPrice();
        this.quantity = Integer.valueOf(p.getQuantity());
        PrimeFaces.current().executeScript("PF('categoryDialog').show()");

    }

    public void deleteProduct(Product p) {
        Long id = p.getId();
        userSessionBean.deleteProduct(id);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product deleted Sucessfully", null));
        this.init();
    }

    //getter and setter
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public StreamedContent getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(StreamedContent imageLogo) {
        this.imageLogo = imageLogo;
    }

    public UploadedFile getLogo() {
        return logo;
    }

    public void setLogo(UploadedFile logo) {
        this.logo = logo;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Boolean getEditMode() {
        return editMode;
    }

    public void setEditMode(Boolean editMode) {
        this.editMode = editMode;
    }

    public List<Orders> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Orders> orderList) {
        this.orderList = orderList;
    }

    public List<Orders> getOrderListForCustomer() {
        return orderListForCustomer;
    }

    public void setOrderListForCustomer(List<Orders> orderListForCustomer) {
        this.orderListForCustomer = orderListForCustomer;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

}
