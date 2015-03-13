/***********************Server Program***************************************/

package OS_Project2;
import java.net.*;
import java.io.*;
import java.util.concurrent.locks.ReentrantLock;

/****************************basic math class********************************/

class BasicMathClass                                                                //basic math class
{
    int counter;
    
    BasicMathClass(int c)                                                           //constructor
    {
        counter = c;
    }
    
    public int magicAdd(int a, int b)                                               //magic add
    {
        int result;
        result = a - b;
        return result;
    }
        
    public int magicSubtract(int a, int b)                                          //magic subtract
    {
        int result;
        result = a + b;
        return result;
    }
    
    public int objectCountBasic(BasicMathClass ob)                                  //return counter value for basic class object
    {
        return ob.counter;
    }
}//basic math class

/***********************advanced math class***********************************/

class AdvancedMathClass                                                             //advanced class
{
    int counter;
    
    AdvancedMathClass(int c)                                                        //constructor
    {
        counter = c;
    }
    
    public int magicFindMin(int a, int b, int c)                                    //find min
    {
        int max  = a;
        if (b > max) 
            max = b;
        if (c > max)
            max = c;                
        return max;
    }
    
    public int magicFindMax(int a, int b, int c)                                    //find max
    {
        int min  = a;
        if (b < min) 
            min = b;
        if (c < min)
            min = c;                
        return min;
    }
    
    public int objectCountAdvanced(AdvancedMathClass ob)                            //return counter value for basic class object
    {
        return ob.counter;
    }
}//advanced math class

/********************implementation of thread*******************************/

class ServerForClient extends Thread                                                //handle multiple client connection
{
   private ServerSocket sersocket;
   
   /******************constructor***************************************/
   
   public ServerForClient(int port) throws IOException
   {
      sersocket = new ServerSocket(port);
   }

  /************thread starts from here***********************************/
   
   public void run()                                                                //thread handler
   {
      Server ob = new Server();
      int r = 0, count = 0;
      
      while(true)
      {
         try
         {
            System.out.println("Server is Waiting for Clients..... ");
            Socket socket = sersocket.accept();
            System.out.println("Client Connected");
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            Object o1 = input.readObject();                                         //clients want service
            String[] message = (String[])o1;
            ob.query_copy(message);
            System.out.println("Method Invoked from Client");
            
            if (message[0].equals("count"))                                         //client invoke counter value
            {
                if(message[1].equals("ROR11")) count = ob.basicMethodCount(1); 
                else if (message[1].equals("ROR12")) count = ob.basicMethodCount(2);
                else if(message[1].equals("ROR21")) count = ob.advancedMethodCount(1); 
                else if(message[1].equals("ROR22")) count = ob.advancedMethodCount(2);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                System.out.println("Send Result to Client");
                out.writeInt(count);  
                out.flush();
            }
                
            else if ( (message[0].equals("ROR11")) || (message[0].equals("ROR12")) )
            {
                r = ob.basicMethodCall();                                           //basic class skeleton
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                System.out.println("Send Result to Client");
                out.writeInt(r);                                                    //send method result to client
                out.flush();
            }
            
            else
            {
                r = ob.advancedMethodCall();                                        //advanced class skeleton
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                System.out.println("Send Result to Client");
                out.writeInt(r);                                                    //send method result to client
                out.flush();
            }
            
            socket.close();
            input.close();
         }
         catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }
         catch(IOException e)
         {
            e.printStackTrace(System.out);
            break;
         }
         catch(Exception e)
         {
            System.out.println("Exception " + e);
         }
      }//while
   }//run
}//class thread

/*************************main class*************************************/

public class Server 
{
    static int PORT = 1225;
    static int PortForClient = 2730;
    static String[][] RRM = new String[4][5];
    BasicMathClass obj11 = new BasicMathClass(0);
    BasicMathClass obj12 = new BasicMathClass(0);
    AdvancedMathClass obj21 = new AdvancedMathClass(0);
    AdvancedMathClass obj22 = new AdvancedMathClass(0);
    String[] fromOutside = new String[5];
    
    public void query_copy(String [] a)                                             //copy client query
    {
        for(int i = 0; i < 5; i++)
            fromOutside[i] = a[i];
    }
    
    /***********************handle RRM****************************************/
    
