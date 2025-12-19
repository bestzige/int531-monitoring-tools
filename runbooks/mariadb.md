# MariaDB Runbook

---

## Table of Contents

- [Down](#down)
- [Connections](#connections)
- [Slow Queries & Latency](#slow)
- [InnoDB Buffer Pool](#bufferpool)
- [General Guidelines](#general-guidelines)

---

## Down

### Alert: `MariaDBDown`

**Severity:** Critical
**Trigger:** `up{job="mariadb"} == 0` **OR** `mysql_up == 0` for 10 seconds

**Meaning:**

- Prometheus cannot scrape the MariaDB exporter, **or**
- Exporter is reachable but MariaDB itself is not responding

This usually indicates MariaDB is down, stuck, or unreachable.

### Immediate Checks

- Check Prometheus **Targets** page for `job=mariadb`
- Verify MariaDB service is running
- Verify exporter process/container is running
- Check network connectivity (port, firewall, security group)

### Diagnosis

- MariaDB process crashed or OOM-killed
- Disk full preventing startup
- Exporter credentials invalid
- Node / VM failure

### Mitigation

- Restart MariaDB service
- Restart mariadb-exporter
- Free disk space if full
- Roll back recent config or secret changes

### Escalation

- Page DBA / on-call immediately
- Escalate to infrastructure team if host-level issue

---

## Connections

### Alert: `MariaDBTooManyConnections`

**Severity:** Warning
**Meaning:** Active connections exceed **80%** of `max_connections` for 5 minutes.

### Alert: `MariaDBConnectionExhaustion`

**Severity:** Critical
**Meaning:** Active connections exceed **95%**, new connections may be rejected.

### Immediate Checks

```sql
SHOW STATUS LIKE 'Threads_connected';
SHOW VARIABLES LIKE 'max_connections';
```

### Diagnosis

- Application connection leaks
- Traffic spikes or batch jobs
- Slow queries holding connections
- `max_connections` set too low

### Mitigation

- Identify long-running queries:

```sql
SHOW PROCESSLIST;
```

- Restart misbehaving application pods or services
- Increase `max_connections` **temporarily** if safe
- Ensure applications use connection pooling

### Escalation

- Page DBA if critical alert fires
- Notify application owners if leak suspected

---

## Slow Queries & Latency

### Alert: `MariaDBSlowQueries`

**Severity:** Warning
**Meaning:** Slow queries exceed **1 per second** for 5 minutes.

### Alert: `MariaDBQueryLatencyDegraded`

**Severity:** Critical
**Meaning:** More than **10% of queries** are classified as slow.

### Immediate Checks

- Confirm slow query log is enabled
- Check query volume trends
- Identify recently deployed application changes

### Diagnosis

- Missing or inefficient indexes
- Full table scans
- Lock contention or blocking transactions
- Heavy queries introduced by new code

### Mitigation

- Analyze queries:

```sql
EXPLAIN <query>;
```

- Add or optimize indexes
- Reduce query scope (filters, limits)
- Roll back recent application deployments if needed

### Notes

- Alerts may trigger during scheduled batch jobs or migrations

---

## InnoDB Buffer Pool

### Alert: `MariaDBHighBufferPoolUsage`

**Severity:** Warning
**Meaning:** Buffer pool usage > **90%** for 10 minutes.

### Alert: `MariaDBBufferPoolCritical`

**Severity:** Critical
**Meaning:** Buffer pool usage > **97%**, increased disk reads and latency risk.

### Immediate Checks

- Check memory usage on the DB host
- Review buffer pool metrics in Grafana

### Diagnosis

- Working dataset larger than buffer pool
- Increased traffic or query volume
- Insufficient RAM allocated to MariaDB

### Mitigation

- Increase `innodb_buffer_pool_size` if memory allows
- Reduce memory pressure from other services
- Optimize queries to reduce unnecessary reads

### Escalation

- Page DBA if usage remains >97% or performance degrades

---

## General Guidelines

- Treat MariaDB alerts as **high-impact incidents**
- Avoid restarts during peak traffic unless required
- Prefer application- or query-level fixes first
- Capture metrics, queries, and timelines for postmortems

---
