package ch1.dup1;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by crownus on 29/10/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, Integer> counts = new HashMap<>();
        if (args.length == 0) {
            countLines(System.in, counts);
        } else {
            for (String arg : args) {
                try (InputStream file = Files.newInputStream(Paths.get(arg))) {
                    countLines(file, counts);
                }

            }
        }
    }

    private static void countLines(InputStream in, Map<String, Integer> counts) {
        Scanner scanner = new Scanner(in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            counts.merge(line, 1, (v, __) -> v + 1);
        }
        scanner.close();
        counts.entrySet().stream().filter(map -> map.getValue() > 1).forEach(map -> System.out.printf("%d\t%s\n", map.getValue(), map.getKey()));

    }

}
