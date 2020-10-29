package bg.sofia.uni.fmi.mjt.revolut.account;

public abstract class Account{

    private double amount;
    private String IBAN;
    protected String currency;

    public Account(String IBAN) {
        this(IBAN, 0);
    }

    public Account(String IBAN, double amount) {
        this.IBAN = IBAN;
        this.amount = amount;
    }

    public abstract String getCurrency();

    public double getAmount() {
        return amount;
    }

    // complete the implementation
    public boolean amountCheck(double amount){
        return amount <= this.amount;
    }

    public void executePayment(double amount){
        this.amount-=amount;
    }

    public void deposit(double amount){
        this.amount+=amount;
    }

    public String getIBAN(){
        return this.IBAN;
    }
}
