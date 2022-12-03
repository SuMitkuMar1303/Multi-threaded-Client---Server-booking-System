import java.sql.*;
import java.io.*;
import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
public class admin {
    public static void main(String[] args) throws IOException
    {
        Connection connect = null;
        Statement st = null;
        ResultSet res = null;
        String query = "";
        try {
            Class.forName("org.postgresql.Driver");
            connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","12345");
            if(connect!=null)
            {
                System.out.println("Connection Done!");
            }
            else
            {
                System.out.println("Connection Failed!");
            }
            connect.setAutoCommit(false);
            st = connect.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
        File file = new File("C:\\Users\\kumar\\Desktop\\trail\\DBMSproject\\src\\admin_input.txt");
        FileWriter output = new FileWriter("C:\\Users\\kumar\\Desktop\\trail\\DBMSproject\\src\\admin_output.txt",true);
        BufferedWriter b = new BufferedWriter(output);
        // Path fileName = Path.of("C:\\Users\\kumar\\Desktop\\DBMSproject\\src\\admin_output.txt");
 
        // Writing into the file
        
        BufferedReader br;
        String s="",border = "---------------------------------------------------------------";
        try 
        {
            br =  new BufferedReader(new FileReader(file));
            s = br.readLine();
        
            while (s!=null)
            {
                String tnum="",date = "",ac="",sl="";
                int i=0;
                while(s.charAt(i)!=' ')
                {
                    tnum += s.charAt(i);
                    i++;
                }
                i++;
                while(s.charAt(i)==' ')
                {
                    i++;
                }
                while(s.charAt(i)!=' ')
                {
                    date += s.charAt(i);
                    i++;
                }
                i++;
                while(s.charAt(i)==' ')
                {
                    i++;
                }
                while(s.charAt(i)!=' ')
                {
                    ac += s.charAt(i);
                    i++;
                }
                i++;
                while(s.charAt(i)==' ')
                {
                    i++;
                }
                System.out.println(i + "----------" + s.length());
                while(i<s.length()&&s.charAt(i)!=' ')
                {
                    sl += s.charAt(i);
                    System.out.println(i + "----------");
                    i++;
                }
                query = "select add_train("+tnum + ",'" + date + "'," + ac + ","+sl +");";
                System.out.println(query);
                res =st.executeQuery(query);
                res.next();
                String x= res.getString("add_train");
                System.out.println(x + "----------");
                // Files.writeString(fileName,x);
                b.write(s);
                b.newLine();
                b.write(x);
                b.newLine();
                b.write(border);
                b.newLine();
                s=br.readLine();
                connect.commit();
            }
            connect.close();
            b.close();
        } 
        catch (Exception e) {
            System.out.println(e);
        }

    }
}
