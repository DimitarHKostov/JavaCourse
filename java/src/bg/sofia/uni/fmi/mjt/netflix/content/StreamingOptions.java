package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;

public abstract class StreamingOptions implements Streamable{
    public StreamingOptions(String name, Genre genre, PgRating rating){
        this.name=name;
        this.genre=genre;
        this.rating=rating;
        this.duration=0;
    }

    public String getTitle(){
        if(this.name!=null){
            return this.name;
        }
        return null;
    }

    public Genre getGenre(){
        if(this.genre!=null){
            return this.genre;
        }
        return null;
    }

    public int getDuration(){
        return this.duration;
    }

    public PgRating getRating(){
        if(this.rating!=null){
            return this.rating;
        }
        return null;
    }

    private String name;
    private PgRating rating;
    private Genre genre;
    protected int duration;
}
