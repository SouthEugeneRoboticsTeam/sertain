# sertain

Library for FRC

### Setting Up

To create a robot program, start by using the `robot` function. The function takes a lambda, which should serve as the entry point of the robot program. For example:

```kotlin
fun main() = runBlocking {
  robot {
    // Your code goes here
  }
}
```

### Coroutines

Note that this library is built using coroutines. However, the RobotRIO only has access to two threads, so you need to use `RobotScope` instead of `GlobalScope` when calling coroutines. Follow this example:

```kotlin
RobotScope.launch {
  // Code to run in the coroutine
}
```

### Events

It is also possible to subscribe to events. Events are the core component of sertain, and run almost everything. To create an event, do:

```kotlin
class MyEvent : Event()
```

Then subscribe to the event by calling `subscribe`, like so:

```kotlin
subscribe<MyEvent> {
  // Code to run when event is fired
}
```

Finally, fire the event using `fire`:

```kotlin
fire(MyEvent())
```

Keep in mind that `subscribe` suspends the coroutine that it is called it. If that coroutine is cancelled, the subscription will no longer be active. This can be used to your advantage, for instance if you wanted to only subscribe until a certain event occured.

Sertain also provides pre-defined event subscribers. Event subscribers launch a new coroutine that is subscribed to the event. For example, `onConnect` subscribes to the `Connect` event, like so:

```kotlin
onConnect {
  // Code to run when the event occures.
}
```

### Subsystems

Subsystems represent different components of the robot. A subsystem should only preform one task at a time. If you need to run more than one task, then you should probably be using two different subsystems. To create a subsystem, extend the `Subsystem` class.

```kotlin
class MySubsystem : Subsystem("MY_SUBSYSTEM") {
  // Subsystem code goes here
}
```

There are several ways to use subsystems, but this guide will only cover one. To add the subsystem to the robot, you need to call `add` inside the `robot` function, like so:

```kotlin
robot {
  add<MySubsystem>()
}
```
 
You can assign tasks to subsystems by calling `doTask`. Inside the `doTask` block, you can call `use` to get instances of the subsystems that you wish to occupy. The `action` block should contain the code that you want to run. Use this example:

```kotlin
doTask {
  val mySubsystem = use<MySubsystem>()
  action {
    // Use the subsystem here
  }
}
```

Note that one task can be assigned to multiple subsystems, and that susystems can only do one task at a time. If you try to assign a task to an already occupied subsystem, the previous task will be replaced by the new one. Also note that because a task is a coroutine that is cancelled once it is finished, they can be used in combination with event subscribers to subscribe only for the duration of the task.
