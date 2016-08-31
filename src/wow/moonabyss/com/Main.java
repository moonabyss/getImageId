package wow.moonabyss.com;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class Main {
    public static void main(String args[]) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("items.txt"));

            StringBuilder sb = new StringBuilder();
            sb.append("USE site;\r\n");
            String line = br.readLine();
            while (line != null) {
                //System.out.println(line);
                //getUrl(line);    //Download file
                sb.append("INSERT INTO ItemIcon VALUES ("+line+", \"\");\r\n");
                line = br.readLine();
                //Thread.sleep(1000);
            }
            File file = new File("items.sql");
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(sb.toString());
            } finally {
                if (writer != null) writer.close();
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File folder = new File("wow");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("USE site;\r\n");
        for (File fileEntry : folder.listFiles()) {
            try {
                //System.out.println(getIconName(fileEntry.getName()));
                sb2.append("UPDATE ItemIcon SET ItemPic = \""+getIconName(fileEntry.getName())+"\" WHERE ItemId = "+fileEntry.getName().substring(0, fileEntry.getName().length()-5)+";\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            File file2 = new File("icons.sql");
            BufferedWriter writer2 = null;
            try {
                writer2 = new BufferedWriter(new FileWriter(file2));
                writer2.write(sb2.toString());
            } finally {
                if (writer2 != null) writer2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


/*
        BufferedReader br = new BufferedReader(new FileReader("wow/15451.html"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();

            Pattern p = Pattern.compile("large&#x2F;(.+).jpg");
            Matcher m = p.matcher(everything);
            if (m.find())
                System.out.println("15451 "+m.group(1));

            //System.out.println(everything);
        } finally {
            br.close();
        }
*/
    }

    private static void copy(InputStream from, OutputStream to) throws IOException {
        byte[] buffer = new byte[4096];
        while (true) {
            int numBytes = from.read(buffer);
            if (numBytes == -1) {
                break;
            }
            to.write(buffer, 0, numBytes);
        }
    }

    private static void getUrl(String id) {
        InputStream is = null;
        OutputStream out = null;
        try {
            out = new FileOutputStream("wow/" + id + ".html");

            URL url = new URL("http://ru.wowhead.com/item=" + id);
            URLConnection conn = url.openConnection();
            conn.connect();
            is = conn.getInputStream();

            copy(is, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getIconName(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("wow/"+fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();

            Pattern p = Pattern.compile("large&#x2F;(.+).jpg");
            Matcher m = p.matcher(everything);
            if (m.find())
                return m.group(1);
        } finally {
            br.close();
        }
        return "";
    }
}
