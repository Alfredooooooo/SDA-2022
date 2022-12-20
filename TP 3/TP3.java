import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

// Collaborators: Kevin Alexander, Muhammad Naufal Zaky Alsar
// Referensi: https://scele.cs.ui.ac.id/mod/resource/view.php?id=128135
// Referensi: https://stackoverflow.com/questions/18552964/finding-path-with-maximum-minimum-capacity-in-graph?noredirect=1&lq=1
// Referensi: https://stackoverflow.com/questions/873126/finding-the-path-with-the-maximum-minimal-weight
// Referensi: https://en.wikipedia.org/wiki/Widest_path_problem
// Referensi: https://www.geeksforgeeks.org/widest-path-problem-practical-application-of-dijkstras-algorithm/
// Referensi: https://drive.google.com/drive/folders/1jsMPzfmydPEY-wEAXjhhCTTVSxJDDDmU

public class TP3 {
    private static InputReader in;
    private static PrintWriter out;
    static Graph graph;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt(); // Banyak pos
        int M = in.nextInt(); // Banyak terowongan

        graph = new Graph(N);
        graph.addVertex(); // Untuk dummy VertexDistance (index-0)
        int doubleN = N*2;
        for (int i = 0; i < doubleN; i++) {
            graph.addVertex();
        } 

        for (int i = 0; i < M; i++) {
            int source = in.nextInt();
            int destination = in.nextInt();
            int weight_distance = in.nextInt();
            int weight_capacity = in.nextInt();
            graph.addEdge(source, destination, weight_distance, weight_capacity);
            graph.addEdge(source + N, N + destination, weight_distance, weight_capacity);
            graph.addEdge(source, destination + N, 0, 0);
            graph.addEdge(source + N, destination, 0, 0);
        }

        graph.setArray();

        int P = in.nextInt(); // Banyak kurcaci
        
        graph.setPosKurcaciBerada(P);
        for (int i = 0; i < P; i++) {
            graph.posKurcaciBerada[i] = in.nextInt();
        }

        // Query
        int Q = in.nextInt(); // Banyak query
        for (int i = 0; i<Q; i++) {
            String query = in.next();
            if (query.equals("KABUR")) {
                int F = in.nextInt(); // source 
                int E = in.nextInt(); // destination

                // TODO: Implementasi algoritma di sini
                long res = graph.handleKabur(F, E);
                out.println(res);
            } else if (query.equals("SIMULASI")) {
                int K = in.nextInt(); // Banyak pos keluar

                // TODO: Implementasi algoritma di sini
                graph.setDummyVertex(K);
                for (int j = 0; j < K; j++) {
                    graph.neighbours[0][j] = in.nextInt();
                    graph.weight_distances[0][j] = 0;
                }
                long res = graph.handleSimulasi();
                out.println(res);
            } else if (query.equals("SUPER")) {
                int V1 = in.nextInt(); // source
                int V2 = in.nextInt(); // first-destination
                int V3 = in.nextInt(); // end-destination

                // TODO: Implementasi algoritma di sini
                long res = graph.handleSuper(V1, V2, V3);
                out.println(res);
            }
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
    }
}

class Graph {
    int jumlahVertex;
    int[] posKurcaciBerada;
    ArrayList<ArrayList<VertexDistance>> tempDataDistance;
    ArrayList<ArrayList<VertexCapacity>> tempDataCapacity;
    int[][] neighbours;
    long[][] weight_distances;
    long[][] weight_capacities;

    public Graph(int jumlahVertex) {
        tempDataDistance = new ArrayList<ArrayList<VertexDistance>>();
        tempDataCapacity = new ArrayList<ArrayList<VertexCapacity>>();
        this.jumlahVertex = jumlahVertex;
        neighbours = new int[2*jumlahVertex + 1][];
        weight_distances = new long[2*jumlahVertex + 1][];
        weight_capacities = new long[2*jumlahVertex + 1][];
    }

