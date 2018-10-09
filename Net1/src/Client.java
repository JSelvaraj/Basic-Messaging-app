import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

public class Client {

    private static String DEFAULT_ADDRESS = "127.0.0.1";
    private static int port_number = 51638;
    private static Socket socket;

    public static void main(String args[]) {
        int option;
        do {
            option = menu();
            if (option == 1) {
                sendMessage();
            } else if (option == 2) {
                getServerAddress();
            } else if (option == 3) {
                retrieveMessagesFromServer();
            } else if (option != 4){
                System.out.println("Choice not recognised.");
            }
        } while (option != 4);
    }
    private static int menu() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Please Select Option: 1/2/3/4");
        System.out.println();
        System.out.println("Current destination IP:" + DEFAULT_ADDRESS);
        System.out.println("Current Destination Port Number: " + port_number);
        System.out.println();
        System.out.println("1. Send Message to Server");
        System.out.println("2. Change IP Address and Port Number");
        System.out.println("3. Retrieve Today's Messages from the Server");
        System.out.println("4. Exit Program");
        System.out.println();
        System.out.print("Option: ");
        int option = -1;
        try {
            option = reader.read() - 48;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------");
        return option;
    }
    private static void getServerAddress() {
        Scanner kb = new Scanner(System.in);
        System.out.println();
        System.out.print("Do you want to connect to a class mate? (y/n): ");
        String input = "";
        while (!input.equals("y") && !input.equals("n")) {
            System.out.print("Do you want to connect to a class mate? (y/n): ");
            input = kb.nextLine();
            if (input.equals("y")) {
                System.out.print("What is the username of your classmate?: ");
                DEFAULT_ADDRESS = kb.nextLine() + ".host.cs.st-andrews.ac.uk";
                System.out.println();
            } else if (input.equals("n")) {
                System.out.print("Please enter the Server IP Address: ");
                DEFAULT_ADDRESS = kb.nextLine();
                System.out.println();
            } else {
                System.out.println("Please type y for yes, n for no.");
            }
        }

        System.out.print("Please Enter Server port number: ");
        port_number = kb.nextInt();
    }
    private static void sendMessage() {
        Scanner kb = new Scanner(System.in);
        String sentence = "";
        try {
            connectToServer();
            PrintWriter messager = new PrintWriter(socket.getOutputStream(), true);
            messager.println("log messages");
            while (!sentence.equals("q")) {
                System.out.print("Please Enter a Message: ");
                sentence = kb.nextLine();
                if (!sentence.equals("q")) {
                    messager.println(sentence);
                    messager.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Server not found, Client will now return to menu...");
        }
    }
    private static void retrieveMessagesFromServer() {
        try {
            String sentence;
            connectToServer();

            PrintWriter messager = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            messager.println("retrieve messages");
            do {
                sentence = reader.readLine();
                if (!(sentence == null)){
                    System.out.println(sentence);
                }
            } while (!(sentence == null));
            socket.close();
            System.out.println("All files retrieved...");
            System.out.println("Returning to Menu");
        } catch (IOException e) {
            System.out.println("Server not found, Client will now return to menu...");
        }
    }
    private static void connectToServer() {
        try {
            System.out.println();
            socket = new Socket(DEFAULT_ADDRESS, port_number);
            System.out.print("Looking for Server......");
            System.out.println("Found Server ....");
            socket.setSoTimeout(500);
        } catch (IOException e) {
            System.out.println("Server not found, Client will now return to menu...");
        }
    }

}
