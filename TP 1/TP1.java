import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;
// import java.util.TreeSet;

public class copy2 {
    private static InputReader in;
    private static PrintWriter out;

    public static int M;
    public static Makanan[] makananArray;
    public static int V; // Jumlah koki
    public static Koki[] kokiArray;
    // public static Koki[] kokiArraySort;
    public static int jumlahPelanggan;
    public static int N;
    public static int Y;
    public static Pelanggan[] pelangganArray;
    public static Queue<Pesanan> queuePesanan = new ArrayDeque<Pesanan>();
    public static PriorityQueue<Koki> sortedSetKokiA = new PriorityQueue<Koki>();
    public static PriorityQueue<Koki> sortedSetKokiG = new PriorityQueue<Koki>();
    public static PriorityQueue<Koki> sortedSetKokiS = new PriorityQueue<Koki>();
    public static int[][][][] cache;
    public static int costA;
    public static int costG;
    public static int costS;
    public static ArrayList<Integer> arrayIngatA = new ArrayList<Integer>();
    public static ArrayList<Integer> arrayIngatG = new ArrayList<Integer>();
    public static ArrayList<Integer> arrayIngatS = new ArrayList<Integer>();
    public static int indexA = 0;
    public static int indexG = 0;
    public static int indexS = 0;


    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Setting makanan
        M = in.nextInt();
        // System.out.println(M);
        makananArray = new Makanan[M+1];
        cache = new int[1001][3][3][3];

        for (int i = 0; i<1001; i++) {
            for (int j = 0; j<3; j++) {
                for (int k = 0; k<3; k++) {
                    for (int l = 0; l<3; l++) {
                        cache[i][j][k][l] = -1;
                    }
                }
            }
        }

        for (int i = 1; i <= M; i++) {
            int harga = in.nextInt();
            char tipe = in.nextChar();
            Makanan makanan = new Makanan(harga, tipe);
            makananArray[i] = makanan;
            if (tipe == 'A') {
                arrayIngatA.add(i);
            } else if (tipe == 'G') {
                arrayIngatG.add(i);
            } else {
                arrayIngatS.add(i);
            }
            // System.out.println(harga + " " + tipe);
        }

        // Setting koki
        V = in.nextInt();
        // System.out.println(V);
        kokiArray = new Koki[V];
        // kokiArraySort = new Koki[V];
        for (int i = 0; i<V; i++) {
            char tipe = in.nextChar();
            Koki koki = new Koki(i+1, tipe);
            kokiArray[i] = koki;
            // kokiArraySort[i] = koki;
            if (tipe == 'A') {
                sortedSetKokiA.add(koki);
            } else if (tipe == 'G') {
                sortedSetKokiG.add(koki);
            } else if (tipe == 'S') {
                sortedSetKokiS.add(koki);
            }
            // System.out.print(tipe + " ");
        }
        // System.out.println();
        jumlahPelanggan = in.nextInt(); // Jumlah pelanggan
        // System.out.println(jumlahPelanggan);
        pelangganArray = new Pelanggan[jumlahPelanggan+1];
        N = in.nextInt(); // Banyak kursi yang tersedia pada restoran
        // System.out.println(N);
        Y = in.nextInt(); // Jumlah hari restoran beroperasi
        // System.out.println(Y);

