# Phase C: String Consolidation Design

## Overview

Process 251 value-duplicate key pairs from the Phase B similarity report and consolidate them according to 5 semantic rules. The goal is to reduce redundancy in the localization registry while preserving semantic distinction between keys that serve different UI roles.

---

## Rules

### Rule 1 — Cross-module duplicates: KEEP BOTH
`common.*` ↔ `social.*` pairs with same value → keep both keys.  
Rationale: modules should be independently localizable.  
Side effect: verify that each callsite references a key from its own module. Log a warning if a cross-module call is detected.

### Rule 2 — Word-order synonyms / semantic duplicates: MERGE → 1 key
Same value, same semantic meaning, word order or naming convention differs → merge.  
Examples:
- `social.toast.community.add.member.failed` ↔ `social.toast.membership.add.failed` → keep generic key
- `social.toast.live.chat.demote.to.member.success.toast` ↔ `social.toast.member.demote.success.toast` → keep generic key

### Rule 3 — Same value + same UI component context: MERGE → 1 key
Same value, both used in same UI component type (e.g. both are toasts, both are dialog buttons) → merge.  
Canonical key = shorter + no specific context prefix.

### Rule 4 — Same value, different UI type: KEEP BOTH
Same value but used in different UI roles → keep both.  
Examples:
- `.tab.` vs `.status.` → distinct
- `.button.` (main UI action) vs `.label.event.xxx.title` (page title) → distinct
- button in main UI screen vs button in dialog → distinct

### Rule 5 — Button navigation ↔ page/dialog title: KEEP BOTH
Same rationale as Rule 4. A button that navigates to a screen and the title of that screen serve different semantic roles even if the text is identical.

---

## Canonical Key Selection (for MERGE)

Apply both criteria; shorter + less specific wins:

1. **Shorter** — fewer dot-separated segments preferred
2. **No specific context prefix** — prefixes like `live.chat`, `community.setup`, `post.composer`, `event.setup` are specific; `member`, `user`, `toast` alone are generic

If criteria conflict, prefer the key without specific context prefix.

---

## Rule Decision Tree (per pair)

```
Is one key in common.* and the other in social.*?
  YES → KEEP_BOTH (Rule 1)
  NO ↓

Do the keys have different UI-type segments?
  - .tab. vs .status. / .button. / .label.
  - button in main UI vs button in dialog
  YES → KEEP_BOTH (Rule 4/5)
  NO ↓

Both buttons in same dialog context OR semantic synonyms?
  YES → MERGE, canonical = shorter + generic key (Rule 2/3)
  NO → FLAG for human review
```

---

## Execution Stages

### Stage 1 — Classify
Script reads all 251 pairs and applies the decision tree.  
Output: a decision list file with one entry per pair:
```
MERGE   | variant_key | canonical_key
KEEP_BOTH | key_a | key_b
FLAGGED | key_a | key_b | reason
```

### Stage 2 — Human Approval
Decision list is displayed to the user for review and correction before any files are touched.  
User can change any decision (MERGE → KEEP_BOTH or vice versa) before proceeding.

### Stage 3 — Execute
For each `MERGE` decision:
- Remove variant key from registry `.kt` file (remove the map entry)
- Remove variant key's XML entry from `strings.xml`
- Replace all callsite references (`amitySocialString("variant.key")` → `amitySocialString("canonical.key")`) across the project
- Build and verify

For each `KEEP_BOTH` decision:
- Scan callsites for cross-module calls
- Log any violations (e.g. social module calling a `common.*` key directly)

---

## Scope

- Registry files: `AmitySocialStrings.kt`, `AmityCommonStrings.kt`
- XML: `social-compose/src/main/res/values/strings.xml`, `common-compose/src/main/res/values/strings.xml`
- Callsite search: all `.kt` files under the worktree root (excluding `/sample/`)
- Thai sample file: `sample/src/main/java/.../AmitySocialThaiStrings.kt` — update after merge

---

## Out of Scope

- Adding new keys
- Changing string values
- Modifying cross-platform alignment or key naming conventions
- MEDIUM/LOW pairs that require additional judgment beyond the 5 rules (flagged for manual review)
