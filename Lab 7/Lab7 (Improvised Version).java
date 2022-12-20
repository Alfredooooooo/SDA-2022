import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Lab7Solusi {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt(), M = in.nextInt(); // N = jumlah benteng, M = jumlah benteng diserang
        ArrayList<ArrayList<Distance>> tempData = new ArrayList<>();

        for (int i = 1; i <= N; i++) {
            tempData.add(new ArrayList<>());
        }

        int[] daftarBentengDiserang = new int[M];
        for (int i = 0; i < M; i++) {
            daftarBentengDiserang[i] = in.nextInt() - 1; // menggunakan 0 indexing
        }

        int E = in.nextInt();
        for (int i = 0; i < E; i++) {
            // tukar antara destination dan source karena kita akan
            // melakukan Dijkstra dari benteng yang DISERANG
            int destination = in.nextInt() - 1; // menggunakan 0 indexing
            int source = in.nextInt() - 1; // menggunakan 0 indexing
            int weight = in.nextInt(); 
            tempData.get(source).add(new Distance(destination, weight)); // menggunakan 0 indexing
        }

        int[][] neighbours = new int[N + 2][];
        long[][] weights = new long[N + 2][];
        for (int i = 0; i < N; i++) {
            ArrayList<Distance> currData = tempData.get(i);
            int size = currData.size();
            neighbours[i] = new int[size];
            weights[i] = new long[size];
            for (int j = 0; j < size; j++) {
                Distance distance = currData.get(j);
                neighbours[i][j] = distance.destination;
                weights[i][j] = distance.weight_distance;
            }
        }

        ArrayList<ArrayList<Long>> shortestPath = new ArrayList<>();

        for (int i = 0; i<M; i ++) {
            ArrayList<Long> currShortestPath = new ArrayList<>();
            for (int j = 0; j < N; j++) {
                currShortestPath.add((long) 1e18);
            }

            MinHeap pq = new MinHeap();
            pq.insert(new Distance(daftarBentengDiserang[i], 0));

            while (pq.size > 0) {
                Distance current = pq.delete_min();
                int benteng = current.destination;
                long jumlahMusuh = current.weight_distance;

                // Seandainya kalau sudah optimal / weight edge lebih besar dari memo maka terminate (karena dari memo udh lebih cepet)
                if (currShortestPath.get(benteng) <= jumlahMusuh)
                    continue;
                
                currShortestPath.set(benteng, jumlahMusuh);
                int[] currNeighbours = neighbours[benteng];
                long[] currWeights = weights[benteng];
                // ArrayList<Distance> currData = tempData.get(benteng);
                // int size = currData.size();

                for (int j = 0; j < currWeights.length; j++) {
                    // Seandainya kalau memonya lebih besar, maka masukin pq agar terupdate!
                    if (currShortestPath.get(currNeighbours[j]) > currWeights[j] + jumlahMusuh) {
                        // Akan selalu update current weight distance dengan weights[j] (weight node yg dikunjungi)
                        pq.insert(new Distance(currNeighbours[j], currWeights[j] + jumlahMusuh));
                    }
                }
            }
            // Add memo ke index source benteng yang skrg di iterasikan
            shortestPath.add(currShortestPath);
        }
        // out.println("tes");
        int Q = in.nextInt();
        // System.out.println("INi " + Q);
        // out.println("tes");
        while (Q-- > 0) {
            tes(shortestPath);
        }

        out.close();
    }
    
    static void tes(ArrayList<ArrayList<Long>> shortestPath) {
        int source = in.nextInt() - 1; // menggunakan 0 indexing
        long power = in.nextInt();

        for (int i = 0; i < shortestPath.size(); i++) {
            //                 System.out.println("Ini musuh " + shortestPath.get(i).get(source));
            // System.out.println("Ini power " + power);
            if (shortestPath.get(i).get(source) < power) {
                out.println("YES");
                return;
            }
        }
        out.println("NO");
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

    }

    
}

