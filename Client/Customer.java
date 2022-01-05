/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import Server.Date;
import soap.webservice.Server;
import soap.webservice.ServerService;

/**
 *
 * @author Сергей
 */
public class Customer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException, Exception {
        Server service = new ServerService(new URL("http://localhost:1986/Barber?wsdl")).getServerPort();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("getS SERVICES");
            System.out.println("getT FREE TIMES");
            System.out.println("record IN BARBER");
            System.out.println("cansel RECORDING IN BARBER");
            System.out.println("exit\n");
            String buff = bufferedReader.readLine();
            String[] tokens = buff.split(" ");
            if(tokens[0].equals("getT")) {
                if(tokens.length != 4) {
                    System.out.println("\ninvalid arguments in function "
                            + "getFreeTimes\nthere should be 3 parameters:"
                            + " year, month and day\n");
                    continue;
                }
                int year = Integer.parseInt(tokens[1]);
                int month = Integer.parseInt(tokens[2]);
                int day = Integer.parseInt(tokens[3]);
                System.out.print(service.getFreeTimes(year, month, day) + "\n");
            } else if (tokens[0].equals("record")) {
                if(tokens.length != 7) {
                    System.out.println("\ninvalid arguments in function "
                            + "signUp\nthere should be 6 parameters: "
                            + "year, month, day, hour, minute, id\n");
                    continue;
                }
                int year = Integer.parseInt(tokens[1]);
                int month = Integer.parseInt(tokens[2]);
                int day = Integer.parseInt(tokens[3]);
                int hour = Integer.parseInt(tokens[4]);
                int minute = Integer.parseInt(tokens[5]);
                int id = Integer.parseInt(tokens[6]);
                System.out.print(service.signUp(year, month, day, hour, minute, id) + "\n");
            } else if (tokens[0].equals("getS")) {
                System.out.print("\n"+service.getListOfServices() + "\n");
            } else if (tokens[0].equals("cansel")) {
                if(tokens.length != 2) {
                    System.out.println("\ninvalid arguments in function "
                            + "canselSignUp\nthere should be 1 parameters: "
                            + "id\n");
                    continue;
                }
                int id = Integer.parseInt(tokens[1]);
                if(service.canselSignUp(id))
                    System.out.println("\nSUCCESSFUL CANSEL RECORD");
                else
                    System.out.println("\nFAILED CANSEL RECORD");
            }else if (tokens[0].equals("exit")) {
                break;
            }
            
        }
    }
    
}
