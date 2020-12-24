package bg.sofia.uni.fmi.mjt.restaurant;

import java.util.concurrent.atomic.AtomicInteger;

public class Chef extends Thread {
    private int id;
    private final Restaurant restaurant;
    private AtomicInteger cookedMeals = new AtomicInteger(0);

    public Chef(int id, Restaurant restaurant) {
        this.id = id;
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        this.cook();
    }

    public int getChefId() {
        return this.id;
    }

    private void cook() {
        while (this.restaurant.nextOrder() != null) {
            this.cookedMeals.getAndIncrement();
        }
    }

    /**
     * Returns the total number of meals that this chef has cooked.
     **/
    public synchronized int getTotalCookedMeals() {
        return this.cookedMeals.get();
    }

}