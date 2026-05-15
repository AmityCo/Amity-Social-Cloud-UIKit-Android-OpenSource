# Phase C: String Consolidation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Consolidate 251 value-duplicate key pairs in the Android UIKit localization registry by applying 5 semantic rules, producing a human-approved merge list, then executing all MERGE decisions (registry + XML + callsite rewrites) and verifying the build.

**Architecture:** Three stages — (1) classify all pairs using the rule decision tree into MERGE/KEEP_BOTH/FLAGGED, (2) show the classification list to human for approval/correction, (3) execute approved MERGEs (remove variant key from registry + XML, replace callsites project-wide) and scan KEEP_BOTH pairs for cross-module callsite violations.

**Tech Stack:** Python 3, Kotlin source files, Android Gradle build

---

## File Map

| File | Role |
|------|------|
| `/tmp/phase_c_classify.py` | Stage 1 script — reads pairs, applies rules, writes decision list |
| `/tmp/phase_c_decisions.txt` | Output of Stage 1 — one line per pair: `MERGE\|variant\|canonical`, `KEEP_BOTH\|a\|b`, `FLAGGED\|a\|b\|reason` |
| `/tmp/phase_c_execute.py` | Stage 3 script — reads approved decisions, patches registry + XML + callsites |
| `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/localization/AmitySocialStrings.kt` | Social localization registry |
| `common-compose/src/main/java/com/amity/socialcloud/uikit/common/localization/AmityCommonStrings.kt` | Common localization registry |
| `social-compose/src/main/res/values/strings.xml` | Social XML strings |
| `common-compose/src/main/res/values/strings.xml` | Common XML strings |
| `sample/src/main/java/com/amity/socialcloud/uikit/sample/localization/AmitySocialThaiStrings.kt` | Thai sample overrides — update after merge |

---

## Task 1: Build the Phase B pair list

The classify script needs the raw 251 pairs. The Phase B report already exists at `/tmp/dedup_phase_b_report.txt`. This task extracts the pairs into a structured Python-readable format.

**Files:**
- Read: `/tmp/dedup_phase_b_report.txt`
- Create: `/tmp/phase_c_pairs.py` (embedded constant — list of dicts)

- [ ] **Step 1: Inspect the Phase B report format**

```bash
head -20 /tmp/dedup_phase_b_report.txt
```

Expected output: tab-separated lines like:
```
HIGH     common.button.all    social.tab.tab.all    'All'    Consider consolidating
```

- [ ] **Step 2: Write the pair-extractor script**

Create `/tmp/phase_c_extract_pairs.py`:

```python
#!/usr/bin/env python3
"""Extract pairs from Phase B report into JSON for classifier."""
import re, json

REPORT = "/tmp/dedup_phase_b_report.txt"
OUT    = "/tmp/phase_c_pairs.json"

pairs = []
with open(REPORT) as f:
    for line in f:
        line = line.rstrip()
        if not line or line.startswith("Sim") or line.startswith("---") or line.startswith("Total"):
            continue
        parts = re.split(r'\s{2,}', line)
        if len(parts) < 4:
            continue
        level, key_a, key_b, value = parts[0].strip(), parts[1].strip(), parts[2].strip(), parts[3].strip()
        pairs.append({"level": level, "key_a": key_a, "key_b": key_b, "value": value})

with open(OUT, "w") as f:
    json.dump(pairs, f, indent=2)

print(f"Extracted {len(pairs)} pairs → {OUT}")
```

- [ ] **Step 3: Run the extractor**

```bash
python3 /tmp/phase_c_extract_pairs.py
```

Expected output:
```
Extracted 251 pairs → /tmp/phase_c_pairs.json
```

- [ ] **Step 4: Spot-check output**

```bash
python3 -c "import json; p=json.load(open('/tmp/phase_c_pairs.json')); print(len(p), p[0])"
```

Expected: `251` and first dict shown.

---

## Task 2: Write the classifier script (Stage 1)

Applies the 5-rule decision tree to every pair and writes `/tmp/phase_c_decisions.txt`.

**Decision tree (implemented as code):**

