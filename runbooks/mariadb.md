# MariaDB Runbook

## Down

**Symptom**

- mysqld_up = 0

**Action**

1. Check container / systemd
2. Check disk full?
3. Check error log
4. Restart DB

---

## Connections

**Symptom**

- Connections > 80%

**Action**

1. `SHOW PROCESSLIST`
2. หา connection leak
3. Increase max_connections
4. Add pool (Hikari / Prisma / Sequelize)

---

## Slow Queries

**Symptom**

- slow_queries increasing

**Action**

1. Enable slow_query_log
2. EXPLAIN query
3. Add index
4. Cache hot query

---

## Buffer Pool

**Symptom**

- Buffer pool > 90%

**Action**

1. Increase innodb_buffer_pool_size
2. Reduce memory usage
3. Scale DB instance