class Distance implements Comparable<Distance> {
    int destination;
    long weight_distance;

    public Distance(int destination, long weight) {
        this.destination = destination;
        this.weight_distance = weight;
    }

    @Override
    public int compareTo(Distance o) {
        // TODO Auto-generated method stub
        if (weight_distance < o.weight_distance) return -1;
        if (weight_distance > o.weight_distance) return 1;
        return 0;
    }
}

class MinHeap {
    ArrayList<Distance> arrayDistance;
    int size = 0;

    public MinHeap() {
        arrayDistance = new ArrayList<Distance>();
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
        Distance tmp = arrayDistance.get(fpos);
        arrayDistance.set(fpos, arrayDistance.get(spos));
        arrayDistance.set(spos, tmp);
    }

    Distance leftChild(int parentIndex) {
        return arrayDistance.get(getLeftIdx(parentIndex));
    }
    
    Distance rightChild(int parentIndex) {
        return arrayDistance.get(getRightIdx(parentIndex));
    }
    
    Distance parent(int childIndex) {
        return arrayDistance.get(getParentIdx(childIndex));
    }
    

    // void swapInsert(int current) {
    //     Distance temp = arrayDistance[current];
    //     arrayDistance[current] = arrayDistance[parent(current)];
    //     arrayDistance[parent(current)] = temp;
    // }

    // void insert(Distance newDistance) {
    //     arrayDistance[size] = newDistance;

    //     int current = size;
    //     size++;

    //     while (arrayDistance[current].compareTo(arrayDistance[parent(current)]) < 0) {
    //         if (current == 0) break;
    //         swap(current, parent(current));
    //     }
    // }

    void insert(Distance newDistance) {
        size++;
        arrayDistance.add(newDistance);
        heapifyUp(arrayDistance.size() - 1);
    }

    public Distance peek() {
        if (arrayDistance.isEmpty()) return null;
        return arrayDistance.get(0);
    }

    // https://www.digitalocean.com/community/tutorials/min-heap-binary-tree
    Distance delete_min() {
        // if (size == 0) {
        //     return null;
        // }

        // Distance lastElement = arrayDistance[size-1];

        // Distance res = arrayDistance[0];
        // arrayDistance[0] = lastElement;

        // size--;

        // heapifyDown();
        // return res;
        size--;
        Distance removedObject = peek();

		if (arrayDistance.size() == 1) arrayDistance.clear();
		else {
            arrayDistance.set(0, arrayDistance.get(arrayDistance.size()- 1));
            arrayDistance.remove(arrayDistance.size() - 1);
			heapifyDown(0);
		}

		return removedObject;
    }

    void heapifyUp(int idx) {
        Distance node = arrayDistance.get(idx);
        int parentIdx = getParentIdx(idx);
        while(idx > 0 && node.compareTo(arrayDistance.get(parentIdx)) < 0) {
            arrayDistance.set(idx, arrayDistance.get(parentIdx));
            // arrayDistance.get(parentIdx).position = idx;
            idx = parentIdx;
            parentIdx = getParentIdx(idx);
        }
        // arrayDistance.get(idx).position = idx;
        arrayDistance.set(idx, node);
    }

    void heapifyDown(int idx) {
        Distance node = arrayDistance.get(idx);
		int heapSize = arrayDistance.size();

		while (true) {
			int leftIdx = getLeftIdx(idx);
			if (leftIdx >= heapSize) {
                arrayDistance.set(idx, node);
                // arrayDistance.get(idx).position = idx;
				break;
			} else {
				int minChildIdx = leftIdx;
				int rightIdx = getRightIdx(idx);
				if (rightIdx < heapSize && arrayDistance.get(rightIdx).compareTo(arrayDistance.get(leftIdx)) < 0) minChildIdx = rightIdx;

				if (node.compareTo(arrayDistance.get(minChildIdx)) > 0) {
                    arrayDistance.set(idx, arrayDistance.get(minChildIdx));
                    // arrayDistance.get(minChildIdx).position = idx;
					idx = minChildIdx;
				} else {
                    arrayDistance.set(idx, node);
                    // arrayDistance.get(idx).position = idx;
					break;
				}
			}
		}
    }