```
Rule 1: one key starts with "common.", other with "social." → KEEP_BOTH
Rule 4/5: UI-type segments differ → KEEP_BOTH
  Segment sets that are distinct from each other:
    - "tab" vs {"status", "button", "label", "modal", "error", "empty_state", "toast", "permission", "notification", "setting", "placeholder"}
    - "status" vs {"tab", "button", "label", "modal"}
    - button outside modal/dialog (key does NOT contain "modal" or "dialog") vs label/modal title → KEEP_BOTH
  Special case: button in dialog vs button in dialog → MERGE
Rule 2/3: else → MERGE, canonical = shorter + generic key
  If tie on length, prefer key without specific context segment
  Specific context segments (non-exhaustive): live.chat, community.setup, post.composer, event.setup, livestream, snackbar
Ambiguous: anything not clearly resolved → FLAGGED
```

**Files:**
- Read: `/tmp/phase_c_pairs.json`
- Create: `/tmp/phase_c_classify.py`
- Write: `/tmp/phase_c_decisions.txt`

- [ ] **Step 1: Write the classifier**

Create `/tmp/phase_c_classify.py`:

```python
#!/usr/bin/env python3
"""Phase C Stage 1: Classify 251 pairs into MERGE / KEEP_BOTH / FLAGGED."""
import json, re

PAIRS_FILE    = "/tmp/phase_c_pairs.json"
DECISIONS_OUT = "/tmp/phase_c_decisions.txt"

# Segments that indicate a specific UI-type role
TAB_SEGMENTS     = {"tab"}
STATUS_SEGMENTS  = {"status"}
BUTTON_SEGMENTS  = {"button"}
LABEL_SEGMENTS   = {"label"}
MODAL_SEGMENTS   = {"modal", "dialog"}
TOAST_SEGMENTS   = {"toast"}
ERROR_SEGMENTS   = {"error"}
EMPTY_SEGMENTS   = {"empty_state"}
PERM_SEGMENTS    = {"permission"}
NOTIF_SEGMENTS   = {"notification"}
SETTING_SEGMENTS = {"setting"}
PLACEHOLDER_SEGS = {"placeholder"}

# Pairs of UI-type segment sets that are semantically distinct → KEEP_BOTH
DISTINCT_TYPE_PAIRS = [
    (TAB_SEGMENTS, STATUS_SEGMENTS),
    (TAB_SEGMENTS, BUTTON_SEGMENTS),
    (TAB_SEGMENTS, LABEL_SEGMENTS),
    (TAB_SEGMENTS, MODAL_SEGMENTS),
    (TAB_SEGMENTS, TOAST_SEGMENTS),
    (TAB_SEGMENTS, ERROR_SEGMENTS),
    (TAB_SEGMENTS, EMPTY_SEGMENTS),
    (STATUS_SEGMENTS, BUTTON_SEGMENTS),
    (STATUS_SEGMENTS, LABEL_SEGMENTS),
    (STATUS_SEGMENTS, MODAL_SEGMENTS),
]

# Specific context prefixes that make a key LESS generic
SPECIFIC_CONTEXTS = [
    "live.chat", "community.setup", "post.composer", "event.setup",
    "create.livestream", "setup.edit", "setup.create", "setup.saving",
    "setup.creating", "setup.updating", "community.invitation",
    "pending.join", "pending.post", "image.poll", "report.reason",
    "select.event", "select.livestream", "select.poll", "select.post",
    "event.detail.header", "event.feed", "event.info",
    "event.setup", "dialog.title", "snackbar",
]

def key_segments(key):
    """Return set of all dot-separated segments."""
    return set(key.split("."))

def ui_type_set(key):
    """Return which UI-type segment groups are present in key."""
    segs = key_segments(key)
    types = set()
    if segs & TAB_SEGMENTS: types.add("tab")
    if segs & STATUS_SEGMENTS: types.add("status")
    if segs & BUTTON_SEGMENTS: types.add("button")
    if segs & LABEL_SEGMENTS: types.add("label")
    if segs & MODAL_SEGMENTS: types.add("modal")
    if segs & TOAST_SEGMENTS: types.add("toast")
    if segs & ERROR_SEGMENTS: types.add("error")
    if segs & EMPTY_SEGMENTS: types.add("empty_state")
    if segs & PERM_SEGMENTS: types.add("permission")
    if segs & NOTIF_SEGMENTS: types.add("notification")
    if segs & SETTING_SEGMENTS: types.add("setting")
    if segs & PLACEHOLDER_SEGS: types.add("placeholder")
    return types

def is_dialog_button(key):
    segs = key_segments(key)
    return bool(segs & BUTTON_SEGMENTS) and bool(segs & (MODAL_SEGMENTS | {"dialog"}))

def is_main_ui_button(key):
    segs = key_segments(key)
    return bool(segs & BUTTON_SEGMENTS) and not bool(segs & (MODAL_SEGMENTS | {"dialog"}))

def ui_types_are_distinct(key_a, key_b):
    types_a = ui_type_set(key_a)
    types_b = ui_type_set(key_b)
    for set_x, set_y in DISTINCT_TYPE_PAIRS:
        a_in_x = bool(types_a & set_x)
        b_in_y = bool(types_b & set_y)
        a_in_y = bool(types_a & set_y)
        b_in_x = bool(types_b & set_x)
        if (a_in_x and b_in_y) or (a_in_y and b_in_x):
            return True
    # main UI button vs label/modal title → distinct
    if (is_main_ui_button(key_a) and not is_main_ui_button(key_b) and
            bool(ui_type_set(key_b) & {"label", "modal"})):
        return True
    if (is_main_ui_button(key_b) and not is_main_ui_button(key_a) and
            bool(ui_type_set(key_a) & {"label", "modal"})):
        return True
    return False

def specificity_score(key):
    """Higher = more specific (worse canonical). Lower = more generic (better canonical)."""
    score = len(key.split("."))  # base: shorter is better
    for ctx in SPECIFIC_CONTEXTS:
        if ctx in key:
            score += 10
    return score

def pick_canonical(key_a, key_b):
    """Return (canonical, variant) — canonical is the one to KEEP."""
    score_a = specificity_score(key_a)
    score_b = specificity_score(key_b)
    if score_a <= score_b:
        return key_a, key_b
    return key_b, key_a

def classify(pair):
    key_a, key_b = pair["key_a"], pair["key_b"]
    # Rule 1: cross-module
    a_mod = key_a.split(".")[0]
    b_mod = key_b.split(".")[0]
    if a_mod != b_mod:
        return "KEEP_BOTH", key_a, key_b, "cross-module"
    # Rule 4/5: distinct UI types
    if ui_types_are_distinct(key_a, key_b):
        return "KEEP_BOTH", key_a, key_b, "distinct-ui-type"
    # Rule 2/3: merge
    canonical, variant = pick_canonical(key_a, key_b)
    return "MERGE", variant, canonical, ""

pairs = json.load(open(PAIRS_FILE))
decisions = []
counts = {"MERGE": 0, "KEEP_BOTH": 0, "FLAGGED": 0}

with open(DECISIONS_OUT, "w") as out:
    out.write(f"{'Action':<12} {'Variant / Key A':<60} {'Canonical / Key B':<60} {'Note'}\n")
    out.write("-" * 160 + "\n")
    for p in pairs:
        action, key_x, key_y, note = classify(p)
        counts[action] += 1
        decisions.append((action, key_x, key_y, note, p["value"]))
        out.write(f"{action:<12} {key_x:<60} {key_y:<60} {note}\n")
    out.write(f"\nTotal: {len(pairs)}  MERGE={counts['MERGE']}  KEEP_BOTH={counts['KEEP_BOTH']}  FLAGGED={counts['FLAGGED']}\n")

print(f"Decisions written to {DECISIONS_OUT}")
print(f"MERGE={counts['MERGE']}  KEEP_BOTH={counts['KEEP_BOTH']}  FLAGGED={counts['FLAGGED']}")
```

