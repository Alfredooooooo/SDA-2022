import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

public class coba {
    static ArrayDeque<Integer> conveyorBelt = new ArrayDeque<Integer>();

    static int f(Queue<Integer> qu) {
        Stack<Integer> newStack = new Stack<Integer>();
        Queue<Integer> q = qu;

        for (int i = 0; i<3; i++) {
            newStack.add(q.poll());
        }

        System.out.println(newStack);

        return -1;
    }
    public static void main(String[] args) {
        // Queue<Integer> q = new ArrayDeque<Integer>();
        // Stack<Integer> s = new Stack<Integer>();
        // q.add(1);
        // q.add(2);
        // q.add(3);

        // for (int i = 0; i < q.size(); i++) {
        //     int p = q.poll();
        //     s.add(p);
        //     System.out.println(p);
        // }

        // System.out.println(s.pop());

        // for (int i = 0; i<-1; i++) {
        //     System.out.println("a");
        // }

        for (int i = 0; i<3; i++) {
            conveyorBelt.add(i);
        }

        System.out.println(conveyorBelt);
        System.out.println(conveyorBelt);
        System.out.println(conveyorBelt);

        conveyorBelt.add(4);
        for (int i = 0; i<3; i++) {
            System.out.println(conveyorBelt.pollLast());
        }

        // for (int i = 99; i>0; i--) {
        //     System.out.print(i);
        // }
    }
}
