package events;

public class NiceEntry<E> implements IEventQueue.Entry<E> {

    private final Double time;
    private final E event;

    public NiceEntry(Double time, E event) {
        this.time = time;
        this.event = event;
    }

    @Override
    public Double getTime() {
        return time;
    }

    @Override
    public E getEvent() {
        return event;
    }

}
