---
name: crypto-cash-review
description: Reviews changes to Crypto Cash Android, a not-yet-scaffolded Android client for a Cash-App-style crypto payment product (per README.md). Use this instead of a generic code review for the first commits that add a Gradle/Android project, any module handling money amounts, balances, or transaction sending, and any change to README.md's stated scope — until real source exists, apply it as a pre-flight checklist rather than a diff-vs-invariants review.
---

# Crypto Cash Android review

## Current state of this repo (verify this hasn't changed before assuming otherwise)

As of this skill's writing, the repository contains **only `README.md` and `.gitignore`** — no
`build.gradle(.kts)`, no `settings.gradle`, no `app/` module, no Kotlin/Java source, no CI config.
The `.gitignore` is pre-populated for a standard Android Studio/Gradle project (`.gradle/`,
`local.properties`, `*.jks`/`*.keystore`, `google-services.json`, `*.hprof`), so the intended stack
is native Android with Gradle, but nothing has been scaffolded yet. **Before applying the checks
below, run `git log --oneline -20` and `find . -not -path './.git*' -type f` to confirm whether
real source has landed since**, and if so, re-derive the actual package/module layout instead of
assuming these guesses.

From `README.md`, the stated product intent is: "a clean mobile experience for sending, receiving,
and managing digital payments," explicitly "crypto-focused," modeled loosely on Cash App. Nothing
in the repo yet specifies which chains/tokens, custody model (custodial vs. self-custody wallet),
or backend the app talks to — do not let a PR quietly bake in an assumption about any of these
without it being an explicit, visible decision (e.g., in the PR description or a new
CLAUDE.md/architecture doc), since this is a payments app and that choice has real security
consequences.

## What to check on a PR that scaffolds the project (first `build.gradle`/module structure)

- Package/namespace and `minSdk`/`compileSdk`/`targetSdk` choices should be stated deliberately in
  the PR, not left at whatever template defaults; flag if they're unexplained.
- Confirm `google-services.json`, `*.jks`/`*.keystore`, and `local.properties` are actually excluded
  from what's staged (the `.gitignore` already lists them — check `git status`/`git diff --stat` on
  the PR itself, since a contributor can still `git add -f` past a gitignore entry).
- If the PR introduces a backend/API client or wallet SDK dependency, check whether it also
  introduces the first place a private key, seed phrase, or API secret could end up — flag any of
  these appearing in source, resources, `local.properties` being read into `BuildConfig`, or logs.

## What to check once money/transaction logic starts appearing

These are not yet-implemented invariants to enforce against existing code (there is none) — treat
them as the specific risks this PR is introducing for the first time, and check the new code
against them directly:

- **No `Float`/`Double` for any monetary or token amount.** A "send/receive/manage digital
  payments" app doing arithmetic on floating-point currency or crypto amounts is a correctness bug
  from the first line — look for `BigDecimal`/integer-minor-units (e.g. satoshis/wei-equivalent)
  instead, and flag any `Float`/`Double` field or parameter that represents a balance, price, fee,
  or transfer amount.
- **Balance/ledger updates must not double-apply.** Since the product is literally "sending,
  receiving, managing" money, any function that debits/credits a balance on a send or receive event
  needs an idempotency guard (e.g. a transaction ID check before applying) — flag any such function
  that has no protection against being invoked twice for the same event (retry, duplicate webhook,
  re-composition in Compose, etc.).
- **Key/secret handling for a crypto wallet.** If a private key, seed phrase, or mnemonic is
  introduced, it must not be stored in plain `SharedPreferences`, a Room column, or any
  non-hardware-backed store — flag anything short of Android Keystore-backed encryption or an
  equivalent vetted wallet SDK.
- **No unexplained network destination for a payment amount, address, or key material.** Given the
  security profile of a payments app, any new outbound call carrying an amount, wallet address, or
  key/secret should have an obvious, reviewable destination (a documented backend endpoint) rather
  than reaching a new host with no accompanying explanation.

## What NOT to do

Don't invent or enforce architecture this repo hasn't actually adopted (no MVVM/Compose/Hilt/Room
conventions exist yet to hold a PR to) — a generic review pass for ordinary bugs is still
appropriate on top of the above; just don't claim a specific pattern is "the convention here" until
it's been introduced and used more than once.
