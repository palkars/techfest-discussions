# Artifical Neural Network - How network learn

## Claude Prompts and Response

1. Prompt:
    I have a group of 6 techno savvy people mostly developers with 10-25 yrs of software development experience. I want to learn how Artificial Neural Networks work focusing on mathematics behind it and ultimately learning how gradient descent works. Can you please list out tasks that we can split in 6 people and study individual areas and hten colelctively come together for an hour or so to discuss and form better understanding of ANN and Gradient descent
Good news: this topic decomposes almost perfectly into six pieces that chain together, so each person owns one link and the group assembles the whole derivation on the day.

## Two things to fix before anyone starts

**Agree on notation.** This is the single biggest cause of a wasted session. Nielsen, Goodfellow, Bishop and 3Blue1Brown all use different conventions and people end up arguing about transposes instead of ideas. Pick one up front. A safe default:

- Column vectors, $W^{(l)}$ has shape $(n_l \times n_{l-1})$
- $z^{(l)} = W^{(l)}a^{(l-1)} + b^{(l)}$, $a^{(l)} = \sigma(z^{(l)})$
- $\delta^{(l)} = \partial C / \partial z^{(l)}$
- Denominator layout for all derivatives

**Agree on one toy network.** Everyone anchors their prep to the same example: 2 inputs → 2 hidden units (sigmoid) → 1 output (sigmoid), binary cross-entropy loss, with fixed made-up weights. Six explanations of the same object beat six explanations of six objects.

## The six areas

**1. Forward pass and the linear algebra**
Affine transforms as the core primitive. Shape algebra through the whole net, why batching turns vectors into matrices, why this is all GEMM under the hood. Deliverable: the full shape trace of the toy net for batch size 1 and batch size 32, plus a hand-computed forward pass with the fixed weights that everyone else will reuse.

