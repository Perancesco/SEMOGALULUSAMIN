package com.datastruct;
/* 
 * Struktur data Graph dengan bobot pada setiap edge
 * sources: https://www.lavivienpost.net/weighted-graph-as-adjacency-list/  
 * 
 */
import java.util.*;

class Edge<T> {
    private T neighbor; //connected vertex
    private int weight; //weight

    //Constructor, Time O(1) Space O(1)
    public Edge(T n, int w) {
        neighbor = n;
        weight = w;
    }

	public void setNeighbor(T neighbor) {
		this.neighbor = neighbor;
	}
    public T getNeighbor() {
        return neighbor;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public int getWeight() {
        return weight;
    }

    //Time O(1) Space O(1)
    @Override
    public String toString() {
        return "(" + neighbor + "," + weight + "km)";
    }
}

public class Graph<T> {
    //Map<T, LinkedList<Edge<T>>> adj;
    private Map<T, MyLinearList<Edge<T>>> adj;
    boolean directed;
    Random rand = new Random(); // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH

    //Constructor, Time O(1) Space O(1)
    public Graph(boolean type) {
        adj = new HashMap<>();
        directed = type; // false: undirected, true: directed
    }

    //Add edges including adding nodes, Time O(1) Space O(1)
    public void addEdge(T a, T b, int w) {
        if (w > 15) { // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
            System.out.println("❌ Jarak lebih dari 15 km, edge ditolak!"); // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
            return; // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
        } // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
        adj.putIfAbsent(a, new MyLinearList<>()); //add node
        adj.putIfAbsent(b, new MyLinearList<>()); //add node

        adj.get(a).pushQ(new Edge<>(b, w)); // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
        if (!directed) //undirected
            adj.get(b).pushQ(new Edge<>(a, w)); // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    }

    //DFS 
	public void DFS(T src) {
		
	}

	//BFS
	public void BFS(T src) { 
		 
	}
    // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    // ================= INPUT GRAPH VIA SCANNER =================
    public void inputGraph(Scanner sc) {

        System.out.print("Jumlah edge: ");
        int e = Integer.parseInt(sc.nextLine());

        System.out.print("Mode jarak (1 = manual, 2 = random): ");
        int mode = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < e; i++) {
            System.out.println("\nEdge ke-" + (i + 1));
            System.out.print("Node A: ");
            T a = (T) sc.nextLine();

            System.out.print("Node B: ");
            T b = (T) sc.nextLine();

            int w;
            if (mode == 1) {
                System.out.print("Jarak (1-15 km): ");
                w = Integer.parseInt(sc.nextLine());
            } else {
                w = rand.nextInt(15) + 1;
                System.out.println("Jarak random: " + w + " km");
            }

            addEdge(a, b, w);
        }
    }

    // ================= RANDOM START =================
    public T getRandomStart() {
        int idx = rand.nextInt(adj.size());
        return new ArrayList<>(adj.keySet()).get(idx);
    }

    // ================= DIJKSTRA =================
    public void dijkstra(T start) {

        Map<T, Integer> dist = new HashMap<>();
        Map<T, T> prev = new HashMap<>();

        for (T v : adj.keySet()) {
            dist.put(v, Integer.MAX_VALUE);
            prev.put(v, null);
        }

        dist.put(start, 0);

        PriorityQueue<T> pq =
                new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            T curr = pq.poll();

            Node<Edge<T>> node = adj.get(curr).head;
            while (node != null) {
                Edge<T> e = node.getData();
                T neigh = e.getNeighbor();

                int newDist = dist.get(curr) + e.getWeight();
                if (newDist < dist.get(neigh)) {
                    dist.put(neigh, newDist);
                    prev.put(neigh, curr);
                    pq.add(neigh);
                }
                node = node.getNext();
            }
        }

        System.out.println("\n=== HASIL JALUR TERPENDEK SURVEYOR ===");
        System.out.println("Mulai dari: " + start);

        for (T v : adj.keySet()) {
            System.out.print("Ke " + v + " (" + dist.get(v) + " km): ");
            printPath(prev, v);
        }
    }

    private void printPath(Map<T, T> prev, T v) {
        Stack<T> s = new Stack<>();
        while (v != null) {
            s.push(v);
            v = prev.get(v);
        }
        while (!s.isEmpty()) {
            System.out.print(s.pop());
            if (!s.isEmpty()) System.out.print(" -> ");
        }
        System.out.println();
    }
    
}
