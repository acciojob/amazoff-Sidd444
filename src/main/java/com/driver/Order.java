package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {
         this.id=id;
         int dt=Integer.parseInt(deliveryTime);
         int h=dt/60,m=dt%60;
         String DT=String.format("%02d:%02d",h,m);
         this.deliveryTime=Integer.parseInt(DT);
        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
