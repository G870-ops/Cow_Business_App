# 🐄 Cow Business — AI-Powered Dairy Farm Profit Calculator

> **An advanced Android application to calculate, forecast, and analyze dairy farm profitability with AI-driven predictions, loan EMI integration, and PDF report generation.**

---

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [App Flow](#app-flow)
- [Screenshots](#screenshots)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [How to Use](#how-to-use)
- [Business Logic](#business-logic)
- [PDF Report Generation](#pdf-report-generation)
- [Bug Fixes Applied](#bug-fixes-applied)
- [Author](#author)

---

## 🌟 Overview

**Cow Business** is a smart Android application designed for dairy farmers and agribusiness investors. It takes your farm's financial data as input and delivers:

- Net **profit/loss** calculations over any number of years
- Monthly **EMI** calculation for farm loans
- AI-based **business predictions and suggestions**
- Interactive **bar charts** for visual financial analysis
- **PDF report export** with year-wise forecasts and AI advisories

---

## ✨ Features

| Feature | Description |
|---|---|
| 🐄 **Profit Calculator** | Calculates total net profit/loss based on cows, milk yield, price, expenses, and loan |
| 🏦 **Loan EMI Integration** | EMI computed using the standard reducing-balance formula |
| 📊 **Interactive Bar Charts** | Year-wise profit/loss forecast rendered using MPAndroidChart |
| 🤖 **AI Prediction Engine** | Rule-based AI suggestions for improving farm productivity |
| 📄 **PDF Report Export** | Multi-page PDF with header, year-wise table, AI advisory, QR stamp, and share support |
| 💱 **Multi-Currency Support** | 32+ global currencies (USD `$`, INR `₹`, EUR `€`, GBP `£`, JPY `¥`, NPR `रू`, BDT `৳`, PKR `₨`, AED `د.إ`, SAR `﷼`, CAD `C$`, AUD `A$`, CHF, RUB `₽`, KRW `₩`, TRY `₺`, BRL `R$`, ZAR `R`, etc.) with auto-save & restore |
| 🌙 **Dark Mode** | Toggle dark mode with aesthetic color picker (Cyan, Emerald, Amber, Crimson) |
| 💾 **Data Persistence** | Inputs and selected currency are auto-saved via `SharedPreferences` and restored on next launch |
| ✨ **Splash Screen** | Animated splash with glitch effect, typing animation, and fade transitions |
| 🔄 **Reset with Confirmation** | AlertDialog confirmation before wiping all local data |

---

## 🔀 App Flow

```
SplashActivity (3.8s)
    │
    ▼  (fade transition)
MainActivity
    │
    ├──► [Calculate] ──────────────► ResultActivity
    │                                    │
    │                                    ├── Profit/Loss multi-year bar chart
    │                                    ├── Monthly EMI display
    │                                    ├── AI advisory text with currency
    │                                    └── [Download PDF] → share via intent
    │
    ├──► [Profit Analysis] ─────────► Bar chart preview + PDF generation
    │
    └──► [Loss Analysis] ───────────► Bar chart preview + PDF generation
```

---

## 📱 Screenshots

> _Add screenshots here by placing images in `/docs/screenshots/` and updating the paths below._

| Splash Screen | Main Screen | Result Screen |
|:---:|:---:|:---:|
| _(Add screenshot)_ | _(Add screenshot)_ | _(Add screenshot)_ |

---

## 🛠 Tech Stack

| Technology | Purpose |
|---|---|
| **Java** | Primary programming language |
| **Android SDK 36** | Target platform (min SDK 24 / Android 7.0+) |
| **AndroidX AppCompat** | Backward-compatible UI components |
| **Material3 (DayNight)** | Theme system with dark mode support |
| **MPAndroidChart v3.1.0** | Bar charts for financial visualization |
| **Android PdfDocument API** | Built-in PDF generation (no external lib needed) |
| **FileProvider** | Secure file sharing for PDF export |
| **SharedPreferences** | Lightweight local data persistence |
| **Handler + Runnable** | Animations (neon glow, typing effect, glitch) |

---

## 📁 Project Structure

```
CowBusiness/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/cowbusiness/
│   │       │   ├── SplashActivity.java       # Launch screen with animations
│   │       │   ├── MainActivity.java         # Core input & analysis screen
│   │       │   ├── ResultActivity.java       # Results display & PDF download
│   │       │   ├── BusinessCalculator.java   # Profit/loss calculation logic
│   │       │   ├── LoanCalculator.java       # EMI calculation (reducing balance)
│   │       │   └── PredictionHelper.java     # AI-style farm prediction logic
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   ├── activity_splash.xml
│   │       │   │   ├── activity_main.xml
│   │       │   │   └── activity_result.xml
│   │       │   ├── drawable/                 # App logos, backgrounds, QR code
│   │       │   ├── anim/                     # Glitch animation XML
│   │       │   ├── values/
│   │       │   │   ├── strings.xml
│   │       │   │   ├── colors.xml
│   │       │   │   └── themes.xml
│   │       │   └── xml/
│   │       │       └── provider_paths.xml    # FileProvider path config
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

---

## ⚙️ Installation & Setup

### Prerequisites
- **Android Studio** Ladybug (2024.2+) or newer
- **JDK 11** or higher
- **Android device/emulator** running API 24 (Android 7.0) or above

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/CowBusiness.git
   cd CowBusiness
   ```

2. **Open in Android Studio**
   - File → Open → Select the `CowBusiness` folder

3. **Sync Gradle**
   - Android Studio will automatically prompt to sync. Click **Sync Now**.
   - The `MPAndroidChart` library is fetched from JitPack — ensure you have JitPack in your `settings.gradle`:
     ```groovy
     dependencyResolutionManagement {
         repositories {
             google()
             mavenCentral()
             maven { url 'https://jitpack.io' }   // Required for MPAndroidChart
         }
     }
     ```

4. **Build & Run**
   - Connect a device or start an emulator
   - Click ▶️ **Run** (Shift+F10)

---

## 📖 How to Use

1. **Launch the app** — the animated splash screen initializes for ~3.8 seconds.
2. **Select your currency** from the spinner (32 world currencies supported, e.g. `$`, `₹`, `€`, `£`, `¥`, `रू`, `৳`, `₨`, `AED`, `﷼`, `C$`, `A$`, `CHF`).
3. **Fill in the input fields** on the main screen:

   | Field | Description |
   |---|---|
   | Number of Cows | Total cows in your dairy farm |
   | Cost per Cow | Purchase price per cow |
   | Milk per Cow/Day | Daily milk yield per cow (litres) |
   | Milk Price/Litre | Current selling price per litre |
   | Monthly Expense | Total operational cost per month |
   | Years | Number of years to forecast |
   | Loan Amount | *(Optional)* Total loan taken |
   | Interest Rate (%) | *(Optional)* Annual interest rate |

4. **Tap Calculate** to view the full financial report and multi-year projection in `ResultActivity`.
5. **Tap Profit Analysis / Loss Analysis** to see a chart preview on the main screen and generate a detailed PDF.
6. **In ResultActivity**, tap **GENERATE PDF REPORT** to export and share the report with live charts and full breakdown.

---

## 🧮 Business Logic

### `BusinessCalculator.calculateProfit()`

```
Net Profit = Total Revenue - (Initial Investment + Total Operating Expenses + Total Loan Interest)

Where:
  Total Revenue         = (cows × milkPerCow × milkPrice × 365) × years
  Initial Investment    = cows × costPerCow
  Total Operating Cost  = monthlyExpense × 12 × years
  Total Loan Interest   = (EMI × totalMonths) - loanPrincipal
```

### `LoanCalculator.calculateEMI()`

Standard **Reducing-Balance EMI Formula**:

```
EMI = P × r × (1 + r)^n
      ─────────────────
         (1 + r)^n − 1

Where:
  P = Principal loan amount
  r = Monthly interest rate  (annualRate / 12 / 100)
  n = Total months
```
> Returns `0.0` safely when loan = 0, rate = 0, or months = 0.

### `PredictionHelper.predict()`

Dynamic AI strategic predictions tailored to cow count, milk yield, expenses, net profit/loss, and the selected currency symbol.

---

## 📄 PDF Report Generation

Two PDF generators are present:

### 1. `MainActivity.generateDetailedPDF()` — Triggered by Analysis buttons
- **Page 1**: Business parameters, year-wise forecast table with neon bars, AI advisory, QR code stamp, footer with timestamp and report ID
- **Multi-page support**: Automatically starts a new page if content overflows past Y=720

### 2. `ResultActivity.generateReportPDF()` — Triggered by Download PDF button
- Embeds the **live bar chart bitmap** directly into the PDF
- Includes multi-line AI advisory using `StaticLayout`
- Includes full breakdown of herd size, daily yield, operational expenses, and loan commitments
- Shares via Android's `ACTION_SEND` intent (WhatsApp, Gmail, Drive, etc.)

---

## 🐛 Bug Fixes & Improvements Applied

All existing features and data have been strictly preserved while resolving calculation bugs and adding full currency support:

| # | Component | Issue | Solution |
|---|---|---|---|
| 1 | `performFinancialAnalysis` | **Omitted Herd Count & Cow Cost**: In `MainActivity`, revenue was calculated without multiplying by `totalCows`, and cow purchase cost was completely omitted. It also used 360 days instead of 365. This caused a 10x-30x calculation error and false "Analysis Mismatch: Loss" alerts. | Unified logic with `BusinessCalculator.calculateProfit`, calculating exact total cows, milk yield, 365 days, initial investment, and accurate net profit/loss. |
| 2 | `LoanCalculator.java` & `BusinessCalculator.java` | **Division-by-Zero Risk**: EMI formula could encounter `0` denominator when rate or loan was 0. | Added safety guard and delegated loan interest calculation in `BusinessCalculator` to `LoanCalculator.calculateEMI`. |
| 3 | `Multi-Currency Support` | **Limited Currencies & No Persistence**: Previously only 6 codes were listed and currency selection was not saved across app restarts. | Added 32 world currency options (`$`, `₹`, `€`, `£`, `¥`, `रू`, `৳`, `₨`, `د.إ`, `﷼`, `C$`, `A$`, `CHF`, `₽`, `₩`, etc.), formatted currency across UI and PDFs, and persisted selected currency in `SharedPreferences`. |
| 4 | `PredictionHelper.java` | **Unused & Currency-Blind**: AI prediction was hardcoded for fixed amounts without considering chosen currency or net profit. | Added currency-aware overloaded `predict()` method integrating herd size, yield, expenses, net profit, and currency symbol. |
| 5 | `ResultActivity.java` | **Single-Bar Chart**: Chart only displayed a single bar. | Updated to render multi-year cumulative projection bars with dynamic currency labeling. |
| 6 | **Formatting & Validations** | **Zero/Negative Years & Missing Separators**: Forecast years could be 0; large numbers lacked thousand separators. | Added validation ensuring positive cow count and years (≥1), and formatted all currency amounts with commas (`%,.2f`). |

---

## 👨‍💻 Author

**Goutam Gupta**
Goutam Computer Institute

> *"Built with passion to empower dairy farmers with smart business intelligence."*

---

## 📜 License

This project is for educational and personal use.
© 2024 Goutam Gupta — Goutam Computer Institute. All rights reserved.