    // void percolateUp(int idx) {
    //     Distance node = arrayDistance[idx];
    //     int parentIdx = getParentIdx(idx);
    //     while(idx > 0 && node.compareTo(arrayDistance[parentIdx]) < 0){
    //         arrayDistance[idx] = arrayDistance[getParentIdx(idx)];
    //         // arrayDistance[getParentIdx(idx)].pos = idx;
    //         idx = getParentIdx(idx);
    //         parentIdx = getParentIdx(idx);
    //     }
    //     // arrayDistance[idx].pos = idx;
    //     arrayDistance[idx] = node;
    // }

    // void percolateDown(int index) {
    //     if (index > (size/2) && index < size) return;
    //     if (index >= size) return;

    //     if (arrayDistance[index] == null || (arrayDistance[getLeftIdx(index)] != null && arrayDistance[index].compareTo(arrayDistance[getLeftIdx(index)]) > 0) || (arrayDistance[getRightIdx(index)] != null && arrayDistance[index].compareTo(arrayDistance[getRightIdx(index)]) > 0)) {
    //         if (arrayDistance[getLeftIdx(index)] == null || (arrayDistance[getRightIdx(index)]!=null && arrayDistance[getRightIdx(index)].harga<arrayDistance[getLeftIdx(index)].harga)
    //         || (arrayDistance[getRightIdx(index)]!= null && arrayDistance[getRightIdx(index)].harga==arrayDistance[getLeftIdx(index)].harga
    //                 && arrayDistance[getRightIdx(index)].id<arrayDistance[getLeftIdx(index)].id)) {
    //                     Distance temp = arrayDistance[index];
    //                     arrayDistance[index] = arrayDistance[getRightIdx(index)];
    //                     arrayDistance[getRightIdx(index)] = temp;
    //                     percolateDown(getRightIdx(index));
    //         } else if (arrayDistance[getRightIdx(index)] == null || arrayDistance[getLeftIdx(index)] != null) {
    //             Distance temp = arrayDistance[index];
    //             arrayDistance[index] = arrayDistance[getLeftIdx(index)];
    //             arrayDistance[getLeftIdx(index)] = temp;
    //             percolateDown(getLeftIdx(index));
    //         } 
    //     }
    // }

    // https://stackoverflow.com/questions/15493056/min-heapify-method-min-heap-algorithm
    void minHeapify(int parentIndex) {     
        int left = getLeftIdx(parentIndex);
        int right = getRightIdx(parentIndex);
        int smallest = parentIndex;
        if(arrayDistance.size()>left && arrayDistance.get(left).compareTo(arrayDistance.get(parentIndex)) < 0)
        {
            smallest = left;
        }

        if(arrayDistance.size()>right && arrayDistance.get(right).compareTo(arrayDistance.get(parentIndex)) < 0)
        {
            smallest = right;
        }

        if(smallest != parentIndex)
        {
            swap(parentIndex, smallest);
            minHeapify(smallest);
        }
    }

    // Distance delete_element(int index) {
    //     arrayDistance[index] = arrayDistance[0];
    //     arrayDistance[index].harga -= 1;
        
    //     Distance res = arrayDistance[index];
    //     int current = index;
    //     while (current > 0 && arrayDistance[current].compareTo(arrayDistance[getParentIdx(current)]) < 0) {
    //         Distance temp = arrayDistance[getParentIdx(current)];
    //         arrayDistance[getParentIdx(current)] = arrayDistance[current];
    //         arrayDistance[current] = temp;
    //         current = getParentIdx(current);
    //     } 
        
    //     delete_min();
    //     return res;
    // }
}