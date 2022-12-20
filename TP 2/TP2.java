import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

// Collaborator: Naufal Alsar 2106752041

// Reference other than mentioned, means it's from scele or 

public class TP2 {
    private static InputReader in;
    static PrintWriter out;
    static LinkedMachine linkedMachine;
    // static Machine[] arrayMachine;


    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt(); // Jumlah Mesin
        linkedMachine = new LinkedMachine(N);
        for (int i = 0; i<N; i++) {
            int M = in.nextInt(); // Jumlah skor
            Machine newMachine = new Machine(i+1, M);

            for (int j = 0; j<M; j++) {
                int skor = in.nextInt();
                newMachine.addSkor(skor);
            }
            linkedMachine.addMachine(newMachine);
        }

        linkedMachine.current = linkedMachine.head;
        // linkedMachine.current.tree.printInorder(linkedMachine.current.tree.root);
        int Q = in.nextInt(); // Jumlah aktivitas
        for (int i = 0; i<Q; i++) {
            String jenisAktivitas = in.next();
            if (jenisAktivitas.equals("MAIN")) {
                int skor = in.nextInt();
                int res = linkedMachine.handleMain(skor);
                out.println(res);
            } else if (jenisAktivitas.equals("GERAK")) {
                String arahGerak = in.next();
                int res = linkedMachine.handleGerak(arahGerak);
                out.println(res);
            } else if (jenisAktivitas.equals("HAPUS")) {
                int banyakSkorValidation = in.nextInt();
                long res = linkedMachine.handleHapus(banyakSkorValidation);
                out.println(res);
            } else if (jenisAktivitas.equals("LIHAT")) {
                int lowerBound = in.nextInt();
                int upperBound = in.nextInt();
                int res = linkedMachine.handleLihat(lowerBound, upperBound);
                out.println(res);
            } else if (jenisAktivitas.equals("EVALUASI")) {
                int res = linkedMachine.handleEvaluasi();
                out.println(res);
            }
        }

        out.close();
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

class Node {
    int key, height, jumlahSkor, jumlahAnak;
    Node left, right;

    Node(int key) {
        this.key = key;
        this.height = 1;
        this.jumlahSkor = 1;
        this.jumlahAnak = 0;
    }
}

class AVLSkorTree {
    Node root;
    long sumSkor;
    int jumlahNode;

    // Reference: https://www.geeksforgeeks.org/deletion-in-an-avl-tree/
    Node rightRotate(Node node) {
        // Mulai rotasi, dimulai dari mencari node.left
        Node nodeToBeRotated = node.left;
        Node melihatAnak = nodeToBeRotated.right;

        // Mulai rotasi
        nodeToBeRotated.right = node;
        node.left =  melihatAnak;
        
        // Merubah tinggi
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        nodeToBeRotated.height = Math.max(getHeight(nodeToBeRotated.left), getHeight(nodeToBeRotated.right)) + 1;

        // Merubah jumlah anak setelah di rotate
        int jmlAnakKananDariAnakKiri = 0;
        if (melihatAnak != null) jmlAnakKananDariAnakKiri = melihatAnak.jumlahAnak + melihatAnak.jumlahSkor;
        node.jumlahAnak = node.jumlahAnak - (nodeToBeRotated.jumlahAnak + nodeToBeRotated.jumlahSkor) + jmlAnakKananDariAnakKiri;
        nodeToBeRotated.jumlahAnak = nodeToBeRotated.jumlahAnak - jmlAnakKananDariAnakKiri + node.jumlahAnak + node.jumlahSkor;
        
        // Return new root
        return nodeToBeRotated;
    }

    Node leftRotate(Node node) {
        // Mulai rotasi, dimulai dari mencari node.right
        Node nodeToBeRotated = node.right;
        Node melihatAnak = nodeToBeRotated.left;

        // Mulai rotasi
        nodeToBeRotated.left = node;
        node.right =  melihatAnak;
        
        // Merubah tinggi
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        nodeToBeRotated.height = Math.max(getHeight(nodeToBeRotated.left), getHeight(nodeToBeRotated.right)) + 1;

        // Merubah jumlah anak setelah di rotate
        int jmlAnakKiridariAnakKanan = 0;
        if (melihatAnak != null) jmlAnakKiridariAnakKanan = melihatAnak.jumlahAnak + melihatAnak.jumlahSkor;
        node.jumlahAnak = node.jumlahAnak - (nodeToBeRotated.jumlahAnak + nodeToBeRotated.jumlahSkor) + jmlAnakKiridariAnakKanan;
        nodeToBeRotated.jumlahAnak = nodeToBeRotated.jumlahAnak - jmlAnakKiridariAnakKanan + node.jumlahAnak + node.jumlahSkor;

        // Return new root
        return nodeToBeRotated;    
    }