        for (int i = 0; i<Y; i++) {
            // out.println("HALO");
            int jumlahPelangganI = in.nextInt(); // Jumlah pelanggan hari-i
            // System.out.println(jumlahPelangganI);
            int saveKursi = N;
            int[] statusArray = new int[jumlahPelangganI];
            // ADT untuk ruang tunggu
            for (int j = 0; j<jumlahPelangganI; j++) {
                int idPelanggan = in.nextInt();
                char status = in.nextChar();
                long uang = in.nextInt();
                Pelanggan pelangganYangDicari = pelangganArray[idPelanggan];
                // System.out.println("Jumlah N: " + N);
                // System.out.println(idPelanggan + " " + status + " " + uang);
                int tipe = -1;
                if (status == '+') {
                    if (j == 0) {
                        statusArray[j] = 1;
                    } else {
                        statusArray[j] = statusArray[j-1] + 1;
                    }
                    tipe = 0;
                } else if (status == '-') {
                    if (j == 0) {
                        statusArray[j] = 0;
                    } else {
                        statusArray[j] = statusArray[j-1];
                    }
                    tipe = 1;
                } else {
                    int range = in.nextInt();
                    int jumlahPositifDalamRange = -1;
                    if (j == range) {
                        jumlahPositifDalamRange = statusArray[j-1];
                    } else {
                        jumlahPositifDalamRange = statusArray[j-1]-statusArray[j-range-1];
                    }
                    int jumlahNegatifDalamRange = range-jumlahPositifDalamRange;
                    if (jumlahNegatifDalamRange >= jumlahPositifDalamRange) { 
                        statusArray[j] = statusArray[j-1];
                        tipe = 1;
                    } else {
                        statusArray[j] = statusArray[j-1] + 1;
                        tipe = 0;
                    }
                }

                if (pelangganYangDicari != null) {
                    if (pelangganYangDicari.getStatus() !=3) {
                        if (tipe == 1) {
                            if (N > 0) {
                                pelangganYangDicari.setStatus(1);
                                N -= 1;
                                out.print("1 ");
                            } else {
                                pelangganYangDicari.setStatus(2);
                                out.print("2 ");;
                            }
                        } else if (tipe == 0) {
                            pelangganYangDicari.setStatus(0);
                            out.print("0 ");
                            continue;
                        }
                    } else {
                        out.print("3 ");;
                        continue;
                    }
                    pelangganYangDicari.setHargaPesanan(0);
                    pelangganYangDicari.setUang(uang);
                } else {
                    if (tipe == 1) {
                        if (N>0) {
                            tipe = 1;
                            N -= 1;
                        } else {
                            tipe = 2;
                        }
                    }
                    Pelanggan pelanggan = new Pelanggan(idPelanggan, uang, tipe);
                    out.print(tipe + " ");;
                    pelangganArray[idPelanggan] = pelanggan;
                }
            }
            out.println();

            int X = in.nextInt(); // Pelayanan
            // System.out.println(X);
            for (int l = 0; l<X; l++) {
                char method = in.nextChar();
                if (method == 'P') {
                    int idPelanggan = in.nextInt();
                    int idMakanan = in.nextInt();
                    out.println(P(idPelanggan, idMakanan));
                } else if (method == 'L') {
                    out.println(L());
                } else if (method == 'B') {
                    int idPelanggan = in.nextInt();
                    out.println(B(idPelanggan));
                } else if (method == 'C') {
                    int jumlahKoki = in.nextInt();
                    C(jumlahKoki);
                    out.println();
                } else {
                    costA = in.nextInt();
                    costG = in.nextInt();
                    costS = in.nextInt();
                    out.println(D(0, 0, 0, 1));
                }
            }
            // System.out.println("Jumlah Kursi: " + N);
            // System.out.println("Save Kursi: " + saveKursi);
            N = saveKursi; // Saat hari selesai, jumlah kursi akan kembalikan
        }
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

