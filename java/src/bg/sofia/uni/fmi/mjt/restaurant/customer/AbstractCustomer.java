package bg.sofia.uni.fmi.mjt.restaurant.customer;

import bg.sofia.uni.fmi.mjt.restaurant.Meal;
import bg.sofia.uni.fmi.mjt.restaurant.Order;
import bg.sofia.uni.fmi.mjt.restaurant.Restaurant;

public abstract class AbstractCustomer extends Thread {
    private final Restaurant restaurant;

    public AbstractCustomer(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        this.orderMeal();
    }

    private void orderMeal() {
        Meal randomMeal = Meal.chooseFromMenu();
        this.restaurant.submitOrder(new Order(randomMeal, this));
    }

    public abstract boolean hasVipCard();
}