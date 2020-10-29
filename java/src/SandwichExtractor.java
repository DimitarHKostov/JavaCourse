import java.util.Arrays;

public class SandwichExtractor {
    public static String[] extractIngredients(String sandwich){
        if(sandwich==null){
            return new String[0];
        }

        if(sandwich.equals("")){
            return new String[0];
        }
        sandwich=sandwich.replaceAll(" ", "");

        if(sandwich.indexOf("bread")==sandwich.lastIndexOf("bread")){
            return new String[0];
        }

        String sub = sandwich.substring(sandwich.indexOf("bread")+5, sandwich.lastIndexOf("bread"));
        String[] ingredients = sub.split("-");
        String[] workingSet = new String[ingredients.length];
        Arrays.fill(workingSet, null);
        int indexWorkingSet=0;
        for(String s: ingredients){
            if(!s.equals("")){
                if(!s.equals("olives")) {
                    workingSet[indexWorkingSet] = s;
                    indexWorkingSet++;
                }
            }
        }

        String[] result = new String[indexWorkingSet];
        int i=0;
        while(i<indexWorkingSet){
            result[i]=workingSet[i];
            i++;
        }

        Arrays.sort(result);
        return result;
    }
}
