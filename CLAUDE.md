# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository layout

The Gradle workspace is `ARE/`, not the repo root. All Gradle commands must be run from `ARE/`.

- `ARE/are` — the publishable library module (`com.chinalwb.are`, Java only, AGP 8.5.2, minSdk 21 / compileSdk 34, published as `com.github.chinalwb:are`).
- `ARE/app` — the demo app (`com.chinalwb.are.demo`, Java + a little Kotlin, viewBinding enabled). Launcher activity is `IndexActivity`.
- `docs/sdk-production-roadmap.md` — the current plan for hardening the library into a real SDK; check it before large refactors so changes align with the intended direction.
- `AGENTS.md` — repository conventions (commit style, PR expectations); its guidance applies here too.

Requires JDK 17 (AGP 8.5.2). `ARE/local.properties` holds `sdk.dir` and is machine-specific.

## Commands

```bash
cd ARE
./gradlew assemble                 # build both modules
./gradlew :app:assembleDebug       # demo APK
./gradlew :are:build               # compile library + unit tests + package
./gradlew :are:testDebugUnitTest   # JVM unit tests
./gradlew :are:connectedAndroidTest# instrumentation tests (needs device/emulator)
./gradlew lint                     # lint (are module has abortOnError false)

# single test class / method
./gradlew :are:testDebugUnitTest --tests "com.chinalwb.are.UtilPathResolutionTest"
./gradlew :are:testDebugUnitTest --tests "com.chinalwb.are.UtilPathResolutionTest.buildExternalStorageDocumentPath_returnsMatchingVolumePath"
```

JVM unit tests cover logic that can be expressed without framework types (the `Util` paragraph and path helpers). Everything span- or view-shaped needs instrumentation: `SpannableStringBuilder`, `Layout` and `Canvas` all return stubs under the mockable android.jar. Instrumentation tests build editors on `TestHostActivity` (declared in `src/androidTest/AndroidManifest.xml`) — `AREditText` resolves its padding via `Util.getPixelByDp`, which walks the context chain up to an `Activity`, so an application context is not enough.

## Architecture

Everything is built on Android `Spanned`/`Editable` and `android.text.style` spans — there is no WebView and no custom text layout.

### The core loop

`AREditText extends AppCompatEditText` holds a list of `IARE_Style`. Its `TextWatcher.afterTextChanged` calls `style.applyStyle(editable, start, end)` on every registered style for the changed range; `onSelectionChanged` fans out to `IARE_ToolItem.onSelectionChanged` so toolbar buttons update their checked state. So: **a style is only active if it was registered on the AREditText**, via either `setToolbar(IARE_Toolbar)` (pulls `getStyle()` off each tool item) or `setFixedToolbar(ARE_Toolbar)` (pulls `getStylesList()`).

Block-level styles (list, quote, alignment, indent) all resolve their range through `Util.getCurrentCursorLine` / `getThisLineStart` / `getThisLineEnd`. Those are **paragraph** boundaries computed from the text, not `Layout` lines — a `Layout` line is a *visual* line, so reading boundaries off it styled only the fragment of a wrapped paragraph that fit on screen, and returned stale offsets right after an edit (the layout has not been rebuilt yet). The underlying `Util.getParagraph{Index,Start,End,Count}(CharSequence, int)` helpers are pure and unit-tested; `getThisLineEnd` includes the trailing `\n`, matching `Layout#getLineEnd`.

Ordered-list numbers are display-only state, recomputed for the whole document by `ARE_ListNumbering.renumber(Editable)` rather than patched span by span (`Editable#getSpans` returns spans in insertion order, never document order). `reNumberBehindListItemSpans` is kept as a thin delegating wrapper. Numbering restarts at 1 whenever a non-ordered paragraph — plain text or a bullet — interrupts the run.

List items carry a leading `Constants.ZERO_WIDTH_SPACE_STR` marker so an empty item still has a line to draw on; anything that stops a line being a list item has to delete that marker too (`Util.removeZeroWidthMarker`), or it accumulates invisibly and lands in the exported HTML.

`ARE_ABS_Style<E>` is the generic base for character styles. It reflects the span class `E` out of its generic superclass and implements the full checked/unchecked × insert/delete/select matrix of span splitting and merging. New character styles should extend it and implement `newSpan()` rather than hand-rolling span math.

### Two parallel style hierarchies — know which one you are editing

This is the single biggest trap in the codebase. Each feature exists twice:

| | Fixed toolbar | Composable toolbar |
|---|---|---|
| Toolbar | `styles/toolbar/ARE_Toolbar` (LinearLayout, inflates `are_toolbar.xml`, hardcodes every style) | `styles/toolbar/ARE_ToolbarDefault` (HorizontalScrollView, implements `IARE_Toolbar`) |
| Style impl | `styles/ARE_Bold`, `ARE_Italic`, … | `styles/toolitems/styles/ARE_Style_Bold`, `ARE_Style_Italic`, … |
| Entry point | `AREditor` (RelativeLayout wrapping `AREditText` + `ARE_Toolbar`) | `AREditText` + `IARE_Toolbar` assembled by the host |
| Extra layer | — | `styles/toolitems/ARE_ToolItem_*` (view + style pairing, extends `ARE_ToolItem_Abstract`) |

