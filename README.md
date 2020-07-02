<p align="center">
  <a href="#">
    <img alt="Botful" src="https://i.imgur.com/WAhi8NS.png" />
  </a>
</p>

<h4 align="center">
  Write more for your robot with less, and be absolutely <i>SERT</i>ain that your robot works.
</h4>

<p align="center">
  <a href="https://github.com/SouthEugeneRoboticsTeam/sertain/actions">
    <img src="https://img.shields.io/github/workflow/status/SouthEugeneRoboticsTeam/sertain/Java CI?style=flat-square" />
  </a>
  
  <a href="https://jitpack.io/#SouthEugeneRoboticsTeam/org.sert2521.sertain">
    <img src="https://img.shields.io/jitpack/v/github/SouthEugeneRoboticsTeam/sertain?style=flat-square" />
  </a>
  
  <a href="https://sert2521.org">
    <img src="https://img.shields.io/badge/sert-2521-blueviolet?style=flat-square" />
  </a>
</p>
 
### Getting Started

Sertain is still in pre-release, but you can use the latest version by adding this to your gradle build script:

```gradle
repositories {
  // other repositories
  maven { url 'https://jitpack.io' }
}

dependencies {
  // other dependencies
  implementation 'com.github.SouthEugeneRoboticsTeam:org.sert2521.sertain:0.0.12'
}
```

You can also replace the any commit on master, and you can replace `0.0.12` with `any-branch-SNAPSHOT`. You can also use a commit hash as the version without a branch name. See [the Jitpack docs](https://jitpack.io/docs/) for more details.

### Creating a Robot Program
To create a robot program, start by using the `robot` function. The function takes a lambda, which should serve as the entry point of the robot program. For example:

```kotlin
suspend fun main() = robot {
  // Your code goes here
}
```

### Coroutines

Note that this library is built using coroutines. However, the RoboRIO only has access to two threads, so if you should need to create a global coroutine, you need to use `RobotScope` as the coroutine scope. `RobotScope` can be accessed through the `robot` block. Follow this example:

```kotlin
robot {
  launch {
    // Global coroutine code here
  }
}
```

### Events

It is also possible to subscribe to events. Events are the core component of sertain, and run almost everything. To create an event, do:

```kotlin
class MyEvent : Event
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
Keep in mind that `subscribe` behaves like `launch`. It returns a `Job`, so you can use functions like `cancel` and `join` on it.

Sertain also provides pre-defined event subscribers. Event subscribers simply wrap the `subscribe` function for a specific type of event. For instance, `onEnable` subscribes to the `Enable` event, like so:

```kotlin
onEnable {
  // Code to run when the event occures.
}
```

### Subsystems

Subsystems represent different components of the robot. A subsystem should only preform one task at a time. If you need to run more than one task, then you should probably be using two different subsystems. To create a subsystem, extend the `Subsystem` class.

```kotlin
class MySubsystem : Subsystem() {
  // Subsystem code goes here
}
```

To begin using your subsystem, you need to call `add` to get it's `Worker`. A `Worker` allows you to access your subsystem in a safe mannor. You should call `add` above the main function to ensure that it gets called at the beggining of the program:

```kotlin
val mySubsystem = add<MySubsystem>()
```
 
You can assign tasks to subsystems by calling `use`. Inside the block, you can get instances of your subsystems in the order they are passed into `use`. The block should contain the code that you want to run. Use this example:

```kotlin
use(mySubsystem, myOtherSubsystem) { mySubsystem, myOtherSubsystem ->
  // code goes here
}
```

Additionaly, if your command only uses one subsystem, you can just call your subsystem like a function instead. Follow this example:

```kotlin
mySubsystem { mySubsystem ->
  // code goes here
}
```

Note that one task can be assigned to multiple subsystems, and that susystems can only do one task at a time. If you try to assign a task to an already occupied subsystem, the previous task will be replaced by the new one. Also note that because a task is a coroutine that is cancelled once it is finished, they can be used in combination with event subscribers to subscribe only for the duration of the task.

*A full guide is coming soon.*
