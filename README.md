# WordPuzzle

A modern Android word puzzle game built with **Jetpack Compose**. Players swipe between letters on a wheel to form words and fill a crossword-style grid.

## Features

- **Interactive Letter Wheel**: Smooth touch-based selection of letters with visual connections.
- **Crossword Grid**: A dynamic grid that reveals letters as you find the correct words.
- **Level System**: Progression through multiple levels, each with increasing difficulty.
- **Animations**: Uses Compose animations for letter selection, feedback, and grid reveals.
- **MVVM Architecture**: Built using `ViewModel`, and a repository pattern.
- **JSON Based Levels**: Levels are easily configurable via a central JSON file.

## Project Structure

- `ui/components/`: Reusable Compose components like `LetterWheel` and `CrosswordGrid`.
- `ui/screen/`: Main screen implementations (`GameScreen`, `LevelSelectScreen`, `HomeScreen`).
- `viewmodel/`: Game logic and state management using `GameViewModel`.
- `data/`: Data models and `LevelRepository` for loading game levels.
- `navigation/`: Navigation graph defining the app's flow.

## Getting Started

1. Clone the repository.
2. Open in **Android Studio Ladybug** (or newer).
3. Sync Gradle and run the `:app` module.

## Tech Stack

- **Kotlin**
- **Jetpack Compose** (UI)
- **ViewModel & StateFlow** (State Management)
- **Navigation Compose** (App Navigation)
- **Gson** (JSON Parsing)
- **Coroutines** (Asynchronous tasks)
- 
## Download

You can download the latest preview version of the app here:
- [Google Drive](https://drive.google.com/drive/folders/1hMLA4_tsNFFVREmMSiVm6MqnqSqLCPT8?usp=sharing)

