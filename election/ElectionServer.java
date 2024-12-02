package election;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class ElectionServer {
    public static final int PORT = 1253;
    public static final String IP = "143.106.244.62";
    private ElectionData electionData;
    private Set<String> registeredCPFs;
    private ServerSocket serverSocket;
    private boolean running;

    public ElectionData getElectionData(){
        return this.electionData;
    }

    public ElectionServer(ElectionData electionData) {
        this.electionData = electionData;
        this.registeredCPFs = new HashSet<>();
        this.running = true; 
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado na porta " + PORT);
            while (running) {
                Socket clientSocket = serverSocket.accept(); 
                new ClientHandler(clientSocket).start(); 
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            } else {
                System.out.println("Servidor encerrado.");
            }
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close(); 
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeServer() {
        running = false; 
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                String cpf = (String) in.readObject();  
                int voteIndex = (int) in.readObject();  

                if (registeredCPFs.contains(cpf)) {
                    out.writeObject("CPF já registrado.");
                } else {
                    registeredCPFs.add(cpf);
                    electionData.addVote(voteIndex);  
                    out.writeObject("Voto computado com sucesso.");
                    out.writeObject(electionData);  
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close(); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

