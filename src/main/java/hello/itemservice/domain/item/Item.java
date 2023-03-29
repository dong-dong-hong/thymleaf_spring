package hello.itemservice.domain.item;

import lombok.Data;

@Data
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantiy;

    public Item() {}

    public Item(String itemName, Integer price, Integer quantiy) {
        this.itemName = itemName;
        this.price = price;
        this.quantiy = quantiy;
    }
}
