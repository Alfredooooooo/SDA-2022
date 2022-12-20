import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

public class Lab7 {
    private static InputReader in;
    private static PrintWriter out;
    static Vertex[] arrVertex;
    static Vertex[] arrToBeVisited;
    static Long maxValue = Long.MAX_VALUE;

    // Referensi: https://www.softwaretestinghelp.com/dijkstras-algorithm-in-java/ -> Partially Inspired
    // Referensi: https://cp-algorithms.com/graph/dijkstra.html#proof -> For Understanding
    // Referensi: https://www.baeldung.com/java-dijkstra -> Highly Inspired

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt(), M = in.nextInt();
        arrVertex = new Vertex[N+1];
        for (int i = 1; i <= N; i++) {
            // TODO: Inisialisasi setiap benteng
            arrVertex[i] = new Vertex(i);
        }

        arrToBeVisited = new Vertex[M];
        for (int i = 0; i < M; i++) {
            int F = in.nextInt();
            // TODO: Tandai benteng F sebagai benteng diserang
            arrToBeVisited[i] = arrVertex[F];
            arrToBeVisited[i].weight_distance = 0;
        }

        int E = in.nextInt();
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(), B = in.nextInt(), W = in.nextInt();
            // TODO: Inisialisasi jalan berarah dari benteng A ke B dengan W musuh
            arrVertex[B].adjacency_list_with_map.put(arrVertex[A], (long) W);
        }

        algo_dijkstra(arrToBeVisited); // Precompute distance dari benteng diserang ke semua benteng lainnya

        int Q = in.nextInt();
        while (Q-- > 0) {
            int S = in.nextInt(), K = in.nextInt();
            // TODO: Implementasi query
            if (K <= arrVertex[S].weight_distance) {
                out.println("NO");
            } else {
                out.println("YES");
            }
            // Thus, O(1)
        }

        out.close();
    }

    static void algo_dijkstra(Vertex[] src_vertex) {
        Set<Vertex> visited = new HashSet<>();
        Set<Vertex> unvisited = new HashSet<>();
        for (Vertex vertex : src_vertex) {
            unvisited.add(vertex);
        }

        while (unvisited.size() > 0) {
            Vertex current_vertex = getMinimumVertex(unvisited);
            visited.add(current_vertex);
            unvisited.remove(current_vertex);
            // Copied from baeldung
            for (Entry<Vertex, Long> adjacencyPair: current_vertex.adjacency_list_with_map.entrySet()) {
                Vertex adjacentVertexInList = adjacencyPair.getKey();
                Long weightDistanceOfCurrentAdjacentVertex = adjacencyPair.getValue();
                if (!visited.contains(adjacentVertexInList)) {
                    findMinimalDistances(adjacentVertexInList, weightDistanceOfCurrentAdjacentVertex, current_vertex);
                    unvisited.add(adjacentVertexInList);
                }
            }
        }
    }

    // Preferably, priority queue tapi karena baeldung pake set, maka iterate dulu
    static Vertex getMinimumVertex(Set<Vertex> vertexSet) {
        Vertex minimumVertex = null;
        Long minimumVertexWeightDistance = maxValue;
        for (Vertex vertex : vertexSet) {
            Long currentIterationDistance = vertex.weight_distance;
            if (currentIterationDistance < minimumVertexWeightDistance) {
                minimumVertex = vertex;
                minimumVertexWeightDistance = currentIterationDistance; 
            }
        }
        return minimumVertex;
    }

    // Cari kalau ada jalan yang lebih pendek
    static void findMinimalDistances(Vertex nodeToBeEvaluated, Long weightDistanceToBeEvaluated, Vertex nodeSource) {
        if (nodeToBeEvaluated.weight_distance > nodeSource.weight_distance + weightDistanceToBeEvaluated) {
            nodeToBeEvaluated.weight_distance = nodeSource.weight_distance + weightDistanceToBeEvaluated;
            LinkedList<Vertex> shortestPath = new LinkedList<>(nodeSource.shortestPath);
            shortestPath.add(nodeSource);
            nodeToBeEvaluated.shortestPath = shortestPath;
        }
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

    static class Vertex implements Comparator<Vertex> {
        int id;
        long weight_distance = maxValue;
        Map<Vertex, Long> adjacency_list_with_map = new HashMap<>();
        // Baeldung uses linked list untuk mendeskripsikan shortest path yang dihitung dari awal
        List<Vertex> shortestPath = new LinkedList<>();
    
        public Vertex(int id) {
            this.id = id;
        }
    
        @Override
        public int compare(Vertex node1, Vertex node2) 
        { 
            if (node1.weight_distance < node2.weight_distance) 
                return -1; 
            if (node1.weight_distance > node2.weight_distance) 
                return 1; 
            return 0; 
        } 
    }
}
