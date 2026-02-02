import java.util.Scanner;
import com.datastruct.*;

/*
 * === TAMBAHAN UNTUK TEST ASISTEN DS ===
 * - Studi kasus: Internet Service Provider (ISP)
 * - CRUD pelanggan dengan BinarySearchTree<String, Pelanggan>
 * - Data awal 7 pelanggan
 * - Soal 3: Antrian CS pakai Heap<Integer, CSRequest> (priority queue, min-heap)
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

// data request untuk CS (Soal 3)
class CSRequest {
    String kodePelanggan;
    String nama;
    int waktuKedatangan;   // menit
    int prioritas;         // makin kecil makin didahulukan
    int durasi;            // menit pelayanan

    CSRequest(String kodePelanggan, String nama, int waktuKedatangan, int prioritas, int durasi) {
        this.kodePelanggan = kodePelanggan;
        this.nama = nama;
        this.waktuKedatangan = waktuKedatangan;
        this.prioritas = prioritas;
        this.durasi = durasi;
    }
}

public class App {

    // === GLOBAL UNTUK SOAL 3 ===
    // menyimpan waktu CS terakhir selesai melayani
    static int waktuCS = 0;

    // ===== MENU =====

    private static void tampilMenuUtama() {
        System.out.println("\n=== MENU UTAMA ISP ===");
        System.out.println("1. Kelola data pelanggan (CRUD) - BST");
        System.out.println("2. Antrian CS (Priority Queue) - Soal 3");
        System.out.println("3. (Soal 4) Rute surveyor - Graph");
        System.out.println("0. Keluar");
        System.out.print("Pilih menu: ");
    }

    private static void tampilMenuCRUD() {
        System.out.println("\n--- MENU CRUD PELANGGAN ---");
        System.out.println("1. Tambah pelanggan");
        System.out.println("2. Update data pelanggan");
        System.out.println("3. Hapus pelanggan");
        System.out.println("4. Cari pelanggan");
        System.out.println("5. Tampilkan semua pelanggan (in-order)");
        System.out.println("0. Kembali ke menu utama");
        System.out.print("Pilih menu CRUD: ");
    }

    private static void tampilMenuCS() {
        System.out.println("\n--- MENU ANTRIAN CS (PRIORITY QUEUE) ---");
        System.out.println("1. Tambah ke antrian CS");
        System.out.println("2. Proses antrian CS");
        System.out.println("0. Kembali ke menu utama");
        System.out.print("Pilih menu: ");
    }

    // ===== UTIL =====

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
        bst.insert("CUS001", new Pelanggan("CUS001", "Andi",  "Paket 20 Mbps",  "Jl. Merdeka 1", 250000));
        bst.insert("CUS002", new Pelanggan("CUS002", "Budi",  "Paket 50 Mbps",  "Jl. Merdeka 2", 350000));
        bst.insert("CUS003", new Pelanggan("CUS003", "Citra", "Paket 10 Mbps",  "Jl. Anggrek 5", 200000));
        bst.insert("CUS004", new Pelanggan("CUS004", "Dewi",  "Paket 100 Mbps", "Jl. Melati 7",  500000));
        bst.insert("CUS005", new Pelanggan("CUS005", "Eka",   "Paket 30 Mbps",  "Jl. Kenanga 9", 280000));
        bst.insert("CUS006", new Pelanggan("CUS006", "Fajar", "Paket 20 Mbps",  "Jl. Sakura 3",  250000));
        bst.insert("CUS007", new Pelanggan("CUS007", "Gina",  "Paket 50 Mbps",  "Jl. Mawar 4",   350000));
    }

    // key heap kombinasi prioritas & waktu kedatangan
    private static Integer makeKey(int prioritas, int waktuKedatangan) {
        return prioritas * 100000 + waktuKedatangan;
    }

    // proses antrian CS menggunakan heap
    private static void prosesAntrian(Heap<Integer, CSRequest> pq) {
        if (pq.size() == 0) {
            System.out.println("Antrian kosong.");
            return;
        }

        System.out.println();
        System.out.printf("%-7s | %-6s | %-5s | %-9s | %-7s | %-15s%n",
                "Datang", "Tunggu", "Mulai", "Prioritas", "Kode", "Nama");

        while (pq.size() > 0) {
            CSRequest r = pq.removeFirstData();
            if (r == null) break;

            int mulai = waktuCS;
            if (mulai < r.waktuKedatangan) mulai = r.waktuKedatangan;
            int tunggu = mulai - r.waktuKedatangan;

            System.out.printf("%-7d | %-6d | %-5d | %-9d | %-7s | %-15s%n",
                    r.waktuKedatangan, tunggu, mulai, r.prioritas, r.kodePelanggan, r.nama);

            waktuCS = mulai + r.durasi;
        }

    }
    

    // ===== MAIN =====

    public static void main(String[] args) {

        System.out.println("Good luck for the test!");

        Scanner sc = new Scanner(System.in);

        BinarySearchTree<String, Pelanggan> bst = new BinarySearchTree<>();
        isiDataAwal(bst);

        // Heap sebagai priority queue (min-heap) untuk CS
        Heap<Integer, CSRequest> pq = new Heap<>(200, true);

        int pilih;
        do {
            tampilMenuUtama();
            String input = sc.nextLine();
            if (input.isEmpty()) pilih = -1;
            else pilih = Integer.parseInt(input);

            switch (pilih) {
                case 1: {
                    int pilihCrud;
                    do {
                        tampilMenuCRUD();
                        String inCrud = sc.nextLine();
                        if (inCrud.isEmpty()) pilihCrud = -1;
                        else pilihCrud = Integer.parseInt(inCrud);

                        switch (pilihCrud) {
                            case 1: {
                                System.out.println("\n== Tambah Pelanggan ==");
                                System.out.print("Kode pelanggan : ");
                                String kode = sc.nextLine();

                                Pelanggan ada = bst.search(kode);
                                if (ada != null) {
                                    System.out.println("Kode sudah terdaftar, gunakan menu update.");
                                } else {
                                    Pelanggan p = inputPelanggan(sc, kode);
                                    bst.insert(kode, p);
                                    System.out.println("Pelanggan berhasil ditambahkan.");
                                }
                                break;
                            }
                            case 2: {
                                System.out.println("\n== Update Pelanggan ==");
                                System.out.print("Kode pelanggan : ");
                                String kode = sc.nextLine();

                                Pelanggan p = bst.search(kode);
                                if (p == null) {
                                    System.out.println("Pelanggan dengan kode tersebut tidak ditemukan.");
                                } else {
                                    System.out.println("Data lama : " + p);
                                    System.out.println("Masukkan data baru (kosong = tetap).");

                                    System.out.print("Nama baru (enter jika sama): ");
                                    String namaBaru = sc.nextLine();
                                    if (!namaBaru.isEmpty()) p.nama = namaBaru;

                                    System.out.print("Paket baru (enter jika sama): ");
                                    String paketBaru = sc.nextLine();
                                    if (!paketBaru.isEmpty()) p.paket = paketBaru;

                                    System.out.print("Alamat baru (enter jika sama): ");
                                    String alamatBaru = sc.nextLine();
                                    if (!alamatBaru.isEmpty()) p.alamat = alamatBaru;

                                    System.out.print("Tagihan baru (enter jika sama): ");
                                    String tagihanStr = sc.nextLine();
                                    if (!tagihanStr.isEmpty()) p.tagihan = Integer.parseInt(tagihanStr);

                                    System.out.println("Data pelanggan berhasil diupdate.");
                                }
                                break;
                            }
                            case 3: {
                                System.out.println("\n== Hapus Pelanggan ==");
                                System.out.print("Kode pelanggan : ");
                                String kode = sc.nextLine();

                                Pelanggan p = bst.search(kode);
                                if (p == null) {
                                    System.out.println("Pelanggan tidak ditemukan.");
                                } else {
                                    bst.delete(kode);
                                    System.out.println("Pelanggan dengan kode " + kode + " berhasil dihapus.");
                                }
                                break;
                            }
                            case 4: {
                                System.out.println("\n== Cari Pelanggan ==");
                                System.out.print("Kode pelanggan : ");
                                String kode = sc.nextLine();

                                Pelanggan p = bst.search(kode);
                                if (p == null) System.out.println("Pelanggan tidak ditemukan.");
                                else System.out.println("Data pelanggan : " + p);
                                break;
                            }
                            case 5: {
                                System.out.println("\n== Daftar Pelanggan (In-Order) ==");
                                bst.inOrderPerLine();
                                break;
                            }
                            case 0: {
                                System.out.println("Kembali ke menu utama.");
                                break;
                            }
                            default: {
                                System.out.println("Pilihan CRUD tidak dikenal.");
                            }
                        }
                    } while (pilihCrud != 0);
                    break;
                }

                case 2: {
                    int menuCS;
                    do {
                        tampilMenuCS();
                        String in = sc.nextLine();
                        if (in.isEmpty()) menuCS = -1;
                        else menuCS = Integer.parseInt(in);

                        switch (menuCS) {
                            case 1: {
                                System.out.println("\n== Tambah ke Antrian CS ==");
                                System.out.print("Kode pelanggan : ");
                                String kode = sc.nextLine();

                                Pelanggan p = bst.search(kode);
                                if (p == null) {
                                    System.out.println("Kode tidak ada di database, tambahkan dulu lewat menu CRUD.");
                                    break;
                                }

                                System.out.print("Waktu kedatangan (menit) : ");
                                int datang = Integer.parseInt(sc.nextLine());

                                System.out.print("Nilai prioritas (kecil lebih cepat) : ");
                                int prior = Integer.parseInt(sc.nextLine());

                                System.out.print("Durasi layanan (menit) : ");
                                int durasi = Integer.parseInt(sc.nextLine());

                                CSRequest req = new CSRequest(p.kode, p.nama, datang, prior, durasi);
                                Integer key = makeKey(prior, datang);

                                pq.insert(key, req);
                                System.out.println("Pelanggan masuk antrian CS.");
                                break;
                            }
                            case 2: {
                                prosesAntrian(pq);
                                break;
                            }
                            case 0: {
                                System.out.println("Kembali ke menu utama.");
                                break;
                            }
                            default: {
                                System.out.println("Pilihan tidak dikenal.");
                            }
                        }
                    } while (menuCS != 0);
                    break;
                }

                case 3: {
                    System.out.println("\n=== RUTE SURVEYOR ===");

                    Graph<String> g = new Graph<>(false);
                    g.inputGraph(sc);

                    System.out.print("\nPilih start (1 = manual, 2 = random): ");
                    int pilihStart = Integer.parseInt(sc.nextLine());

                    String start;
                    if (pilihStart == 1) {
                        System.out.print("Masukkan node awal: ");
                        start = sc.nextLine();
                    } else {
                        start = g.getRandomStart();
                        System.out.println("Start random: " + start);
                    }

                    g.dijkstra(start);
                    break;
                }


                case 0: {
                    System.out.println("Keluar program.");
                    break;
                }

                default: {
                    System.out.println("Pilihan tidak dikenal.");
                }
            }

        } while (pilih != 0);

        sc.close();
    }
}
