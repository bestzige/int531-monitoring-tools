# API Golden Signals Runbook

---

## Table of Contents

1. [Availability](#availability)
2. [Traffic](#traffic)
3. [Latency](#latency)
4. [Errors (Route-Level)](#errors-route-level)
5. [Error Budget](#error-budget)
6. [Saturation](#saturation)
7. [General Response Guidelines](#general-response-guidelines)

---

## Availability

### Alert: `ApiDown`

**Severity:** Critical
**Meaning:** Prometheus cannot scrape the API target. The API process, container, or node is likely unavailable.

#### Immediate Checks

- Check Prometheus **Targets** page (`/targets`)
- Verify API pod / container / VM is running
- Check recent deployments, restarts, or crashes
- Validate ingress, load balancer, and network connectivity

#### Diagnosis

- Pod crashlooping or OOMKilled
- Node failure or resource exhaustion
- DNS or service discovery failure
- TLS or certificate expiration

#### Mitigation

- Restart API service or pod
- Roll back recent deployment
- Scale replicas if supported
- Fix networking, DNS, or certificate issues

#### Escalation

- Page on-call engineer immediately
- Escalate to platform / infrastructure team if root cause is outside the API

---

## Traffic

### Alert: `ApiLowTraffic`

**Severity:** Warning
**Meaning:** API request rate dropped below expected baseline.

#### Immediate Checks

- Confirm traffic drop in Grafana dashboards
- Check upstream services or clients
- Validate ingress and load balancer health

#### Diagnosis

- Client outage or paused workload
- DNS or routing misconfiguration
- Authentication or authorization failures

#### Mitigation

- Restore routing or load balancer configuration
- Notify upstream service owners
- Roll back recent gateway, auth, or routing changes

#### Notes

- This alert may be informational during off-peak hours

---

## Latency

### Alert: `ApiHighLatencyP95`

**Severity:** Warning
**Meaning:** 95th percentile request latency exceeds 1 second.

### Alert: `ApiHighLatencyP99`

**Severity:** Critical
**Meaning:** Tail latency is severely degraded and user impact is likely.

### Alert: `ApiTooManySlowRequests`

**Severity:** Warning
**Meaning:** More than 10% of requests exceed 1 second latency.

#### Immediate Checks

- Inspect latency dashboards (p50 / p95 / p99)
- Check correlation with error rate and traffic
- Identify affected routes

#### Diagnosis

- Slow downstream dependencies (database, cache, external APIs)
- CPU, memory, or I/O saturation
- Garbage collection pauses or thread exhaustion
- Hot routes or inefficient queries

#### Mitigation

- Scale API replicas
- Scale or restart slow dependencies
- Enable caching or optimize queries
- Apply rate limiting or load shedding if available

#### Escalation

- Page on-call engineer if `ApiHighLatencyP99` fires
- Engage database or platform teams if dependency-related

---

## Errors (Route-Level)

### Alert: `ApiRoute500Error`

**Severity:** Critical
**Meaning:** One or more HTTP 500 errors occurred on a specific API route.

**Alert Context Includes:**

- HTTP method
- Route template (example: `/api/students`)

#### Immediate Checks

- Inspect application logs for stack traces
- Identify the affected route and method
- Check recent deployments or configuration changes

#### Diagnosis

- Unhandled exceptions
- Invalid assumptions or input validation failures
- Dependency outages or timeouts

#### Mitigation

- Restart unhealthy instances
- Roll back recent deployment
- Disable problematic feature flags

---

### Alert: `ApiRoute500Spike`

**Severity:** Critical
**Meaning:** Rapid increase in HTTP 500 errors on a specific route.

#### Immediate Checks

- Check error rate trend in Grafana
- Confirm whether traffic volume increased
- Identify whether a single route is responsible

#### Mitigation

- Roll back recent changes immediately if correlated
- Temporarily block or rate-limit problematic route
- Scale API if errors are load-induced

---

### Alert: `ApiRouteHighErrorRate`

**Severity:** Critical
**Meaning:** Sustained 5xx error rate above threshold (typically 10%) on a specific route.

#### Immediate Checks

- Compare error rate vs total traffic per route
- Verify whether SLOs are at risk

#### Mitigation

- Freeze deployments
- Focus on restoring successful responses
- Apply traffic shaping or fallback logic

#### Escalation

- Page API on-call immediately
- Escalate to dependency owners if applicable

---

## Error Budget

### Alert: `ApiErrorBudgetBurn`

**Severity:** Critical
**Meaning:** Error budget is burning rapidly; SLO violation is imminent.

#### Immediate Checks

- Confirm remaining error budget
- Identify if incident is already ongoing

#### Mitigation

- Freeze all non-essential deployments
- Prioritize reliability over feature delivery
- Reduce load or disable non-critical endpoints

#### Follow-up

- Conduct post-incident review
- Adjust SLOs or alert thresholds if necessary

---

## Saturation

### Alert: `ApiHighSaturation`

**Severity:** Warning
**Meaning:** High number of in-flight requests may cause queuing and latency.

#### Immediate Checks

- Check CPU, memory, and event loop metrics
- Inspect concurrency limits and queue depth

#### Diagnosis

- Traffic surge
- Slow downstream dependencies
- Insufficient replica count or resource limits

#### Mitigation

- Scale horizontally
- Increase resource limits
- Apply rate limiting or backpressure

---

## General Response Guidelines

- Acknowledge critical alerts immediately
- Communicate status in the incident channel
- Prefer rollback over risky fixes during incidents
- Capture timeline, metrics, and decisions for postmortem
- Review alert noise and tuning regularly

---
