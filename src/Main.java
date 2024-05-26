import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final int TRIAL_ROUNDS = 100;
    public static final int SLEEP_DURATION = 1;
    public static final int OUTLIER_THRESHOLD = 500;

    public static boolean stringEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return false;
            }
            /*try {
                Thread.sleep(SLEEP_DURATION);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
        }
        return true;
    }

    public static long timedEquals(String a, String b) {
        long startTime = System.nanoTime();
        stringEquals(a, b);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public static String bruteForce(int passwordLength, String secretString) {
        StringBuilder guessedPassword = new StringBuilder();

        for (int i = 0; i < passwordLength; i++) {
            System.out.println("Guessed Password: " + guessedPassword);

            Map<Character, Long> candidates = new HashMap<>();

            for (char c = 'a'; c <= 'z'; c++) {
                String testval = guessedPassword.toString() + c;
                for (int j = 0; j < passwordLength - guessedPassword.length() - 1; j++) {
                    testval += "a";
                }

                long totalTime = 0;
                long avgCount = 0 ;
                for (int x = 0; x < TRIAL_ROUNDS; x++) {
                    long timeTaken = timedEquals(testval, secretString);
                    if(timeTaken < OUTLIER_THRESHOLD) {
                        totalTime += timeTaken;
                        avgCount += 1;
                    }
                }

                if(avgCount == 0) {
                    candidates.put(c, 0L);
                } else {
                    long timeAvg = totalTime / avgCount;
                    candidates.put(c, timeAvg);
                }
            }

            // Find the key with the highest value
            Character maxKey = null;
            long maxValue = Long.MIN_VALUE;

            for (Map.Entry<Character, Long> entry : candidates.entrySet()) {
                if (entry.getValue() > maxValue) {
                    maxValue = entry.getValue();
                    maxKey = entry.getKey();
                }
            }
            System.out.println("Max Key: " + maxKey);
            System.out.println("Candidates: " + candidates);
            guessedPassword.append(maxKey);
        }
        return guessedPassword.toString();
    }

    public static void main(String[] args) {
        String password = "abbacccc";
        int passwordLength = password.length();

        String guessedPassword = bruteForce(passwordLength, password);
        System.out.println("I'm guessing: " + guessedPassword);
    }
}
