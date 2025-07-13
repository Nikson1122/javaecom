package com.milan.managedBean;

import com.milan.entities.Customer;
import com.milan.entities.Organization;
import com.milan.sessionBeans.ExcelServiceBean;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

@ManagedBean
@SessionScoped
public class ExcelBean implements Serializable {
    
    @EJB
    private ExcelServiceBean excelService;
    
    private List<Organization> organizations = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private boolean fileProcessed = false;
    private boolean saveInProgress = false;
    private String fileName;
    
//    
//    public void upload(FileUploadEvent event) {
//    UploadedFile file = event.getFile();
//    fileName = file.getFileName();
//    
//    if (file == null || file.getSize() == 0) {
//        addErrorMessage("Please select a valid Excel file.");
//        fileProcessed = false; // Explicitly set to false
//        return;
//    }
//
//    try (InputStream input = file.getInputStream();
//         Workbook workbook = new XSSFWorkbook(input)) {
//        
//        organizations.clear();
//        customers.clear();
//        
//        processSheet(workbook.getSheet("Organizations"), "Organization");
//        processSheet(workbook.getSheet("Customers"), "Customer");
//        
//        fileProcessed = true; // THIS IS CRUCIAL
//        addSuccessMessage("File processed successfully. Click Save.");
//        
//    } catch (Exception e) {
//        fileProcessed = false; // Explicitly set to false on error
//        addErrorMessage("Error processing file: " + e.getMessage());
//        e.printStackTrace(); // Log the full error
//    }
//}
    
    
    public void upload(FileUploadEvent event) {
    UploadedFile file = event.getFile();
    fileName = file.getFileName();
        System.out.println("File nae : "+fileName);
    if (file == null || file.getSize() == 0) {
        addErrorMessage("Please select a valid Excel file.");
        fileProcessed = false;
        return;
    }

    if (!fileName.toLowerCase().endsWith(".xlsx")) {
        addErrorMessage("Only .xlsx files are supported.");
        fileProcessed = false;
        return;
    }

    try (InputStream input = file.getInputStream()) {
        Workbook workbook = new XSSFWorkbook(input);
        
        organizations.clear();
        customers.clear();
        
        processSheet(workbook.getSheet("Organizations"), "Organization");
        processSheet(workbook.getSheet("Customers"), "Customer");
        
        if (organizations.isEmpty() && customers.isEmpty()) {
            addErrorMessage("No valid data found in the Excel file.");
            fileProcessed = false;
        } else {
            fileProcessed = true;
            addSuccessMessage("File processed successfully. Found " + 
                            organizations.size() + " organizations and " + 
                            customers.size() + " customers. Click Save to store in database.");
        }
        
    } catch (Exception e) {
        fileProcessed = false;
        addErrorMessage("Error processing file: " + e.getMessage());
        e.printStackTrace();
    }
}

//    public void upload(FileUploadEvent event) {
//        UploadedFile file = event.getFile();
//        fileName = file.getFileName();
//        
//        if (file == null || file.getSize() == 0) {
//            addErrorMessage("Please select a valid Excel file.");
//            return;
//        }
//
//        try (InputStream input = file.getInputStream();
//             Workbook workbook = new XSSFWorkbook(input)) {
//            
//            organizations.clear();
//            customers.clear();
//            
//            processSheet(workbook.getSheet("Organizations"), "Organization");
//            processSheet(workbook.getSheet("Customers"), "Customer");
//            
//            fileProcessed = true;
//            addSuccessMessage("File '" + fileName + "' processed successfully. Ready to save.");
//            
//        } catch (Exception e) {
//            addErrorMessage("Error processing file: " + e.getMessage());
//            fileProcessed = false;
//        }
//    }

