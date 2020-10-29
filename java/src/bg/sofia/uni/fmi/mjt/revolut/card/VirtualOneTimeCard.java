package bg.sofia.uni.fmi.mjt.revolut.card;

import java.time.LocalDate;

public class VirtualOneTimeCard extends AbstractCard{
    public VirtualOneTimeCard(String number, int pin, LocalDate expirationDate){
        super(number,pin,expirationDate);
        this.type="VIRTUALONETIME";
    }

    @Override
    public String getType(){
        return this.type;
    }
}
