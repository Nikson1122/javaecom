package com.milan.managedBean;

import com.milan.entities.Barchart;
import com.milan.sessionBeans.SalesServiceBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

@ManagedBean (name = "salesManagedBean")
@ViewScoped
public class SalesManagedBean implements Serializable {

    @EJB
    private SalesServiceBean salesService;

    private List<Barchart> salesList;
    private BarChartModel barChartModel;

    @PostConstruct
    public void init() {
        salesList = salesService.getAllSales();
        createBarChartModel();
    }

 private void createBarChartModel() {
    barChartModel = new BarChartModel();
    ChartSeries series = new ChartSeries();
    series.setLabel("Sales");

    for (Barchart sale : salesList) {
        String date = sale.getSalesDate().toString(); // Don't format, just use the string
        double amount = 0;

        try {
            amount = Double.parseDouble(sale.getAmount());
        } catch (Exception e) {
            System.out.println("Invalid amount for sale ID " + sale.getId() + ": " + sale.getAmount());
        }

        series.set(date, amount);
    }

    barChartModel.addSeries(series);
    barChartModel.setTitle("Sales per Date");
    barChartModel.setLegendPosition("ne");
    barChartModel.setAnimate(true);
    barChartModel.setShowPointLabels(true);
    barChartModel.setBarMargin(10);
    barChartModel.setBarPadding(5);
}


    public List<Barchart> getSalesList() {
        return salesList;
    }

    public BarChartModel getBarChartModel() {
        return barChartModel;
    }
}



















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