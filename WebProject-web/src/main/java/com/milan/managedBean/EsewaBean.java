package com.milan.managedBean;

import com.milan.entities.Payment;
import com.milan.entities.Product;
import com.milan.sessionBeans.PaymentSessionBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "esewaBean")
@SessionScoped
public class EsewaBean implements Serializable {
    
    @EJB
    private PaymentSessionBean paymentSessionBean=new PaymentSessionBean();

    private String name;
    private String orderId;
    private boolean ispaid;
    private Double paidAmount;
    private String transactionUUID;
    private String signature="";
    private boolean paymentStored = false;
    
    private List<Payment> allPayments;


public void init() {
    this.allPayments = paymentSessionBean.getAllPayments();
}

public List<Payment> getAllPayments() {
    if (allPayments == null) {
        allPayments = paymentSessionBean.getAllPayments();
    }
    return allPayments;
}


    // Replace with your actual merchant secret key
    private final String merchantSecret = "8gBm/:&EnhH.1/q";

    public EsewaBean() {
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderId() {
        return orderId;
    }

      void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isIspaid() {
        return ispaid;
    }

    public void setIspaid(boolean ispaid) {
        this.ispaid = ispaid;
    }
    
    public void setTransactionUUID(String transactionUUID) {
    this.transactionUUID = transactionUUID;
}
    
    public void setSignature(String signature) {
    this.signature = signature;
}



    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getTransactionUUID() {
        return transactionUUID;
    }

    public String getSignature() {
        return signature;
    }

    // Prepares unique UUID and signature for the payment form
    public void preparePayment() {
        this.transactionUUID = UUID.randomUUID().toString();
        
        String baseString = "total_amount=" + paidAmount +  
                           ",transaction_uuid=" + transactionUUID + 
                           ",product_code=EPAYTEST";
        this.signature = generateSignature(baseString, merchantSecret);
    }

    private String generateSignature(String baseString, String secretKey) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(baseString.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature", e);
        }
    }

    public String pay(Product p) {
        // Set payment details from the product
//        setPaidAmount(p.getPrice());
//        setName(p.getName());
//        setOrderId(p.getId().toString());
        
        this.paidAmount=p.getPrice();
        this.name=p.getName();
        this.orderId = p.getId().toString();
        System.out.println("here");
        // Prepare the payment (generate UUID and signature)
        preparePayment();
//        StorepaymentInDB();
        System.out.println("Generated Signature: " + this.signature);

        return "esewa.xhtml?faces-redirect=true";
    }
    
    public void StorepaymentInDB(){
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(paidAmount);
        payment.setProductName(name);
        payment.setTransactionUUID(transactionUUID);
        payment.setSignature(signature);
        
        payment.setPaymentDate(new Date());
        
        paymentSessionBean.savePayment(payment);
        
  
        
    }
    
 public void StorepaymentOnSucess(){
 if(!paymentStored){
     if(verifyEsewaPayment()){
         StorepaymentInDB();
         paymentStored = true;
         System.out.println("Payment verified and Saved");
     }
     
     else{
         System.out.println("Payment verification failed");
     }
 }
 }
    
 
public boolean verifyEsewaPayment() {
    try {
        String urlStr = String.format(
            "https://epay.esewa.com.np/api/epay/transaction/status/?product_code=EPAYTEST&total_amount=%s&transaction_uuid=%s",
            paidAmount, transactionUUID
        );

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        String jsonResponse = response.toString();
        System.out.println("Esewa Response: " + jsonResponse);

        // Simple manual check for "status":"COMPLETE"
        return jsonResponse.contains("\"status\":\"COMPLETE\"");
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
 
}