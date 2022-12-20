import java.io.*;
import java.util.StringTokenizer;

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer
        // int result = 0;
        int totalBuang = 0;
        int totalS = 0;   
        int totalO = 0;
        int totalF = 0;
        int totalI = 0;
        int totalT = 0;
        int totalA = 0;           
        for (int i = 0; i<x.length; i++) {
            String str = Character.toString(x[i]);
            if (!"SOFITA".contains(str)) {
                continue;
            } else {
                if (str.equals("S")) {
                    totalS += 1;
                } else if (totalS != 0 && str.equals("O")) {
                    totalO += 1;
                } else if (totalS != 0 && totalO != 0 && str.equals("F")) {
                    totalF += 1;
                } else if (totalS != 0 && totalO != 0 && totalF != 0 && str.equals("I")) {
                    totalI += 1;
                } else if (totalS != 0 && totalO != 0 && totalF != 0 && totalI != 0 && str.equals("T")) {
                    totalT += 1;
                } else if (totalS != 0 && totalO != 0 && totalF != 0 && totalI != 0 && totalT != 0 && str.equals("A")) {
                    totalA += 1;
                    totalA--;
                    totalS--;
                    totalO--;
                    totalF--;
                    totalI--;
                    totalT--;
                    totalBuang++;
                    if (totalS == 0 || totalO == 0 || totalF == 0 || totalI == 0 || totalT == 0) {
                        totalS = 0;   
                        totalO = 0;
                        totalF = 0;
                        totalI = 0;
                        totalT = 0;
                        totalA = 0;             
                    }
                }


                // String command = decideString(str);
                // // System.out.println(command);
                // if (command.equals("")) {
                //     array.add(str);
                // } else {
                //     int index = getIndexArray(array, command);
                //     if (index != -1) {
                //         array.remove(index);
                //         array.add(command + str);
                //     }
                // }
            }
        }
        // System.out.println(array);
        // for (String k: array) {
        //     if (k.equals("SOFITA")) {
        //         result += 1;
        //     }
        // }
        String blabla = "" + totalS + totalO + totalF + totalI + totalT + totalA;
        System.out.println(blabla);
        return N-totalBuang*6;
    }
    
    // static String decideString(String str) {
    //     if (str.equals("O")) {
    //         return "S";
    //     } else if (str.equals("F")) {
    //         return "SO";
    //     } else if (str.equals("I")) {
    //         return "SOF";
    //     } else if (str.equals("T")) {
    //         return "SOFI";
    //     } else if (str.equals("A")) {
    //         return "SOFIT";
    //     }
    //     return "";
    // }

    // static int getIndexArray(ArrayList<String> array, String command) {
    //     for (int i = 0; i<array.size(); i++) {
    //         if (array.get(i).equals(command)) {
    //             return i;
    //         }
    //     }
    //     return -1;
    // }
    
    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        char[] x = new char[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.next().charAt(0);
        }

        int ans = getTotalDeletedLetters(N, x);
        out.println(ans);

        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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