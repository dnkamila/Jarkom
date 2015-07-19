import java.net.*;
import java.io.*;

public class GreetingServer extends Thread
{
   private ServerSocket serverSocket;
   private FileInputStream fis;
   private BufferedInputStream bis;
   
   public GreetingServer(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(3600000);
   }

   public void run()
   {
      while(true)
      {
         try
         {
            System.out.println("Waiting for client on port " +
            serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to "
                  + server.getRemoteSocketAddress());
            DataInputStream in =
                  new DataInputStream(server.getInputStream());
            System.out.println(in.readUTF());
            DataOutputStream out =
                 new DataOutputStream(server.getOutputStream());
            out.writeUTF("Thank you for connecting to "
              + server.getLocalSocketAddress() + "\nGoodbye!");
            server.close();
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   public static void main(String [] args)
	{
		try
		{
			BufferedReader read = new BufferedReader(new FileReader(".\\DaftarIP.txt"));
			String input = "";
			//ini untuk multiple socket
			while ((input = read.readLine()) != null)
			{
				String[] daftarUser = input.split(";");
				String namaUser = daftarUser[1];
				String IP = daftarUser[0].split(":")[0];
				String port = daftarUser[0].split(":")[1];
				Thread t = new GreetingServer(Integer.parseInt(port));
				t.start();
				System.out.println(namaUser + " " + IP + " " + port);
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
