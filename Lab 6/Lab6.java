import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi class ini
class Saham implements Comparable<Saham> {
    public int id;
    public int position;
    public int harga;

    public Saham(int id, int harga) {
        this.id = id;
        this.harga = harga;
        this.position = 0;
    }

    @Override
    public int compareTo(Saham o) {
        if (o == null) return 1;
        if (this.harga < o.harga) return -1;
        if (this.harga > o.harga) return 1;
        if (this.id < o.id) return -1;
        if (this.id > o.id) return 1;
        return 0;
    }
}

// Sangat terinspirasi dari https://medium.com/@ankur.singh4012/implementing-min-heap-in-java-413d1c20f90d
// dan https://drive.google.com/drive/folders/1jsMPzfmydPEY-wEAXjhhCTTVSxJDDDmU
class MinHeap {
    ArrayList<Saham> arraySaham;

    public MinHeap() {
        arraySaham = new ArrayList<Saham>();
    }

    int getParentIdx (int i){
        return (i-1)/2;
    }

    int getLeftIdx(int i){
        return 2*i + 1;
    }

    int getRightIdx(int i){
        return 2 * (i+1);
    }

    // boolean hasLeftChild(int index) {
    //     return getLeftIdx(index) < size;
    // }
    
    // boolean hasRightChild(int index) {
    //     return getRightIdx(index) < size;
    // }
    
    boolean hasParent(int index) {
        return getParentIdx(index) >= 0;
    }

    // boolean isLeaf(int pos) {
    //     if (pos > (size / 2)) {
    //         return true;
    //     }
    //     return false;
    // }

    void swap(int fpos, int spos) {
        Saham tmp = arraySaham.get(fpos);
        arraySaham.set(fpos, arraySaham.get(spos));
        arraySaham.set(spos, tmp);
    }

    Saham leftChild(int parentIndex) {
        return arraySaham.get(getLeftIdx(parentIndex));
    }
    
    Saham rightChild(int parentIndex) {
        return arraySaham.get(getRightIdx(parentIndex));
    }
    
    Saham parent(int childIndex) {
        return arraySaham.get(getParentIdx(childIndex));
    }
    

    // void swapInsert(int current) {
    //     Saham temp = arraySaham[current];
    //     arraySaham[current] = arraySaham[parent(current)];
    //     arraySaham[parent(current)] = temp;
    // }

    // void insert(Saham newSaham) {
    //     arraySaham[size] = newSaham;

    //     int current = size;
    //     size++;

    //     while (arraySaham[current].compareTo(arraySaham[parent(current)]) < 0) {
    //         if (current == 0) break;
    //         swap(current, parent(current));
    //     }
    // }

    void insert(Saham newSaham) {
        arraySaham.add(newSaham);
        heapifyUp(arraySaham.size() - 1);
    }

    public Saham peek() {
        if (arraySaham.isEmpty()) return null;
        return arraySaham.get(0);
    }

    // https://www.digitalocean.com/community/tutorials/min-heap-binary-tree
    Saham delete_min() {
        // if (size == 0) {
        //     return null;
        // }

        // Saham lastElement = arraySaham[size-1];

        // Saham res = arraySaham[0];
        // arraySaham[0] = lastElement;

        // size--;

        // heapifyDown();
        // return res;

        Saham removedObject = peek();

		if (arraySaham.size() == 1) arraySaham.clear();
		else {
            arraySaham.set(0, arraySaham.get(arraySaham.size()- 1));
            arraySaham.remove(arraySaham.size() - 1);
			heapifyDown(0);
		}

		return removedObject;
    }

    void heapifyUp(int idx) {
        Saham node = arraySaham.get(idx);
        int parentIdx = getParentIdx(idx);
        while(idx > 0 && node.compareTo(arraySaham.get(parentIdx)) < 0) {
            arraySaham.set(idx, arraySaham.get(parentIdx));
            arraySaham.get(parentIdx).position = idx;
            idx = parentIdx;
            parentIdx = getParentIdx(idx);
        }
        arraySaham.get(idx).position = idx;
        arraySaham.set(idx, node);
    }

