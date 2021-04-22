package events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HeapQueue<E> implements IEventQueue<E> {

    private final List<Entry<E>> events = new ArrayList<>();

    @Override
    public void enqueue(Double time, E event) {
        events.add(new NiceEntry<>(time, event));
        heapUp();
    }

    @Override
    public Entry<E> dequeue() {
        if (events.isEmpty())
            return null;

        Entry<E> event = events.get(0);
        events.set(0, events.get(events.size() - 1));
        events.remove(events.size() - 1);
        heapDown();
        return event;
    }

    private void heapUp() {
        int index = events.size() - 1;
        while (hasParent(index) && getParent(index).getTime() > events.get(index).getTime()) {
            swap(getIndexOfParent(index), index);
            index = getIndexOfParent(index);
        }
    }

    private void heapDown() {
        int index = 0;
        while (hasLeftChild(index)) {
            int smallerChildIndex = hasRightChild(index) && getRightChild(index).getTime() < getLeftChild(index).getTime() ? getIndexOfRightChild(index) : getIndexOfLeftChild(index);

            if (events.get(index).getTime() < events.get(smallerChildIndex).getTime())
                break;

            swap(index, smallerChildIndex);
            index = smallerChildIndex;
        }
    }

    private void swap(int n, int m) {
        Collections.swap(events, n, m);
    }

    private int getIndexOfParent(int index) {
        return (index - 2) / 2;
    }

    private int getIndexOfLeftChild(int index) {
        return index * 2 + 1;
    }

    private int getIndexOfRightChild(int index) {
        return index * 2 + 2;
    }

    private boolean hasParent(int index) {
        return getIndexOfParent(index) >= 0;
    }

    private boolean hasLeftChild(int index) {
        return getIndexOfLeftChild(index) < events.size();
    }

    private boolean hasRightChild(int index) {
        return getIndexOfRightChild(index) < events.size();
    }

    private Entry<E> getParent(int index) {
        return events.get(getIndexOfParent(index));
    }

    private Entry<E> getLeftChild(int index) {
        return events.get(getIndexOfLeftChild(index));
    }

    private Entry<E> getRightChild(int index) {
        return events.get(getIndexOfRightChild(index));
    }

}
