package ch1.fetch;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

/**
 * Created by crownus on 29/10/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            LocalDateTime start = LocalDateTime.now();
            URL url = new URL(arg);
            Scanner scanner = new Scanner(url.openConnection().getInputStream());
            while (scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }
            scanner.close();
            float msecs = ChronoUnit.MILLIS.between(start, LocalDateTime.now()) / 1000.0f;
            System.out.printf("%.2fs elapsed\n", msecs);
        }

    }
}
