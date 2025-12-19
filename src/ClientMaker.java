import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientMaker implements Runnable {
    private final BlockingQueue<Order> tasks;
    private final City city;
    private final AtomicInteger counter;
    private final Random rand;
    private volatile boolean active;
    private final int max;

    public ClientMaker(BlockingQueue<Order> tasks, City city, int max) {
        this.tasks = tasks;
        this.city = city;
        this.counter = new AtomicInteger(1);
        this.rand = new Random();
        this.active = true;
        this.max = max;
    }

    public void run() {
        System.out.println("Генератор клиентов стартовал");
        int made = 0;

        while (active && made < max) {
            try {
                Node start = city.getRandomPoint();
                Node end;

                do {
                    end = city.getRandomPoint();
                } while (end.equals(start));

                Route r = city.findPath(start, end);
                if (r == null) continue;

                Order task = new Order(
                        counter.getAndIncrement(),
                        start,
                        end
                );

                tasks.put(task);
                made++;

                System.out.println("Создан " + task + " (путь: " + r.getTotal() + " км)");

                Thread.sleep(rand.nextInt(500) + 50);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Генератор клиентов остановлен. Создано " + made + " заказов");
    }

    public void stop() {
        active = false;
    }
}