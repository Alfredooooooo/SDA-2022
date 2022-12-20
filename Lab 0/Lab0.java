import java.io.*;
import java.util.StringTokenizer;

public class Lab0 {
    private static InputReader in;
    private static PrintWriter out;

    static int multiplyMod(int N, int Mod, int[] x) {
        int ans = x[0];
        for (int i = 1; i < N; i++) {
            if (ans > Mod) {
                ans %= Mod;
            }

            ans = searchAnswer(ans, x[i], Mod);
        }
        return ans;
    }

    static int searchAnswer(int previousAnswer, int multiplier, int Mod) {
        int currentAnswer = 0;

        while (multiplier > 0) {
            // Checking whether multiplier is odd or even, if odd, then proceeds
            if (multiplier % 2 != 0) {
                currentAnswer += previousAnswer;
                currentAnswer %= Mod;
            }

            // Multiplying it by 2 so that the power of 2 increases
            previousAnswer = (previousAnswer * 2) % Mod;

            // Dividing it by 2 to define odd or even
            multiplier /= 2;
        }

        // Return the answer
        return currentAnswer % Mod;
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        int[] x = new int[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.nextInt();
        }

        // TODO: implement method multiplyMod(int, int, int[]) to get the answer
        int ans = multiplyMod(N, (int) (1e9 + 7), x);
        out.println(ans);

        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}