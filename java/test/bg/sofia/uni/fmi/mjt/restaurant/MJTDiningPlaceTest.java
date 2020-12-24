package bg.sofia.uni.fmi.mjt.restaurant;

import bg.sofia.uni.fmi.mjt.restaurant.customer.AbstractCustomer;
import bg.sofia.uni.fmi.mjt.restaurant.customer.Customer;
import bg.sofia.uni.fmi.mjt.restaurant.customer.VipCustomer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class MJTDiningPlaceTest {
    private Restaurant restaurant;

    @Before
    public void setup() {
        this.restaurant = new MJTDiningPlace(3);
        AbstractCustomer[] normalCustomers = new Customer[10];
        AbstractCustomer[] vipCustomers = new VipCustomer[10];

        int ix = 0;
        while (ix < 10) {
            normalCustomers[ix] = new Customer(this.restaurant);
            vipCustomers[ix] = new VipCustomer(this.restaurant);

            normalCustomers[ix].start();
            vipCustomers[ix].start();

            ix++;
        }

        for (int i = 0; i < 10; i++) {
            try {
                normalCustomers[i].join();
                vipCustomers[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }

        synchronized (this.restaurant) {
            this.restaurant.close();
        }

        AbstractCustomer[] newOnes = new VipCustomer[10];
        for (int j = 0; j < 10; j++) {
            newOnes[j] = new VipCustomer(this.restaurant);
            newOnes[j].start();
        }
    }

    @Test
    public void testGetOrdersCountExactNumberPreparedMeals() {
        int totalPreparedDishes = 0;
        Chef[] chefs = this.restaurant.getChefs();

        for (Chef chef : chefs) {
            totalPreparedDishes += chef.getTotalCookedMeals();
        }

        assertEquals(20, totalPreparedDishes);
    }

    @Test
    public void testGetOrdersCountWrongNumberPreparedMeals() {
        int totalPreparedDishes = 0;
        Chef[] chefs = this.restaurant.getChefs();

        for (Chef chef : chefs) {
            totalPreparedDishes += chef.getTotalCookedMeals();
        }

        assertNotEquals(21, totalPreparedDishes);
        assertNotEquals(19, totalPreparedDishes);
    }

    @Test
    public void testGetOrdersCountExactMealOrdersReceived() {
        assertEquals(20, this.restaurant.getOrdersCount());
    }

    @Test
    public void testGetOrdersCountWrongMealOrdersReceived() {
        assertNotEquals(19, this.restaurant.getOrdersCount());
        assertNotEquals(21, this.restaurant.getOrdersCount());
    }

    @Test
    public void testGetChefsExact() {
        Chef[] chefs = this.restaurant.getChefs();
        List<Integer> ids = new ArrayList<>();

        for (Chef chef : chefs) {
            ids.add(chef.getChefId());
        }

        List<Integer> expected = List.of(0, 1, 2);

        assertTrue(expected.containsAll(ids) && ids.containsAll(expected));
    }

    @Test
    public void testGetChefsWrong() {
        Chef[] chefs = this.restaurant.getChefs();
        List<Integer> ids = new ArrayList<>();

        for (Chef chef : chefs) {
            ids.add(chef.getChefId());
        }

        List<Integer> expected = List.of(0, 1, 2, 3);

        assertFalse(expected.containsAll(ids) && ids.containsAll(expected));
    }
}
