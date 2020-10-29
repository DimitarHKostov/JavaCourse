package bg.sofia.uni.fmi.mjt.revolut.account;

public class BGNAccount extends Account{
    public BGNAccount(String IBAN){
        super(IBAN);
        this.currency="BGN";
    }

    public BGNAccount(String IBAN, double amount){
        super(IBAN, amount);
        this.currency="BGN";
    }

    public String getCurrency(){
        return this.currency;
    }
}
