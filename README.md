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
| 💱 **Multi-Currency Support** | Select from USD `$`, INR `₹`, GBP `£`, EUR `€`, JPY `¥`, NPR `रू` |
| 🌙 **Dark Mode** | Toggle dark mode with aesthetic color picker (Cyan, Emerald, Amber, Crimson) |
| 💾 **Data Persistence** | Inputs are auto-saved via `SharedPreferences` and restored on next launch |
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
    │                                    ├── Profit/Loss bar chart
    │                                    ├── EMI display
    │                                    ├── AI advisory text
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
2. **Fill in the input fields** on the main screen:

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

3. **Select your currency** from the spinner (top of screen).
4. **Tap Calculate** to view the full financial report in `ResultActivity`.
5. **Tap Profit Analysis / Loss Analysis** to see a chart preview on the main screen and generate a detailed PDF.
6. **In ResultActivity**, tap **Download PDF** to export and share the report.

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

| Condition | Prediction |
|---|---|
| milkPerDay > 15 AND expense < 30,000 | 🟢 High yield farm — Very profitable |
| milkPerDay > 10 | 🟡 Average growth — Improve feeding strategy |
| Otherwise | 🔴 Low productivity — Consider better cow breed |

---

## 📄 PDF Report Generation

Two PDF generators are present:

### 1. `MainActivity.generateDetailedPDF()` — Triggered by Analysis buttons
- **Page 1**: Business parameters, year-wise forecast table with neon bars, AI advisory, QR code stamp, footer with timestamp and report ID
- **Multi-page support**: Automatically starts a new page if content overflows past Y=720

### 2. `ResultActivity.generateReportPDF()` — Triggered by Download PDF button
- Embeds the **live bar chart bitmap** directly into the PDF
- Includes multi-line AI advisory using `StaticLayout`
- Shares via Android's `ACTION_SEND` intent (WhatsApp, Gmail, Drive, etc.)

---

## 🐛 Bug Fixes Applied

The following bugs were identified and fixed (all existing features preserved):

| # | File | Bug | Fix |
|---|---|---|---|
| 1 | `LoanCalculator.java` | **Critical crash**: When `loan=0` or `rate=0`, the EMI formula denominator became `0`, causing `NaN` / `Infinity` | Added early return `0.0` when loan ≤ 0, rate ≤ 0, or months ≤ 0 |
| 2 | `PredictionHelper.java` | **Mixed-language text**: Arabic word `متوسط` appeared in an English string (copy-paste error) | Replaced with English `"Average"` |
| 3 | `ResultActivity.java` | **Invisible text**: Profit label color was `Color.WHITE` on a potentially light background | Changed to `#4CAF50` (green) — consistent with chart's profit color |
| 4 | `MainActivity.java` | **Dead code**: `canvas.drawText("", ...)` drew nothing and was misleading | Removed and replaced with a descriptive comment |

---

## 👨‍💻 Author

**Goutam Gupta**
Goutam Computer Institute

> *"Built with passion to empower dairy farmers with smart business intelligence."*

---

## 📜 License

This project is for educational and personal use.
© 2024 Goutam Gupta — Goutam Computer Institute. All rights reserved.
