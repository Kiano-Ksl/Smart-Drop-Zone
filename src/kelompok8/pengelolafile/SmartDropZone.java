package kelompok8.pengelolafile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class SmartDropZone extends JFrame {
    
    private DefaultListModel<String> modelDaftarFile;
    private JList<String> listFileTersimpan;
    private JCheckBox cekSelaluDiDepan;
    
    private CardLayout tumpukanHalaman;
    private JPanel panelUtama;
    
    private ArrayList<File> fileSiapPindah = new ArrayList<>();
    
    public SmartDropZone() {
        setTitle("Smart Drop-Zone - Kelompok 8");
        setSize(480, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());
        
        // Checkbox Always on Top
        cekSelaluDiDepan = new JCheckBox("Selalu Tampil di Depan Layar", true);
        cekSelaluDiDepan.setHorizontalAlignment(SwingConstants.CENTER);
        cekSelaluDiDepan.setFont(new Font("Arial", Font.BOLD, 12));
        cekSelaluDiDepan.addActionListener(e -> setAlwaysOnTop(cekSelaluDiDepan.isSelected()));
        add(cekSelaluDiDepan, BorderLayout.NORTH);
        
        tumpukanHalaman = new CardLayout();
        panelUtama = new JPanel(tumpukanHalaman);
    
        // HALAMAN 1: AREA DROP ZONE
        JPanel halamanDropZone = new JPanel(new BorderLayout(10, 10));
        halamanDropZone.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        
        modelDaftarFile = new DefaultListModel<>();
        listFileTersimpan = new JList<>(modelDaftarFile);
        listFileTersimpan.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        listFileTersimpan.setDragEnabled(true);
        listFileTersimpan.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        // Fungsi Drag & Drop (Masuk & Keluar)
        listFileTersimpan.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) return false;
                try {
                    List<File> daftarFile = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File fileMasuk : daftarFile) {
                        if (fileMasuk.length() > 2000000000L) {
                            JOptionPane.showMessageDialog(SmartDropZone.this, "Gagal! File melebihi 2 GB: " + fileMasuk.getName());
                        } else {
                            fileSiapPindah.add(fileMasuk);
                            modelDaftarFile.addElement("📁 " + fileMasuk.getName()); 
                        }
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public int getSourceActions(JComponent c) {
                SmartDropZone.this.setAlwaysOnTop(false); 
                return TransferHandler.MOVE; 
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                return new Transferable() {
                    @Override
                    public DataFlavor[] getTransferDataFlavors() {
                        return new DataFlavor[]{DataFlavor.javaFileListFlavor};
                    }
                    @Override
                    public boolean isDataFlavorSupported(DataFlavor flavor) {
                        return DataFlavor.javaFileListFlavor.equals(flavor);
                    }
                    @Override
                    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                        List<File> fileTerpilih = new ArrayList<>();
                        for (int index : listFileTersimpan.getSelectedIndices()) {
                            fileTerpilih.add(fileSiapPindah.get(index));
                        }
                        return fileTerpilih;
                    }
                };
            }

            @Override
            protected void exportDone(JComponent source, Transferable data, int action) {
                SmartDropZone.this.setAlwaysOnTop(cekSelaluDiDepan.isSelected());
                if (action == TransferHandler.MOVE) {
                    int[] terpilih = listFileTersimpan.getSelectedIndices();
                    for (int i = terpilih.length - 1; i >= 0; i--) {
                        int index = terpilih[i];
                        // Kirim objek File untuk mendapatkan lokasi awalnya
                        simpanKeRiwayat(fileSiapPindah.get(index));
                        fileSiapPindah.remove(index);
                        modelDaftarFile.remove(index);
                    }
                }
            }
        });
        
        JScrollPane scrollList = new JScrollPane(listFileTersimpan);
        scrollList.setBorder(BorderFactory.createTitledBorder("Area Drop Zone (Tarik File Masuk & Tarik Keluar di Sini)"));
        halamanDropZone.add(scrollList, BorderLayout.CENTER);
        
        JButton tombolLihatHistori = new JButton("Lihat Histori Pemindahan");
        tombolLihatHistori.setBackground(new Color(70, 130, 180)); 
        tombolLihatHistori.setForeground(Color.WHITE);
        tombolLihatHistori.setFont(new Font("Arial", Font.BOLD, 14));
        tombolLihatHistori.setPreferredSize(new Dimension(100, 40));
        halamanDropZone.add(tombolLihatHistori, BorderLayout.SOUTH);

        // HALAMAN 2: AREA HISTORI
        JPanel halamanHistori = new JPanel(new BorderLayout(10, 10));
        halamanHistori.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextArea teksHistori = new JTextArea();
        teksHistori.setEditable(false);
        teksHistori.setFont(new Font("Consolas", Font.PLAIN, 12));
        teksHistori.setMargin(new Insets(5, 5, 5, 5));
        
        JScrollPane scrollHistori = new JScrollPane(teksHistori);
        scrollHistori.setBorder(BorderFactory.createTitledBorder("Catatan Riwayat (Waktu | Nama File | Lokasi Awal):"));
        halamanHistori.add(scrollHistori, BorderLayout.CENTER);
        
        // Panel untuk 2 Tombol di bawah Halaman Histori
        JPanel panelTombolHistori = new JPanel(new GridLayout(1, 2, 10, 0));
        
        JButton tombolKembali = new JButton("Kembali");
        tombolKembali.setBackground(new Color(105, 105, 105)); 
        tombolKembali.setForeground(Color.WHITE);
        tombolKembali.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton tombolHapusHistori = new JButton("Hapus Histori");
        tombolHapusHistori.setBackground(new Color(220, 20, 60)); 
        tombolHapusHistori.setForeground(Color.WHITE);
        tombolHapusHistori.setFont(new Font("Arial", Font.BOLD, 14));
        
        panelTombolHistori.add(tombolKembali);
        panelTombolHistori.add(tombolHapusHistori);
        panelTombolHistori.setPreferredSize(new Dimension(100, 40));
        
        halamanHistori.add(panelTombolHistori, BorderLayout.SOUTH);
        
        // MEMASUKKAN KEDUA HALAMAN & AKSI TOMBOL
        panelUtama.add(halamanDropZone, "HALAMAN_DROP");
        panelUtama.add(halamanHistori, "HALAMAN_HISTORI");
        add(panelUtama, BorderLayout.CENTER);
        
        tombolLihatHistori.addActionListener(e -> {
            bacaHistoriDariFile(teksHistori);
            tumpukanHalaman.show(panelUtama, "HALAMAN_HISTORI");
        });
        
        tombolKembali.addActionListener(e -> {
            tumpukanHalaman.show(panelUtama, "HALAMAN_DROP");
        });
        
        // Aksi Tombol Hapus dengan Konfirmasi (Alert)
        tombolHapusHistori.addActionListener(e -> {
            // Memunculkan kotak dialog peringatan Ya/Tidak
            int pilihan = JOptionPane.showConfirmDialog(
                    SmartDropZone.this, 
                    "Apakah anda yakin ingin menghapus histori?", 
                    "Konfirmasi Hapus", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE
            );
            
            // Jika user menekan tombol "Yes"
            if (pilihan == JOptionPane.YES_OPTION) {
                File fileHistori = new File("riwayat_pemindahan.txt");
                if (fileHistori.exists()) {
                    fileHistori.delete(); 
                }
                bacaHistoriDariFile(teksHistori); 
            }
        });
    }

    // FUNGSI MENYIMPAN KE TXT (Format Baru)
    private void simpanKeRiwayat(File fileAwal) {
        try {
            FileWriter penulis = new FileWriter("riwayat_pemindahan.txt", true);
            PrintWriter cetakTeks = new PrintWriter(penulis);
            String waktu = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            
            // Format: [Waktu] Nama File | Lokasi Awal: ...
            cetakTeks.println("[" + waktu + "] " + fileAwal.getName());
            cetakTeks.println("   Lokasi Awal: " + fileAwal.getAbsolutePath());
            cetakTeks.println("--------------------------------------------------");
            
            cetakTeks.close();
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat menyimpan riwayat.");
        }
    }
    
    // FUNGSI MEMBACA DARI TXT
    private void bacaHistoriDariFile(JTextArea areaTeks) {
        areaTeks.setText(""); 
        try {
            File fileHistori = new File("riwayat_pemindahan.txt");
            if (fileHistori.exists()) {
                Scanner pembaca = new Scanner(fileHistori);
                while (pembaca.hasNextLine()) {
                    areaTeks.append(pembaca.nextLine() + "\n");
                }
                pembaca.close();
            } else {
                areaTeks.setText("Belum ada histori pemindahan file.");
            }
        } catch (Exception e) {
            areaTeks.setText("Gagal membaca data histori.");
        }
    }

    public static void main(String[] args) {
        SmartDropZone aplikasi = new SmartDropZone();
        aplikasi.setVisible(true);
    }
}