    void heapifyDown(int idx) {
        Saham node = arraySaham.get(idx);
		int heapSize = arraySaham.size();

		while (true) {
			int leftIdx = getLeftIdx(idx);
			if (leftIdx >= heapSize) {
                arraySaham.set(idx, node);
                arraySaham.get(idx).position = idx;
				break;
			} else {
				int minChildIdx = leftIdx;
				int rightIdx = getRightIdx(idx);
				if (rightIdx < heapSize && arraySaham.get(rightIdx).compareTo(arraySaham.get(leftIdx)) < 0) minChildIdx = rightIdx;

				if (node.compareTo(arraySaham.get(minChildIdx)) > 0) {
                    arraySaham.set(idx, arraySaham.get(minChildIdx));
                    arraySaham.get(minChildIdx).position = idx;
					idx = minChildIdx;
				} else {
                    arraySaham.set(idx, node);
                    arraySaham.get(idx).position = idx;
					break;
				}
			}
		}
    }



    // void percolateUp(int idx) {
    //     Saham node = arraySaham[idx];
    //     int parentIdx = getParentIdx(idx);
    //     while(idx > 0 && node.compareTo(arraySaham[parentIdx]) < 0){
    //         arraySaham[idx] = arraySaham[getParentIdx(idx)];
    //         // arraySaham[getParentIdx(idx)].pos = idx;
    //         idx = getParentIdx(idx);
    //         parentIdx = getParentIdx(idx);
    //     }
    //     // arraySaham[idx].pos = idx;
    //     arraySaham[idx] = node;
    // }

    // void percolateDown(int index) {
    //     if (index > (size/2) && index < size) return;
    //     if (index >= size) return;

    //     if (arraySaham[index] == null || (arraySaham[getLeftIdx(index)] != null && arraySaham[index].compareTo(arraySaham[getLeftIdx(index)]) > 0) || (arraySaham[getRightIdx(index)] != null && arraySaham[index].compareTo(arraySaham[getRightIdx(index)]) > 0)) {
    //         if (arraySaham[getLeftIdx(index)] == null || (arraySaham[getRightIdx(index)]!=null && arraySaham[getRightIdx(index)].harga<arraySaham[getLeftIdx(index)].harga)
    //         || (arraySaham[getRightIdx(index)]!= null && arraySaham[getRightIdx(index)].harga==arraySaham[getLeftIdx(index)].harga
    //                 && arraySaham[getRightIdx(index)].id<arraySaham[getLeftIdx(index)].id)) {
    //                     Saham temp = arraySaham[index];
    //                     arraySaham[index] = arraySaham[getRightIdx(index)];
    //                     arraySaham[getRightIdx(index)] = temp;
    //                     percolateDown(getRightIdx(index));
    //         } else if (arraySaham[getRightIdx(index)] == null || arraySaham[getLeftIdx(index)] != null) {
    //             Saham temp = arraySaham[index];
    //             arraySaham[index] = arraySaham[getLeftIdx(index)];
    //             arraySaham[getLeftIdx(index)] = temp;
    //             percolateDown(getLeftIdx(index));
    //         } 
    //     }
    // }

    // https://stackoverflow.com/questions/15493056/min-heapify-method-min-heap-algorithm
    void minHeapify(int parentIndex) {     
        int left = getLeftIdx(parentIndex);
        int right = getRightIdx(parentIndex);
        int smallest = parentIndex;
        if(arraySaham.size()>left && arraySaham.get(left).compareTo(arraySaham.get(parentIndex)) < 0)
        {
            smallest = left;
        }

        if(arraySaham.size()>right && arraySaham.get(right).compareTo(arraySaham.get(parentIndex)) < 0)
        {
            smallest = right;
        }

        if(smallest != parentIndex)
        {
            swap(parentIndex, smallest);
            minHeapify(smallest);
        }
    }

    // Saham delete_element(int index) {
    //     arraySaham[index] = arraySaham[0];
    //     arraySaham[index].harga -= 1;
        
    //     Saham res = arraySaham[index];
    //     int current = index;
    //     while (current > 0 && arraySaham[current].compareTo(arraySaham[getParentIdx(current)]) < 0) {
    //         Saham temp = arraySaham[getParentIdx(current)];
    //         arraySaham[getParentIdx(current)] = arraySaham[current];
    //         arraySaham[current] = temp;
    //         current = getParentIdx(current);
    //     } 
        
