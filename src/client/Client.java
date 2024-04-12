/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package client;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author paolo
 */
public class Client {

    Socket mioSocket = null;
    int porta = 6789;

    DataInputStream in;
    DataOutputStream out;

    public Socket connetti() throws IOException {

        try {


            System.out.println("[0] - Provo a connettermi al server.");
            Socket mioSocket = new Socket(InetAddress.getLocalHost(), porta);

            System.out.println("[1] - Connesso!" + "\n");

            this.in = new DataInputStream(mioSocket.getInputStream());
            this.out = new DataOutputStream(mioSocket.getOutputStream());


        } catch (UnknownHostException e) {

            System.err.println("Host sconosciuto");

        } catch (IOException e) {

            System.err.println("Errore generale!");

        }

        return mioSocket;

    }
    
    public void conferma() throws IOException {
        
        System.out.println("[3] - Invio messaggio di conferma." + "\n");
        String conferma = "[Conferma] - Cartella creata con successo!";        
        
        try {
            
            out.writeBytes(conferma + "\n");
        
        } catch (IOException e) {
            
            e.getStackTrace();
            
        }
    }
    
    public void ricevi(int[][] cartella) throws IOException {
        
        
        while (true) {
            
            int estratto = in.readInt();
            
            if (estratto == -1) {
                
                System.err.println("Il server e' stato chiuso.");
                break;
                
            } else {
                
                System.out.println("Estratto: " + estratto);
                controlla(estratto, cartella, true);
            
            }
            

            

            
        }
        
    }
    
    //      MAIN
    public static void main(String[] args) throws IOException {
        
        Client c = new Client();
        c.connetti();

        int cartella[][] = CreaCartella();
        print(cartella);
        c.conferma();
        c.ricevi(cartella);
        
    }
    
    /**
     * 
     * @param estratto
     * @param cartella 
     * @param check True se il controllo avviene sulla cartella, False se avviene sul tabellone
     * 
     */
    
    
    public void controlla(int estratto, int cartella[][], boolean check) throws IOException {
        
        
        
        //status[0]:Ambo - status[1]:Terno - status[2]:Quaterna  - status[3]:Cinquina - status[4]:Tombola
        boolean status[] = null;
        
        //controllo sulla cartella
        if (check == true) {
            
            status[0] = false;
            status[1] = false;
            status[2] = false;
            status[3] = false;
            status[4] = false;
            
            int contatore = 0;
            
            for (int i = 0; i < cartella.length; i++) {

                for (int j = 0; j < cartella[i].length; j++) {

                    if (cartella[i][j] == estratto) {

                        cartella[i][j] = 91;

                    }

                }

            }
            print(cartella);

            for (int i = 0; i < cartella.length; i++) {

                contatore = 0;

                for (int j = 0; j < cartella[i].length; j++) {

                    if (cartella[i][j] == 91) {

                        contatore++;

                    }

                }

                if (contatore >= 2) {
                
                    if (status[0] == false && contatore == 2) {

                        System.out.print("Hai fatto ambo!" + "\n");
                        status[0] = true;
                        out.writeBoolean(status[0]);

                    } else if (status[1] == false && contatore == 3) {

                        System.out.print("Hai fatto terno!" + "\n");
                        status[1] = true;
                        out.writeBoolean(status[1]);

                    } else if (status[2] == false && contatore == 4) {

                        System.out.print("Hai fatto quaterna !" + "\n");
                        status[2] = true;
                        out.writeBoolean(status[2]);

                    } else if (status[3] == false && contatore == 5) {

                        System.out.print("Hai fatto cinquina!" + "\n");
                        status[3] = true;
                        out.writeBoolean(status[3]);

                    }
                    
                }

            }
            
        }
        //controllo sul tabellone
        else if (check == false) {
            
            
            
        }
        
    }

