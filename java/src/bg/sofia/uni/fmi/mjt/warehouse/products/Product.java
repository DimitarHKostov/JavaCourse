package bg.sofia.uni.fmi.mjt.warehouse.products;

import java.time.LocalDateTime;

public class Product<L, P> {
    private final L label;
    private final P parcel;
    private final LocalDateTime submissionDate;

    public Product(L label, P parcel, LocalDateTime submissionDate) {
        this.label = label;
        this.parcel = parcel;
        this.submissionDate = submissionDate;
    }

    public L getLabel() {
        return this.label;
    }

    public P getParcel() {
        return this.parcel;
    }

    public LocalDateTime getSubmissionDate() {
        return this.submissionDate;
    }
}
