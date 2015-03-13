package OS_Project2;

import java.net.*;
import java.io.*;
import java.util.Random;

/****************************basic math class********************************/

class BasicMathClas                                                                //basic math class
{
    int counter;
    Client o = new Client();
    
    public int magicAdd(int a, int b)                                               //magic add
    {
        int result;
        result = o.remoteMethod1(a,b,"magicAdd");
        return result;
    }
        
    public int magicSubtract(int a, int b)                                          //magic subtract
    {
        int result;
        result = o.remoteMethod1(a,b,"magicSubtract");
        return result;
    }
    
    public int objectCountBasic(String ob)                                          //return counter value for basic class object
    {
        return o.counter(ob);
    }
}//basic math class

/***********************advanced math class***********************************/

class AdvancedMathClas                                                              //advanced class
{
    int counter;
    Client o = new Client();
    public int magicFindMin(int a, int b, int c)                                    //find min
    {
        return o.remoteMethod2(a,b,c,"magicFindMin");
    }
    
    public int magicFindMax(int a, int b, int c)                                    //find max
    {
        return o.remoteMethod2(a,b,c,"magicFindMax");
    }
    
    public int objectCountAdvanced(String ob)                                       //return counter value for basic class object
    {
        return o.counter(ob);
    }
}//advanced math class

/**************************main class**********************************/

public class Client {
    static String[] query_result = new String[4];
    static int port = 0;
    static InetAddress ip = null;
    static String[] objList = new String[] {"ROR11","ROR12","ROR21","ROR22"}; 
    static String[][] storeRRM = new String[4][4];                                  //keep RRM table
    static int index = 0;
    static String[] methodCall = new String[5];                                     //parameter marshalling
    static int receivedResult = 0;
    static int parameter1 = 0, parameter2 = 0, parameter3 = 0;
    static String objectName = null;
    static int randomInt = 0;
    static Random randomGenerator1 = new Random();
    static BasicMathClas ROL11 = new BasicMathClas();
    static BasicMathClas ROL12 = new BasicMathClas();
    static AdvancedMathClas ROL21 = new AdvancedMathClas();
    static AdvancedMathClas ROL22 = new AdvancedMathClas();
    
    /**********************connect with server*****************************/
    
    static void ConnectWithServer()
    {
        for(int k = 1; k <= 1000; k++)                                              //make 1000 requests
        {
            ConnectWithBinder();  
            if(storeRRM[randomInt][0].equals("ROR11"))
                proxy1(storeRRM[randomInt][0]);
            else if(storeRRM[randomInt][0].equals("ROR12"))
                proxy2(storeRRM[randomInt][0]);
            else if(storeRRM[randomInt][0].equals("ROR21"))
                proxy3(storeRRM[randomInt][0]);
            else if(storeRRM[randomInt][0].equals("ROR22"))
                proxy4(storeRRM[randomInt][0]);//find for the remote objects
        }
    }//connect with server function
    
    /******************remote counter call******************************/
    
    public int counter(String o)
    {
        int r = 0;
    try
        {
            for(int kin = 0; kin < 5; kin++)
                methodCall[kin] = null;
            methodCall[0] = "count";
            methodCall[1] = o;
            Socket socket = null;
            socket = new Socket(ip, port);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(methodCall);                                             //method invoke from server
            output.flush();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            r = in.readInt();                                                           //get result
            socket.close();
            in.close();
            output.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception " + e);
        }
        return r;                                                                       //return result to proxy
    }
     
    /****************call remote method**********************/
    
    public int remoteMethod1(int i1, int i2, String s)
    {   
        int r = 0;
        methodCall[1] = s;
        methodCall[2] = Integer.toString(i1);
        methodCall[3] = Integer.toString(i2);
        try{
                Socket socket = null;
                socket = new Socket(ip, port);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Connected with Server");
                output.writeObject(methodCall);                                         //method invoke
                output.flush();
                System.out.println("Method Invoked");
                DataInputStream in = new DataInputStream(socket.getInputStream());
                r = in.readInt();                                                       //get result
                System.out.println("Got Method Result from Server");
                socket.close();
                
        }
        catch(SocketException e) 
        {
           System.out.println("SocketException " + e);
        }
        catch(Exception e) 
        {
             System.out.println("Exception " + e);
        }
        return r;
    }//remote method 1
    
