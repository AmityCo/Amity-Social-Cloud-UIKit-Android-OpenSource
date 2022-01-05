package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemAudioMessageSenderBindingImpl extends AmityItemAudioMessageSenderBinding implements com.ekoapp.ekosdk.uikit.chat.generated.callback.OnLongClickListener.Listener, com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(11);
        sIncludes.setIncludes(0, 
            new String[] {"amity_view_date", "amity_view_msg_error", "amity_view_msg_deleted"},
            new int[] {8, 9, 10},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_date,
                com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_error,
                com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_deleted});
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgErrorBinding mboundView01;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding mboundView02;
    @NonNull
    private final android.widget.TextView mboundView3;
    @NonNull
    private final com.google.android.material.progressindicator.CircularProgressIndicator mboundView4;
    @NonNull
    private final com.google.android.material.progressindicator.CircularProgressIndicator mboundView5;
    @NonNull
    private final android.widget.TextView mboundView6;
    // variables
    @Nullable
    private final android.view.View.OnLongClickListener mCallback12;
    @Nullable
    private final android.view.View.OnClickListener mCallback13;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemAudioMessageSenderBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private AmityItemAudioMessageSenderBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 14
            , (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) bindings[8]
            , (android.widget.ImageView) bindings[2]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[1]
            , (android.widget.TextView) bindings[7]
            );
        this.ivPlay.setTag(null);
        this.layoutAudio.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView01 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgErrorBinding) bindings[9];
        setContainedBinding(this.mboundView01);
        this.mboundView02 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding) bindings[10];
        setContainedBinding(this.mboundView02);
        this.mboundView3 = (android.widget.TextView) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView4 = (com.google.android.material.progressindicator.CircularProgressIndicator) bindings[4];
        this.mboundView4.setTag(null);
        this.mboundView5 = (com.google.android.material.progressindicator.CircularProgressIndicator) bindings[5];
        this.mboundView5.setTag(null);
        this.mboundView6 = (android.widget.TextView) bindings[6];
        this.mboundView6.setTag(null);
        this.tvTime.setTag(null);
        setRootTag(root);
        // listeners
        mCallback12 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnLongClickListener(this, 1);
        mCallback13 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener(this, 2);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8000L;
        }
        dateHeader.invalidateAll();
        mboundView01.invalidateAll();
        mboundView02.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (dateHeader.hasPendingBindings()) {
            return true;
        }
        if (mboundView01.hasPendingBindings()) {
            return true;
        }
        if (mboundView02.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.vmAudioMsg == variableId) {
            setVmAudioMsg((com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setVmAudioMsg(@Nullable com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel VmAudioMsg) {
        this.mVmAudioMsg = VmAudioMsg;
        synchronized(this) {
            mDirtyFlags |= 0x4000L;
        }
        notifyPropertyChanged(BR.vmAudioMsg);
        super.requestRebind();
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        dateHeader.setLifecycleOwner(lifecycleOwner);
        mboundView01.setLifecycleOwner(lifecycleOwner);
        mboundView02.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmAudioMsgDuration((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeVmAudioMsgUploading((androidx.databinding.ObservableBoolean) object, fieldId);
            case 2 :
                return onChangeVmAudioMsgUploadProgress((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 3 :
                return onChangeVmAudioMsgIsFailed((androidx.databinding.ObservableBoolean) object, fieldId);
            case 4 :
                return onChangeVmAudioMsgIsPlaying((androidx.databinding.ObservableBoolean) object, fieldId);
            case 5 :
                return onChangeVmAudioMsgMsgTime((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 6 :
                return onChangeVmAudioMsgBuffering((androidx.databinding.ObservableBoolean) object, fieldId);
            case 7 :
                return onChangeVmAudioMsgIsDeleted((androidx.databinding.ObservableBoolean) object, fieldId);
            case 8 :
                return onChangeVmAudioMsgInSelectionMode((androidx.databinding.ObservableBoolean) object, fieldId);
            case 9 :
                return onChangeVmAudioMsgDateFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 10 :
                return onChangeDateHeader((com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) object, fieldId);
            case 11 :
                return onChangeVmAudioMsgSenderFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 12 :
                return onChangeVmAudioMsgMsgDate((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 13 :
                return onChangeVmAudioMsgIsDateVisible((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeVmAudioMsgDuration(androidx.databinding.ObservableField<java.lang.String> VmAudioMsgDuration, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgUploading(androidx.databinding.ObservableBoolean VmAudioMsgUploading, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgUploadProgress(androidx.databinding.ObservableField<java.lang.Integer> VmAudioMsgUploadProgress, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgIsFailed(androidx.databinding.ObservableBoolean VmAudioMsgIsFailed, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgIsPlaying(androidx.databinding.ObservableBoolean VmAudioMsgIsPlaying, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgMsgTime(androidx.databinding.ObservableField<java.lang.String> VmAudioMsgMsgTime, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgBuffering(androidx.databinding.ObservableBoolean VmAudioMsgBuffering, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgIsDeleted(androidx.databinding.ObservableBoolean VmAudioMsgIsDeleted, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgInSelectionMode(androidx.databinding.ObservableBoolean VmAudioMsgInSelectionMode, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x100L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgDateFillColor(androidx.databinding.ObservableField<java.lang.Integer> VmAudioMsgDateFillColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x200L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeDateHeader(com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding DateHeader, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x400L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgSenderFillColor(androidx.databinding.ObservableField<java.lang.Integer> VmAudioMsgSenderFillColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x800L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgMsgDate(androidx.databinding.ObservableField<java.lang.String> VmAudioMsgMsgDate, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1000L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgIsDateVisible(androidx.databinding.ObservableBoolean VmAudioMsgIsDateVisible, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2000L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        androidx.databinding.ObservableField<java.lang.String> vmAudioMsgDuration = null;
        int androidxDatabindingViewDataBindingSafeUnboxVmAudioMsgUploadProgressGet = 0;
        int vmAudioMsgUploadingViewVISIBLEViewGONE = 0;
        int vmAudioMsgIsDeletedViewGONEViewVISIBLE = 0;
        androidx.databinding.ObservableBoolean vmAudioMsgUploading = null;
        androidx.databinding.ObservableField<java.lang.Integer> vmAudioMsgUploadProgress = null;
        boolean vmAudioMsgBufferingGet = false;
        int vmAudioMsgInSelectionModeViewVISIBLEViewGONE = 0;
        int vmAudioMsgBufferingViewVISIBLEViewGONE = 0;
        boolean vmAudioMsgIsDateVisibleGet = false;
        int androidxDatabindingViewDataBindingSafeUnboxVmAudioMsgDateFillColorGet = 0;
        androidx.databinding.ObservableBoolean vmAudioMsgIsFailed = null;
        boolean vmAudioMsgUploadingBooleanTrueVmAudioMsgBuffering = false;
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel vmAudioMsg = mVmAudioMsg;
        java.lang.String vmAudioMsgDurationGet = null;
        androidx.databinding.ObservableBoolean vmAudioMsgIsPlaying = null;
        int vmAudioMsgIsFailedViewVISIBLEViewGONE = 0;
        int vmAudioMsgUploadingBooleanTrueVmAudioMsgBufferingViewGONEViewVISIBLE = 0;
        java.lang.Integer vmAudioMsgDateFillColorGet = null;
        boolean VmAudioMsgUploading1 = false;
        androidx.databinding.ObservableField<java.lang.String> vmAudioMsgMsgTime = null;
        android.graphics.drawable.Drawable vmAudioMsgIsPlayingIvPlayAndroidDrawableAmityIcPauseIvPlayAndroidDrawableAmityIcPlay = null;
        androidx.databinding.ObservableBoolean vmAudioMsgBuffering = null;
        androidx.databinding.ObservableBoolean vmAudioMsgIsDeleted = null;
        boolean vmAudioMsgUploadingGet = false;
        boolean vmAudioMsgInSelectionModeGet = false;
        java.lang.Integer vmAudioMsgSenderFillColorGet = null;
        int vmAudioMsgIsDateVisibleViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableBoolean vmAudioMsgInSelectionMode = null;
        androidx.databinding.ObservableField<java.lang.Integer> vmAudioMsgDateFillColor = null;
        java.lang.String vmAudioMsgMsgDateGet = null;
        androidx.databinding.ObservableField<java.lang.Integer> vmAudioMsgSenderFillColor = null;
        boolean vmAudioMsgIsPlayingGet = false;
        boolean vmAudioMsgIsDeletedGet = false;
        androidx.databinding.ObservableField<java.lang.String> vmAudioMsgMsgDate = null;
        java.lang.String vmAudioMsgMsgTimeGet = null;
        androidx.databinding.ObservableBoolean vmAudioMsgIsDateVisible = null;
        java.lang.Integer vmAudioMsgUploadProgressGet = null;
        boolean vmAudioMsgIsFailedGet = false;

        if ((dirtyFlags & 0xfbffL) != 0) {


            if ((dirtyFlags & 0xc001L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.duration
                        vmAudioMsgDuration = vmAudioMsg.getDuration();
                    }
                    updateRegistration(0, vmAudioMsgDuration);


                    if (vmAudioMsgDuration != null) {
                        // read vmAudioMsg.duration.get()
                        vmAudioMsgDurationGet = vmAudioMsgDuration.get();
                    }
            }
            if ((dirtyFlags & 0xc042L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.uploading
                        vmAudioMsgUploading = vmAudioMsg.getUploading();
                    }
                    updateRegistration(1, vmAudioMsgUploading);


                    if (vmAudioMsgUploading != null) {
                        // read vmAudioMsg.uploading.get()
                        vmAudioMsgUploadingGet = vmAudioMsgUploading.get();
                    }
                if((dirtyFlags & 0xc002L) != 0) {
                    if(vmAudioMsgUploadingGet) {
                            dirtyFlags |= 0x20000L;
                    }
                    else {
                            dirtyFlags |= 0x10000L;
                    }
                }
                if((dirtyFlags & 0xc042L) != 0) {
                    if(vmAudioMsgUploadingGet) {
                            dirtyFlags |= 0x2000000L;
                    }
                    else {
                            dirtyFlags |= 0x1000000L;
                    }
                }

                if ((dirtyFlags & 0xc002L) != 0) {

                        // read vmAudioMsg.uploading.get() ? View.VISIBLE : View.GONE
                        vmAudioMsgUploadingViewVISIBLEViewGONE = ((vmAudioMsgUploadingGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                        // read !vmAudioMsg.uploading.get()
                        VmAudioMsgUploading1 = !vmAudioMsgUploadingGet;
                }
            }
            if ((dirtyFlags & 0xc004L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.uploadProgress
                        vmAudioMsgUploadProgress = vmAudioMsg.getUploadProgress();
                    }
                    updateRegistration(2, vmAudioMsgUploadProgress);


                    if (vmAudioMsgUploadProgress != null) {
                        // read vmAudioMsg.uploadProgress.get()
                        vmAudioMsgUploadProgressGet = vmAudioMsgUploadProgress.get();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vmAudioMsg.uploadProgress.get())
                    androidxDatabindingViewDataBindingSafeUnboxVmAudioMsgUploadProgressGet = androidx.databinding.ViewDataBinding.safeUnbox(vmAudioMsgUploadProgressGet);
            }
            if ((dirtyFlags & 0xc008L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.isFailed()
                        vmAudioMsgIsFailed = vmAudioMsg.isFailed();
                    }
                    updateRegistration(3, vmAudioMsgIsFailed);


                    if (vmAudioMsgIsFailed != null) {
                        // read vmAudioMsg.isFailed().get()
                        vmAudioMsgIsFailedGet = vmAudioMsgIsFailed.get();
                    }
                if((dirtyFlags & 0xc008L) != 0) {
                    if(vmAudioMsgIsFailedGet) {
                            dirtyFlags |= 0x8000000L;
                    }
                    else {
                            dirtyFlags |= 0x4000000L;
                    }
                }


                    // read vmAudioMsg.isFailed().get() ? View.VISIBLE : View.GONE
                    vmAudioMsgIsFailedViewVISIBLEViewGONE = ((vmAudioMsgIsFailedGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0xc010L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.isPlaying()
                        vmAudioMsgIsPlaying = vmAudioMsg.isPlaying();
                    }
                    updateRegistration(4, vmAudioMsgIsPlaying);


                    if (vmAudioMsgIsPlaying != null) {
                        // read vmAudioMsg.isPlaying().get()
                        vmAudioMsgIsPlayingGet = vmAudioMsgIsPlaying.get();
                    }
                if((dirtyFlags & 0xc010L) != 0) {
                    if(vmAudioMsgIsPlayingGet) {
                            dirtyFlags |= 0x80000000L;
                    }
                    else {
                            dirtyFlags |= 0x40000000L;
                    }
                }


                    // read vmAudioMsg.isPlaying().get() ? @android:drawable/amity_ic_pause : @android:drawable/amity_ic_play
                    vmAudioMsgIsPlayingIvPlayAndroidDrawableAmityIcPauseIvPlayAndroidDrawableAmityIcPlay = ((vmAudioMsgIsPlayingGet) ? (androidx.appcompat.content.res.AppCompatResources.getDrawable(ivPlay.getContext(), R.drawable.amity_ic_pause)) : (androidx.appcompat.content.res.AppCompatResources.getDrawable(ivPlay.getContext(), R.drawable.amity_ic_play)));
            }
            if ((dirtyFlags & 0xc020L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.msgTime
                        vmAudioMsgMsgTime = vmAudioMsg.getMsgTime();
                    }
                    updateRegistration(5, vmAudioMsgMsgTime);


                    if (vmAudioMsgMsgTime != null) {
                        // read vmAudioMsg.msgTime.get()
                        vmAudioMsgMsgTimeGet = vmAudioMsgMsgTime.get();
                    }
            }
            if ((dirtyFlags & 0xc040L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.buffering
                        vmAudioMsgBuffering = vmAudioMsg.getBuffering();
                    }
                    updateRegistration(6, vmAudioMsgBuffering);


                    if (vmAudioMsgBuffering != null) {
                        // read vmAudioMsg.buffering.get()
                        vmAudioMsgBufferingGet = vmAudioMsgBuffering.get();
                    }
                if((dirtyFlags & 0xc040L) != 0) {
                    if(vmAudioMsgBufferingGet) {
                            dirtyFlags |= 0x800000L;
                    }
                    else {
                            dirtyFlags |= 0x400000L;
                    }
                }


                    // read vmAudioMsg.buffering.get() ? View.VISIBLE : View.GONE
                    vmAudioMsgBufferingViewVISIBLEViewGONE = ((vmAudioMsgBufferingGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0xc080L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.isDeleted()
                        vmAudioMsgIsDeleted = vmAudioMsg.isDeleted();
                    }
                    updateRegistration(7, vmAudioMsgIsDeleted);


                    if (vmAudioMsgIsDeleted != null) {
                        // read vmAudioMsg.isDeleted().get()
                        vmAudioMsgIsDeletedGet = vmAudioMsgIsDeleted.get();
                    }
                if((dirtyFlags & 0xc080L) != 0) {
                    if(vmAudioMsgIsDeletedGet) {
                            dirtyFlags |= 0x80000L;
                    }
                    else {
                            dirtyFlags |= 0x40000L;
                    }
                }


                    // read vmAudioMsg.isDeleted().get() ? View.GONE : View.VISIBLE
                    vmAudioMsgIsDeletedViewGONEViewVISIBLE = ((vmAudioMsgIsDeletedGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0xc100L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.inSelectionMode
                        vmAudioMsgInSelectionMode = vmAudioMsg.getInSelectionMode();
                    }
                    updateRegistration(8, vmAudioMsgInSelectionMode);


                    if (vmAudioMsgInSelectionMode != null) {
                        // read vmAudioMsg.inSelectionMode.get()
                        vmAudioMsgInSelectionModeGet = vmAudioMsgInSelectionMode.get();
                    }
                if((dirtyFlags & 0xc100L) != 0) {
                    if(vmAudioMsgInSelectionModeGet) {
                            dirtyFlags |= 0x200000L;
                    }
                    else {
                            dirtyFlags |= 0x100000L;
                    }
                }


                    // read vmAudioMsg.inSelectionMode.get() ? View.VISIBLE : View.GONE
                    vmAudioMsgInSelectionModeViewVISIBLEViewGONE = ((vmAudioMsgInSelectionModeGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0xc200L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.dateFillColor
                        vmAudioMsgDateFillColor = vmAudioMsg.getDateFillColor();
                    }
                    updateRegistration(9, vmAudioMsgDateFillColor);


                    if (vmAudioMsgDateFillColor != null) {
                        // read vmAudioMsg.dateFillColor.get()
                        vmAudioMsgDateFillColorGet = vmAudioMsgDateFillColor.get();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vmAudioMsg.dateFillColor.get())
                    androidxDatabindingViewDataBindingSafeUnboxVmAudioMsgDateFillColorGet = androidx.databinding.ViewDataBinding.safeUnbox(vmAudioMsgDateFillColorGet);
            }
            if ((dirtyFlags & 0xc800L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.senderFillColor
                        vmAudioMsgSenderFillColor = vmAudioMsg.getSenderFillColor();
                    }
                    updateRegistration(11, vmAudioMsgSenderFillColor);


                    if (vmAudioMsgSenderFillColor != null) {
                        // read vmAudioMsg.senderFillColor.get()
                        vmAudioMsgSenderFillColorGet = vmAudioMsgSenderFillColor.get();
                    }
            }
            if ((dirtyFlags & 0xd000L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.msgDate
                        vmAudioMsgMsgDate = vmAudioMsg.getMsgDate();
                    }
                    updateRegistration(12, vmAudioMsgMsgDate);


                    if (vmAudioMsgMsgDate != null) {
                        // read vmAudioMsg.msgDate.get()
                        vmAudioMsgMsgDateGet = vmAudioMsgMsgDate.get();
                    }
            }
            if ((dirtyFlags & 0xe000L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.isDateVisible()
                        vmAudioMsgIsDateVisible = vmAudioMsg.isDateVisible();
                    }
                    updateRegistration(13, vmAudioMsgIsDateVisible);


                    if (vmAudioMsgIsDateVisible != null) {
                        // read vmAudioMsg.isDateVisible().get()
                        vmAudioMsgIsDateVisibleGet = vmAudioMsgIsDateVisible.get();
                    }
                if((dirtyFlags & 0xe000L) != 0) {
                    if(vmAudioMsgIsDateVisibleGet) {
                            dirtyFlags |= 0x200000000L;
                    }
                    else {
                            dirtyFlags |= 0x100000000L;
                    }
                }


                    // read vmAudioMsg.isDateVisible().get() ? View.VISIBLE : View.GONE
                    vmAudioMsgIsDateVisibleViewVISIBLEViewGONE = ((vmAudioMsgIsDateVisibleGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished

        if ((dirtyFlags & 0x1000000L) != 0) {

                if (vmAudioMsg != null) {
                    // read vmAudioMsg.buffering
                    vmAudioMsgBuffering = vmAudioMsg.getBuffering();
                }
                updateRegistration(6, vmAudioMsgBuffering);


                if (vmAudioMsgBuffering != null) {
                    // read vmAudioMsg.buffering.get()
                    vmAudioMsgBufferingGet = vmAudioMsgBuffering.get();
                }
            if((dirtyFlags & 0xc040L) != 0) {
                if(vmAudioMsgBufferingGet) {
                        dirtyFlags |= 0x800000L;
                }
                else {
                        dirtyFlags |= 0x400000L;
                }
            }
        }

        if ((dirtyFlags & 0xc042L) != 0) {

                // read vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get()
                vmAudioMsgUploadingBooleanTrueVmAudioMsgBuffering = ((vmAudioMsgUploadingGet) ? (true) : (vmAudioMsgBufferingGet));
            if((dirtyFlags & 0xc042L) != 0) {
                if(vmAudioMsgUploadingBooleanTrueVmAudioMsgBuffering) {
                        dirtyFlags |= 0x20000000L;
                }
                else {
                        dirtyFlags |= 0x10000000L;
                }
            }


                // read vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get() ? View.GONE : View.VISIBLE
                vmAudioMsgUploadingBooleanTrueVmAudioMsgBufferingViewGONEViewVISIBLE = ((vmAudioMsgUploadingBooleanTrueVmAudioMsgBuffering) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0xe000L) != 0) {
            // api target 1

            this.dateHeader.getRoot().setVisibility(vmAudioMsgIsDateVisibleViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xd000L) != 0) {
            // api target 1

            this.dateHeader.setDate(vmAudioMsgMsgDateGet);
        }
        if ((dirtyFlags & 0xc200L) != 0) {
            // api target 1

            this.dateHeader.setDateFillColor(androidxDatabindingViewDataBindingSafeUnboxVmAudioMsgDateFillColorGet);
        }
        if ((dirtyFlags & 0xc010L) != 0) {
            // api target 1

            androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable(this.ivPlay, vmAudioMsgIsPlayingIvPlayAndroidDrawableAmityIcPauseIvPlayAndroidDrawableAmityIcPlay);
        }
        if ((dirtyFlags & 0xc002L) != 0) {
            // api target 1

            androidx.databinding.adapters.ViewBindingAdapter.setOnClick(this.ivPlay, mCallback13, VmAudioMsgUploading1);
            this.mboundView4.setVisibility(vmAudioMsgUploadingViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x8000L) != 0) {
            // api target 1

            this.layoutAudio.setOnLongClickListener(mCallback12);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.mboundView6, true, (java.lang.Float)null, (java.lang.Float)null, mboundView6.getResources().getDimension(R.dimen.amity_zero), (java.lang.Float)null, (java.lang.Integer)null, (java.lang.Integer)null, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvTime, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0xc080L) != 0) {
            // api target 1

            this.layoutAudio.setVisibility(vmAudioMsgIsDeletedViewGONEViewVISIBLE);
            this.tvTime.setVisibility(vmAudioMsgIsDeletedViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0xc800L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.layoutAudio, true, (java.lang.Float)null, (java.lang.Float)null, layoutAudio.getResources().getDimension(R.dimen.amity_zero), (java.lang.Float)null, vmAudioMsgSenderFillColorGet, (java.lang.Integer)null, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0xc008L) != 0) {
            // api target 1

            this.mboundView01.getRoot().setVisibility(vmAudioMsgIsFailedViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xc000L) != 0) {
            // api target 1

            this.mboundView01.setViewModel(vmAudioMsg);
            this.mboundView02.setViewModel(vmAudioMsg);
        }
        if ((dirtyFlags & 0xc001L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView3, vmAudioMsgDurationGet);
        }
        if ((dirtyFlags & 0xc042L) != 0) {
            // api target 1

            this.mboundView3.setVisibility(vmAudioMsgUploadingBooleanTrueVmAudioMsgBufferingViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0xc004L) != 0) {
            // api target 1

            this.mboundView4.setProgress(androidxDatabindingViewDataBindingSafeUnboxVmAudioMsgUploadProgressGet);
        }
        if ((dirtyFlags & 0xc040L) != 0) {
            // api target 1

            this.mboundView5.setVisibility(vmAudioMsgBufferingViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xc100L) != 0) {
            // api target 1

            this.mboundView6.setVisibility(vmAudioMsgInSelectionModeViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xc020L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvTime, vmAudioMsgMsgTimeGet);
        }
        executeBindingsOn(dateHeader);
        executeBindingsOn(mboundView01);
        executeBindingsOn(mboundView02);
    }
    // Listener Stub Implementations
    // callback impls
    public final boolean _internalCallbackOnLongClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // vmAudioMsg.onLongPress()
        boolean vmAudioMsgOnLongPress = false;
        // vmAudioMsg != null
        boolean vmAudioMsgJavaLangObjectNull = false;
        // vmAudioMsg
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel vmAudioMsg = mVmAudioMsg;



        vmAudioMsgJavaLangObjectNull = (vmAudioMsg) != (null);
        if (vmAudioMsgJavaLangObjectNull) {


            vmAudioMsgOnLongPress = vmAudioMsg.onLongPress();
        }
        return vmAudioMsgOnLongPress;
    }
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // vmAudioMsg != null
        boolean vmAudioMsgJavaLangObjectNull = false;
        // vmAudioMsg
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel vmAudioMsg = mVmAudioMsg;



        vmAudioMsgJavaLangObjectNull = (vmAudioMsg) != (null);
        if (vmAudioMsgJavaLangObjectNull) {


            vmAudioMsg.playButtonClicked();
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): vmAudioMsg.duration
        flag 1 (0x2L): vmAudioMsg.uploading
        flag 2 (0x3L): vmAudioMsg.uploadProgress
        flag 3 (0x4L): vmAudioMsg.isFailed()
        flag 4 (0x5L): vmAudioMsg.isPlaying()
        flag 5 (0x6L): vmAudioMsg.msgTime
        flag 6 (0x7L): vmAudioMsg.buffering
        flag 7 (0x8L): vmAudioMsg.isDeleted()
        flag 8 (0x9L): vmAudioMsg.inSelectionMode
        flag 9 (0xaL): vmAudioMsg.dateFillColor
        flag 10 (0xbL): dateHeader
        flag 11 (0xcL): vmAudioMsg.senderFillColor
        flag 12 (0xdL): vmAudioMsg.msgDate
        flag 13 (0xeL): vmAudioMsg.isDateVisible()
        flag 14 (0xfL): vmAudioMsg
        flag 15 (0x10L): null
        flag 16 (0x11L): vmAudioMsg.uploading.get() ? View.VISIBLE : View.GONE
        flag 17 (0x12L): vmAudioMsg.uploading.get() ? View.VISIBLE : View.GONE
        flag 18 (0x13L): vmAudioMsg.isDeleted().get() ? View.GONE : View.VISIBLE
        flag 19 (0x14L): vmAudioMsg.isDeleted().get() ? View.GONE : View.VISIBLE
        flag 20 (0x15L): vmAudioMsg.inSelectionMode.get() ? View.VISIBLE : View.GONE
        flag 21 (0x16L): vmAudioMsg.inSelectionMode.get() ? View.VISIBLE : View.GONE
        flag 22 (0x17L): vmAudioMsg.buffering.get() ? View.VISIBLE : View.GONE
        flag 23 (0x18L): vmAudioMsg.buffering.get() ? View.VISIBLE : View.GONE
        flag 24 (0x19L): vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get()
        flag 25 (0x1aL): vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get()
        flag 26 (0x1bL): vmAudioMsg.isFailed().get() ? View.VISIBLE : View.GONE
        flag 27 (0x1cL): vmAudioMsg.isFailed().get() ? View.VISIBLE : View.GONE
        flag 28 (0x1dL): vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get() ? View.GONE : View.VISIBLE
        flag 29 (0x1eL): vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get() ? View.GONE : View.VISIBLE
        flag 30 (0x1fL): vmAudioMsg.isPlaying().get() ? @android:drawable/amity_ic_pause : @android:drawable/amity_ic_play
        flag 31 (0x20L): vmAudioMsg.isPlaying().get() ? @android:drawable/amity_ic_pause : @android:drawable/amity_ic_play
        flag 32 (0x21L): vmAudioMsg.isDateVisible().get() ? View.VISIBLE : View.GONE
        flag 33 (0x22L): vmAudioMsg.isDateVisible().get() ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}