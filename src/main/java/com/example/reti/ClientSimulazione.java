package com.example.reti;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientSimulazione {
    public static void main(String[] args) {
        try {
            // Connessione al server di simulazione sulla porta 8000
            Socket socket = new Socket("localhost", 8000);

            // Inizializzazione del PrintWriter per inviare dati al server
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Inizializzazione dello scanner per leggere l'input dell'utente da console
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Visualizzazione del menu di simulazione
                System.out.println("Menu di simulazione:");
                System.out.println("1. Simulazione ingresso casello");
                System.out.println("2. Simulazione uscita casello");
                System.out.println("3. Esci");

                // Legge la scelta dell'utente
                System.out.print("Inserisci la tua scelta: ");
                int scelta = scanner.nextInt();

                // Esegue l'azione in base alla scelta dell'utente
                switch (scelta) {
                    case 1:
                        // Simulazione di ingresso al casello
                        simulaIngresso(writer);
                        break;
                    case 2:
                        // Simulazione di uscita dal casello
                        simulaUscita(writer);
                        break;
                    case 3:
                        // Chiude la connessione e esci dal ciclo
                        socket.close();
                        return;
                    default:
                        System.out.println("Scelta non valida. Riprova.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void simulaIngresso(PrintWriter writer) {
        // Implementazione della logica di simulazione per l'ingresso al casello
        String datiSimulazione = "Ingresso al casello";
        // Invia i dati di simulazione al server
        inviaDatiDiSimulazione(writer, datiSimulazione);
    }

    private static void simulaUscita(PrintWriter writer) {
        // Implementazione della logica di simulazione per l'uscita dal casello
        String datiSimulazione = "Uscita dal casello";
        // Invia i dati di simulazione al server
        inviaDatiDiSimulazione(writer, datiSimulazione);
    }

    private static void inviaDatiDiSimulazione(PrintWriter writer, String datiSimulazione) {
        // Invia i dati di simulazione al server di simulazione
        writer.println(datiSimulazione);
        System.out.println("Simulazione inviata con successo.");
    }
}
