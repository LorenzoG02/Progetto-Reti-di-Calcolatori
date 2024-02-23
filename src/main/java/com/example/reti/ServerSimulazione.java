package com.example.reti;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

// Server per la simulazione del traffico al casello
public class ServerSimulazione {
    // Dichiarazione della mappa per i dispositivi Telepass
    private static Map<String, Boolean> telepassDevices = new HashMap<>();

    // Metodo per ottenere la mappa dei dispositivi Telepass
    public static Map<String, Boolean> getTelepassDevices() {
        return telepassDevices;
    }

    // Metodo principale per avviare il server di simulazione
    public static void main(String[] args) {
        try {
            // Creazione del ServerSocket sulla porta 8000 per accettare connessioni dai client di simulazione
            ServerSocket serverSocket = new ServerSocket(8000);

            while (true) {
                // Accettazione della connessione in ingresso da parte di un client
                Socket clientSocket = serverSocket.accept();

                // Gestione della simulazione del client in un thread separato, passando anche la mappa dei dispositivi Telepass
                Thread thread = new Thread(new ServerSimulazioneHandler(clientSocket, telepassDevices));
                thread.start(); // Avvio del thread per gestire la simulazione del client
            }
        } catch (IOException e) {
            e.printStackTrace(); // Gestione delle eccezioni di I/O
        }
    }
}

