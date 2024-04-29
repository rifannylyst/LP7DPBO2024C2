# LP7DPBO2024C2

Saya Rifanny Lysara Annastasya [2200163] mengerjakan LP7 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

### Desain Program:

1. **StartForm**: Kelas ini adalah GUI Form awal yang muncul saat program pertama kali dijalankan. Form ini berisi tombol "Start Game" yang akan membuka permainan Flappy Bird saat ditekan.

2. **FlappyBird**: Kelas ini mewarisi JPanel dan merupakan panel tempat permainan Flappy Bird akan ditampilkan. Kelas ini berfungsi sebagai layar utama permainan yang menampilkan latar belakang, burung, pipa, dan skor.

3. **Player**: Kelas ini merepresentasikan burung dalam permainan. Berisi atribut-atribut seperti posisi, lebar, tinggi, gambar burung, dan kecepatan vertikal.

4. **Pipe**: Kelas ini merepresentasikan pipa dalam permainan. Berisi atribut-atribut seperti posisi, lebar, tinggi, gambar pipa, dan kecepatan horizontal.

### Alur Program:

1. **StartForm Ditampilkan**:
   - Saat program pertama kali dijalankan, GUI Form `StartForm` akan muncul. Form ini berisi tombol "Start Game".

2. **Tombol "Start Game" Ditekan**:
   - Saat tombol "Start Game" ditekan, form `StartForm` akan ditutup.
   - JFrame baru untuk permainan Flappy Bird akan dibuat dan ditampilkan.

3. **Inisialisasi Game**:
   - Saat JFrame game `FlappyBird` ditampilkan, semua inisialisasi permainan dilakukan.
   - Inisialisasi termasuk mengatur ukuran frame, membuat objek `FlappyBird`, menambahkan objek `FlappyBird` ke dalam frame, dan memulai timer untuk pergerakan burung dan pipa.

4. **Pergerakan dan Update Layar**:
   - Setiap kali timer dipicu, metode `move()` di kelas `FlappyBird` akan dipanggil.
   - Metode `move()` akan mengatur pergerakan burung, pergerakan pipa, serta mendeteksi tabrakan atau melewati pipa.
   - Setelah pembaruan status permainan selesai, layar akan diperbarui dengan memanggil metode `repaint()`.

5. **Input Pengguna**:
   - Saat tombol spasi ditekan, burung akan melompat naik ke atas.
   - Input pengguna ditangkap oleh metode `keyPressed()` di kelas `FlappyBird`.

6. **Game Over**:
   - Saat burung menabrak pipa atau jatuh di bawah layar, permainan akan berakhir.
   - Metode `gameOver()` akan dipanggil, menghentikan timer dan menampilkan layar game over.
   - Setelah jeda waktu tertentu, dialog game over akan ditampilkan dengan skor akhir dan opsi untuk mengulang permainan atau keluar.

7. **Restart Game**:
   - Jika pengguna memilih untuk mengulang permainan, skor akan direset, pipa akan dihapus, posisi burung akan diatur ulang, dan permainan akan dimulai kembali.
   - Jika pengguna memilih untuk keluar, program akan ditutup.

### Dokumentasi

https://github.com/rifannylyst/LP7DPBO2024C2/assets/147616851/27eb75ba-52cd-466f-8859-6526614aa809

