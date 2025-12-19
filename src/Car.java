import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable {
    private final int id;
    private final Manager manager;
    private final City city;
    private final AtomicBoolean free;
    private Node pos;
    private Order current;
    private final ReentrantLock lock;

    public Car(int id, Manager manager, City city, Node start) {
        this.id = id;
        this.manager = manager;
        this.city = city;
        this.pos = start;
        this.free = new AtomicBoolean(true);
        this.lock = new ReentrantLock();
    }

    public void run() {
        System.out.printf("Машина #%d стартовала в %s%n", id, pos);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Order task = manager.giveTask(this);
                if (task == null) {
                    Thread.sleep(100);
                    continue;
                }

                lock.lock();
                try {
                    free.set(false);
                    current = task;

                    System.out.printf("Машина #%d взяла %s%n", id, task);

                    Route toClient = city.findPath(pos, task.getFrom());
                    if (toClient == null) {
                        free.set(true);
                        continue;
                    }

                    System.out.printf("Машина #%d едет за клиентом: %s%n", id, toClient);
                    int wait1 = toClient.getTotal() * 100;
                    Thread.sleep(wait1);

                    pos = task.getFrom();

                    Route trip = city.findPath(task.getFrom(), task.getTo());
                    if (trip == null) {
                        free.set(true);
                        continue;
                    }

                    System.out.printf("Машина #%d везет клиента: %s%n", id, trip);
                    int wait2 = trip.getTotal() * 150;
                    Thread.sleep(wait2);

                    pos = task.getTo();

                    System.out.printf("Машина #%d закончила заказ #%d. Теперь в %s%n",
                            id, task.getNum(), pos);

                    manager.taskDone(this, task);

                    current = null;
                    free.set(true);

                } finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.printf("Машина #%d остановлена%n", id);
    }

    public boolean isFree() {
        return free.get();
    }

    public Node getPos() {
        lock.lock();
        try {
            return pos;
        } finally {
            lock.unlock();
        }
    }

    public int getId() {
        return id;
    }

    public int distTo(Node place) {
        lock.lock();
        try {
            Route r = city.findPath(pos, place);
            return r != null ? r.getTotal() : Integer.MAX_VALUE;
        } finally {
            lock.unlock();
        }
    }
}