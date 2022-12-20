import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// Kolaborator: Naufal Alsar

// TODO: Lengkapi Class Gedung
class Gedung {
    Gedung next;
    Gedung prev;
    public String namaGedung;
    public int jumlahLantai;



    public Gedung(String namaGedung, int jumlahLantai) {
        this.namaGedung = namaGedung;
        this.jumlahLantai = jumlahLantai;

    }

}

// TODO: Lengkapi Class Karakter
class Karakter {

    public Gedung gedungKarakterBerada;
    public int lantaiKarakterBerada;
    public int arahGerak; // 0: bawah ke atas, 1: atas ke bawah

    public Karakter(Gedung gedungKarakterBerada, int lantaiKarakterBerada, int arahGerak) {
        this.gedungKarakterBerada = gedungKarakterBerada;
        this.lantaiKarakterBerada = lantaiKarakterBerada;
        this.arahGerak = arahGerak;
    }

}

public class Lab4 {

    private static InputReader in;
    static PrintWriter out;

    public static Gedung[] arrayGedung;
    public static Gedung gedungHead;
    public static Karakter denji;
    public static Karakter iblis;
    public static int jumlahPertemuan = 0;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int jumlahGedung = in.nextInt();
        arrayGedung = new Gedung[jumlahGedung];
        for (int i = 0; i < jumlahGedung; i++) {
            String namaGedung = in.next();
            int jumlahLantai = in.nextInt();

            // TODO: Inisiasi gedung pada kondisi awal
            arrayGedung[i] = new Gedung(namaGedung, jumlahLantai);
        }
        // Inisialisasi next()
        for (int i = 0; i<jumlahGedung; i++) {
            if (i != jumlahGedung-1)
                arrayGedung[i].next = arrayGedung[i+1];
            else
                arrayGedung[i].next = arrayGedung[0];
        }
        gedungHead = arrayGedung[0];


        String gedungDenjiString = in.next();
        int lantaiDenji = in.nextInt();
        // TODO: Tetapkan kondisi awal Denji
        denji = new Karakter(null, lantaiDenji, 0);

        String gedungIblisString = in.next();
        int lantaiIblis = in.nextInt();
        // TODO: Tetapkan kondisi awal Iblis
        iblis = new Karakter(null, lantaiIblis, 1);

        int Q = in.nextInt();

        for (int i = 0; i<jumlahGedung; i++) {
            if (gedungDenjiString.equals(arrayGedung[i].namaGedung)) denji.gedungKarakterBerada = arrayGedung[i];

            if (gedungIblisString.equals(arrayGedung[i].namaGedung)) iblis.gedungKarakterBerada = arrayGedung[i];
        }

        for (int i = 0; i < Q; i++) {

            String command = in.next();

            if (command.equals("GERAK")) {
                gerak();
            } else if (command.equals("HANCUR")) {
                hancur();
            } else if (command.equals("TAMBAH")) {
                tambah();
            } else if (command.equals("PINDAH")) {
                pindah();
            }
        }

