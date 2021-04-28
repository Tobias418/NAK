package events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Experiment {

    List<Double> times = new ArrayList<>(10000);

    IEventQueue<Object> queue;

    public Experiment() {
        Random rnd = new Random();
        for (int i = 0; i < 10000; i++) {
            times.add(rnd.nextDouble() * 100);
        }
    }

    public void initialize(int initialSize) {
        queue = new HeapQueue<>();
        Random rnd = new Random();
        for (int i = 0; i < initialSize; i++) {
            queue.enqueue(rnd.nextDouble() * 100, "initial");
        }
    }

    public void evaluate(int repetitions) {
        long start = System.nanoTime();

        for (int i = 0; i < repetitions; i++) {
            times.forEach(time -> queue.enqueue(time, "Hey there"));

            while (queue.dequeue() != null) {
            }
        }

        long end = System.nanoTime();
        System.out.println((end - start) / 1000000f + "ms");
    }

}
