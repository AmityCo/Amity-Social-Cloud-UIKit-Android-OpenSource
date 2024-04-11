package com.amity.socialcloud.uikit.common.eventbus

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers

open class BaseStateEventBus<T : Any> {
	
	private val eventPublisher: PublishProcessor<T> = PublishProcessor.create()
	private var currentEvent: T? = null
	
	fun observe(): Flowable<T> {
		return currentEvent
			?.let(eventPublisher::startWithItem)
			?: eventPublisher
				.onBackpressureLatest()
				.subscribeOn(Schedulers.io())
	}
	
	fun publish(event: T) {
		currentEvent = event
		eventPublisher.onNext(event)
	}
	
	fun getCurrentEvent(): T? {
		return currentEvent
	}
	
}