    void addVertex() {
        tempDataDistance.add(new ArrayList<>());
        tempDataCapacity.add(new ArrayList<>());
    }

    void addEdge(int source, int destination, int weight_distance, int weight_capacity) {
        // Distance
        tempDataDistance.get(source).add(new VertexDistance(destination, weight_distance));
        tempDataDistance.get(destination).add(new VertexDistance(source, weight_distance));
        // Capacity
        tempDataCapacity.get(source).add(new VertexCapacity(destination, weight_capacity));
        tempDataCapacity.get(destination).add(new VertexCapacity(source, weight_capacity));
    }

    void setArray() {
        int doubleJumlahVertex = jumlahVertex * 2;
        for (int i = 1; i <= doubleJumlahVertex; i++) {
            ArrayList<VertexDistance> currDataDistance = tempDataDistance.get(i);
            ArrayList<VertexCapacity> currDataCapacity = tempDataCapacity.get(i);
            int size = currDataDistance.size();
            neighbours[i] = new int[size];
            weight_distances[i] = new long[size];
            weight_capacities[i] = new long[size];
            for (int j = 0; j < size; j++) {
                VertexDistance currVertexDistance = currDataDistance.get(j);
                VertexCapacity currVertexCapacity = currDataCapacity.get(j);
                neighbours[i][j] = currVertexDistance.destination;
                weight_distances[i][j] = currVertexDistance.weight_distance;
                weight_capacities[i][j] = currVertexCapacity.weight_capacity;
            }
        }
    }

    void setDummyVertex(int K) {
        neighbours[0] = new int[K];
        weight_distances[0] = new long[K];
    }

    public void setPosKurcaciBerada(int P) {
        posKurcaciBerada = new int[P];
    }

    // Reference: https://www.geeksforgeeks.org/widest-path-problem-practical-application-of-dijkstras-algorithm/
    long handleKabur(int F, int E) {

        /* Ide:
        1. Lakukan inisiasi array widest dengan setiap elemennya bernilai -infinity
        2. Membuat elemen source (F) pada array widest dengan weight infinity
        3. Melakukan insert pada maximum priority queue dengan elemen source F namun capacity 0
        4. Melakukan perulangan selama pq tidak kosong
            4.1. Melakukan extract pada pq
            4.2. Melakukan perulangan pada setiap tetangga dari vertex yang diextract
                4.2.1. Memastikan mencari max weight yang sedang dibawa dan tampung sebagai weight
                       Dengan cara: weight = Math.max(widest[currNeighbours[j]], Math.min(widest[currVertexIndex], currWeightCapacities[j]));
                       Dimana kita mencari maksimal dari widest capacity neighbour sekarang dan minimal dari widest capactiy vertex sekarang 
                       dan weight capacity pada neighbour sekarang (neighbour.weight_capacity)
                4.2.2. Melakukan update pada widest jika weight lebih besar dari widest
                4.2.3. Melakukan insert pada queue dengan capacity baru */

        Long[] widest = new Long[jumlahVertex + 1];

        for (int i = 0; i < jumlahVertex + 1; i++) {
            widest[i] = -(long) 1e18;
        }

        MaxHeap container = new MaxHeap();
        container.insert(new VertexCapacity(F, 0));
        widest[F] = (long) 1e18;

        while (container.size > 0) {
            VertexCapacity currVertex = container.delete_max();
            int currVertexIndex = currVertex.destination;
            // long currVertexWeight = currVertex.weight_capacity;

            int[] currNeighbours = neighbours[currVertexIndex];
            long[] currWeightCapacities = weight_capacities[currVertexIndex];

            // Iterate neighbours
            for (int j = 0; j < currWeightCapacities.length; j++) {

                /* Finding the widest capacity to the vertex
                using current_source vertex's widest capacity
                and its widest capacity so far */
                if (currNeighbours[j] > jumlahVertex) {
                    continue;
                }

                long weight = Math.max(widest[currNeighbours[j]], Math.min(widest[currVertexIndex], currWeightCapacities[j]));
                // System.out.println("Ini weight " + weight);

                // Relaxation of edge and adding into Priority Queue
                if (weight > widest[currNeighbours[j]]) {
                    // Updating bottle-neck distance
                    widest[currNeighbours[j]] = weight;

                    // Adding the relaxed edge in the priority queue
                    container.insert(new VertexCapacity(currNeighbours[j], weight));
                }
                }
            }

            return widest[E];
        }

