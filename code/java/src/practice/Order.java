import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;

public class Order {
    private final String client;
    private final String stock;
    private final int quantity;

    public Order(String client, String stock, int quantity) {
        this.client = client;
        this.stock = stock;
        this.quantity = quantity;
    }

    public String getClient()  { return client; }
    public String getStock()   { return stock; }
    public int getQuantity()   { return quantity; }

    @Override
    public String toString() {
        return client + "-" + stock + "-" + quantity;
    }
}

class Demo1{

    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Alice", "AAPL", 10),
                new Order("Alice", "GOOG", 5),
                new Order("Alice", "AAPL", 3),
                new Order("Bob",   "TSLA", 8),
                new Order("Bob",   "AAPL", 2),
                new Order("Carol", "GOOG", 7)
        );

        // Return a Map<String, Integer> of each client to their total quantity across all their orders.
        Map<String, Integer> P4 = orders.stream()
                .collect(Collectors.groupingBy(Order::getClient, Collectors.summingInt(Order::getQuantity)));

        System.out.println(P4);

        // Return a Map<String, List<String>> of each client to the list of distinct stocks they've ordered.

        Map<String, List<String>> P5 = orders.stream()
                .collect(Collectors.groupingBy(Order::getClient,
                        mapping(Order::getStock,
                                Collectors.collectingAndThen(
                                        Collectors.toSet(),
                                        ArrayList::new
                                ))));

        System.out.println(P5);

        // Return the client who has placed the most orders (by count of orders). Handle the empty case.
        String P6 = orders.stream()
                .collect(Collectors.groupingBy(Order::getClient, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No clients found");

        System.out.println(P6);


        //Return a Map<String, Integer> of each client to the quantity of their single largest order (the max quantity among their orders, not the total). Hint: groupingBy + a downstream that reduces to a max — think maxBy or reducing, wrapped so you get an Integer not an Optional.
        Map<String, Integer> P7 = orders.stream()
                .collect(Collectors.groupingBy(Order::getClient,
                        Collectors.collectingAndThen(
                                Collectors.maxBy((o1, o2) -> Integer.compare(o1.getQuantity(), o2.getQuantity()))
                                , o -> o.map(Order::getQuantity).orElse(0)
                        )));

        System.out.println(P7);

        // Return the stock that appears in the most orders (by order count, across all clients). This is the P6 max-entry pattern applied to a different key. Handle empty.
        String P8 = orders.stream()
                .collect(Collectors.groupingBy(Order::getStock, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No stocks found");

        System.out.println(P8);

        // Return a Map<String, String> of each client to a single comma-joined string of all their stocks, e.g. Alice → "AAPL,GOOG,AAPL". Hint: groupingBy + mapping + Collectors.joining(",").
        Map<String, String> P9 = orders.stream()
                .collect(Collectors.groupingBy(Order::getClient,
                        Collectors.mapping(Order::getStock, Collectors.joining(","))));

        System.out.println(P9);

    }
}