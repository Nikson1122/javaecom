package com.milan.managedBean;

import com.milan.entities.Barchart;
import com.milan.sessionBeans.SalesServiceBean;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

@ManagedBean(name = "salesManagedBean")
@ViewScoped
public class SalesManagedBean implements Serializable {

    @EJB
    private SalesServiceBean salesService;

    private Date startDate;
    private Date endDate;
    private String filterName;
    private List<Barchart> salesList = new ArrayList<>();
    private List<Barchart> filteredSalesList = new ArrayList<>();
    private BarChartModel barChartModel = new BarChartModel();

    @PostConstruct
    public void init() {
        loadAllSales();
    }

    public void loadAllSales() {
        List<Barchart> result = salesService.getAllSales();
        if (result != null) {
            salesList = result;
        }
        resetFilters();
    }

    public void filterSales() {
        filteredSalesList = new ArrayList<>();
        
        for (Barchart sale : salesList) {
            try {
                Date saleDate = parseDate(sale.getSalesDate());
                
                boolean dateInRange = isWithinDateRange(saleDate, startDate, endDate);
                boolean nameMatch = isNameMatch(sale.getPurchasedBy(), filterName);
                
                if (dateInRange && nameMatch) {
                    filteredSalesList.add(sale);
                }
            } catch (Exception e) {
                System.out.println("Error processing sale: " + sale.getId());
            }
        }
        createBarChartModel();
    }

    private boolean isWithinDateRange(Date dateToCheck, Date rangeStart, Date rangeEnd) {
        if (dateToCheck == null) return false;
        
        boolean afterStart = (rangeStart == null) || !dateToCheck.before(rangeStart);
        boolean beforeEnd = (rangeEnd == null) || !dateToCheck.after(rangeEnd);
        
        return afterStart && beforeEnd;
    }

    private boolean isNameMatch(String purchasedBy, String filter) {
        if (filter == null || filter.trim().isEmpty()) return true;
        return purchasedBy != null && purchasedBy.equalsIgnoreCase(filter.trim());
    }

