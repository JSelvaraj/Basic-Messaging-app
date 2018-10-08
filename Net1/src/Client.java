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

    public static void main(String args[]) {
        int option;
        do {
            option = menu();
            if (option == 1) {
                sendMessage();
            } else if (option == 2) {
                getServerAddress();
            } else if (option == 4) {
                    retrieveMessagesFromServer();
            }
        } while (option != 3);

    }

    private static void getServerAddress() {
        Scanner kb = new Scanner(System.in);
        System.out.print("Please enter the Server IP Address: ");
        String IPaddress = kb.nextLine();
        System.out.println();
        System.out.print("Please Enter Server port number: ");
        if (IPaddress != null || IPaddress != "") {
            DEFAULT_ADDRESS = IPaddress;
        }
        port_number = kb.nextInt();

    }

    private static void sendMessage() {
        Scanner kb = new Scanner(System.in);
        String sentence = "";
        try {
            System.out.println();
            Socket socket = new Socket(DEFAULT_ADDRESS, port_number);
            System.out.print("Looking for Server......");
            PrintWriter messager = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Found Server ....");
            socket.setSoTimeout(500);
            messager.println("log messages");
            while (!sentence.equals("q")) {
                System.out.print("Please Enter a Message: ");
                sentence = kb.nextLine();
                messager.println(sentence);
                messager.flush();
            }
        } catch (IOException e) {
            System.out.println("Server not found, Client will now close.");
        }
    }

    private static int menu() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.println("Please Select Option: 1/2/3");
        System.out.println();
        System.out.println("Current destination IP:" + DEFAULT_ADDRESS);
        System.out.println("Current Destination Port Number: " + port_number);
        System.out.println();
        System.out.println("1. Send Message to Server");
        System.out.println("2. Change IP Address and Port Number");
        System.out.println("3. Exit Program");
        System.out.println();
        System.out.print("Option: ");
        int option = -1;
        try {
            option = reader.read() - 48;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return option;
    }

    private static void retrieveTodaysMessages() throws FileNotFoundException {
        System.out.println();
        System.out.println();
        Scanner scanner = null;
        System.out.println("Searching for directory...");
        System.out.println();
        String todaysFilesfolder = DEFAULT_ADDRESS + "/CS2003/Net1/" + currentDate() + "/";
        File todaysFileDirectory = new File(todaysFilesfolder);
        File[] filesArray = todaysFileDirectory.listFiles();
        Arrays.sort(filesArray);
        for (File file : filesArray) {
            scanner = new Scanner(file);
            System.out.println(scanner.nextLine());
            scanner.close();
        }


    }

    private static String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private static void retrieveMessagesFromServer() {
        try {
            String sentence;
            System.out.println();
            Socket socket = new Socket(DEFAULT_ADDRESS, port_number);

            System.out.print("Looking for Server......");

            PrintWriter messager = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Found Server ....");

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
        }
    }

}
