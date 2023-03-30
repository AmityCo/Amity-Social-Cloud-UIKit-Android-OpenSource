package com.amity.socialcloud.uikit.community.profile.fragment

import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityPickerFragment
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.common.views.dialog.AmityBottomSheetDialogFragment
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityOptionMenuColorUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentEditUserProfileBinding
import com.amity.socialcloud.uikit.community.profile.viewmodel.AmityEditUserProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class AmityUserProfileEditorFragment : AmityPickerFragment() {
    private var menuItemSaveProfile: MenuItem? = null
    private val ID_MENU_ITEM_SAVE_PROFILE: Int = 111
    private val TAG = AmityUserProfileEditorFragment::class.java.canonicalName
    private val viewModel: AmityEditUserProfileViewModel by activityViewModels()
    lateinit var binding: AmityFragmentEditUserProfileBinding
    private var profileUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addViewModelListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.amity_fragment_edit_user_profile,
                container,
                false
            )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        getUserDetails()
        binding.avatarView.setOnClickListener {
            showOptionTakePhoto()
        }

        observeProfileUpdate()

        binding.etDisplayName.filters = arrayOf<InputFilter>(LengthFilter(viewModel.userNameMaxTextLength))
        binding.etAbout.filters = arrayOf<InputFilter>(LengthFilter(viewModel.aboutMaxTextLength))
    }


    private fun observeProfileUpdate() {
        viewModel.mediatorLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.checkProfileUpdate()
        })
        viewModel.hasProfileUpdate.observe(viewLifecycleOwner, Observer {
            if (it != null)
                updateSaveProfileMenu(it)
        })
    }


    override fun onImagePicked(data: Uri?) {
        profileUri = data
        setProfilePicture(profileUri)
        viewModel.checkProfileUpdate()
    }

    private fun setProfilePicture(profileUri: Uri?) {
        viewModel.updateProfileUri(profileUri)
        Glide.with(requireContext())
            .load(profileUri)
            .placeholder(R.drawable.amity_ic_default_profile1)
            .centerCrop()
            .into(binding.ivAvatar)
    }

    override fun onFilePicked(data: Uri?) {

    }

    override fun onPhotoClicked(file: File?) {
        profileUri = Uri.fromFile(file)
        setProfilePicture(profileUri)
        viewModel.checkProfileUpdate()
    }


    private fun getUserDetails() {
        viewModel.getUser()?.also {
            disposable.add(
                it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it != null) {
                            viewModel.user = it
                            setupUserData(it)
                        }
                    }, {
                        Log.d(TAG, it.message ?: "")
                    })
            )
        }
    }

    private fun setupUserData(user: AmityUser) {
        binding.etDisplayName.setText(user.getDisplayName())
        binding.etAbout.setText(user.getDescription())
        user.getAvatar()?.getUrl(AmityImage.Size.SMALL)?.apply {
            setProfilePicture(Uri.parse(this))
        }
    }

    private fun initToolBar() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.amity_edit_profile)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuItemSaveProfile =
            menu.add(Menu.NONE, ID_MENU_ITEM_SAVE_PROFILE, Menu.NONE, getString(R.string.amity_save))
        menuItemSaveProfile?.setTitle(R.string.amity_save)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        updateSaveProfileMenu(viewModel.hasProfileUpdate.value ?: false)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == ID_MENU_ITEM_SAVE_PROFILE) {
            if (profileUri == null) {
                updateUser()
            } else {
                uploadProfilePicture(profileUri!!)
            }
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateSaveProfileMenu(enabled: Boolean) {
        menuItemSaveProfile?.isEnabled = enabled
        val s = SpannableString(getString(R.string.amity_save))
        s.setSpan(
            ForegroundColorSpan(
                AmityOptionMenuColorUtil.getColor(
                    menuItemSaveProfile?.isEnabled ?: false, requireContext()
                )
            ), 0, s.length, 0
        )
        menuItemSaveProfile?.title = s
    }

    private fun addViewModelListener() {
        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.PROFILE_PICTURE_UPLOAD_FAILED -> {
                    handleErrorProfilePictureUpload()
                }
                AmityEventIdentifier.PROFILE_PICTURE_UPLOAD_SUCCESS -> {
                    updateUser()
                }
                else -> {
                }
            }
        }
    }

    private fun handleErrorProfilePictureUpload() {
        view?.showSnackBar(getString(R.string.amity_upload_failed_profile_picture), Snackbar.LENGTH_SHORT)
    }

    private fun uploadProfilePicture(uri: Uri) {
        disposable.add(
            viewModel.uploadProfilePicture(
                uri
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewModel.updateImageUploadStatus(it)
                }.doOnError {
                    viewModel.errorOnUpdate()
                }.subscribe()
        )
    }

    private fun showOptionTakePhoto() {
        val fragment =
            AmityBottomSheetDialogFragment.newInstance(R.menu.amity_upload_profile_picture)

        fragment.show(childFragmentManager, AmityBottomSheetDialogFragment.toString())
        fragment.setOnNavigationItemSelectedListener(object :
            AmityBottomSheetDialogFragment.OnNavigationItemSelectedListener {
            override fun onItemSelected(item: MenuItem) {
                handleUploadPhotoOption(item)
            }

        })
    }

    private fun handleUploadPhotoOption(item: MenuItem) {
        if (item.itemId == R.id.actionTakePicture) {
            takePicture()
        } else if (item.itemId == R.id.actionPickPicture) {
            pickImage()
        }
    }

    private fun updateUser() {
        disposable.add(
            viewModel.updateUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    activity?.onBackPressed()
                }, {
                    viewModel.errorOnUpdate()
                    context?.also {
                        view?.showSnackBar(getString(R.string.amity_edit_profile_update_failed), Snackbar.LENGTH_SHORT)
                    }
                })
        )
    }

    class Builder internal constructor(){
        fun build(activity: AppCompatActivity): AmityUserProfileEditorFragment {
            return AmityUserProfileEditorFragment()
        }
    }

    companion object {

        fun newInstance(): Builder {
            return Builder()
        }
    }

}