    private void processSheet(Sheet sheet, String type) {
        if (sheet == null) {
            addWarningMessage(type + " sheet not found in the file.");
            return;
        }

        Iterator<Row> rowIterator = sheet.iterator();
        if (!rowIterator.hasNext()) return; // Skip empty sheet
        
        // Skip header row
        rowIterator.next();
        
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            try {
                if (type.equals("Organization")) {
                    organizations.add(createOrganization(row));
                } else {
                    customers.add(createCustomer(row));
                }
            } catch (Exception e) {
                addWarningMessage("Error processing row " + (row.getRowNum()+1) + " in " + type + " sheet: " + e.getMessage());
            }
        }
    }

    private Organization createOrganization(Row row) {
        Organization org = new Organization();
        org.setName(getCellValue(row.getCell(0)));
        org.setAddress(getCellValue(row.getCell(1)));
        org.setContactNumber(getCellValue(row.getCell(2)));
        return org;
    }

    private Customer createCustomer(Row row) {
        Customer cust = new Customer();
        cust.setName(getCellValue(row.getCell(0)));
        cust.setAddress(getCellValue(row.getCell(1)));
        cust.setContactNumber(getCellValue(row.getCell(2)));
        
        String orgName = getCellValue(row.getCell(3));
        if (orgName != null && !orgName.isEmpty()) {
            Organization org = new Organization();
            org.setName(orgName);
            cust.setOrganization(org);
        }
        
        return cust;
    }
    
    public void saveData() {
    if (!fileProcessed || saveInProgress) return;
    
    saveInProgress = true;
    FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("savin record");
    try {
        // Save organizations first
        Map<String, Organization> savedOrgs = new HashMap<>();
        for (Organization org : organizations) {
            try {
                Organization saved = excelService.createOrganization(org);
                savedOrgs.put(saved.getName(), saved);
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "Warning", "Failed to save organization: " + org.getName() + ". Error: " + e.getMessage()));
            }
        }

        // Save customers
        int customerSuccessCount = 0;
        for (Customer cust : customers) {
            try {
                if (cust.getOrganization() != null && cust.getOrganization().getName() != null) {
                    Organization persistedOrg = savedOrgs.get(cust.getOrganization().getName());
                    if (persistedOrg != null) {
                        excelService.createCustomer(cust, persistedOrg.getId());
                        customerSuccessCount++;
                    } else {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "Warning", "Organization not found for customer: " + cust.getName()));
                    }
                } else {
                    // Save customer without organization
                    excelService.createCustomer(cust, null);
                    customerSuccessCount++;
                }
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "Warning", "Failed to save customer: " + cust.getName() + ". Error: " + e.getMessage()));
            }
        }

        // Reset state
        organizations.clear();
        customers.clear();
        fileProcessed = false;
        
        addSuccessMessage("Successfully saved " + savedOrgs.size() + 
                        " organizations and " + customerSuccessCount + " customers.");
        
    } catch (Exception e) {
        addErrorMessage("Failed to save data: " + e.getMessage());
        e.printStackTrace();
    } finally {
        saveInProgress = false;
    }
}

//    public void saveData() {
//        if (!fileProcessed || saveInProgress) return;
//        
//        saveInProgress = true;
//        try {
//            // Save organizations first
//            Map<String, Organization> savedOrgs = new HashMap<>();
//            for (Organization org : organizations) {
//                Organization saved = excelService.createOrganization(org);
//                savedOrgs.put(saved.getName(), saved);
//            }
//
//            // Save customers with proper organization references
//            int successCount = 0;
//            for (Customer cust : customers) {
//                if (cust.getOrganization() != null) {
//                    Organization persistedOrg = savedOrgs.get(cust.getOrganization().getName());
//                    if (persistedOrg != null) {
//                        excelService.createCustomer(cust, persistedOrg.getId());
//                        successCount++;
//                    }
//                }
//            }
//
//            // Reset state after successful save
//            organizations.clear();
//            customers.clear();
//            fileProcessed = false;
//            
//            addSuccessMessage("Successfully saved " + savedOrgs.size() + 
//                            " organizations and " + successCount + " customers.");
//            
//        } catch (Exception e) {
//            addErrorMessage("Failed to save data: " + e.getMessage());
//        } finally {
//            saveInProgress = false;
//        }
//    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }

    private void addWarningMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", message));
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }

    // Getters and Setters
    public List<Organization> getOrganizations() { return organizations; }
    public List<Customer> getCustomers() { return customers; }
    public boolean isFileProcessed() { return fileProcessed; }
    public String getFileName() { return fileName; }
}

