
import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Server {


    static int portnumber = 51638;
    static ServerSocket server;
    static Socket theSocket = null;
    static OutputStream ops = null;
    static InputStream ips = null;


    public static void main(String args[]) {

        startUpServer();

        try {
            String sentence = "";

            System.out.println("Server started ... listening on port " + server.getInetAddress().getHostName() + " " + portnumber + " ...");
            theSocket = server.accept(); // Accepts the incoming connection.
            System.out.println("Server got new connection request from " + theSocket.getInetAddress());
            ops = theSocket.getOutputStream();
            ips = theSocket.getInputStream();
            System.out.println("Connection Complete.");
            System.out.println("Waiting for Command.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(ips));
            sentence = reader.readLine();
            if (sentence.equals("retrieve messages")) {
                readFiles();
            } else if (sentence.equals("log messages")) {
                while (!sentence.equals("q")) {
                    sentence = reader.readLine();
                    makeDirectory();
                    if (!sentence.equals("q")) {
                        logMessageFile(sentence);
                        System.out.println("Message has been received and logged.");
                    }
                }
                System.out.println("Escape character detected, Server closing ....");
            }
            theSocket.close();
        } catch (IOException e) {

        }
    }

    public static void startUpServer() {
        try {
            server = new ServerSocket(portnumber);
            System.out.print("Starting Up Server ... ");
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }

    }

    private static void makeDirectory() {
        File directory = new File(getDirectoryName());
        if (directory.exists()) {
        } else {
            directory.mkdir();
        }

    }

    private static String getDirectoryName() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String directory = "/cs/home/js395/nginx_default/CS2003/Net1/" + dateFormat.format(date);
        return directory;
    }

    private static String getFileName() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss.SSSS");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
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

    private static void readFiles() throws IOException {

        System.out.println();

        File folder = new File(getDirectoryName());
        File[] files = folder.listFiles();
        PrintWriter output = new PrintWriter(ops);
        Arrays.sort(files);
        for (File file : files) {
            Scanner scanner = new Scanner(file);
                String filename = file.toString();
                filename = filename.substring((filename.length()-22)); //This selects the last 22
            // characters of the filepath, which will always be the name of the log message
                output.println(filename);
                output.println(scanner.nextLine());
                output.flush();
            scanner.close();
        }
        System.out.println();
        System.out.println("All Files Sent...");
    }


}