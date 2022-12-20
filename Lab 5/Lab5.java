import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Stack;
import java.util.StringTokenizer;

// Collaborator: Naufal Alsar - 2106752041
// Collaborator: Achieva Futura Gemilang
// Collaborator: Rafif Naufal Rahmadika - 2106636275
// Collaborator: Muhammad Hadziq Razin - 2106707076


public class Lab5 {

    private static InputReader in;
    static PrintWriter out;
    static AVLTree tree = new AVLTree();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int numOfInitialPlayers = in.nextInt();
        for (int i = 0; i < numOfInitialPlayers; i++) {
            String playerName = in.next();
            // out.println("playerName: " + playerName);
            int powerLevel = in.nextInt();
            tree.root = tree.insertNode(tree.root, powerLevel, playerName);
            out.println(tree.countLesser(tree.root, powerLevel));
        }

        int numOfQueries = in.nextInt();
        for (int i = 0; i < numOfQueries; i++) {
            String cmd = in.next();
            // System.out.println("empty stack " + i);
            if (cmd.equals("MASUK")) {
                handleQueryMasuk();
            } else {
                handleQueryDuo();
            }
        }

        out.close();
    }

    static void handleQueryMasuk() {
        String playerName = in.next();
        int powerLevel = in.nextInt();
        tree.root = tree.insertNode(tree.root, powerLevel, playerName);
        // tree.print2D(tree.root);
        out.println(tree.countLesser(tree.root, powerLevel));
    }

    static void handleQueryDuo() {
        int lowerBoundInterval = in.nextInt();
        int upperBoundInterval = in.nextInt();
        Node lowerBoundNode = tree.lowerBound(tree.root, lowerBoundInterval);

        if (lowerBoundInterval > upperBoundInterval) {
            out.println("-1 -1");
            return;
        }
        
        if (lowerBoundNode == null) {
            out.println("-1 -1");
            return;
        }

        // lowerBoundNode.printStack();

        String playerLowerBound = "";
        if (lowerBoundNode.namaPlayer.size() >= 1) {
            playerLowerBound = lowerBoundNode.namaPlayer.pop();
        } else {
            playerLowerBound = lowerBoundNode.playerName;
        }
        // System.out.println("Ini interval " + lowerBoundInterval + " tapi lowerboudnnya " + lowerBoundNode.key + "dan nama player " + playerLowerBound);

        Node upperBoundNode = tree.upperBound(tree.root, upperBoundInterval);

        if (upperBoundNode == null || upperBoundNode.key < lowerBoundNode.key) {
            out.println("-1 -1");
            if (lowerBoundNode.jumlahPlayer > 1) lowerBoundNode.namaPlayer.push(playerLowerBound);
            return;
        }

    
        String playerUpperBound = "";

        if (upperBoundNode.namaPlayer.size() >= 1) {
            playerUpperBound = upperBoundNode.namaPlayer.pop();
        } else {
            playerUpperBound = upperBoundNode.playerName;
        }
        // System.out.println("Ini interval " + upperBoundInterval + " tapi upperboundnya " + upperBoundNode.key + "dan nama player " + playerUpperBound);

        if (playerLowerBound.compareTo(playerUpperBound) == 0) {
            out.println("-1 -1");
            return;
        }
        
        // if (upperBoundNode.key == lowerBoundNode.key && upperBoundNode.namaPlayer.isEmpty()) {
        //     out.println("-1 -1");
        //     lowerBoundNode.pushNamaPlayer(playerLowerBound);
        //     return;
        // } 

        tree.root = tree.deleteNode(tree.root, lowerBoundNode.key);
        tree.root = tree.deleteNode(tree.root, upperBoundNode.key);
        
        printLexicographical(playerLowerBound, playerUpperBound);
    }

    static void printLexicographical(String playerAtas, String playerBawah) {
        if (playerBawah.compareTo(playerAtas) > 0) out.println(playerAtas + " " + playerBawah);
        else out.println(playerBawah + " " + playerAtas);
    }
    // taken from https://codeforces.com/submissions/Petr
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
    int key, height, jumlahPlayer, lesserThan;
    Node left, right;
    String playerName;
    Stack<String> namaPlayer = new Stack<String>();

    Node(int key, String playerName) {
        this.key = key;
        this.height = 1;
        this.jumlahPlayer = 1;
        this.lesserThan = 0;
        this.playerName = playerName;
        // System.out.println("Nama player adalah " + this.playerName);
    }

    void pushNamaPlayer(String playerName) {
        namaPlayer.push(playerName);
    }

    void printStack() {
        System.out.println(Arrays.toString(namaPlayer.toArray()));
    }
}

/* Reference taken from Geeks For Geeks (I believe this is allowed), https://www.geeksforgeeks.org/deletion-in-an-avl-tree/?ref=lbp */
class AVLTree {

