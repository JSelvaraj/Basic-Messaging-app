
import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {


    static int portnumber = 51638;
    static ServerSocket server;


    public static void main(String args[]) {

        if (args.length == 0) {
            System.out.println("  usage:\n java Server <message>");
            System.exit(0);
        }
        makeDirectory();
        String message = "";
        for (String s: args) {
            message +=  s + " ";
        }
        logMessageFile(message);





        //        startUpServer();
//
//        try {
//            Socket theSocket = null;
//            OutputStream ops = null;
//            InputStream ips = null;
//
//            theSocket = server.accept();
//            ops = theSocket.getOutputStream();
//            ips = theSocket.getInputStream();
//            server.close();
//
//        } catch ( IOException e) {}

    }

    public static void startUpServer() {
        try {
            server = new ServerSocket(portnumber);
        } catch(IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }

    }

    private static void makeDirectory() {
        File directory = new File(getDirectoryNameString());
        if (directory.exists()) {}
        else {
            directory.mkdir();
        }

    }

    private static String getDirectoryNameString () {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private static String getFileNameString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss.SSSS");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * should only call getFileNameString once otherwise the second/milliseconds will be different each time
     * the method is called.
      */

    private static void logMessageFile (String message) {
        String currentDateTime = getFileNameString();
        String filename = getDirectoryNameString() + File.separator + currentDateTime +".txt";
        File file = new File(filename);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(message);
            writer.flush();
            writer.close();
        } catch (IOException e) {}

    }


}