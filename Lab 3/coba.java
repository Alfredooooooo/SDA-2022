/**
 * coba
 */
public class coba {

    public static void main(String[] args) {
        // System.out.println(mystery(10, 3));

        // print(5);

        // int[] coins = {5, 1,2,3,4,5};
        // System.out.println(tukarKoin(coins, 1));

        int arr[] = {12, 10, 30, 50, 100};
        System.out.println(" "+fun(arr, 5)+" ");
    }

    static int mystery(int n, int m) {
        System.out.println("a");
        if (n == 0)
            return 1;
        return mystery(n/m, m) * m;
    }

    static int recA(int x) {
        if (x == 0) {
            return x;
        } else {
            return x + recA(x / 5);
        }
    }
    static int recB(int y, int z) {
        if (y != 0) {
            return recB(y - 1, z - 1);
        } else {
            return z;
        }
    }
    static int recC(int n) {
        if (n > 10) {
            return n - 1;
        } else {
            return recC(recC(n + 7));
        }
    }
    static void print(int n)
    {
        if (n < 0)
            return;
    
        System.out.print(" " + n);
    
        // The last executed statement
        // is recursive call
        print(n - 1);
    }


    static int tukarKoin(int[] coins, int change) {
        int minCoin = change;
        for (int coin: coins) {
            if (coin > change) {
                continue;
            } 
            if (coin == change) {
                return 1;
            }
            int thisCoin = 1 + tukarKoin(coins, change-1);
            if (thisCoin < minCoin) {
                minCoin = thisCoin;
            }
        }

        return change;
    }

    static int fun(int a[],int n)
    {
        int x;
        if(n == 1)
            return a[0];
        else {
            x = fun(a, n - 1);
            System.out.println(x + " "+ n);
        if(x > a[n - 1])
            return x;
        else {
            System.out.println("done");
            System.out.println(a[n-1]);
            return a[n - 1];
        }
        }
    }
}