# KYLR - Secure UPI Payments Ecosystem

KYLR is a fully functional, high-fidelity UPI payments application built with a modern Android stack. It adheres to NPCI guidelines for device binding and transaction flows while providing an editorial, high-end user experience inspired by top-tier financial dashboards.

<img width="1774" height="887" alt="image" src="https://github.com/user-attachments/assets/fbf514d3-5275-466e-98b2-d72200fab5b8" />


## 🚀 Features

- **KYLR Vault™**: A proprietary, high-security transaction pipeline that handles settlements, balance management, and ledger updates.
- **Smart Onboarding**: Seamless mobile verification and device binding flow.
- **Bank Connectivity**: Support for multiple banks with a deep-link account discovery system.
- **Editorial Dashboard**: A Bento-style home screen featuring gradient cards, real-time balance tracking, and quick actions.
- **Precision Payments**: A custom numeric keypad and reactive search for contacts to ensure fast and error-free transfers.
- **High-Fidelity UI**: Built entirely using Jetpack Compose and Material 3, featuring tonal layering, dynamic states, and modern typography (Plus Jakarta Sans & Manrope).

## 🛠 Tech Stack

- **UI Framework**: Jetpack Compose (Material 3)
- **Language**: Kotlin
- **Navigation**: Navigation Compose
- **Backend Architecture**: State-driven pipeline via `KylrVault`
- **State Management**: Kotlin Flows & StateFlow
- **Architecture Pattern**: Clean Architecture with functional decomposition
- **Icons**: Material Symbols Outlined & Extended

## 📂 Project Structure

```text
com.example.kylr/
├── data/
│   ├── backend/        # KYLR Vault (The core transaction engine)
│   └── model/          # Domain models (Transaction, Bank, Contact)
├── ui/
│   ├── navigation/     # Central NavGraph and Screen definitions
│   ├── theme/          # Custom M3 Design System (Colors, Type, Theme)
│   └── screens/        # High-fidelity hubs (Home, Payments, SendMoney, etc.)
└── MainActivity.kt     # App entry point with Compose setContent
```

## 🔐 Compliance & Security

- **Device Binding**: The app is architected to require a unique link between the physical SIM/hardware and the user profile, as per NPCI requirements.
- **Vault Logic**: All transactions are processed through an isolated pipeline (`KylrVault`) that performs atomic updates to prevent balance inconsistencies.
- **M3 Tonal Layering**: Uses the latest Material 3 surface containers to provide a secure and clear visual hierarchy.

## 🚦 Getting Started

1. **Clone the repository.**
2. **Open in Android Studio Ladybug (or newer).**
3. **Sync Gradle**: The project uses Version Catalogs (`libs.versions.toml`) for dependency management.
4. **Run**: Deploy to an emulator or device running API 24+.

---
Developed as a robust, maintainable foundation for the future of digital payments.
