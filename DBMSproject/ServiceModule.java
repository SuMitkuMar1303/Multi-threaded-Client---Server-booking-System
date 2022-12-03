// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.InputStreamReader;
// import java.io.OutputStreamWriter;
// import java.io.PrintWriter;
// import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// import javax.lang.model.util.ElementScanner14;

import java.io.*;
// import java.util.*;
import java.sql.*;

class QueryRunner implements Runnable
{
    //  Declare socket for client access
    protected Socket socketConnection;

    public QueryRunner(Socket clientSocket)
    {
        this.socketConnection =  clientSocket;
    }

    public void run()
    {
        
        Connection connection = null;
        Statement st = null;
        ResultSet res = null;
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345");
            if (connection != null) {

                System.out.println("connection OK");
            } else {
                System.out.println("connection FAILED");
            }
            connection.setAutoCommit(false);
            st = connection.createStatement();
        }
        catch (Exception e) {
            System.out.println(e);
        }
       
      try
        {
            //  Reading data from client
            InputStreamReader inputStream = new InputStreamReader(socketConnection.getInputStream()) ;
            BufferedReader bufferedInput = new BufferedReader(inputStream) ;
            OutputStreamWriter outputStream = new OutputStreamWriter(socketConnection.getOutputStream()) ;
            BufferedWriter bufferedOutput = new BufferedWriter(outputStream) ;
            PrintWriter printWriter = new PrintWriter(bufferedOutput, true) ;
            String clientCommand = "" ;
            // String responseQuery = "" ;
            String Border ="----------------------------------------------------------------------------------------------------------";
            // Read client query from the socket endpoint
            clientCommand = bufferedInput.readLine(); 

            while( ! clientCommand.equals("#"))
            {
                
                // System.out.println("Recieved data <" + clientCommand + "> from client : " + socketConnection.getRemoteSocketAddress().toString());

                int n = clientCommand.length();
                String nump="";                
                int i=0;
                for(;i<n;i++)
                {
                    if(clientCommand.charAt(i)!=' ')
                    {
                        nump+=clientCommand.charAt(i);
                    }
                    else 
                    {
                        break;
                    }
                }
                i++;
                int num= Integer.parseInt(nump);  
                String[] name= new String[num];
                String tnum="",dt="",query="",type = "";
                int j=0;
                while(j<num)
                {
                    String pname="";
                    for(;i<n;i++)
                    {
                        if(clientCommand.charAt(i)!=','&&clientCommand.charAt(i)!=' ')
                        {
                            pname+=clientCommand.charAt(i);
                        }
                        else 
                        {
                            break;
                        }
                    }
                    if(clientCommand.charAt(i)==',')
                    {
                        i++;
                    }
                    // i++;
                    i++;
                    name[j] = pname;
                    j++;
                }
                for(;i<n;i++)
                {
                    if(clientCommand.charAt(i)!=' ')
                    {
                        tnum+=clientCommand.charAt(i);
                    }
                    else 
                    {
                        break;
                    }
                }
                i++;
                for(;i<n;i++)
                {
                    if(clientCommand.charAt(i)!=' ')
                    {
                        dt+=clientCommand.charAt(i);
                    }
                    else 
                    {
                        break;
                    }
                }
                i++;
                for(;i<n;i++)
                {
                    if(clientCommand.charAt(i)!=' ')
                    {
                        type+=clientCommand.charAt(i);
                    }
                    else 
                    {
                        break;
                    }
                }
                
                query = "select check_train(" + tnum + ",'" + dt + "'," + nump +",'" + type + "');" ;
                try {
                   
                        res = st.executeQuery(query);
                        res.next();
                    
                    
                    
                    String s = res.getString("check_train");
                    if(s.charAt(0)=='T')
                    {
                        printWriter.println(Border);
                        printWriter.println(clientCommand);
                        printWriter.println(s);
                    }
                    else if(s.charAt(0)=='Y')
                    {
                        printWriter.println(Border);
                        printWriter.println(clientCommand);
                        String total_seat = "",seat="";
                        i=4;
                        while(s.charAt(i)!=' ')
                        {
                            total_seat+=s.charAt(i);
                            i++;
                        }
                        i++;
                        while(s.charAt(i)==' ')
                        {
                            i++;
                        }
                        while(i<s.length()&&s.charAt(i)!=' ')
                        {
                            seat+=s.charAt(i);
                            i++;
                        }
                        Integer total_seat_num = Integer.parseInt(total_seat);
                        Integer passenger_seat = Integer.parseInt(seat);
                        total_seat_num++;
                        String tpnr =   tnum +"-"+ dt.substring(2, 4)+"." + dt.substring(5, 7)+"." + dt.substring(8, 10)+"-" ;
                        Integer int_pnr = total_seat_num;
                        for(int z=0;z<num;z++)
                        {
                            
                            passenger_seat++;
                            String coach ="",berth_type="";
                            Integer k;
                            if(type.equals("SL"))
                            {
                                k = passenger_seat/24;
                                
                            }
                            else
                            {
                               k = passenger_seat/18;
                            }
                            if(passenger_seat%24!=0)
                            {
                                k++;
                            }
                            
                            coach = type + "-" + k.toString();
                            int berth_seat =0;
                            if(type.equals("SL"))
                            {
                                k = passenger_seat%24;
                                berth_seat = passenger_seat%24;
                                if(berth_seat==0)
                                {
                                    berth_seat =24;
                                    k=24;
                                }
                                k=k%8;
                            }
                            else
                            {
                                k = passenger_seat%18;
                               berth_seat = passenger_seat%18;
                               if(berth_seat==0)
                                {
                                    berth_seat =18;
                                    k=18;
                                }
                               k=k%6;
                            }
                            if(type.equals("SL"))
                            {
                                if(k==1||k==4)
                                {
                                    berth_type = "LB";
                                }
                                else if(k==2||k==5)
                                {
                                    berth_type = "MB";
                                }
                                else if(k==3||k==6)
                                {
                                    berth_type = "UB";
                                }
                                else if(k==7)
                                {
                                    berth_type = "SL";
                                }
                                else 
                                {
                                    berth_type = "SU";
                                }
                            }
                            else
                            {
                                if(k==1||k==2)
                                {
                                    berth_type = "LB";
                                }
                                else if(k==3||k==4)
                                {
                                    berth_type = "UB";
                                }
                                else if(k==5)
                                {
                                    berth_type = "SL";
                                }
                                else 
                                {
                                    berth_type = "SU";
                                }
                            }
                            query = "select book_ticket('"+ tpnr + int_pnr.toString() + "'," + tnum + ",'" + dt +"','" + name[z]+ "'"+ ",'"+ berth_type +"'," + berth_seat+",'" + coach + "');" ;
                                    res = st.executeQuery(query);
                            printWriter.println(tpnr + int_pnr.toString() + " " + tnum +" "+ dt +" "+ name[z] +" "+ berth_type +" " +berth_seat+" " + coach);
                            int_pnr++;
                        }
                        
                        
                    }
                    else 
                    {
                        printWriter.println(Border);
                        printWriter.println(clientCommand);
                        printWriter.println("Seat is not avaiable");
                    }
                    connection.commit();

                }
                catch (SQLException e) {
                    System.out.println("Connection Error");
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }    
                //  Sending data back to the client
                // printWriter.println(responseQuery);
                // Read next client query
                clientCommand = bufferedInput.readLine(); 
            }
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e + "here---\n");
            }
            inputStream.close();
            bufferedInput.close();
            outputStream.close();
            bufferedOutput.close();
            printWriter.close();
            socketConnection.close();
        }
        catch(IOException e)
        {
            return;
        }
    }
}

/**
 * Main Class to controll the program flow
 */
public class ServiceModule 
{
    // Server listens to port
    static int serverPort = 7008;
    // Max no of parallel requests the server can process
    // static int numServerCores = 20; 
    static int numServerCores =Runtime.getRuntime().availableProcessors();


    //------------ Main----------------------
    public static void main(String[] args) throws IOException 
    {    
        // Creating a thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(numServerCores);
        
        try (//Creating a server socket to listen for clients
        ServerSocket serverSocket = new ServerSocket(serverPort)) {
            Socket socketConnection = null;
            
            // Always-ON server
            while(true)
            {
                System.out.println("Listening port : " + serverPort + "\nWaiting for clients...");
                socketConnection = serverSocket.accept();   // Accept a connection from a client
                System.out.println("Accepted client :" + socketConnection.getRemoteSocketAddress().toString() + "\n");
                //  Create a runnable task
                Runnable runnableTask = new QueryRunner(socketConnection);
                //  Submit task for execution   
                executorService.submit(runnableTask);
            }
        }
    }
}

