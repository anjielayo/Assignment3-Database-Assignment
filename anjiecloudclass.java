/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cen414dat;

import static cen414dat.CEN414Dat.redisData;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 *
 * @author ariel
 */
public class anjiecloudclass {
static HashMap<Double, String> redisData = new HashMap<Double, String>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean useSsl=true;
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> value = new ArrayList<String>();
        Info info = new Info();
        String anjiecachekey="FNwzpCCWvxww4No0hany4gag+bR3tiqiHHBMDkemT3c=";
        
        JedisShardInfo shardInfo=new JedisShardInfo("anjiecloud.redis.cache.windows.net",6380,useSsl);//use secondary connection string
        shardInfo.setPassword("lekjcnyf2Djg+sLbV7PQeJ1xacfZJrm3iZmrWABDhGw=");//use secondary access key
        //Simple PING command
        Jedis jedis=new Jedis(shardInfo.getHost());
        try{
            jedis.auth(anjiecachekey);
            jedis.connect();
            System.out.println("My Cache Response: "+jedis.ping());
            
            if (jedis.llen("newState") == 0 && jedis.llen("newNumber") == 0)  {
             
            //jedis.zadd("faac", (Map) Info.map);
             for(Map.Entry m: Info.map.entrySet()){
                 
                 jedis.lpush("newState",(String)m.getValue());
                 jedis.lpush("newNumber",m.getKey().toString());
             }
            
        }
        for(String s: jedis.lrange("newState", 0, 1000)){
            names.add(s);
                       
          }
         for(String r: jedis.lrange("newNumber", 0, 1000)){
               value.add(r);
            }
         for(int i =0; i < names.size()-1; i++){
             redisData.put(Double.parseDouble(value.get(i)), names.get(i));
         }

         
        ArrayList<String> states = new ArrayList<String>();
         for (Map.Entry m : redisData.entrySet()) {
             states.add((String)m.getValue());
         }
         
         String[] statesArray = new String[states.size()];
         states.toArray(statesArray);

        

        JComboBox<String> stateList = new JComboBox<>(statesArray);
        stateList.addItemListener(new MyHandler());
       
        JFrame jframe = new JFrame();
        JLabel item1 = new JLabel("IGR Statistics H1 2020");
        jframe.add(item1);
        
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLayout(new FlowLayout());
        jframe.setSize(400,200);
        jframe.setVisible(true);
        
        jframe.add(stateList);
        
        

// get the selected item:
       // String selectedBook = (String) stateList.getSelectedItem();
       

        // check whether the server is running or not
        System.out.println("Server is running: " + jedis.ping());
        //getting the percentage for each state
       

        // storing the data into redis database
      
        
        for (Map.Entry m : Info.map.entrySet()) {
            System.out.println(m.getKey() + " " + m.getValue());

  
        }
        }
        catch(JedisConnectionException e){
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null,"You require a working internet connection. Please reconnect and try again.");
        }
    }
    
       public static class MyHandler implements ItemListener{


        @Override
        public void itemStateChanged(ItemEvent e) {
             for (Map.Entry m : redisData.entrySet()) {
if(e.getItem().toString() == m.getValue()&& e.getStateChange() == 1){
                     
                     JOptionPane.showMessageDialog(null, m.getKey(), "VALUE IN BILLIONS", 1);
                     
                     System.out.println(m.getKey());
                     break;
                     
                 }
          

            
        }
       
        }
        
         }
}
    
    