    public static void startRRM()                                                   //RRM module start
    {
        String address = null;
        String P = Integer.toString(PortForClient);                                 //get port
        try
        {
            InetAddress addy = InetAddress.getLocalHost();                          //get local host address
            address = addy.getHostAddress();
        }
        catch(Exception e)
        {
            System.out.println("Couldn't start " + e.getMessage());
        }
        //make RRM table
        RRM[0][0] = "Server";RRM[0][1] = "ROR11";RRM[0][2] = "obj11";RRM[0][3] = address;RRM[0][4] = P;
        RRM[1][0] = "Server";RRM[1][1] = "ROR12";RRM[1][2] = "obj12";RRM[1][3] = address;RRM[1][4] = P; 
        RRM[2][0] = "Server";RRM[2][1] = "ROR21";RRM[2][2] = "obj21";RRM[2][3] = address;RRM[2][4] = P;
        RRM[3][0] = "Server";RRM[3][1] = "ROR22";RRM[3][2] = "obj22";RRM[3][3] = address;RRM[3][4] = P;
        
        try
        {
            for(int k = 0; k < 4; k++)                                              //send RRM table one by one to binder
            {
                InetAddress ip = InetAddress.getByName("localhost");                
                Socket sock = new Socket(ip, 1830);
                System.out.println("Connect with Binder");
                ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(RRM[k]);                                            //publish remote object to the name service
                oos.flush();
                oos.close();
                sock.close();
                Thread.sleep(10);
            }
        }
        catch(Exception e)
        {
            System.out.println("Couldn't start " + e.getMessage()) ;     
        }
    }//startRRM function
    
     /*****************basic method call**********************************/
   
   public int basicMethodCall()                                                     //basic class skeleton
   {
       int r = 0;
       int param1 = Integer.parseInt(fromOutside[2]);
       int param2 = Integer.parseInt(fromOutside[3]);
       ReentrantLock l1 = new ReentrantLock();  

       for(int k = 0; k < 2; k++)
       {
           if ( RRM[k][1].equals(fromOutside[0]) )
           {
               if (RRM[k][2].equals("obj11"))                                       //select object
               {
                   try
                   {
                        l1.lock();  
                        obj11.counter++;                                            //increment counter
                   }
                   finally
                   {
                        l1.unlock();
                   }
                   if(fromOutside[1].equals("magicAdd"))                            //select method
                       r = obj11.magicAdd(param1, param2);                          //call method
                   else if(fromOutside[1].equals("magicSubtract"))
                       r = obj11.magicSubtract(param1, param2);
                   break;
               }
               else if (RRM[k][2].equals("obj12"))                                  //select object
               {
                   try
                   {
                        l1.lock();  
                            obj12.counter++;                                        //increment counter
                   }
                   finally
                   {
                        l1.unlock();
                   }                                                                //increment counter
                   if(fromOutside[1].equals("magicAdd"))                            //select method
                       r = obj12.magicAdd(param1, param2);                          //call method
                   else if(fromOutside[1].equals("magicSubtract"))
                       r = obj12.magicSubtract(param1, param2);
                   break;
               }
                   
           }
       }//search RRM
      return r;
   }//function
   
   /****************advanced method call********************************/
   
   public int advancedMethodCall()                                                  //advanced class skeleton
   {
       int r = 0;
       int param1 = Integer.parseInt(fromOutside[2]);
       int param2 = Integer.parseInt(fromOutside[3]);
       int param3 = Integer.parseInt(fromOutside[4]);
       ReentrantLock l1 = new ReentrantLock();  
       
       for(int k = 2; k < 4; k++)
       {
           if ( RRM[k][1].equals(fromOutside[0]) )
           {
               if (RRM[k][2].equals("obj21"))                                       //select object
               {
                   try
                   {
                        l1.lock();  
                        obj21.counter++;                                            //increment counter
                   }
                   finally
                   {
                        l1.unlock();
                   }                                                                //increment counter
                   if(fromOutside[1].equals("magicFindMin"))                        //select method
                       r = obj21.magicFindMin(param1, param2,param3);
                   else if(fromOutside[1].equals("magicFindMax"))
                       r = obj21.magicFindMax(param1, param2,param3);
                   break;
               }
               else if (RRM[k][2].equals("obj22"))
               {
                   try
                   {
                        l1.lock();  
                        obj22.counter++;                                            //increment counter
                   }
                   finally
                   {
                        l1.unlock();
                   }                                                                //increment counter
                   if(fromOutside[1].equals("magicFindMin"))                        //select method
                       r = obj22.magicFindMin(param1, param2,param3);
                   else if(fromOutside[1].equals("magicFindMax"))
                       r = obj22.magicFindMax(param1, param2,param3);
                   break;
               }
           }
       }//search RRM
      return r;
   }//function
   
   /***********************Call Basic Class read count**********************/
   
   public int basicMethodCount(int flag)
   {
       if (flag == 1)
           return obj11.objectCountBasic(obj11);
       else 
           return obj12.objectCountBasic(obj12);
   }//basic count
   
   /***********************Call Advanced Class read count**********************/
   
   public int advancedMethodCount(int flag)
   {
       if (flag == 1)
           return obj21.objectCountAdvanced(obj21);
       else 
           return obj22.objectCountAdvanced(obj22);
   }//advanced count
   
    /*************************client connection*****************************/
   
    static void WaitForClient()                                                     //wait for client to connect
    {
        try
        {
            Thread t = new ServerForClient(PortForClient);
            t.start();                                                              //start thread
        }
        catch(Exception e)
        {
            System.out.println("Couldn't start " + e.getMessage()) ;   
        }
    }//WaitForClient
    
   /*************************main function*********************************/ 
    public static void main(String[] args) 
    {
       startRRM();
       WaitForClient();
    } // main 
}  // Server class
   
