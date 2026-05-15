#!/usr/bin/env python3
"""
Renames localization string keys in Kotlin files.

Transforms quoted string literals matching "social.*" or "common.*" to
"amity_social_*" or "amity_common_*" format.

Usage:
    python3 scripts/rename_localization_keys.py [--dry-run] [--root PATH]

Options:
    --dry-run   Print substitutions without modifying files
    --root      Repo root path (default: current directory)
"""

import re
import os
import sys
import argparse
from pathlib import Path

# Matches a quoted string starting with social. or common.
# Group 1: full key including prefix (e.g. social.button.comments)
KEY_PATTERN = re.compile(r'"((social|common)\.[^"]+)"')


def transform_key(key: str) -> str:
    """Transform a dot-notation key to amity_ underscore format.

    Examples:
        "social.button.comments"            -> "amity_social_button_comments"
        "common.button.cancel"              -> "amity_common_button_cancel"
        "social.empty_state.no_posts"       -> "amity_social_empty_state_no_posts"
        "social.label.hardcoded:config/foo" -> "amity_social_label_hardcoded_config_foo"
    """
    return "amity_" + re.sub(r"[^a-zA-Z0-9_]", "_", key)


def process_file(path: Path, dry_run: bool) -> list:
    """Process a single .kt file. Returns list of (line_number, old, new) substitutions."""
    try:
        content = path.read_text(encoding="utf-8")
    except Exception as e:
        print(f"  ERROR reading {path}: {e}", file=sys.stderr)
        return []

    lines = content.splitlines(keepends=True)
    substitutions = []
    new_lines = []

    for i, line in enumerate(lines, start=1):
        for match in KEY_PATTERN.finditer(line):
            old_key = match.group(1)
            new_key = transform_key(old_key)
            if old_key != new_key:  # guard: already transformed keys are idempotent
                substitutions.append((i, f'"{old_key}"', f'"{new_key}"'))
        # Replace all matches in this line
        new_line = KEY_PATTERN.sub(lambda m: f'"{transform_key(m.group(1))}"', line)
        new_lines.append(new_line)

    if substitutions and not dry_run:
        path.write_text("".join(new_lines), encoding="utf-8")

    return substitutions


def main():
    parser = argparse.ArgumentParser(description="Rename localization keys in Kotlin files.")
    parser.add_argument("--dry-run", action="store_true", help="Print changes without modifying files")
    parser.add_argument("--root", default=".", help="Repo root path (default: current directory)")
    args = parser.parse_args()

    root = Path(args.root).resolve()
    dry_run = args.dry_run

    if dry_run:
        print("DRY RUN — no files will be modified\n")

    total_substitutions = 0
    total_files = 0

    for kt_file in sorted(root.rglob("*.kt")):
        # Skip build output directories
        parts = kt_file.parts
        if any(part in ("build", ".gradle", "generated") for part in parts):
            continue

        subs = process_file(kt_file, dry_run)
        if subs:
            rel = kt_file.relative_to(root)
            print(f"\n{rel}")
            for line_no, old, new in subs:
                print(f"  line {line_no:4d}:  {old}  \u2192  {new}")
            total_substitutions += len(subs)
            total_files += 1

    print(f"\n{'DRY RUN ' if dry_run else ''}TOTAL: {total_substitutions} substitutions across {total_files} files")


if __name__ == "__main__":
    main()