    /**********************remote method call*******************************/
    
    public int remoteMethod2(int i1, int i2, int i3, String s)
    {   
        int r = 0;
        methodCall[1] = s;
        methodCall[2] = Integer.toString(i1);
        methodCall[3] = Integer.toString(i2);
        methodCall[4] = Integer.toString(i3);
        try{
                Socket socket = null;
                socket = new Socket(ip, port);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Connected with Server");
                output.writeObject(methodCall);                                             //method invoke
                output.flush();
                System.out.println("Method Invoked");
                DataInputStream in = new DataInputStream(socket.getInputStream());
                r = in.readInt();                                                           //get result
                System.out.println("Got Method Result from Server");
                socket.close();
                
        }
        catch(SocketException e) 
        {
           System.out.println("SocketException " + e);
        }
        catch(Exception e) 
        {
             System.out.println("Exception " + e);
        }
        return r;
    }//remote method 2
    
    /***************basicMathCall********************************/
    
    static void proxy1(String objct)                                                        //proxy 
    {
        int k = 0;
        int re = 0;
        Random randomGenerator = new Random();
        int methodRandom1 = randomGenerator.nextInt(2);
        for(k = 0; k < 5; k++)
            methodCall[k] = null;
        
        if(methodRandom1 == 0)                                                              //choose one method
        {
            methodCall[0] = objct; 
            parameter1 = randomGenerator1. nextInt(100);
            parameter2 = randomGenerator1. nextInt(100);
            re = ROL11.magicAdd(parameter1,parameter2);
        }
        else if(methodRandom1 == 1)
        {
            methodCall[0] = objct; 
            parameter1 = randomGenerator1. nextInt(100);
            parameter2 = randomGenerator1. nextInt(100);
            re = ROL11.magicSubtract(parameter1,parameter2);
        }
    }//basic method call
    
    /***************basicMathCall********************************/
    
    static void proxy2(String objct)                                                        //proxy 
    {
        int k = 0;
        int re = 0;
        Random randomGenerator = new Random();
        int methodRandom1 = randomGenerator.nextInt(2);
        for(k = 0; k < 5; k++)
            methodCall[k] = null;
        
        if(methodRandom1 == 0)                                                              //choose one method
        {
            methodCall[0] = objct; 
            parameter1 = randomGenerator1. nextInt(100);
            parameter2 = randomGenerator1. nextInt(100);
            re = ROL12.magicAdd(parameter1,parameter2);
        }
        else if(methodRandom1 == 1)
        {
            methodCall[0] = objct; 
            parameter1 = randomGenerator1. nextInt(100);
            parameter2 = randomGenerator1. nextInt(100);
            re = ROL12.magicSubtract(parameter1,parameter2);
        }
    }//basic method call

    /********************advancedMathCall************************************/
    
    static void proxy3(String objct)                                                        //proxy
    {
        int k = 0;
        int re = 0;
        Random randomGenerator = new Random();
        int methodRandom2 = randomGenerator.nextInt(2);
        for(k = 0; k < 5; k++)
            methodCall[k] = null;
              
        if(methodRandom2 == 0)                                                              //choose one method
        {
            methodCall[0] = objct; 
            parameter1 = randomGenerator1. nextInt(100);
            parameter2 = randomGenerator1. nextInt(100);
            parameter3 = randomGenerator1. nextInt(100);
            re = ROL21.magicFindMin(parameter1,parameter2,parameter3);
        }
        else if(methodRandom2 == 1)                                                         //choose one method
        {
            methodCall[0] = objct; 
            parameter1 = randomGenerator1. nextInt(100);
            parameter2 = randomGenerator1. nextInt(100);
            parameter3 = randomGenerator1. nextInt(100);  
            re = ROL21.magicFindMax(parameter1,parameter2,parameter3);

        }
    }//advanced method call
    
