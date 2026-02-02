import java.util.Scanner;
import com.datastruct.*;

/*
 * === TEST ASISTEN DATA STRUCTURE ===
 * Studi kasus : Internet Service Provider (ISP)
 * - CRUD pelanggan : Binary Search Tree
 * - Antrian CS     : Priority Queue (Heap Min)
 * - Rute surveyor  : Graph
 */

class Pelanggan {
    String kode;
    String nama;
    String paket;
    String alamat;
    int tagihan;

    Pelanggan(String kode, String nama, String paket, String alamat, int tagihan) {
        this.kode = kode;
        this.nama = nama;
        this.paket = paket;
        this.alamat = alamat;
        this.tagihan = tagihan;
    }

    @Override
    public String toString() {
        return kode + " | " + nama + " | " + paket + " | " + alamat + " | Rp" + tagihan;
    }
}

// ===== DATA REQUEST CS =====
class CSRequest {
    String kodePelanggan;
    String nama;
    int waktuKedatangan;
    int prioritas;
    int durasi;

    CSRequest(String kodePelanggan, String nama, int waktuKedatangan, int prioritas, int durasi) {
        this.kodePelanggan = kodePelanggan;
        this.nama = nama;
        this.waktuKedatangan = waktuKedatangan;
        this.prioritas = prioritas;
        this.durasi = durasi;
    }
}

public class App {

    // waktu CS terakhir selesai melayani
    static int waktuCS = 0;

    // ================= MENU =================

    private static void tampilMenuUtama() {
        System.out.println("\n=== MENU UTAMA ISP ===");
        System.out.println("1. Kelola Data Pelanggan (BST)");
        System.out.println("2. Antrian CS (Priority Queue)");
        System.out.println("3. Rute Surveyor (Graph)");
        System.out.println("0. Keluar");
        System.out.print("Pilih menu: ");
    }

    private static void tampilMenuCRUD() {
        System.out.println("\n--- MENU CRUD PELANGGAN ---");
        System.out.println("1. Tambah pelanggan");
        System.out.println("2. Update pelanggan");
        System.out.println("3. Hapus pelanggan");
        System.out.println("4. Cari pelanggan");
        System.out.println("5. Tampilkan semua (In-Order)");
        System.out.println("0. Kembali");
        System.out.print("Pilih: ");
    }

    private static void tampilMenuCS() {
        System.out.println("\n--- MENU ANTRIAN CS ---");
        System.out.println("1. Tambah ke antrian");
        System.out.println("2. Proses antrian");
        System.out.println("0. Kembali");
        System.out.print("Pilih: ");
    }

    // ================= UTIL =================

    private static Pelanggan inputPelanggan(Scanner sc, String kode) {
        System.out.print("Nama        : ");
        String nama = sc.nextLine();
        System.out.print("Paket       : ");
        String paket = sc.nextLine();
        System.out.print("Alamat      : ");
        String alamat = sc.nextLine();
        System.out.print("Tagihan (Rp): ");
        int tagihan = Integer.parseInt(sc.nextLine());
        return new Pelanggan(kode, nama, paket, alamat, tagihan);
    }

    private static void isiDataAwal(BinarySearchTree<String, Pelanggan> bst) {
        bst.insert("CUS001", new Pelanggan("CUS001", "Andi", "20 Mbps", "Jl. Merdeka 1", 250000));
        bst.insert("CUS002", new Pelanggan("CUS002", "Budi", "50 Mbps", "Jl. Merdeka 2", 350000));
        bst.insert("CUS003", new Pelanggan("CUS003", "Citra", "10 Mbps", "Jl. Anggrek 5", 200000));
        bst.insert("CUS004", new Pelanggan("CUS004", "Dewi", "100 Mbps", "Jl. Melati 7", 500000));
        bst.insert("CUS005", new Pelanggan("CUS005", "Eko", "30 Mbps", "Jl. Kenanga 3", 300000));
        bst.insert("CUS006", new Pelanggan("CUS006", "Fajar", "20 Mbps", "Jl. Mawar 10", 250000));
        bst.insert("CUS007", new Pelanggan("CUS007", "Gita", "75 Mbps", "Jl. Dahlia 8", 450000));
        bst.insert("CUS008", new Pelanggan("CUS008", "Hendra", "10 Mbps", "Jl. Flamboyan 2", 200000));
        bst.insert("CUS009", new Pelanggan("CUS009", "Intan", "50 Mbps", "Jl. Cempaka 6", 350000));
        bst.insert("CUS010", new Pelanggan("CUS010", "Joko", "100 Mbps", "Jl. Teratai 12", 500000));

    }

