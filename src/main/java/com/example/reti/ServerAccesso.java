package com.example.reti;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerAccesso {
    public static void main(String[] args) {
        try {
            // Creazione del ServerSocket sulla porta 54321 per accettare connessioni dai client di accesso
            ServerSocket serverSocket = new ServerSocket(54321);

            while (true) {
                // Accettazione della connessione in ingresso da parte di un client
                Socket clientSocket = serverSocket.accept();

                // Gestione della connessione del client in un thread separato
                Thread thread = new Thread(new ServerAccessoHandler(clientSocket));
                thread.start(); // Avvio del thread per gestire la connessione del client
            }
        } catch (IOException e) {
            // Gestione delle eccezioni di I/O
            e.printStackTrace();
        }
    }
}
