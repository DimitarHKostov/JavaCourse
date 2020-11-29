package bg.sofia.uni.fmi.mjt.shopping;

import bg.sofia.uni.fmi.mjt.shopping.item.Apple;
import bg.sofia.uni.fmi.mjt.shopping.item.Chocolate;
import bg.sofia.uni.fmi.mjt.shopping.item.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MapShoppingCartTest {
    @Mock
    private ProductCatalog productCatalog;

    @InjectMocks
    private MapShoppingCart mapShoppingCart;

    public static int UPPER_BOUND = 100000;

    @Test
    public void testGetUniqueItemsFromListCheckEquality() {
        Item c1 = new Chocolate("chocolate1");
        this.mapShoppingCart.addItem(c1);
        this.mapShoppingCart.addItem(c1);
        this.mapShoppingCart.addItem(c1);

        Item c2 = new Chocolate("chocolate2");
        this.mapShoppingCart.addItem(c2);
        this.mapShoppingCart.addItem(c2);
        this.mapShoppingCart.addItem(c2);

        Item a1 = new Apple("apple1");
        this.mapShoppingCart.addItem(a1);
        this.mapShoppingCart.addItem(a1);
        this.mapShoppingCart.addItem(a1);

        Item a2 = new Apple("apple2");
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);

        Set<Item> filtered = new HashSet<>();
        filtered.add(c1);
        filtered.add(c2);
        filtered.add(a1);
        filtered.add(a2);

        assertTrue(filtered.containsAll(this.mapShoppingCart.getUniqueItems()));
        assertTrue(this.mapShoppingCart.getUniqueItems().containsAll(filtered));
    }

    @Test
    public void testGetUniqueItemsFromListCheckEqualitySizeForALotOfItems() {
        Item c1 = new Chocolate("chocolate1");
        Item c2 = new Chocolate("chocolate2");
        Item a1 = new Apple("apple1");
        Item a2 = new Apple("apple2");

        int cnt = 0;
        while (cnt++ < UPPER_BOUND) {
            this.mapShoppingCart.addItem(c1);
            this.mapShoppingCart.addItem(c2);
            this.mapShoppingCart.addItem(a1);
            this.mapShoppingCart.addItem(a2);
        }

        assertEquals(4, this.mapShoppingCart.getUniqueItems().size());
    }

    @Test
    public void testGetUniqueItemsSizeTest() {
        Item c1 = new Chocolate("chocolate1");
        this.mapShoppingCart.addItem(c1);
        this.mapShoppingCart.addItem(c1);
        this.mapShoppingCart.addItem(c1);

        Item c2 = new Chocolate("chocolate2");
        this.mapShoppingCart.addItem(c2);
        this.mapShoppingCart.addItem(c2);
        this.mapShoppingCart.addItem(c2);

        Item a1 = new Apple("apple1");
        this.mapShoppingCart.addItem(a1);
        this.mapShoppingCart.addItem(a1);
        this.mapShoppingCart.addItem(a1);

        Item a2 = new Apple("apple2");
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);

        assertEquals(4, this.mapShoppingCart.getUniqueItems().size());
    }

    @Test
    public void testGetSortedItemsEquality() {
        Item c1 = new Chocolate("chocolate1");
        this.mapShoppingCart.addItem(c1);

        Item c2 = new Chocolate("chocolate2");
        this.mapShoppingCart.addItem(c2);
        this.mapShoppingCart.addItem(c2);

        Item a1 = new Apple("apple1");
        this.mapShoppingCart.addItem(a1);
        this.mapShoppingCart.addItem(a1);
        this.mapShoppingCart.addItem(a1);

        Item a2 = new Apple("apple2");
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);

        List<Item> sorted = new LinkedList<>();
        sorted.add(a2);
        sorted.add(a1);
        sorted.add(c2);
        sorted.add(c1);

        when(productCatalog.getProductInfo("chocolate1")).thenReturn(new ProductInfo("c1", "black", 10.0));
        when(productCatalog.getProductInfo("chocolate2")).thenReturn(new ProductInfo("c2", "red", 20.0));
        when(productCatalog.getProductInfo("apple1")).thenReturn(new ProductInfo("a1", "blue", 30.0));
        when(productCatalog.getProductInfo("apple1")).thenReturn(new ProductInfo("a2", "grey", 40.0));

        Collection<Item> probablySorted = this.mapShoppingCart.getSortedItems();

        Iterator<Item> it = sorted.iterator();
        for (Item currentItem : probablySorted) {
            Item item = it.next();
            assertEquals(item.getId(), currentItem.getId());
        }
    }

    @Test
    public void testGetSortedItemsEqualityEqualsMethod() {
        Item c1 = new Chocolate("chocolate1");
        this.mapShoppingCart.addItem(c1);

        Item c2 = new Chocolate("chocolate2");
        this.mapShoppingCart.addItem(c2);
        this.mapShoppingCart.addItem(c2);

        Item a1 = new Apple("apple1");
        this.mapShoppingCart.addItem(a1);
        this.mapShoppingCart.addItem(a1);
        this.mapShoppingCart.addItem(a1);

        Item a2 = new Apple("apple2");
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);

        List<Item> sorted = new LinkedList<>();
        sorted.add(a2);
        sorted.add(a1);
        sorted.add(c2);
        sorted.add(c1);

        when(productCatalog.getProductInfo("chocolate1")).thenReturn(new ProductInfo("c1", "black", 10.0));
        when(productCatalog.getProductInfo("chocolate2")).thenReturn(new ProductInfo("c2", "red", 20.0));
        when(productCatalog.getProductInfo("apple1")).thenReturn(new ProductInfo("a1", "blue", 30.0));
        when(productCatalog.getProductInfo("apple1")).thenReturn(new ProductInfo("a2", "grey", 40.0));

        Collection<Item> probablySorted = this.mapShoppingCart.getSortedItems();

        Iterator<Item> it = sorted.iterator();
        for (Item currentItem : probablySorted) {
            Item item = it.next();
            assertTrue(item.equals(currentItem));
            assertTrue(currentItem.equals(item));
        }
    }

    @Test
    public void testGetSortedItemsEqualityHashCodeMethod() {
        Item c1 = new Chocolate("chocolate1");
        this.mapShoppingCart.addItem(c1);

        Item c2 = new Chocolate("chocolate2");
        this.mapShoppingCart.addItem(c2);
        this.mapShoppingCart.addItem(c2);

        Item a1 = new Apple("apple1");
        this.mapShoppingCart.addItem(a1);
        this.mapShoppingCart.addItem(a1);
        this.mapShoppingCart.addItem(a1);

        Item a2 = new Apple("apple2");
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);
        this.mapShoppingCart.addItem(a2);

        List<Item> sorted = new LinkedList<>();
        sorted.add(a2);
        sorted.add(a1);
        sorted.add(c2);
        sorted.add(c1);

        when(productCatalog.getProductInfo("chocolate1")).thenReturn(new ProductInfo("c1", "black", 10.0));
        when(productCatalog.getProductInfo("chocolate2")).thenReturn(new ProductInfo("c2", "red", 20.0));
        when(productCatalog.getProductInfo("apple1")).thenReturn(new ProductInfo("a1", "blue", 30.0));
        when(productCatalog.getProductInfo("apple1")).thenReturn(new ProductInfo("a2", "grey", 40.0));

        Collection<Item> probablySorted = this.mapShoppingCart.getSortedItems();

        Iterator<Item> it = sorted.iterator();
        for (Item currentItem : probablySorted) {
            Item item = it.next();
            assertEquals(item.hashCode(), currentItem.hashCode());
            assertEquals(currentItem.hashCode(), item.hashCode());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemNullParameterExpectedIllegalArgumentException() {
        this.mapShoppingCart.addItem(null);
    }

    @Test
    public void testAddItemZeroToOne() {
        Item c1 = new Chocolate("c1");
        Item a1 = new Apple("a1");
        this.mapShoppingCart.addItem(c1);
        assertEquals(1, this.mapShoppingCart.getUniqueItems().size());
        this.mapShoppingCart.addItem(a1);
        assertEquals(2, this.mapShoppingCart.getUniqueItems().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveItemNullParameterExceptedIllegalArgumentException() {
        this.mapShoppingCart.removeItem(null);
    }

    @Test(expected = ItemNotFoundException.class)
    public void testRemoveItemNotFound() {
        this.mapShoppingCart.addItem(new Chocolate("chocolate1"));
        Item notAddedApple = new Apple("apple1");
        this.mapShoppingCart.removeItem(notAddedApple);
    }

    @Test
    public void testRemoveItemTwoToOne() {
        Item c1 = new Chocolate("c1");
        Item a1 = new Apple("a1");
        this.mapShoppingCart.addItem(c1);
        this.mapShoppingCart.addItem(a1);

        this.mapShoppingCart.removeItem(a1);

        assertEquals(1, this.mapShoppingCart.getUniqueItems().size());
    }

    @Test
    public void testGetTotalSumToBePaidMultipleItems() {
        this.mapShoppingCart.addItem(new Apple("apple1"));
        this.mapShoppingCart.addItem(new Apple("apple2"));
        this.mapShoppingCart.addItem(new Chocolate("chocolate1"));
        this.mapShoppingCart.addItem(new Chocolate("chocolate2"));

        when(productCatalog.getProductInfo("apple1")).thenReturn(new ProductInfo("qbulka1", "chervena", 10.0));
        when(productCatalog.getProductInfo("apple2")).thenReturn(new ProductInfo("qbulka2", "zelena", 20.0));
        when(productCatalog.getProductInfo("chocolate1")).thenReturn(new ProductInfo("shokolad1", "bql", 30.0));
        when(productCatalog.getProductInfo("chocolate2")).thenReturn(new ProductInfo("shokolad2", "cheren", 40.0));

        assertEquals(100.0, this.mapShoppingCart.getTotal(), 0.1);
    }

    @Test
    public void testGetTotalSumToBePaidZeroItems() {
        assertEquals(0.0, this.mapShoppingCart.getTotal(), 0.1);
    }
}
