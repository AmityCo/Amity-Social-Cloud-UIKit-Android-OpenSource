package com.ekoapp.ekosdk.uikit.chat.databinding;
import com.ekoapp.ekosdk.uikit.chat.R;
import com.ekoapp.ekosdk.uikit.chat.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AmityFragmentChatBindingImpl extends AmityFragmentChatBinding implements com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener.Listener, com.ekoapp.ekosdk.uikit.chat.generated.callback.OnScrollStateChanged.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(14);
        sIncludes.setIncludes(1, 
            new String[] {"amity_chat_bar", "amity_view_date", "amity_chat_compose_bar"},
            new int[] {10, 11, 12},
            new int[] {com.ekoapp.ekosdk.uikit.chat.R.layout.amity_chat_bar,
                com.ekoapp.ekosdk.uikit.chat.R.layout.amity_view_date,
                com.ekoapp.ekosdk.uikit.chat.R.layout.amity_chat_compose_bar});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.rightBarrier, 13);
    }
    // views
    @NonNull
    private final android.widget.FrameLayout mboundView0;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding mboundView1;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback15;
    @Nullable
    private final android.view.View.OnClickListener mCallback16;
    @Nullable
    private final android.view.View.OnClickListener mCallback17;
    @Nullable
    private final com.ekoapp.ekosdk.uikit.components.OnScrollStateChanged mCallback14;
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener etMessageandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.text.get()
            //         is viewModel.text.set((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(etMessage);
            // localize variables for thread safety
            // viewModel.text
            androidx.databinding.ObservableField<java.lang.String> viewModelText = null;
            // viewModel.text.get()
            java.lang.String viewModelTextGet = null;
            // viewModel
            com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel viewModel = mViewModel;
            // viewModel.text != null
            boolean viewModelTextJavaLangObjectNull = false;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {


                viewModelText = viewModel.getText();

                viewModelTextJavaLangObjectNull = (viewModelText) != (null);
                if (viewModelTextJavaLangObjectNull) {




                    viewModelText.set(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };

    public AmityFragmentChatBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds));
    }
    private AmityFragmentChatBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 10
            , (android.widget.ImageView) bindings[7]
            , (com.ekoapp.ekosdk.uikit.chat.databinding.AmityChatBarBinding) bindings[10]
            , (com.ekoapp.ekosdk.uikit.chat.databinding.AmityChatComposeBarBinding) bindings[12]
            , (com.google.android.material.textfield.TextInputEditText) bindings[4]
            , (com.google.android.material.button.MaterialButton) bindings[8]
            , (android.widget.ImageView) bindings[3]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[1]
            , (android.view.View) bindings[5]
            , (com.ekoapp.ekosdk.uikit.components.EkoAudioRecorderView) bindings[9]
            , (androidx.constraintlayout.widget.Barrier) bindings[13]
            , (androidx.recyclerview.widget.RecyclerView) bindings[2]
            , (android.widget.TextView) bindings[6]
            );
        this.btnAdd.setTag(null);
        this.etMessage.setTag(null);
        this.ivSend.setTag(null);
        this.ivVoiceMsg.setTag(null);
        this.layoutParent.setTag(null);
        this.mboundView0 = (android.widget.FrameLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (com.ekoapp.ekosdk.uikit.chat.databinding.AmityViewDateBinding) bindings[11];
        setContainedBinding(this.mboundView1);
        this.recordBackground.setTag(null);
        this.recorderView.setTag(null);
        this.rvChatList.setTag(null);
        this.tvRecord.setTag(null);
        setRootTag(root);
        // listeners
        mCallback15 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener(this, 2);
        mCallback16 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener(this, 3);
        mCallback17 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnClickListener(this, 4);
        mCallback14 = new com.ekoapp.ekosdk.uikit.chat.generated.callback.OnScrollStateChanged(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x800L;
        }
        chatToolBar.invalidateAll();
        mboundView1.invalidateAll();
        composeBar.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (chatToolBar.hasPendingBindings()) {
            return true;
        }
        if (mboundView1.hasPendingBindings()) {
            return true;
        }
        if (composeBar.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.viewModel == variableId) {
            setViewModel((com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel ViewModel) {
        this.mViewModel = ViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x400L;
        }
        notifyPropertyChanged(BR.viewModel);
        super.requestRebind();
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        chatToolBar.setLifecycleOwner(lifecycleOwner);
        mboundView1.setLifecycleOwner(lifecycleOwner);
        composeBar.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeChatToolBar((com.ekoapp.ekosdk.uikit.chat.databinding.AmityChatBarBinding) object, fieldId);
            case 1 :
                return onChangeViewModelTitle((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 2 :
                return onChangeViewModelDateFillColor((androidx.databinding.ObservableField<java.lang.Integer>) object, fieldId);
            case 3 :
                return onChangeViewModelIsRecording((androidx.databinding.ObservableBoolean) object, fieldId);
            case 4 :
                return onChangeViewModelIsScrollable((androidx.databinding.ObservableBoolean) object, fieldId);
            case 5 :
                return onChangeViewModelStickyDate((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 6 :
                return onChangeViewModelText((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 7 :
                return onChangeViewModelShowComposeBar((androidx.databinding.ObservableBoolean) object, fieldId);
            case 8 :
                return onChangeViewModelIsVoiceMsgUi((androidx.databinding.ObservableBoolean) object, fieldId);
            case 9 :
                return onChangeComposeBar((com.ekoapp.ekosdk.uikit.chat.databinding.AmityChatComposeBarBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeChatToolBar(com.ekoapp.ekosdk.uikit.chat.databinding.AmityChatBarBinding ChatToolBar, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelTitle(androidx.databinding.ObservableField<java.lang.String> ViewModelTitle, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelDateFillColor(androidx.databinding.ObservableField<java.lang.Integer> ViewModelDateFillColor, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelIsRecording(androidx.databinding.ObservableBoolean ViewModelIsRecording, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelIsScrollable(androidx.databinding.ObservableBoolean ViewModelIsScrollable, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelStickyDate(androidx.databinding.ObservableField<java.lang.String> ViewModelStickyDate, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelText(androidx.databinding.ObservableField<java.lang.String> ViewModelText, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelShowComposeBar(androidx.databinding.ObservableBoolean ViewModelShowComposeBar, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeViewModelIsVoiceMsgUi(androidx.databinding.ObservableBoolean ViewModelIsVoiceMsgUi, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x100L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeComposeBar(com.ekoapp.ekosdk.uikit.chat.databinding.AmityChatComposeBarBinding ComposeBar, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x200L;
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
        int textUtilsIsEmptyViewModelTextTrimViewModelIsVoiceMsgUiViewVISIBLEViewGONE = 0;
        int viewModelIsRecordingViewVISIBLEViewINVISIBLE = 0;
        boolean viewModelIsRecordingGet = false;
        com.ekoapp.ekosdk.uikit.components.EkoChatComposeBarClickListener viewModelComposeBarClickListener = null;
        java.lang.String viewModelTextTrim = null;
        int textUtilsIsEmptyViewModelTextTrimBooleanTrueViewModelIsVoiceMsgUiViewGONEViewVISIBLE = 0;
        boolean textUtilsIsEmptyViewModelTextTrim = false;
        java.lang.String viewModelStickyDateGet = null;
        boolean viewModelShowComposeBarGet = false;
        boolean viewModelIsVoiceMsgUiGet = false;
        androidx.databinding.ObservableField<java.lang.String> viewModelTitle = null;
        boolean textUtilsIsEmptyViewModelTextTrimBooleanTrueViewModelIsVoiceMsgUi = false;
        boolean textUtilsIsEmptyViewModelTextTrimViewModelIsVoiceMsgUi = false;
        int androidxDatabindingViewDataBindingSafeUnboxViewModelDateFillColorGet = 0;
        androidx.databinding.ObservableField<java.lang.Integer> viewModelDateFillColor = null;
        androidx.databinding.ObservableBoolean viewModelIsRecording = null;
        java.lang.Integer viewModelDateFillColorGet = null;
        int viewModelIsScrollableViewVISIBLEViewGONE = 0;
        java.lang.String viewModelTextGet = null;
        boolean viewModelIsScrollableGet = false;
        boolean viewModelIsVoiceMsgUi = false;
        androidx.databinding.ObservableBoolean viewModelIsScrollable = null;
        int viewModelShowComposeBarViewVISIBLEViewGONE = 0;
        androidx.databinding.ObservableField<java.lang.String> viewModelStickyDate = null;
        androidx.databinding.ObservableField<java.lang.String> viewModelText = null;
        android.graphics.drawable.Drawable viewModelIsVoiceMsgUiIvVoiceMsgAndroidDrawableAmityIcKeyboardIvVoiceMsgAndroidDrawableAmityIcVoiceMessage = null;
        androidx.databinding.ObservableBoolean viewModelShowComposeBar = null;
        java.lang.String viewModelTitleGet = null;
        int viewModelIsVoiceMsgUiViewVISIBLEViewGONE = 0;
        android.graphics.drawable.Drawable viewModelShowComposeBarBtnAddAndroidDrawableAmityIcCrossBtnAddAndroidDrawableAmityIcChatAdd = null;
        androidx.databinding.ObservableBoolean ViewModelIsVoiceMsgUi1 = null;
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel viewModel = mViewModel;

        if ((dirtyFlags & 0xdfeL) != 0) {


            if ((dirtyFlags & 0xc00L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.composeBarClickListener
                        viewModelComposeBarClickListener = viewModel.getComposeBarClickListener();
                    }
            }
            if ((dirtyFlags & 0xc02L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.title
                        viewModelTitle = viewModel.getTitle();
                    }
                    updateRegistration(1, viewModelTitle);


                    if (viewModelTitle != null) {
                        // read viewModel.title.get()
                        viewModelTitleGet = viewModelTitle.get();
                    }
            }
            if ((dirtyFlags & 0xc04L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.dateFillColor
                        viewModelDateFillColor = viewModel.getDateFillColor();
                    }
                    updateRegistration(2, viewModelDateFillColor);


                    if (viewModelDateFillColor != null) {
                        // read viewModel.dateFillColor.get()
                        viewModelDateFillColorGet = viewModelDateFillColor.get();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(viewModel.dateFillColor.get())
                    androidxDatabindingViewDataBindingSafeUnboxViewModelDateFillColorGet = androidx.databinding.ViewDataBinding.safeUnbox(viewModelDateFillColorGet);
            }
            if ((dirtyFlags & 0xc08L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isRecording()
                        viewModelIsRecording = viewModel.isRecording();
                    }
                    updateRegistration(3, viewModelIsRecording);


                    if (viewModelIsRecording != null) {
                        // read viewModel.isRecording().get()
                        viewModelIsRecordingGet = viewModelIsRecording.get();
                    }
                if((dirtyFlags & 0xc08L) != 0) {
                    if(viewModelIsRecordingGet) {
                            dirtyFlags |= 0x8000L;
                    }
                    else {
                            dirtyFlags |= 0x4000L;
                    }
                }


                    // read viewModel.isRecording().get() ? View.VISIBLE : View.INVISIBLE
                    viewModelIsRecordingViewVISIBLEViewINVISIBLE = ((viewModelIsRecordingGet) ? (android.view.View.VISIBLE) : (android.view.View.INVISIBLE));
            }
            if ((dirtyFlags & 0xc10L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.isScrollable()
                        viewModelIsScrollable = viewModel.isScrollable();
                    }
                    updateRegistration(4, viewModelIsScrollable);


                    if (viewModelIsScrollable != null) {
                        // read viewModel.isScrollable().get()
                        viewModelIsScrollableGet = viewModelIsScrollable.get();
                    }
                if((dirtyFlags & 0xc10L) != 0) {
                    if(viewModelIsScrollableGet) {
                            dirtyFlags |= 0x200000L;
                    }
                    else {
                            dirtyFlags |= 0x100000L;
                    }
                }


                    // read viewModel.isScrollable().get() ? View.VISIBLE : View.GONE
                    viewModelIsScrollableViewVISIBLEViewGONE = ((viewModelIsScrollableGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0xc20L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.stickyDate
                        viewModelStickyDate = viewModel.getStickyDate();
                    }
                    updateRegistration(5, viewModelStickyDate);


                    if (viewModelStickyDate != null) {
                        // read viewModel.stickyDate.get()
                        viewModelStickyDateGet = viewModelStickyDate.get();
                    }
            }
            if ((dirtyFlags & 0xd40L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.text
                        viewModelText = viewModel.getText();
                        // read viewModel.isVoiceMsgUi()
                        ViewModelIsVoiceMsgUi1 = viewModel.isVoiceMsgUi();
                    }
                    updateRegistration(6, viewModelText);
                    updateRegistration(8, ViewModelIsVoiceMsgUi1);


                    if (viewModelText != null) {
                        // read viewModel.text.get()
                        viewModelTextGet = viewModelText.get();
                    }
                    if (ViewModelIsVoiceMsgUi1 != null) {
                        // read viewModel.isVoiceMsgUi().get()
                        viewModelIsVoiceMsgUiGet = ViewModelIsVoiceMsgUi1.get();
                    }
                if((dirtyFlags & 0xd00L) != 0) {
                    if(viewModelIsVoiceMsgUiGet) {
                            dirtyFlags |= 0x2000000L;
                            dirtyFlags |= 0x8000000L;
                    }
                    else {
                            dirtyFlags |= 0x1000000L;
                            dirtyFlags |= 0x4000000L;
                    }
                }


                    if (viewModelTextGet != null) {
                        // read viewModel.text.get().trim()
                        viewModelTextTrim = viewModelTextGet.trim();
                    }
                    // read !viewModel.isVoiceMsgUi().get()
                    viewModelIsVoiceMsgUi = !viewModelIsVoiceMsgUiGet;


                    // read TextUtils.isEmpty(viewModel.text.get().trim())
                    textUtilsIsEmptyViewModelTextTrim = android.text.TextUtils.isEmpty(viewModelTextTrim);
                if((dirtyFlags & 0xd40L) != 0) {
                    if(textUtilsIsEmptyViewModelTextTrim) {
                            dirtyFlags |= 0x80000L;
                    }
                    else {
                            dirtyFlags |= 0x40000L;
                    }
                }


                    // read (TextUtils.isEmpty(viewModel.text.get().trim())) & (!viewModel.isVoiceMsgUi().get())
                    textUtilsIsEmptyViewModelTextTrimViewModelIsVoiceMsgUi = (textUtilsIsEmptyViewModelTextTrim) & (viewModelIsVoiceMsgUi);
                if((dirtyFlags & 0xd40L) != 0) {
                    if(textUtilsIsEmptyViewModelTextTrimViewModelIsVoiceMsgUi) {
                            dirtyFlags |= 0x2000L;
                    }
                    else {
                            dirtyFlags |= 0x1000L;
                    }
                }


                    // read (TextUtils.isEmpty(viewModel.text.get().trim())) & (!viewModel.isVoiceMsgUi().get()) ? View.VISIBLE : View.GONE
                    textUtilsIsEmptyViewModelTextTrimViewModelIsVoiceMsgUiViewVISIBLEViewGONE = ((textUtilsIsEmptyViewModelTextTrimViewModelIsVoiceMsgUi) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                if ((dirtyFlags & 0xd00L) != 0) {

                        // read viewModel.isVoiceMsgUi().get() ? @android:drawable/amity_ic_keyboard : @android:drawable/amity_ic_voice_message
                        viewModelIsVoiceMsgUiIvVoiceMsgAndroidDrawableAmityIcKeyboardIvVoiceMsgAndroidDrawableAmityIcVoiceMessage = ((viewModelIsVoiceMsgUiGet) ? (androidx.appcompat.content.res.AppCompatResources.getDrawable(ivVoiceMsg.getContext(), R.drawable.amity_ic_keyboard)) : (androidx.appcompat.content.res.AppCompatResources.getDrawable(ivVoiceMsg.getContext(), R.drawable.amity_ic_voice_message)));
                        // read viewModel.isVoiceMsgUi().get() ? View.VISIBLE : View.GONE
                        viewModelIsVoiceMsgUiViewVISIBLEViewGONE = ((viewModelIsVoiceMsgUiGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                }
            }
            if ((dirtyFlags & 0xc80L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.showComposeBar
                        viewModelShowComposeBar = viewModel.getShowComposeBar();
                    }
                    updateRegistration(7, viewModelShowComposeBar);


                    if (viewModelShowComposeBar != null) {
                        // read viewModel.showComposeBar.get()
                        viewModelShowComposeBarGet = viewModelShowComposeBar.get();
                    }
                if((dirtyFlags & 0xc80L) != 0) {
                    if(viewModelShowComposeBarGet) {
                            dirtyFlags |= 0x800000L;
                            dirtyFlags |= 0x20000000L;
                    }
                    else {
                            dirtyFlags |= 0x400000L;
                            dirtyFlags |= 0x10000000L;
                    }
                }


                    // read viewModel.showComposeBar.get() ? View.VISIBLE : View.GONE
                    viewModelShowComposeBarViewVISIBLEViewGONE = ((viewModelShowComposeBarGet) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
                    // read viewModel.showComposeBar.get() ? @android:drawable/amity_ic_cross : @android:drawable/amity_ic_chat_add
                    viewModelShowComposeBarBtnAddAndroidDrawableAmityIcCrossBtnAddAndroidDrawableAmityIcChatAdd = ((viewModelShowComposeBarGet) ? (androidx.appcompat.content.res.AppCompatResources.getDrawable(btnAdd.getContext(), R.drawable.amity_ic_cross)) : (androidx.appcompat.content.res.AppCompatResources.getDrawable(btnAdd.getContext(), R.drawable.amity_ic_chat_add)));
            }
        }
        // batch finished

        if ((dirtyFlags & 0xd40L) != 0) {

                // read TextUtils.isEmpty(viewModel.text.get().trim()) ? true : viewModel.isVoiceMsgUi().get()
                textUtilsIsEmptyViewModelTextTrimBooleanTrueViewModelIsVoiceMsgUi = ((textUtilsIsEmptyViewModelTextTrim) ? (true) : (viewModelIsVoiceMsgUiGet));
            if((dirtyFlags & 0xd40L) != 0) {
                if(textUtilsIsEmptyViewModelTextTrimBooleanTrueViewModelIsVoiceMsgUi) {
                        dirtyFlags |= 0x20000L;
                }
                else {
                        dirtyFlags |= 0x10000L;
                }
            }


                // read TextUtils.isEmpty(viewModel.text.get().trim()) ? true : viewModel.isVoiceMsgUi().get() ? View.GONE : View.VISIBLE
                textUtilsIsEmptyViewModelTextTrimBooleanTrueViewModelIsVoiceMsgUiViewGONEViewVISIBLE = ((textUtilsIsEmptyViewModelTextTrimBooleanTrueViewModelIsVoiceMsgUi) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0x800L) != 0) {
            // api target 1

            this.btnAdd.setOnClickListener(mCallback16);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setImageViewTint(this.btnAdd, getColorFromResource(btnAdd, R.color.amityColorBase), com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE1);
            this.etMessage.setHintTextColor(getColorFromResource(etMessage, R.color.amityColorBase));
            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.etMessage, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, etMessageandroidTextAttrChanged);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setEkoTextColor(this.etMessage, (com.ekoapp.ekosdk.uikit.common.views.ColorShade)null, com.ekoapp.ekosdk.uikit.common.views.ColorShade.SHADE3);
            this.ivSend.setOnClickListener(mCallback17);
            this.ivVoiceMsg.setOnClickListener(mCallback15);
            com.ekoapp.ekosdk.uikit.components.BindingUtilityKt.setOnRVScroll(this.rvChatList, (com.ekoapp.ekosdk.uikit.components.OnScroll)null, mCallback14);
        }
        if ((dirtyFlags & 0xc80L) != 0) {
            // api target 1

            androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable(this.btnAdd, viewModelShowComposeBarBtnAddAndroidDrawableAmityIcCrossBtnAddAndroidDrawableAmityIcChatAdd);
            this.composeBar.getRoot().setVisibility(viewModelShowComposeBarViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xd40L) != 0) {
            // api target 1

            this.btnAdd.setVisibility(textUtilsIsEmptyViewModelTextTrimViewModelIsVoiceMsgUiViewVISIBLEViewGONE);
            this.ivSend.setVisibility(textUtilsIsEmptyViewModelTextTrimBooleanTrueViewModelIsVoiceMsgUiViewGONEViewVISIBLE);
        }
        if ((dirtyFlags & 0xc02L) != 0) {
            // api target 1

            this.chatToolBar.setTitle(viewModelTitleGet);
        }
        if ((dirtyFlags & 0xc00L) != 0) {
            // api target 1

            this.composeBar.setClickListener(viewModelComposeBarClickListener);
        }
        if ((dirtyFlags & 0xd00L) != 0) {
            // api target 1

            this.etMessage.setEnabled(viewModelIsVoiceMsgUi);
            androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable(this.ivVoiceMsg, viewModelIsVoiceMsgUiIvVoiceMsgAndroidDrawableAmityIcKeyboardIvVoiceMsgAndroidDrawableAmityIcVoiceMessage);
            this.recordBackground.setVisibility(viewModelIsVoiceMsgUiViewVISIBLEViewGONE);
            this.tvRecord.setVisibility(viewModelIsVoiceMsgUiViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xc40L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.etMessage, viewModelTextGet);
        }
        if ((dirtyFlags & 0xc10L) != 0) {
            // api target 1

            this.mboundView1.getRoot().setVisibility(viewModelIsScrollableViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0xc20L) != 0) {
            // api target 1

            this.mboundView1.setDate(viewModelStickyDateGet);
        }
        if ((dirtyFlags & 0xc04L) != 0) {
            // api target 1

            this.mboundView1.setDateFillColor(androidxDatabindingViewDataBindingSafeUnboxViewModelDateFillColorGet);
        }
        if ((dirtyFlags & 0xc08L) != 0) {
            // api target 1

            this.recorderView.setVisibility(viewModelIsRecordingViewVISIBLEViewINVISIBLE);
        }
        executeBindingsOn(chatToolBar);
        executeBindingsOn(mboundView1);
        executeBindingsOn(composeBar);
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 2: {
                // localize variables for thread safety
                // viewModel
                com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.toggleRecordingView();
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // viewModel
                com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.toggleComposeBar();
                }
                break;
            }
            case 4: {
                // localize variables for thread safety
                // viewModel
                com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel viewModel = mViewModel;
                // viewModel != null
                boolean viewModelJavaLangObjectNull = false;



                viewModelJavaLangObjectNull = (viewModel) != (null);
                if (viewModelJavaLangObjectNull) {


                    viewModel.sendMessage();
                }
                break;
            }
        }
    }
    public final void _internalCallbackOnScrollStateChanged(int sourceId , androidx.recyclerview.widget.RecyclerView callbackArg_0, int callbackArg_1) {
        // localize variables for thread safety
        // viewModel
        com.ekoapp.ekosdk.uikit.chat.messages.viewModel.EkoMessageListViewModel viewModel = mViewModel;
        // viewModel != null
        boolean viewModelJavaLangObjectNull = false;



        viewModelJavaLangObjectNull = (viewModel) != (null);
        if (viewModelJavaLangObjectNull) {




            viewModel.onRVScrollStateChanged(callbackArg_0, callbackArg_1);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): chatToolBar
        flag 1 (0x2L): viewModel.title
        flag 2 (0x3L): viewModel.dateFillColor
        flag 3 (0x4L): viewModel.isRecording()
        flag 4 (0x5L): viewModel.isScrollable()
        flag 5 (0x6L): viewModel.stickyDate
        flag 6 (0x7L): viewModel.text
        flag 7 (0x8L): viewModel.showComposeBar
        flag 8 (0x9L): viewModel.isVoiceMsgUi()
        flag 9 (0xaL): composeBar
        flag 10 (0xbL): viewModel
        flag 11 (0xcL): null
        flag 12 (0xdL): (TextUtils.isEmpty(viewModel.text.get().trim())) & (!viewModel.isVoiceMsgUi().get()) ? View.VISIBLE : View.GONE
        flag 13 (0xeL): (TextUtils.isEmpty(viewModel.text.get().trim())) & (!viewModel.isVoiceMsgUi().get()) ? View.VISIBLE : View.GONE
        flag 14 (0xfL): viewModel.isRecording().get() ? View.VISIBLE : View.INVISIBLE
        flag 15 (0x10L): viewModel.isRecording().get() ? View.VISIBLE : View.INVISIBLE
        flag 16 (0x11L): TextUtils.isEmpty(viewModel.text.get().trim()) ? true : viewModel.isVoiceMsgUi().get() ? View.GONE : View.VISIBLE
        flag 17 (0x12L): TextUtils.isEmpty(viewModel.text.get().trim()) ? true : viewModel.isVoiceMsgUi().get() ? View.GONE : View.VISIBLE
        flag 18 (0x13L): TextUtils.isEmpty(viewModel.text.get().trim()) ? true : viewModel.isVoiceMsgUi().get()
        flag 19 (0x14L): TextUtils.isEmpty(viewModel.text.get().trim()) ? true : viewModel.isVoiceMsgUi().get()
        flag 20 (0x15L): viewModel.isScrollable().get() ? View.VISIBLE : View.GONE
        flag 21 (0x16L): viewModel.isScrollable().get() ? View.VISIBLE : View.GONE
        flag 22 (0x17L): viewModel.showComposeBar.get() ? View.VISIBLE : View.GONE
        flag 23 (0x18L): viewModel.showComposeBar.get() ? View.VISIBLE : View.GONE
        flag 24 (0x19L): viewModel.isVoiceMsgUi().get() ? @android:drawable/amity_ic_keyboard : @android:drawable/amity_ic_voice_message
        flag 25 (0x1aL): viewModel.isVoiceMsgUi().get() ? @android:drawable/amity_ic_keyboard : @android:drawable/amity_ic_voice_message
        flag 26 (0x1bL): viewModel.isVoiceMsgUi().get() ? View.VISIBLE : View.GONE
        flag 27 (0x1cL): viewModel.isVoiceMsgUi().get() ? View.VISIBLE : View.GONE
        flag 28 (0x1dL): viewModel.showComposeBar.get() ? @android:drawable/amity_ic_cross : @android:drawable/amity_ic_chat_add
        flag 29 (0x1eL): viewModel.showComposeBar.get() ? @android:drawable/amity_ic_cross : @android:drawable/amity_ic_chat_add
    flag mapping end*/
    //end
}