package bg.sofia.uni.fmi.mjt.shopping;

import bg.sofia.uni.fmi.mjt.shopping.item.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ListShoppingCart implements ShoppingCart {
    private final ArrayList<Item> items;
    private final ProductCatalog catalog;

    public ListShoppingCart(ProductCatalog catalog) {
        this.catalog = catalog;
        this.items = new ArrayList<>();
    }

    @Override
    public Collection<Item> getUniqueItems() {
        Set<Item> unique = new TreeSet<>(new Comparator<Item>() {
            @Override
            public int compare(Item a, Item b) {
                return Integer.compare(a.hashCode(), b.hashCode());
            }
        });

        unique.addAll(this.items);
        return unique;
    }

    @Override
    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }

        items.add(item);
    }

    @Override
    public void removeItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }

        if (!this.items.contains(item)) {
            throw new ItemNotFoundException("Item not found");
        }

        this.items.remove(item);
    }

    @Override
    public double getTotal() {
        double total = 0;
        for (Item item : items) {
            ProductInfo info = catalog.getProductInfo(item.getId());
            total += info.price();
        }
        return total;
    }

    @Override
    public Collection<Item> getSortedItems() {
        Map<Item, Integer> itemToQuantity = create_map();
        Map<Item, Integer> sortedItems = new TreeMap<>(new Comparator<Item>() {
            public int compare(Item item1, Item item2) {
                return itemToQuantity.get(item2).compareTo(itemToQuantity.get(item1));
            }
        });

        sortedItems.putAll(itemToQuantity);

        return sortedItems.keySet();
    }

    private Map<Item, Integer> create_map() {
        Map<Item, Integer> itemToQuantity = new HashMap<>();
        for (Item item : items) {
            if (!itemToQuantity.containsKey(item)) {
                itemToQuantity.put(item, 1);
            } else {
                itemToQuantity.replace(item, itemToQuantity.get(item) + 1);
            }
        }
        return itemToQuantity;
    }
}