- [ ] **Step 2: Run the classifier**

```bash
python3 /tmp/phase_c_classify.py
```

Expected: printed summary like `MERGE=XX  KEEP_BOTH=YY  FLAGGED=ZZ`

- [ ] **Step 3: Review the decision list**

```bash
cat /tmp/phase_c_decisions.txt
```

Read through MERGE decisions and FLAGGED items. Manually edit the file to correct any misclassification before proceeding to Stage 3. Change `MERGE` to `KEEP_BOTH` or vice versa on individual lines as needed.

---

## Task 3: Write the executor script (Stage 3)

Reads the approved `/tmp/phase_c_decisions.txt` and applies all MERGE decisions to registry, XML, and callsites.

**Files:**
- Read: `/tmp/phase_c_decisions.txt`
- Read/Modify: `AmitySocialStrings.kt`, `AmityCommonStrings.kt`, both `strings.xml`
- Modify: all `.kt` files in worktree (callsite replacements)
- Create: `/tmp/phase_c_execute.py`

Worktree root: `/Users/warakorn/Documents/GitHub/cleverden/frontend-agentic/worktree/localization-alignment`

- [ ] **Step 1: Write the executor**

Create `/tmp/phase_c_execute.py`:

```python
#!/usr/bin/env python3
"""Phase C Stage 3: Execute approved MERGE decisions."""
import os, re, sys

DECISIONS_FILE = "/tmp/phase_c_decisions.txt"
WORKTREE = "/Users/warakorn/Documents/GitHub/cleverden/frontend-agentic/worktree/localization-alignment"

SOCIAL_KT  = os.path.join(WORKTREE, "social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/localization/AmitySocialStrings.kt")
COMMON_KT  = os.path.join(WORKTREE, "common-compose/src/main/java/com/amity/socialcloud/uikit/common/localization/AmityCommonStrings.kt")
SOCIAL_XML = os.path.join(WORKTREE, "social-compose/src/main/res/values/strings.xml")
COMMON_XML = os.path.join(WORKTREE, "common-compose/src/main/res/values/strings.xml")

def key_to_xml_name(key):
    """social.button.ok → amity_social_button_ok"""
    return "amity_" + key.replace(".", "_")

def load_decisions():
    merges = []   # (variant, canonical)
    keep_both = []  # (key_a, key_b)
    with open(DECISIONS_FILE) as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("Action") or line.startswith("---") or line.startswith("Total"):
                continue
            parts = line.split()
            if len(parts) < 3:
                continue
            action = parts[0]
            # Split by whitespace — key_x and key_y are first two tokens after action
            tokens = line.split()
            action = tokens[0]
            # Find the two key columns (fixed-width in file)
            rest = line[12:]  # skip action column (12 chars)
            key_x = rest[:60].strip()
            key_y = rest[60:120].strip()
            if action == "MERGE":
                merges.append((key_x, key_y))  # variant, canonical
            elif action == "KEEP_BOTH":
                keep_both.append((key_x, key_y))
    return merges, keep_both

def remove_registry_entry(kt_path, key):
    """Remove the map entry for key from the registry .kt file."""
    content = open(kt_path).read()
    # Match lines like:  put("social.button.ok", R.string.amity_social_button_ok)
    pattern = rf'\s*put\("{re.escape(key)}"[^)]*\)\n'
    new_content, n = re.subn(pattern, "\n", content)
    if n == 0:
        print(f"  WARNING: registry entry for '{key}' not found in {os.path.basename(kt_path)}")
    else:
        open(kt_path, "w").write(new_content)
        print(f"  Removed registry entry: {key}")

def remove_xml_entry(xml_path, xml_name):
    """Remove the <string name="xml_name">...</string> entry from XML."""
    content = open(xml_path).read()
    pattern = rf'\s*<string name="{re.escape(xml_name)}"[^>]*>.*?</string>'
    new_content, n = re.subn(pattern, "", content, flags=re.DOTALL)
    if n == 0:
        print(f"  WARNING: XML entry '{xml_name}' not found in {os.path.basename(xml_path)}")
    else:
        open(xml_path, "w").write(new_content)
        print(f"  Removed XML entry: {xml_name}")

def replace_callsites(variant, canonical):
    """Replace amitySocialString("variant") → amitySocialString("canonical") in all .kt files."""
    count = 0
    for root, dirs, files in os.walk(WORKTREE):
        dirs[:] = [d for d in dirs if d not in {".git", "build", ".gradle"}]
        for fname in files:
            if not fname.endswith(".kt"):
                continue
            path = os.path.join(root, fname)
            content = open(path).read()
            # Match amitySocialString("variant") or amityCommonString("variant")
            pattern = rf'(amity(?:Social|Common)String\("){re.escape(variant)}("\))'
            new_content, n = re.subn(pattern, rf'\g<1>{canonical}\2', content)
            if n > 0:
                open(path, "w").write(new_content)
                count += n
                rel = os.path.relpath(path, WORKTREE)
                print(f"    {rel}: {n} replacement(s)")
    return count

def scan_cross_module_callsites(key_a, key_b):
    """For KEEP_BOTH: warn if a social module file calls a common.* key or vice versa."""
    warnings = []
    for root, dirs, files in os.walk(WORKTREE):
        dirs[:] = [d for d in dirs if d not in {".git", "build", ".gradle", "sample"}]
        for fname in files:
            if not fname.endswith(".kt"):
                continue
            path = os.path.join(root, fname)
            content = open(path).read()
            rel = os.path.relpath(path, WORKTREE)
            is_common = rel.startswith("common-compose")
            is_social = rel.startswith("social-compose")
            for key in (key_a, key_b):
                is_common_key = key.startswith("common.")
                is_social_key = key.startswith("social.")
                if f'"{key}"' in content:
                    if is_common and is_social_key:
                        warnings.append(f"  CROSS-MODULE: common file {rel} calls social key '{key}'")
                    if is_social and is_common_key:
                        warnings.append(f"  CROSS-MODULE: social file {rel} calls common key '{key}'")
    return warnings

# --- Main ---
merges, keep_both = load_decisions()
print(f"Loaded {len(merges)} MERGE decisions, {len(keep_both)} KEEP_BOTH decisions")

total_replacements = 0
cross_module_warnings = []

print("\n=== Executing MERGE decisions ===")
for variant, canonical in merges:
    print(f"\nMERGE: {variant} → {canonical}")
    # Determine which registry and XML to patch
    module = variant.split(".")[0]
    if module == "social":
        remove_registry_entry(SOCIAL_KT, variant)
        remove_xml_entry(SOCIAL_XML, key_to_xml_name(variant))
    elif module == "common":
        remove_registry_entry(COMMON_KT, variant)
        remove_xml_entry(COMMON_XML, key_to_xml_name(variant))
    else:
        print(f"  UNKNOWN module for key '{variant}' — skipping registry/XML removal")
    n = replace_callsites(variant, canonical)
    total_replacements += n
    print(f"  Callsite replacements: {n}")

print(f"\nTotal callsite replacements: {total_replacements}")

print("\n=== Scanning KEEP_BOTH for cross-module violations ===")
for key_a, key_b in keep_both:
    warns = scan_cross_module_callsites(key_a, key_b)
    cross_module_warnings.extend(warns)

if cross_module_warnings:
    print("\nCross-module callsite warnings:")
    for w in cross_module_warnings:
        print(w)
else:
    print("No cross-module violations found.")

print("\nDone. Run ./gradlew assembleDebug to verify build.")
```

