/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author Сергей
 */
public class BarberService {
    private String nameService;
    private int price;
    private int durationInMinutes;
    
    public BarberService(String nameService, int price, int duration) throws Exception {
        if(price <= 0 || duration <= 0)
            throw new Exception("Error: Invalid price or duration!");
        this.nameService = nameService;
        this.durationInMinutes = duration;
        this.price = price;
    }
    
    public int getDurationInMinutes(){
        return durationInMinutes;
    }
    @Override
    public String toString() {
        return "Service: " + nameService + "\n" + 
                "Price: " + price + "\n" + 
                "Duration: " + durationInMinutes + "\n" +
                "service ID: " + this.hashCode() + "\n";
    }
    
    @Override
    public int hashCode() {
        int result = nameService == null ? 0 : nameService.hashCode();
        result += durationInMinutes;
        result += price;
        return result;
    }   
}