    //     delete_min();
    //     return res;
    // }
}

// https://www.digitalocean.com/community/tutorials/max-heap-java
class MaxHeap {
    ArrayList<Saham> arraySaham;

    public MaxHeap() {
        arraySaham = new ArrayList<Saham>();
    }

    int getParentIdx (int i){
        return (i-1)/2;
    }

    int getLeftIdx(int i){
        return 2*i + 1;
    }

    int getRightIdx(int i){
        return 2 * (i+1);
    }

    // boolean hasLeftChild(int index) {
    //     return getLeftIdx(index) < size;
    // }
    
    // boolean hasRightChild(int index) {
    //     return getRightIdx(index) < size;
    // }
    
    // boolean hasParent(int index) {
    //     return getParentIdx(index) >= 0;
    // }

    // boolean isLeaf(int pos) {
    //     if (pos > (size / 2)) {
    //         return true;
    //     }
    //     return false;
    // }

    void swap(int fpos, int spos) {
        Saham tmp = arraySaham.get(fpos);
        arraySaham.set(fpos, arraySaham.get(spos));
        arraySaham.set(spos, tmp);
    }

    void heapifyUp(int idx) {
        Saham node = arraySaham.get(idx);
        int parentIdx = getParentIdx(idx);
        while(idx > 0 && node.compareTo(arraySaham.get(parentIdx)) > 0){
            arraySaham.set(idx, arraySaham.get(parentIdx));
            arraySaham.get(parentIdx).position = idx;
            idx = parentIdx;
            parentIdx = getParentIdx(idx);
        }
        arraySaham.get(idx).position = idx;
        arraySaham.set(idx, node);
    }

    void downHeapify(int idx) {
        // //checking if the node is a leaf node 
        // if (pos >= (size / 2) && pos <= size)
        //     return;
        
        // //checking if a swap is needed
        // if (arraySaham[pos].compareTo(arraySaham[getLeftIdx(pos)]) < 0 ||
        //         arraySaham[pos].compareTo(arraySaham[getRightIdx(pos)]) < 0) {
        
        // //replacing parent with maximum of left and right child 
        //     if (arraySaham[getLeftIdx(pos)].compareTo(arraySaham[getRightIdx(pos)]) > 0) {
        //         swap(pos, getLeftIdx(pos));
        //         //after swaping, heapify is called on the children 
        //         downHeapify(getLeftIdx(pos));
        //     } else {
        //         swap(pos, getRightIdx(pos));
        //         //after swaping, heapify is called on the children 
        //         downHeapify(getRightIdx(pos));
        //     }
        // }

        Saham node = arraySaham.get(idx);
		int heapSize = arraySaham.size();

		while (true) {
			int leftIdx = getLeftIdx(idx);
			if (leftIdx >= heapSize) {
                arraySaham.set(idx, node);
                arraySaham.get(idx).position = idx;
				break;
			} else {
				int minChildIdx = leftIdx;
				int rightIdx = getRightIdx(idx);
				if (rightIdx < heapSize && arraySaham.get(rightIdx).compareTo(arraySaham.get(leftIdx)) > 0)
					minChildIdx = rightIdx;

				if (node.compareTo(arraySaham.get(minChildIdx)) < 0) {
                    arraySaham.set(idx, arraySaham.get(minChildIdx));
                    arraySaham.get(minChildIdx).position = idx;
					idx = minChildIdx;
				} else {
                    arraySaham.set(idx, node);
                    arraySaham.get(idx).position = idx;
					break;
				}
			}
		}
    }

    void insert(Saham newSaham) {
        arraySaham.add(newSaham);
        // System.out.println("Ini insert heap size " + (size-1));
        heapifyUp(arraySaham.size()-1);
    }

    Saham peek() {
        if (arraySaham.isEmpty()) return null;
        return arraySaham.get(0);
    }

