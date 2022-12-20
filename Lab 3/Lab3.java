import java.io.*;
import java.util.StringTokenizer;

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;
    public static int[] cache = new int[10001];

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
        }

        for (int i = 0; i<10001; i++) {
                cache[i] = -1;     
        }
        // Run Solusi
        int solution = getMaxRedVotes(0, N - 1);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    public static int getMaxRedVotes(int start, int end) {
        // TODO : Implementasikan solusi rekursif untuk mendapatkan skor vote maksimal
        // untuk RED pada subarray A[start ... end] (inklusif)
        // int memorizedResult = m[start][end];
        // if (memorizedResult != 1001) {
        //     return memorizedResult;
        // }
        // System.out.println("start " + start);
        // System.out.println("end " + end);
        if (start == end) {
            if ((A[start]) == "R".charAt(0)) {
                return 1;
            } else {
                return 0;
            }
        } else if (start > end) {
            return 0;
        }

        if (cache[start] > -1) {
            return cache[start];
        }

        int res = 0;
        int totalCount = 0; // getMaxRedValue(1,6) => votes[1,4] + getMaxRedValue(5,6)
        for (int i = start; i<=end; i++) { 
            // Keep track jumlah red dan blue
            int votes = 0;
            if (A[i] == "R".charAt(0)) {
                totalCount += 1;
            } else {
                totalCount -= 1;
            }
            
            if (totalCount > 0) {
                votes = (i+1) - start;
            } else {
                votes = 0;
            }
            int tempRes = votes + getMaxRedVotes(i+1, end);
            // System.out.println("Result: " + res);
            // System.out.println("Tempres " + tempRes);
            if (res < tempRes) {
                res = tempRes;
            }
        }
        cache[start] = res;

        return res;
    }

    // public static int findVotes(int start, int end) {
    //     int count = 0;
    //     for (int p = start; p<end; p++) {
    //         if (A[p] == "R".charAt(0)) {
    //             count += 1;
    //         } else {
    //             count -= 1;
    //         }
    //     }
    //     if (count > 0) {
    //         return end-start;
    //     }
    //     return 0;
    // }
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

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}