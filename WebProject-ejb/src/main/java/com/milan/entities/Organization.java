    package com.milan.entities;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.List;
    import javax.persistence.*;

    @Entity
    @Table(name = "organization")
    public class Organization implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "org_name")
        private String name;

        private String address;

        @Column(name = "contact_number")
        private String contactNumber;

        @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
        private List<Customer> customers = new ArrayList<>();

        public Organization() {
        }

        public Organization(String name, String address, String contactNumber) {
            this.name = name;
            this.address = address;
            this.contactNumber = contactNumber;
        }

        // Basic getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getContactNumber() { return contactNumber; }
        public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
        public List<Customer> getCustomers() { return customers; }
        public void setCustomers(List<Customer> customers) { this.customers = customers; }

        // Only essential relationship method
        public void addCustomer(Customer customer) {
            customers.add(customer);
            customer.setOrganization(this);
        }
    }