    Saham delete_max() {
        // if (size == 0) {
        //     return null;
        // }

        // Saham lastElement = arraySaham[size-1];

        // Saham res = arraySaham[0];
        // arraySaham[0] = lastElement;

        // size--;

        // downHeapify(0);
        // return res;

        
        Saham removedObject = peek();

		if (arraySaham.size() == 1) arraySaham.clear();
		else {
            arraySaham.set(0, arraySaham.get(arraySaham.size()-1));
            arraySaham.remove(arraySaham.size()-1);
			downHeapify(0);
		}

		return removedObject;


        // Saham lastElement = arraySaham[size-1];

        // Saham res = arraySaham[0];
        // arraySaham[0] = lastElement;

        // size--;

        // downHeapify(0);
        // return res;
        // Saham max = arraySaham[0];
        // arraySaham[0] = arraySaham[size--];
        // downHeapify(0);
        // return max;
    }

    // Saham delete_element(int index) {
    //     arraySaham[index] = arraySaham[0];
    //     arraySaham[index].harga += 1;
        
    //     Saham res = arraySaham[index];
    //     int current = index;
    //     while (current > 0 && arraySaham[current].compareTo(arraySaham[getParentIdx(current)]) > 0) {
    //         Saham temp = arraySaham[getParentIdx(current)];
    //         arraySaham[getParentIdx(current)] = arraySaham[current];
    //         arraySaham[current] = temp;
    //         current = getParentIdx(current);
    //     }
        
    //     delete_max();
    //     return res;
    // }
}

public class Lab6 {

    private static InputReader in;
    private static PrintWriter out;
    static MinHeap minheap = new MinHeap();
    static MaxHeap maxheap = new MaxHeap();
    static Map<Integer, Saham> map = new HashMap<Integer, Saham>();
    static Saham currentMedian;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        Saham[] arrSahamSemua = new Saham[400001];
        int N = in.nextInt();

        for (int i = 1; i <= N; i++) {
            int harga = in.nextInt();
            Saham newSaham = new Saham(i, harga);
            // System.out.println("ini i saat penambahan " + i);
            addSaham(newSaham);
            map.put(i, newSaham);

            arrSahamSemua[i] = newSaham;
        }

        int Q = in.nextInt();

