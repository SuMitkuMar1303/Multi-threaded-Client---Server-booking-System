import java.util.concurrent.ExecutorService ;
import java.util.concurrent.Executors   ;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;
// import org.postgresql.jdbc.PgConnection.AbortCommand;

import java.io.IOException  ;
import java.io.File;


    
//======================================================================================================================================

public class client
{

    static Lock lock  = new ReentrantLock();
    static File arr[];
    static int Index=-1;
    // public static String get_input_file()
    // {
    //     String s;
    //     lock.lock();
    //     //----------------------------------------------------lock-----------------------------------------------------------
    //     Index++;
        
    //     if(Index==arr.length)
    //     {
    //         s="";
    //         return s;
    //     }
    //     s=arr[Index].getName();

    //     System.out.println("Query sent from " + Thread.currentThread().getName()+"    " + Index + "   " + s + "----------here\n");
    //     lock.unlock();
    //     //----------------------------------------------------unlock---------------------------------------------------------
    //     return s;
    // }

    public static void main(String args[])throws IOException
    {

        String maindirpath = "./Input/";
        // File object
        File maindir = new File(maindirpath);
        if (maindir.exists() && maindir.isDirectory()) {
            ;
        }
        else
        {
            System.out.println("not good====\n");
            System.exit(99);
        }
        
        arr=maindir.listFiles();

        System.out.println("Total number of input files --- "+arr.length );

        int thread_in_secondlevel=5;

        // int firstLevelThreads =  Runtime.getRuntime().availableProcessors();
        int firstLevelThreads =  4;

        // double firstLevelThreads =  Math.ceil(arr.length/thread_in_secondlevel);



        // Creating a thread pool
        ExecutorService executorService =Executors.newFixedThreadPool(firstLevelThreads);
        int i=0;
        int a =thread_in_secondlevel-1;
        Runnable r = new invokeWorkers(arr,a); 

        while(i<arr.length)
        {
            Runnable runnableTask = new invokeWorkers(i);    //  Pass arg, if any to constructor sendQuery(arg)
            executorService.submit(runnableTask) ;
            i+=thread_in_secondlevel;
            // System.out.println("=================" + i+ "=========================================\n");
        }

        try
        {    // Wait for 8 sec and then exit the executor service
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS))
            {
                executorService.shutdownNow();
            } 
        } 
        catch (InterruptedException e)
        {
            executorService.shutdownNow();
        }
    }
}
