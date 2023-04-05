package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ItemRepository {
    private static final Map<Long,Item> store = new HashMap<>(); // static 실무에선 hashMap x
//    private static final Map<Long,Item> store = new ConcurrentHashMap<>(); 사용하려면 이거
    private static long sequence = 0L; // static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) {
        Item findbyItem = findById(itemId);
        findbyItem.setItemName(updateParam.getItemName());
        findbyItem.setPrice(updateParam.getPrice());
        findbyItem.setQuantity(updateParam.getQuantity());
        findbyItem.setOpen(updateParam.getOpen());
        findbyItem.setRegions(updateParam.getRegions());
        findbyItem.setItemType(updateParam.getItemType());
        findbyItem.setDeliveryCode(updateParam.getDeliveryCode());
    }

    public void clearStore() {
        store.clear();
    }
}