    Node root;

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
        int jmlAnak = 0;
        if (melihatAnak != null) jmlAnak = melihatAnak.lesserThan + melihatAnak.jumlahPlayer;
        System.out.println("Jumlah anak " + jmlAnak);
        System.out.println("Jumlah lesser than node " + node.lesserThan + "jumlah lessser than nodetoberotated " + nodeToBeRotated.lesserThan);
        node.lesserThan = node.lesserThan - (nodeToBeRotated.lesserThan + nodeToBeRotated.jumlahPlayer) + jmlAnak;
        nodeToBeRotated.lesserThan = nodeToBeRotated.lesserThan - jmlAnak + node.lesserThan + node.jumlahPlayer;
        System.out.println("Setelah rebalancing node X " + node.lesserThan + " node Y " + nodeToBeRotated.lesserThan);
        
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
        int jmlAnak = 0;
        if (melihatAnak != null) jmlAnak = melihatAnak.lesserThan + melihatAnak.jumlahPlayer;
        System.out.println("Jumlah anak " + jmlAnak);
        System.out.println("Jumlah lesser than node " + node.lesserThan + "jumlah lessser than nodetoberotated " + nodeToBeRotated.lesserThan);
        node.lesserThan = node.lesserThan - (nodeToBeRotated.lesserThan + nodeToBeRotated.jumlahPlayer) + jmlAnak;
        nodeToBeRotated.lesserThan = nodeToBeRotated.lesserThan - jmlAnak + node.lesserThan + node.jumlahPlayer;
        System.out.println("Setelah rebalancing node X " + node.lesserThan + " node Y " + nodeToBeRotated.lesserThan);

        // Return new root
        return nodeToBeRotated;    
    }

    Node insertNode(Node node, int key, String playerName) {        
        if (node == null) {
            Node new_node = new Node(key, playerName);
            return new_node;
        }
        
        // Insert Node
        if (key < node.key) {
            node.lesserThan += 1;
            node.left = insertNode(node.left, key, playerName);
        }
        else if (key > node.key) {
            node.lesserThan += 1;
            node.right = insertNode(node.right, key, playerName);
        } else {
            // System.out.println("Nama player adalah " + playerName);
            node.pushNamaPlayer(playerName);
            node.jumlahPlayer += 1;
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
            node.lesserThan -= 1;
            node.left = deleteNode(node.left, key);
        }
        else if (key > node.key) {
            node.lesserThan -= 1;
            node.right = deleteNode(node.right, key);
        }
        // Kalau node yg dimaksud merupakan node yang benar, maka lakukan proses
        else {
            if (node.jumlahPlayer > 1) {
                node.jumlahPlayer -= 1;
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
                node.namaPlayer = temp.namaPlayer;
                node.playerName = temp.playerName;
                node.jumlahPlayer = temp.jumlahPlayer;

                temp.jumlahPlayer = 1;
                // Delete inorder successor
                node.right = deleteNode(node.right, temp.key);
                node.lesserThan -= 1;
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

    // Inorder successor taken from geeks for geeks
    Node minValueNode(Node node) { 
        Node current = node; 

        /* loop down to find the leftmost leaf */
        while (current.left != null) current = current.left; 

        return current; 
    } 

    // Reference from https://stackoverflow.com/questions/53242229/count-of-nodes-in-binary-search-tree-smaller-than-key
    int countLesserKey(Node node, int key) {
        int res = 0;

        if (node == null) return 0;

        else if (node.key < key) {
            // System.out.println("Ini jumlah player tiap tiap " + node.key + " " + node.jumlahPlayer);
            res += node.jumlahPlayer;
            res += countLesserKey(node.left, key);
            res += countLesserKey(node.right, key);
        } 
        
        else {
            // System.out.println("Ini jumlah player tiap tiap " + node.key + " " + node.jumlahPlayer);

            res += countLesserKey(node.left, key);}

        return res;
    }

    // Utility function to get height of node
    int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get balance factor of node
    int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    // Referensi: https://www.geeksforgeeks.org/count-greater-nodes-in-avl-tree/
    int countLesser(Node node, int x) {
        int res = 0;

        while (node != null) {
            System.out.println("Ini untuk melihat node " + node.key);
            System.out.println("Ini node lesser than semua " + node.lesserThan);
            int jumlah = (node.left != null) ? node.left.lesserThan + node.left.jumlahPlayer : 0;
            System.out.println("Ini jumlah = " + jumlah);
            if (node.key > x) {
                // res = res + jumlah + 1 + 1;
                node = node.left;
            } else if (node.key < x) {
                res = res + jumlah + node.jumlahPlayer;
                System.out.println("Ini res node.key<x = " + res);
                node = node.right;
            } else {
                res = res + jumlah;
                System.out.println("Ini res equal = " + res);
                break;
            }
        }

        return res;
    }
    // final int COUNT = 10;
    // void print2DUtil(Node root, int space)
    // {
    //     // Base case
    //     if (root == null)
    //         return;

    //     // Increase distance between levels
    //     space += COUNT;

//     // Process right child first
    //     print2DUtil(root.right, space);

    //     // Print current node after space
    //     // count
    //     System.out.print("\n");
    //     for (int i = COUNT; i < space; i++)
    //         System.out.print(" ");
    //     System.out.print(root.key + "\n");
 
    //     // Process left child
    //     print2DUtil(root.left, space);
    // }
 
    // // Wrapper over print2DUtil()
    // void print2D(Node root)
    // {
    //     // Pass initial space count as 0
    //     print2DUtil(root, 0);
    // }}
}