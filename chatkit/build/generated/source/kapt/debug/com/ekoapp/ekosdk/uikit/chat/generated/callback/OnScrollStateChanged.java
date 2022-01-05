package com.ekoapp.ekosdk.uikit.chat.generated.callback;
public final class OnScrollStateChanged implements com.ekoapp.ekosdk.uikit.components.OnScrollStateChanged {
    final Listener mListener;
    final int mSourceId;
    public OnScrollStateChanged(Listener listener, int sourceId) {
        mListener = listener;
        mSourceId = sourceId;
    }
    @Override
    public void onScrollStateChanged(androidx.recyclerview.widget.RecyclerView callbackArg_0, int callbackArg_1) {
        mListener._internalCallbackOnScrollStateChanged(mSourceId , callbackArg_0, callbackArg_1);
    }
    public interface Listener {
        void _internalCallbackOnScrollStateChanged(int sourceId , androidx.recyclerview.widget.RecyclerView callbackArg_0, int callbackArg_1);
    }
}