**2. Activation functions and why nonlinearity is mandatory**
Prove that stacked linear layers collapse to a single linear layer. Then sigmoid, tanh, ReLU, softmax: definitions, derivatives, and range/saturation behaviour. Deliverable: derivative of each activation worked out by hand, and a concrete numeric demonstration of gradient saturation (what $\sigma'(z)$ looks like at $z = 8$).

**3. Loss functions and the probabilistic framing**
MSE vs cross-entropy, and why cross-entropy is the maximum-likelihood estimator for a Bernoulli/categorical output. Deliverable: derive $\partial C/\partial z$ for softmax + cross-entropy and show it collapses to $\hat{y} - y$. This result is the "aha" of the whole session, and the fact that MSE + sigmoid does *not* collapse this way explains why nobody uses it for classification.

**4. Multivariable calculus for backprop**
Partial derivatives, gradient, Jacobian, Hessian (briefly). The multivariable chain rule on a computational graph, including the sum-over-paths rule when a variable feeds multiple downstream nodes. Deliverable: a small non-neural computational graph (say $f = (x+y)\cdot\sin(xy)$) differentiated both forward-mode and reverse-mode, showing why reverse mode is cheap when outputs ≪ inputs.

**5. Backpropagation itself**
The four backprop equations: output-layer error, error recursion $\delta^{(l)} = (W^{(l+1)T}\delta^{(l+1)}) \odot \sigma'(z^{(l)})$, and the two weight/bias gradient formulas. Derive the recursion rather than quoting it. Deliverable: full symbolic derivation for the toy net plus the cost analysis (backward pass ≈ 2× forward pass, regardless of depth).

**6. Gradient descent and optimisation**
The update rule and why the negative gradient is the steepest descent direction. Batch vs mini-batch vs stochastic, and what the noise actually buys you. Learning rate behaviour, divergence when $\eta > 2/L$ for an $L$-smooth function, convex vs non-convex landscapes, saddle points. Then momentum → RMSProp → Adam as a progression, each fixing a specific failure of the last. Deliverable: hand-run 3 iterations of GD on $f(w) = w^2$ at three learning rates (0.1, 0.9, 1.1) to show convergence, oscillation and divergence.

## The hour

- **0–5** Person 1 sets up the toy net and forward pass on the whiteboard.
- **5–30** Persons 2–5 each take ~6 minutes, building the derivation forward on the *same* board. Hard stop each, no slides. By minute 30 you should have a complete symbolic gradient.
- **30–40** Person 6 applies one update step and covers the optimiser landscape.
- **40–55** Group capstone: compute $\partial C/\partial w^{(1)}_{11}$ numerically for the toy net, then verify it against the finite-difference estimate $(C(w+\epsilon) - C(w-\epsilon))/2\epsilon$. This is gradient checking, it takes five minutes, and it converts abstract belief into "we actually got the same number."
- **55–60** Open questions and what to attack next.

## Worth flagging

Have everyone read Michael Nielsen's *Neural Networks and Deep Learning* chapters 1–2 as shared baseline (free online, and it uses roughly the notation above). 3Blue1Brown's four-part series is the best intuition layer and costs under an hour. Goodfellow chapters 6 and 8 for anyone who wants more depth on 5 and 6.

Given the group's experience level, the thing most likely to feel unsatisfying is stopping at the scalar case. Push person 4 and person 5 to state everything as vector-Jacobian products, because that is what autodiff frameworks actually implement, and it makes the jump to attention or convolutions much shorter later.


## Step 1

## What "step 1" actually owns

Person 1's job is to make the group fluent in one claim: **a neural network's forward pass is nothing but a chain of affine maps with a nonlinearity wedged between them.** Everything else in the session (backprop, gradient descent) is differentiating this chain. If the shapes and the data flow aren't crisp here, nothing downstream lands.

Four sub-topics, in order.

### 1a. The affine transform as the primitive

A single layer computes

$$z^{(l)} = W^{(l)}a^{(l-1)} + b^{(l)}, \qquad a^{(l)} = \sigma(z^{(l)})$$

Two things to internalise rather than memorise:

- **$W$ is a change of basis plus a projection.** Row $i$ of $W$ is a vector; $z_i$ is the dot product of that row with the input. So each neuron is asking "how aligned is the input with my template?" A layer with $n_l$ rows asks $n_l$ such questions in parallel.
- **The bias is not a special case.** It's the translation that makes the map affine rather than linear. Some texts fold it in by appending a constant 1 to the input and an extra column to $W$ (the "bias trick"). Worth showing both, because CS231n uses the trick and PyTorch doesn't.

### 1b. The shared toy network, computed by hand

Fix these and publish them to the group before the session:

$$W^{(1)} = \begin{bmatrix} 0.15 & 0.20 \\ 0.25 & 0.30 \end{bmatrix}, \quad b^{(1)} = \begin{bmatrix} 0.35 \\ 0.35 \end{bmatrix}$$
$$W^{(2)} = \begin{bmatrix} 0.40 & 0.45 \end{bmatrix}, \quad b^{(2)} = \begin{bmatrix} 0.60 \end{bmatrix}$$
$$x = \begin{bmatrix} 0.05 \\ 0.10 \end{bmatrix}, \quad y = 1$$

Worked through:

| quantity | computation | value |
|---|---|---|
| $z^{(1)}_1$ | $0.15(0.05) + 0.20(0.10) + 0.35$ | 0.377500 |
| $z^{(1)}_2$ | $0.25(0.05) + 0.30(0.10) + 0.35$ | 0.392500 |
| $a^{(1)}_1$ | $\sigma(0.3775)$ | 0.593270 |
| $a^{(1)}_2$ | $\sigma(0.3925)$ | 0.596884 |
| $z^{(2)}$ | $0.40(0.593270) + 0.45(0.596884) + 0.60$ | 1.105906 |
| $a^{(2)} = \hat{y}$ | $\sigma(1.105906)$ | 0.751366 |
| $C$ | $-\ln(0.751366)$ | 0.285857 |

Those six numbers are the session's shared ground truth. Persons 2–6 all refer back to them, and the gradient check at minute 40 uses them.

### 1c. Shape algebra and batching

The rule that prevents 90% of tensor bugs: **an inner dimension must match and it disappears; the outer dimensions survive.** $(m \times k)(k \times n) \to (m \times n)$.

Column convention (the maths convention, and what Nielsen uses), batch of $N$ stacked as columns:

| tensor | batch = 1 | batch = 32 |
|---|---|---|
| $X$ | (2, 1) | (2, 32) |
| $W^{(1)}$ | (2, 2) | (2, 2) |
| $W^{(1)}X$ | (2, 1) | (2, 32) |
| $b^{(1)}$ | (2, 1) | (2, 1) → broadcast |
| $A^{(1)}$ | (2, 1) | (2, 32) |
| $W^{(2)}$ | (1, 2) | (1, 2) |
| $\hat{Y}$ | (1, 1) | (1, 32) |

Note that **the weight shapes never change with batch size.** That's the whole point: parameters are shared across examples, batch is a free axis.

Then the twist that trips everyone up in real code. PyTorch, TensorFlow and every real framework use the **row convention**: data is `(batch, features)`, and `nn.Linear` stores weight as `(out_features, in_features)` and computes `x @ W.T + b`. So the same maths reads as $A^{(l)} = \sigma(A^{(l-1)}W^{(l)T} + b^{(l)})$ with $A$ of shape (32, 2). Person 1 should show both and state which one the group is using for the rest of the hour. Pick the column convention for the derivations, and just note the transpose when someone opens a PyTorch file.

Broadcasting deserves 60 seconds too: adding a (2,1) bias to a (2,32) matrix is a rank-expansion that costs no real memory, and it's the reason the bias gradient later turns into a sum over the batch axis.

### 1d. Why this is all GEMM

For your audience this is the part that makes it click, because it connects to things they already know about cache and memory bandwidth.

- A layer's forward cost is $2 \cdot n_{out} \cdot n_{in} \cdot N$ FLOPs (one multiply and one add per weight per example).
- Matrix–matrix multiply has arithmetic intensity $O(k)$ — you read $O(n^2)$ data and do $O(n^3)$ work — so it's compute-bound and can saturate a GPU. Matrix–**vector** multiply reads a weight once and uses it once, so it's memory-bound and wastes the hardware. That single fact is why we batch, and why inference on batch size 1 is so much less efficient per example than training.
- Everything else (activations, biases) is elementwise and memory-bound, which is why frameworks fuse those ops into the GEMM epilogue.

Good closing line for person 1: "A forward pass is a sequence of GEMMs separated by cheap elementwise functions. Training is doing that twice in reverse."

## Deliverable checklist

1. The six-number table above, computed by hand, distributed before the session.
2. The shape table for batch 1 and batch 32.
3. A 15-line NumPy implementation reproducing the table exactly, plus the PyTorch equivalent showing the transpose.
4. FLOP count for the toy net, and for a realistic layer (say 4096 → 4096, batch 256) to give a sense of scale.
5. One slide on the two conventions, with the group's chosen one circled.

## Resources

**Start here (a few hours total)**
- **3Blue1Brown, "Neural Networks" chapter 1** — the best 19 minutes on what a layer does geometrically. Chapter 2 covers the loss.
- **Michael Nielsen, *Neural Networks and Deep Learning*, Chapter 1** at neuralnetworksanddeeplearning.com — free, and the section "Implementing our network to classify digits" builds exactly the vectorised forward pass described above. Uses the column convention.
- **Andrej Karpathy, "Neural Networks: Zero to Hero"** — the first two videos (micrograd, then makemore) build a forward pass and autodiff from scratch in plain Python. For developers this is the single highest-value resource on the list; you'll find the notion of a computational graph more natural than the matrix notation, and it sets up person 4's topic perfectly.

**Go deeper**
- **CS231n course notes**, "Neural Networks Part 1: Setting up the Architecture" at cs231n.github.io — the most concise treatment of layer-wise vectorisation and the bias trick. The linked "Linear Algebra for Deep Learning" and backprop notes are both worth reading.
- **Goodfellow, Bengio & Courville, *Deep Learning*** — Chapter 2 for the linear algebra refresher, Chapter 6.1–6.3 for feedforward networks. Free at deeplearningbook.org.
- **Christopher Bishop, *Deep Learning: Foundations and Concepts*** (2023) — Chapter 6. More rigorous and more modern than PRML; the best textbook treatment if someone wants the probabilistic framing early.
- **Matt Mazur, "A Step by Step Backpropagation Example"** — the origin of the weight values above. Arithmetic-heavy and worth having open as a cross-check.

**For the GEMM angle specifically**
- **Gilbert Strang, *Linear Algebra and Learning from Data*** — Chapter 1 on the four ways to view matrix multiplication (row, column, outer product, block). Reframes $Wx$ four different ways, and at least one will click for each person.
- **Siboehm's "How to Optimize a CUDA Matmul Kernel"** blog post — if anyone wants to know what actually happens when they call `torch.matmul`. Optional, but your group will enjoy it.

One warning: skip Goodfellow chapter 6's later sections for now. They branch into architecture design and will pull the discussion away from the derivation chain you're trying to build.