package workbook;

import lombok.Getter;

@Getter
public class Order {
    private final String client;
    private final String stock;
    private final int quantity;
    private final double price;
    public Order(String client, String stock, int quantity, double price) {
        this.client = client; this.stock = stock;
        this.quantity = quantity; this.price = price;
    }

}
