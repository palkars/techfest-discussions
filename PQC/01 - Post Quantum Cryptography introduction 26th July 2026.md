# Post-Quantum Cryptography: Concepts, Standards, and Implementation

## Overview & Agenda
1. **Cryptography Fundamentals**: General Concepts, Symmetric vs. Asymmetric Paradigms
2. **The Quantum Threat**: Shor's and Grover's Algorithms & Public-Key Vulnerabilities
3. **NIST Standardization Focus**: ML-KEM (Kyber) and ML-DSA (Dilithium)
4. **Symmetric Security in the Quantum Era**: Impact on Hash Functions and Block Ciphers
5. **Implementation Ecosystem**: Java Providers & Multilanguage Software Libraries
6. **Mathematical Foundations**: Lattice-Based Cryptography & The Shortest Vector Problem (SVP)

---

## 1. Fundamentals of Cryptography

Cryptography ensures information security via three primary primitives: Confidentiality, Integrity, and Authenticity.

### 1.1 Symmetric Cryptography
In symmetric cryptography, both sender and receiver share a **single secret key** used for both encryption and decryption (or message authentication).

* **Primary Primitives**:
  * **Block Ciphers**: Advanced Encryption Standard (AES-128, AES-256), Triple-DES.
  * **Stream Ciphers**: ChaCha20.
  * **Hash Functions / MACs**: SHA-2, SHA-3, HMAC, Poly1305.
* **Core Security Characteristics**:
  * High computational efficiency and throughput.
  * Security relies on key length $n$; brute-force searching requires $2^n$ operations classically.
* **Key Distribution Challenge**: Requires a secure out-of-band channel or a public-key key exchange mechanism to distribute secret keys initially.

### 1.2 Asymmetric (Public-Key) Cryptography
Asymmetric cryptography utilizes a **mathematically linked key pair**: a **Public Key** (distributed freely) and a **Private Key** (kept secret).

* **Primary Primitives**:
  * **Key Establishment / Exchange**: Diffie-Hellman (DH), Elliptic Curve Diffie-Hellman (ECDH).
  * **Public-Key Encryption / Digital Signatures**: RSA, DSA, Elliptic Curve Digital Signature Algorithm (ECDSA), Ed25519.
* **Mathematical Foundations**:
  * **Integer Factorization Problem (IFP)**: Given $N = p \cdot q$, finding prime factors $p$ and $q$ is computationally intractable for large $N$ (basis of RSA).
  * **Discrete Logarithm Problem (DLP)**: Given $g$ and $g^x \pmod p$, solving for $x$ is intractable (basis of DH/DSA).
  * **Elliptic Curve Discrete Logarithm Problem (ECDLP)**: Given $P$ and $Q = [k]P$ on an elliptic curve, solving for the scalar $k$ is intractable (basis of ECC).

---

## 2. Why Quantum Computers Impact Public-Key Cryptography

Quantum computing leverages quantum mechanical phenomena—specifically **superposition** and **entanglement**—to perform certain mathematical operations exponentially faster than classical computers.

```
+-----------------------------------------------------------------------------------+
|                                 QUANTUM IMPACT                                    |
+------------------------------------+----------------------------------------------+
| Asymmetric Cryptography            | Symmetric Cryptography                       |
| (RSA, ECC, DH, ECDSA)              | (AES, SHA-2, SHA-3)                          |
+------------------------------------+----------------------------------------------+
| Threat: Shor's Algorithm           | Threat: Grover's Algorithm                   |
| Impact: COMPLETE BREAK             | Impact: KEY-SIZE REDUCTION                   |
| Speedup: Exponential               | Speedup: Quadratic                           |
| Mitigation: Transition to PQC      | Mitigation: Double key sizes (e.g., AES-256) |
+------------------------------------+----------------------------------------------+
```

### 2.1 The Threat to Public-Key Cryptography (Shor's Algorithm)
In 1994, Peter Shor formulated a quantum algorithm that solves period-finding in polynomial time $O((\log N)^3)$.

* **Impact on RSA**: Shor's algorithm efficiently factors large integers, breaking RSA.
* **Impact on ECC/DH**: Shor's algorithm efficiently computes discrete logarithms over finite fields and elliptic curve groups, breaking DH, ECDH, DSA, and ECDSA.
* **Consequence**: Once a Cryptographically Relevant Quantum Computer (CRQC) is built, **all classical public-key infrastructure (PKI) will be fundamentally broken**.
* **Harvest Now, Decrypt Later (HNDL)**: Adversaries are currently recording encrypted classical traffic to decrypt it retroactively once a CRQC becomes available.

### 2.2 Why Symmetric Cryptography & Hash Functions Resist Quantum Attacks
Symmetric primitives do not rely on structured algebraic hidden-subgroup problems (like integer factorization or discrete logs).

* **Grover's Algorithm (1996)**: Provides a quantum speedup for unstructured database searching.
  * Classical search complexity for key length $n$: $O(2^n)$.
  * Quantum search complexity using Grover's algorithm: $O(2^{n/2})$ (a **quadratic speedup**).
