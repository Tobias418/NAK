package events;

import experimental.ExecuteMe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SchlechtesExperiment {

    List<Double> times = new ArrayList<>(100);

    IEventQueue<Object> queue;

    public SchlechtesExperiment(int elementCount) {
        Random rnd = new Random();
        for (int i = 0; i < elementCount; i++) {
            times.add(rnd.nextDouble() * 100);
        }
    }

    public void initialize(int initialSize) {
        queue = new FutureEvents<>();
        Random rnd = new Random();
        for (int i = 0; i < initialSize; i++) {
            queue.enqueue(rnd.nextDouble() * 100, "initial");
        }
    }

    public void evaluate(int repetitions) {
        for (int i = 0; i < repetitions; i++) {
            times.forEach(time -> queue.enqueue(time, "Hey there"));

            while (queue.dequeue() != null) {
            }
        }
    }



}
