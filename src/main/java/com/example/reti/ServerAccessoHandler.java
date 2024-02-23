package com.example.reti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ServerAccessoHandler implements Runnable {
    private Socket clientSocket;

    // Costruttore per inizializzare il ServerAccessoHandler con il Socket del client
    public ServerAccessoHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                // Inizializzazione del BufferedReader per leggere l'input dal client
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Inizializzazione del PrintWriter per scrivere l'output al client
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        )
        {
            String inputLine;
            // Loop per leggere continuamente l'input dal client
            while ((inputLine = reader.readLine()) != null) {
                // Verifica se l'input contiene dati di simulazione
                if (inputLine.startsWith("DATI_SIMULAZIONE:")) {
                    // Estrae i dati di simulazione dall'input
                    String datiSimulazione = inputLine.substring("DATI_SIMULAZIONE:".length());
                    // Stampa i dati di simulazione ricevuti
                    System.out.println("Ricevuti dati di simulazione: " + datiSimulazione);
                    // Registra i dati di simulazione
                    logDatiSimulazione(datiSimulazione);
                    // Invia conferma al client che i dati di simulazione sono stati ricevuti
                    writer.println("Conferma ricezione dati simulazione");
                } else {
                    // Gestisce le richieste di accesso dell'utente
                    switch (inputLine) {
                        case "amministratore":
                            // Se l'utente è un amministratore, conferma l'accesso
                            System.out.println("Accesso amministratore autorizzato.");
                            writer.println("Accesso amministratore confermato");
                            break;
                        case "utente":
                            // Se l'utente è un utente, conferma l'accesso
                            System.out.println("Accesso utente autorizzato.");
                            writer.println("Accesso utente confermato");
                            break;
                        default:
                            // Se il tipo di accesso non è valido, invia un messaggio di errore al client
                            System.out.println("Tipo di accesso non valido.");
                            writer.println("Tipo di accesso non valido");
                            break;
                    }
                }
            }
        } catch (SocketException e) {
            // Gestisce eccezioni di connessione resettata
        } catch (IOException e) {
            // Gestisce altre eccezioni di I/O
            e.printStackTrace();
        }
    }

    // Metodo per registrare i dati di simulazione
    private void logDatiSimulazione(String dati) {
        System.out.println("Log dati di simulazione: " + dati);
    }
}


