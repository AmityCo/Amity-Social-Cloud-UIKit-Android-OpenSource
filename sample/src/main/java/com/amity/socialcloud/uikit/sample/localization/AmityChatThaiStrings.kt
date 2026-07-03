package com.amity.socialcloud.uikit.sample.localization

/**
 * Thai (th) translations for chat-compose strings.
 *
 * Usage in your Application.onCreate():
 *   DefaultAmityChatStringProvider.setLocale("th", AmityChatThaiStrings.strings)
 */
object AmityChatThaiStrings {

    val strings: Map<String, String> = mapOf(

        // Chat Home
        "chat.home.title" to "แชท",
        "chat.tab.all" to "ทั้งหมด",
        "chat.tab.direct" to "แชทส่วนตัว",
        "chat.tab.groups" to "กลุ่ม",
        "chat.home.empty.title" to "ยังไม่มีบทสนทนา",
        "chat.home.empty.description" to "มาสร้างแชทเพื่อเริ่มต้นกันเลย",
        "chat.create.direct" to "แชทส่วนตัว",
        "chat.create.group" to "แชทกลุ่ม",
        "chat.archived.navbar.title" to "แชทที่เก็บถาวร",

        // Conversation
        "chat.unsupported.message" to "ประเภทข้อความไม่รองรับ",
        "chat.message.deleted" to "ข้อความนี้ถูกลบแล้ว",
        "chat.load.error" to "ไม่สามารถโหลดแชทได้",
        "chat.composer.placeholder" to "พิมพ์ข้อความ…",
        "chat.replying.to" to "ตอบกลับ %1\$s",
        "chat.editing.message" to "กำลังแก้ไขข้อความ",

        // Chat User Actions
        "chat.action.mute" to "ปิดการแจ้งเตือนแล้ว",
        "chat.action.unmute" to "เปิดการแจ้งเตือนแล้ว",
        "chat.action.report.user" to "รายงานผู้ใช้",
        "chat.action.unreport.user" to "ยกเลิกรายงานผู้ใช้",
        "chat.action.block.user" to "บล็อกผู้ใช้",
        "chat.action.unblock.user" to "ยกเลิกการบล็อกผู้ใช้",
        "chat.blocked.message" to "คุณไม่สามารถส่งข้อความถึงบุคคลนี้ได้",
        "chat.block.confirm.title" to "บล็อกผู้ใช้",
        "chat.block.confirm.message" to "คุณแน่ใจหรือไม่ว่าต้องการบล็อก %1\$s? พวกเขาจะไม่ได้รับการแจ้งเตือน",
        "chat.unblock.confirm.title" to "ยกเลิกการบล็อกผู้ใช้",
        "chat.unblock.confirm.message" to "คุณแน่ใจหรือไม่ว่าต้องการยกเลิกการบล็อก %1\$s?",
        "chat.block.success" to "บล็อกผู้ใช้แล้ว",
        "chat.unblock.success" to "ยกเลิกการบล็อกผู้ใช้แล้ว",

        // Message Options
        "chat.option.reply" to "ตอบกลับ",
        "chat.option.copy" to "คัดลอก",
        "chat.option.edit" to "แก้ไข",
        "chat.option.delete" to "ลบ",
        "chat.option.report" to "รายงาน",
        "chat.option.unreport" to "ยกเลิกรายงาน",
        "chat.action.save" to "บันทึก",

        // Failed / status messages
        "chat.message.not.sent" to "ข้อความของคุณไม่ถูกส่ง",
        "chat.message.resend" to "ส่งใหม่",
        "chat.message.failed.to.send" to "ส่งข้อความไม่สำเร็จ",
        "chat.cancel" to "ยกเลิก",

        // Group chat
        "chat.group.member.count" to "%1\$d สมาชิก",
        "chat.waiting.for.network" to "กำลังรอเครือข่าย…",
        "chat.group.user.muted" to "คุณถูกปิดเสียง คุณจะไม่สามารถส่งข้อความได้",
        "chat.group.permission.only.moderators.banner" to "สมาชิกที่ไม่ใช่ผู้ดูแลสามารถอ่านได้แต่ไม่สามารถส่งข้อความได้",
        "chat.group.user.banned" to "คุณถูกแบนจากกลุ่มนี้ คุณไม่สามารถส่งข้อความได้",

        // Create chat
        "chat.create.conversation.title" to "บทสนทนาใหม่",
        "chat.no.users.found" to "ไม่พบผู้ใช้",
        "chat.select.members.title" to "กลุ่มใหม่",
        "chat.next" to "ถัดไป",
        "chat.create.group.title" to "กลุ่มใหม่",
        "chat.group.name.label" to "ชื่อกลุ่ม",
        "chat.group.name.placeholder" to "ตั้งชื่อกลุ่มของคุณ",
        "chat.group.members.label" to "สมาชิก",
        "chat.create.group.button" to "สร้าง",

        // Group settings
        "chat.group.settings.section" to "การตั้งค่ากลุ่ม",
        "chat.group.notifications" to "การแจ้งเตือนกลุ่ม",
        "chat.your.preferences.section" to "การตั้งค่าของคุณ",
        "chat.edit.group.profile.navbar.title" to "โปรไฟล์กลุ่ม",
        "chat.group.members" to "สมาชิก",
        "chat.group.leave" to "ออกจากกลุ่ม",
        "chat.group.leave.confirm.title" to "ออกจากกลุ่ม",
        "chat.group.leave.confirm.message" to "คุณแน่ใจหรือไม่ว่าต้องการออกจากกลุ่มนี้?",
        "chat.group.leave.last.mod.title" to "คุณเป็นผู้ดูแลคนเดียว",
        "chat.group.leave.last.mod.message" to "โปรดเลื่อนตำแหน่งสมาชิกคนอื่นเป็นผู้ดูแลก่อนออกจากกลุ่ม",
        "chat.group.promote.member" to "เลื่อนตำแหน่งสมาชิก",
        "chat.group.save" to "บันทึก",
        "chat.group.profile" to "โปรไฟล์กลุ่ม",
        "chat.group.name.required" to "(จำเป็น)",
        "chat.group.name.optional" to "(ไม่บังคับ)",
        "chat.privacy.label" to "ความเป็นส่วนตัว",
        "chat.create.group.public.title" to "สาธารณะ",
        "chat.create.group.public.subtitle" to "ทุกคนสามารถค้นหากลุ่มผ่านการค้นหาและเข้าร่วมบทสนทนาได้",
        "chat.create.group.private.title" to "ส่วนตัว",
        "chat.create.group.private.subtitle" to "กลุ่มถูกซ่อนจากการค้นหาและเข้าถึงได้เฉพาะผู้ที่ได้รับเชิญจากผู้ดูแลเท่านั้น",

        // Member List
        "chat.member.list.title" to "สมาชิกทั้งหมด",
        "chat.group.member.list.tab.title" to "สมาชิก",
        "chat.member.tab.moderators" to "ผู้ดูแล",
        "chat.member.action.promote" to "เลื่อนตำแหน่งเป็นผู้ดูแล",
        "chat.member.action.demote" to "ลดตำแหน่งจากผู้ดูแล",
        "chat.member.action.remove" to "นำออกจากกลุ่ม",
        "chat.user.action.ban" to "แบนจากกลุ่ม",
        "chat.mute.confirm.label" to "ปิดเสียง",
        "chat.unmute.confirm.label" to "เปิดเสียง",
        "chat.member.you.suffix" to "(คุณ)",
        "chat.member.you" to "คุณ",
        "chat.add.member.title" to "เพิ่มสมาชิก",
        "chat.add.member.button" to "เพิ่ม",

        // Search
        "chat.search.placeholder" to "ค้นหาช่อง…",
        "chat.search.min.chars" to "พิมพ์อย่างน้อย 3 ตัวอักษร",
        "chat.search.no.results" to "ไม่พบผลลัพธ์",
        "chat.search.tab.chats" to "แชท",
        "chat.search.tab.messages" to "ข้อความ",

        // Message Report
        "chat.report.title" to "เหตุผลในการรายงาน",
        "chat.report.description" to "บอกเราว่าทำไมคุณถึงรายงานเนื้อหานี้ รายงานของคุณจะถูกตรวจสอบโดยผู้ดูแลและเก็บเป็นความลับ",
        "chat.report.submit" to "ส่ง",
        "chat.toast.message.reported" to "รายงานข้อความแล้ว",
        "chat.unreport.success" to "ยกเลิกรายงานข้อความแล้ว",
        "chat.report.others" to "อื่นๆ",

        // Notification Preferences
        "chat.group.notif.pref.navbar.title" to "การแจ้งเตือน",
        "chat.notifications.on" to "เปิด",
        "chat.notifications.off" to "ปิด",
        "chat.group.notification.preference.description" to "เปิดเพื่อรับการแจ้งเตือนแบบพุชจากกลุ่มนี้",
        "chat.group.notifications.disabled" to "การแจ้งเตือนกลุ่มถูกปิดใช้งานโดยผู้ดูแล",
        "chat.notifications.disabled" to "การแจ้งเตือนถูกปิดใช้งาน เปิดใช้งานการแจ้งเตือนเพื่อรับข้อความ",

        // Notification Modes
        "chat.group.notification.default.label" to "ค่าเริ่มต้น",
        "chat.edit.group.notif.mode.default.desc" to "โดยค่าเริ่มต้น สมาชิกในกลุ่มนี้จะได้รับการแจ้งเตือน แต่สามารถเลือกปิดได้",
        "group.notification.silent" to "โหมดเงียบ",
        "group.notification.silent.desc" to "ไม่มีการแจ้งเตือนสำหรับทุกคนในช่องนี้ สมาชิกไม่สามารถเปิดการแจ้งเตือนในช่องได้",
        "group.notification.subscribe" to "โหมดสมัครรับข้อมูล",
        "group.notification.subscribe.desc" to "สมาชิกทุกคนมีตัวเลือกรับการแจ้งเตือน แต่ต้องเปิดใช้งานเอง โดยค่าเริ่มต้นการแจ้งเตือนจะปิดสำหรับแต่ละสมาชิก",

        // Media Composer
        "chat.media.camera" to "กล้อง",
        "chat.media.photo" to "มีเดีย",
        "chat.reply.photo.label" to "รูปภาพ",
        "chat.reply.video.label" to "วิดีโอ",

        // Member Actions (extended)
        "chat.option.report" to "รายงาน",
        "chat.member.action.unreport" to "ยกเลิกรายงาน",
        "chat.mute.confirm.title" to "ยืนยันการปิดเสียง",
        "chat.mute.confirm.message" to "คุณแน่ใจหรือไม่ว่าต้องการปิดเสียงผู้ใช้นี้? พวกเขาจะไม่สามารถส่งหรือรีแอคชันข้อความได้อีกต่อไป",
        "chat.unmute.confirm.title" to "ยืนยันการเปิดเสียง",
        "chat.unmute.confirm.message" to "คุณแน่ใจหรือไม่ว่าต้องการเปิดเสียงผู้ใช้นี้? พวกเขาสามารถส่งหรือรีแอคชันข้อความได้แล้ว",
        "chat.ban.confirm.title" to "แบนผู้ใช้",
        "chat.ban.confirm.message" to "คุณแน่ใจหรือไม่ว่าต้องการแบนผู้ใช้นี้? พวกเขาจะไม่สามารถเข้าร่วมในกลุ่มนี้ได้อีกต่อไป",
        "chat.unban.confirm.title" to "ยกเลิกการแบนผู้ใช้",
        "chat.unban.confirm.message" to "คุณแน่ใจหรือไม่ว่าต้องการยกเลิกการแบนผู้ใช้นี้?",

        // Banned Members Page
        "chat.banned.member.list.navbar.title" to "ผู้ใช้ที่ถูกแบน",
        "chat.banned.members.empty" to "ยังไม่มีอะไรที่นี่",
        "chat.member.action.unban" to "ยกเลิกการแบน",

        // Message Preview
        "chat.preview.sent.photo" to "ส่งรูปภาพ",
        "chat.preview.sent.video" to "ส่งวิดีโอ",
        "chat.preview.deleted" to "ข้อความนี้ถูกลบแล้ว",
        "chat.preview.no.message" to "ยังไม่มีข้อความ",
        "chat.deleted.user" to "ผู้ใช้ที่ถูกลบ",
        "chat.archived.toast" to "เก็บถาวรแชทแล้ว",
        "chat.create.new.chat" to "สร้างแชทใหม่",

        // Archive / Unarchive
        "chat.archive" to "เก็บถาวร",
        "chat.unarchive" to "ยกเลิกการเก็บถาวร",
        "chat.unarchived.toast" to "ยกเลิกการเก็บถาวรแชทแล้ว",
        "chat.archive.error.toast" to "ไม่สามารถเก็บถาวรแชทได้ กรุณาลองใหม่",
        "chat.unarchive.error.toast" to "ไม่สามารถยกเลิกการเก็บถาวรแชทได้ กรุณาลองใหม่",
        "chat.archive.limit.title" to "เก็บถาวรแชทมากเกินไป",
        "chat.archive.limit.message" to "คุณสามารถเก็บถาวรได้สูงสุด 100 รายการแชท",

        // Member Permissions
        "chat.group.member.permissions.navbar.title" to "สิทธิ์สมาชิก",
        "chat.group.edit.permissions.messaging.title" to "การส่งข้อความ",
        "chat.group.edit.permissions.everyone.title" to "ทุกคน",
        "chat.group.edit.permissions.everyone.description" to "สมาชิกทุกคนสามารถส่งข้อความได้",
        "chat.group.edit.permissions.moderators.only.title" to "ผู้ดูแลเท่านั้น",
        "chat.group.edit.permissions.moderators.only.description" to "เฉพาะผู้ดูแลเท่านั้นที่สามารถส่งข้อความได้",

        // Settings (extended)
        "chat.group.banned.members" to "ผู้ใช้ที่ถูกแบน",
        "chat.group.member.permissions" to "สิทธิ์สมาชิก",

        // Message Bubble
        "chat.see.more" to "ดูเพิ่มเติม",

        // Reply
        "chat.reply.you" to "คุณตอบกลับแล้ว",
        "chat.reply.you.to.yourself" to "คุณตอบกลับตัวเอง",
        "chat.reply.to.you" to "ตอบกลับคุณ",
        "chat.reply.to.themself" to "ตอบกลับตัวเอง",
        "chat.reply.you.to.deleted" to "คุณตอบกลับข้อความที่ถูกลบ",
        "chat.reply.to.deleted" to "ตอบกลับข้อความที่ถูกลบ",

        // Status strings
        "chat.status.edited" to "แก้ไขแล้ว",
        "chat.sending.status" to "กำลังส่ง…",
        "chat.status.failed" to "ล้มเหลว",
        "chat.preview.not.available" to "ไม่สามารถแสดงตัวอย่างได้",
        "chat.preview.message" to "ข้อความ",

        // Success / error snackbar strings
        "chat.action.report.user.success" to "รายงานผู้ใช้แล้ว",
        "chat.action.unreport.user.success" to "ยกเลิกรายงานผู้ใช้แล้ว",
        "chat.error.report.user" to "ไม่สามารถรายงานผู้ใช้ได้",
        "chat.error.unreport.user" to "ไม่สามารถยกเลิกรายงานผู้ใช้ได้",
        "chat.error.update.notification" to "ไม่สามารถอัปเดตการตั้งค่าการแจ้งเตือนได้",
        "chat.error.leave.group.failed" to "ไม่สามารถออกจากกลุ่มได้",
        "chat.toast.message.reported.error" to "ไม่สามารถรายงานข้อความได้",
        "chat.success.member.added" to "เพิ่มสมาชิกแล้ว",
        "chat.add.group.member.toast.failed" to "ไม่สามารถเพิ่มสมาชิกได้",
        "chat.action.user.removed" to "นำสมาชิกออกแล้ว",
        "chat.error.remove.member" to "ไม่สามารถนำสมาชิกออกได้",
        "chat.error.promote.member" to "ไม่สามารถเลื่อนตำแหน่งผู้ใช้ได้",
        "chat.action.demote.member.failed" to "ไม่สามารถลดตำแหน่งผู้ใช้ได้",
        "chat.action.mute.user" to "ปิดเสียงผู้ใช้แล้ว",
        "chat.error.mute.member" to "ไม่สามารถปิดเสียงผู้ใช้ได้",
        "chat.action.unmute.user" to "เปิดเสียงผู้ใช้แล้ว",
        "chat.error.unmute.member" to "ไม่สามารถเปิดเสียงผู้ใช้ได้",
        "chat.action.ban.member.failed" to "ไม่สามารถแบนสมาชิกได้",
        "chat.action.unban.user" to "ยกเลิกการแบนสมาชิกแล้ว",
        "chat.error.unban.member" to "ไม่สามารถยกเลิกการแบนสมาชิกได้",
        "chat.privacy.warning" to "ตรวจสอบให้แน่ใจว่าเลือกการตั้งค่าความเป็นส่วนตัวที่ถูกต้องสำหรับกลุ่มของคุณ เนื่องจากไม่สามารถเปลี่ยนแปลงได้ในภายหลัง",
        "chat.save.photo.success" to "บันทึกรูปภาพแล้ว",
        "chat.save.photo.failed" to "ไม่สามารถบันทึกรูปภาพได้",
        "chat.save.video.success" to "บันทึกวิดีโอแล้ว",
        "chat.save.video.failed" to "ไม่สามารถบันทึกวิดีโอได้",
        "chat.archived.empty.title" to "ไม่มีแชทที่เก็บถาวร",
        "chat.archived.badge.label" to "เก็บถาวรแล้ว",
        "chat.group.notification.save.success" to "บันทึกการตั้งค่าการแจ้งเตือนแล้ว",
        "group.notification.save.error" to "ไม่สามารถบันทึกการตั้งค่าการแจ้งเตือนได้",

        // Notification Preferences (extended)
        "chat.group.notification.preference.title" to "อนุญาตการแจ้งเตือน",

        // Member List toasts
        "chat.group.member.list.toast.banned" to "แบนผู้ใช้แล้ว",
        "chat.group.member.list.toast.promoted" to "เลื่อนตำแหน่งสมาชิกแล้ว",
        "chat.group.member.list.toast.demoted" to "ลดตำแหน่งสมาชิกแล้ว",

        // Message Actions
        "chat.mention.everyone" to "แจ้งเตือนทุกคน",

        // --- Missing translations added below ---

        // Action failures
        "chat.action.leave.group.failed" to "ไม่สามารถออกจากแชทกลุ่มได้ กรุณาลองใหม่",
        "chat.action.mute.failed" to "ไม่สามารถปิดการแจ้งเตือนได้ กรุณาลองใหม่",
        "chat.action.mute.user.failed" to "ไม่สามารถปิดเสียงผู้ใช้ได้ กรุณาลองใหม่",
        "chat.action.promote.member.failed" to "ไม่สามารถเลื่อนตำแหน่งสมาชิกได้ กรุณาลองใหม่",
        "chat.action.remove.member" to "นำสมาชิกออกแล้ว",
        "chat.action.remove.member.failed" to "ไม่สามารถนำสมาชิกออกได้ กรุณาลองใหม่",
        "chat.action.report.user.failed" to "ไม่สามารถรายงานผู้ใช้ได้ กรุณาลองใหม่",
        "chat.action.turn.off.notification" to "ปิดการแจ้งเตือน",
        "chat.action.turn.on.notification" to "เปิดการแจ้งเตือน",
        "chat.action.unban.user.failed" to "ไม่สามารถยกเลิกการแบนผู้ใช้ได้ กรุณาลองใหม่",
        "chat.action.unmute.failed" to "ไม่สามารถเปิดการแจ้งเตือนได้ กรุณาลองใหม่",
        "chat.action.unmute.user.failed" to "ไม่สามารถเปิดเสียงผู้ใช้ได้ กรุณาลองใหม่",
        "chat.action.unreport.user.failed" to "ไม่สามารถยกเลิกรายงานผู้ใช้ได้ กรุณาลองใหม่",

        // Add member
        "chat.add.member.chip" to "เพิ่ม",

        // Archived
        "chat.archived" to "เก็บถาวรแล้ว",

        // Confirm labels
        "chat.ban.confirm.label" to "แบน",
        "chat.block.confirm.label" to "บล็อก",
        "chat.block.failed" to "ไม่สามารถบล็อกผู้ใช้ได้ กรุณาลองใหม่",
        "chat.unban.confirm.label" to "ยกเลิกการแบน",
        "chat.unblock.confirm.label" to "ยกเลิกการบล็อก",
        "chat.unblock.failed" to "ไม่สามารถยกเลิกการบล็อกผู้ใช้ได้ กรุณาลองใหม่",

        // Link preview
        "chat.bubble.link.preview.no.data" to "ไม่มีข้อมูลแสดง",

        // Button
        "chat.button.ok" to "ตกลง",

        // Character limit alert
        "chat.char.limit.alert.message" to "ข้อความของคุณยาวเกินไป กรุณาย่อข้อความแล้วลองใหม่",
        "chat.char.limit.alert.title" to "ไม่สามารถส่งข้อความได้",

        // Create group
        "chat.create.group.error" to "ไม่สามารถสร้างแชทกลุ่มได้ กรุณาลองใหม่",
        "chat.create.group.success" to "สร้างแชทกลุ่มแล้ว",

        // Delete message alert
        "chat.delete.alert.message" to "ข้อความนี้จะถูกลบจากอุปกรณ์ของเพื่อนคุณด้วย",
        "chat.delete.alert.title" to "ลบข้อความนี้?",

        // Edit group profile
        "chat.edit.group.profile.name.placeholder" to "กรุณาใส่ชื่อกลุ่ม",

        // Banned from chat
        "chat.error.banned.chat.sub.title" to "จากกิจกรรมที่ผ่านมาของคุณ คุณถูกแบนจากกลุ่มนี้",
        "chat.error.banned.chat.title" to "คุณถูกแบน",

        // Group edit save buttons
        "chat.group.edit.notification.save" to "บันทึก",
        "chat.group.edit.permission.save" to "บันทึก",
        "chat.group.edit.profile" to "อัปเดตโปรไฟล์กลุ่มแล้ว",
        "chat.group.edit.profile.failed" to "ไม่สามารถอัปเดตโปรไฟล์กลุ่มได้ กรุณาลองใหม่",
        "chat.group.edit.profile.save" to "บันทึก",

        // Group leave
        "chat.group.leave.confirm.label" to "ออก",

        // Group member actions
        "chat.group.member.action.mute" to "ปิดเสียงผู้ใช้",
        "chat.group.member.action.unmute" to "เปิดเสียงผู้ใช้",

        // Member list - demote
        "chat.group.member.list.demote.confirm" to "ลดตำแหน่ง",
        "chat.group.member.list.demote.message" to "คุณแน่ใจหรือไม่ว่าต้องการลดตำแหน่งผู้ดูแลคนนี้? พวกเขาจะไม่สามารถใช้ฟีเจอร์ของผู้ดูแลได้อีก",
        "chat.group.member.list.demote.title" to "ลดตำแหน่งผู้ดูแล",

        // Member list - promote
        "chat.group.member.list.promote.confirm" to "เลื่อนตำแหน่ง",
        "chat.group.member.list.promote.message" to "คุณแน่ใจหรือไม่ว่าต้องการเลื่อนตำแหน่งสมาชิกคนนี้เป็นผู้ดูแล? พวกเขาจะสามารถใช้ฟีเจอร์ของผู้ดูแลทั้งหมดได้",
        "chat.group.member.list.promote.title" to "เลื่อนตำแหน่งเป็นผู้ดูแล",

        // Member list - remove
        "chat.group.member.list.remove.confirm" to "นำออก",
        "chat.group.member.list.remove.message" to "คุณแน่ใจหรือไม่ว่าต้องการนำสมาชิกคนนี้ออกจากกลุ่ม? พวกเขาจะทราบว่าถูกนำออก",
        "chat.group.member.list.remove.title" to "ยืนยันการนำออก",

        // Group notification modes
        "chat.group.notification.default.desc" to "โดยค่าเริ่มต้น สมาชิกในชุมชนนี้จะได้รับการแจ้งเตือน แต่สามารถเลือกปิดได้",
        "chat.group.notification.default.title" to "โหมดค่าเริ่มต้น",
        "chat.group.notification.silent.desc" to "ไม่มีการแจ้งเตือนสำหรับทุกคนในช่องนี้ สมาชิกไม่สามารถเปิดการแจ้งเตือนในช่องได้",
        "chat.group.notification.silent.label" to "ปิดเสียง",
        "chat.group.notification.silent.title" to "โหมดปิดเสียง",
        "chat.group.notification.subscribe.desc" to "สมาชิกทุกคนมีตัวเลือกในการรับการแจ้งเตือน แต่ต้องเปิดเอง โดยค่าเริ่มต้นการแจ้งเตือนจะปิดสำหรับสมาชิกแต่ละคน",
        "chat.group.notification.subscribe.label" to "ติดตาม",
        "chat.group.notification.subscribe.title" to "โหมดติดตาม",

        // Home menu
        "chat.home.menu.archived" to "เก็บถาวร",

        // Banned label
        "chat.label.banned.from.chat" to "คุณถูกแบนจากแชท",

        // Leave without finishing
        "chat.leave.without.finishing.label" to "ออก",
        "chat.leave.without.finishing.message" to "ความคืบหน้าของคุณจะไม่ถูกบันทึก และกลุ่มจะไม่ถูกสร้าง",
        "chat.leave.without.finishing.title" to "ออกโดยไม่เสร็จสิ้น?",

        // Loading
        "chat.loading.label" to "กำลังโหลดแชท…",

        // Member limit
        "chat.member.limit.reached.message" to "คุณมีสมาชิกครบจำนวนสูงสุดที่อนุญาตในกลุ่มนี้แล้ว",
        "chat.member.limit.reached.title" to "ถึงจำนวนสมาชิกสูงสุดแล้ว",

        // Messages
        "chat.message.no.preview" to "ไม่รองรับการแสดงตัวอย่างสำหรับข้อความประเภทนี้",
        "chat.message.replying.yourself" to "ตัวเอง",

        // No members
        "chat.no.members.found" to "ไม่พบสมาชิก",

        // Notifications
        "chat.notifications.title" to "การแจ้งเตือน",

        // Mention limit
        "chat.reach.mention.limit.message" to "คุณสามารถกล่าวถึงได้สูงสุด 30 คนต่อข้อความ",
        "chat.reach.mention.limit.title" to "กล่าวถึงผู้ใช้มากเกินไป",

        // Reply patterns
        "chat.reply.name.to.name" to "%1\$s ตอบกลับ %2\$s",
        "chat.reply.name.to.themself" to "%s ตอบกลับตัวเอง",
        "chat.reply.name.to.you" to "%s ตอบกลับคุณ",
        "chat.reply.you.to.name" to "คุณตอบกลับ %s",

        // Timestamp
        "chat.timestamp.now" to "ตอนนี้",

        // Toast messages
        "chat.toast.banned.word" to "ไม่สามารถส่งข้อความได้เนื่องจากมีคำที่ถูกบล็อก",
        "chat.toast.copied" to "คัดลอกแล้ว",
        "chat.toast.delete.error" to "ไม่สามารถลบข้อความได้ กรุณาลองใหม่",
        "chat.toast.group.chat.left" to "ออกจากแชทกลุ่มแล้ว",
        "chat.toast.link.not.allow" to "ไม่สามารถส่งข้อความได้เนื่องจากมีลิงก์ที่ไม่อนุญาต",
        "chat.toast.member.added" to "เพิ่มสมาชิกแล้ว",
        "chat.toast.members.add.error" to "ไม่สามารถเพิ่มสมาชิกได้ กรุณาลองใหม่",
        "chat.toast.members.added" to "เพิ่มสมาชิกแล้ว",
        "chat.toast.reply.parent.deleted" to "ข้อความนี้ไม่พร้อมใช้งานอีกต่อไป",
        "chat.toast.unreport.message.error" to "ไม่สามารถยกเลิกรายงานข้อความได้ กรุณาลองใหม่",

        // Unknown user
        "chat.unknown.user" to "ไม่ทราบชื่อ",

        // User is muted
        "chat.user.is.muted" to "คุณถูกปิดเสียง คุณจะไม่สามารถส่งข้อความได้",

        "chat.reaction.label.like" to "ถูกใจ",
        "chat.reaction.label.love" to "รัก",
        "chat.reaction.label.fire" to "เจ๋ง",
        "chat.reaction.label.happy" to "ฮา",
        "chat.reaction.label.sad" to "เศร้า",

        // Edit group permissions
        "chat.edit.group.perm.toast.failed" to "ไม่สามารถบันทึกสิทธิ์ได้ กรุณาลองใหม่",
        "chat.edit.group.perm.toast.success" to "บันทึกสิทธิ์เรียบร้อยแล้ว",

        // Banned chat
        "chat.error.banned.chat.navbar.title" to "แชท",

        // Jump to message
        "chat.jump.to.message.unavailable" to "ข้อความไม่พร้อมใช้งาน",

        // Report error
        "chat.report.error.close" to "ปิด",
        "chat.report.error.desc" to "ข้อความที่คุณกำลังค้นหาไม่พร้อมใช้งาน",
        "chat.report.error.title" to "เกิดข้อผิดพลาด",

        // Report other reason
        "chat.report.other.reason.desc" to "อธิบายเหตุผลของคุณ",
        "chat.report.other.reason.optional" to "(ไม่บังคับ)",
        "chat.report.other.reason.placeholder" to "แชร์รายละเอียดเพิ่มเติมเกี่ยวกับปัญหานี้",

        // Unreport
        "chat.toast.un.report.message" to "ยกเลิกรายงานข้อความแล้ว",
        "chat.toast.un.report.message.error" to "ไม่สามารถยกเลิกรายงานข้อความได้ กรุณาลองใหม่",
    )
}
