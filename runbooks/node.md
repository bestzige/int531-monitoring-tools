# Node Runbook

---

## Table of Contents

- [Down](#down)
- [CPU & Saturation](#cpu)
- [Memory Pressure](#memory)
- [Disk Capacity & IO](#disk)
- [Load & Scheduling](#load)
- [Network Errors](#network)
- [General Guidelines](#general-guidelines)

---

## Down

### 🔴 Alert: `NodeDown`

**Severity:** Critical
**Trigger:** `up{job="node-exporter"} == 0` for 2 minutes

**Meaning:**

- Prometheus cannot scrape node-exporter on the VM
- The VM may be powered off, unreachable, or severely unhealthy

On Proxmox, this often indicates a **VM crash, freeze, or host-level issue**.

### Immediate Checks

- Check Prometheus **Targets** page for `job=node-exporter`
- Verify VM power state in **Proxmox UI**
- Test network connectivity (ping / SSH)
- Check node-exporter service on the VM

### Diagnosis

- VM stopped or kernel panic
- VM migrated or restarted
- node-exporter service crashed
- Network or firewall issue
- Proxmox host resource exhaustion

### Mitigation

- Restart node-exporter service
- Reboot the VM if unresponsive
- Migrate VM to another Proxmox host if needed
- Verify firewall and security group rules

### Escalation

- Page on-call immediately
- Escalate to infrastructure team if multiple VMs affected

---

## CPU & Saturation

### ⚠️ Alert: `NodeHighCpuUsage`

**Severity:** Warning
**Meaning:** CPU usage > 80% for sustained period

### 🚨 Alert: `NodeCpuSaturationCritical`

**Severity:** Critical
**Meaning:** CPU usage > 95%, workload starvation likely

### Immediate Checks

```bash
top
htop
mpstat -P ALL 1
```

- Check `%user`, `%system`, `%iowait`, `%steal`

### Diagnosis

- CPU overcommit on Proxmox host
- Noisy neighbor VM
- Application runaway processes
- Insufficient vCPU allocation

### Mitigation

- Restart or throttle offending processes
- Scale application horizontally
- Increase vCPU allocation
- Migrate VM to less loaded Proxmox host

### Escalation

- Escalate if CPU steal or saturation persists

---

## Memory Pressure

### ⚠️ Alert: `NodeHighMemoryUsage`

**Severity:** Warning
**Meaning:** Available memory < 20%

### 🚨 Alert: `NodeMemoryCritical`

**Severity:** Critical
**Meaning:** Available memory < 10%, OOM risk

### Immediate Checks

```bash
free -m
vmstat 1
ps aux --sort=-%mem | head
```

### Diagnosis

- Memory leaks
- Too many services on VM
- Insufficient RAM allocation
- Cache growth (DB, JVM, etc.)

### Mitigation

- Restart memory-hungry services
- Increase VM RAM
- Reduce workload density
- Tune application memory limits

### Escalation

- Page on-call if OOM events observed

---

## Disk Capacity & IO

### ⚠️ Alert: `NodeDiskFillingUp`

**Severity:** Warning
**Meaning:** Disk usage > 80%

### 🚨 Alert: `NodeDiskFullImminent`

**Severity:** Critical
**Meaning:** Disk usage > 90%, write failures likely

### Immediate Checks

```bash
df -h
lsblk
iostat -xz 1
```

### Diagnosis

- Log growth
- Backup or dump files
- Application misconfiguration
- Proxmox thin disk nearing limit

### Mitigation

- Clean up logs and temp files
- Rotate logs
- Expand disk in Proxmox
- Migrate VM storage if IO wait is high

### Escalation

- Escalate immediately if root filesystem is impacted

---

## Load & Scheduling

### ⚠️ Alert: `NodeHighLoad`

**Severity:** Warning
**Meaning:** Load average exceeds CPU core count

### Immediate Checks

```bash
uptime
htop
```

### Diagnosis

- CPU-bound workloads
- IO wait bottlenecks
- Too many concurrent processes

### Mitigation

- Reduce concurrency
- Scale services
- Increase vCPU or migrate VM

---

## Network Errors

### ⚠️ Alert: `NodeNetworkErrors`

**Severity:** Warning
**Meaning:** Network receive/transmit errors detected

### Immediate Checks

```bash
ip -s link
ethtool -S <iface>
```

### Diagnosis

- Packet drops
- MTU mismatch
- Proxmox bridge issues
- Host NIC problems

### Mitigation

- Restart network interface
- Validate MTU and bridge config
- Migrate VM to another host

### Escalation

- Escalate if errors persist or multiple VMs affected

---

## General Guidelines

- Node alerts indicate **infrastructure health**, treat as high priority
- Always correlate with **Proxmox host metrics**
- IO wait and CPU steal are early warning signs
- Prefer VM migration over restarts when possible
- Capture timelines and metrics for postmortems

---
