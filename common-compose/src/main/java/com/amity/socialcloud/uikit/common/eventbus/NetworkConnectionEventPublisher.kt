package com.amity.socialcloud.uikit.common.eventbus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.core.content.ContextCompat
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent


object NetworkConnectionEventPublisher {
	fun initPublisher(context: Context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			ContextCompat.getSystemService(context, ConnectivityManager::class.java)?.registerDefaultNetworkCallback(
				object : ConnectivityManager.NetworkCallback() {
					override fun onAvailable(network: Network) {
						NetworkConnectionEventBus.publish(NetworkConnectionEvent.Connected)
						super.onAvailable(network)
					}
					
					override fun onLost(network: Network) {
						NetworkConnectionEventBus.publish(NetworkConnectionEvent.Disconnected)
						super.onLost(network)
					}
					
					override fun onUnavailable() {
						NetworkConnectionEventBus.publish(NetworkConnectionEvent.Disconnected)
						super.onUnavailable()
					}
				})
		} else {
			(context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
				?.let { connectivityManager ->
					val receiver = object : BroadcastReceiver() {
						override fun onReceive(context: Context?, intent: Intent?) {
							connectivityManager.activeNetworkInfo?.let {
								if (it.isConnected) {
									NetworkConnectionEventBus.publish(NetworkConnectionEvent.Connected)
								} else {
									NetworkConnectionEventBus.publish(NetworkConnectionEvent.Disconnected)
								}
							}
						}
					}
					context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
				}
		}
	}
	
}