    // From scele
    long handleSimulasi() {

/*         Ide: 
        1. Melakukan djikstra dengan source sumber mulai dari dummy vertex sehingga hanya 1x iterasi
        2. Dapatkan max shortestpath dari tiap pos kurcaci */

        // System.out.println("Ini jumlah vertex " + jumlahVertex);
        ArrayList<Long> currShortestPath = new ArrayList<>();
        for (int j = 0; j < jumlahVertex + 1; j++) {
            currShortestPath.add((long) 1e18);
        }

        MinHeap pq = new MinHeap();
        pq.insert(new VertexDistance(0, 0)); // Dummy Vertex as first destination
        
        while (pq.size > 0) {
            VertexDistance currVertex = pq.delete_min();
            int currVertexIndex = currVertex.destination;
            long currVertexWeight = currVertex.weight_distance;

            // Seandainya kalau sudah optimal / weight edge lebih besar dari memo maka terminate (karena dari memo udh lebih cepet)
            if (currShortestPath.get(currVertexIndex) <= currVertexWeight) continue;

            currShortestPath.set(currVertexIndex, currVertexWeight);
            int[] currNeighbours = neighbours[currVertexIndex];
            long[] currWeights = weight_distances[currVertexIndex];

            for (int j = 0; j < currWeights.length; j++) {

                // System.out.println("Ini j " + j);
                // System.out.println("Ini curr neighbour  " + currNeighbours[j]);
                if (currNeighbours[j] > jumlahVertex) {
                    continue;
                }

                // Seandainya kalau memonya lebih besar, maka masukin pq agar terupdate!
                if (currShortestPath.get(currNeighbours[j]) > currWeights[j] + currVertexWeight) {
                    // Akan selalu update current weight distance dengan weights[j] (weight node yg dikunjungi)
                    pq.insert(new VertexDistance(currNeighbours[j], currWeights[j] + currVertexWeight));
                }
            }
        }

        // Setelah semua shortest path terupdate, maka kita cek apakah posisi kurcaci berada di posisi mana
        long maxDistance = 0;
        for (int q = 0; q < posKurcaciBerada.length; q++) {
            maxDistance = Math.max(maxDistance, currShortestPath.get(posKurcaciBerada[q]));
        }

        return maxDistance;
    }

