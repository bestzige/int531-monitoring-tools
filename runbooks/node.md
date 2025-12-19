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

### Alert: `NodeDown`

**Severity:** Critical
**Trigger:** `up{job="node"} == 0` for 2 minutes

**Meaning:**

- Prometheus cannot scrape node-exporter on the VM
- The VM may be powered off, unreachable, or severely unhealthy

On Proxmox, this often indicates a **VM crash, freeze, or host-level issue**.

### Immediate Checks

- Check Prometheus **Targets** page for `job=node`
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

### Alert: `NodeHighCpuUsage`

**Severity:** Warning
**Meaning:** CPU usage exceeds 80% for a sustained period

### Alert: `NodeCpuCritical`

**Severity:** Critical
**Meaning:** CPU usage exceeds 95%, workload starvation likely

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

### Alert: `NodeHighMemoryUsage`

**Severity:** Warning
**Meaning:** Available memory below 15%

### Alert: `NodeMemoryCritical`

**Severity:** Critical
**Meaning:** Available memory below 5%, OOM risk

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
- Cache growth (databases, JVM, etc.)

### Mitigation

- Restart memory-hungry services
- Increase VM RAM
- Reduce workload density
- Tune application memory limits

### Escalation

- Page on-call if OOM events are observed

---

## Disk Capacity & IO

### Alert: `NodeDiskAlmostFull`

**Severity:** Warning
**Meaning:** Available disk space below 15%

### Alert: `NodeDiskCritical`

**Severity:** Critical
**Meaning:** Available disk space below 5%, write failures likely

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

- Clean up logs and temporary files
- Rotate logs
- Expand disk in Proxmox
- Migrate VM storage if IO wait is high

### Escalation

- Escalate immediately if the root filesystem is impacted

---

## Load & Scheduling

### Alert: `NodeHighLoad`

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
- Increase vCPU allocation or migrate VM

---

## Network Errors

### Alert: `NodeNetworkErrors`

**Severity:** Warning
**Meaning:** Network receive or transmit errors detected

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
- Validate MTU and bridge configuration
- Migrate VM to another host

### Escalation

- Escalate if errors persist or multiple VMs are affected

---

## General Guidelines

- Node alerts indicate **infrastructure health** and should be treated as high priority
- Always correlate with **Proxmox host metrics**
- IO wait and CPU steal are early warning signs
- Prefer VM migration over restarts when possible
- Capture timelines and metrics for postmortems

---