* **Implications for Hash Functions & Block Ciphers**:
  * **Preimage / Collision Resistance**: SHA-256 provides $2^{128}$ bits of preimage security against Grover's attack, which remains robust. Collision resistance under BHT quantum algorithm requires approx 2^{n/3}$ operations, making SHA-384 and SHA-512 extremely secure.
  * **AES-128**: Reduced to $2^{64}$ quantum operations (theoretically vulnerable).
  * **AES-256**: Reduced to $2^{128}$ quantum operations (remains secure against quantum attacks).
* **Conclusion**: Symmetric cryptography and hash functions do **not** need to be replaced by new mathematical paradigms. Increasing key/hash lengths (e.g., moving to AES-256 and SHA-3/SHA-512) fully mitigates the quantum threat.

---

## 3. NIST Post-Quantum Standards: ML-KEM and ML-DSA

In August 2024, NIST released its first standardized PQC standards (FIPS 203, FIPS 204, and FIPS 205), marking the official transition to quantum-resistant public-key cryptography.

```
+-----------------------------------------------------------------------------------+
|                             NIST PQC STANDARDS (2024)                             |
+---------------------+-----------------------+---------------------+---------------+
| Standard            | Original Name         | Function            | Basis         |
+---------------------+-----------------------+---------------------+---------------+
| FIPS 203 (ML-KEM)   | CRYSTALS-Kyber        | Key Encapsulation   | Module-LWE    |
| FIPS 204 (ML-DSA)   | CRYSTALS-Dilithium   | Digital Signature   | Module-LWE/SIS|
| FIPS 205 (SLH-DSA)  | SPHINCS+              | Digital Signature   | Hash-Based    |
+---------------------+-----------------------+---------------------+---------------+
```

### 3.1 ML-KEM (Module-Lattice-Based Key-Encapsulation Mechanism)
* **Standard**: FIPS 203 (derived from CRYSTALS-Kyber).
* **Purpose**: Secure key exchange / public-key encryption.
* **Mathematical Security Basis**: Hardness of the **Module Learning With Errors (M-LWE)** problem.
* **Parameter Sets**:
  * **ML-KEM-512**: NIST Security Category 1 (approx$ AES-128 equivalent).
  * **ML-KEM-768**: NIST Security Category 3 (approx$ AES-192 equivalent; recommended general-purpose baseline).
  * **ML-KEM-1024**: NIST Security Category 5 (approx$ AES-256 equivalent).
* **Key Characteristics**: Highly efficient operation, small public key and ciphertext sizes compared to other PQC candidates (ML-KEM-768 public key: 1,184 bytes; ciphertext: 1,088 bytes).

### 3.2 ML-DSA (Module-Lattice-Based Digital Signature Algorithm)
* **Standard**: FIPS 204 (derived from CRYSTALS-Dilithium).
* **Purpose**: General-purpose digital signatures (identity verification, authentication, code signing).
* **Mathematical Security Basis**: Hardness of the **Module Learning With Errors (M-LWE)** and **Module Short Integer Solution (M-SIS)** problems.
* **Parameter Sets**:
  * **ML-DSA-44**: Security Category 2.
  * **ML-DSA-65**: Security Category 3 (primary recommended parameter set).
  * **ML-DSA-87**: Security Category 5.
* **Key Characteristics**: Based on the Fiat-Shamir with Aborts paradigm. Offers excellent performance and moderate signature sizes (ML-DSA-65 signature: 3,309 bytes).

---

## 4. Software & Java Libraries Supporting PQC

## 4. Software & Language Libraries Supporting PQC

Transitioning production systems to PQC requires software support across cryptographic providers and programming environments.

### 4.1 Java Ecosystem Support
1. **Bouncy Castle (Java Cryptography Architecture - JCA / JCE)**:
   * **Version Support**: Bouncy Castle v1.78+ supports finalized NIST FIPS 203 (ML-KEM), FIPS 204 (ML-DSA), and FIPS 205 (SLH-DSA) standards.
   * **Classes**: `org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider`.
   * **Usage**: Provides seamless integration with standard Java `Cipher`, `KeyGenerator`, `KeyAgreement`, and `Signature` APIs.
2. **OpenJDK / JDK Core**:
   * JDK Security Enhancement initiatives (JEPs) integrate PQC key encapsulation and signature primitives into standard Java runtime providers (`SunJCE`).

### 4.2 Python Ecosystem Support
1. **`liboqs-python` (`oqs`)**:
   * **Description**: The official Python wrapper for the Open Quantum Safe (OQS) C library.
   * **Capabilities**: Provides object-oriented Pythonic APIs for key encapsulation (`oqs.KeyEncapsulation`) and digital signatures (`oqs.Signature`).
   * **Supported Algorithms**: Full support for NIST-standardized ML-KEM, ML-DSA, and SLH-DSA parameter sets.
2. **PyCA `cryptography`**:
   * **Description**: The industry-standard Python cryptography library maintained by Python Cryptographic Authority.
   * **Capabilities**: Actively incorporating PQC primitives, hybrid key exchanges (e.g., ECDH + ML-KEM), and standardized bindings as NIST specifications finalize.
