import events.HeapQueue;
import events.IEventQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    static List<Double> times = new ArrayList<>();

    static {
        Random rnd = new Random();
        for (int i = 0; i < 1000000; i++) {
            times.add(rnd.nextDouble() * 100);
        }
    }

    public static void test() {
        long start = System.nanoTime();

        IEventQueue<String> fortniteAbende = new HeapQueue<>();
        times.forEach(time -> fortniteAbende.enqueue(time, "Hey there"));

        IEventQueue.Entry<String> event;
        while ((event = fortniteAbende.dequeue()) != null) {
        }

        System.out.println((System.nanoTime() - start) / 1000000f + "ms");
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//
//            Experiment ex = new Experiment();
//            ex.initialize(0);
//            ex.evaluate(10);
//        }

//        for (int i = 0; i < 10; i++) {
//            test();
////            System.gc();
//        }
        test();
        test();
        test();
        test();
    }
}
