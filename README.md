# Crypto Cash — Android

**Send, receive, buy, and sell cryptocurrency — as easy as sending a text.**

Crypto Cash is a mobile-first crypto wallet and payments app for Android (API 26+). It brings together the simplicity of Cash App with the power of a full crypto exchange.

---

## ✨ Core Features

| Feature | Description |
|---|---|
| **Buy Crypto** | Purchase BTC, ETH, LTC, SOL, USDC with any linked payment method. A transparent 1.75% fee is shown before you confirm. |
| **Sell Crypto** | Convert crypto to cash, deposited to any linked bank account or card. |
| **Send Crypto** | Send to any wallet address. Live fiat equivalent shown while typing. Confirmation screen before submitting. |
| **Deposit Cash** | Free ACH (1–3 days) or Instant Deposit (1.5% fee) — you choose the speed. |
| **Payment Methods** | Link **unlimited** bank accounts, debit cards, and credit cards. Set a default, add or remove at any time. |
| **Transaction History** | Full log of buys, sells, sends, receives, and deposits. |
| **KYC Verification** | 4-step guided identity flow to unlock higher limits. |

---

## 💰 Monetization (No Subscription Required)

- **1.75% transaction spread** on every buy/sell (embedded in price)
- **1.5% instant deposit fee** vs free standard ACH
- **Interchange revenue** from a future branded debit card
- **Referral program** — users earn BTC for each referral
- **Yield product** — earn interest on idle USDC balances
- **Business merchant accounts** — flat-fee crypto payment acceptance

---

## 🛡️ Security & Privacy

- Biometric authentication (fingerprint / face unlock)
- 6-digit PIN fallback with SHA-256 hashing
- 5-minute session timeout
- TLS-only networking (`network_security_config.xml`)
- No cleartext traffic allowed

---

## ♿ Accessibility

- Full TalkBack support (content descriptions on all interactive views)
- WCAG AA color contrast (Material 3 tokens)
- Dynamic font size (`sp` units throughout)
- Haptic feedback on key actions (click, success, error)
- RTL layout support (`android:supportsRtl="true"`)

---

## 🌍 Localization

App is fully localized in **5 languages**:
- 🇺🇸 English (default)
- 🇪🇸 Spanish (`values-es`)
- 🇫🇷 French (`values-fr`)
- 🇧🇷 Portuguese (`values-pt`)
- 🇮🇳 Hindi (`values-hi`)

---

## 🏗️ Architecture

```
app/
├── onboarding/     3-screen walkthrough + guest/demo mode
├── auth/           Biometric, PIN setup/verify, session management
├── home/           Balance overview (crypto + fiat), MVVM
├── buy/            Buy crypto with fee preview
├── sell/           Sell crypto to any linked account
├── send/           Send to wallet address with confirmation screen
├── deposit/        Standard ACH & Instant Deposit flows
├── payment/        Multi-account/card management (unlimited)
├── history/        Transaction list with RecyclerView
├── kyc/            4-step identity verification
├── notifications/  FCM push + per-channel preferences
├── data/
│   ├── local/      Room database (transactions, balances)
│   └── repository/ WalletRepository with retry/backoff
└── util/           CurrencyFormatter, HapticHelper, NetworkHelper
```

**Stack:** Kotlin · Material Design 3 · Navigation Component · Room · DataStore · Biometric · WorkManager · Firebase Cloud Messaging · Retrofit · Coroutines

---

## 🚀 Getting Started

1. Clone the repo
2. Open in Android Studio Hedgehog or later
3. Add your `google-services.json` to `app/` (for FCM push notifications)
4. Build & run on API 26+ device or emulator

```bash
./gradlew assembleDebug
```

---

## 📱 Supported Devices

- Android 8.0+ (API 26) — covers ~95% of active Android devices
- Phones, tablets, and foldables
- Portrait and landscape
- Notch / edge-to-edge displays
- Dark mode

---

## 🤝 Contributing

Contributions, suggestions, and improvements are welcome. If you plan to expand the project, keep documentation up to date as new features and setup steps are introduced.

