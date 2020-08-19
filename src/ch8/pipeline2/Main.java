package ch8.pipeline2;

import java.util.concurrent.SynchronousQueue;

/**
 * Created by crownus on 06/11/2016.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> naturals = new SynchronousQueue();
        SynchronousQueue<Integer> squares = new SynchronousQueue();
        new Thread(() -> counter(naturals)).start();
        new Thread(() -> squarer(squares, naturals)).start();
        printer(squares);
    }

    private static void counter(SynchronousQueue<Integer> naturals) {
        try {
            for (int i = 0; i < 100; i++) {
                naturals.put(i);
            }
            naturals.put(-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void squarer(SynchronousQueue<Integer> squares, SynchronousQueue<Integer> naturals) {
        try {
            Integer v;
            while ((v = naturals.take()) != -1) {
                squares.put(v * v);
            }
            squares.put(-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printer(SynchronousQueue<Integer> squares) {
        try {
            Integer v;
            while ((v = squares.take()) != -1) {
                System.out.println(v);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