3. **`pqcrypto`**:
   * **Description**: Python bindings wrapping clean C reference implementations of NIST post-quantum candidates.
   * **Capabilities**: Lightweight library offering straightforward `kem` and `sign` modules for rapid prototyping and academic evaluation.

### 4.3 C/C++, Rust, and Multilanguage Libraries
* **liboqs (Open Quantum Safe Project)**:
  * Open-source C library providing a unified API for quantum-safe key exchange and signature algorithms.
  * Offers native bindings for C++, Python, Rust, Go, and Java (`liboqs-java`).
* **OpenSSL 3.x**:
  * Integrates PQC via the **oqs-provider** module, allowing Apache, Nginx, and TLS stacks to support ML-KEM and ML-DSA seamlessly.
* **AWS KMS & BoringSSL**:
  * Commercial and open-source TLS implementations incorporating hybrid key exchanges (e.g., X25519 + ML-KEM-768).

---

## 5. Security Foundations: Lattice-Based Cryptography & SVP

Both ML-KEM (Kyber) and ML-DSA (Dilithium) derive their security from the mathematical hardness of lattice problems in high-dimensional spaces.

```
       Lattice Grid in 2D Space (Conceptual)
       
       o-------o-------o-------o-------o
      /       /       /       /       /
     o-------o-------o-------o-------o
    /       /       /       /       /
   o-------o-------v-------o-------o  <-- Shortest Vector v (SVP)
  /       /       /       /       /
 o-------o-------o-------o-------o
```
### 5.1 What is a Lattice?
A lattice $\mathcal{L}$ in $\mathbb{R}^n$ is a discrete additive subgroup defined by a set of linearly independent basis vectors $\mathbf{b}_1, \mathbf{b}_2, \dots, \mathbf{b}_m \in \mathbb{R}^n$:

$$\mathcal{L}\left(\mathbf{b}_1, \dots, \mathbf{b}_m\right) = \left\{ \sum_{i=1}^m a_i \mathbf{b}_i \;\middle|\; a_i \in \mathbb{Z} \right\}$$

### 5.2 The Shortest Vector Problem (SVP)
* **Definition**: Given a basis for a lattice $\mathcal{L}$, find the non-zero vector $\mathbf{v} \in \mathcal{L}$ that minimizes the Euclidean norm $\|\mathbf{v}\|$.
* **Computational Complexity**:
  * In low dimensions ($n \le 30$), SVP is easily solved via basis reduction algorithms (e.g., Lenstra–Lenstra–Lovász / LLL).
  * In high dimensions ($n \ge 500$ or $1000$), exact SVP is **NP-hard** under randomized reductions.
  * **Approximate SVP ($\text{SVP}_\gamma$)**: Finding a vector within a factor $\gamma(n)$ of the shortest vector is computationally infeasible for polynomial factors $\gamma$ in high dimensions.

### 5.3 From Lattices to Cryptographic Schemes: LWE and Module-LWE
Directly using raw SVP for cryptography is cumbersome. Modern PQC uses average-case lattice problems that reduce to worst-case lattice problems:

1. **Learning With Errors (LWE)** (Regev, 2005):
   * Given random linear equations with small additive noise $e$:
     $$\mathbf{a}_i \cdot \mathbf{s} + e_i \approx b_i \pmod q$$
   * Distinguishing $( \mathbf{a}_i, b_i )$ from uniform random pairs is as hard as solving worst-case lattice problems (like SVP) in dimension $n$.
2. **Module Learning With Errors (M-LWE)**:
   * Replaces vectors over integers with vectors over polynomial rings $\mathbb{R}_q = \mathbb{Z}_q[X]/(X^n + 1)$.
   * Balances the high security of standard LWE with the compact key sizes and computational efficiency of Ring-LWE.
   * **Underpins ML-KEM and ML-DSA**.

### 5.4 Why Quantum Computers Cannot Efficiently Solve SVP / LWE
* Shor's algorithm relies on finding hidden periodicities in **abelian groups** using Quantum Fourier Transforms (QFT).
* Lattices lack the algebraic periodic structure exploited by Shor's algorithm.
* The best-known quantum algorithms for solving high-dimensional lattice problems (e.g., Quantum Sieve algorithms) offer only mild speedups over classical algorithms and still require **exponential time** $2^{O(n)}$.

---

## Summary & Key Takeaways

1. **Classical Asymmetry Breakdown**: Public-key primitives (RSA, ECC, DH) are vulnerable to exponential speedup by Shor's algorithm on quantum hardware.
2. **Symmetric Primitives Persist**: AES-256 and SHA-256/SHA-3 remain secure against Grover's algorithm with simple key/hash length extensions.
3. **NIST PQC Standards**: ML-KEM (FIPS 203) for key encapsulation and ML-DSA (FIPS 204) for digital signatures lead the transition.
4. **Implementation Readiness**: Ecosystems like Java (via Bouncy Castle) and C/C++ (via liboqs) provide ready-to-use FIPS 203/204 implementations.
5. **Lattice Security**: Security rests on hard geometric lattice problems, specifically the Shortest Vector Problem (SVP) and Module-LWE, which resist both classical and quantum algorithms.
"""