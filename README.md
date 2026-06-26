# Crypto Cash Android

Crypto Cash Android is a mobile payment application inspired by the simplicity of Cash App, focused on crypto-based money movement. The goal is a clean, intuitive Android experience for sending, receiving, and managing digital payments.

## Overview

The app targets Android users who want a straightforward interface for crypto payments — no exchange complexity, just peer-to-peer transfers with a familiar mobile UX.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM + Repository pattern |
| Async | Kotlin Coroutines + Flow |
| DI | Hilt |
| Networking | Retrofit + OkHttp |
| Local storage | Room |
| Build | Gradle (Kotlin DSL) |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 34 (Android 14) |

## Planned Project Structure

```
app/
├── src/main/
│   ├── java/com/cryptocash/
│   │   ├── data/
│   │   │   ├── local/        # Room DAOs, entities, database
│   │   │   ├── remote/       # API service interfaces, DTOs
│   │   │   └── repository/   # Repository implementations
│   │   ├── domain/
│   │   │   ├── model/        # Core business models
│   │   │   └── usecase/      # Business logic use cases
│   │   ├── ui/
│   │   │   ├── home/         # Balance dashboard screen
│   │   │   ├── send/         # Send payment flow
│   │   │   ├── receive/      # Receive / QR code screen
│   │   │   ├── history/      # Transaction history
│   │   │   └── settings/     # User settings
│   │   └── di/               # Hilt modules
│   └── res/
│       ├── values/           # Strings, colors, themes
│       └── drawable/         # Icons, vector assets
└── build.gradle.kts
```

## Core Features (Planned)

### Send Payment
- Enter recipient address or scan QR code
- Enter amount in crypto or fiat equivalent
- Review and confirm transaction with fee estimate
- Real-time status tracking after submission

### Receive Payment
- Generate wallet address with QR code
- Optional amount request with deep-link sharing

### Transaction History
- Chronological list of sent and received transactions
- Filter by status (pending, confirmed, failed)
- Transaction detail view with on-chain reference

### Balance Dashboard
- Current balance in crypto and local fiat
- Price chart (24h / 7d / 30d)
- Quick-access send and receive buttons

## Development Setup

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK with API level 34 platform tools

### Getting Started

```bash
# Clone the repository
git clone https://github.com/jaimesorrow/chat.git
cd chat

# Open in Android Studio and let Gradle sync complete
# Then run on an emulator or physical device (API 26+)
```

### Environment Configuration

Create `local.properties` in the project root (not committed):

```properties
sdk.dir=/path/to/your/Android/sdk
API_BASE_URL=https://api.example.com
```

## Architecture

The app follows **Clean Architecture** layered as:

```
UI (Compose screens + ViewModels)
        ↓
Domain (Use cases + models)
        ↓
Data (Repositories → Remote API + Local DB)
```

- **ViewModels** expose `StateFlow<UiState>` consumed by Compose screens.
- **Use cases** encapsulate single business operations and are injected into ViewModels.
- **Repositories** abstract data sources; the domain layer never imports Retrofit or Room directly.
- **Hilt** wires dependencies at compile time with no manual DI boilerplate.

## Contributing

Contributions, suggestions, and improvements are welcome.

- Keep documentation up to date as new features and setup steps are introduced.
- Follow the existing architecture: new screens go in `ui/`, new business logic in `domain/usecase/`.
- Write unit tests for use cases and repository logic; write UI tests with Compose Testing for critical flows (send, receive).
- Use conventional commits: `feat:`, `fix:`, `docs:`, `refactor:`, `test:`.

## Project Status

Early stage — repository structure and documentation are being established. Source code will be added as development progresses.