    long handleSuper(int V1, int V2, int V3) {

        /* Ide:
        1. Membuat semacam 2 graf dalam bentuk indexing, makanya menjadi 2n+1 dengan n = jumlah vertex dan + 1 untuk dummy vertex
        2. Melakukan Djikstra untuk V1 dengan edge 0 dan tidak 0, lakukan juga dengan edge 0 dan tidak 0 untuk V2
        3. Mencari shortest path dari V1 ke V2 dengan edge 0 lalu ditambahkan ke totaltime1 dan tidak 0 lalu ditambahkan ke totaltime2
        4. Mencari shortest path dari V2 ke V3 dengan edge 0 lalu ditambahkan ke totaltime2 dan tidak 0 lalu ditambahkan ke totaltime1
        5. Cari min dari totaltime1 dan totaltime2 */

        ArrayList<Long> currShortestPath = new ArrayList<>();
        int doubleJumlahVertex = jumlahVertex * 2;
        for (int j = 0; j < doubleJumlahVertex + 1; j++) {
            currShortestPath.add((long) 1e18);
        }

        MinHeap pq = new MinHeap();
        pq.insert(new VertexDistance(V1, 0));

        while (pq.size > 0) {
            VertexDistance currVertex = pq.delete_min();
            int currVertexIndex = currVertex.destination;
            long currVertexWeight = currVertex.weight_distance;

            // Seandainya kalau sudah optimal / weight edge lebih besar dari memo maka terminate (karena dari memo udh lebih cepet)
            if (currShortestPath.get(currVertexIndex) <= currVertexWeight) continue;

            currShortestPath.set(currVertexIndex, currVertexWeight);
            int[] currNeighbours = neighbours[currVertexIndex];
            long[] currWeights = weight_distances[currVertexIndex];

            for (int j = 0; j < currWeights.length; j++) {
                if (currVertexIndex > jumlahVertex) {
                    if (currNeighbours[j] <= jumlahVertex) {
                        continue;
                    }
                } 
                // Seandainya kalau memonya lebih besar, maka masukin pq agar terupdate!
                if (currShortestPath.get(currNeighbours[j]) > currWeights[j] + currVertexWeight) {
                    // Akan selalu update current weight distance dengan weights[j] (weight node yg dikunjungi)
                    pq.insert(new VertexDistance(currNeighbours[j], currWeights[j] + currVertexWeight));
                }
            }
        }

        long totalTime1 = 0;
        long totalTime2 = 0;
        long totalTime3 = 0;
        // System.out.println("jumlahVertex skrg " + jumlahVertex);
        // System.out.println("Jumlah V2 " + V2);

        totalTime1 += currShortestPath.get(V2 + jumlahVertex);
        totalTime2 += currShortestPath.get(V2);
        totalTime3 += currShortestPath.get(V2);

        currShortestPath = new ArrayList<>();

        for (int j = 0; j < doubleJumlahVertex + 1; j++) {
            currShortestPath.add((long) 1e18);
        }

        pq = new MinHeap();
        pq.insert(new VertexDistance(V2, 0));

        while (pq.size > 0) {
            VertexDistance currVertex = pq.delete_min();
            int currVertexIndex = currVertex.destination;
            long currVertexWeight = currVertex.weight_distance;

            // Seandainya kalau sudah optimal / weight edge lebih besar dari memo maka terminate (karena dari memo udh lebih cepet)
            if (currShortestPath.get(currVertexIndex) <= currVertexWeight) continue;

            currShortestPath.set(currVertexIndex, currVertexWeight);
            int[] currNeighbours = neighbours[currVertexIndex];
            long[] currWeights = weight_distances[currVertexIndex];

            for (int j = 0; j < currWeights.length; j++) {
                if (currVertexIndex > jumlahVertex) {
                    if (currNeighbours[j] <= jumlahVertex) {
                        continue;
                    }
                }
                // Seandainya kalau memonya lebih besar, maka masukin pq agar terupdate!
                if (currShortestPath.get(currNeighbours[j]) > currWeights[j] + currVertexWeight) {
                    // Akan selalu update current weight distance dengan weights[j] (weight node yg dikunjungi)
                    pq.insert(new VertexDistance(currNeighbours[j], currWeights[j] + currVertexWeight));
                }
                
            }
        }
        
        // System.out.println("What is total time: " + totalTime);
        totalTime1 += currShortestPath.get(V3);
        totalTime2 += currShortestPath.get(V3 + jumlahVertex);
        totalTime3 += currShortestPath.get(V3);
;
        long totalTime = Math.min(totalTime1, totalTime2);
        totalTime = Math.min(totalTime, totalTime3);

        return totalTime;
    }
}

class VertexDistance implements Comparable<VertexDistance> {
    int destination;
    long weight_distance;

    public VertexDistance(int destination, long weight_distance) {
        this.destination = destination;
        this.weight_distance = weight_distance;
    }

    @Override
    public int compareTo(VertexDistance o) {
        // TODO Auto-generated method stub
        if (weight_distance < o.weight_distance) return -1;
        if (weight_distance > o.weight_distance) return 1;
        return 0;
    }
}

