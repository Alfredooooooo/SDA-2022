public class tes {
    public static void main(String[] args) {
        int[][] m = new int[1001][1001];

        m[1][2] = 1001;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j<m.length; j++) {
                System.out.println(m[i][j]);
            }
        }
        System.out.println(m[1][2]);

        char[] a = new char[2];
        a[0] = "R".charAt(0);
        a[1] = "B".charAt(0);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }

        if (a[0] == "R".charAt(0)) {
            System.out.println("benar");
        }
    }
}
