package events;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class HeapQueueTest {

    @Test
    public void enqueue_empty_justWorks()
    {
        IEventQueue<String> queue = new FutureEvents<>();
        queue.enqueue(0d, null);
    }

    @Test
    public void enqueueDequeue_checkTime_justWorks()
    {
        double time = 42.42d;
        IEventQueue<String> queue = new FutureEvents<>();
        queue.enqueue(time, null);

        Assert.assertEquals(time, queue.dequeue().getTime(), 0.0);

    }

    @Test
    public void dequeue_checkEvent_justWorks()
    {
        Object obj = "the cake is a lie";
        IEventQueue<Object> queue = new FutureEvents<>();
        queue.enqueue(0d, obj);
        Assert.assertSame(obj, queue.dequeue().getEvent());
    }

    @Test
    public void enqueue_populatedQueue_justWorks()
    {
        IEventQueue<String> queue = new FutureEvents<>();
        queue.enqueue(0d, null);
        queue.enqueue(1d, null);
    }

    @Test
    public void enqueue_sameEvent_justWorks()
    {
        IEventQueue<String> queue = new FutureEvents<>();
        queue.enqueue(0d, null);
        queue.enqueue(0d, null);
    }

    @Test(expected = IllegalStateException.class)
    public void dequeue_empty_crashes()
    {
        IEventQueue<String> queue = new FutureEvents<>();
        queue.dequeue();
    }

    @Test
    public void dequeue_populatedQueue_justWorks()
    {
        IEventQueue<String> queue = new FutureEvents<>();
        queue.enqueue(0d,null);
        queue.dequeue();
    }

    @Test
    public void enqueueDequeue_correctOrder_justWorks()
    {
        IEventQueue<String> queue = new FutureEvents<>();
        queue.enqueue(0d, null);
        queue.enqueue(1d, null);

        Assert.assertEquals(queue.dequeue().getTime(),new Double(0d));
        Assert.assertEquals(queue.dequeue().getTime(),new Double(1d));
    }

    @Test
    public void enqueueDequeue_wrongOrder_justWorks()
    {
        IEventQueue<String> queue = new FutureEvents<>();
        queue.enqueue(1d, null);
        queue.enqueue(0d, null);

        Assert.assertEquals(queue.dequeue().getTime(),new Double(0d));
        Assert.assertEquals(queue.dequeue().getTime(),new Double(1d));
    }

    @Test
    public void enqueueDequeue_10kElems_justWorks()
    {
        int elemCount = 10_000;
        Random random = new Random(1);
        IEventQueue<String> queue = new FutureEvents<>();

        for(int index = 0; index < elemCount; index++)
        {
            // Enqueues multiple elements with random timestamp
            queue.enqueue(random.nextDouble(), "");
        }

        IEventQueue.Entry<String> previousEntry = new NiceEntry<>(-1d, "");
        for(int index = 0; index < elemCount; index++)
        {
            IEventQueue.Entry<String> currentElem = queue.dequeue();


//            System.out.println("previousEntry: " + previousEntry.getTime());
//            System.out.println("currentElem: " + currentElem.getTime());
//            System.out.println();

            // If previousEntry has a bigger / later timestamp than currentElem
            if(previousEntry.getTime() > currentElem.getTime())
            {
                throw new IllegalStateException("False order");
            }

            previousEntry = currentElem;
        }

    }




}
