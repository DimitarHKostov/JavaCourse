package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;

public class Series extends StreamingOptions{
    public Series(String name, Genre genre, PgRating rating, Episode[] episodes){
        super(name, genre, rating);
        this.episodes=episodes;
    }

    @Override
    public int getDuration(){
        if(this.episodes!=null){
            int totalDuration=0;
            for(Episode currentEpisode: this.episodes){
                totalDuration+=currentEpisode.duration();
            }
            return totalDuration;
        }
        return 0;
    }

    private Episode[] episodes;
}
