/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import soap.webservice.Server;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import soap.webservice.ServerService;

/**
 *
 * @author Сергей
 */
public class Employee {
    public static final int port = 1986;
    public static final String url = "http://localhost:%d/Barber?wsdl";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException {
        Server service = new ServerService(new URL("http://localhost:1986/Barber?wsdl")).getServerPort();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("add NEW SERVICE");
            System.out.println("delete SERVICE");
            System.out.println("get SERVICES");
            System.out.println("exit\n");
            String buff = bufferedReader.readLine();
            String[] tokens = buff.split(" ");
            if(tokens[0].equals("add")) {
                if(tokens.length != 4) {
                    System.out.println("\ninvalid arguments in function "
                            + "addNewService\nthere should be 3 parameters:"
                            + " nameService, price and duration\n");
                    continue;
                }
                String nameService = tokens[1];
                int price = Integer.parseInt(tokens[2]);
                int duration = Integer.parseInt(tokens[3]);
                int id = service.addNewService(nameService, price, duration);
                if(id == -1)
                    System.out.println("\nFAILED ADD: Such a service "
                            + "already exists");
                else
                    System.out.println("\nSUCCESSFUL ADD: Service ID = " + id);
            } else if (tokens[0].equals("delete")) {
                if(tokens.length != 2) {
                    System.out.println("\ninvalid arguments in function "
                            + "deleteService\nthere should be 1 parameters: "
                            + "id\n");
                    continue;
                }
                int id = Integer.parseInt(tokens[1]);
                if(service.deleteService(id))
                    System.out.println("\nDELETE SUCCESSFUL!");
                else
                    System.out.println("\nDELETE FAILED!");
            } else if (tokens[0].equals("get")) {
                System.out.print(service.getListOfServices() + "\n");
            } else if (tokens[0].equals("exit")) {
                break;
            }
            
        }
    }
    
}
