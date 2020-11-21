package bg.sofia.uni.fmi.mjt.warehouse;

import bg.sofia.uni.fmi.mjt.warehouse.exceptions.CapacityExceededException;
import bg.sofia.uni.fmi.mjt.warehouse.exceptions.ParcelNotFoundException;
import bg.sofia.uni.fmi.mjt.warehouse.products.Product;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class MJTExpressWarehouse<L, P> implements DeliveryServiceWarehouse<L, P> {
    private static final double MAXSPACE = 100;
    private final int capacity;
    private final int retentionPeriod;
    private final Set<Product<L, P>> products;

    public MJTExpressWarehouse(int capacity, int retentionPeriod) {
        this.capacity = capacity;
        this.retentionPeriod = retentionPeriod;
        this.products = new HashSet<>();
    }

    @Override
    public void submitParcel(L label, P parcel, LocalDateTime submissionDate) throws CapacityExceededException {
        if (label == null) {
            throw new IllegalArgumentException("Label is null.");
        }

        if (parcel == null) {
            throw new IllegalArgumentException("Parcel is null.");
        }

        if (submissionDate == null) {
            throw new IllegalArgumentException("SubmissionDate is null.");
        }

        if (submissionDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Future date submissions are not allowed.");
        }

        if (this.products.size() >= capacity) {
            if (!allocateSpace()) {
                throw new CapacityExceededException("Capacity is full.");
            }
        }

        this.products.add(new Product<L, P>(label, parcel, submissionDate));
    }

    private boolean isAvailable(Product<L, P> product) {
        long daysInStock = ChronoUnit.DAYS.between(product.getSubmissionDate(), LocalDateTime.now());
        return daysInStock <= retentionPeriod;
    }

    private boolean allocateSpace() {
        for (Product<L, P> product : this.products) {
            if (!this.isAvailable(product)) {
                this.products.remove(product);
                return true;
            }
        }

        return false;
    }

    private boolean removeOldItems() {
        boolean cleaner = this.allocateSpace();

        while (cleaner) {
            cleaner = this.allocateSpace();
        }

        return cleaner;
    }

    @Override
    public P getParcel(L label) {
        if (label == null) {
            throw new IllegalArgumentException("Label is null");
        }

        for (Product<L, P> product : this.products) {
            if (this.isAvailable(product) && product.getLabel() == label) {
                return product.getParcel();
            }
        }

        return null;
    }

    @Override
    public P deliverParcel(L label) throws ParcelNotFoundException {
        if (label == null) {
            throw new IllegalArgumentException("Label is null.");
        }

        Iterator<Product<L, P>> it = this.products.iterator();
        while (it.hasNext()) {
            Product<L, P> product = it.next();
            if (this.isAvailable(product) && product.getLabel() == label) {
                P parcel = product.getParcel();
                it.remove();
                return parcel;
            }
        }

        throw new ParcelNotFoundException("Parcel not found.");
    }

    private double roundUpTo(int digits, double number) {
        String n = String.valueOf(number);
        if (n.length() > 3) {
            n = n.substring(0, 2 + digits);
            Double truncated = Double.valueOf(n);
            return truncated;
        }
        return number;
    }

    @Override
    public double getWarehouseSpaceLeft() {
        double occupiedSpace = (this.products.size() * MAXSPACE) / (double) capacity;
        double spaceLeft = MAXSPACE - occupiedSpace;
        spaceLeft = spaceLeft / 100;
        return this.roundUpTo(2, spaceLeft);
    }

    @Override
    public Map<L, P> getWarehouseItems() {
        this.removeOldItems();

        Map<L, P> items = new HashMap<>();

        for (Product<L, P> product : this.products) {
            items.put(product.getLabel(), product.getParcel());
        }

        return items;
    }

    private Map<L, P> transferBefore(LocalDateTime dateTime) {
        Map<L, P> items = new HashMap<>();

        Iterator<Product<L, P>> it = this.products.iterator();
        while (it.hasNext()) {
            Product<L, P> product = it.next();
            if (product.getSubmissionDate().isBefore(dateTime)) {
                items.put(product.getLabel(), product.getParcel());
                it.remove();
            }
        }

        return items;
    }

    @Override
    public Map<L, P> deliverParcelsSubmittedBefore(LocalDateTime before) {
        if (before == null) {
            throw new IllegalArgumentException("Date(before) is null;");
        }

        return before.isAfter(LocalDateTime.now())
                ? this.transferBefore(LocalDateTime.now()) : this.transferBefore(before);
    }

    private Map<L, P> transferAfter(LocalDateTime dateTime) {
        Map<L, P> items = new HashMap<>();

        Iterator<Product<L, P>> it = this.products.iterator();
        while (it.hasNext()) {
            Product<L, P> product = it.next();
            if (product.getSubmissionDate().isAfter(dateTime)) {
                items.put(product.getLabel(), product.getParcel());
                it.remove();
            }
        }

        return items;
    }

    @Override
    public Map<L, P> deliverParcelsSubmittedAfter(LocalDateTime after) {
        if (after == null) {
            throw new IllegalArgumentException("Date(after) is null.");
        }

        if (LocalDateTime.now().isBefore(after)) {
            return new HashMap<L, P>();
        }

        return this.transferAfter(after);
    }
}
