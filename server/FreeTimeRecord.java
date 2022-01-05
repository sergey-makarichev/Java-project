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
public class FreeTimeRecord {
    private Date date;
    private int hours;
    private int minutes;
    private int duration;
    private boolean isBusy;
    public FreeTimeRecord() {
        this.duration = 0;
    }
    public FreeTimeRecord(int year, int month, int day, int h, int m, int dur) throws Exception {
        date = new Date(year, month, day);
        if(h < 0 || h > 24) throw new Exception("Incorrect input of hour");
        this.hours = h;
        if(m < 0 || m > 60) throw new Exception("Incorrect input of minutes");
        this.minutes = m;
        this.duration = dur;
        this.isBusy = false;
    }
    
    public FreeTimeRecord(Date date, int h, int m, int dur) throws Exception {
        this.date = date;
        if(h < 0 || h > 24) throw new Exception("Incorrect input of hour");
        this.hours = h;
        if(m < 0 || m > 60) throw new Exception("Incorrect input of minutes");
        this.minutes = m;
        this.duration = dur;
        this.isBusy = false;
    }
    public boolean isBusy() {
        return isBusy;
    }
    public void signUp() {
        isBusy = true;
    }
    
    public void cancelRecording() {
        isBusy = false;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreeTimeRecord time = (FreeTimeRecord) o;
        if(this.hours == time.hours &&
           this.minutes == time.minutes &&
           this.duration <= time.duration)
          return true;
      return false;
    }
    
    @Override
    public String toString() {
        return "Time: " + hours + ":" + minutes + "\n" +
                "Duration: " + duration;
    }
    
    @Override
    public int hashCode() {
        int result = 3 * hours;
        result += 7 * minutes;
        result += 9 * duration;
        result += date.hashCode();
        return result;
    }
}
