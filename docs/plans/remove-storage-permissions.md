# Remove Storage Permissions Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Remove all unnecessary `READ_EXTERNAL_STORAGE` and `WRITE_EXTERNAL_STORAGE` declarations from UIKit Android and Android SDK to achieve Google Play compliance, replacing legacy permission-gating code with direct Photo Picker calls (which require no storage permissions).

**Architecture:** All media picking already uses `PickVisualMedia` (Android Photo Picker). This plan is pure cleanup — remove dead permission checks and manifest declarations. No new logic is introduced. The logging module is simplified to always use `getExternalFilesDir()` which requires no permission since API 19.

**Tech Stack:** Android (Kotlin), AndroidManifest.xml, ActivityResultContracts.PickVisualMedia

---

## File Map

### UIKit Android (`~/Documents/GitHub/Amity-Social-Cloud-UIKit-Android`)

**Manifests (delete lines only):**
- `common/src/main/AndroidManifest.xml` — remove lines 10–11
- `common-compose/src/main/AndroidManifest.xml` — remove lines 5, 11
- `social-compose/src/main/AndroidManifest.xml` — remove lines 8–10

**Kotlin:**
- `social/src/main/java/com/amity/socialcloud/uikit/community/newsfeed/fragment/AmityBaseCreatePostFragment.kt`
- `social/src/main/java/com/amity/socialcloud/uikit/community/newsfeed/fragment/AmityLiveStreamPostCreatorFragment.kt`
- `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/clip/create/AmityCreateClipPage.kt`
- `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/livestream/create/AmityCreateLivestreamPage.kt`
- `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/livestream/room/create/AmityCreateRoomPage.kt`
- `chat/src/main/java/com/amity/socialcloud/uikit/chat/home/fragment/AmityChatRoomWithDefaultComposeBarFragment.kt`
- `logging/src/main/java/com/amity/socialcloud/uikit/logging/AmityFileLoggingTree.kt`

### Android SDK (`~/Documents/GitHub/Amity-Social-Cloud-SDK-Android`)

**Manifests:**
- `amity-sdk/src/main/AndroidManifest.xml` — remove READ_EXTERNAL_STORAGE
- `amity-push-baidu/src/main/AndroidManifest.xml` — remove WRITE_EXTERNAL_STORAGE

---

## Task 1: UIKit — Fix Manifests

**Files:**
- Modify: `common/src/main/AndroidManifest.xml`
- Modify: `common-compose/src/main/AndroidManifest.xml`
- Modify: `social-compose/src/main/AndroidManifest.xml`

- [ ] **Step 1: Remove storage permissions from `common` manifest**

Open `common/src/main/AndroidManifest.xml`. Remove these two lines:
```xml
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

- [ ] **Step 2: Remove storage permissions from `common-compose` manifest**

Open `common-compose/src/main/AndroidManifest.xml`. Remove both occurrences of:
```xml
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```
(There are 2 entries — one at line ~5 and one at line ~11. Remove both.)

Also remove any commented-out `READ_MEDIA_IMAGES` / `READ_MEDIA_VIDEO` lines to keep the file clean.

- [ ] **Step 3: Remove storage permissions from `social-compose` manifest**

Open `social-compose/src/main/AndroidManifest.xml`. Remove:
```xml
    <uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="28" />
