package bg.sofia.uni.fmi.mjt.wish.list.handler;

import java.util.List;

public class OutputTextHandler {
    public static final String DISCONNECT_COMMAND = "[ Disconnected from server ]";
    public static final String UNKNOWN_COMMAND = "[ Unknown command ]";
    private static final String BEGIN_SUCCESSFUL_POST = "[ Gift ";
    private static final String RECIPIENT_SUCCESSFUL_POST = " for student ";
    private static final String FINAL_SUCCESSFUL_POST = " submitted successfully ]";
    public static final String WRONG_NUMBER_OF_PARAMETERS = "Sorry, wrong number of parameters";
    public static final String WRONG_POST_FORMAT = "Need to specify the present and the recipient";
    public static final String RECIPIENT_SAME_PRESENT_BEGIN = "[ The same gift for student ";
    public static final String RECIPIENT_SAME_PRESENT_END = " was already submitted ]";

    public static final String NO_STUDENTS_COMMAND = "[ There are no students present in the wish list ]";
    public static final String NO_PARAMETERS_ALLOWED_COMMAND = "no parameters allowed!!!";

    public static String formatSuccessfulPostWish(String gift, String recipient) {
        return BEGIN_SUCCESSFUL_POST + gift
                + RECIPIENT_SUCCESSFUL_POST + recipient + FINAL_SUCCESSFUL_POST;
    }

    public static String formatDuplicatePostWishPresent(String recipient) {
        return RECIPIENT_SAME_PRESENT_BEGIN + recipient + RECIPIENT_SAME_PRESENT_END;
    }

    public static String formatGetWish(String student, List<String> presents) {
        StringBuilder accumulator = new StringBuilder();

        int i = 0;
        while (i < presents.size() - 1) {
            accumulator.append(presents.get(i)).append(", ");
            i++;
        }
        accumulator.append(presents.get(i));

        return "[ " + student + ": [" + accumulator.toString() + "] ]";
    }

    public static String extractPresent(String[] tokens) {
        StringBuilder builder = new StringBuilder();
        int index = 2;
        while (index < tokens.length - 1) {
            builder.append(tokens[index]).append(" ");
            index++;
        }
        builder.append(tokens[index]);

        return builder.toString();
    }
}
