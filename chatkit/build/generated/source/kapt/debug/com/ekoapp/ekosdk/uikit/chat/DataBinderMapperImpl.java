package com.ekoapp.ekosdk.uikit.chat;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityActivityChatHomeBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityActivityEditMessageBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityChatBarBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityChatComposeBarBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityEditMsgBarBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityFragmentChatBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityFragmentRecentChatBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemAudioMessageReceiverBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemAudioMessageSenderBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemImageMsgReceiverBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemImageMsgSenderBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemRecentMessageBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemTextMessageReceiverBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemTextMessageSenderBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityItemUnknownMessageBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityPopupMsgDeleteBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityPopupMsgReportBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityPopupTextMsgSenderBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBindingImpl;
import com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgErrorBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_AMITYACTIVITYCHATHOME = 1;

  private static final int LAYOUT_AMITYACTIVITYEDITMESSAGE = 2;

  private static final int LAYOUT_AMITYCHATBAR = 3;

  private static final int LAYOUT_AMITYCHATCOMPOSEBAR = 4;

  private static final int LAYOUT_AMITYEDITMSGBAR = 5;

  private static final int LAYOUT_AMITYFRAGMENTCHAT = 6;

  private static final int LAYOUT_AMITYFRAGMENTRECENTCHAT = 7;

  private static final int LAYOUT_AMITYITEMAUDIOMESSAGERECEIVER = 8;

  private static final int LAYOUT_AMITYITEMAUDIOMESSAGESENDER = 9;

  private static final int LAYOUT_AMITYITEMIMAGEMSGRECEIVER = 10;

  private static final int LAYOUT_AMITYITEMIMAGEMSGSENDER = 11;

  private static final int LAYOUT_AMITYITEMRECENTMESSAGE = 12;

  private static final int LAYOUT_AMITYITEMTEXTMESSAGERECEIVER = 13;

  private static final int LAYOUT_AMITYITEMTEXTMESSAGESENDER = 14;

  private static final int LAYOUT_AMITYITEMUNKNOWNMESSAGE = 15;

  private static final int LAYOUT_AMITYPOPUPMSGDELETE = 16;

  private static final int LAYOUT_AMITYPOPUPMSGREPORT = 17;

  private static final int LAYOUT_AMITYPOPUPTEXTMSGSENDER = 18;

  private static final int LAYOUT_AMITYVIEWDATE = 19;

  private static final int LAYOUT_AMITYVIEWMSGDELETED = 20;

  private static final int LAYOUT_AMITYVIEWMSGERROR = 21;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(21);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_activity_chat_home, LAYOUT_AMITYACTIVITYCHATHOME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_activity_edit_message, LAYOUT_AMITYACTIVITYEDITMESSAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_chat_bar, LAYOUT_AMITYCHATBAR);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_chat_compose_bar, LAYOUT_AMITYCHATCOMPOSEBAR);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_edit_msg_bar, LAYOUT_AMITYEDITMSGBAR);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_fragment_chat, LAYOUT_AMITYFRAGMENTCHAT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_fragment_recent_chat, LAYOUT_AMITYFRAGMENTRECENTCHAT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_audio_message_receiver, LAYOUT_AMITYITEMAUDIOMESSAGERECEIVER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_audio_message_sender, LAYOUT_AMITYITEMAUDIOMESSAGESENDER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_image_msg_receiver, LAYOUT_AMITYITEMIMAGEMSGRECEIVER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_image_msg_sender, LAYOUT_AMITYITEMIMAGEMSGSENDER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_recent_message, LAYOUT_AMITYITEMRECENTMESSAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_text_message_receiver, LAYOUT_AMITYITEMTEXTMESSAGERECEIVER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_text_message_sender, LAYOUT_AMITYITEMTEXTMESSAGESENDER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_unknown_message, LAYOUT_AMITYITEMUNKNOWNMESSAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_popup_msg_delete, LAYOUT_AMITYPOPUPMSGDELETE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_popup_msg_report, LAYOUT_AMITYPOPUPMSGREPORT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_popup_text_msg_sender, LAYOUT_AMITYPOPUPTEXTMSGSENDER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_date, LAYOUT_AMITYVIEWDATE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_deleted, LAYOUT_AMITYVIEWMSGDELETED);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_error, LAYOUT_AMITYVIEWMSGERROR);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_AMITYACTIVITYCHATHOME: {
          if ("layout/amity_activity_chat_home_0".equals(tag)) {
            return new AmityActivityChatHomeBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_activity_chat_home is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYACTIVITYEDITMESSAGE: {
          if ("layout/amity_activity_edit_message_0".equals(tag)) {
            return new AmityActivityEditMessageBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_activity_edit_message is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYCHATBAR: {
          if ("layout/amity_chat_bar_0".equals(tag)) {
            return new AmityChatBarBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_chat_bar is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYCHATCOMPOSEBAR: {
          if ("layout/amity_chat_compose_bar_0".equals(tag)) {
            return new AmityChatComposeBarBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_chat_compose_bar is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYEDITMSGBAR: {
          if ("layout/amity_edit_msg_bar_0".equals(tag)) {
            return new AmityEditMsgBarBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_edit_msg_bar is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYFRAGMENTCHAT: {
          if ("layout/amity_fragment_chat_0".equals(tag)) {
            return new AmityFragmentChatBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_fragment_chat is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYFRAGMENTRECENTCHAT: {
          if ("layout/amity_fragment_recent_chat_0".equals(tag)) {
            return new AmityFragmentRecentChatBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_fragment_recent_chat is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYITEMAUDIOMESSAGERECEIVER: {
          if ("layout/amity_item_audio_message_receiver_0".equals(tag)) {
            return new AmityItemAudioMessageReceiverBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_item_audio_message_receiver is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYITEMAUDIOMESSAGESENDER: {
          if ("layout/amity_item_audio_message_sender_0".equals(tag)) {
            return new AmityItemAudioMessageSenderBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_item_audio_message_sender is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYITEMIMAGEMSGRECEIVER: {
          if ("layout/amity_item_image_msg_receiver_0".equals(tag)) {
            return new AmityItemImageMsgReceiverBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_item_image_msg_receiver is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYITEMIMAGEMSGSENDER: {
          if ("layout/amity_item_image_msg_sender_0".equals(tag)) {
            return new AmityItemImageMsgSenderBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_item_image_msg_sender is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYITEMRECENTMESSAGE: {
          if ("layout/amity_item_recent_message_0".equals(tag)) {
            return new AmityItemRecentMessageBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_item_recent_message is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYITEMTEXTMESSAGERECEIVER: {
          if ("layout/amity_item_text_message_receiver_0".equals(tag)) {
            return new AmityItemTextMessageReceiverBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_item_text_message_receiver is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYITEMTEXTMESSAGESENDER: {
          if ("layout/amity_item_text_message_sender_0".equals(tag)) {
            return new AmityItemTextMessageSenderBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_item_text_message_sender is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYITEMUNKNOWNMESSAGE: {
          if ("layout/amity_item_unknown_message_0".equals(tag)) {
            return new AmityItemUnknownMessageBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_item_unknown_message is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYPOPUPMSGDELETE: {
          if ("layout/amity_popup_msg_delete_0".equals(tag)) {
            return new AmityPopupMsgDeleteBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_popup_msg_delete is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYPOPUPMSGREPORT: {
          if ("layout/amity_popup_msg_report_0".equals(tag)) {
            return new AmityPopupMsgReportBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_popup_msg_report is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYPOPUPTEXTMSGSENDER: {
          if ("layout/amity_popup_text_msg_sender_0".equals(tag)) {
            return new AmityPopupTextMsgSenderBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_popup_text_msg_sender is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYVIEWDATE: {
          if ("layout/amity_view_date_0".equals(tag)) {
            return new AmityViewDateBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_view_date is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYVIEWMSGDELETED: {
          if ("layout/amity_view_msg_deleted_0".equals(tag)) {
            return new AmityViewMsgDeletedBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_view_msg_deleted is invalid. Received: " + tag);
        }
        case  LAYOUT_AMITYVIEWMSGERROR: {
          if ("layout/amity_view_msg_error_0".equals(tag)) {
            return new AmityViewMsgErrorBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for amity_view_msg_error is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(2);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    result.add(new com.ekoapp.ekosdk.uikit.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(26);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "alertColor");
      sKeys.put(2, "avatarUrl");
      sKeys.put(3, "clickListener");
      sKeys.put(4, "date");
      sKeys.put(5, "dateFillColor");
      sKeys.put(6, "delete");
      sKeys.put(7, "description");
      sKeys.put(8, "disable");
      sKeys.put(9, "isLoggedInUser");
      sKeys.put(10, "isSender");
      sKeys.put(11, "leftDrawable");
      sKeys.put(12, "leftString");
      sKeys.put(13, "listener");
      sKeys.put(14, "lonPressListener");
      sKeys.put(15, "menuItem");
      sKeys.put(16, "name");
      sKeys.put(17, "postCount");
      sKeys.put(18, "rightDrawable");
      sKeys.put(19, "rightString");
      sKeys.put(20, "rightStringActive");
      sKeys.put(21, "title");
      sKeys.put(22, "viewModel");
      sKeys.put(23, "vmAudioMsg");
      sKeys.put(24, "vmImageMessage");
      sKeys.put(25, "vmTextMessage");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(21);

    static {
      sKeys.put("layout/amity_activity_chat_home_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_activity_chat_home);
      sKeys.put("layout/amity_activity_edit_message_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_activity_edit_message);
      sKeys.put("layout/amity_chat_bar_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_chat_bar);
      sKeys.put("layout/amity_chat_compose_bar_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_chat_compose_bar);
      sKeys.put("layout/amity_edit_msg_bar_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_edit_msg_bar);
      sKeys.put("layout/amity_fragment_chat_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_fragment_chat);
      sKeys.put("layout/amity_fragment_recent_chat_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_fragment_recent_chat);
      sKeys.put("layout/amity_item_audio_message_receiver_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_audio_message_receiver);
      sKeys.put("layout/amity_item_audio_message_sender_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_audio_message_sender);
      sKeys.put("layout/amity_item_image_msg_receiver_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_image_msg_receiver);
      sKeys.put("layout/amity_item_image_msg_sender_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_image_msg_sender);
      sKeys.put("layout/amity_item_recent_message_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_recent_message);
      sKeys.put("layout/amity_item_text_message_receiver_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_text_message_receiver);
      sKeys.put("layout/amity_item_text_message_sender_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_text_message_sender);
      sKeys.put("layout/amity_item_unknown_message_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_item_unknown_message);
      sKeys.put("layout/amity_popup_msg_delete_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_popup_msg_delete);
      sKeys.put("layout/amity_popup_msg_report_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_popup_msg_report);
      sKeys.put("layout/amity_popup_text_msg_sender_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_popup_text_msg_sender);
      sKeys.put("layout/amity_view_date_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_date);
      sKeys.put("layout/amity_view_msg_deleted_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_deleted);
      sKeys.put("layout/amity_view_msg_error_0", com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_error);
    }
  }
}