```

- [ ] **Step 4: Verify no storage permissions remain in UIKit manifests**

Run from UIKit repo root:
```bash
grep -r "READ_EXTERNAL_STORAGE\|WRITE_EXTERNAL_STORAGE" */src/main/AndroidManifest.xml
```
Expected: no output (only `sample/` manifest may still have them — that's acceptable)

- [ ] **Step 5: Commit**

```bash
git add common/src/main/AndroidManifest.xml common-compose/src/main/AndroidManifest.xml social-compose/src/main/AndroidManifest.xml
git commit -m "chore: remove READ/WRITE_EXTERNAL_STORAGE from manifests"
```

---

## Task 2: UIKit — Fix `AmityBaseCreatePostFragment` (social module)

**Files:**
- Modify: `social/src/main/java/com/amity/socialcloud/uikit/community/newsfeed/fragment/AmityBaseCreatePostFragment.kt`

Context: `handleAddFiles()` calls `grantStoragePermission()` before opening a file picker. `grantCameraPermission()` adds `READ_EXTERNAL_STORAGE` for API < 33. Both need cleanup. The `openFilePicker()` uses `ACTION_OPEN_DOCUMENT` (not media picker) — keep it but call directly without permission gate.

- [ ] **Step 1: Remove `grantStoragePermission()` method**

Delete the entire method (approximately lines 550–562):
```kotlin
private fun grantStoragePermission(requestCode: Int, onPermissionGrant: () -> Unit) {
    val requiredPermissions = emptyList<String>().toMutableList()
    requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)

    val hasRequiredPermission = requiredPermissions.fold(true) { acc, permission ->
        acc && hasPermission(permission)
    }
    if (hasRequiredPermission) {
        onPermissionGrant()
    } else {
        requestPermission(requiredPermissions.toTypedArray(), requestCode)
    }
}
```

- [ ] **Step 2: Update `handleAddFiles()` to call `openFilePicker()` directly**

Change:
```kotlin
private fun handleAddFiles() {
    if (hasReachedSelectionLimit()) {
        view?.showSnackBar(getString(R.string.amity_create_post_max_image_selected_warning))
    } else {
        grantStoragePermission(REQUEST_STORAGE_PERMISSION_FILE_UPLOAD) { openFilePicker() }
    }
}
```
To:
```kotlin
private fun handleAddFiles() {
    if (hasReachedSelectionLimit()) {
        view?.showSnackBar(getString(R.string.amity_create_post_max_image_selected_warning))
    } else {
        openFilePicker()
    }
}
```

- [ ] **Step 3: Remove `READ_EXTERNAL_STORAGE` from `grantCameraPermission()`**

Change:
```kotlin
private fun grantCameraPermission(requestCode: Int, onPermissionGrant: () -> Unit) {
    val requiredPermissions = mutableListOf(Manifest.permission.CAMERA)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val hasRequiredPermission = requiredPermissions.fold(true) { acc, permission ->
        acc && hasPermission(permission)
    }
    if (hasRequiredPermission) {
        onPermissionGrant()
    } else {
        requestPermission(requiredPermissions.toTypedArray(), requestCode)
    }
}
```
To:
```kotlin
private fun grantCameraPermission(requestCode: Int, onPermissionGrant: () -> Unit) {
    val requiredPermissions = mutableListOf(Manifest.permission.CAMERA)
    val hasRequiredPermission = requiredPermissions.fold(true) { acc, permission ->
        acc && hasPermission(permission)
    }
    if (hasRequiredPermission) {
        onPermissionGrant()
    } else {
        requestPermission(requiredPermissions.toTypedArray(), requestCode)
    }
}
```

- [ ] **Step 4: Remove unused `REQUEST_STORAGE_PERMISSION_FILE_UPLOAD` constant if it is now unused**

Search for `REQUEST_STORAGE_PERMISSION_FILE_UPLOAD` in the file. If it only appeared in the `grantStoragePermission` call and is declared as a constant, remove the constant declaration too.

- [ ] **Step 5: Remove unused `Manifest` import if no longer referenced**

Check if `import android.Manifest` is still used elsewhere in the file. If not, remove it.

- [ ] **Step 6: Build the `social` module to verify no compile errors**

```bash
./gradlew :social:compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add social/src/main/java/com/amity/socialcloud/uikit/community/newsfeed/fragment/AmityBaseCreatePostFragment.kt
git commit -m "chore: remove storage permission gate from post creation"
```

---

## Task 3: UIKit — Fix `AmityLiveStreamPostCreatorFragment` (social module)

**Files:**
- Modify: `social/src/main/java/com/amity/socialcloud/uikit/community/newsfeed/fragment/AmityLiveStreamPostCreatorFragment.kt`

Context: `grantStoragePermission()` gates image picker for thumbnail upload. The picker itself is already `PickVisualMedia` — call it directly.

- [ ] **Step 1: Find and remove `grantStoragePermission()` method**

Delete the entire method:
```kotlin
private fun grantStoragePermission(requestCode: Int, onPermissionGrant: () -> Unit) {
    if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        onPermissionGrant()
    } else {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCode)
    }
}
```

- [ ] **Step 2: Find call sites of `grantStoragePermission` and replace with direct picker launch**

Search for any remaining call to `grantStoragePermission` in this file and replace each with the direct action it was guarding (e.g., `imagePickerLauncher.launch(...)`).

- [ ] **Step 3: Remove unused `Manifest` import if no longer referenced**

- [ ] **Step 4: Build to verify**

```bash
./gradlew :social:compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add social/src/main/java/com/amity/socialcloud/uikit/community/newsfeed/fragment/AmityLiveStreamPostCreatorFragment.kt
git commit -m "chore: remove WRITE_EXTERNAL_STORAGE gate from livestream thumbnail picker"
```

---

## Task 4: UIKit — Fix `AmityCreateClipPage` (social-compose module)

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/clip/create/AmityCreateClipPage.kt`

Context: `mediaPickerPermissions = READ_EXTERNAL_STORAGE` is used to gate `mediaPickerLauncher` (PickVisualMedia). Replace with direct launch — no permission needed.

- [ ] **Step 1: Remove `mediaPickerPermissions` variable**

