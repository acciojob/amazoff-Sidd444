package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;
class pair{
    String partner_id;
    int last_time;
    pair(String partner_id,int last_time)
    {
        this.partner_id = partner_id;
        this.last_time = last_time;
    }
}

@Repository
public class OrderRepository {
    int unassigned_order=0;
    HashMap<String ,Order> order_db = new HashMap<>();
    HashMap<String , DeliveryPartner> partner_db = new HashMap<>();
    HashMap<String , List<Order>> partner_order__db = new HashMap<>();

    HashMap<String,String> assigned_orders = new HashMap<>();

    Stack<pair> st = new Stack<>();
    public void add_order(Order order)
    {
        unassigned_order++;
        String ord_id = order.getId();
        order_db.put(ord_id,order);
    }
    public void add_deliverypartner(String dp_id)
    {
        DeliveryPartner dp_class = new DeliveryPartner(dp_id);
        partner_db.put(dp_id,dp_class);
    }
    public void add_order_to_partner(String ord_id,String dp_id)
    {
        List<Order> temp = partner_order__db.getOrDefault(dp_id,new ArrayList<>());
        Order ord = order_db.get(ord_id);
        temp.add(ord);
        partner_order__db.put(dp_id,temp);
        assigned_orders.put(ord_id,dp_id);
        unassigned_order--;
        DeliveryPartner partner = partner_db.get(dp_id);
        partner.setNumberOfOrders(temp.size());

    }
    public Order get_order_by_id(String ord_id)
    {
//        if(order_db.containsKey(ord_id))
//            return order_db.get(ord_id);
//        return null;
        for(String s : order_db.keySet()){
            if(s.equals(ord_id))return order_db.get(s);
        }
        return null;
    }
    public DeliveryPartner get_dp_by_id(String dp_id)
    {
        if(partner_db.containsKey(dp_id))
            return partner_db.get(dp_id);
        return null;
    }
    public List<Order> get_order_by_dpid(String dp_id)
    {
        if(partner_order__db.containsKey(dp_id))
            return partner_order__db.get(dp_id);
        return new ArrayList<>();
    }
    public List<String> get_all_orders()
    {
        List<String> ords = new ArrayList<>();
        for(String ord_id:order_db.keySet())
        {
            ords.add(ord_id);
        }
        return ords;
    }
    public int get_unassigned_order_count()
    {
        //return order_db.size()-assigned_orders.size();
        return order_db.size()-assigned_orders.size();
//        int count=0;
//        boolean flag = false;
//        for(Order order:order_db.values())
//        {
//            flag = false;
//            for(List<Order> list_ord:partner_order__db.values())
//            {
//                if(list_ord.contains(order))
//                {
//                    flag = true;
//                    break;
//                }
//            }
//
//            if(flag==false)
//            {
//                count++;
//                unassigned_orders.put(order.getId(),order);
//            }
//        }
//        return count;
    }
    public void delete_partner_and_order(String dp_id)
    {
        List<Order> list_ord = partner_order__db.getOrDefault(dp_id,new ArrayList<>());
        for(Order ord:list_ord)
        {
            assigned_orders.remove(ord.getId());
            unassigned_order--;
        }
        partner_db.remove(dp_id);
        partner_order__db.remove(dp_id);

    }
    public void delete_ord_and_unassign(String ord_id)
    {
        String part_id = "";
        boolean found = false;
        for(String dp_id:partner_order__db.keySet())
        {
            List<Order> ords = partner_order__db.get(dp_id);
            //List<Order> temp_remove = new ArrayList<>(ords);
            for(Order ord:ords)
            {
                if(ord.getId().equals(ord_id))
                {
                    List<Order> temp = new ArrayList<>(ords);
                    part_id = dp_id;
                    temp.remove(ord);
                    partner_order__db.put(part_id,temp);
                    st.add(new pair(part_id,ord.getDeliveryTime()));
                    found = true;
                    break;
                }
            }
            if(found)
                break;


        }
        assigned_orders.remove(ord_id);
        order_db.remove(ord_id);
    }

}
