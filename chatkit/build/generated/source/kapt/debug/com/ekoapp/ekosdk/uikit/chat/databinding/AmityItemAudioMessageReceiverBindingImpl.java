package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityItemAudioMessageReceiverBindingImpl extends AmityItemAudioMessageReceiverBinding implements com.ekoapp.ekosdk.uikit.chat.generated.callback.OnLongClickListener.Listener, com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(13);
        sIncludes.setIncludes(0, 
            new String[] {"amity_view_date", "amity_view_msg_deleted"},
            new int[] {10, 12},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_date,
                com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_deleted});
        sIncludes.setIncludes(1, 
            new String[] {"amity_view_msg_error"},
            new int[] {11},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_msg_error});
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding mboundView01;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgErrorBinding mboundView1;
    @NonNull
    private final android.widget.TextView mboundView6;
    @NonNull
    private final com.google.android.material.progressindicator.CircularProgressIndicator mboundView7;
    @NonNull
    private final android.widget.TextView mboundView8;
    // variables
    @Nullable
    private final android.view.View.OnLongClickListener mCallback2;
    @Nullable
    private final android.view.View.OnClickListener mCallback3;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public AmityItemAudioMessageReceiverBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private AmityItemAudioMessageReceiverBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 15
            , (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) bindings[10]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[2]
            , (android.widget.ImageView) bindings[5]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[4]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[1]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[9]
            );
        this.ivAvatar.setTag(null);
        this.ivPlay.setTag(null);
        this.layoutAudio.setTag(null);
        this.layoutAudioReceiver.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView01 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgDeletedBinding) bindings[12];
        setContainedBinding(this.mboundView01);
        this.mboundView1 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewMsgErrorBinding) bindings[11];
        setContainedBinding(this.mboundView1);
        this.mboundView6 = (android.widget.TextView) bindings[6];
        this.mboundView6.setTag(null);
        this.mboundView7 = (com.google.android.material.progressindicator.CircularProgressIndicator) bindings[7];
        this.mboundView7.setTag(null);
        this.mboundView8 = (android.widget.TextView) bindings[8];
        this.mboundView8.setTag(null);
        this.tvName.setTag(null);
        this.tvTime.setTag(null);
        setRootTag(root);
        // listeners
        mCallback2 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnLongClickListener(this, 1);
        mCallback3 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener(this, 2);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x10000L;
        }
        dateHeader.invalidateAll();
        mboundView1.invalidateAll();
        mboundView01.invalidateAll();
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
        if (mboundView1.hasPendingBindings()) {
            return true;
        }
        if (mboundView01.hasPendingBindings()) {
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
            mDirtyFlags |= 0x8000L;
        }
        notifyPropertyChanged(BR.vmAudioMsg);
        super.requestRebind();
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        dateHeader.setLifecycleOwner(lifecycleOwner);
        mboundView1.setLifecycleOwner(lifecycleOwner);
        mboundView01.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmAudioMsgDuration((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeVmAudioMsgReceiverFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 2 :
                return onChangeVmAudioMsgUploading((androidx.databinding.ObservableBoolean) object, fieldId);
            case 3 :
                return onChangeVmAudioMsgIsFailed((androidx.databinding.ObservableBoolean) object, fieldId);
            case 4 :
                return onChangeVmAudioMsgIsSenderVisible((androidx.databinding.ObservableBoolean) object, fieldId);
            case 5 :
                return onChangeVmAudioMsgIsPlaying((androidx.databinding.ObservableBoolean) object, fieldId);
            case 6 :
                return onChangeVmAudioMsgMsgTime((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 7 :
                return onChangeVmAudioMsgBuffering((androidx.databinding.ObservableBoolean) object, fieldId);
            case 8 :
                return onChangeVmAudioMsgIsDeleted((androidx.databinding.ObservableBoolean) object, fieldId);
            case 9 :
                return onChangeVmAudioMsgInSelectionMode((androidx.databinding.ObservableBoolean) object, fieldId);
            case 10 :
                return onChangeVmAudioMsgSender((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 11 :
                return onChangeVmAudioMsgDateFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 12 :
                return onChangeDateHeader((com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) object, fieldId);
            case 13 :
                return onChangeVmAudioMsgMsgDate((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 14 :
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
    private boolean onChangeVmAudioMsgReceiverFillColor(androidx.databinding.ObservableField<java.lang.Integer> VmAudioMsgReceiverFillColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgUploading(androidx.databinding.ObservableBoolean VmAudioMsgUploading, int fieldId) {
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
    private boolean onChangeVmAudioMsgIsSenderVisible(androidx.databinding.ObservableBoolean VmAudioMsgIsSenderVisible, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgIsPlaying(androidx.databinding.ObservableBoolean VmAudioMsgIsPlaying, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgMsgTime(androidx.databinding.ObservableField<java.lang.String> VmAudioMsgMsgTime, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgBuffering(androidx.databinding.ObservableBoolean VmAudioMsgBuffering, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgIsDeleted(androidx.databinding.ObservableBoolean VmAudioMsgIsDeleted, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x100L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgInSelectionMode(androidx.databinding.ObservableBoolean VmAudioMsgInSelectionMode, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x200L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgSender(androidx.databinding.ObservableField<java.lang.String> VmAudioMsgSender, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x400L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgDateFillColor(androidx.databinding.ObservableField<java.lang.Integer> VmAudioMsgDateFillColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x800L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeDateHeader(com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding DateHeader, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1000L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgMsgDate(androidx.databinding.ObservableField<java.lang.String> VmAudioMsgMsgDate, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2000L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmAudioMsgIsDateVisible(androidx.databinding.ObservableBoolean VmAudioMsgIsDateVisible, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4000L;
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
        int vmAudioMsgUploadingBooleanTrueVmAudioMsgBufferingViewVISIBLEViewGONE = 0;
        boolean vmAudioMsgInSelectionModeBooleanTrueVmAudioMsgUploading = false;
        androidx.databinding.ObservableField<java.lang.Integer> vmAudioMsgReceiverFillColor = null;
        int vmAudioMsgIsDeletedViewGONEViewVISIBLE = 0;
        androidx.databinding.ObservableBoolean vmAudioMsgUploading = null;
        boolean vmAudioMsgBufferingGet = false;
        int vmAudioMsgInSelectionModeBooleanTrueVmAudioMsgUploadingViewVISIBLEViewGONE = 0;
        boolean vmAudioMsgIsDateVisibleGet = false;
        int androidxDatabindingViewDataBindingSafeUnboxVmAudioMsgDateFillColorGet = 0;
        androidx.databinding.ObservableBoolean vmAudioMsgIsFailed = null;
        boolean vmAudioMsgUploadingBooleanTrueVmAudioMsgBuffering = false;
        int vmAudioMsgIsSenderVisibleViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableBoolean vmAudioMsgIsSenderVisible = null;
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoAudioMsgViewModel vmAudioMsg = mVmAudioMsg;
        java.lang.String vmAudioMsgDurationGet = null;
        androidx.databinding.ObservableBoolean vmAudioMsgIsPlaying = null;
        int vmAudioMsgIsFailedViewVISIBLEViewGONE = 0;
        int vmAudioMsgUploadingBooleanTrueVmAudioMsgBufferingViewGONEViewVISIBLE = 0;
        java.lang.Integer vmAudioMsgDateFillColorGet = null;
        androidx.databinding.ObservableField<java.lang.String> vmAudioMsgMsgTime = null;
        android.graphics.drawable.Drawable vmAudioMsgIsPlayingIvPlayAndroidDrawableAmityIcPauseIvPlayAndroidDrawableAmityIcPlay = null;
        androidx.databinding.ObservableBoolean vmAudioMsgBuffering = null;
        androidx.databinding.ObservableBoolean vmAudioMsgIsDeleted = null;
        boolean vmAudioMsgUploadingGet = false;
        boolean vmAudioMsgInSelectionModeGet = false;
        int vmAudioMsgIsDateVisibleViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableBoolean vmAudioMsgInSelectionMode = null;
        androidx.databinding.ObservableField<java.lang.String> vmAudioMsgSender = null;
        androidx.databinding.ObservableField<java.lang.Integer> vmAudioMsgDateFillColor = null;
        java.lang.String vmAudioMsgMsgDateGet = null;
        boolean vmAudioMsgIsSenderVisibleGet = false;
        boolean vmAudioMsgIsPlayingGet = false;
        java.lang.String vmAudioMsgSenderGet = null;
        boolean vmAudioMsgIsDeletedGet = false;
        androidx.databinding.ObservableField<java.lang.String> vmAudioMsgMsgDate = null;
        java.lang.String vmAudioMsgMsgTimeGet = null;
        androidx.databinding.ObservableBoolean vmAudioMsgIsDateVisible = null;
        java.lang.Integer vmAudioMsgReceiverFillColorGet = null;
        boolean vmAudioMsgIsFailedGet = false;

        if ((dirtyFlags & 0x1efffL) != 0) {


            if ((dirtyFlags & 0x18001L) != 0) {

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
            if ((dirtyFlags & 0x18002L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.receiverFillColor
                        vmAudioMsgReceiverFillColor = vmAudioMsg.getReceiverFillColor();
                    }
                    updateRegistration(1, vmAudioMsgReceiverFillColor);


                    if (vmAudioMsgReceiverFillColor != null) {
                        // read vmAudioMsg.receiverFillColor.get()
                        vmAudioMsgReceiverFillColorGet = vmAudioMsgReceiverFillColor.get();
                    }
            }
            if ((dirtyFlags & 0x18084L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.uploading
                        vmAudioMsgUploading = vmAudioMsg.getUploading();
                    }
                    updateRegistration(2, vmAudioMsgUploading);


                    if (vmAudioMsgUploading != null) {
                        // read vmAudioMsg.uploading.get()
                        vmAudioMsgUploadingGet = vmAudioMsgUploading.get();
                    }
                if((dirtyFlags & 0x18084L) != 0) {
                    if(vmAudioMsgUploadingGet) {
                            dirtyFlags |= 0x4000000L;
                    }
                    else {
                            dirtyFlags |= 0x2000000L;
                    }
                }
            }
            if ((dirtyFlags & 0x18008L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.isFailed()
                        vmAudioMsgIsFailed = vmAudioMsg.isFailed();
                    }
                    updateRegistration(3, vmAudioMsgIsFailed);


                    if (vmAudioMsgIsFailed != null) {
                        // read vmAudioMsg.isFailed().get()
                        vmAudioMsgIsFailedGet = vmAudioMsgIsFailed.get();
                    }
                if((dirtyFlags & 0x18008L) != 0) {
                    if(vmAudioMsgIsFailedGet) {
                            dirtyFlags |= 0x40000000L;
                    }
                    else {
                            dirtyFlags |= 0x20000000L;
                    }
                }


                    // read vmAudioMsg.isFailed().get() ? View.VISIBLE : View.GONE
                    vmAudioMsgIsFailedViewVISIBLEViewGONE = ((vmAudioMsgIsFailedGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x18010L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.isSenderVisible()
                        vmAudioMsgIsSenderVisible = vmAudioMsg.isSenderVisible();
                    }
                    updateRegistration(4, vmAudioMsgIsSenderVisible);


                    if (vmAudioMsgIsSenderVisible != null) {
                        // read vmAudioMsg.isSenderVisible().get()
                        vmAudioMsgIsSenderVisibleGet = vmAudioMsgIsSenderVisible.get();
                    }
                if((dirtyFlags & 0x18010L) != 0) {
                    if(vmAudioMsgIsSenderVisibleGet) {
                            dirtyFlags |= 0x10000000L;
                    }
                    else {
                            dirtyFlags |= 0x8000000L;
                    }
                }


                    // read vmAudioMsg.isSenderVisible().get() ? View.VISIBLE : View.GONE
                    vmAudioMsgIsSenderVisibleViewVISIBLEViewGONE = ((vmAudioMsgIsSenderVisibleGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x18020L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.isPlaying()
                        vmAudioMsgIsPlaying = vmAudioMsg.isPlaying();
                    }
                    updateRegistration(5, vmAudioMsgIsPlaying);


                    if (vmAudioMsgIsPlaying != null) {
                        // read vmAudioMsg.isPlaying().get()
                        vmAudioMsgIsPlayingGet = vmAudioMsgIsPlaying.get();
                    }
                if((dirtyFlags & 0x18020L) != 0) {
                    if(vmAudioMsgIsPlayingGet) {
                            dirtyFlags |= 0x400000000L;
                    }
                    else {
                            dirtyFlags |= 0x200000000L;
                    }
                }


                    // read vmAudioMsg.isPlaying().get() ? @android:drawable/amity_ic_pause : @android:drawable/amity_ic_play
                    vmAudioMsgIsPlayingIvPlayAndroidDrawableAmityIcPauseIvPlayAndroidDrawableAmityIcPlay = ((vmAudioMsgIsPlayingGet) ? (androidx.appcompat.content.res.AppCompatResources.getDrawable(ivPlay.getContext(), R.drawable.amity_ic_pause)) : (androidx.appcompat.content.res.AppCompatResources.getDrawable(ivPlay.getContext(), R.drawable.amity_ic_play)));
            }
            if ((dirtyFlags & 0x18040L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.msgTime
                        vmAudioMsgMsgTime = vmAudioMsg.getMsgTime();
                    }
                    updateRegistration(6, vmAudioMsgMsgTime);


                    if (vmAudioMsgMsgTime != null) {
                        // read vmAudioMsg.msgTime.get()
                        vmAudioMsgMsgTimeGet = vmAudioMsgMsgTime.get();
                    }
            }
            if ((dirtyFlags & 0x18100L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.isDeleted()
                        vmAudioMsgIsDeleted = vmAudioMsg.isDeleted();
                    }
                    updateRegistration(8, vmAudioMsgIsDeleted);


                    if (vmAudioMsgIsDeleted != null) {
                        // read vmAudioMsg.isDeleted().get()
                        vmAudioMsgIsDeletedGet = vmAudioMsgIsDeleted.get();
                    }
                if((dirtyFlags & 0x18100L) != 0) {
                    if(vmAudioMsgIsDeletedGet) {
                            dirtyFlags |= 0x400000L;
                    }
                    else {
                            dirtyFlags |= 0x200000L;
                    }
                }


                    // read vmAudioMsg.isDeleted().get() ? View.GONE : View.VISIBLE
                    vmAudioMsgIsDeletedViewGONEViewVISIBLE = ((vmAudioMsgIsDeletedGet) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0x18204L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.inSelectionMode
                        vmAudioMsgInSelectionMode = vmAudioMsg.getInSelectionMode();
                    }
                    updateRegistration(9, vmAudioMsgInSelectionMode);


                    if (vmAudioMsgInSelectionMode != null) {
                        // read vmAudioMsg.inSelectionMode.get()
                        vmAudioMsgInSelectionModeGet = vmAudioMsgInSelectionMode.get();
                    }
                if((dirtyFlags & 0x18204L) != 0) {
                    if(vmAudioMsgInSelectionModeGet) {
                            dirtyFlags |= 0x100000L;
                    }
                    else {
                            dirtyFlags |= 0x80000L;
                    }
                }
            }
            if ((dirtyFlags & 0x18400L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.sender
                        vmAudioMsgSender = vmAudioMsg.getSender();
                    }
                    updateRegistration(10, vmAudioMsgSender);


                    if (vmAudioMsgSender != null) {
                        // read vmAudioMsg.sender.get()
                        vmAudioMsgSenderGet = vmAudioMsgSender.get();
                    }
            }
            if ((dirtyFlags & 0x18800L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.dateFillColor
                        vmAudioMsgDateFillColor = vmAudioMsg.getDateFillColor();
                    }
                    updateRegistration(11, vmAudioMsgDateFillColor);


                    if (vmAudioMsgDateFillColor != null) {
                        // read vmAudioMsg.dateFillColor.get()
                        vmAudioMsgDateFillColorGet = vmAudioMsgDateFillColor.get();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vmAudioMsg.dateFillColor.get())
                    androidxDatabindingViewDataBindingSafeUnboxVmAudioMsgDateFillColorGet = androidx.databinding.ViewDataBinding.safeUnbox(vmAudioMsgDateFillColorGet);
            }
            if ((dirtyFlags & 0x1a000L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.msgDate
                        vmAudioMsgMsgDate = vmAudioMsg.getMsgDate();
                    }
                    updateRegistration(13, vmAudioMsgMsgDate);


                    if (vmAudioMsgMsgDate != null) {
                        // read vmAudioMsg.msgDate.get()
                        vmAudioMsgMsgDateGet = vmAudioMsgMsgDate.get();
                    }
            }
            if ((dirtyFlags & 0x1c000L) != 0) {

                    if (vmAudioMsg != null) {
                        // read vmAudioMsg.isDateVisible()
                        vmAudioMsgIsDateVisible = vmAudioMsg.isDateVisible();
                    }
                    updateRegistration(14, vmAudioMsgIsDateVisible);


                    if (vmAudioMsgIsDateVisible != null) {
                        // read vmAudioMsg.isDateVisible().get()
                        vmAudioMsgIsDateVisibleGet = vmAudioMsgIsDateVisible.get();
                    }
                if((dirtyFlags & 0x1c000L) != 0) {
                    if(vmAudioMsgIsDateVisibleGet) {
                            dirtyFlags |= 0x1000000000L;
                    }
                    else {
                            dirtyFlags |= 0x800000000L;
                    }
                }


                    // read vmAudioMsg.isDateVisible().get() ? View.VISIBLE : View.GONE
                    vmAudioMsgIsDateVisibleViewVISIBLEViewGONE = ((vmAudioMsgIsDateVisibleGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
        }
        // batch finished

        if ((dirtyFlags & 0x80000L) != 0) {

                if (vmAudioMsg != null) {
                    // read vmAudioMsg.uploading
                    vmAudioMsgUploading = vmAudioMsg.getUploading();
                }
                updateRegistration(2, vmAudioMsgUploading);


                if (vmAudioMsgUploading != null) {
                    // read vmAudioMsg.uploading.get()
                    vmAudioMsgUploadingGet = vmAudioMsgUploading.get();
                }
            if((dirtyFlags & 0x18084L) != 0) {
                if(vmAudioMsgUploadingGet) {
                        dirtyFlags |= 0x4000000L;
                }
                else {
                        dirtyFlags |= 0x2000000L;
                }
            }
        }

        if ((dirtyFlags & 0x18204L) != 0) {

                // read vmAudioMsg.inSelectionMode.get() ? true : vmAudioMsg.uploading.get()
                vmAudioMsgInSelectionModeBooleanTrueVmAudioMsgUploading = ((vmAudioMsgInSelectionModeGet) ? (true) : (vmAudioMsgUploadingGet));
            if((dirtyFlags & 0x18204L) != 0) {
                if(vmAudioMsgInSelectionModeBooleanTrueVmAudioMsgUploading) {
                        dirtyFlags |= 0x1000000L;
                }
                else {
                        dirtyFlags |= 0x800000L;
                }
            }


                // read vmAudioMsg.inSelectionMode.get() ? true : vmAudioMsg.uploading.get() ? View.VISIBLE : View.GONE
                vmAudioMsgInSelectionModeBooleanTrueVmAudioMsgUploadingViewVISIBLEViewGONE = ((vmAudioMsgInSelectionModeBooleanTrueVmAudioMsgUploading) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished

        if ((dirtyFlags & 0x2000000L) != 0) {

                if (vmAudioMsg != null) {
                    // read vmAudioMsg.buffering
                    vmAudioMsgBuffering = vmAudioMsg.getBuffering();
                }
                updateRegistration(7, vmAudioMsgBuffering);


                if (vmAudioMsgBuffering != null) {
                    // read vmAudioMsg.buffering.get()
                    vmAudioMsgBufferingGet = vmAudioMsgBuffering.get();
                }
        }

        if ((dirtyFlags & 0x18084L) != 0) {

                // read vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get()
                vmAudioMsgUploadingBooleanTrueVmAudioMsgBuffering = ((vmAudioMsgUploadingGet) ? (true) : (vmAudioMsgBufferingGet));
            if((dirtyFlags & 0x18084L) != 0) {
                if(vmAudioMsgUploadingBooleanTrueVmAudioMsgBuffering) {
                        dirtyFlags |= 0x40000L;
                        dirtyFlags |= 0x100000000L;
                }
                else {
                        dirtyFlags |= 0x20000L;
                        dirtyFlags |= 0x80000000L;
                }
            }


                // read vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get() ? View.VISIBLE : View.GONE
                vmAudioMsgUploadingBooleanTrueVmAudioMsgBufferingViewVISIBLEViewGONE = ((vmAudioMsgUploadingBooleanTrueVmAudioMsgBuffering) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                // read vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get() ? View.GONE : View.VISIBLE
                vmAudioMsgUploadingBooleanTrueVmAudioMsgBufferingViewGONEViewVISIBLE = ((vmAudioMsgUploadingBooleanTrueVmAudioMsgBuffering) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0x1c000L) != 0) {
            // api target 1

            this.dateHeader.getRoot().setVisibility(vmAudioMsgIsDateVisibleViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x1a000L) != 0) {
            // api target 1

            this.dateHeader.setDate(vmAudioMsgMsgDateGet);
        }
        if ((dirtyFlags & 0x18800L) != 0) {
            // api target 1

            this.dateHeader.setDateFillColor(androidxDatabindingViewDataBindingSafeUnboxVmAudioMsgDateFillColorGet);
        }
        if ((dirtyFlags & 0x18010L) != 0) {
            // api target 1

            this.ivAvatar.setVisibility(vmAudioMsgIsSenderVisibleViewVISIBLEViewGONE);
            this.tvName.setVisibility(vmAudioMsgIsSenderVisibleViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x10000L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoViewBackgroundColor(this.ivAvatar, getColorFromResource(ivAvatar, R.color.amityColorPrimary), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            this.ivPlay.setOnClickListener(mCallback3);
            this.layoutAudio.setOnLongClickListener(mCallback2);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.mboundView8, true, (java.lang.Float)null, (java.lang.Float)null, mboundView8.getResources().getDimension(R.dimen.amity_zero), (java.lang.Float)null, (java.lang.Integer)null, (java.lang.Integer)null, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvName, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.tvTime, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE2, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x18020L) != 0) {
            // api target 1

            androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable(this.ivPlay, vmAudioMsgIsPlayingIvPlayAndroidDrawableAmityIcPauseIvPlayAndroidDrawableAmityIcPlay);
        }
        if ((dirtyFlags & 0x18002L) != 0) {
            // api target 1

            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setRoundedCorner(this.layoutAudio, true, layoutAudio.getResources().getDimension(R.dimen.amity_zero), (java.lang.Float)null, (java.lang.Float)null, (java.lang.Float)null, vmAudioMsgReceiverFillColorGet, (java.lang.Integer)null, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null);
        }
        if ((dirtyFlags & 0x18100L) != 0) {
            // api target 1

            this.layoutAudioReceiver.setVisibility(vmAudioMsgIsDeletedViewGONEViewVISIBLE);
            this.tvTime.setVisibility(vmAudioMsgIsDeletedViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0x18000L) != 0) {
            // api target 1

            this.mboundView01.setViewModel(vmAudioMsg);
            this.mboundView1.setViewModel(vmAudioMsg);
        }
        if ((dirtyFlags & 0x18008L) != 0) {
            // api target 1

            this.mboundView1.getRoot().setVisibility(vmAudioMsgIsFailedViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x18001L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView6, vmAudioMsgDurationGet);
        }
        if ((dirtyFlags & 0x18084L) != 0) {
            // api target 1

            this.mboundView6.setVisibility(vmAudioMsgUploadingBooleanTrueVmAudioMsgBufferingViewGONEViewVISIBLE);
            this.mboundView7.setVisibility(vmAudioMsgUploadingBooleanTrueVmAudioMsgBufferingViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x18204L) != 0) {
            // api target 1

            this.mboundView8.setVisibility(vmAudioMsgInSelectionModeBooleanTrueVmAudioMsgUploadingViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x18400L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvName, vmAudioMsgSenderGet);
        }
        if ((dirtyFlags & 0x18040L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvTime, vmAudioMsgMsgTimeGet);
        }
        executeBindingsOn(dateHeader);
        executeBindingsOn(mboundView1);
        executeBindingsOn(mboundView01);
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
        flag 1 (0x2L): vmAudioMsg.receiverFillColor
        flag 2 (0x3L): vmAudioMsg.uploading
        flag 3 (0x4L): vmAudioMsg.isFailed()
        flag 4 (0x5L): vmAudioMsg.isSenderVisible()
        flag 5 (0x6L): vmAudioMsg.isPlaying()
        flag 6 (0x7L): vmAudioMsg.msgTime
        flag 7 (0x8L): vmAudioMsg.buffering
        flag 8 (0x9L): vmAudioMsg.isDeleted()
        flag 9 (0xaL): vmAudioMsg.inSelectionMode
        flag 10 (0xbL): vmAudioMsg.sender
        flag 11 (0xcL): vmAudioMsg.dateFillColor
        flag 12 (0xdL): dateHeader
        flag 13 (0xeL): vmAudioMsg.msgDate
        flag 14 (0xfL): vmAudioMsg.isDateVisible()
        flag 15 (0x10L): vmAudioMsg
        flag 16 (0x11L): null
        flag 17 (0x12L): vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get() ? View.VISIBLE : View.GONE
        flag 18 (0x13L): vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get() ? View.VISIBLE : View.GONE
        flag 19 (0x14L): vmAudioMsg.inSelectionMode.get() ? true : vmAudioMsg.uploading.get()
        flag 20 (0x15L): vmAudioMsg.inSelectionMode.get() ? true : vmAudioMsg.uploading.get()
        flag 21 (0x16L): vmAudioMsg.isDeleted().get() ? View.GONE : View.VISIBLE
        flag 22 (0x17L): vmAudioMsg.isDeleted().get() ? View.GONE : View.VISIBLE
        flag 23 (0x18L): vmAudioMsg.inSelectionMode.get() ? true : vmAudioMsg.uploading.get() ? View.VISIBLE : View.GONE
        flag 24 (0x19L): vmAudioMsg.inSelectionMode.get() ? true : vmAudioMsg.uploading.get() ? View.VISIBLE : View.GONE
        flag 25 (0x1aL): vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get()
        flag 26 (0x1bL): vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get()
        flag 27 (0x1cL): vmAudioMsg.isSenderVisible().get() ? View.VISIBLE : View.GONE
        flag 28 (0x1dL): vmAudioMsg.isSenderVisible().get() ? View.VISIBLE : View.GONE
        flag 29 (0x1eL): vmAudioMsg.isFailed().get() ? View.VISIBLE : View.GONE
        flag 30 (0x1fL): vmAudioMsg.isFailed().get() ? View.VISIBLE : View.GONE
        flag 31 (0x20L): vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get() ? View.GONE : View.VISIBLE
        flag 32 (0x21L): vmAudioMsg.uploading.get() ? true : vmAudioMsg.buffering.get() ? View.GONE : View.VISIBLE
        flag 33 (0x22L): vmAudioMsg.isPlaying().get() ? @android:drawable/amity_ic_pause : @android:drawable/amity_ic_play
        flag 34 (0x23L): vmAudioMsg.isPlaying().get() ? @android:drawable/amity_ic_pause : @android:drawable/amity_ic_play
        flag 35 (0x24L): vmAudioMsg.isDateVisible().get() ? View.VISIBLE : View.GONE
        flag 36 (0x25L): vmAudioMsg.isDateVisible().get() ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}