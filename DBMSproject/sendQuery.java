import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;





class sendQuery implements Runnable 
{   /**********************/
     int sockPort = 7008 ;
    /*********************/
    String s;
    // static File arr[];
    // static int index=-1;
    sendQuery(String a)
    {
     // Red args if any
     s=a;
    }   
    
    @Override
    public void run()
    {
        try 
        {
            //Creating a client socket to send query requests
            Socket socketConnection = new Socket("localhost", sockPort) ;
            
            // Files for input queries and responses

            //--------------------------------


            // String s=get_input_file();
            String inputfile = "./Input/" + s;
            // System.out.println(Thread.currentThread().getName()+"   "+inputfile + "-----------send Query here-\n");
            String outputfile = "./Output/" + s ;

            //---------------------------------

            //-----Initialising the Input & ouput file-streams and buffers-------
            OutputStreamWriter outputStream = new OutputStreamWriter(socketConnection.getOutputStream());
            BufferedWriter bufferedOutput = new BufferedWriter(outputStream);
            InputStreamReader inputStream = new InputStreamReader(socketConnection.getInputStream());
            BufferedReader bufferedInput = new BufferedReader(inputStream);
            PrintWriter printWriter = new PrintWriter(bufferedOutput,true);
            File queries = new File(inputfile); 
            File output = new File(outputfile); 
            FileWriter filewriter = new FileWriter(output);
            Scanner queryScanner = new Scanner(queries);
            String query = "";
            //--------------------------------------------------------------------

            // Read input queries and write to the output stream
            while(queryScanner.hasNextLine())
            {
                query = queryScanner.nextLine();
                printWriter.println(query);
            }

            // System.out.println("Query sent from " + Thread.currentThread().getName()+ "   " + s + "\n");

            System.out.println("Initiating file --- " + s );

            // Get query responses from the input end of the socket of client
            String result;
            while( (result = bufferedInput.readLine()) != null)
            {
                filewriter.write(result + "\n");
            }    
            // close the buffers and socket
            filewriter.close();
            queryScanner.close();
            printWriter.close();
            socketConnection.close();
        } 
        catch (IOException e1)
        {
            e1.printStackTrace();
        }   
    }
}