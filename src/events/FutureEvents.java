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

        throw new IllegalStateException("Dequeued from empty Queue");
    }

}