        public char nextChar() {
            return next().charAt(0);
        }
    }

    public static int P(int idPelanggan, int indexMakanan) {
        Makanan makanan = makananArray[indexMakanan];
        char tipe = makanan.getTipe();
        // System.out.println("lihatTipe: " + tipe);
        Pesanan pesananMakanan = new Pesanan(idPelanggan, makanan.getHarga());
        // pesananMakanan.setIdPemesan(idPelanggan);
        // System.out.println("LihatPemesan: " + pesananMakanan.getIdPemesan());
        if (tipe == 'A') {
            Koki kokiPemasak = sortedSetKokiA.peek();
            pesananMakanan.setKokiPemasak(kokiPemasak);;
            queuePesanan.add(pesananMakanan);
            return kokiPemasak.getIdKoki();
        } else if (tipe == 'G') {
            Koki kokiPemasak = sortedSetKokiG.peek();            
            pesananMakanan.setKokiPemasak(kokiPemasak);
            queuePesanan.add(pesananMakanan);
            return kokiPemasak.getIdKoki();
        }
        Koki kokiPemasak = sortedSetKokiS.peek();            
        pesananMakanan.setKokiPemasak(kokiPemasak);
        queuePesanan.add(pesananMakanan);
        return kokiPemasak.getIdKoki();
    }

    public static int L() {
        // System.out.println("lihat: " + dequePesanan.peek().getIdPemesan());
        Pesanan pesananTerPop = queuePesanan.poll();
        int idPelangganYangMemesan = pesananTerPop.getIdPemesan();
        pelangganArray[idPelangganYangMemesan].incrementHargaPesanan(pesananTerPop.getHargaPesanan());
        Koki kokiPemasak = pesananTerPop.getKokiPemasak();
        if (kokiPemasak.getSpesialisasi() == 'A') {
            sortedSetKokiA.remove(kokiPemasak);
            kokiPemasak.incrementJumlahLayananSelesai();
            sortedSetKokiA.add(kokiPemasak);
        } else if (kokiPemasak.getSpesialisasi() == 'G') {
            sortedSetKokiG.remove(kokiPemasak);
            kokiPemasak.incrementJumlahLayananSelesai();
            sortedSetKokiG.add(kokiPemasak);
        } else if (kokiPemasak.getSpesialisasi() == 'S') {
            // System.out.println(sortedSetKokiS);
            sortedSetKokiS.remove(kokiPemasak);
            kokiPemasak.incrementJumlahLayananSelesai();
            sortedSetKokiS.add(kokiPemasak);
            // System.out.println(sortedSetKokiS);
        }
        return idPelangganYangMemesan;
    }

    public static int B(int idPelanggan) {
        Pelanggan pelangganYangDimaksud = pelangganArray[idPelanggan];
        // long kembalian = pelangganYangDimaksud.getUang() - pelangganYangDimaksud.getHargaPesanan();
        // pelangganYangDimaksud.setUang(kembalian);
        // N += 1;
        long kembalian = pelangganYangDimaksud.getUang() - pelangganYangDimaksud.getHargaPesanan();
        if (kembalian >= 0) {
            pelangganYangDimaksud.setUang(kembalian);
            pelangganYangDimaksud.setHargaPesanan(0);
            return 1;
        }
        pelangganYangDimaksud.setStatus(3);
        return 0;
    }

    public static void C(int jumlahKoki) {
        Arrays.sort(kokiArray);
        for (int i = 0; i<jumlahKoki; i++) {
            out.print(kokiArray[i].getIdKoki() + " ");
        }
    }

    public static int D(int A, int G, int S, int start) {
        if (start >= M) {
            return 0;
        }
        if (makananArray[start].getTipe() == 'A') {
            if (A == 0) {
                int res1 = D(0, G, S, start+1) + makananArray[start].getHarga();
                int res2;
                if (indexA+1 <= arrayIngatA.size()) {
                    indexA += 1;
                    if (indexA == arrayIngatA.size()) {
                        res2 = D(1, G, S, arrayIngatA.get(indexA-1)) + costA;
                    } else {
                        res2 = D(1, G, S, arrayIngatA.get(indexA-1)) + (arrayIngatA.get(indexA-1)-arrayIngatA.get(indexA-2))*costA;
                    }
                } else {
                    res2 = 0;
                }

                int res3 = Math.min(res1, res2);

                if (res3 == res1) {
                    cache[start][0][G][S] = res1;
                } else {
                    cache[start][1][G][S] = res2;
                }
                return res3;
            } else if (A == 1) {
                int res4;
                if (indexA+1 <= arrayIngatA.size()) {
                    indexA += 1;
                    if (arrayIngatA.size() == indexA) {
                        res4 = D(1, G, S, arrayIngatA.get(indexA-1)) + costA;
                    } else {
                        res4 = D(1, G, S, arrayIngatA.get(indexA-1)) + (arrayIngatA.get(indexA-1)-arrayIngatA.get(indexA-2))*costA;
                    }
                } else {
                    res4 = 0;
                }
                int res5 = D(2, G, S, start+1);

                int res6 = Math.min(res4, res5);

                if (res6 == res4) {
                    cache[start][1][G][S] = res4;
                } else {
                    cache[start][2][G][S] = res5;
                }
                return res6;
            } else {
                return 0;
            }
        } else if (makananArray[start].getTipe() == 'G') {
            if (G == 0) {
                int res1 = D(A, 0, S, start+1) + makananArray[start].getHarga();
                int res2;
                if (indexG+1 <= arrayIngatG.size()) {
                    indexG += 1;
                    if (indexG == arrayIngatG.size()) {
                        res2 = D(A, 1, S, arrayIngatG.get(indexG-1)) + costG;
                    } else {
                        res2 = D(A, 1, S, arrayIngatG.get(indexG-1)) + (arrayIngatG.get(indexG-1)-arrayIngatG.get(indexG-2))*costG;
                    }
                } else {
                    res2 = 0;
                }

                int res3 = Math.min(res1, res2);

                if (res3 == res1) {
                    cache[start][A][0][S] = res1;
                } else {
                    cache[start][A][1][S] = res2;
                }
                return res3;
            } else if (G == 1) {
                int res4;
                if (indexG+1 <= arrayIngatG.size()) {
                    indexG += 1;
                    if (arrayIngatG.size() == indexG) {
                        res4 = D(A, 1, S, arrayIngatG.get(indexG-1)) + costG;
                    } else {
                        res4 = D(A, 1, S, arrayIngatG.get(indexG-1)) + (arrayIngatG.get(indexG-1)-arrayIngatG.get(indexG-2))*costG;
                    }
                } else {
                    res4 = 0;
                }
                int res5 = D(A, 2, S, start+1);

                int res6 = Math.min(res4, res5);
                if (res6 == res4) {
                    cache[start][A][1][S] = res4;
                } else {
                    cache[start][A][2][S] = res5;
                }
                return res6;
            } else {
                return 0;
            }
        } 
        else {
            if (S == 0) {
                int res1 = D(A, G, 0, start+1) + makananArray[start].getHarga();
                int res2;
                if (indexS+1 <= arrayIngatS.size()) {
                    indexS += 1;
                    if (indexS == arrayIngatS.size()) {
                        res2 = D(A, G, 1, arrayIngatS.get(indexS-1)) + costS;
                    } else {
                        res2 = D(A, G, 1, arrayIngatS.get(indexS-1)) + (arrayIngatS.get(indexS-1)-arrayIngatS.get(indexS-2))*costS;
                    }
                } else {
                    res2 = 0;
                }

                int res3 = Math.min(res1, res2);

                if (res3 == res1) {
                    cache[start][A][G][0] = res1;
                } else {
                    cache[start][A][G][1] = res2;
                }
                return res3;
            } else if (S == 1) {
                int res4;
                if (indexS+1 <= arrayIngatS.size()) {
                    indexS += 1;
                    if (arrayIngatS.size() == indexS) {
                        res4 = D(A, G, 1, arrayIngatS.get(indexS-1)) + costS;
                    } else {
                        res4 = D(A, G, 1, arrayIngatS.get(indexS-1)) + (arrayIngatS.get(indexS-1)-arrayIngatS.get(indexS-2))*costS;
                    }
                } else {
                    res4 = 0;
                }
                int res5 = D(A, G, 2, start+1);

                int res6 = Math.min(res4, res5);
                if (res6 == res4) {
                    cache[start][A][G][1] = res4;
                } else {
                    cache[start][A][G][2] = res5;
                }
                return res6;
            } else {
                return 0;
            }
        }

        // if (makananArray[start].getHarga() < res) {
        //     res = Math.min()
        // }

        // cache[start][A][G][S] = res;
    }
} 

