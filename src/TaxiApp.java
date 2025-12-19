import java.util.concurrent.*;

public class TaxiApp {
    public static void main(String[] args) {
        System.out.println("=== Система такси ===\n");

        City city = new City();
        BlockingQueue<Order> tasks = new LinkedBlockingQueue<>();
        Manager manager = new Manager(tasks, city);

        ExecutorService pool = Executors.newFixedThreadPool(5);

        System.out.println("Точки города:");
        for (Node point : city.getAllPoints()) {
            System.out.println("  - " + point);
        }
        System.out.println();

        String[] starts = {"центр", "вокзал", "тц", "аэропорт", "больница"};

        for (int i = 1; i <= 5; i++) {
            Node start = city.getPlace(starts[i-1]);
            Car car = new Car(i, manager, city, start);
            manager.addCar(car);
            pool.submit(car);
        }

        ClientMaker maker = new ClientMaker(tasks, city, 10);
        Thread makerThread = new Thread(maker);
        Thread managerThread = new Thread(manager);

        makerThread.start();
        managerThread.start();

        try {
            makerThread.join();
            maker.stop();

            Thread.sleep(8000);

            manager.stop();
            managerThread.join(2000);

            pool.shutdown();
            pool.awaitTermination(3, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== Итог ===");
        System.out.println("Осталось заказов: " + tasks.size());
        System.out.println("Система выключена");
    }
}