# POS Kasir — Prototipe (uji stack teknologi)

Prototipe awal untuk menguji stack yang dipilih sebelum desain UI final.

## Stack
- **Android (Kotlin + Jetpack Compose)** — aplikasi kasir (folder `android/`).
- **Android (Kotlin + Jetpack Compose)** — aplikasi kasir (belum disertakan, menunggu desain UI final).
- **Room Database** — penyimpanan lokal menu & transaksi (offline-first) di sisi Android.
- **Node.js + Fastify** — backend API (folder `backend/`), bisa dijalankan lokal.
- **PostgreSQL** — opsional untuk sinkronisasi multi-outlet (belum wajib untuk prototipe).
- **GitHub** — version control.

## Menjalankan backend
```bash
cd backend
npm install
npm start
```
Server berjalan di `http://0.0.0.0:3000`. Buka `/` untuk UI sederhana (kasir, dapur, menu).

## Akun demo
- admin / admin123
- kasir / kasir123
- dapur / dapur123

## Endpoint API
- `POST /auth/login`
- `GET /menu`, `POST/PUT/DELETE /menu/:id` (auth)
- `POST /orders` (auth), `GET /orders`, `GET /orders/:id`, `PATCH /orders/:id/status` (auth)
- `GET /health`

## Menjalankan Android
1. Buka folder `android/` di Android Studio.
2. Tunggu Gradle sync selesai.
3. Jalankan di emulator/device. Backend URL default `http://10.0.2.2:3000/` (emulator). Ubah `RetrofitClient.BASE_URL` jika perlu.
4. Pastikan backend berjalan.

## Test
```bash
cd backend && npm test
```

## Rencana lanjutan
- Port UI ke Android (Kotlin + Compose) dengan Room.
- Tambah PostgreSQL + sinkronisasi saat backend dipasang.