class Makanan {
    private int harga;
    private char tipe;
    // private int idPemesan;
    // private int idPemasak;

    public Makanan(int harga, char tipe) {
        this.harga = harga;
        this.tipe = tipe;
        // this.idPemesan = -1;
        // this.idPemasak = -1;
    }

    public int getHarga() {
        return harga;
    }

    public char getTipe() {
        return tipe;
    }

    // public int getIdPemesan() {
    //     return idPemesan;
    // }

    // public int getIdPemasak() {
    //     return idPemasak;
    // }

    // public void setIdPemesan(int idPemesan) {
    //     this.idPemesan = idPemesan;
    // }

    // public void setIdPemasak(int idPemasak) {
    //     this.idPemasak = idPemasak;
    // }
}

class Pesanan {
    private int idPemesan;
    private Koki kokiPemasak;
    private int hargaPesanan;

    public Pesanan (int idPemesan, int hargaPesanan) {
        this.idPemesan = idPemesan;
        this.hargaPesanan = hargaPesanan;
    }

    public int getIdPemesan() {
        return idPemesan;
    }

    public int getHargaPesanan() {
        return hargaPesanan;
    }

    public Koki getKokiPemasak() {
        return kokiPemasak;
    }

    public void setKokiPemasak(Koki kokiPemasak) {
        this.kokiPemasak = kokiPemasak;
    }
}

