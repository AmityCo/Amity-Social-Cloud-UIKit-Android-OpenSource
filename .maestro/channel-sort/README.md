# PDT-5753 — channel list sorted by last message

## What these flows cover

| Flow | Covers |
| ---- | ------ |
| `00-login.yaml` | Logs in on staging. `sortBy=lastMessage` is flag-gated per environment and the flag is on for staging. |
| `01-send-moves-channel-to-top.yaml` | Sending a message moves that channel to the top of the chat list, at send time. |

## Running them

```
maestro test .maestro/channel-sort/00-login.yaml -e USER_ID=<id>
maestro test .maestro/channel-sort/01-send-moves-channel-to-top.yaml \
  -e USER_ID=<id> \
  -e TOP_CHANNEL="<name of the top row>" \
  -e LOWER_CHANNEL="<name of a channel below it>"
```

`01` logs in itself. The sample app's session does not survive a plain `launchApp`, so it cannot
rely on `00-login` having run in a previous invocation.

## Account requirement

`01` needs an account with at least two chat channels that the user is a member of, and the
channel passed as `LOWER_CHANNEL` must start below `TOP_CHANNEL`.

`Web-Test` on staging has **no** chat channels, so it cannot be used. `fifa` on staging has about
ten and is what the run below used.

## Results — 2026-09-28, staging, emulator (Pixel_7, API 36)

### `01` passes

```
maestro test .maestro/channel-sort/01-send-moves-channel-to-top.yaml \
  -e USER_ID=fifa -e TOP_CHANNEL="MQTT" -e LOWER_CHANNEL="Chet79"
```

All steps completed. `Chet79` started third, and after sending it was first, above `MQTT`,
showing the sent text and a "Now" timestamp.

This is new behaviour on Android. Before this change the SDK did not update the channel row on
the user's own send at all — it waited for the `message.created` echo. Note that `01` passes under
**both** sort values, because sending moves `lastActivity` and `lastMessageAt` together. It proves
the send path, not the difference between the two sorts.

### The two sorts really do order differently

Same account, same moment, only `feature_flags.chat.channel_list_sort_by` changed. Rows 2 and 4
swap:

| Position | `lastActivity` | `lastMessage` |
| -------- | -------------- | ------------- |
| 1 | Doyok, rezyfr, brezy, rezyblkA (8 Sep) | Doyok, rezyfr, brezy, rezyblkA (8 Sep) |
| 2 | test123456 (3 Sep) | **MQTT (19 Aug)** |
| 3 | Chet79 (2 Sep) | Chet79 (2 Sep) |
| 4 | MQTT (19 Aug) | **test123456 (3 Sep)** |

The date on each row is the last message's time, so under `lastMessage` the order does not follow
the visible dates. Every channel on this account except the one just posted to has
`lastMessageAt = null` — they all predate the backend release and the backfill has not reached
them — so they are ordered by `createdAt` instead. `MQTT` was created after `test123456`.

Two consequences worth knowing before reading anything into this table:

- **It does not yet exercise the null-ordering rule.** With only one non-null channel, ordering
  nulls last (getChannels REQ-009) and ordering them by `COALESCE(lastMessageAt, createdAt)` give
  the same visible order. The two rules differ only when a null channel's `createdAt` is newer
  than a non-null channel's `lastMessageAt`: `COALESCE` interleaves them, nulls-last does not.
  Re-check with a mix of both.
- **Once the backfill lands the order will change.** It will follow the real last-message times,
  which is what the row dates already show.

### The staging flag is on

The chat home page loads with `channel_list_sort_by` set to `"lastMessage"` and the channel query
returns no `422` / `500000`. The backend accepts the value the SDK sends.

## Building for `lastMessage`

The committed default in `common/src/main/assets/config.json` is `"lastActivity"`, which keeps the
behaviour every existing integrator has today. To exercise `lastMessage`, set that key to
`"lastMessage"` and rebuild. Until the SDK change of this ticket is released, also uncomment the
`includeBuild('../Amity-Social-Cloud-SDK-Android')` block in `settings.gradle` so the app builds
against the local SDK branch. Neither change belongs in a commit.
