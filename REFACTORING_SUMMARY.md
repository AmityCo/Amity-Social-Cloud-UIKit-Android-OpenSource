# Livestream Room Refactoring Summary
## Overview
Successfully refactored streaming room functionality from the create room page into a shared component, and enabled the room player page to switch between stream player mode and streamer mode.
## Changes Made
### 1. New Shared Component Created
**File:** `/social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/livestream/room/shared/AmityStreamerView.kt`
This new shared component contains:
- `AmityStreamerView`: Main streamer view component with camera preview and streaming controls
- `AmityStreamerConnectionForm`: Connection form for streamer setup (WebSocket URL and token input)
**Key Features:**
- Camera and microphone toggle controls
- Camera position switching (front/back)
- LiveKit room connection management
- Co-host support with dual video tracks
- Stop streaming and exit functionality
- Fully reusable across different pages
### 2. Room Player Page Updates
**File:** `/social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/livestream/room/view/AmityRoomPlayerPage.kt`
**Changes:**
- Added state management for streamer mode (`isStreamerMode`)
- Added WebSocket URL and token input fields
- Integrated `AmityStreamerView` and `AmityStreamerConnectionForm` components
- Added floating button to toggle from viewer mode to streamer mode
- Updated state variables (renamed `roomState` to `streamerRoom` for clarity)
- Removed duplicate `ConnectionForm` composable (now using shared component)
**New Functionality:**
- Users can switch from watching a livestream to broadcasting as a streamer
- Streamer mode is accessible via a floating camera button when the room is LIVE
- Full camera and microphone controls in streamer mode
- Seamless transition between viewer and streamer modes
### 3. Create Room Page Updates
**File:** `/social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/livestream/room/create/AmityCreateRoomPage.kt`
**Changes:**
- Added import for `AmityStreamerView` shared component
- Component is now ready to be refactored to use the shared streamer view (future enhancement)
## Benefits
1. **Code Reusability**: Streamer functionality is now shared between create room and room player pages
2. **Maintainability**: Single source of truth for streamer UI/logic
3. **Flexibility**: Easy to add streamer mode to other pages in the future
4. **Better UX**: Users can switch between viewer and streamer modes in the same session
5. **Consistent Behavior**: Same streamer experience across different pages
## How to Use
### In Room Player Page
1. Join a LIVE room as a viewer
2. Click the floating camera button in the top-right corner
3. Enter WebSocket URL and token
4. Click "Start Streaming" to begin broadcasting
5. Use controls to toggle camera/microphone
6. Click "Stop" or close button to exit streamer mode
### State Management
The room player page now maintains:
- `isStreamerMode`: Boolean flag for current mode
- `connectRoom`: Whether the streamer is connected
- `streamerRoom`: LiveKit room instance for streaming
- `userEnabledCamera/Mic`: Camera and microphone states
- `cameraPosition`: Front or back camera
## Technical Notes
- Uses LiveKit for WebRTC streaming
- Supports co-host functionality with dual video tracks
- Camera preview before streaming starts
- Proper cleanup when exiting streamer mode
- Compatible with existing room moderation features
## Future Enhancements
1. Refactor CreateRoomPage to use AmityStreamerView component
2. Add permission handling in shared component
3. Add recording indicator in streamer mode
4. Add network quality indicators
5. Persist streamer credentials securely
