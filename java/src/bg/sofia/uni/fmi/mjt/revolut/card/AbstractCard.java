package bg.sofia.uni.fmi.mjt.revolut.card;

import java.time.LocalDate;

public abstract class AbstractCard implements Card {
    public AbstractCard(String number, int pin, LocalDate expirationDate){
        this.pin=pin;
        this.blocked=false;
        this.failedAttempts=0;
        this.expirationDate=expirationDate;
        this.number=number;
    }

    @Override
    public boolean checkPin(int pin){

        if(pin!=this.pin){
            this.failedAttempts++;
        }

        if(failedAttempts>=3){
            this.block();
            return false;
        }

        if(pin==this.pin && this.failedAttempts>0){
            this.failedAttempts=0;
        }

        return this.pin==pin;
    }

    @Override
    public boolean isBlocked(){
        return this.blocked;
    }

    @Override
    public LocalDate getExpirationDate(){
        return this.expirationDate;
    }

    @Override
    public void block(){
        this.blocked=true;
    }

    public void unblock(){
        this.blocked=false;
    }

    protected String number;
    protected String type;
    protected LocalDate expirationDate;
    protected int pin;
    private boolean blocked;
    private int failedAttempts;
}
