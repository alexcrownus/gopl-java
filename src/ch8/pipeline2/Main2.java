package ch8.pipeline2;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by crownus on 06/11/2016.
 */
public class Main2 {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(() -> System.out.println("Hello"), 1, TimeUnit.SECONDS);
        executor.shutdown();
    }
}
