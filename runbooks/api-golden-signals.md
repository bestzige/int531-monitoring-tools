# API Golden Signals Runbook

---

## Table of Contents

1. [Availability](#availability)
2. [Traffic](#traffic)
3. [Latency](#latency)
4. [Errors](#errors)
5. [Error Budget](#error-budget)
6. [Saturation](#saturation)
7. [General Response Guidelines](#general-response-guidelines)

---

## Availability

### 🚨 Alert: `ApiDown`

**Severity:** Critical
**Meaning:** Prometheus cannot scrape the API target. The API is likely down or unreachable.

#### Immediate Checks

- Check Prometheus target status (`/targets`)
- Verify API pod / VM / container is running
- Check recent deployments or restarts
- Validate network connectivity (LB, ingress, firewall)

#### Diagnosis

- Pod crashlooping?
- Node failure?
- DNS or service discovery issues?
- TLS certificate expired?

#### Mitigation

- Restart API service
- Roll back recent deployment
- Scale replicas if applicable
- Fix networking or DNS issues

#### Escalation

- Page on-call engineer immediately
- Escalate to infra / platform team if root cause is outside API

---

## Traffic

### 🚦 Alert: `ApiLowTraffic`

**Severity:** Warning
**Meaning:** API request rate dropped below expected baseline.

#### Immediate Checks

- Confirm traffic drop in Grafana dashboards
- Check upstream services / clients
- Validate ingress / load balancer health

#### Diagnosis

- Client outage or deploy?
- Misconfigured routing or DNS?
- Auth failures preventing requests?

#### Mitigation

- Restore routing / LB configuration
- Notify client or upstream service owners
- Roll back recent gateway or auth changes

#### Notes

- This alert may be informational during off‑peak hours

---

## Latency

### ⏱ Alert: `ApiHighLatencyP95`

**Severity:** Warning
**Meaning:** 95% of requests exceed 1s latency.

### 🐢 Alert: `ApiHighLatencyP99`

**Severity:** Critical
**Meaning:** Tail latency is severely degraded.

### 🐌 Alert: `ApiTooManySlowRequests`

**Severity:** Warning
**Meaning:** >10% of requests exceed 1 second.

#### Immediate Checks

- Inspect latency dashboards (p50 / p95 / p99)
- Check error rate correlation
- Look at request volume changes

#### Diagnosis

- Downstream dependency latency (DB, cache, external API)
- Resource starvation (CPU, memory, I/O)
- GC pauses or thread exhaustion
- Hot endpoints or heavy queries

#### Mitigation

- Scale API replicas
- Scale or restart slow dependencies
- Enable caching or rate limiting
- Temporarily shed load if supported

#### Escalation

- Page on-call if p99 alert fires
- Engage database or platform teams if dependency-related

---

## Errors

### ❌ Alert: `ApiHighErrorRate`

**Severity:** Critical
**Meaning:** Sustained 5xx error rate above 5%.

### 🔥 Alert: `ApiErrorSpike`

**Severity:** Critical
**Meaning:** Sudden spike of 5xx errors.

#### Immediate Checks

- Check logs for stack traces or error patterns
- Identify affected endpoints
- Verify recent deployments or config changes

#### Diagnosis

- Unhandled exceptions
- Dependency failures
- Timeout misconfiguration
- Invalid input or schema mismatch

#### Mitigation

- Roll back recent deployment
- Restart unhealthy instances
- Disable problematic feature flags
- Apply hotfix if safe

#### Escalation

- Immediate page to API on-call
- Escalate to owning team of failing dependency

---

## Error Budget

### 📉 Alert: `ApiErrorBudgetBurn`

**Severity:** Critical
**Meaning:** Error rate >10%, SLO burn is high.

#### Immediate Checks

- Calculate remaining error budget
- Identify if incident is already ongoing

#### Mitigation

- Freeze deployments
- Focus solely on stability
- Apply traffic shaping or throttling

#### Follow‑up

- Conduct postmortem
- Review SLO thresholds and alert tuning

---

## Saturation

### 🧠 Alert: `ApiHighSaturation`

**Severity:** Warning
**Meaning:** Too many in-flight requests, risk of queuing and latency.

#### Immediate Checks

- Check CPU, memory, and thread pools
- Inspect concurrency and queue metrics

#### Diagnosis

- Traffic surge
- Slow downstream dependencies
- Insufficient replica count

#### Mitigation

- Scale horizontally
- Increase resource limits
- Apply rate limits or backpressure

---

## General Response Guidelines

- Always acknowledge critical alerts promptly
- Communicate status in incident channel
- Prefer rollback over risky fixes during incidents
- Capture timeline and metrics for postmortem

---