    Node insertNode(Node node, int key) {        
        if (node == null) {
            jumlahNode += 1;
            sumSkor += key;
            Node new_node = new Node(key);
            return new_node;
        }
        
        // Insert Node
        if (key < node.key) {
            node.jumlahAnak += 1;
            node.left = insertNode(node.left, key);
        }
        else if (key > node.key) {
            node.jumlahAnak += 1;
            node.right = insertNode(node.right, key);
        } else {
            sumSkor += key;
            jumlahNode += 1;
            node.jumlahSkor += 1;
            return node;
        }

        // Update height
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

        // Dapet balance untuk melakukan balancing pada AVL tree
        int balance = getBalance(node);

        // Check kasus single rotation kanan
        if (balance > 1 && key < node.left.key) return rightRotate(node);

        // Check kasus single rotation kiri
        if (balance < -1 && key > node.right.key) return leftRotate(node);

        // Check kasus double rotation kanan (1. left rotate 2. right rotate ke root)
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Check kasus double rotation kiri (1. right rotate 2. left rotate ke root)
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        // Apabila node tidak terjadi balancing
        return node;
    }

    Node deleteNode(Node node, int key) {
        if (node == null) {
            return node;
        }

        if (key < node.key) {
            node.jumlahAnak -= 1;
            node.left = deleteNode(node.left, key);
        }
        else if (key > node.key) {
            node.jumlahAnak -= 1;
            node.right = deleteNode(node.right, key);
        }
        // Kalau node yg dimaksud merupakan node yang benar, maka lakukan proses deletion
        else {
            sumSkor -= key;
            jumlahNode -= 1;
            if (node.jumlahSkor > 1) {
                node.jumlahSkor -= 1;
                return node;
            }
            // Hanya ada satu anak / tidak ada anak
            if ((node.left == null) || (node.right == null)) {
                Node temp = null;
                if (temp == node.left) temp = node.right; // Kalau dia ada anak, dan anak kirinya kosong, maka bisa jadi anak kanan ada
                else temp = node.left;

                if (temp == null) { // Kasus tidak ada anak
                    temp = node;
                    node = null;
                }
                else node = temp; // Kasus satu anak
            } else {
                // Kasus ada 2 anak 
                // Ambil inorder successor
                Node temp = minValueNode(node.right);
                
                // Copy keynya
                node.key = temp.key;
                node.jumlahSkor = temp.jumlahSkor;

                temp.jumlahSkor = 1;
                // Delete inorder successor
                node.right = deleteNode(node.right, temp.key);
                node.jumlahAnak -= 1;
            }
        }

        // this.print2D(root);

        // Kasus apabila tree hanya ada satu node
        if (node == null) return node;

        // System.out.println("ini ngecek coba coba " + node.key);
        // Update height
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

        // Dapet balance untuk melakukan balancing pada AVL tree
        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) return rightRotate(node); 

        // Left Right Case 
        if (balance > 1 && getBalance(node.left) < 0) { 
            node.left = leftRotate(node.left); 
            return rightRotate(node); 
        } 

        // Right Right Case 
        if (balance < -1 && getBalance(node.right) <= 0) return leftRotate(node); 

        // Right Left Case 
        if (balance < -1 && getBalance(node.right) > 0) { 
            node.right = rightRotate(node.right); 
            return leftRotate(node); 
        } 

        // Apabila node tidak terjadi balancing
        return node;
    }

    // Inorder successor taken from geeks for geeks
    Node minValueNode(Node node) { 
        Node current = node; 

        /* loop down to find the leftmost leaf */
        while (current.left != null) current = current.left; 

        return current; 
    } 

        
    int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    // Reference https://www.geeksforgeeks.org/floor-in-binary-search-tree-bst/?ref=lbp
    Node lowerBound(Node node, int value) {
        // if (node != null && ((node.left == null && node.right == null && node.key >= value) || node.key == value)) return node;
        // else if (node == null || (node.left == null && node.right == null && node.key < value)) return null; 

        // Base case dan apabila 
        if (node == null || node.key == value) return node;

        // Cek anak kanan
        if (node.key < value) return lowerBound(node.right, value);

        // Berarti node akan ada di anak kiri atau node yg dimaksud sekarang
        Node ceil = lowerBound(node.left, value);

        if (ceil == null) return node;
        return (ceil.key >= value) ? ceil: node;

        // if (node.key > value && node.left != null) return lowerBound(node.left, value);
        // else if (node.key < value && node.right != null) return lowerBound(node.right, value);
        // else return node; 

    }

