# 📦 Smart Drop-Zone (Java GUI)

**Smart Drop-Zone** adalah aplikasi utilitas desktop ringan berbasis Java GUI yang dirancang untuk mempermudah dan mempercepat proses penyortiran serta pendistribusian file melalui mekanisme *drag-and-drop*. Aplikasi ini dirancang menggunakan arsitektur pemrograman *Super Basic* tanpa memerlukan instalasi *database* pihak ketiga (menggunakan metode baca-tulis *File I/O* murni).

---

## 🎥 Demonstrasi Aplikasi


---

## 📸 Tangkapan Layar (Screenshots)

**Halaman Drop Zone & Halaman Histori:**


---

## ✨ Fitur Utama
1. **Always-on-Top Floating Window**: Jendela aplikasi akan selalu berada di lapisan layar paling depan, memastikan proses *drag-and-drop* file tidak terganggu oleh aplikasi lain.
2. **In-and-Out Drag System**: Mendukung penangkapan file yang masuk ke dalam program, serta sensor ekspor untuk memindahkan (*Cut*) file menuju destinasi akhir di Windows Explorer.
3. **Smart History Tracker**: Secara otomatis mencatat riwayat lalu-lintas file (Waktu, Nama File, Lokasi Awal) ke dalam *local storage* menggunakan metode `File I/O`.
4. **Safety Limit**: Proteksi *error handling* yang menolak masuknya file berukuran lebih dari 2 GB untuk menjaga efisiensi *RAM*.

---

## 🚀 Cara Menjalankan Source Code (Untuk Developer)
1. *Clone repository* ini:
```bash
git clone https://github.com/username-kamu/Smart-Drop-Zone.git
```
2. Buka proyek menggunakan **NetBeans IDE** atau *editor* Java lainnya.
3. Pastikan *JDK* yang digunakan minimal adalah versi 8.
4. *Run* file pada direktori utama: `kelompok8.pengelolafile.SmartDropZone.java`

*(Ingin langsung memakai aplikasinya tanpa coding? Silakan unduh file `.exe` di tab **[Releases]**)*

---

## 👨‍💻 Tim Pengembang
Proyek Tugas Besar Praktikum Pemrograman Berorientasi Objek (PBO).
**Kelompok 8:**
* Muh. Ammar Samkhan
* Airil Nasbi
* La Ode Muhammad Fadli Malik
* M.W.Rafi'putra

---
*Jika proyek ini membantu memberikan inspirasi, jangan ragu untuk memberikan ⭐ (Star) pada repository ini!*