        // TODO
        for (int i = 0; i < Q; i++) {
            // out.println();
            // out.println();

            String q = in.next();

            if (q.equals("TAMBAH")) {
                int harga = in.nextInt();
                Saham newSaham = new Saham(N+1, harga);
                addSaham(newSaham);
                map.put(N+1, newSaham);
                N += 1;
                out.println(currentMedian.id);
            } else if (q.equals("UBAH")) {
                int nomorSeri = in.nextInt();
                int harga = in.nextInt();
                update(nomorSeri, harga);
                out.println(currentMedian.id);
            }
            // out.println("Test " + q);
            
            // for (int j = 0; j < maxheap.size; j++) {
            //     out.println(maxheap.arraySaham[j].id + " " + "dengan harga " + maxheap.arraySaham[j].harga);
            // }
            
            // out.println("MIN HEAP  ");

            // for (int j = 0; j < minheap.size; j++) {
            //     out.println(minheap.arraySaham[j].id + " dengan harga " + minheap.arraySaham[j].harga);
            // }
        }
        out.flush();
    }

    static void addSaham(Saham newSaham) {
        if (maxheap.arraySaham.size() == 0 && minheap.arraySaham.size() == 0) {
            maxheap.insert(newSaham);
            currentMedian = newSaham;
            return;
        }

        currentMedian = findMedian();
        if (newSaham.compareTo(currentMedian) <= 0) {
            maxheap.insert(newSaham);
        } else {
            minheap.insert(newSaham);
        }
        balancing();
        currentMedian = findMedian();
    }

    static Saham findMedian() {
        if (maxheap.arraySaham.size() == minheap.arraySaham.size()) {
            // if (maxheap.arraySaham.size() == 0) {
            //     return null;
            // }
            return minheap.arraySaham.get(0);
        } else if (maxheap.arraySaham.size() == 1 + minheap.arraySaham.size()) {
            return maxheap.arraySaham.get(0);
        } else {
            return minheap.arraySaham.get(0);
        }
    }

    static void balancing() {
        if (Math.abs(maxheap.arraySaham.size()-minheap.arraySaham.size()) > 1) {
            if (maxheap.arraySaham.size() > minheap.arraySaham.size()) {
                Saham temp = maxheap.delete_max();
                minheap.insert(temp);
            } else if (maxheap.arraySaham.size() < minheap.arraySaham.size()) {
                Saham temp = minheap.delete_min();
                maxheap.insert(temp);
            }
        }
    }

    static void update(int nomorSeri, int hargaBaru) {     
        Saham sahamYangDicari = map.get(nomorSeri);
        
        Saham median = findMedian();
        if (sahamYangDicari.compareTo(median) < 0) {
            // MaxHeap
            sahamYangDicari.harga = hargaBaru;
            int posisiSaham = sahamYangDicari.position;
            maxheap.downHeapify(posisiSaham);
            maxheap.heapifyUp(sahamYangDicari.position);

            if (maxheap.arraySaham.get(0).compareTo(minheap.arraySaham.get(0)) > 0) {
                Saham sahamYangPengenDitaroLagi = maxheap.delete_max();
                balancing();
                addSaham(sahamYangPengenDitaroLagi);
                currentMedian = findMedian();
            }
            currentMedian = findMedian();

        } else if (sahamYangDicari.compareTo(median) > 0) {
            // MinHeap
            sahamYangDicari.harga = hargaBaru;
            int posisiSaham = sahamYangDicari.position;
            minheap.heapifyDown(posisiSaham);
            minheap.heapifyUp(sahamYangDicari.position);

            if (minheap.arraySaham.get(0).compareTo(maxheap.arraySaham.get(0)) < 0) {
                Saham sahamYangPengenDitaroLagi = minheap.delete_min();
                balancing();
                addSaham(sahamYangPengenDitaroLagi);
                currentMedian = findMedian();
            }
            currentMedian = findMedian();

        } else {
            // Median
            if (minheap.arraySaham.get(0) == median) {
                // MinHeap
                median.harga = hargaBaru;
                // Saham medianTemp = median;
                minheap.heapifyDown(0);
                minheap.heapifyUp(sahamYangDicari.position);
                if (minheap.arraySaham.get(0).compareTo(maxheap.arraySaham.get(0)) <= 0) { // rootminheap < rootmaxheap
                    Saham sahamYangPengenDitaroLagi = minheap.delete_min();
                    balancing();
                    addSaham(sahamYangPengenDitaroLagi);
                    currentMedian = findMedian();
                }
                currentMedian = findMedian();
            } else {
                // MaxHeap
                median.harga = hargaBaru;
                maxheap.downHeapify(0);
                maxheap.heapifyUp(sahamYangDicari.position);

                if (maxheap.arraySaham.get(0).compareTo(minheap.arraySaham.get(0)) >= 0) { // rootmaxheap > rootminheap
                    Saham sahamYangPengenDitaroLagi = maxheap.delete_max();
                    balancing();
                    addSaham(sahamYangPengenDitaroLagi);
                    currentMedian = findMedian();
                }
                currentMedian = findMedian();

            }
        }
        // int i = 0;
        // while(i < sizeMax) {
        //     // System.out.println("ini i " + i);
        //     if (maxheap.arraySaham[i].id == nomorSeri) {
        //         idYangDicari = i;
        //         max = true;
        //         break;
        //     }
        //     i++;
        // }

        // int sizeMin = minheap.size;
        // int j = 0;
        // while(j < sizeMin) {
        //     // System.out.println("ini j " + j);
        //     if (minheap.arraySaham[j].id == nomorSeri) {
        //         idYangDicari = j;
        //         min = true;
        //         break;
        //     }
        //     j++;
        // }

        // Saham temp = null;
        // if (idYangDicari == -1) return;

        // if (max == true) temp = maxheap.delete_element(idYangDicari); 
        // else if (min == true) temp = minheap.delete_element(idYangDicari);
        
        // // out.println("Kondisi max " + max + "kondisi min " + min );

        // if (temp == null) return;
        // balancing();
        // // out.println("Ini id apa? " + temp.id);
        // temp.harga = hargaBaru;
        // addSaham(temp);
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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}