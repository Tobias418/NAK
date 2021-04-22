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
        for (int i = 0; i < 20000; i++) {
            times.add(rnd.nextDouble() * 100);
        }
    }

    public static void test() {
        long start = System.currentTimeMillis();

        IEventQueue<String> fortniteAbende = new FutureEvents<>();
        times.forEach(time -> fortniteAbende.enqueue(time, "Hey there"));

        Entry<String> event;
        while ((event = fortniteAbende.dequeue()) != null) {
        }

        System.out.println(System.currentTimeMillis() - start + "ms");
    }

    public static void main(String[] args) {
        test();
        test();
        test();
        test();
    }
}
