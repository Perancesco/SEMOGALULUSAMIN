package com.datastruct;
/* * Struktur data Graph dengan bobot pada setiap edge
 */
import java.util.*;

class Edge<T> {
    private T neighbor; 
    private int weight; 

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

    @Override
    public String toString() {
        return "(" + neighbor + "," + weight + "km)";
    }
}

public class Graph<T> {
    private Map<T, MyLinearList<Edge<T>>> adj;
    boolean directed;
    Random rand = new Random(); 

    public Graph(boolean type) {
        adj = new HashMap<>();
        directed = type; 
    }

    public void addEdge(T a, T b, int w) {
        // Validasi jarak maksimal 15 km
        if (w > 15) { 
            System.out.println(" Jarak jgn lebih dari 15 km!"); 
            return; 
        } 
        adj.putIfAbsent(a, new MyLinearList<>()); 
        adj.putIfAbsent(b, new MyLinearList<>()); 

        adj.get(a).pushQ(new Edge<>(b, w)); 
        if (!directed) 
            adj.get(b).pushQ(new Edge<>(a, w)); 
    }

    // ================= INPUT GRAPH VIA SCANNER =================
    // Menambahkan parameter validNodes agar surveyor cuma bisa mengunjungi yang ada di antrian
    public void inputGraph(Scanner sc, MyArrayList<String> validNodes) {

        System.out.print("Jumlah edge: ");
        int e = Integer.parseInt(sc.nextLine());

        System.out.print("Mode jarak (1 = manual, 2 = random): ");
        int mode = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < e; i++) {
            System.out.println("\nEdge ke-" + (i + 1));
            
            T a, b;
            // Validasi Node A harus ada di daftar antrian CS
            while (true) {
                System.out.print("Node A (Kode Pelanggan): ");
                a = (T) sc.nextLine();
                boolean valid = false;
                for(int j=0; j<validNodes.size(); j++) {
                    if(validNodes.get(j).equals(a)) { valid = true; break; }
                }
                if(valid) break;
                System.out.println("Error: Kode " + a + " tidak ditemukan dalam antrian CS!");
            }

            // Validasi Node B harus ada di daftar antrian CS
            while (true) {
                System.out.print("Node B (Kode Pelanggan): ");
                b = (T) sc.nextLine();
                boolean valid = false;
                for(int j=0; j<validNodes.size(); j++) {
                    if(validNodes.get(j).equals(b)) { valid = true; break; }
                }
                if(valid) break;
                System.out.println("Error: Kode " + b + " tidak ditemukan dalam antrian CS!");
            }

            int w = 0;
            if (mode == 1) {
                // Perbaikan: Jika lebih dari 15km, harus mengulangi input (bukan skip)
                while (true) {
                    System.out.print("Jarak (1-15 km): ");
                    w = Integer.parseInt(sc.nextLine());
                    if (w <= 15) break;
                    System.out.println("Error: Jarak maksimal adalah 15 km. Ulangi!");
                }
            } else {
                w = rand.nextInt(15) + 1;
                System.out.println("Jarak random: " + w + " km");
            }

            addEdge(a, b, w);
        }
    }

    public T getRandomStart() {
        if (adj.isEmpty()) return null;
        int idx = rand.nextInt(adj.size());
        return new ArrayList<>(adj.keySet()).get(idx);
    }

    // ================= DIJKSTRA =================
    public void dijkstra(T start) {
        if (start == null) { System.out.println("Graph kosong!"); return; }
        Map<T, Integer> dist = new HashMap<>();
        Map<T, T> prev = new HashMap<>();

        for (T v : adj.keySet()) {
            dist.put(v, Integer.MAX_VALUE);
            prev.put(v, null);
        }

        dist.put(start, 0);

        PriorityQueue<T> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
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
            if (dist.get(v) == Integer.MAX_VALUE) {
                System.out.println("Ke " + v + ": Tidak terjangkau");
            } else {
                System.out.print("Ke " + v + " (" + dist.get(v) + " km): ");
                printPath(prev, v);
            }
        }
    }

    private void printPath(Map<T, T> prev, T v) {
        Stack<T> s = new Stack<>();
        T temp = v;
        while (temp != null) {
            s.push(temp);
            temp = prev.get(temp);
        }
        while (!s.isEmpty()) {
            System.out.print(s.pop());
            if (!s.isEmpty()) System.out.print(" -> ");
        }
        System.out.println();
    }
}