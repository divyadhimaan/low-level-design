# Amazon Locker System

> **Difficulty:** Easy
> **Reference:** [Hello Interview – Amazon Locker LLD](https://www.hellointerview.com/learn/low-level-design/problem-breakdowns/amazon-locker)

---

## Problem Statement

Design a locker system like Amazon Locker where delivery drivers can deposit packages and customers can pick them up using a code.

---

## Clarifying Questions

| Question | Answer |
|---|---|
| Are there different compartment sizes? | Yes — SMALL, MEDIUM, LARGE. Match size exactly; no fallback to larger compartments. |
| What is in scope? | Locker operations only — from driver deposit to customer pickup. Delivery routing is out of scope. |
| How does the customer get the code? | Return the code from deposit; notification (SMS/email) is handled by a downstream system. |
| What happens on multiple wrong code attempts? | Return an error; no lockout logic needed. |
| Are access tokens unique per package? | Yes — one token per package, 1:1 mapping. |
| How long do codes last? | 7 days. Expired codes are rejected; package stays until staff removes it. |
| What if no compartment of the right size is available? | Return an error. No queueing or reservations. |

---

## Requirements

### In Scope
1. Carrier deposits a package by specifying size (SMALL / MEDIUM / LARGE).
   - System assigns an available compartment of matching size.
   - Opens the compartment and returns an access token, or throws an error if no space.
2. One access token is generated per package upon successful deposit.
3. Customer retrieves a package by entering the access token.
   - System validates the token and opens the compartment.
   - Throws a specific error if the token is invalid or expired.
4. Access tokens expire after **7 days**.
   - Expired tokens are rejected.
   - Package stays in the compartment until staff physically removes it.
5. Staff can open all expired compartments to handle packages manually.
6. Invalid tokens (wrong, already used, expired) return clear error messages.

### Out of Scope
- Delivery logistics / how the package gets to the locker
- Notification to the customer (SMS, email)
- Lockout after failed code attempts
- Multiple locker stations
- Payment or pricing
- UI / rendering layer

---

## Core Entities

| Entity | Responsibility |
|---|---|
| `Locker` | Orchestrator. Owns all compartments and the token lookup map. Entry point for deposit and pickup. |
| `AccessToken` | Bearer token for compartment access. Holds the code, expiration timestamp, and a reference to the compartment it unlocks. Enforces expiry. |
| `Compartment` | A physical locker slot. Has a size and tracks its own occupancy state. |
| `CompartmentSize` | Enum — `SMALL`, `MEDIUM`, `LARGE`. |

> **Why no `Package` entity?** The only package attribute our system cares about is its size — which is just an input parameter. Everything else (customer info, shipping ID) belongs to an external fulfillment system.

---

## Class Design

```
class Locker
    - compartments: List<Compartment>
    - accessTokenMap: Map<String, AccessToken>

    + getInstance(compartments) -> Locker        // Singleton
    + depositPackage(size: String) -> String     // returns token code, throws if no space
    + retrievePackage(tokenCode: String) -> void // throws if invalid/expired
    + openExpiredCompartments() -> void          // staff operation

class AccessToken
    - code: String
    - expirationTime: Instant
    - compartment: Compartment

    + isExpired() -> boolean
    + getCode() -> String
    + getCompartment() -> Compartment

class Compartment
    - compartmentSize: CompartmentSize
    - occupied: boolean

    + getCompartmentSize() -> String
    + isOccupied() -> boolean
    + markOccupied() -> void
    + markFree() -> void
    + open() -> void                             // simulates hardware unlock

enum CompartmentSize
    SMALL | MEDIUM | LARGE
```

---

## Key Flows

### Deposit Package

```
depositPackage(size):
    compartment = getAvailableCompartment(size)
    if compartment == null → throw "No available compartment of size X"

    compartment.open()
    compartment.markOccupied()
    accessToken = generateAccessToken(compartment)   // 6-digit code, TTL = 7 days
    accessTokenMap.put(accessToken.code, accessToken)

    return accessToken.code
```

### Retrieve Package

```
retrievePackage(tokenCode):
    if tokenCode is null/empty → throw "Token code cannot be null or empty"

    accessToken = accessTokenMap.get(tokenCode)
    if accessToken == null → throw "Invalid access token"
    if accessToken.isExpired() → throw "Access token has expired"

    accessToken.getCompartment().open()
    clearDeposit(accessToken)                        // markFree + remove from map
```

### Open Expired Compartments (Staff)

```
openExpiredCompartments():
    for each accessToken in accessTokenMap.values():
        if accessToken.isExpired():
            accessToken.getCompartment().open()
            clearDeposit(accessToken)
```

---

## Design Decisions & Trade-offs

### Occupancy state on `Compartment` vs. a Set in `Locker`
- **Chosen:** `occupied` flag lives on `Compartment`.
- **Why:** Physical presence (package is in the slot) is intrinsic to the compartment — it describes the compartment's own condition, not a system-managed relationship.
- **Alternative:** Track an `occupiedCompartments: Set<Compartment>` in `Locker` (useful if you want O(1) lookup without scanning all compartments). This is the approach used in the Parking Lot problem.
- **Rule of thumb:** Physical state → on the entity. Relational state → in the orchestrator.

### Strict size matching
- Rejected fallback logic (e.g., put a SMALL package in a MEDIUM slot).
- Simplifies allocation and avoids fragmentation; drivers are told to go to another locker if no matching size is free.

### Access token as a first-class entity
- Token isn't just a string field on `Compartment`.
- It owns expiration logic (`isExpired()`) and the compartment reference.
- This keeps expiry checks out of `Locker` and makes the model easier to extend (e.g., one-time-use enforcement, renewal).

### Token code format
- Generated as a zero-padded 6-digit integer (`String.format("%06d", random.nextInt(1_000_000))`).
- UUID was an alternative but a 6-digit numeric code is more user-friendly for manual entry.

### `Locker` as a Singleton
- Only one `Locker` instance should manage the compartments at a given physical location.
- Guards against duplicate allocation from concurrent access.

---

## Scenario Walkthrough

**Initial state:** Compartments = [SMALL, MEDIUM, LARGE], all free. `accessTokenMap = {}`

### Scenario 1 — Successful deposit
```
depositPackage("MEDIUM")
  → getAvailableCompartment → returns MEDIUM compartment
  → MEDIUM.open()
  → MEDIUM.markOccupied()
  → generateAccessToken → AccessToken("123456", now+7days, MEDIUM)
  → accessTokenMap.put("123456", token)
  → returns "123456"

State: MEDIUM.occupied=true, accessTokenMap={"123456" → token}
```

### Scenario 2 — Successful retrieval
```
retrievePackage("123456")
  → accessTokenMap.get("123456") → token found
  → token.isExpired() → false
  → MEDIUM.open()
  → clearDeposit: MEDIUM.markFree(), accessTokenMap.remove("123456")

State: MEDIUM.occupied=false, accessTokenMap={}
```

### Scenario 3 — Expired token
```
retrievePackage("EXPIRED")
  → accessTokenMap.get("EXPIRED") → token found
  → token.isExpired() → true (expiration < now)
  → throw "Access token has expired"

State: compartment still occupied, token still in map (staff must clear physically)
```

---

## Extensibility

| Feature | How to add |
|---|---|
| Lockout after N failed attempts | Add `failedAttempts: Map<String, Integer>` to `Locker`; increment on invalid token, throw after threshold |
| Token delivery (SMS/email) | `Locker.depositPackage` returns the code; a downstream `NotificationService` handles delivery |
| Multiple locker stations | Wrap multiple `Locker` instances in a `LockerNetwork` that routes by location or available capacity |
| Fallback to larger compartment | Replace strict size match in `getAvailableCompartment` with a best-fit strategy |
| Package sensors | Two-phase deposit: `open()` → driver confirms → `confirmDeposit()` generates token only after physical confirmation |
