# API Golden Signals Runbook

## Traffic

**Symptom**

- Request rate ต่ำหรือเป็นศูนย์

**Action**

- ตรวจ service
- ตรวจ ingress / DNS
- rollback release ล่าสุด

---

## Latency

**Symptom**

- p95 latency > 1s

**Action**

- ดู slow query
- ตรวจ event loop lag
- scale service

---

## Errors

**Symptom**

- 5xx error rate > 5%

**Action**

- ดู error log
- dependency down
- rollback release

---

## Saturation

**Symptom**

- active_requests สูง

**Action**

- scale horizontal
- เพิ่ม worker
- rate limit
