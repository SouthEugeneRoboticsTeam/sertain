package org.sert2521.sertain.events

import org.sert2521.sertain.coroutines.Observable

abstract class ObservableEvent<T> (target: Observable<T>, val value: T): TargetedEvent<Observable<T>>(target)

class Change<T>(target: Observable<T>, value: T) : ObservableEvent<T>(target, value)
