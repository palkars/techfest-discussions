# Using Quantum Programming using IBM Qiskit

## Setup
1. Sign up for IBM CLoud account and  IBM Quantum Computing account at https://quantum.cloud.ibm.com/signin?redirectTo=%2F
2. This will ask for Credit card information - this is just to verify the profile.

## Quick start
1. Install python, Qiskit and other required libraries - https://quantum.cloud.ibm.com/docs/en/guides/quick-start
2. Using Jupyter notebook, run the qubit program in the quick-start guide above.

## Code and explanation:
```
from qiskit import QuantumCircuit
from qiskit.primitives import StatevectorSampler

qc = QuantumCircuit(3)
qc.h(0)
qc.cx(0, 1)
qc.cx(1,2)
qc.x(1)
qc.measure_all()

sampler = StatevectorSampler()
result = sampler.run([qc], shots=1024).result()
print(result[0].data.meas.get_counts())

# Uncomment lines 2 and 8 if you are not using Python in a Jupyter notebook
# import matplotlib.pyplot as plt
from qiskit.visualization import plot_histogram

counts = result[0].data.meas.get_counts()
plot_histogram(counts)

# plt.show()
```

Happy to build this up from scratch.

## The classical bit, as a baseline

A regular computer bit is a switch: it's either 0 or 1, full stop. Nothing in between, nothing fuzzy. Every calculation you've ever done on a laptop is ultimately billions of these switches flipping deterministically.

## What a qubit actually is

A qubit is also a two-state system — it has a "0" state and a "1" state, usually written **|0⟩** and **|1⟩** (that funny bracket is just physics notation for "a quantum state named 0"). But *before you look at it*, a qubit isn't stuck at one or the other. It can be in a **superposition** — a combination of both |0⟩ and |1⟩ at the same time, each with a certain "weight."

The important, easy-to-misunderstand point: this isn't the same as "we don't know yet whether it's secretly 0 or 1" (like a coin under your hand mid-flip, which classically *is* one or the other, you just haven't looked). A qubit in superposition genuinely doesn't have a definite value yet — the ambiguity is physical, not just informational. Physicists have run experiments that rule out the "secretly-already-decided" explanation.

## Measurement collapses it

Here's the catch: you can never directly observe a qubit's superposition. The moment you measure it, it's forced to snap to a definite classical answer — either 0 or 1 — and the superposition is gone. You get one bit of classical information out, chosen probabilistically according to the "weights" the superposition had.

So if a qubit is in a 50/50 superposition, measuring it gives you 0 half the time and 1 half the time, at random — like a fair coin, except the randomness was baked into the qubit's actual state, not hidden from you.

This is why quantum programs always work the same way: prepare a state, do some operations, measure many times ("shots"), and look at the statistics of the outcomes — you can't just peek at the superposition directly.

## Gates: how you manipulate a qubit

A "gate" is an operation that rotates or transforms a qubit's state. Two from your code:

- **Hadamard gate (`h`)**: takes a qubit that's definitely |0⟩ and turns it into an exact 50/50 superposition of |0⟩ and |1⟩. This is the standard way to introduce quantum randomness/superposition into a circuit.
- **CNOT / controlled-X gate (`cx`)**: takes two qubits — a "control" and a "target." If the control is |1⟩, it flips the target; if the control is |0⟩, it leaves the target alone. Normally that's just a conditional flip, nothing exotic. But if the control qubit is in superposition (not definitely 0 or 1), the gate effectively links the two qubits' fates together *before either is measured*. That linking is entanglement.

## Entanglement, concretely

After `qc.h(0)` then `qc.cx(0,1)` in your example, the two qubits are in a state where:
- Qubit 0 is 50/50 between 0 and 1 (thanks to the Hadamard)
- Qubit 1 has no independent randomness of its own — its fate is *tied* to qubit 0's

When you finally measure both, you always get matching results: `00` or `11`, never `01` or `10`. Measuring qubit 0 as "1" and measuring qubit 1 as "1" aren't two separate coin flips that happened to agree — they were never separate. That's the "spooky" bit Einstein was uneasy about: this correlation holds even though, before measurement, neither qubit had a definite value on its own.

## Mapping this back to your code

```python
qc = QuantumCircuit(2)   # two qubits, both start as |0⟩
qc.h(0)                  # qubit 0: |0⟩ -> 50/50 superposition of |0⟩ and |1⟩
qc.cx(0, 1)               # entangle qubit 1 with qubit 0
qc.measure_all()          # collapse both qubits to classical 0/1, record the result
```

Then `sampler.run([qc], shots=1024)` repeats this whole prepare-and-measure cycle 1024 times (a fresh independent qubit pair each shot — quantum states can't be "rewound" and re-measured, they have to be recreated), and counts how often each outcome (`00`, `01`, `10`, `11`) showed up. Because of the entanglement, you only ever see `00` and `11`, roughly 50/50 — which is exactly the signature that tells you entanglement actually happened, rather than you just having a bug in your code.

A couple of natural next questions if you want to go further: why superposition is written with numbers (amplitudes) rather than just "50/50," and what makes qubits physically hard to build (why real hardware needs error correction). Want me to go into either?

`qc.x(1)` applies a **Pauli-X gate** to qubit 1 — this is the quantum equivalent of a classical NOT gate. It flips the qubit's basis value: |0⟩ becomes |1⟩, and |1⟩ becomes |0⟩. If the qubit is in superposition, X swaps the roles of the two components rather than resolving anything — it's a deterministic flip, not a measurement, so no randomness or collapse happens here.

## What it does in this specific circuit

Walking through your circuit:

1. `qc.h(0)` — qubit 0 becomes a 50/50 superposition of 0 and 1.
2. `qc.cx(0,1)` — entangles qubit 1 with qubit 0 (they now agree: both 0 or both 1).
3. `qc.cx(1,2)` — entangles qubit 2 with qubit 1, which chains it to qubit 0 too. At this point all three qubits are entangled and would always agree: `000` or `111` (this is the "GHZ state" mentioned on the IBM page).
4. `qc.x(1)` — unconditionally flips qubit 1. Whatever qubit 1 "would have been," it's now the opposite. Since qubits 0 and 2 still agree with each other (they weren't touched), but qubit 1 has been flipped relative to them, the outcomes shift from `000`/`111` to **`010`/`101`**.

So instead of seeing all three bits match, you'll see the middle bit always be the *opposite* of the two outer bits, split roughly 50/50 between `010` and `101` across your 1024 shots.

This is a nice illustration of the difference between a **gate** (like `x`, `h`, `cx` — deterministic, reversible, doesn't destroy superposition/entanglement) and a **measurement** (`measure_all()` — probabilistic, irreversible, collapses everything to classical bits). The X gate is doing ordinary, deterministic quantum logic; it's only the final `measure_all()` that introduces randomness into what you actually see printed.

## Further study 
1. Follow this course https://quantum.cloud.ibm.com/learning/en/courses/basics-of-quantum-information

