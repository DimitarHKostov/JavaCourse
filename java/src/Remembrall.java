import java.util.Arrays;

public class Remembrall {
    public static boolean isPhoneNumberForgettable(String phoneNumber){
        if(phoneNumber==null){
            return false;
        }

        if(phoneNumber.equals("")){
            return false;
        }

        int[] digits = new int[10];
        Arrays.fill(digits, 0);
        for(int i=0; i<phoneNumber.length(); ++i){
            char current = phoneNumber.charAt(i);
            if(current<'0' || current>'9'){
                if(current!=' ' && current!='-'){
                    return true;
                }
            }
            if(current >= '0' && current <= '9'){
                digits[current-'0']++;
            }
        }

        boolean flag = true;
        for(int i=0; i<10; ++i){
            if(digits[i]>1){
                flag = false;
            }
        }

        return flag;
    }
}