//package com.milan.managedBean;
//
//import com.milan.entities.Customer;
//import com.milan.entities.Organization;
//import com.milan.sessionBeans.ExcelServiceBean;
//
//import java.io.InputStream;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.ejb.EJB;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//import javax.faces.context.FacesContext;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.primefaces.event.FileUploadEvent;
//import org.primefaces.model.file.UploadedFile;
//
//@ManagedBean
//@ViewScoped
//public class ExcelBean implements Serializable {
//
//    @EJB
//    private ExcelServiceBean excelService;
//
//    private List<Organization> organizations = new ArrayList<>();
//    private List<Customer> customers = new ArrayList<>();
//    private boolean fileProcessed = false;
//
//    public void upload(FileUploadEvent event) {
//        UploadedFile file = event.getFile();
//
//        if (file == null || file.getSize() == 0) {
//            addMessage("Please select a valid Excel file.", FacesMessage.SEVERITY_ERROR);
//            return;
//        }
//
//        try (InputStream input = file.getInputStream();
//             Workbook workbook = new XSSFWorkbook(input)) {
//
//            Sheet orgSheet = workbook.getSheet("Organizations");
//            if (orgSheet != null) {
//                organizations.clear();
//                processOrganizationSheet(orgSheet);
//            } else {
//                addMessage("Sheet 'Organizations' not found!", FacesMessage.SEVERITY_WARN);
//            }
//
//            Sheet custSheet = workbook.getSheet("Customers");
//            if (custSheet != null) {
//                customers.clear();
//                processCustomerSheet(custSheet);
//            } else {
//                addMessage("Sheet 'Customers' not found!", FacesMessage.SEVERITY_WARN);
//            }
//
//            fileProcessed = true;
//            addMessage("File processed successfully.", FacesMessage.SEVERITY_INFO);
//
//        } catch (Exception e) {
//            addMessage("Error processing file: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
//            e.printStackTrace();
//            fileProcessed = false;
//        }
//    }
//
//    private void processOrganizationSheet(Sheet sheet) {
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) continue; // Skip header
//
//            Organization org = new Organization();
//            org.setName(getCellStringValue(row.getCell(0)));
//            org.setAddress(getCellStringValue(row.getCell(1)));
//            org.setContactNumber(getCellStringValue(row.getCell(2)));
//
//            organizations.add(org);
//        }
//    }
//
//    private void processCustomerSheet(Sheet sheet) {
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) continue; // Skip header
//
//            Customer cust = new Customer();
//            cust.setName(getCellStringValue(row.getCell(0)));
//            cust.setAddress(getCellStringValue(row.getCell(1)));
//            cust.setContactNumber(getCellStringValue(row.getCell(2)));
//
//            String orgName = getCellStringValue(row.getCell(3));
//            Organization linkedOrg = findOrganizationByName(orgName);
//
//            if (linkedOrg != null) {
//                cust.setOrganization(linkedOrg);
//            }
//
//            customers.add(cust);
//        }
//    }
//
//    private Organization findOrganizationByName(String orgName) {
//        if (orgName == null) return null;
//        for (Organization org : organizations) {
//            if (org.getName() != null && org.getName().equals(orgName)) {
//                return org;
//            }
//        }
//        return null;
//    }
//    
//    
//    
//    public void saveData() {
//    try {
//        // Step 1: Persist organizations and collect them in a map
//        Map<String, Organization> savedOrgs = new HashMap<>();
//        for (Organization org : organizations) {
//            Organization saved = excelService.createOrganization(org); // persist and return
//            savedOrgs.put(saved.getName(), saved); // map by name
//        }
//
//        // Step 2: Assign real persisted org to each customer before persisting
//        for (Customer cust : customers) {
//            Organization persistedOrg = savedOrgs.get(cust.getOrganization().getName());
//            if (persistedOrg != null) {
//                cust.setOrganization(persistedOrg);
//                excelService.createCustomer(cust, persistedOrg.getId());
//            } else {
//                addMessage("Customer " + cust.getName() + " has no saved organization.", FacesMessage.SEVERITY_WARN);
//            }
//        }
//
//        addMessage("Data saved successfully.", FacesMessage.SEVERITY_INFO);
//
//        organizations.clear();
//        customers.clear();
//        fileProcessed = false;
//
//    } catch (Exception e) {
//        addMessage("Error saving data: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
//        e.printStackTrace();
//    }
//}
//
//
////    public void saveData() {
////        try {
////            for (Organization org : organizations) {
////                excelService.createOrganization(org);
////            }
////
////            for (Customer cust : customers) {
////                if (cust.getOrganization() != null && cust.getOrganization().getId() != null) {
////                    excelService.createCustomer(cust, cust.getOrganization().getId());
////                } else {
////                    addMessage("Customer " + cust.getName() + " has no linked organization saved.", FacesMessage.SEVERITY_WARN);
////                }
////            }
////
////            addMessage("Data saved successfully.", FacesMessage.SEVERITY_INFO);
////
////            organizations.clear();
////            customers.clear();
////            fileProcessed = false;
////
////        } catch (Exception e) {
////            addMessage("Error saving data: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
////            e.printStackTrace();
////        }
////    }
//
//    private String getCellStringValue(Cell cell) {
//        if (cell == null) return "";
//        DataFormatter formatter = new DataFormatter();
//        return formatter.formatCellValue(cell).trim();
//    }
//
//    private void addMessage(String message, FacesMessage.Severity severity) {
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, null, message));
//    }
//
//    // Getters and Setters
//
//    public List<Organization> getOrganizations() {
//        return organizations;
//    }
//
//    public List<Customer> getCustomers() {
//        return customers;
//    }
//
//    public boolean isFileProcessed() {
//        return fileProcessed;
//    }
//}
//