- [ ] **Step 2: Verify the script is syntactically valid**

```bash
python3 -m py_compile /tmp/phase_c_execute.py && echo "OK"
```

Expected: `OK`

---

## Task 4: Human review and approval of decisions

This task is a manual review step. Do not skip it.

- [ ] **Step 1: Open the decision file**

```bash
cat /tmp/phase_c_decisions.txt | grep "^MERGE" | wc -l
cat /tmp/phase_c_decisions.txt | grep "^KEEP_BOTH" | wc -l
cat /tmp/phase_c_decisions.txt | grep "^FLAGGED" | wc -l
```

- [ ] **Step 2: Review all MERGE lines**

```bash
grep "^MERGE" /tmp/phase_c_decisions.txt
```

For each MERGE pair, verify:
- Both keys are in the same module (`social.*` vs `social.*`)
- The canonical key (right column) is genuinely more generic
- The variant key (left column) is safe to remove

Change `MERGE` → `KEEP_BOTH` on any line you disagree with using a text editor.

- [ ] **Step 3: Review all FLAGGED lines**

```bash
grep "^FLAGGED" /tmp/phase_c_decisions.txt
```

Manually change each `FLAGGED` line to either `MERGE variant canonical` or `KEEP_BOTH key_a key_b`.

- [ ] **Step 4: Re-verify counts after edits**