Delete:
```kotlin
val mediaPickerPermissions = Manifest.permission.READ_EXTERNAL_STORAGE
```

- [ ] **Step 2: Remove `isMediaPickerPermissionGranted` state**

Delete:
```kotlin
val isMediaPickerPermissionGranted by remember {
    mutableStateOf(
        ContextCompat.checkSelfPermission(
            context,
            mediaPickerPermissions
        ) == PackageManager.PERMISSION_GRANTED
    )
}
```

- [ ] **Step 3: Remove `mediaPickerPermissionLauncher`**

Delete the entire `rememberLauncherForActivityResult` block for `RequestPermission` that launches `mediaPickerPermissions`. It looks like:
```kotlin
val mediaPickerPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) {
        mediaPickerLauncher.launch(
            PickVisualMediaRequest(
                mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
            )
        )
    } else {
        behavior.goToNoPermissionPage(context = context)
    }
}
```

- [ ] **Step 4: Replace `mediaPickerPermissionLauncher.launch(mediaPickerPermissions)` call with direct picker launch**

Find the call site (around line 416):
```kotlin
mediaPickerPermissionLauncher.launch(mediaPickerPermissions)
```
Replace with:
```kotlin
mediaPickerLauncher.launch(
    PickVisualMediaRequest(
        mediaType = ActivityResultContracts.PickVisualMedia.VideoOnly
    )
)
```

- [ ] **Step 5: Remove unused imports (`PackageManager`, `ContextCompat`, `Manifest` if no longer used)**

- [ ] **Step 6: Build to verify**

```bash
./gradlew :social-compose:compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/clip/create/AmityCreateClipPage.kt
git commit -m "chore: remove READ_EXTERNAL_STORAGE gate from clip video picker"
```

---

## Task 5: UIKit — Fix `AmityCreateLivestreamPage` and `AmityCreateRoomPage` (social-compose module)

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/livestream/create/AmityCreateLivestreamPage.kt`
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/livestream/room/create/AmityCreateRoomPage.kt`

Context: Same pattern as Task 4 — `mediaPickerPermissions = READ_EXTERNAL_STORAGE` gates `PickVisualMedia.ImageOnly` for thumbnail selection.

Apply the same changes as Task 4 to **both files**:

- [ ] **Step 1: In `AmityCreateLivestreamPage.kt` — remove `mediaPickerPermissions`, `isMediaPickerPermissionGranted`, `mediaPickerPermissionLauncher`**

Same pattern as Task 4. Remove the variable, the state, and the permission launcher.

- [ ] **Step 2: In `AmityCreateLivestreamPage.kt` — replace `mediaPickerPermissionLauncher.launch(mediaPickerPermissions)` with direct launch**

Find all call sites (there may be 2 — around lines 935):
```kotlin
mediaPickerPermissionLauncher.launch(mediaPickerPermissions)
```
Replace each with:
```kotlin
mediaPickerLauncher.launch(
    PickVisualMediaRequest(
        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
    )
)
```

- [ ] **Step 3: Apply same changes to `AmityCreateRoomPage.kt`**

Same pattern — remove `mediaPickerPermissions`, `isMediaPickerPermissionGranted`, `mediaPickerPermissionLauncher`, and replace call sites (~line 1166) with direct `mediaPickerLauncher.launch(...)`.

- [ ] **Step 4: Remove unused imports in both files**

- [ ] **Step 5: Build to verify**

```bash
./gradlew :social-compose:compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/livestream/create/AmityCreateLivestreamPage.kt
git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/livestream/room/create/AmityCreateRoomPage.kt
git commit -m "chore: remove READ_EXTERNAL_STORAGE gate from livestream/room thumbnail picker"
```

---

## Task 6: UIKit — Fix `AmityChatRoomWithDefaultComposeBarFragment` (chat module)

**Files:**
- Modify: `chat/src/main/java/com/amity/socialcloud/uikit/chat/home/fragment/AmityChatRoomWithDefaultComposeBarFragment.kt`

Context: `requiredPermissions` is used for **audio recording** (RECORD_AUDIO + READ_MEDIA_AUDIO). The storage permissions were added for older APIs but are not needed — audio recording does not require storage access. The image picker in this file already uses `PickVisualMedia` (no permission needed).

- [ ] **Step 1: Simplify `requiredPermissions`**

Change:
```kotlin
private val requiredPermissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
    arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
} else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
    arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
} else {
    arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_MEDIA_AUDIO
    )
}
```
To:
```kotlin
private val requiredPermissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
    arrayOf(Manifest.permission.RECORD_AUDIO)
} else {
    arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_MEDIA_AUDIO
    )
}
```

- [ ] **Step 2: Remove unused `Manifest.permission.READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE` imports if no longer referenced**