    //Metodi utili non nel main
    public static void rimuoviDuplicatiEGeneraCasualeColonne(int[][] matrice, int max, int min) {
        Random random = new Random();

        for (int j = 0; j < matrice[0].length; j++) {
            boolean[] presenti = new boolean[90];

            for (int i = 0; i < matrice.length; i++) {
                if (!presenti[matrice[i][j]]) {
                    presenti[matrice[i][j]] = true;
                } else {
                    int numeroCasuale;
                    do {
                        numeroCasuale = random.nextInt(max - min) + min;
                    } while (presenti[numeroCasuale]);
                    presenti[numeroCasuale] = true;
                    matrice[i][j] = numeroCasuale;
                }
            }
        }
    }

    public static void ordinaMatrice(int[][]cartella){

        for (int col = 0; col < cartella[0].length; col++) {

            for (int i = 0; i < cartella.length - 1; i++) {

                int min = i;

                for (int j = i + 1; j < cartella.length; j++) {

                    if (cartella[j][col] < cartella[min][col]) {

                        min = j;

                    }
                }
                if (min != i) {

                    int temp = cartella[i][col];
                    cartella[i][col] = cartella[min][col];
                    cartella[min][col] = temp;

                }
            }
        }
    }

    public static void spaziVuoti(int matrice[][]) {

        Random rand = new Random();

        int spazivuoti[][] = new int[3][4];

        for (int i = 0; i < spazivuoti.length; i++) {

            for (int j = 0; j < spazivuoti[i].length; j++) {

                spazivuoti[i][j] = rand.nextInt(8 - 0) - 0;

            }

        }

        rimuoviDuplicatiEGeneraCasuale(spazivuoti, 8, 0);


        for (int i = 0; i < spazivuoti.length; i++) {

            for (int j = 0; j < spazivuoti[i].length; j++) {

                int k = spazivuoti[i][j];

                matrice[i][k] = 0;

            }

        }

    }

    public static void rimuoviDuplicatiEGeneraCasuale(int[][] matrice, int max, int min) {
        Random random = new Random();

        for (int i = 0; i < matrice.length; i++) {
            boolean[] presenti = new boolean[9];
            int[] nuovaRiga = new int[matrice[i].length];
            int index = 0;

            for (int numero : matrice[i]) {
                if (!presenti[numero]) {
                    presenti[numero] = true;
                    nuovaRiga[index++] = numero;
                } else {
                    int numeroCasuale;
                    do {
                        numeroCasuale = random.nextInt(max - min) + min; // Genera un numero casuale da 0 a 8
                    } while (presenti[numeroCasuale]);
                    presenti[numeroCasuale] = true;
                    nuovaRiga[index++] = numeroCasuale;
                }
            }

            matrice[i] = Arrays.copyOf(nuovaRiga, index);
        }
    }
    
        //metodi utili nel main
    public static int[][] CreaCartella() {

        Random rand = new Random();
        
        int cartella[][] = new int[3][9];
        
        int max = 0;
        int min = 0;

        for (int i = 0; i < cartella.length; i++) {

            max = 9;
            min = 1;

            for (int j = 0; j < cartella[i].length; j++) {

                cartella[i][j] = rand.nextInt(max - min) + min;
                rimuoviDuplicatiEGeneraCasualeColonne(cartella, max, min);

                max += 10;
                min += 10;

            }

        }

        ordinaMatrice(cartella);
        spaziVuoti(cartella);

        return cartella;

    }
    
    public static void print(int cartella[][]) {
        
        
        for (int i = 0; i < cartella.length; i++) {
            
            for (int j = 0; j < cartella[i].length; j++) {
                
                if (cartella[i][j] < 10 && cartella[i][j] != 0) {
                    
                    System.out.print("0");
                    
                }
                

                if (cartella[i][j] != 0 && cartella[i][j] != 91) {
                    
                    System.out.print(cartella[i][j] + " ");
                    
                } else if (cartella[i][j] == 0) {
                    
                    System.out.print("// ");
                    
                } else if (cartella[i][j] > 90) {
                    
                    System.out.print("|| ");
                    
                }
                

                
                
                if (j == 8) {
                    
                    System.out.println();
                    
                }
                
            }
            
        }
        
        System.out.println();
        
    }

}
