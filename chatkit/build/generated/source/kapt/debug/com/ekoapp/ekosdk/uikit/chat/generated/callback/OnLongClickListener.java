package com.ekoapp.ekosdk.uikit.chat.generated.callback;
public final class OnLongClickListener implements android.view.View.OnLongClickListener {
    final Listener mListener;
    final int mSourceId;
    public OnLongClickListener(Listener listener, int sourceId) {
        mListener = listener;
        mSourceId = sourceId;
    }
    @Override
    public boolean onLongClick(android.view.View callbackArg_0) {
        return mListener._internalCallbackOnLongClick(mSourceId , callbackArg_0);
    }
    public interface Listener {
        boolean _internalCallbackOnLongClick(int sourceId , android.view.View callbackArg_0);
    }
}