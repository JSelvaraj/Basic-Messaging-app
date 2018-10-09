
import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Server {


    private static int portnumber = 51638;
    private static ServerSocket server;
    private static Socket theSocket = null;
    private static OutputStream ops = null;
    private static InputStream ips = null;

    /**
     * This is the main method. It starts the server, waits for a connection, then for a command from that connection.
     * It calls a method according to the command given to it. After that it closes the server and exits.
     * @param args not used.
     */
    public static void main(String args[]) {

        startUpServer();

        try {
            String sentence;
            System.out.println("Server started ... listening on port " + portnumber + " ...");
            theSocket = server.accept(); // Accepts the incoming connection.
            System.out.println("Server got new connection request from " + theSocket.getInetAddress());
            ips = theSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips));
            System.out.println("Connection Complete....");
            System.out.println("Waiting for Command...");
            sentence = reader.readLine();
            System.out.println("Command received ...");
            reader.close();
            if (sentence.equals("retrieve messages")) {
                retrieveFiles();
            } else if (sentence.equals("log messages")) {
                receiveMessages();
            }
            theSocket.close();
        } catch (IOException e) {

        }
    }
    /**
     * This sets up the server socket.
     */
    public static void startUpServer() {
        try {
            server = new ServerSocket(portnumber);
            System.out.print("Starting Up Server ... ");
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }

    }
    /**
     * This method waits for text from the client. It makes a directory for that day and creates a log of each of the
     * messages sent to the server. It stops listening when the character 'q' is sent.
     */
    private static void receiveMessages() {
        String sentence = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips));
            do {
                sentence = reader.readLine();
                if (!sentence.equals("q")) {
                    makeDirectory();
                    logMessageFile(sentence);
                    System.out.println("Message has been received and logged.");
                }
            } while (!sentence.equals("q"));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Escape character detected, Server closing ....");
    }
    /**
     * This method takes in a string and writes it to a file named after the date and time, in a directory named after
     * the current date.
     *
     * should only call getFileName once otherwise the second/milliseconds will be different each time
     * the method is called.
     */
    private static void logMessageFile(String message) {
        String currentDateTime = getFileName();
        String filename = getDirectoryName() + File.separator + currentDateTime;
        File file = new File(filename);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(message);
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
    }


    /**
     * This gets the current date in the format YYYY-MM-DD and checks if a directory for it already exists. If it doesn't
     * it makes one.
     */
    private static void makeDirectory() {
        File directory = new File(getDirectoryName());
        if (directory.exists()) {
        } else {
            directory.mkdir();
        }
    }
    /**
     * Gets a string for the current date in the form YYYY-MM-DD
     * @return a string for the current date.
     */
    private static String getDirectoryName() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String directory = "/cs/home/js395/nginx_default/CS2003/Net1/" + dateFormat.format(date);
        return directory;
    }
    /**
     * Gets a string for the current date and time
     * @return returns a string containing the current date and time in the form YYYY-MM-DD.mmss.SSSS"
     */
    private static String getFileName() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss.SSSS");
        Date date = new Date();
        return dateFormat.format(date);
    }
    /**
     *
     * This method retrieve the current date and looks for a directory with that date. If it finds that directory it
     * goes through each file in that directory and sends the filename and contents of the file (in alphabetical order
     * according to filename) to the client.
     */
    private static void retrieveFiles(){

        System.out.println();

        File folder = new File(getDirectoryName());
        File[] files = folder.listFiles();
        try {
            ops = theSocket.getOutputStream();
            PrintWriter output = new PrintWriter(ops);
            Arrays.sort(files);
            for (File file : files) {
                Scanner scanner = new Scanner(file);
                String filename = file.toString();
                filename = filename.substring((filename.length() - 22)); //This selects the last 22
                // characters of the filepath, which will always be the name of the log message
                output.println(filename);
                output.println(scanner.nextLine());
                output.flush();
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("All Files Sent...");
    }

}