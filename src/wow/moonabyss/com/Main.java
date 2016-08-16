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
    public static void main(String args[]) throws Exception {
        OutputStream out = new FileOutputStream("wow/15451.html");

        URL url = new URL("http://ru.wowhead.com/item=15451");
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();

        copy(is, out);

        is.close();
        out.close();
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
}
