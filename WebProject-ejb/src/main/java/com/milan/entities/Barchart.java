package com.milan.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "Sales")
public class Barchart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "amount", nullable = false, length = 100)
    private String amount;

    @Column(name = "sales_date", nullable = false, length = 100)
    private String salesDate;

    @Column(name = "purchasedby", nullable = false, length = 100)
    private String purchasedBy;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public String getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(String purchasedBy) {
        this.purchasedBy = purchasedBy;
    }
}

