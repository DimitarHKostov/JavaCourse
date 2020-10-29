package bg.sofia.uni.fmi.mjt.revolut.account;

public class EURAccount extends Account{
    public EURAccount(String IBAN){
        super(IBAN);
        this.currency="EUR";
    }

    public EURAccount(String IBAN, double amount){
        super(IBAN, amount);
        this.currency="EUR";
    }

    public String getCurrency(){
        return this.currency;
    }
}
