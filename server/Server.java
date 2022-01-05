/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import org.glassfish.pfl.basic.contain.Pair;

/**
 *
 * @author Сергей
 */
@WebService
public class Server {
    private Map<Integer, BarberService> listOfServices = new HashMap<>();
    private List<Pair<Integer,FreeTimeRecord>> freeTimes = new  ArrayList();
    public static final int port = 1986;
    
    @WebMethod
    public int addNewService(String nameService, int price, int duration) {
        int servHash = 0;
        try {
            BarberService serv = new BarberService(nameService, price, duration);
            servHash = serv.hashCode();
            if(listOfServices.containsKey(servHash))
                return -1;
            listOfServices.put(servHash, serv);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return servHash;
    }
    
    @WebMethod
    public boolean deleteService(int id) {
        if(listOfServices.isEmpty() || !listOfServices.containsKey(id))
            return false;
        listOfServices.remove(id);
        return true;
    }
    
    @WebMethod
    public String getListOfServices() {
        List<BarberService> valueList = new ArrayList<>(listOfServices.values());
        String tmp = "List of services\n";
        for(int i = 0; i < valueList.size(); i++)
            tmp = tmp + valueList.get(i).toString() + "\n";
        return tmp;
    }
    
    @WebMethod
    public String getFreeTimes(int year, int month, int day) throws Exception {
        Date date = new Date(year,month, day);
        int dateHash = date.hashCode();
        String tmp = "Free times:\n"; 
        if(freeTimes.isEmpty())
            return tmp + "there is no free time for this date\n";
        for(int i = 0; i < freeTimes.size(); i++) {
            if(freeTimes.get(i).first() == dateHash &&
                    !freeTimes.get(i).second().isBusy()) {
                System.out.println(freeTimes.get(i).second().isBusy());
                tmp = tmp + freeTimes.get(i).second().toString() + "\n\n";
            }
        }
        return tmp;
    }
    
    @WebMethod
    public String signUp(int year, int month, int day, int hour, int minute, int id) throws Exception {
        Date date = new Date(year, month, day);
        BarberService serv = listOfServices.get(id);
        if(serv == null)
            return "such service does not exist\n";
        int dateHash = date.hashCode();
        FreeTimeRecord tmp = new FreeTimeRecord(date, hour, minute, serv.getDurationInMinutes());
        for(int i = 0; i < freeTimes.size(); i++) {
            if(freeTimes.get(i).first() == dateHash &&
                    !freeTimes.get(i).second().isBusy() &&
                    tmp.equals(freeTimes.get(i).second())) {
                freeTimes.get(i).second().signUp();
                return "Succesful! \n" + serv.toString() + "record ID: " + freeTimes.get(i).second().hashCode() + "\n";
            }
        }
        return "Failed! \n";
    }
    
    @WebMethod
    public boolean canselSignUp(int id) {
        for(int i = 0; i < freeTimes.size(); i++) {
            if(id == freeTimes.get(i).second().hashCode()) {
                freeTimes.get(i).second().cancelRecording();
                return true;
            }
        }
        return false;
    }
    
    private void init() throws Exception {
        for(int i = 1; i < 7; i++) {
            Date date = new Date(2021, 11, i);
            for(int j = 10; j < 15; j++) {
                FreeTimeRecord times = new FreeTimeRecord(date, j, 0, 35 + j);
                Pair p = new Pair<>(date.hashCode(),times);
                freeTimes.add(p);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.init();
        String url = String.format("http://localhost:%d/Barber", port);
        Endpoint.publish(url, server);
    }
    
}
