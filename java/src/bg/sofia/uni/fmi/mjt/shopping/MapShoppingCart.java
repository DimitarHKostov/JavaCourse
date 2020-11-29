package bg.sofia.uni.fmi.mjt.shopping;

import bg.sofia.uni.fmi.mjt.shopping.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MapShoppingCart implements ShoppingCart {

    private final Map<Item, Integer> items;
    private final ProductCatalog catalog;

    public MapShoppingCart(ProductCatalog catalog) {
        this.catalog = catalog;
        this.items = new HashMap<>();
    }

    public Collection<Item> getUniqueItems() {
        Collection<Item> uniqueItemSet = new HashSet<>();

        for (Map.Entry<Item, Integer> entry : this.items.entrySet()) {
            uniqueItemSet.add(entry.getKey());
        }

        return uniqueItemSet;
    }

    @Override
    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }

        if (this.items.containsKey(item)) {
            this.items.replace(item, this.items.get(item) + 1);
        } else {
            this.items.put(item, 1);
        }
    }

    @Override
    public void removeItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }

        if (!this.items.containsKey(item)) {
            throw new ItemNotFoundException("Item not found");
        }

        if (this.items.get(item) == 1) {
            this.items.remove(item);
        } else {
            this.items.put(item, this.items.get(item) - 1);
        }
    }

    @Override
    public double getTotal() {
        int total = 0;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            ProductInfo info = catalog.getProductInfo(entry.getKey().getId());
            total += (info.price() * entry.getValue());
        }
        return total;
    }

    @Override
    public Collection<Item> getSortedItems() {
        List<Item> sortedItems = new ArrayList<>(items.keySet());

        Collections.sort(sortedItems, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return Integer.compare(items.get(item2), items.get(item1));
            }
        });

        return sortedItems;
    }
}