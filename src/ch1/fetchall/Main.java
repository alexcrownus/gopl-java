package ch1.fetchall;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by crownus on 30/10/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        completion(args);
    }

    private static void executor(String[] args) throws InterruptedException, IOException {
        LocalDateTime start = LocalDateTime.now();
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<String>> tasks = new ArrayList<>();

        for (String arg : args) {
            tasks.add(fetch(arg));
        }
        executor.invokeAll(tasks)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    } catch (ExecutionException e) {
                        throw new IllegalStateException(e);
                    }

                })
                .forEach(System.out::println);
        float msecs = ChronoUnit.MILLIS.between(start, LocalDateTime.now()) / 1000.0f;
        System.out.printf("%.2fs elapsed\n", msecs);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    private static void completion(String[] args) throws InterruptedException, IOException, ExecutionException {
        Instant start = Instant.now();
        ExecutorService executor = Executors.newCachedThreadPool();
        ExecutorCompletionService<String> service = new ExecutorCompletionService<>(executor);
        for (String arg : args) {
            service.submit(fetch(arg));
        }
        for (int i = 0; i < args.length; i++) {
            System.out.println(service.take().get());
        }
        float msecs = ChronoUnit.MILLIS.between(start, Instant.now()) / 1000.0f;
        System.out.printf("%.2fs elapsed\n", msecs);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
    }

    private static Callable<String> fetch(String urlStr) throws IOException {
        return () -> {
            Instant start = Instant.now();
            URL url = new URL(urlStr);
            InputStream in = url.openStream();
            int nbytes = copy(in, DISCARD);
            float msecs = ChronoUnit.MILLIS.between(start, Instant.now()) / 1000.0f;
            return String.format("%.2fs  %7d  %s", msecs, nbytes, urlStr);
        };
    }

    private static int copy(InputStream in, OutputStream out) throws IOException {
        int written = 0;
        final int BLOCK_SIZE = 1024;
        byte[] bytes = new byte[BLOCK_SIZE];
        int len;
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);
            if (len > 0) {
                written += len;
            }
        }
        return written;
    }

    private static final DevNull DISCARD = new DevNull();

    private static class DevNull extends OutputStream {


        @Override
        public void write(int b) throws IOException {

        }

        public void write(byte[] b, int off, int len) throws IOException {

        }
    }
}
