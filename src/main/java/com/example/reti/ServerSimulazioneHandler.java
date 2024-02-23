package com.example.reti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ServerSimulazioneHandler implements Runnable {
    private Socket clientSocket;
    private Map<String, Boolean> telepassDevices;

    // Costruttore della classe, riceve il socket del client e la mappa dei dispositivi Telepass
    public ServerSimulazioneHandler(Socket clientSocket, Map<String, Boolean> telepassDevices) {
        this.clientSocket = clientSocket;
        this.telepassDevices = ServerSimulazione.getTelepassDevices();
    }

    @Override
    public void run() {
        try {
            // Inizializzazione del BufferedReader per leggere i dati inviati dal client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String simulationData;
            // Ciclo di lettura dei dati inviati dal client
            while ((simulationData = reader.readLine()) != null) {
                // Esecuzione della simulazione e invio dei risultati al server centrale
                String risultatiSimulazione = eseguiSimulazione(simulationData);
                inviaDatiAlServerCentrale(risultatiSimulazione);
            }

        } catch (IOException e) {
            // Gestione delle eccezioni durante la lettura dei dati o la chiusura del socket
            System.err.println("Errore durante la lettura dei dati dal client: " + e.getMessage());
        } finally {
            try {
                // Chiusura del socket del client
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Errore durante la chiusura del socket: " + e.getMessage());
            }
        }
    }

    // Metodo per eseguire la simulazione in base ai dati ricevuti
    private String eseguiSimulazione(String simulationData) {
        // Suddivisione dei dati in tipo di evento e targa
        String[] parts = simulationData.split(":");
        String tipoEvento = parts[0];
        String targa = parts[1];

        // Gestione dei diversi tipi di evento
        if (tipoEvento.equals("INGRESSO")) {
            // Simulazione di ingresso al casello
            System.out.println("Macchina con targa " + targa + " è entrata al casello.");
            boolean hasTelepass = telepassDevices.getOrDefault(targa, false);
            if (!hasTelepass) {
                // Avvio di un timer per simulare l'uscita dopo 5 secondi
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Macchina con targa " + targa + " è uscita dal casello.");
                        inviaDatiAlServerCentrale("USCITA:" + targa);
                    }
                }, 5000);
            }
        } else if (tipoEvento.equals("INSERISCI_TELEPASS")) {
            // Simulazione di inserimento del dispositivo Telepass
            System.out.println("Dispositivo Telepass inserito per la macchina con targa " + targa);
            telepassDevices.put(targa, true);
        } else if (tipoEvento.equals("RIMUOVI_TELEPASS")) {
            // Simulazione di richiesta di rimozione del dispositivo Telepass
            System.out.println("Richiesta di rimozione del dispositivo Telepass per la macchina con targa " + targa);
            if (telepassDevices.containsKey(targa)) {
                boolean hasTelepass = telepassDevices.get(targa);
                if (hasTelepass) {
                    // Rimozione effettiva del dispositivo Telepass
                    System.out.println("Dispositivo Telepass rimosso per la macchina con targa " + targa);
                    System.out.println("Rimozione del Telepass avvenuta.");
                    telepassDevices.put(targa, false);
                }
            } else {
                // Il dispositivo Telepass non esiste
                System.out.println("Il dispositivo Telepass " + targa + " non esiste.");
                inviaDatiAlServerCentrale("Non esiste nessun dispositivo per la macchina con targa:" + targa);
                System.out.println("Rimozione del Telepass annullata.");
            }
        }
        // Composizione e restituzione dei risultati della simulazione
        return tipoEvento + ":" + targa;
    }

    // Metodo per inviare i risultati della simulazione al server centrale
    private void inviaDatiAlServerCentrale(String datiSimulazione) {
        try (Socket socket = new Socket("localhost", 54321);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            // Invio dei dati al server centrale
            out.println("DATI_SIMULAZIONE:" + datiSimulazione);
            System.out.println("Dati inviati al ServerAccesso con successo.");
        } catch (IOException e) {
            // Gestione delle eccezioni durante l'invio dei dati al server centrale
            System.err.println("Errore nell'invio dei dati al ServerAccesso: " + e.getMessage());
        }
    }
}
