package bg.sofia.uni.fmi.mjt.netflix.platform;

import bg.sofia.uni.fmi.mjt.netflix.account.Account;
import bg.sofia.uni.fmi.mjt.netflix.content.Streamable;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentNotFoundException;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentUnavailableException;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Netflix implements StreamingService {
    public Netflix(Account[] accounts, Streamable[] streamableContent){
        this.accounts=accounts;
        this.streamableContent=streamableContent;
        this.timesViewed=new HashMap<>();
        this.totalMinutesWatched=0;
    }

    public void watch(Account user, String videoContentName) throws ContentUnavailableException{
        if(user==null){
            throw new UserNotFoundException();
        }

        boolean foundUser=false;

        for(Account currentAccount: this.accounts){
            if(user==currentAccount){
                foundUser=true;
                break;
            }
        }

        if(!foundUser){
            throw new UserNotFoundException();
        }

        if(videoContentName==null){
            throw new ContentNotFoundException();
        }

        Streamable possibleVideo=null;

        for(Streamable video: this.streamableContent){
            if(video.getTitle().equals(videoContentName)){
                possibleVideo=video;
            }
        }

        if(possibleVideo==null){
            throw new ContentNotFoundException();
        }

        LocalDateTime userBirthday = user.birthdayDate();

        if(possibleVideo.getRating()==PgRating.PG13){
            LocalDateTime lowerBoundPG13 = LocalDateTime.of(userBirthday.getYear()+14, userBirthday.getMonth(),
                    userBirthday.getDayOfMonth(), userBirthday.getHour(), userBirthday.getMinute());
            if(LocalDateTime.now().isBefore(lowerBoundPG13)){
                throw new ContentUnavailableException();
            }
        }

        if(possibleVideo.getRating()==PgRating.NC17){
            LocalDateTime lowerBoundNC17 = LocalDateTime.of(userBirthday.getYear()+18, userBirthday.getMonth(),
                    userBirthday.getDayOfMonth(), userBirthday.getHour(), userBirthday.getMinute());
            if(LocalDateTime.now().isBefore(lowerBoundNC17)){
                throw new ContentUnavailableException();
            }
        }

        if(this.timesViewed.containsKey(possibleVideo)){
            int timesViewed = this.timesViewed.get(possibleVideo);
            this.timesViewed.replace(possibleVideo, timesViewed+1);
        }else{
            timesViewed.put(possibleVideo, 1);
        }
        this.totalMinutesWatched+=possibleVideo.getDuration();
    }

    public Streamable findByName(String videoContentName){
        if(videoContentName==null || videoContentName.equals("")){
            return null;
        }

        for(Streamable video: this.streamableContent){
            if(video.getTitle().equals(videoContentName)){
                return video;
            }
        }

        return null;
    }

    public Streamable mostViewed(){
        if(this.timesViewed.size()==0){
            return null;
        }

        Iterator<Map.Entry<Streamable, Integer>> currentPair = timesViewed.entrySet().iterator();
        Map.Entry<Streamable, Integer> next = currentPair.next();
        Streamable mostViewedContent = next.getKey();
        int currentlyMostViewed = next.getValue();

        while(currentPair.hasNext()){
            next = currentPair.next();
            if(next.getValue() > currentlyMostViewed){
                mostViewedContent = next.getKey();
                currentlyMostViewed = next.getValue();
            }
        }

        return mostViewedContent;
    }

    public int totalWatchedTimeByUsers(){
        return this.totalMinutesWatched;
    }

    private Account[] accounts;
    private Streamable[] streamableContent;
    private Map<Streamable, Integer> timesViewed;
    private int totalMinutesWatched;
}
