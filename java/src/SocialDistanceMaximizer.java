public class SocialDistanceMaximizer {
    public static int maxDistance(int[] seats){
        int length=seats.length;
        if(length==2){
            return 1;
        }

        int currentDistance=0;
        int currentStartIndex=-1;
        int currentEndIndex=-1;

        int longestDistance=0;
        int longestDistanceStartIndex=-1;
        int longestDistanceEndIndex=-1;

        int numberOfOnes=0;
        boolean needToStartNewSequence=true;

        for(int i=0; i<length; ++i){
            if(seats[i]==0 && needToStartNewSequence){
                needToStartNewSequence=false;
                currentStartIndex=i;
                currentDistance++;
            }else if(seats[i]==0){
                currentDistance++;
            }

            if(currentDistance>longestDistance){
                longestDistance=currentDistance;
                longestDistanceStartIndex=currentStartIndex;
                longestDistanceEndIndex=currentEndIndex;
            }

            if(seats[i]==1){
                numberOfOnes++;
                currentDistance=0;
                needToStartNewSequence=true;
            }
        }

        if(numberOfOnes==1){
            return longestDistance;
        }

        if(longestDistanceStartIndex==0 || longestDistanceEndIndex==seats.length-1){
            return longestDistance;
        }

        return longestDistance%2==0 ? longestDistance/2 : longestDistance/2+1;
    }
}