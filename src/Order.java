import java.time.LocalDateTime;

public class Order {
    private final int num;
    private final Node from;
    private final Node to;
    private final LocalDateTime time;

    public Order(int num, Node from, Node to) {
        this.num = num;
        this.from = from;
        this.to = to;
        this.time = LocalDateTime.now();
    }

    public int getNum() { return num; }
    public Node getFrom() { return from; }
    public Node getTo() { return to; }
    public LocalDateTime getTime() { return time; }

    public String toString() {
        return String.format("Заказ #%d: %s → %s", num, from.getTitle(), to.getTitle());
    }
}