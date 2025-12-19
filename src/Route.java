import java.util.List;

public class Route {
    private final List<Node> points;
    private final int total;

    public Route(List<Node> points, int total) {
        this.points = points;
        this.total = total;
    }

    public List<Node> getPoints() {
        return points;
    }

    public int getTotal() {
        return total;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Путь (").append(total).append(" км): ");

        for (int i = 0; i < points.size(); i++) {
            if (i > 0) sb.append(" → ");
            sb.append(points.get(i).getTitle());
        }

        return sb.toString();
    }
}