//package com.milan.managedBean;
//
//import java.io.InputStream;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//import javax.faces.context.FacesContext;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.primefaces.event.FileUploadEvent;
//import org.primefaces.model.file.UploadedFile;
//
//@ManagedBean
//@ViewScoped
//public class ExcelBean implements Serializable {
//
//    private List<String> logs = new ArrayList<>();
//    private boolean fileProcessed = false;
//
//    public void upload(FileUploadEvent event) {
//        UploadedFile file = event.getFile();
//
//        // Debug log - check file name and size
//        logs.add("Received file: " + (file != null ? file.getFileName() : "null"));
//        logs.add("File size: " + (file != null ? file.getSize() + " bytes" : "no file"));
//
//        if (file == null || file.getSize() == 0) {
//            logs.add("No file or empty file uploaded!");
//            fileProcessed = false;
//            return;
//        }
//
//        try (InputStream input = file.getInputStream();
//             Workbook workbook = new XSSFWorkbook(input)) {
//
//            logs.add("Workbook has " + workbook.getNumberOfSheets() + " sheets");
//
//            // Just demonstrate sheet names for debugging
//            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//                logs.add("Sheet " + i + ": " + workbook.getSheetName(i));
//            }
//
//            fileProcessed = true;
//            logs.add("File processed successfully!");
//
//        } catch (Exception e) {
//            logs.add("Error processing file: " + e.getMessage());
//            e.printStackTrace();
//            fileProcessed = false;
//        }
//    }
//
//    // For debug output in UI
//    public List<String> getLogs() {
//        return logs;
//    }
//
//    public boolean isFileProcessed() {
//        return fileProcessed;
//    }
//}
//