```bash
grep "^MERGE" /tmp/phase_c_decisions.txt | wc -l
grep "^KEEP_BOTH" /tmp/phase_c_decisions.txt | wc -l
grep "^FLAGGED" /tmp/phase_c_decisions.txt | wc -l
```

FLAGGED count must be 0 before proceeding.

---

## Task 5: Execute the approved decisions

- [ ] **Step 1: Run the executor (dry observation first)**

```bash
python3 /tmp/phase_c_execute.py 2>&1 | head -60
```

Verify the first few MERGE operations look correct (right files being patched).

- [ ] **Step 2: Run the full executor**

```bash
python3 /tmp/phase_c_execute.py 2>&1 | tee /tmp/phase_c_execute_log.txt
```

- [ ] **Step 3: Check for warnings**

```bash
grep -i "warning\|CROSS-MODULE\|not found" /tmp/phase_c_execute_log.txt
```

For any `not found` warning: the key was already removed or never existed — investigate before continuing.  
For any `CROSS-MODULE` warning: a file calls a key from the wrong module — fix the callsite manually.

- [ ] **Step 4: Update Thai sample file**

```bash
python3 - <<'EOF'
import re, os

THAI = "/Users/warakorn/Documents/GitHub/cleverden/frontend-agentic/worktree/localization-alignment/sample/src/main/java/com/amity/socialcloud/uikit/sample/localization/AmitySocialThaiStrings.kt"
DECISIONS = "/tmp/phase_c_decisions.txt"

# Build replacement map from MERGE lines
replacements = {}
with open(DECISIONS) as f:
    for line in f:
        if not line.startswith("MERGE"):
            continue
        rest = line[12:]
        variant = rest[:60].strip()
        canonical = rest[60:120].strip()
        replacements[variant] = canonical

content = open(THAI).read()
count = 0
for variant, canonical in replacements.items():
    pattern = rf'"{re.escape(variant)}"'
    new_content, n = re.subn(pattern, f'"{canonical}"', content)
    if n > 0:
        content = new_content
        count += n

open(THAI, "w").write(content)
print(f"Thai sample: {count} replacement(s)")
EOF
```

