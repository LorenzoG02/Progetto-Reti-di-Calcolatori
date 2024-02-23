package com.example.reti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientAccesso {
    public static void main(String[] args) {
        // Inizializzazione di uno scanner per la lettura dell'input da console
        Scanner scanner = new Scanner(System.in);

        try {
            // Connessione al server di accesso
            try (Socket accessoSocket = new Socket("localhost", 54321);
                 PrintWriter accessoWriter = new PrintWriter(accessoSocket.getOutputStream(), true);
                 BufferedReader accessoReader = new BufferedReader(new InputStreamReader(accessoSocket.getInputStream()))) {

                // Richiesta del tipo di accesso
                System.out.println("Inserisci il tipo di accesso (amministratore/utente): ");
                String tipoAccesso = scanner.nextLine();
                accessoWriter.println(tipoAccesso);

                // Lettura e stampa della risposta dal server di accesso
                String rispostaServer = accessoReader.readLine();
                System.out.println("Risposta dal server: " + rispostaServer);

                // Connessione al server di simulazione
                try (Socket simulazioneSocket = new Socket("localhost", 8000);
                     PrintWriter simulazioneWriter = new PrintWriter(simulazioneSocket.getOutputStream(), true);
                     BufferedReader simulazioneReader = new BufferedReader(new InputStreamReader(simulazioneSocket.getInputStream()))) {

                    // Gestione del menu in base al tipo di accesso
                    if ("utente".equals(tipoAccesso)) {
                        menuUtente(scanner, simulazioneWriter, simulazioneReader);
                    } else if ("amministratore".equals(tipoAccesso)) {
                        menuAmministratore(scanner, simulazioneWriter, simulazioneReader, accessoWriter);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Chiusura dello scanner per liberare le risorse
            scanner.close();
        }
    }

    // Metodo per gestire il menù dell'utente
    private static void menuUtente(Scanner scanner, PrintWriter simulazioneWriter, BufferedReader simulazioneReader) throws IOException {
        int scelta;
        while (true) {
            // Menu Utente
            System.out.println();
            System.out.println("Menu Utente:");
            System.out.println("1. Simulazione - Entrare al casello");
            System.out.println("2. Esci");
            System.out.print("Inserisci la tua scelta: ");

            // Legge l'input come stringa
            String input = scanner.nextLine();

            // Converte l'input in un numero intero
            try {
                scelta = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input non valido. Inserisci un numero corrispondente alla tua scelta.");
                continue;
            }

            switch (scelta) {
                case 1:
                    // Simulazione di ingresso al casello
                    System.out.println();
                    System.out.print("Inserisci la targa: ");
                    String targa = scanner.nextLine();
                    simulazioneWriter.println("INGRESSO:" + targa);
                    System.out.println();
                    System.out.println("Dati di ingresso inviati con successo");
                    break;
                case 2:
                    // Uscita dall'applicazione
                    return;
                default:
                    System.out.println("Scelta non valida. Riprova.");
                    break;
            }
        }
    }

    // Metodo per gestire il menu dell'amministratore
    private static void menuAmministratore(Scanner scanner, PrintWriter simulazioneWriter, BufferedReader simulazioneReader, PrintWriter accessoWriter) throws IOException {
        while (true) {
            // Menu Amministratore
            System.out.println();
            System.out.println("Menu Amministratore:");
            System.out.println("1. Inserire Telepass per una macchina");
            System.out.println("2. Rimuovere Telepass per una macchina");
            System.out.println("3. Esci");
            System.out.print("Inserisci la tua scelta: ");
            int scelta = scanner.nextInt();
            scanner.nextLine(); // Consuma la linea rimanente

            switch (scelta) {
                case 1:
                    // Inserimento Telepass
                    System.out.print("Inserisci la targa del dispositivo Telepass da aggiungere: ");
                    String targaAdd = scanner.nextLine();
                    System.out.println();
                    System.out.println("Targa aggiunta al dispositivo telepass!");
                    simulazioneWriter.println("INSERISCI_TELEPASS:" + targaAdd);
                    break;
                case 2:
                    // Rimozione Telepass
                    System.out.print("Inserisci la targa del dispositivo Telepass da rimuovere: ");
                    String targaRemove = scanner.nextLine();
                    System.out.println();
                    System.out.println("Richiesta di rimozione inoltrata!");
                    simulazioneWriter.println("RIMUOVI_TELEPASS:" + targaRemove);
                    break;
                case 3:
                    // Uscita dal menù amministratore
                    return;
                default:
                    System.out.println("Scelta non valida. Riprova.");
                    break;
            }
        }
    }
}