- [ ] **Step 3: Build to verify**

```bash
./gradlew :chat:compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

```bash
git add chat/src/main/java/com/amity/socialcloud/uikit/chat/home/fragment/AmityChatRoomWithDefaultComposeBarFragment.kt
git commit -m "chore: remove storage permissions from chat audio recording permission request"
```

---

## Task 7: UIKit — Simplify `AmityFileLoggingTree` (logging module)

**Files:**
- Modify: `logging/src/main/java/com/amity/socialcloud/uikit/logging/AmityFileLoggingTree.kt`

Context: `getLogFolder()` checks `WRITE_EXTERNAL_STORAGE` to decide between `getExternalFilesDir()` and `cacheDir`. `getExternalFilesDir()` requires no permission since API 19 — always use it directly.

- [ ] **Step 1: Remove `isStoragePermissionGranted()` method**

Delete:
```kotlin
private fun isStoragePermissionGranted(): Boolean =
    context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
```

- [ ] **Step 2: Simplify `getLogFolder()`**

Change:
```kotlin
private fun getLogFolder(): File? {
    return if (isStoragePermissionGranted()) {
        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    } else {
        context.cacheDir
    }
}
```
To:
```kotlin
private fun getLogFolder(): File? {
    return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
}
```

- [ ] **Step 3: Remove unused imports**

Remove these imports if no longer used:
```kotlin
import android.Manifest
import android.content.pm.PackageManager
```

- [ ] **Step 4: Build to verify**

```bash
./gradlew :logging:compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add logging/src/main/java/com/amity/socialcloud/uikit/logging/AmityFileLoggingTree.kt
git commit -m "chore: simplify file logging to always use getExternalFilesDir, no permission needed"
```

---

## Task 8: Android SDK — Fix Manifests (trunk branch)

**Repo:** `~/Documents/GitHub/Amity-Social-Cloud-SDK-Android`

**Files:**
- Modify: `amity-sdk/src/main/AndroidManifest.xml`
- Modify: `amity-push-baidu/src/main/AndroidManifest.xml`

- [ ] **Step 1: Verify you are on trunk**

```bash
git branch --show-current
```
Expected: `trunk`

If not: `git checkout trunk`

- [ ] **Step 2: Remove `READ_EXTERNAL_STORAGE` from `amity-sdk` manifest**

Open `amity-sdk/src/main/AndroidManifest.xml`. Remove:
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

- [ ] **Step 3: Remove `WRITE_EXTERNAL_STORAGE` from `amity-push-baidu` manifest**

Open `amity-push-baidu/src/main/AndroidManifest.xml`. Remove:
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

- [ ] **Step 4: Verify no storage permissions remain in SDK module manifests**

```bash
grep -r "READ_EXTERNAL_STORAGE\|WRITE_EXTERNAL_STORAGE" amity-sdk/src/main/AndroidManifest.xml amity-push-baidu/src/main/AndroidManifest.xml
```
Expected: no output

- [ ] **Step 5: Build SDK to verify no compile errors**

```bash
./gradlew :amity-sdk:compileDebugKotlin
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add amity-sdk/src/main/AndroidManifest.xml amity-push-baidu/src/main/AndroidManifest.xml
git commit -m "chore: remove unnecessary READ/WRITE_EXTERNAL_STORAGE from SDK manifests"
```

---

## Task 9: Final Verification

- [ ] **Step 1: Full build — UIKit**

```bash
cd ~/Documents/GitHub/Amity-Social-Cloud-UIKit-Android
./gradlew assembleDebug
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 2: Full build — SDK**

```bash
cd ~/Documents/GitHub/Amity-Social-Cloud-SDK-Android
./gradlew assembleDebug
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Verify no storage permissions in merged manifest**

In UIKit, check the merged manifest output for the sample app:
```bash
grep -r "READ_EXTERNAL_STORAGE\|WRITE_EXTERNAL_STORAGE" */build/intermediates/merged_manifest/debug/ 2>/dev/null | grep -v "sample"
```
Expected: no output (or only Baidu AAR's own declaration if Baidu injects it)

- [ ] **Step 4: Manual smoke test checklist**

Test these flows on a physical device or emulator (no permission dialog should appear for any media flow):
- [ ] Create post → add image → Photo Picker opens without dialog
- [ ] Create post → add video → Photo Picker opens without dialog
- [ ] Create story → Photo Picker opens without dialog
- [ ] Create clip → Photo Picker (video) opens without dialog
- [ ] Livestream → select thumbnail → Photo Picker opens without dialog
- [ ] Chat → send image → Photo Picker opens without dialog
- [ ] Camera → photo capture → camera permission dialog only (no storage dialog)
- [ ] Log file is written (check `getExternalFilesDir(DIRECTORY_DOCUMENTS)` path)
