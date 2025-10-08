# Hotel Management System (Java & SQL) · CS202
**English / Türkçe**

---

## 📘 Project Description / Proje Açıklaması

**English**  
This project is a modular **Hotel Management System** developed for the CS202 course.  
The backend data model defines core hospitality entities and workflows: **users & guests**, **hotels & rooms**, **bookings & payments**, **employees & tasks**, plus **housekeeping schedules** and **cleaning history**. The relational schema enforces constraints and uses enums for controlled states (e.g., room types/status, booking/payment/task states).  
A companion SQL script seeds representative data and demonstrates usage with sample INSERT/UPDATE/DELETE and SELECT queries (availability listing, user bookings, housekeeping over maintenance rooms, hotel revenue aggregation).

On the application side, the Java layer is organized into **domain models** (e.g., `User`, `Employee`, `Guest`, `Payment`, `Housekeeping`), **service classes** (e.g., `BookingService`, `HotelService`, `RoomService`, `TaskService`, `CleaningHistoryService`, `HousekeepingService`, `ReceptionistService`) and **data access / connectivity** (`DatabaseConnection`, `UserDAO`). A Swing-based **GUI entry** (`MainGUI`) ties the services to the presentation layer, while administrative and receptionist roles are represented with dedicated components (`Administrator`, `Receptionist`).  
This separation allows the system to evolve from in-memory/data-access calls to more advanced persistence or business rules without breaking the UI contract.

The structure showcases **clean layering (models/services/DAO/GUI)** and a practical **RDBMS-backed domain** ready for extensions such as authentication, reporting dashboards, and role-based workflows.

---

**Türkçe**  
Bu proje, CS202 dersi kapsamında geliştirilmiş modüler bir **Otel Yönetim Sistemi**dir.  
Veri modeli; **kullanıcı & misafir**, **otel & oda**, **rezervasyon & ödeme**, **çalışan & görev**; ayrıca **housekeeping planı** ve **temizlik geçmişi** gibi konaklama süreçlerini kapsar. Şema, ilişkisel kısıtlar ve enum alanlar (ör. oda tipi/durumu, rezervasyon/ödeme/görev durumları) ile veri bütünlüğünü sağlar.  
Eşlik eden SQL betikleri, örnek veri ekleme ve sık kullanılan sorgularla (INSERT/UPDATE/DELETE/SELECT) sistemin işleyişini gösterir — uygun odaların listelenmesi, kullanıcı rezervasyonları, bakım altındaki odalar için housekeeping takibi, otele göre gelir toplamı gibi işlemleri içerir.

Uygulama tarafında Java katmanı; **alan modelleri** (`User`, `Employee`, `Guest`, `Payment`, `Housekeeping`), **servis sınıfları** (`BookingService`, `HotelService`, `RoomService`, `TaskService`, `CleaningHistoryService`, `HousekeepingService`, `ReceptionistService`) ve **erişim/bağlantı** (`DatabaseConnection`, `UserDAO`) olarak yapılandırılmıştır.  
Swing tabanlı **arayüz girişi** (`MainGUI`), servisleri kullanıcı arayüzüyle bağlar; yönetici ve resepsiyon rolleri (`Administrator`, `Receptionist`) ayrı bileşenlerle temsil edilir.  
Bu katmanlı yapı, gelecekte kimlik doğrulama, raporlama veya rol tabanlı iş akışları gibi ek özelliklerin kolayca entegre edilmesine imkân tanır.

---

**Course / Ders:** CS202  
**Stack:** Java (Swing, services/DAO) · SQL (DDL/DML)