    private Date parseDate(String dateString) throws ParseException {
        if (dateString == null || dateString.trim().isEmpty()) return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            System.out.println("Failed to parse date: " + dateString);
            throw e;
        }
    }

    public void resetFilters() {
        startDate = null;
        endDate = null;
        filterName = null;
        filteredSalesList = new ArrayList<>(salesList);
        createBarChartModel();
    }

    private void createBarChartModel() {
        barChartModel.clear();
        ChartSeries series = new ChartSeries();
        series.setLabel("Sales");

        for (Barchart sale : filteredSalesList) {
            try {
                String label = String.format("%s (%s)", 
                    sale.getPurchasedBy() != null ? sale.getPurchasedBy() : "N/A", 
                    sale.getSalesDate() != null ? sale.getSalesDate() : "No Date");
                
                double amount = sale.getAmount() != null ? 
                    Double.parseDouble(sale.getAmount()) : 0;
                
                series.set(label, amount);
            } catch (Exception e) {
                System.out.println("Error processing sale: " + sale.getId());
            }
        }

        barChartModel.addSeries(series);
        barChartModel.setTitle("Sales Data");
        barChartModel.setLegendPosition("ne");
        barChartModel.setAnimate(true);
    }

    public List<SelectItem> getNameOptions() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem("", "All Purchasers"));
        
        try {
            List<String> names = salesService.getDistinctNames();
            if (names != null) {
                for (String name : names) {
                    if (name != null) {
                        items.add(new SelectItem(name, name));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting names: " + e.getMessage());
        }
        return items;
    }

    // Getters and Setters
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getFilterName() { return filterName; }
    public void setFilterName(String filterName) { this.filterName = filterName; }
    public List<Barchart> getFilteredSalesList() { return filteredSalesList; }
    public BarChartModel getBarChartModel() { return barChartModel; }
}



//    package com.milan.managedBean;
//
//import com.milan.entities.Barchart;
//import com.milan.sessionBeans.SalesServiceBean;
//import java.io.Serializable;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//import javax.faces.model.SelectItem;
//import org.primefaces.model.chart.BarChartModel;
//import org.primefaces.model.chart.ChartSeries;
//
//@ManagedBean(name = "salesManagedBean")
//@ViewScoped
//public class SalesManagedBean implements Serializable {
//
//    @EJB
//    private SalesServiceBean salesService;
//
//    private Date filterDate;
//    private Date startDate;
//    private Date endate;
//    private String filterName;
//    private List<Barchart> salesList = new ArrayList<>();
//    private List<Barchart> filteredSalesList = new ArrayList<>();
//    private BarChartModel barChartModel = new BarChartModel();
//
//    @PostConstruct
//    public void init() {
//        loadAllSales();
//    }
//
//    public void loadAllSales() {
//        List<Barchart> result = salesService.getAllSales();
//        if (result != null) {
//            salesList = result;
//        }
//        resetFilters();
//    }
//
//    public void filterSales() {
//        filteredSalesList = new ArrayList<>();
//        
//        for (Barchart sale : salesList) {
//            try {
//                boolean dateMatch = true;
//                if (filterDate != null) {
//                    dateMatch = isSameDay(parseDate(sale.getSalesDate()), filterDate);
//                }
//                
//                boolean nameMatch = filterName == null || filterName.trim().isEmpty() || 
//                                 (sale.getPurchasedBy() != null && 
//                                  sale.getPurchasedBy().equalsIgnoreCase(filterName.trim()));
//                
//                if (dateMatch && nameMatch) {
//                    filteredSalesList.add(sale);
//                }
//            } catch (Exception e) {
//                System.out.println("Error processing sale: " + sale.getId());
//            }
//        }
//        createBarChartModel();
//    }
//
//    private Date parseDate(String dateString) throws ParseException {
//        if (dateString == null || dateString.trim().isEmpty()) {
//            return null;
//        }
//        try {
//            // Adjust this format to match your actual date format
//            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
//        } catch (ParseException e) {
//            System.out.println("Failed to parse date: " + dateString);
//            throw e;
//        }
//    }
//
//    private boolean isSameDay(Date date1, Date date2) {
//        if (date1 == null || date2 == null) {
//            return false;
//        }
//        Calendar cal1 = Calendar.getInstance();
//        Calendar cal2 = Calendar.getInstance();
//        cal1.setTime(date1);
//        cal2.setTime(date2);
//        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
//               cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
//               cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
//    }
//
//    public void resetFilters() {
//        filterDate = null;
//        filterName = null;
//        filteredSalesList = new ArrayList<>(salesList);
//        createBarChartModel();
//    }
//
//    private void createBarChartModel() {
//        barChartModel.clear();
//        ChartSeries series = new ChartSeries();
//        series.setLabel("Sales");
//
//        for (Barchart sale : filteredSalesList) {
//            try {
//                String label = (sale.getPurchasedBy() != null ? sale.getPurchasedBy() : "N/A") + 
//                             " (" + (sale.getSalesDate() != null ? sale.getSalesDate() : "No Date") + ")";
//                double amount = sale.getAmount() != null ? Double.parseDouble(sale.getAmount()) : 0;
//                series.set(label, amount);
//            } catch (Exception e) {
//                System.out.println("Error processing sale: " + sale.getId() + " - " + e.getMessage());
//            }
//        }
//
//        barChartModel.addSeries(series);
//        barChartModel.setTitle("Sales Data");
//        barChartModel.setLegendPosition("ne");
//        barChartModel.setAnimate(true);
//    }
//
//    public List<SelectItem> getNameOptions() {
//        List<SelectItem> items = new ArrayList<>();
//        items.add(new SelectItem("", "All Purchasers"));
//        
//        try {
//            List<String> names = salesService.getDistinctNames();
//            if (names != null) {
//                for (String name : names) {
//                    if (name != null) {
//                        items.add(new SelectItem(name, name));
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error getting names: " + e.getMessage());
//        }
//        return items;
//    }
//
//    // Getters and Setters
//
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    public Date getEndate() {
//        return endate;
//    }
//
//    public void setEndate(Date endate) {
//        this.endate = endate;
//    }
//    
//    
//    public Date getFilterDate() {
//        return filterDate;
//    }
//
//    public void setFilterDate(Date filterDate) {
//        this.filterDate = filterDate;
//    }
//
//    public String getFilterName() {
//        return filterName;
//    }
//
//    public void setFilterName(String filterName) {
//        this.filterName = filterName;
//    }
//
//    public List<Barchart> getFilteredSalesList() {
//        return filteredSalesList;
//    }
//
//    public BarChartModel getBarChartModel() {
//        return barChartModel;
//    }
//}



//package com.milan.managedBean;
//
//import com.milan.entities.Barchart;
//import com.milan.sessionBeans.SalesServiceBean;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.faces.model.SelectItem;
//
//import org.primefaces.model.chart.BarChartModel;
//import org.primefaces.model.chart.ChartSeries;
//
//@ManagedBean (name = "salesManagedBean")
//@ViewScoped
//public class SalesManagedBean implements Serializable {
//    
// 
//
//    @EJB
//    private SalesServiceBean salesService;
//    
//    private Date filterDate;
//    private String filterName;
// 
//
//    private List<Barchart> salesList;
//    
//     private List<Barchart> filteredSalesList;
//    private BarChartModel barChartModel;
//
//    @PostConstruct
//    public void init() {
//        salesList = salesService.getAllSales();
//        createBarChartModel();
//    }
//
// private void createBarChartModel() {
//    barChartModel = new BarChartModel();
//    ChartSeries series = new ChartSeries();
//    series.setLabel("Sales");
//
//    for (Barchart sale : salesList) {
//        String date = sale.getSalesDate().toString(); // Don't format, just use the string
//        double amount = 0;
//
//        try {
//            amount = Double.parseDouble(sale.getAmount());
//        } catch (Exception e) {
//            System.out.println("Invalid amount for sale ID " + sale.getId() + ": " + sale.getAmount());
//        }
//
//        series.set(date, amount);
//    }
//
//    barChartModel.addSeries(series);
//    barChartModel.setTitle("Sales per Date");
//    barChartModel.setLegendPosition("ne");
//    barChartModel.setAnimate(true);
//    barChartModel.setShowPointLabels(true);
//    barChartModel.setBarMargin(10);
//    barChartModel.setBarPadding(5);
//    
//}
//public List<SelectItem> getNameOptions() {
//        List<String> names = salesService.getDistinctNames(); // TO get Distinct
//        List<SelectItem> items = new ArrayList<>();
//        for (String name : names) {
//            items.add(new SelectItem(name, name));
//        }
//        return items;
//    }
//
//    public SalesServiceBean getSalesService() {
//        return salesService;
//    }
//
//    public void setSalesService(SalesServiceBean salesService) {
//        this.salesService = salesService;
//    }
//
//    public Date getFilterDate() {
//        return filterDate;
//    }
//
//    public void setFilterDate(Date filterDate) {
//        this.filterDate = filterDate;
//    }
//
//    public String getFilterName() {
//        return filterName;
//    }
//
//    public void setFilterName(String filterName) {
//        this.filterName = filterName;
//    }
//
//
//
//    public List<Barchart> getSalesList() {
//        return salesList;
//    }
//
//    public BarChartModel getBarChartModel() {
//        return barChartModel;
//    }
//    
//
//}



















//package com.milan.managedBean;
//
//import com.milan.entities.Barchart;
//import com.milan.sessionBeans.SalesServiceBean;
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//import java.io.Serializable;
//import java.util.List;
//
//@ManagedBean
//@ViewScoped
//public class SalesManagedBean implements Serializable {
//
//    @EJB
//    private SalesServiceBean salesService;
//
//    private List<Barchart> salesList;
//
//    @PostConstruct
//    public void init() {
//        salesList = salesService.getAllSales();
//    }
//
//    public List<Barchart> getSalesList() {
//        return salesList;
//    }
//}