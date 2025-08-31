package se.mickelus.mutil.scheduling;

import com.google.common.collect.Queues;
import net.minecraft.server.TickTask;
import net.minecraftforge.event.TickEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Iterator;
import java.util.Queue;

@ParametersAreNonnullByDefault
public class AbstractScheduler {
    private final Queue<Task> queue = Queues.newConcurrentLinkedQueue();
    private int counter;

    public void schedule(int delay, Runnable task) {
        queue.add(new Task(counter + delay, task));
    }

    public void schedule(String id, int delay, Runnable task) {
        queue.removeIf(t -> id.equals(t.id));
        queue.add(new Task(id, counter + delay, task));
    }

    public void tick(TickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        for (Iterator<Task> it = queue.iterator(); it.hasNext(); ) {
            Task task = it.next();
            if (task.getTick() < counter) {
                task.run();
                it.remove();
            }
        }

        counter++;
    }

    static class Task extends TickTask {
        private String id;

        public Task(int timestamp, Runnable task) {
            super(timestamp, task);
        }

        public Task(String id, int timestamp, Runnable task) {
            this(timestamp, task);

            this.id = id;
        }
    }
}
