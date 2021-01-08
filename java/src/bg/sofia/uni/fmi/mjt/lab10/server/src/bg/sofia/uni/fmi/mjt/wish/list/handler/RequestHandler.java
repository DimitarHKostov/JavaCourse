package bg.sofia.uni.fmi.mjt.wish.list.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class RequestHandler implements Runnable {
    private final Socket socket;
    private final ConcurrentHashMap<String, List<String>> wishList;

    public RequestHandler(Socket socket, ConcurrentHashMap<String, List<String>> wishList) {
        this.socket = socket;
        this.wishList = wishList;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true); BufferedReader in =
                new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while (!socket.isClosed() && (inputLine = in.readLine()) != null) {
                if (inputLine.startsWith(InputTextHandler.POST_WISH_COMMAND)) {
                    this.handlePostWish(out, inputLine);
                } else if (inputLine.startsWith(InputTextHandler.GET_WISH_COMMAND)) {
                    this.handleGetWish(out, inputLine);
                } else if (inputLine.equals(InputTextHandler.DISCONNECT_COMMAND)) {
                    out.println(OutputTextHandler.DISCONNECT_COMMAND);
                    this.socket.close();
                } else {
                    out.println(OutputTextHandler.UNKNOWN_COMMAND);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlePostWish(PrintWriter out, String line) {
        if (!line.equals(InputTextHandler.POST_WISH_COMMAND)) {
            String[] tokens = line.split(" ");

            if (tokens.length > 2) {
                this.outputResponse(out, tokens);
            } else {
                out.println(OutputTextHandler.WRONG_NUMBER_OF_PARAMETERS);
            }
        } else {
            out.println(OutputTextHandler.WRONG_POST_FORMAT);
        }
    }

    private void outputResponse(PrintWriter out, String[] lineTokens) {
        String present = OutputTextHandler.extractPresent(lineTokens);

        if (!this.wishList.containsKey(lineTokens[1])) {
            this.wishList.put(lineTokens[1], new ArrayList<>());
            this.wishList.get(lineTokens[1]).add(present);
            String answer = OutputTextHandler.formatSuccessfulPostWish(present, lineTokens[1]);
            out.println(answer);
        } else {
            List<String> a = this.wishList.get(lineTokens[1]);
            if (a.contains(present)) {
                out.println(OutputTextHandler.formatDuplicatePostWishPresent(lineTokens[1]));
            } else {
                a.add(present);
                this.wishList.replace(lineTokens[1], a);
                String answer =
                        OutputTextHandler.formatSuccessfulPostWish(present, lineTokens[1]);
                out.println(answer);
            }
        }
    }

    private void handleGetWish(PrintWriter out, String line) {
        if (line.equals(InputTextHandler.GET_WISH_COMMAND)) {
            if (this.wishList.size() == 0) {
                out.println(OutputTextHandler.NO_STUDENTS_COMMAND);
            } else {
                Set<String> keySet = this.wishList.keySet();
                List<String> keyList = new ArrayList<>(keySet);
                String student = keyList.get((int) (Math.random() * keyList.size()));
                List<String> presents = this.wishList.get(student);
                out.println(OutputTextHandler.formatGetWish(student, presents));
                this.wishList.remove(student);
            }
        } else {
            out.println(OutputTextHandler.NO_PARAMETERS_ALLOWED_COMMAND);
        }
    }
}