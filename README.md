# 📱 QuizApp – Android Clean Architecture Quiz Application

## 🚀 Overview

A basic quiz application built using **Kotlin** and following **Clean Architecture** principles.  
The app includes three main screens:

- 🔄 **Loader Screen** – Simulates a network call and displays a loading animation.  
- ❓ **Questions Screen** – Displays one question at a time, allows option selection or skipping, and shows streak animations.  
- 🏁 **Result Screen** – Summarizes score, skipped questions, current and highest streaks.

---

## 🧱 Architecture

This project is designed using **Clean Architecture**, split into four layers:

### 1. **App Layer (UI / Presentation Layer)**

- Contains all **Fragments** like:
  - `LoaderFragment`
  - `QuestionsFragment`
  - `ResultFragment`
- Uses `MainViewModel` to:
  - Handle screen transitions.
  - Preserve quiz state (score, streaks, current index, etc.) using **LiveData**.
- UI Features:
  - Option selection with ripple effect.
  - Icons and tint feedback: ✅ for correct, ❌ for incorrect.
  - Confetti animation on streaks of 3, 5, or 10.
  - "Fire emoji" streak animation using animated `TextView`s.
  - Skip button for immediate question change.
  - Enforced **portrait-only orientation**.
  - Scrollable screen for all content using `ScrollView` with `fillViewport=true`.
  - Streak layout (`streakLayout`) visibility toggled without collapsing UI layout.

---

### 2. **Domain Layer**

- Contains **business logic** and **use cases**:
  - `GetListOfQuestionsUseCase`:  
    - Calls repository to fetch questions.
    - Adds a **3-second artificial delay** to simulate network call.
    - **Shuffles** the order of questions.
    - **Shuffles options** within each question.
    - Recalculates and sets the new `correctOptionIndex` based on the shuffled list.
- Defines model:
  - `Question` data class with fields:
    - `id`
    - `question`
    - `options: List<String>`
    - `correctOptionIndex`

---

### 3. **Data Layer**

- Handles data fetching and mapping:
  - `GetAPIResponseRepository`: Abstracts the data source logic.
  - Fetches data from a JSON endpoint.
  - Parses JSON into a list of `Question` objects.
  - Uses a mock/public URL to simulate real API data.

---

### 4. **DI Layer (Dependency Injection)**

- Uses **Koin** to inject dependencies:
  - `MainViewModel` provided as a singleton.
  - Binds `GetAPIResponseRepository` to its implementation.
  - Injects `GetListOfQuestionsUseCase`.

---

## 🌟 Features

- ✅ Clean Architecture with strict separation of concerns.
- ✅ Reactive state handling via LiveData.
- ✅ Shuffled questions and options for every quiz attempt.
- ✅ Real-time visual feedback (green/red + icons).
- ✅ Confetti animations for achieving streaks.
- ✅ Fire emoji streak animation based on count.
- ✅ Auto-skip transition with 2-second delay and circular progress.
- ✅ Fully scrollable and responsive UI.
- ✅ Underlined and styled text titles.
- ✅ Option skipping supported anytime.

---

## App Snapshots
<img width="300" alt="Loader Screen" src="https://github.com/user-attachments/assets/156511f6-750e-4b7c-8ae5-3db5d9daae5e" />  
<img width="300" alt="Quiz Screen" src="https://github.com/user-attachments/assets/702a5a77-4203-4aa8-b27b-2412d63fc5c1" />  
<img width="300" alt="Correct Option Highlighted" src="https://github.com/user-attachments/assets/3f1c6b9e-7b5a-45dd-8d6a-5504bf70a37b" />  
<img width="300" alt="Wrong Option Highlighted" src="https://github.com/user-attachments/assets/af3d04bf-ec17-4165-aeaf-a4f208e61eb5" />  
<img width="300" alt="Result Summary" src="https://github.com/user-attachments/assets/04fbff70-fead-4cae-876e-ece51e03bd87" />  
<img width="300" alt="Question Progress" src="https://github.com/user-attachments/assets/1cde75ee-b279-4011-b02c-c579b3777e84" />  
<img width="300" alt="Better Luck Screen" src="https://github.com/user-attachments/assets/e7fa1d3c-dee1-4dc7-b272-bc843df72412" />  

---

## 📁 Sample JSON Structure

```json
[
  {
    "id": 1,
    "question": "What hidden feature do recent Android versions reveal when you tap the version number multiple times in Settings?",
    "options": [
      "Flappy Bird–style game",
      "Virtual pet",
      "Hidden performance menu",
      "System UI tuner"
    ],
    "correctOptionIndex": 0
  }
]






