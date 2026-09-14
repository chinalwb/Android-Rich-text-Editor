# Repository Guidelines

## Project Structure & Module Organization
This repository is centered on the Android Gradle workspace in `ARE/`. The reusable rich-text editor library lives in `ARE/are/src/main/java/com/chinalwb/are`, with resources in `ARE/are/src/main/res` and bundled assets such as fonts in `ARE/are/src/main/assets`. The demo application is in `ARE/app/src/main`, and project documentation is kept at the repository root (`README.md`, `Usage.md`) plus `docs/`.

Tests follow standard Android source sets inside the library module:
- `ARE/are/src/test/java` for local unit tests
- `ARE/are/src/androidTest/java` for instrumentation tests

## Build, Test, and Development Commands
Run Gradle commands from `ARE/`.

- `./gradlew assemble` builds both the `app` and `are` modules.
- `./gradlew :app:assembleDebug` builds the demo APK for local device testing.
- `./gradlew :are:build` compiles the library, runs unit tests, and packages outputs.
- `./gradlew :are:testDebugUnitTest` runs JVM unit tests in `ARE/are/src/test/java`.
- `./gradlew :are:connectedAndroidTest` runs instrumentation tests on a connected emulator or device.
- `./gradlew lint` runs Android lint checks for the workspace.

## Coding Style & Naming Conventions
Follow the existing Java-first code style: 4-space indentation, braces on the same line, and descriptive class names such as `AREditor`, `ARE_ToolbarDefault`, and `ARE_ToolItem_Bold`. Keep package names under `com.chinalwb.are`. Match the current file naming pattern when adding styles, spans, tool items, or demo activities. Kotlin is used sparingly in the demo app; keep new Kotlin code consistent with existing Android conventions and avoid mixing style patterns within one file.

## Testing Guidelines
Add or update unit tests for logic changes in the library module, especially HTML conversion, span handling, and toolbar behavior. Reserve instrumentation tests for Android framework interactions. Name tests after the behavior they verify, for example `HtmlParsingTest` or `toolbarSelectionUpdatesState`.

## Commit & Pull Request Guidelines
Recent commits use short, imperative summaries such as `Fix crash when casting class extending activity` and `Allow setting background color for ColorPickerWindow`. Keep commits focused and easy to scan. Pull requests should include:
- a brief problem/solution summary
- affected modules (`are`, `app`, docs)
- test evidence or the exact Gradle command run
- screenshots or GIFs for toolbar, rendering, or demo UI changes
