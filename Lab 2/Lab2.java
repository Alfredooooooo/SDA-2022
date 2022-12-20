import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
// import java.util.Iterator;
// import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class Lab2 {
    // TODO : Silahkan menambahkan struktur data yang diperlukan
    private static ArrayList<Stack<Integer>> conveyorBelt = new ArrayList<Stack<Integer>>();
    private static InputReader in;
    private static PrintWriter out;
    
    static int geserKanan() {
        // TODO : Implementasi fitur geser kanan conveyor belt
        Stack<Integer> siPalingKanan = conveyorBelt.remove(0);
        conveyorBelt.add(conveyorBelt.size(), siPalingKanan);
        // System.out.println(conveyorBelt);
        if (!siPalingKanan.isEmpty()) {
            return siPalingKanan.peek();
        }
        return -1;
    }

    static int beliRasa(int rasa) {
        // TODO : Implementasi fitur beli rasa, manfaatkan fitur geser kanan
        int jumlahGeser = find(rasa); 
        for (int i = 0; i < jumlahGeser; i++) {
            geserKanan();
        }

        if (jumlahGeser == -1) {
            return jumlahGeser;
        } else if (jumlahGeser == 0) {
            return jumlahGeser;
        } else {
            return conveyorBelt.size()-jumlahGeser;
        }
    }

    static int find(int rasa) {
        // Stack<Stack<Integer>> newStack = new Stack<Stack<Integer>>();
        // int returnAnswer = -1;
        
        // Iterator<Stack<Integer>> iterator = conveyorBelt.iterator();
        // return returnAnswer;
        // int iterate = 0;
        // while (!conveyorBelt.isEmpty()) {
        //     System.out.println(conveyorBelt);
        //     Stack<Integer> thisStack = conveyorBelt.pollLast();
        //     System.out.println(thisStack);
        //     newStack.add(thisStack);
        //     if (!thisStack.isEmpty()) {
        //         if (thisStack.peek() == rasa) {
        //             thisStack.pop();
        //             if (iterate != 0) {
        //                 returnAnswer = conveyorBelt.size() + 1;
        //                 System.out.println(returnAnswer);
        //             } else {
        //                 returnAnswer = 0;
        //             }
        //             conveyorBelt.add(thisStack);
        //             break;
        //         }
        //     }
        //     iterate += 1;
        // }

        // for (int i = 0; i < newStack.size()-1; i++) {
        //     conveyorBelt.add(newStack.pop());
        // }
        // System.out.println(conveyorBelt);
        // return returnAnswer;
        // ArrayList<Stack<Integer>> newArray = new ArrayList<Stack<Integer>>();
        int returnAnswer = -1;
        

        // while (!conveyorBelt.isEmpty()) {
        //     newArray.add(conveyorBelt.poll());
        // }
        // System.out.println(conveyorBelt);
        for (int i = conveyorBelt.size()-1; i >= 0; i--) {
            Stack<Integer> thisStack = conveyorBelt.get(i);
            if (!thisStack.isEmpty()) {
                if (conveyorBelt.get(i).peek() == rasa) {
                    conveyorBelt.get(i).pop();
                    if (i != conveyorBelt.size()-1) {
                        returnAnswer = i+1;
                    } else {
                        returnAnswer = 0;
                    }
                    break;
                }
            }
        }
  
        // for (int i = newArray.size()-1; i>=0; i--) {
        //     Stack<Integer> thisStack = newArray.get(i);
        //     if (!thisStack.isEmpty()) {
        //         if (newArray.get(i).peek() == rasa) {
        //             newArray.get(i).pop();
        //             if (i != newArray.size()-1) {
        //                 returnAnswer = i + 1;
        //             } else {
        //                 returnAnswer = 0;
        //             }
        //             break;
        //         }
        //     }
        // }

        // for (int i = 0; i < newArray.size(); i++) {
        //     conveyorBelt.add(newArray.get(i));
        // }
        // System.out.println(conveyorBelt);
        return returnAnswer;
        // for (int i = 0; i < newArray.size(); i++) {
        //     conveyorBelt.add(newArray.get(i));
        // }
        // System.out.println(conveyorBelt);
        // return returnAnswer;
        // Queue<Stack<Integer>> newQueue = new ArrayDeque<Stack<Integer>>();
        // Stack<Stack<Integer>> stackPencari = new Stack<Stack<Integer>>();
        // Stack<Stack<Integer>> stackPenampung = new Stack<Stack<Integer>>();
        // Stack<Stack<Integer>> stackPembalik = new Stack<Stack<Integer>>();
        // int returnAnswer = -1;
        
        // int size = conveyorBelt.size();
        // // for (int i = 0; i<size; i++) {
        // //     newQueue.add(conveyorBelt.poll());
        // // }
        // while (!conveyorBelt.isEmpty()) {
        //     stackPencari.add(conveyorBelt.poll());
        // }

        // // for (int j = 0; j<size; j++) {
        // //     stackPencari.add(conveyorBelt.poll());
        // // }


        // while (!stackPencari.isEmpty()) {
        //     Stack<Integer> itemPopped = stackPencari.pop();
        //     if (itemPopped.peek() == rasa) {
        //         itemPopped.pop();
        //         stackPenampung.add(itemPopped);
        //         if (stackPencari.size() + 1 != size) {
        //             returnAnswer = stackPencari.size() + 1;
        //         } else {
        //             returnAnswer = 0;
        //         }
        //         break;
        //     } else {
        //         stackPenampung.add(itemPopped);
        //     }
        // }

        
        // int sizeStackPencari = stackPencari.size();
        // for (int i = 0; i<sizeStackPencari; i++) {
        //     stackPembalik.add(stackPencari.pop());
        // }

        // int sizeStackPembalik = stackPembalik.size();
        // for (int l = 0; l < sizeStackPembalik; l++) {
        //     conveyorBelt.add(stackPembalik.pop());
        // }

        // int sizeStackPenampung = stackPenampung.size();
        // for (int m = 0; m < sizeStackPenampung; m++) {
        //     conveyorBelt.add(stackPenampung.pop());
        // }

        // return returnAnswer;
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
        
        int N = in.nextInt();
        int X = in.nextInt();
        int C = in.nextInt();

        for (int i = 0; i < N; ++i) {

            // TODO: Inisiasi toples ke-i
            Stack<Integer> newToples = new Stack<Integer>();

            for (int j = 0; j < X; j++) {

                int rasaKeJ = in.nextInt();

                // TODO: Inisiasi kue ke-j ke dalam toples ke-i
                newToples.push(rasaKeJ);
            }
            conveyorBelt.add(newToples);
        }

        for (int i = 0; i < C; i++) {
            String perintah = in.next();
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan());
            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa));
            }
        }
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