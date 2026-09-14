# Toolbar icon set

The icons in `ARE/are/src/main/res/drawable/are_ic_*.xml` are generated, and the
generated XML is what the build uses — you do not need these scripts to build the
project. They are here so the set can be extended without hand-drawing paths.

Everything is drawn on a 24dp grid with a 20dp live area and a single weight, and
the letterforms are real glyph outlines so `B`, `I`, `U`, `S`, `A` and the list
digits all share one typeface.

## Regenerating

    python3 -m venv .venv
    .venv/bin/pip install fonttools
    .venv/bin/python icons.py

`glyphs.py` reads Arial from `/System/Library/Fonts/Supplemental`, so regenerating
currently needs macOS. Point `FONTS` at any other grotesque to use a different
typeface — the whole set changes together, which is the point.

## Adding an icon

Add a `write(...)` call in `icons.py`, run it, then check the result on a device:

    ./gradlew :are:connectedDebugAndroidTest \
        -Pandroid.testInstrumentationRunnerArguments.class=com.chinalwb.are.IconSheetTest
    adb pull /sdcard/Android/data/com.chinalwb.are.test/files/icon_sheet.png

`IconSheetTest` fails if an icon cannot be inflated, and writes a contact sheet of
the whole set so it can be looked at side by side.
