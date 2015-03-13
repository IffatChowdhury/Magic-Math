/*****************************Binder Program*********************************/

package OS_Project2;
import java.net.*;
import java.io.*;

/**********************main class*******************************************/
public class Binder 
{
    static String[][] RRM = new String[4][4];                               //keep RRM table
    static String[] query_result = new String[4];                           //send information to client
    static int index = 0;                                                   //keep row track in RRM table
    
    /****************query lookup********************************************/
    
    public static void lookUp(String q)                                     //look up in table whether object exists
    {
        int j,k,location = 0;
        for(j = 0; j < 4; j++)
        {
            if (RRM[j][0].equals(q))
                location = j;
        }
        for(k = 0; k < 4; k++)
        {
            query_result[k] = RRM[location][k];                             //for sending result to client
        }
    }//lookUp
    
    /*******************binding**********************************************/
    
   public static void binding(String[] a)                                   //if object does not exist bind it
   {
       int i,j;
       for(i = 0,j = 1; i < 4; i++, j++)
       {
           RRM[index][i] = a[j];                                            //augment RRM table
       }
       index++;
   }//binding
    
    /************************main function**********************************/
    
    public static void main(String args[]) 
    {
        int pp = 1830;
        Socket socket = null;
        String[] query = new String[5];
        
        while(true)
        {
            try
            {
                ServerSocket sersocket =  new ServerSocket(pp);
                System.out.println("Binder is Waiting for Connection..... ");
                socket = sersocket.accept();
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                Object o1 = input.readObject();
                query = (String[])o1;
              
                if (query[0].equals("Server"))                              //got information from server
                {
                    System.out.println("Connected with Server");
                    binding(query);                                         //bind object
                    for(int k = 0; k < 5; k++)
                        query[k] = null;
                    /*for(int in = 0; in < 4; in++)
                    {
                        for(int jn = 0; jn < 4; jn++)
                        {
                            System.out.print(RRM[in][jn]);
                            System.out.print(" ");
                        }
                        System.out.println();
                    }*/
                }
                else if (query[0].equals("Client"))                         //got query from client
                {
                    System.out.println("Connected with Client");
                    lookUp(query[1]);                                       //look up for client
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    output.writeObject(query_result);                       //send look up result to client
                    for(int k = 0; k < 2; k++)
                        query[k] = null;
                    System.out.println("Send Query Result to Client");
                    output.flush();
                    output.close();
                    input.close();
                }
                socket.close();
                sersocket.close();
            }
            catch(SocketException e) 
            {
                System.out.println("SocketException " + e);
            }
            catch(Exception e) 
            {
                System.out.println("Exception " + e);
            }
        }//while
    } // main 
}   // Class Client 

