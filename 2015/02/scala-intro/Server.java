import java.lang.*;
import java.io.*;
import java.net.*;

class Server {
    public static void main(String args[]) {
        try {
            int serverPort = 4020;
            ServerSocket serverSocket = new ServerSocket(serverPort);
            serverSocket.setSoTimeout(10000);

            while(true) {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

                Socket server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                PrintWriter toClient =
                    new PrintWriter(server.getOutputStream(),true);
                BufferedReader fromClient =
                    new BufferedReader(
                            new InputStreamReader(server.getInputStream()));
                String line = fromClient.readLine();
                System.out.println("Server received: " + line);
                toClient.println(line);
                server.close();
            }

        } catch(Exception e) {
            System.out.print(e.getMessage());
        }
    }
}