    /********************advancedMathCall************************************/
    
    static void proxy4(String objct)                                                        //proxy
    {
        int k = 0;
        int re = 0;
        Random randomGenerator = new Random();
        int methodRandom2 = randomGenerator.nextInt(2);
        for(k = 0; k < 5; k++)
            methodCall[k] = null;
        
        if(methodRandom2 == 0)                                                              //choose one method
        {
            methodCall[0] = objct; 
            parameter1 = randomGenerator1. nextInt(100);
            parameter2 = randomGenerator1. nextInt(100);
            parameter3 = randomGenerator1. nextInt(100);
            re = ROL22.magicFindMin(parameter1,parameter2,parameter3);

        }
        else if(methodRandom2 == 1)                                                         //choose one method
        {
            methodCall[0] = objct; 
            parameter1 = randomGenerator1. nextInt(100);
            parameter2 = randomGenerator1. nextInt(100);
            parameter3 = randomGenerator1. nextInt(100);
            re = ROL22.magicFindMax(parameter1,parameter2,parameter3);
        }
    }//advanced method call

    
    /****************connect with binder************************************/
    
    static void ConnectWithBinder()
    {
        String[] query = new String[2];
        query[0] = "Client";
        int flag = 0;
        
        try
        {
            Random randomGenerator = new Random();                                          //random number generate
            randomInt = randomGenerator.nextInt(4);
            if (storeRRM[randomInt][2] != null)                                             //choose object randomly
                {
                    flag = 1;
                }
            if(flag == 1)                                                                   //if object found in local RRM
            {
                port = Integer.parseInt(storeRRM[randomInt][3]);
                ip = InetAddress.getByName(storeRRM[randomInt][2]);
            }
            else                                                                            //if object is not found in local RRM
            {
                query[1] = storeRRM[randomInt][0];
                InetAddress ipp = InetAddress.getByName("localhost");
                Socket sock = new Socket(ipp, 1830);
                System.out.println("Connected with Binder");
                ObjectOutputStream ois = new ObjectOutputStream(sock.getOutputStream());
                ois.writeObject(query);                                                     //send query to binder
                ois.flush();
                ObjectInputStream iis = new ObjectInputStream(sock.getInputStream());
                Object o1 = iis.readObject();
                query_result = (String[])o1;
                storeRRM[randomInt][3] = query_result[3];storeRRM[randomInt][2] = query_result[2];
                port = Integer.parseInt(storeRRM[randomInt][3]);                            //take port from binder
                ip = InetAddress.getByName(storeRRM[randomInt][2]);                         //take ip from binder
                System.out.println("Got Result from Binder");
                sock.close();
            }
            flag = 0;
           
            
        }
        catch(SocketException e) 
        {
            System.out.println("SocketException " + e);
        }
        catch(Exception e) 
        {
            System.out.println("Exception " + e);
        }
    }//connect with binder function
    
    /*******************main function**************************************/
    
     public static void main(String args[]) 
     {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                storeRRM[i][j] = null;
        storeRRM[0][0]="ROR11";storeRRM[1][0]="ROR12";storeRRM[2][0]="ROR21";storeRRM[3][0]="ROR22";
        storeRRM[0][1]="ROL11";storeRRM[1][1]="ROL12";storeRRM[2][1]="ROL21";storeRRM[3][1]="ROL22";
        
        ConnectWithServer();                                                                //connection starts
        int r = ROL11.objectCountBasic("ROR11");                                            //counter call 
        System.out.println("Object One Called " + r + " times");
        r = ROL12.objectCountBasic("ROR12");
        System.out.println("Object Two Called " + r + " times");
        r = ROL21.objectCountAdvanced("ROR21");
        System.out.println("Object Three Called " + r + " times");
        r = ROL22.objectCountAdvanced("ROR22");
        System.out.println("Object Fourth Called " + r + " times");
        
        
     }//main
}//class