        out.close();
    }

    // TODO: Implemen perintah GERAK
    static void gerak() {
        if (denji.arahGerak == 0) { // Bawah ke atas
            if (denji.lantaiKarakterBerada == denji.gedungKarakterBerada.jumlahLantai) {
                denji.gedungKarakterBerada = denji.gedungKarakterBerada.next;
                denji.arahGerak = 1;
                denji.lantaiKarakterBerada = denji.gedungKarakterBerada.jumlahLantai;
            } else denji.lantaiKarakterBerada++;
        } else {
            if (denji.lantaiKarakterBerada == 1) {
                denji.gedungKarakterBerada = denji.gedungKarakterBerada.next;
                denji.arahGerak = 0;
                denji.lantaiKarakterBerada = 1;
            } else denji.lantaiKarakterBerada--;
        }

        if (denji.gedungKarakterBerada == iblis.gedungKarakterBerada && denji.lantaiKarakterBerada == iblis.lantaiKarakterBerada) jumlahPertemuan++;


        if (iblis.arahGerak == 0) { // Bawah ke atas
            if (iblis.lantaiKarakterBerada == iblis.gedungKarakterBerada.jumlahLantai) {
                iblis.gedungKarakterBerada = iblis.gedungKarakterBerada.next;
                iblis.arahGerak = 1;
                iblis.lantaiKarakterBerada = iblis.gedungKarakterBerada.jumlahLantai;
            } else iblis.lantaiKarakterBerada++;
        } else {
            if (iblis.lantaiKarakterBerada == 1) {
                iblis.gedungKarakterBerada = iblis.gedungKarakterBerada.next;
                iblis.arahGerak = 0;
                iblis.lantaiKarakterBerada = 1;
            } else iblis.lantaiKarakterBerada--;
        }

        if (iblis.arahGerak == 0) { // Bawah ke atas
            if (iblis.lantaiKarakterBerada == iblis.gedungKarakterBerada.jumlahLantai) {
                iblis.gedungKarakterBerada = iblis.gedungKarakterBerada.next;
                iblis.arahGerak = 1;
                iblis.lantaiKarakterBerada = iblis.gedungKarakterBerada.jumlahLantai;
            } else iblis.lantaiKarakterBerada++;
        } else {
            if (iblis.lantaiKarakterBerada == 1) {
                iblis.gedungKarakterBerada = iblis.gedungKarakterBerada.next;
                iblis.arahGerak = 0;
                iblis.lantaiKarakterBerada = 1;
            } else iblis.lantaiKarakterBerada--;

        }

        if (denji.gedungKarakterBerada == iblis.gedungKarakterBerada && denji.lantaiKarakterBerada == iblis.lantaiKarakterBerada) jumlahPertemuan++;
        

        out.println(denji.gedungKarakterBerada.namaGedung + " " + denji.lantaiKarakterBerada + " " + iblis.gedungKarakterBerada.namaGedung + " " + iblis.lantaiKarakterBerada + " " + jumlahPertemuan);
    }

    // TODO: Implemen perintah HANCUR
    static void hancur() {
        if ((denji.gedungKarakterBerada == iblis.gedungKarakterBerada && denji.lantaiKarakterBerada == iblis.lantaiKarakterBerada + 1)) {
            out.println(denji.gedungKarakterBerada.namaGedung + " " + -1);
        } else if (denji.lantaiKarakterBerada == 1) {
            out.println(denji.gedungKarakterBerada.namaGedung + " " + -1);
        } else {
            int result = denji.lantaiKarakterBerada - 1;
            if (denji.gedungKarakterBerada == iblis.gedungKarakterBerada && iblis.lantaiKarakterBerada >= denji.lantaiKarakterBerada) iblis.lantaiKarakterBerada--; 
            denji.lantaiKarakterBerada--;
            denji.gedungKarakterBerada.jumlahLantai--;
            out.println(denji.gedungKarakterBerada.namaGedung + " " + result);
        }

    }

    // TODO: Implemen perintah TAMBAH
    static void tambah() {
        iblis.lantaiKarakterBerada++;
        int result = iblis.lantaiKarakterBerada - 1;
        iblis.gedungKarakterBerada.jumlahLantai++;
        if (denji.gedungKarakterBerada.namaGedung.equals(iblis.gedungKarakterBerada.namaGedung) && denji.lantaiKarakterBerada >= iblis.lantaiKarakterBerada-1) denji.lantaiKarakterBerada++;
        
        // out.println("Posisi denji Sekarang " + denji.gedungKarakterBerada.namaGedung + " " + denji.lantaiKarakterBerada);
        // out.println("Posisi Iblis Sekarang " + denji.gedungKarakterBerada.namaGedung + " " + iblis.lantaiKarakterBerada);

        out.println(iblis.gedungKarakterBerada.namaGedung + " " + result);
    }

    // TODO: Implemen perintah PINDAH
    static void pindah() {
        denji.gedungKarakterBerada = denji.gedungKarakterBerada.next;
        if (denji.arahGerak == 0) denji.lantaiKarakterBerada = 1;
        else denji.lantaiKarakterBerada = denji.gedungKarakterBerada.jumlahLantai;

        if (denji.gedungKarakterBerada == iblis.gedungKarakterBerada && denji.lantaiKarakterBerada == iblis.lantaiKarakterBerada) jumlahPertemuan++;
        out.println(denji.gedungKarakterBerada.namaGedung + " "  + denji.lantaiKarakterBerada);
    }

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