class VertexCapacity implements Comparable<VertexCapacity> {
    int destination;
    long weight_capacity;

    public VertexCapacity(int destination, long weight_capacity) {
        this.destination = destination;
        this.weight_capacity = weight_capacity;
    }

    @Override
    public int compareTo(VertexCapacity o) {
        // TODO Auto-generated method stub
        if (weight_capacity < o.weight_capacity) return -1;
        if (weight_capacity > o.weight_capacity) return 1;
        return 0;
    }
}


class MinHeap {
    ArrayList<VertexDistance> arrayVertexDistance;
    int size = 0;

    public MinHeap() {
        arrayVertexDistance = new ArrayList<VertexDistance>();
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

    boolean hasParent(int index) {
        return getParentIdx(index) >= 0;
    }

    void swap(int fpos, int spos) {
        VertexDistance tmp = arrayVertexDistance.get(fpos);
        arrayVertexDistance.set(fpos, arrayVertexDistance.get(spos));
        arrayVertexDistance.set(spos, tmp);
    }

    VertexDistance leftChild(int parentIndex) {
        return arrayVertexDistance.get(getLeftIdx(parentIndex));
    }
    
    VertexDistance rightChild(int parentIndex) {
        return arrayVertexDistance.get(getRightIdx(parentIndex));
    }
    
    VertexDistance parent(int childIndex) {
        return arrayVertexDistance.get(getParentIdx(childIndex));
    }

    void insert(VertexDistance newVertexDistance) {
        size++;
        arrayVertexDistance.add(newVertexDistance);
        heapifyUp(arrayVertexDistance.size() - 1);
    }

    public VertexDistance peek() {
        if (arrayVertexDistance.isEmpty()) return null;
        return arrayVertexDistance.get(0);
    }

    // https://www.digitalocean.com/community/tutorials/min-heap-binary-tree
    VertexDistance delete_min() {
        size--;
        VertexDistance removedObject = peek();

		if (arrayVertexDistance.size() == 1) arrayVertexDistance.clear();
		else {
            arrayVertexDistance.set(0, arrayVertexDistance.get(arrayVertexDistance.size()- 1));
            arrayVertexDistance.remove(arrayVertexDistance.size() - 1);
			heapifyDown(0);
		}

		return removedObject;
    }

    void heapifyUp(int idx) {
        VertexDistance node = arrayVertexDistance.get(idx);
        int parentIdx = getParentIdx(idx);
        while(idx > 0 && node.compareTo(arrayVertexDistance.get(parentIdx)) < 0) {
            arrayVertexDistance.set(idx, arrayVertexDistance.get(parentIdx));
            // arrayVertexDistance.get(parentIdx).position = idx;
            idx = parentIdx;
            parentIdx = getParentIdx(idx);
        }
        // arrayVertexDistance.get(idx).position = idx;
        arrayVertexDistance.set(idx, node);
    }

    void heapifyDown(int idx) {
        VertexDistance node = arrayVertexDistance.get(idx);
		int heapSize = arrayVertexDistance.size();

		while (true) {
			int leftIdx = getLeftIdx(idx);
			if (leftIdx >= heapSize) {
                arrayVertexDistance.set(idx, node);
                // arrayVertexDistance.get(idx).position = idx;
				break;
			} else {
				int minChildIdx = leftIdx;
				int rightIdx = getRightIdx(idx);
				if (rightIdx < heapSize && arrayVertexDistance.get(rightIdx).compareTo(arrayVertexDistance.get(leftIdx)) < 0) minChildIdx = rightIdx;

				if (node.compareTo(arrayVertexDistance.get(minChildIdx)) > 0) {
                    arrayVertexDistance.set(idx, arrayVertexDistance.get(minChildIdx));
                    // arrayVertexDistance.get(minChildIdx).position = idx;
					idx = minChildIdx;
				} else {
                    arrayVertexDistance.set(idx, node);
                    // arrayVertexDistance.get(idx).position = idx;
					break;
				}
			}
		}
    }

    // https://stackoverflow.com/questions/15493056/min-heapify-method-min-heap-algorithm
    void minHeapify(int parentIndex) {     
        int left = getLeftIdx(parentIndex);
        int right = getRightIdx(parentIndex);
        int smallest = parentIndex;
        if(arrayVertexDistance.size()>left && arrayVertexDistance.get(left).compareTo(arrayVertexDistance.get(parentIndex)) < 0)
        {
            smallest = left;
        }

        if(arrayVertexDistance.size()>right && arrayVertexDistance.get(right).compareTo(arrayVertexDistance.get(parentIndex)) < 0)
        {
            smallest = right;
        }

        if(smallest != parentIndex)
        {
            swap(parentIndex, smallest);
            minHeapify(smallest);
        }
    }
}

// https://www.digitalocean.com/community/tutorials/max-heap-java
class MaxHeap {
    ArrayList<VertexCapacity> arrayVertexCapacity;
    int size = 0;