- [ ] **Step 5: Build the project**

```bash
cd /Users/warakorn/Documents/GitHub/cleverden/frontend-agentic/worktree/localization-alignment && ./gradlew assembleDebug 2>&1 | tail -20
```

Expected: `BUILD SUCCESSFUL`

If build fails with "unresolved reference" or "resource not found": a callsite still references a removed key. Search for it:

```bash
grep -r "amitySocialString\|amityCommonString" --include="*.kt" . | grep "<failing-key>"
```

Fix the remaining callsite manually, then re-run the build.

- [ ] **Step 6: Commit**

```bash
cd /Users/warakorn/Documents/GitHub/cleverden/frontend-agentic/worktree/localization-alignment
git add -A
git commit -m "refactor: Phase C string consolidation — merge duplicate keys"
```

---

## Task 6: Push and verify

- [ ] **Step 1: Push branch**

```bash
cd /Users/warakorn/Documents/GitHub/cleverden/frontend-agentic/worktree/localization-alignment
git push origin ws/feat/localization-alignment
```

- [ ] **Step 2: Verify PR #632 shows new commits**

```bash
gh pr view 632 --repo AmityCo/Amity-Social-Cloud-UIKit-Android
```

- [ ] **Step 3: Run a final grep to confirm no variant callsites remain**

For each MERGE decision, the variant key must no longer appear in any production `.kt` file. Run:

```bash
python3 - <<'EOF'
import re, os

DECISIONS = "/tmp/phase_c_decisions.txt"
WORKTREE  = "/Users/warakorn/Documents/GitHub/cleverden/frontend-agentic/worktree/localization-alignment"

stale = []
with open(DECISIONS) as f:
    for line in f:
        if not line.startswith("MERGE"):
            continue
        rest = line[12:]
        variant = rest[:60].strip()
        for root, dirs, files in os.walk(WORKTREE):
            dirs[:] = [d for d in dirs if d not in {".git", "build", ".gradle", "sample"}]
            for fname in files:
                if not fname.endswith(".kt"):
                    continue
                content = open(os.path.join(root, fname)).read()
                if f'"{variant}"' in content:
                    stale.append((variant, os.path.relpath(os.path.join(root, fname), WORKTREE)))

if stale:
    print("STALE CALLSITES FOUND:")
    for v, path in stale:
        print(f"  {v}  in  {path}")
else:
    print("All clear — no stale variant callsites.")
EOF
```

Expected: `All clear — no stale variant callsites.`
