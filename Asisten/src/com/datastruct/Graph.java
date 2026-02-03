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
    // Inisialisasi random untuk penentuan secara acak
    Random rand = new Random(); // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH

    //Constructor, Time O(1) Space O(1)
    public Graph(boolean type) {
        adj = new HashMap<>();
        directed = type; // false: undirected, true: directed
    }

    //Add edges including adding nodes, Time O(1) Space O(1)
    public void addEdge(T a, T b, int w) {
        // Validasi jarak maksimal 15 km
        if (w > 15) { // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
            System.out.println(" Jarak jgn lebih dari 15 km!"); // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
            return; // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH
        } 
        adj.putIfAbsent(a, new MyLinearList<>()); //add node
        adj.putIfAbsent(b, new MyLinearList<>()); //add node

        adj.get(a).pushQ(new Edge<>(b, w)); //add(edge1); //add edge // INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH dikit
        if (!directed) //undirected
            adj.get(b).pushQ(new Edge<>(a, w)); 
    }
    //Print graph as hashmap, Time O(V+E), Space O(1)
	public void printGraph() {
		for (T key: adj.keySet()) {
			//System.out.println(key.toString() + " : " + adj.get(key).toString());
            System.out.print(key.toString() + " : ");
			MyLinearList<Edge<T>> thelist = adj.get(key);
			Node<Edge<T>> curr = thelist.head;
			while(curr != null) {
				System.out.print(curr.getData());
				curr = curr.getNext();
			}
			System.out.println();
		}
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
    // SEMUA KEBAWAH INI DIUBAHHHHHHHHHHHHHHHHHHHHHHHHHHHH

    // ================= INPUT GRAPH VIA SCANNER =================
    // User mengisi sendiri edge dari masing masing node supaya dapat bisa beneran diperlakukan di dunia nyata
    // awalnya saya kepikiran untuk menggunakan koordinat tapi dikarenakan koordinat belum pernah diajarkan jadi saya pake cara
    // biasa saja yaitu edge ke edge menggunakan add edge (sesuai rpaktikum)
    // surveyor cuma bisa mengunjungi yang ada di antrian CS Case 2 (NOmor 3)
    public void inputGraph(Scanner sc, MyArrayList<String> validNodes) {

        System.out.print("Jumlah edge: "); // Edge = rute angar pelanggan berisfat undirected jadi bisa bolak-balik
        int e = Integer.parseInt(sc.nextLine());

        System.out.print("Mode jarak (1 = manual, 2 = random): ");
        int mode = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < e; i++) {
            System.out.println("\nEdge ke-" + (i + 1));
            
            T a, b;
            // untuk valalidasi Node A harus ada di daftar antrian CS
            while (true) {
                System.out.print("Node A (Kode Pelanggan seperti CUS001, CUS002, DLL, harus yang ada di antrian CS): ");
                a = (T) sc.nextLine(); //input node A
                boolean valid = false;
                for(int j=0; j<validNodes.size(); j++) {
                    if(validNodes.get(j).equals(a)) { valid = true; break; } // pengecekan validiats nya apakah ada di antrian CS atau tdk
                }
                if(valid) break;
                System.out.println("Error: Kode " + a + " tidak ditemukan dalam antrian CS!"); //kalo ga ada di antrain cs
            }

            // untuk validasi Node B harus ada di daftar antrian CS
            while (true) {
                System.out.print("Node B (Kode Pelanggan seperti CUS001, CUS002, DLL, harus yang ada di antrian CS): ");
                b = (T) sc.nextLine(); // input node B
                boolean valid = false;
                for(int j=0; j<validNodes.size(); j++) {
                    if(validNodes.get(j).equals(b)) { valid = true; break; }
                }
                if(valid) break;
                System.out.println("Error: Kode " + b + " tidak ditemukan dalam antrian CS!"); //kalo ga ada di antrain cs
            }

            int w = 0;
            if (mode == 1) {
                // INPUT JARAK HARUS mentok 15 km kalo, ga ya input ulang
                while (true) {
                    System.out.print("Jarak (1-15 km): ");
                    w = Integer.parseInt(sc.nextLine());
                    if (w <= 15) break; // kalo misalnya si user input jarak udah bener
                    System.out.println("Error: Jarak maksimal adalah 15 km. Ulangi!"); //kalo misalnya si user input jarak lebih dari 15 km
                }
            } else {
                w = rand.nextInt(15) + 1;
                System.out.println("Jarak random: " + w + " km");
            }

            addEdge(a, b, w);
        }
    }
    // kalo user pilihnya startnya secara random
    public T getRandomStart() {
        if (adj.isEmpty()) return null;
        int idx = rand.nextInt(adj.size());
        int i = 0;
        for (T key : adj.keySet()) {
            if (i == idx) return key;
            i++;
        }
        return null;
    }

    // ================= DIJKSTRA =================
    public void dijkstra(T start) {
        if (start == null) { System.out.println("Graph kosong!"); return; } 
        // pake hashmap buat nyimpen jarak terpendek (pernah diimplementasi di praktikum jadi saya anggap boleh)
        Map<T, Integer> dist = new HashMap<>(); 
        Map<T, T> prev = new HashMap<>();

        for (T v : adj.keySet()) {
            dist.put(v, Integer.MAX_VALUE);
            prev.put(v, null);
        }

        dist.put(start, 0);
        MyArrayList<T> queue = new MyArrayList<>(100);
        queue.add(start);
        // selama queunya ga empty bakal bisa dijalanin
        while (!queue.isEmpty()) {
            T curr = null;
            int minVal = Integer.MAX_VALUE;
            int minIdx = -1;
            // cari node dengan jarak terpendek di queue
            for (int i = 0; i < queue.size(); i++) {
                T node = queue.get(i);
                int d = dist.get(node);
                if (d < minVal) { // kalo jarak ke node itu lebih kecil dari minVal
                    minVal = d;
                    curr = node;
                    minIdx = i;
                }
            }

            // Hapus node yang terpilih dari queue menggunakan method remove(index) dari MyArrayList
            if (minIdx != -1) {
                queue.remove(minIdx);
            } else {
                break;
            }
            // periksa tetangga dari node saat ini
            Node<Edge<T>> node = adj.get(curr).head;
            while (node != null) { // iterasi semua tetangga
                Edge<T> e = node.getData();
                T neigh = e.getNeighbor();

                int newDist = dist.get(curr) + e.getWeight(); // jarak baru
                if (newDist < dist.get(neigh)) {
                    dist.put(neigh, newDist); 
                    prev.put(neigh, curr);
                    
                    // Masukkan ke queue (MyArrayList)
                    queue.add(neigh);
                }
                node = node.getNext(); // next tetangga
            }
        }

        System.out.println("\n=== HASIL JALUR TERPENDEK SURVEYOR ===");
        System.out.println("Mulai dari: " + start); // titik awal ke semua titik lainya 

        for (T v : adj.keySet()) {
            if (dist.get(v) == Integer.MAX_VALUE) {
                System.out.println("Ke " + v + ": Tidak terjangkau"); // kalo ga ada jalur ke titik tersebut 
            } else {
                System.out.print("Ke " + v + " (" + dist.get(v) + " km): ");
                printPath(prev, v);
                System.out.println();
            }
        }
    }

// Nampilin path dari start ke vertex lain pake car reksurif
    private void printPath(Map<T, T> prev, T v) {
        // Base case: jika v adalah start node (prev-nya null)
        if (prev.get(v) == null) {
            System.out.print(v);
            return;
        }

        // Panggil rekursi untuk print node sebelumnya dulu
        printPath(prev, prev.get(v));

        // Print node saat ini setelah kembali dari rekursi
        System.out.print(" -> " + v);
    }
}