    public MaxHeap() {
        arrayVertexCapacity = new ArrayList<VertexCapacity>();
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

    void swap(int fpos, int spos) {
        VertexCapacity tmp = arrayVertexCapacity.get(fpos);
        arrayVertexCapacity.set(fpos, arrayVertexCapacity.get(spos));
        arrayVertexCapacity.set(spos, tmp);
    }

    void heapifyUp(int idx) {
        VertexCapacity node = arrayVertexCapacity.get(idx);
        int parentIdx = getParentIdx(idx);
        while(idx > 0 && node.compareTo(arrayVertexCapacity.get(parentIdx)) > 0){
            arrayVertexCapacity.set(idx, arrayVertexCapacity.get(parentIdx));
            // arrayVertexCapacity.get(parentIdx).position = idx;
            idx = parentIdx;
            parentIdx = getParentIdx(idx);
        }
        // arrayVertexCapacity.get(idx).position = idx;
        arrayVertexCapacity.set(idx, node);
    }

    void downHeapify(int idx) {
        VertexCapacity node = arrayVertexCapacity.get(idx);
		int heapSize = arrayVertexCapacity.size();

		while (true) {
			int leftIdx = getLeftIdx(idx);
			if (leftIdx >= heapSize) {
                arrayVertexCapacity.set(idx, node);
                // arrayVertexCapacity.get(idx).position = idx;
				break;
			} else {
				int minChildIdx = leftIdx;
				int rightIdx = getRightIdx(idx);
				if (rightIdx < heapSize && arrayVertexCapacity.get(rightIdx).compareTo(arrayVertexCapacity.get(leftIdx)) > 0)
					minChildIdx = rightIdx;

				if (node.compareTo(arrayVertexCapacity.get(minChildIdx)) < 0) {
                    arrayVertexCapacity.set(idx, arrayVertexCapacity.get(minChildIdx));
                    // arrayVertexCapacity.get(minChildIdx).position = idx;
					idx = minChildIdx;
				} else {
                    arrayVertexCapacity.set(idx, node);
                    // arrayVertexCapacity.get(idx).position = idx;
					break;
				}
			}
		}
    }

    void insert(VertexCapacity newVertexCapacity) {
        size++;
        arrayVertexCapacity.add(newVertexCapacity);
        // System.out.println("Ini insert heap size " + (size-1));
        heapifyUp(arrayVertexCapacity.size()-1);
    }

    VertexCapacity peek() {
        if (arrayVertexCapacity.isEmpty()) return null;
        return arrayVertexCapacity.get(0);
    }

    VertexCapacity delete_max() {
        size--;
        VertexCapacity removedObject = peek();

		if (arrayVertexCapacity.size() == 1) arrayVertexCapacity.clear();
		else {
            arrayVertexCapacity.set(0, arrayVertexCapacity.get(arrayVertexCapacity.size()-1));
            arrayVertexCapacity.remove(arrayVertexCapacity.size()-1);
			downHeapify(0);
		}

		return removedObject;
    }
}