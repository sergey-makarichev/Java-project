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
public class Date {
    private int month;
    private int year;
    private int day;
    
    public Date(int year, int month, int day) throws Exception {
        if(month < 1 || month > 12) throw new Exception("Incorrect input of hour");
            this.month = month;
        if(year < 0) throw new Exception("Incorrect input of minutes");
            this.year = year;
        if(day < 0 || day > 30) throw new Exception("Incorrect input of minutes");
            this.day = day;
    }
    
    public void setYear(int year) throws Exception {
        if(year < 0) throw new Exception("Incorrect input of minutes");
            this.year = year;
    }
    
    public void setMonth(int month) throws Exception {
        if(month < 0 || month > 12) 
            throw new Exception("Incorrect input of minutes");
        this.month = month;
    }
    
    public void setDay(int day) throws Exception {
        if(day < 0 || day > 30) 
            throw new Exception("Incorrect input of minutes");
        this.day = day;
    }
    
    @Override
    public int hashCode() {
        int result = 2 * month + 3 * year + 5 * day;
        return result;
    } 
}
