package events;

import java.util.*;

public class FutureEvents<T> implements IEventQueue<T> {

    List<NiceEntry<T>> events = new ArrayList<>();

    @Override
    public void enqueue(Double time, T event) {
        events.add(new NiceEntry<>(time, event));
    }

    @Override
    public Entry<T> dequeue() {
        Optional<NiceEntry<T>> tmp = events.parallelStream().min(Comparator.comparingDouble(NiceEntry::getTime));
        if (tmp.isPresent()) {
            Entry<T> event = tmp.get();
            events.remove(event);
            return event;
        }

        return null;
    }

    static List<Double> times = new ArrayList<>();

    static {
        Random rnd = new Random();
        for (int i = 0; i < 100000; i++) {
            times.add(rnd.nextDouble() * 100);
        }
    }

    public static void test() {
        long start = System.nanoTime();

        IEventQueue<String> fortniteAbende = new HeapQueue<>();
        times.forEach(time -> fortniteAbende.enqueue(time, "Hey there"));

        Entry<String> event;
        while ((event = fortniteAbende.dequeue()) != null) {
        }

        System.out.println((System.nanoTime() - start) / 1000000f + "ms");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {

            Experiment ex = new Experiment();
            ex.initialize(0);
            ex.evaluate(10);
        }

//        for (int i = 0; i < 10; i++) {
//            test();
////            System.gc();
//        }
    }
}