    /*
     * Key heap:
     * - waktu kedatangan sebagai prioritas utama
     * - nilai prioritas hanya pembanding jika waktu sama
     */
    private static Integer makeKey(int waktuDatang, int prioritas) {
        return waktuDatang * 100 + prioritas;
    }

    // ================= PROSES CS =================

    private static void prosesAntrian(Heap<Integer, CSRequest> pq) {

        if (pq.size() == 0) {
            System.out.println("Antrian kosong.");
            return;
        }

        System.out.printf("%-7s | %-6s | %-5s | %-9s | %-7s | %-10s%n",
                "Datang", "Tunggu", "Mulai", "Prioritas", "Kode", "Nama");

        while (pq.size() > 0) {
            CSRequest r = pq.removeFirstData();
            if (r == null) break;

            int mulai = Math.max(waktuCS, r.waktuKedatangan);
            int tunggu = mulai - r.waktuKedatangan;

            System.out.printf("%-7d | %-6d | %-5d | %-9d | %-7s | %-10s%n",
                    r.waktuKedatangan, tunggu, mulai,
                    r.prioritas, r.kodePelanggan, r.nama);

            waktuCS = mulai + r.durasi;
        }
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BinarySearchTree<String, Pelanggan> bst = new BinarySearchTree<>();
        isiDataAwal(bst);

        Heap<Integer, CSRequest> pq = new Heap<>(100, true);

        int pilih;
        do {
            tampilMenuUtama();
            pilih = Integer.parseInt(sc.nextLine());

            switch (pilih) {
                case 1: {
                    int c;
                    do {
                        tampilMenuCRUD();
                        c = Integer.parseInt(sc.nextLine());

                        switch (c) {
                            case 1:
                                System.out.print("Kode: ");
                                String kode = sc.nextLine();
                                if (bst.search(kode) != null) {
                                    System.out.println("Kode sudah ada.");
                                } else {
                                    bst.insert(kode, inputPelanggan(sc, kode));
                                }
                                break;

                            case 4:
                                System.out.print("Kode: ");
                                Pelanggan p = bst.search(sc.nextLine());
                                System.out.println(p == null ? "Tidak ditemukan" : p);
                                break;

                            case 5:
                                bst.inOrderPerLine();
                                break;
                        }
                    } while (c != 0);
                    break;
                }

                case 2: {
                    int c;
                    do {
                        tampilMenuCS();
                        c = Integer.parseInt(sc.nextLine());

                        switch (c) {
                            case 1:
                                System.out.print("Kode pelanggan: ");
                                String kode = sc.nextLine();
                                Pelanggan p = bst.search(kode);
                                if (p == null) {
                                    System.out.println("Pelanggan tidak ada.");
                                    break;
                                }

                                System.out.print("Waktu datang: ");
                                int datang = Integer.parseInt(sc.nextLine());
                                System.out.print("Prioritas: ");
                                int prior = Integer.parseInt(sc.nextLine());
                                System.out.print("Durasi: ");
                                int durasi = Integer.parseInt(sc.nextLine());

                                CSRequest r = new CSRequest(kode, p.nama, datang, prior, durasi);
                                pq.insert(makeKey(datang, prior), r);
                                break;

                            case 2:
                                prosesAntrian(pq);
                                break;
                        }
                    } while (c != 0);
                    break;
                }
            }
        } while (pilih != 0);

        sc.close();
    }
}
