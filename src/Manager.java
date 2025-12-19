import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Manager implements Runnable {
    private final BlockingQueue<Order> tasks;
    private final List<Car> cars;
    private final City city;
    private final ReentrantLock carLock;
    private volatile boolean active;

    public Manager(BlockingQueue<Order> tasks, City city) {
        this.tasks = tasks;
        this.city = city;
        this.cars = new ArrayList<>();
        this.carLock = new ReentrantLock();
        this.active = true;
    }

    public void addCar(Car car) {
        carLock.lock();
        try {
            cars.add(car);
        } finally {
            carLock.unlock();
        }
    }

    public void run() {
        System.out.println("Диспетчер запущен");

        while (active || !tasks.isEmpty()) {
            try {
                Order task = tasks.poll(100, TimeUnit.MILLISECONDS);

                if (task != null) {
                    System.out.println("Диспетчер получил: " + task);
                    handle(task);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Диспетчер остановлен");
    }

    private void handle(Order task) {
        carLock.lock();
        try {
            Car best = null;
            int minDist = Integer.MAX_VALUE;

            for (Car car : cars) {
                if (car.isFree()) {
                    int d = car.distTo(task.getFrom());
                    if (d < minDist) {
                        minDist = d;
                        best = car;
                    }
                }
            }

            if (best != null) {
                System.out.printf("Диспетчер дал заказ #%d машине #%d (дистанция: %d км)%n",
                        task.getNum(), best.getId(), minDist);
            } else {
                System.out.println("Нет свободных машин для заказа #" + task.getNum());
                try {
                    tasks.put(task);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        } finally {
            carLock.unlock();
        }
    }

    public Order giveTask(Car car) {
        if (!car.isFree()) {
            return null;
        }

        carLock.lock();
        try {
            for (Order task : tasks) {
                return task;
            }
        } finally {
            carLock.unlock();
        }

        return null;
    }

    public void taskDone(Car car, Order task) {
        System.out.printf("Диспетчер: машина #%d завершила заказ #%d%n",
                car.getId(), task.getNum());
    }

    public void stop() {
        active = false;
    }
}