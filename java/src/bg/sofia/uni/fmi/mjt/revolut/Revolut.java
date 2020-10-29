package bg.sofia.uni.fmi.mjt.revolut;

import bg.sofia.uni.fmi.mjt.revolut.account.Account;
import bg.sofia.uni.fmi.mjt.revolut.card.Card;

import java.time.LocalDate;

public class Revolut implements RevolutAPI{
    public Revolut(){
        this.accounts=null;
        this.cards=null;
    }

    public Revolut(Account[] accounts, Card[] cards){
        this.accounts=accounts;
        this.cards=cards;
    }

    @Override
    public boolean pay(Card card, int pin, double amount, String currency) {
        if(card==null  || currency==null || currency.equals("")){
            return false;
        }

        if(!card.getType().equals("PHYSICAL")){
            return false;
        }

        if(!currency.equals("BGN") && !currency.equals("EUR")){
            return false;
        }

        if(card.getExpirationDate()==null){
            return false;
        }

        return this.lookingForCard(card, amount, currency,pin);
    }

    private boolean lookingForCard(Card card, double amount, String currency, int pin){
        for(Card currentCard: this.cards){
            if(currentCard==card){
                if(currentCard.checkPin(pin) && !currentCard.isBlocked() && LocalDate.now().isBefore(currentCard.getExpirationDate())){
                        for(Account currentAccount: this.accounts){
                            if(currentAccount.amountCheck(amount) && currentAccount.getCurrency().equals(currency)){
                                currentAccount.executePayment(amount);
                                if(currentCard.getType().equals("VIRTUALONETIME")){
                                    currentCard.block();
                                }
                                return true;
                            }
                        }
                }
            }
        }
        return false;
    }

    @Override
    public boolean payOnline(Card card, int pin, double amount, String currency, String shopURL) {
        if(card==null || currency==null || currency.equals("") || shopURL==null || shopURL.equals("")){
            return false;
        }

        if(!currency.equals("BGN") && !currency.equals("EUR")){
            return false;
        }

        String extension = shopURL.substring(shopURL.length()-4);

        if(extension.equals(".biz")){
            return false;
        }

        return this.lookingForCard(card, amount, currency, pin);
    }

    @Override
    public boolean addMoney(Account account, double amount) {
        if(amount<0){
            return false;
        }
        if(account==null){
            return false;
        }
        for(Account currentAccount: this.accounts){
            if(currentAccount==account){
                currentAccount.deposit(amount);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean transferMoney(Account from, Account to, double amount) {
        if(from==null || to==null){
            return false;
        }

        if(amount < 0){
            return false;
        }

        if(from.getAmount()<amount){
            return false;
        }

        double valueChange=0.0;

        if(!from.getCurrency().equals(to.getCurrency())){
            if(from.getCurrency().equals("BGN")){
                valueChange=(double)(amount/1.95583);
            }else{
                valueChange=(double)(amount*1.95583);
            }
        }

        for(Account searchSource: this.accounts){
            if(searchSource==from){
                for(Account searchDestination: this.accounts){
                    if(searchDestination==to){
                        if(!searchSource.getIBAN().equals(searchDestination.getIBAN())){
                            if(searchSource.getCurrency().equals(searchDestination.getCurrency())){
                                from.executePayment(amount);
                                to.deposit(amount);
                                return true;
                            }else{
                                from.executePayment(amount);
                                to.deposit(valueChange);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public double getTotalAmount(){
        double totalSum=0;
        for(Account currentAccount: this.accounts){
            if(currentAccount.getCurrency().equals("BGN")){
                totalSum+=currentAccount.getAmount();
            }else{
                double converted = (double)(currentAccount.getAmount() * 1.95583);
                totalSum+=converted;
            }
        }
        return totalSum;
    }

    private Account[] accounts;
    private Card[] cards;
}
