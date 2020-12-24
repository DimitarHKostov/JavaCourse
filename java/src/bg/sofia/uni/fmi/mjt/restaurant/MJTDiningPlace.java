package bg.sofia.uni.fmi.mjt.restaurant;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MJTDiningPlace implements Restaurant {
    private final Chef[] chefs;
    private final Queue<Order> ordersQueue;
    private AtomicInteger totalOrders = new AtomicInteger(0);
    private AtomicBoolean isClosed = new AtomicBoolean(false);

    public MJTDiningPlace(int numberOfChefs) {
        this.ordersQueue = new PriorityQueue<>((orderOne, orderTwo) -> {
            int vip = Boolean.compare(orderTwo.customer().hasVipCard(), orderOne.customer().hasVipCard());

            if (vip == 0) {
                return Integer.compare(orderTwo.meal().getCookingTime(), orderOne.meal().getCookingTime());
            }

            return vip;
        });

        this.chefs = new Chef[numberOfChefs];
        int chefNumber = 0;

        while (chefNumber < numberOfChefs) {
            chefs[chefNumber] = new Chef(chefNumber, this);
            chefs[chefNumber].start();
            chefNumber++;
        }
    }

    @Override
    public synchronized void submitOrder(Order order) {
        if (!this.isClosed.get()) {
            this.ordersQueue.add(order);
            this.totalOrders.getAndIncrement();

            this.notify();
        }
    }

    @Override
    public synchronized Order nextOrder() {
        while (!this.isClosed.get() && this.ordersQueue.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return this.isClosed.get() ? null : this.ordersQueue.poll();
    }

    @Override
    public synchronized int getOrdersCount() {
        return this.totalOrders.get();
    }

    @Override
    public Chef[] getChefs() {
        return this.chefs;
    }

    @Override
    public synchronized void close() {
        this.isClosed.set(true);
        this.notifyAll();
    }
}
