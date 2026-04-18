# SDK Production Roadmap

## Phase 1: Define the SDK Surface

### 1. Create a public API inventory
Files:
- `README.md`
- `Usage.md`
- `docs/sdk-api.md`

Goal:
- List the only supported entry points: `AREditor`, `AREditText`, `IARE_Toolbar`, `IARE_ToolItem`, and strategy interfaces.
- Mark everything else as internal or non-stable.

### 2. Reduce accidental public surface in docs
Files:
- `README.md`
- `README-zh.md`

Goal:
- Stop teaching consumers to depend on internal classes like spans, activities, or style implementations.

## Phase 2: Remove Unsafe SDK Patterns

### 1. Eliminate static activity state
Files:
- `ARE/are/src/main/java/com/chinalwb/are/activities/Are_VideoPlayerActivity.java`
- `ARE/are/src/main/java/com/chinalwb/are/styles/ARE_Video.java`
- `ARE/are/src/main/java/com/chinalwb/are/styles/toolitems/styles/ARE_Style_Video.java`
- `ARE/are/src/main/java/com/chinalwb/are/styles/toolbar/ARE_Toolbar.java`

Goal:
- Remove `Are_VideoPlayerActivity.sVideoStrategy`.
- Pass behavior through explicit contracts or a launcher/controller object.

### 2. Remove legacy result fallback from the library
Files:
- `ARE/are/src/main/java/com/chinalwb/are/AREditor.java`
- `ARE/are/src/main/java/com/chinalwb/are/styles/toolbar/IARE_Toolbar.java`
- tool item classes under `styles/toolitems`

Goal:
- Move fully to launcher/callback-based APIs.

## Phase 3: Stabilize Extension Points

### 1. Formalize media and mention integration
Files:
- `ARE/are/src/main/java/com/chinalwb/are/strategies/ImageStrategy.java`
- `ARE/are/src/main/java/com/chinalwb/are/strategies/VideoStrategy.java`
- `ARE/are/src/main/java/com/chinalwb/are/strategies/AtStrategy.java`
- `ARE/are/src/main/java/com/chinalwb/are/AREActivityResultHost.java`

Goal:
- Define supported host responsibilities clearly.
- Add error and cancel callbacks.

### 2. Add a typed config object
Files:
- `ARE/are/src/main/java/com/chinalwb/are/AREditor.java`
- `ARE/are/src/main/java/com/chinalwb/are/AREditText.java`

Goal:
- Replace scattered setters with `EditorConfig` and `MediaConfig`.

## Phase 4: Test What Matters

### 1. Round-trip serialization tests
Files:
- `ARE/are/src/test/java/com/chinalwb/are/...`

Targets:
- bold, italic, underline
- lists
- alignment
- links
- colors
- quotes
- hr
- images
- video
- mentions

### 2. Lifecycle and integration tests
Files:
- `ARE/are/src/androidTest/java/com/chinalwb/are/...`

Targets:
- multiple editor instances
- rotation and process recreation
- toolbar state after selection changes
- media insert flows

## Phase 5: Separate SDK from Demo

### 1. Move sample-only behavior out of the library mental model
Files:
- `ARE/app/src/main/java/com/chinalwb/are/demo/...`
- `README.md`

Goal:
- Demo shows integration only.
- No demo assumptions leak into SDK design.

## Phase 6: Packaging

### 1. Split modules
Suggested target:
- `:are-core`
- `:are-ui`
- `:app` sample

Goal:
- Parsing/model code independent from UI/widget code.

## Best Next 3 Tasks

1. Remove `sVideoStrategy` and replace it with explicit per-request wiring.
2. Add HTML round-trip tests for all supported formatting.
3. Write `docs/sdk-api.md` and trim docs to only supported APIs.