class Pelanggan {
    private int idPelanggan;
    private long uang;
    private int status;
    private long hargaPesanan;

    public Pelanggan(int idPelanggan, long uang, int status) {
        this.idPelanggan = idPelanggan;
        this.uang = uang;
        this.status = status;
        this.hargaPesanan = 0;
    }

    public long getUang() {
        return uang;
    }
    public int getStatus() {
        return status;
    }
    public int getIdPelanggan() {
        return idPelanggan;
    }
    
    public long getHargaPesanan() {
        return hargaPesanan;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUang(long uang) {
        this.uang = uang;
    }

    public void setHargaPesanan(int hargaPesanan) {
        this.hargaPesanan = hargaPesanan;
    }

    public void incrementHargaPesanan(int hargaPesanan) {
        this.hargaPesanan += hargaPesanan;
    }
}

class Koki implements Comparable<Koki> {
    private int idKoki;
    private char spesialisasi;
    private int jumlahLayananSelesai;

    public Koki(int idKoki, char spesialisasi) {
        this.idKoki = idKoki;
        this.spesialisasi = spesialisasi;
        this.jumlahLayananSelesai = 0;
    }

    public int getIdKoki() {
        return idKoki;
    }

    public char getSpesialisasi() {
        return spesialisasi;
    }
    
    public int getJumlahLayananSelesai() {
        return jumlahLayananSelesai;
    }

    public void incrementJumlahLayananSelesai() {
        this.jumlahLayananSelesai += 1;
    }

    @Override
    public int compareTo(Koki o) {
        if (this.jumlahLayananSelesai > o.getJumlahLayananSelesai()) {
            return 1;
        } else if (this.jumlahLayananSelesai < o.getJumlahLayananSelesai()) {
            return -1;
        } 
        if (this.spesialisasi < o.getSpesialisasi()) {
            return 1;
        } else if (this.spesialisasi > o.getSpesialisasi()) {
            return -1;
        } 
        if (this.idKoki > o.getIdKoki()) {
            return 1;
        } else if (this.idKoki < o.getIdKoki()) {
            return -1;
        }
        return 0;
    }
}