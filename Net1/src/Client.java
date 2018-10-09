import java.net.Socket;
import java.util.Scanner;
import java.io.*;


public class Client {

    private static String DEFAULT_ADDRESS = "127.0.0.1";
    private static int port_number = 51638;
    private static Socket socket;

    /**
     * The main method calls a text UI and waits for an input from the user until the exit input 4 is given.
     * @param args
     */
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
    /**
     * This provides a basic text UI for the user to navigate the client program. It displays the current destination.
     * @return an int corresponding to what the user wants to do
     */
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
    /**
     * This method is to change the destination the client is trying to connect to. It initially asks if the destination
     * is a classmate, offering a shorter way of entering the hostname if so, otherwise it asks for the full hostname.
     * It also asks for a port number and changes both values accordlingly.
     */
    private static void getServerAddress() {
        Scanner kb = new Scanner(System.in);
        System.out.println();
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
    /**
     * This method sends a message to the server to wait for messages and asks the user for input. It continually asks
     * for user input until the character 'q' is given, at which point it returns to the menu.
     */
    private static void sendMessage() {
        Scanner kb = new Scanner(System.in);
        String sentence = "";
        try {
            connectToServer();
            PrintWriter messager = new PrintWriter(socket.getOutputStream(), true);
            messager.println("log messages");
            messager.flush();
            socket.setSoTimeout(500);
            while (!sentence.equals("q") && !(sentence.length() == 0)) {
                System.out.print("Please Enter a Message: ");
                sentence = kb.nextLine();
                if (sentence.length() > 0) {
					messager.println(sentence);
					messager.flush();
				}
                }
        } catch (IOException e) {
            System.out.println("Server not found, Client will now return to menu...");
        }
        kb.close();
    }
    /**
     * This method sends a message to the server to send todays messages back to the client. The method then waits for
     * input from the server. Once all the messages have been sent it returns to the menu.
     */
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
    /**
     * This is a method used by the other methods to connect to the current destination.
     */
    private static void connectToServer() {
        try {
            System.out.println();
            socket = new Socket(DEFAULT_ADDRESS, port_number);
            System.out.print("Looking for Server......");
            System.out.println("Found Server ....");

        } catch (IOException e) {
            System.out.println("Server not found, Client will now return to menu...");
        }
    }

}
