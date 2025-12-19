import java.util.*;

public class City {
    private final Map<Integer, Node> points;
    private final Map<String, Node> places;

    public City() {
        points = new HashMap<>();
        places = new HashMap<>();
        build();
    }

    private void build() {
        Node n1 = new Node(1, "Центр");
        Node n2 = new Node(2, "Вокзал");
        Node n3 = new Node(3, "ТЦ");
        Node n4 = new Node(4, "Аэропорт");
        Node n5 = new Node(5, "Стадион");
        Node n6 = new Node(6, "Универ");
        Node n7 = new Node(7, "Больница");
        Node n8 = new Node(8, "Парк");

        n1.link(n2, 5);
        n1.link(n3, 3);
        n2.link(n4, 15);
        n2.link(n5, 7);
        n3.link(n6, 4);
        n4.link(n7, 10);
        n5.link(n8, 6);
        n6.link(n7, 8);
        n7.link(n8, 5);

        n2.link(n1, 5);
        n3.link(n1, 3);
        n4.link(n2, 15);
        n5.link(n2, 7);
        n6.link(n3, 4);
        n7.link(n4, 10);
        n8.link(n5, 6);
        n7.link(n6, 8);
        n8.link(n7, 5);

        points.put(1, n1);
        points.put(2, n2);
        points.put(3, n3);
        points.put(4, n4);
        points.put(5, n5);
        points.put(6, n6);
        points.put(7, n7);
        points.put(8, n8);

        places.put("центр", n1);
        places.put("вокзал", n2);
        places.put("тц", n3);
        places.put("аэропорт", n4);
        places.put("стадион", n5);
        places.put("универ", n6);
        places.put("больница", n7);
        places.put("парк", n8);
    }

    public Route findPath(Node from, Node to) {
        Map<Node, Integer> dists = new HashMap<>();
        Map<Node, Node> prev = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(
                Comparator.comparingInt(n -> dists.getOrDefault(n, Integer.MAX_VALUE))
        );

        dists.put(from, 0);
        pq.add(from);

        while (!pq.isEmpty()) {
            Node cur = pq.poll();

            if (cur.equals(to)) break;

            for (Map.Entry<Node, Integer> entry : cur.getLinks().entrySet()) {
                Node next = entry.getKey();
                int newDist = dists.get(cur) + entry.getValue();

                if (newDist < dists.getOrDefault(next, Integer.MAX_VALUE)) {
                    dists.put(next, newDist);
                    prev.put(next, cur);
                    pq.add(next);
                }
            }
        }

        List<Node> path = new ArrayList<>();
        Node cur = to;

        while (cur != null && !cur.equals(from)) {
            path.add(cur);
            cur = prev.get(cur);
        }

        if (cur != null && cur.equals(from)) {
            path.add(from);
            Collections.reverse(path);
            return new Route(path, dists.get(to));
        }

        return null;
    }

    public Node getPoint(int id) {
        return points.get(id);
    }

    public Node getRandomPoint() {
        List<Node> list = new ArrayList<>(points.values());
        return list.get(new Random().nextInt(list.size()));
    }

    public Node getPlace(String name) {
        return places.get(name);
    }

    public Collection<Node> getAllPoints() {
        return points.values();
    }
}