    Node upperBound(Node node, int value) {
        // if (node != null && ((node.left == null && node.right == null && node.key <= value) || node.key == value)) return node;
        // else if (node == null || (node.left == null && node.right == null && node.key > value)) return null;

        // if (node.key < value && node.right != null) return upperBound(node.right, value);
        // else if (node.key > value && node.left != null) return upperBound(node.left, value);
        // else return node;

        // Base case dan apabila 
        if (node == null || node.key == value) return node;

        // Cek anak kiri
        if (node.key > value) return upperBound(node.left, value);

        // Berarti node akan ada di anak kiri atau node yg dimaksud sekarang
        Node floor = upperBound(node.right, value);

        if (floor == null) return node;
        return (floor.key <= value) ? floor : node;
    }

    // Referensi: https://www.geeksforgeeks.org/count-greater-nodes-in-avl-tree/
    int countLesser(Node node, int x) {
        int res = 0;

        while (node != null) {
            int jumlah = (node.left != null) ? node.left.jumlahAnak + node.left.jumlahSkor : 0;
            if (node.key > x) {
                // res = res + jumlah + 1 + 1;
                node = node.left;
            } else if (node.key < x) {
                res = res + jumlah + node.jumlahSkor;
                node = node.right;
            } else {
                res = res + jumlah;
                break;
            }
        }

        return res;
    }

    int countGreater(Node node, int x) {
        int res = 0;

        while (node != null) {
            int jumlah = (node.right != null) ? node.right.jumlahAnak + node.right.jumlahSkor : 0;
            if (node.key > x) {
                // res = res + jumlah + 1 + 1;
                res = res + jumlah + node.jumlahSkor;
                node = node.left;
            } else if (node.key < x) {
                // res = res + jumlah + node.jumlahSkor;
                node = node.right;
            } else {
                res = res + jumlah;
                break;
            }
        }

        return res;
    }


    Node findMaxKey(Node node) {
        Node current = node; 

        /* loop down to find the leftmost leaf */
        while (current != null && current.right != null) current = current.right; 

        return current; 
    } 

    void printInorder(Node node) {
        if (node == null)
            return;
 
        /* first recur on left child */
        printInorder(node.left);
 
        /* then print the data of node */
        System.out.print(node.key + " ");
 
        /* now recur on right child */
        printInorder(node.right);
    }
}

class Machine implements Comparable<Machine>{
    Machine next, prev;
    int idMesin, jumlahSkorMesin;
    AVLSkorTree tree = new AVLSkorTree();

    Machine(int idMesin, int jumlahSkorMesin) {
        this.idMesin = idMesin;
        this.jumlahSkorMesin = jumlahSkorMesin;
    }

    void addSkor(int key) {
        // System.out.println("Ini key: " + key);
        tree.root = tree.insertNode(tree.root, key);
    }

    void setNewTree() {
        tree = new AVLSkorTree();
    }

    @Override
    public int compareTo(Machine o) {
        if (this.tree.jumlahNode > o.tree.jumlahNode) return -1; // Kalau dia lebih besar, taro paling depan
        if (this.tree.jumlahNode < o.tree.jumlahNode) return 1;
        if (this.idMesin < o.idMesin) return -1;
        if (this.idMesin > o.idMesin) return 1;
        return 0;
    } 
}

class LinkedMachine {
    int totalMachine = 0;
    Machine head, tail, current;
    Machine[] arrayMachine;

    LinkedMachine(int arraySize) {
        arrayMachine = new Machine[arraySize];
    }

    void addMachine(Machine newMachine) {
        if (head == null) {
            head = tail = newMachine;
            head.prev = newMachine;
            tail.next = newMachine;
        } else {
            Machine temp = tail;
            tail = newMachine;
            tail.prev = temp;
            temp.next = newMachine;
            tail.next = head;
            head.prev = tail;
        }
        arrayMachine[totalMachine] = newMachine;
        totalMachine += 1;
    }

    int handleMain(int skor) {
        current.tree.root = current.tree.insertNode(current.tree.root, skor);
        return current.tree.countGreater(current.tree.root, skor) + 1;
    }

    int handleGerak(String arahGerak) {
        if (arahGerak.equals("KANAN")) {
            current = current.next;
            return current.idMesin;
        } 
        current = current.prev;
        return current.idMesin;
    }

    long handleHapus(int x) {
        long res = 0;

        if (current.tree.jumlahNode <= x) {
            res = current.tree.sumSkor;
            current.setNewTree();
            Machine currentToChange = current;
            current = current.next;
            if (currentToChange != tail) { // From scele
                if (currentToChange == head) {
                    head = head.next;
                } else {
                    currentToChange.prev.next = currentToChange.next;
                    currentToChange.next.prev = currentToChange.prev;
                }
                Machine temp = tail;
                tail = currentToChange;
                tail.prev = temp;
                temp.next = currentToChange;
                tail.next = head;
                head.prev = tail;
            }
            return res;
        }
        for (int i = 0; i<x; i++) {
            int maxNodeKey = current.tree.findMaxKey(current.tree.root).key;
            current.tree.root = current.tree.deleteNode(current.tree.root, maxNodeKey);
            res += maxNodeKey;
        }
        return res;
    }