They share the `spans/` package and `ARE_ABS_Style`, but the style classes are near-duplicates. A bug fixed in `ARE_Bold` is usually also present in `ARE_Style_Bold`; when fixing behavior, check whether both copies need it. `AREditor` also exposes a couple of styles (`ARE_Emoji`, `ARE_Fontface`, `ARE_IndentLeft/Right`) that have no tool-item equivalent.

### HTML round-trip

`android/inner/Html.java` is a fork of AOSP's `Html` (parsing via the bundled `libs/tagsoup-1.2.1.jar`). It is the serialization format for the whole editor: `fromHtml`/`getHtml` on both `AREditText` and `AREditor` route through it with `AreImageGetter` + `AreTagHandler`.

Custom spans participate by implementing `spans/ARE_Span#getHtml()` — `Html.toHtml` walks spans and appends whatever `getHtml()` returns, so **adding a span that must survive save/load means implementing `ARE_Span` and adding the matching parse path in the forked `Html.java`** (see the `video` tag handling) or in `AreTagHandler`. Non-standard tags in use include `<video>`, `<emoji src="resId">`, and `<a ukey= uname=>` for mentions.

Two invariants hold over `fromHtml` / `getHtml`, both covered by `HtmlRoundTripTest`:
saving and loading is a **fixed point** (an unedited document survives any number of
cycles byte for byte), and the export carries **no editing markers**. Both were
broken: a trailing `\n` was written as a `<br>` that parsed back into another
newline, so every cycle grew a blank line, and a list close counted its paragraph
break twice. When changing the serializer, keep it symmetric with the parser — a
block close already produces a paragraph break on the way back in, so do not also
emit a `<br>` for the empty paragraph it leaves behind. Character-style spans are
sorted before being written so exports are reproducible.

`render/AreTextView` is the read-only counterpart used to display saved HTML (with `AreClickStrategy` for span clicks).

### Host integration points

The library never starts activities for results itself. `AREActivityResultHost` (implemented by the host activity — see `app/.../AREDemoBaseActivity`) exposes `pickImage`, `pickVideo`, `launchAtPicker`, `launchVideoPlayer`, each taking an `AREActivityResultCallback`. Styles do `if (mContext instanceof AREActivityResultHost)` and call through. The older `IARE_Toolbar#onActivityResult` / `AREditor#onActivityResult` dispatch path still exists alongside it and the demo still uses it for the `@` flow.

`strategies/` are the host-supplied behaviors, set on `AREditText`: `ImageStrategy` (upload, then call back into `ARE_Style_Image#insertImage`), `VideoStrategy` (upload, returns URL), `AtStrategy` (open picker / handle selection), `AreClickStrategy` (span clicks in `AreTextView`). `docs/image_upload_insert.md` documents the image flow.

Known global state to be careful with: `Are_VideoPlayerActivity.sVideoStrategy` is a static handoff, and `Constants.SCREEN_WIDTH`/`SCREEN_HEIGHT` are statics initialized by whichever view is constructed first. Removing the static strategy is an explicit roadmap item.

## Toolbar icons

The toolbar icon set is `ARE/are/src/main/res/drawable/are_ic_*.xml` — vector
drawables on a 24dp grid, one weight, letterforms taken from real glyph outlines so
`B` / `I` / `U` / `S` / `A` and the list digits share a typeface. They are
generated by `tools/icons/icons.py` (see its README); edit the generator rather
than the paths, then check the result with `IconSheetTest`, which fails on an icon
that will not inflate and writes a contact sheet of the whole set to the device.

Icons are monochrome and tinted at runtime through `@color/are_tool_item_tint`, so
a host app restyles the toolbar by overriding the `are_toolbar_*` /
`are_tool_item_*` colour resources. "This style is on" is the button's **selected**
state (`ARE_Helper.updateCheckStatus` and `ARE_ToolItem_UpdaterDefault` only call
`setSelected`) — never paint a tool item directly. New tool items should build
their button with `ARE_ToolItem_Abstract.createToolItemView`, which fixes the box,
icon size, tint, active treatment and content description in one place.

## Conventions

Java-first, 4-space indent, braces on the same line, `m`-prefixed fields. Class naming is positional and load-bearing: `ARE_ToolItem_X` (toolbar item) → `ARE_Style_X` (tool-item style) → `AreXSpan` (span). Follow the existing triplet when adding a feature. Kotlin appears only in the demo app (`ARE_ToolItem_Youtube.kt` and friends, a good template for a custom third-party tool item); keep new library code Java.
