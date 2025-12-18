# Node / Node Exporter Runbook

## Node Down

**Symptom**

- Prometheus scrape fail
- Alert: NodeDown

**Action**

1. Ping / SSH เข้าเครื่อง
2. ตรวจ systemd / VM / cloud status
3. ตรวจ firewall / network
4. Restart node_exporter

---

## CPU

**Symptom**

- CPU > 85%

**Action**

1. `top` / `htop`
2. หา process ที่กิน CPU
3. ตรวจ loop / runaway process
4. Scale หรือ restart service

---

## Load

**Symptom**

- Load สูงกว่า CPU core

**Action**

1. เช็ก I/O wait
2. ตรวจ disk / network
3. ดู thread stuck
4. Scale node ถ้าจำเป็น

---

## Memory

**Symptom**

- Available memory < 15%

**Action**

1. `free -m`
2. หา memory leak
3. Restart service
4. เพิ่ม RAM / swap

---

## Disk

**Symptom**

- Disk < 15% free

**Action**

1. `df -h`
2. หาไฟล์ใหญ่ / log
3. Clean log / tmp
4. Extend volume