    int handleLihat(int lowerBound, int upperBound) {
        return current.tree.jumlahNode - (current.tree.countGreater(current.tree.root, upperBound) + current.tree.countLesser(current.tree.root, lowerBound));
    }

    int handleEvaluasi() {
        int resultEvaluasi = -1;

                
        // for (int j = 0; j<arrayMachine.length; j++) {
        //     System.out.println("INI Sebelum " + arrayMachine[j].idMesin);
        // }

        mergeSort(arrayMachine);


        this.head = arrayMachine[0];
        this.tail = arrayMachine[arrayMachine.length-1];
        
        // for (int j = 0; j<arrayMachine.length; j++) {
        //     System.out.println("INI BARU " + arrayMachine[j].idMesin);
        // }

        for (int i = 0; i < arrayMachine.length; i++) {
            if (i+1 != arrayMachine.length) arrayMachine[i].next = arrayMachine[i+1];
            else arrayMachine[i].next = head;

            if (i-1 != -1) arrayMachine[i].prev = arrayMachine[i-1];
            else arrayMachine[i].prev = tail;

            if (arrayMachine[i] == current) resultEvaluasi = i + 1;
        }

        return resultEvaluasi;
    }       

    // Reference: https://www.geeksforgeeks.org/merge-sort-for-doubly-linked-list/
    void print(Machine machine) {
        Machine temp = machine;
        System.out.println("Forward Traversal using next pointer");
        while (machine != null) {
            System.out.print(machine.jumlahSkorMesin + " ");
            temp = machine;
            machine = machine.next;
        }
        System.out.println("\nBackward Traversal using prev pointer");
        while (temp != null) {
            System.out.print(temp.jumlahSkorMesin + " ");
            temp = temp.prev;
        }
    }

    // Reference: https://www.youtube.com/watch?v=bOk35XmHPKs
    void mergeSort(Machine[] arrayMachine) {
        int inputLength = arrayMachine.length;

        if (inputLength < 2) return;

        int midIndex = inputLength/2;
        Machine[] leftHalf = new Machine[midIndex];
        Machine[] rightHalf = new Machine[inputLength-midIndex];

        for (int i = 0; i < midIndex; i++) {
            leftHalf[i] = arrayMachine[i];
        }

        for (int i = midIndex; i < inputLength; i++) {
            rightHalf[i-midIndex] = arrayMachine[i];
        }

        mergeSort(leftHalf);
        mergeSort(rightHalf);
        
        // Merge
        merge(arrayMachine, leftHalf, rightHalf);
    }

    void merge(Machine[] arrayMachine, Machine[] leftHalf, Machine[] rightHalf) {
        int leftSize = leftHalf.length;
        int rightSize = rightHalf.length;

        int i = 0, j = 0, k = 0;

        while (i < leftSize && j < rightSize) {
            // System.out.println("TES EVAL " + leftHalf[i].compareTo(rightHalf[j]));
            if (leftHalf[i].compareTo(rightHalf[j]) <= 0) {
                arrayMachine[k] = leftHalf[i];
                i++;
            } else {
                arrayMachine[k] = rightHalf[j];
                j++;
            }
            k++;
        }

        while (i < leftSize) {
            arrayMachine[k] = leftHalf[i];
            i++;
            k++;
        }

        while (j < rightSize) {
            arrayMachine[k] = rightHalf[j];
            j++;
            k++;
        }
    }

    // Machine split(Machine head) {
    //     Machine findLast = head, findMid = head;
    //     while (findLast.next != null && findMid.next.next != null) {
    //         findLast = findLast.next.next;
    //         findMid = findMid.next;
    //     }
    //     Machine temp = findMid.next;
    //     findMid.next = null;
    //     return temp;
    // }

    // Machine mergeSort(Machine machine) {
    //     if (machine == null || machine.next == null) return machine;

    //     Machine second = split(machine);

    //     // Recursive left half & right half
    //     machine = mergeSort(machine);
    //     second = mergeSort(second);

    //     return merge(machine, second);
    // }

    // Machine merge(Machine first, Machine second) {
    //     if (first == null) return second;
    //     if (second == null) return first;

    //     if (first.jumlahSkor < second.jumlahSkor) {
    //         first.next = merge(first.next, second);
    //         first.next.prev = first;
    //         first.prev = null;

    //     }
    // }
}