package com.amity.socialcloud.uikit.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.common.utils.AmityThemeUtil
import com.amity.socialcloud.uikit.sample.databinding.AmityActivitySettingBinding

class AmitySettingActivity : AppCompatActivity() {

    private val binding: AmityActivitySettingBinding by lazy {
        AmityActivitySettingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        AmityThemeUtil.setCurrentTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.amity_activity_setting)

        binding.btnConfirm.setOnClickListener {
            setTheme()
        }

        binding.btnLogout.setOnClickListener {
            val sharedPref = getSharedPreferences(AmityConstants.PREF_NAME, Context.MODE_PRIVATE)
            sharedPref?.edit()?.clear()?.apply()
            AmityCoreClient.unregisterDeviceForPushNotification(AmityCoreClient.getUserId()).subscribe()
            AmityCoreClient.logout().subscribe {
                this.finish()
            }
        }
    }

    private fun setTheme() {
        val selectedId = binding.rgTheme.checkedRadioButtonId
        val sharedPref = this.getSharedPreferences("AMITY_PREF", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            if (selectedId == binding.theme1.id) {
                putInt("THEME", 1)
            } else if (selectedId == binding.theme2.id) {
                putInt("THEME", 2)
            }
            commit()
        }

        val featureIntent = Intent(this, AmityFeatureListActivity::class.java)
        startActivity(featureIntent)
    }
}