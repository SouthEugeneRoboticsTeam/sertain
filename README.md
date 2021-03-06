# Sertain

<p align="center">
  <a href="https://github.com/SouthEugeneRoboticsTeam/sertain/actions">
    <img src="https://img.shields.io/github/workflow/status/SouthEugeneRoboticsTeam/sertain/Java CI?style=flat-square" />
  </a>
  
  <a href="https://jitpack.io/#sertain/org.sertain">
    <img src="https://img.shields.io/jitpack/v/github/SouthEugeneRoboticsTeam/sertain?style=flat-square" />
  </a>
</p>

**Sertain is a framework designed to make programming robots for the First Robotics Competition as easy as possible. It combines wrappers for WPILib, Pheonix motor controllers, and Spark motor controllers. The framework is written in Kotlin, a language meant to supplement and enhance Java.**

## Why Sertain?

### Minimizes mistakes

Sertain is a framework for beginners and experienced coders alike, so it is designed to prevent as many mistakes as possible. It requires you to specify units so you never mix up meters and feet. It has network tables integration that allows for safe property access and reactive updating. Its clever take on subsystems prevents any subsystem from running two tasks at once. Sertain is dedicated to preventing your robot from running into a wall.

### Cuts down on boilerplate code

Sertain is straight to the point and doesn't require any excess setup. By utilizing Kotlin's syntax, DSL builders, callbacks, and coroutines, Sertain allows you to focus less on boilerplate and more on coding your robot.

### Utilizes Kotlin

Sertain is built from the ground up to take full advantage of the Kotlin, a modern programming language built on top of Java. While Java has gotten better over the years, it still has problems like poor null safety and being very verbose. Kotlin's syntax is easier on the eyes and allows you to do more while write less code. Want to learn Kotlin? Check out this [tutorial](https://beginnersbook.com/2017/12/kotlin-tutorial/)!

## Installation

It is recommended by FIRST that you use [Gradle](https://gradle.org/) to build your FRC projects, so that is the method that this guide will cover.

First, add this line to your `dependencies` block:

```gradle
maven { url 'https://jitpack.io' }
```

Then, add this line to your `repositories` block:

```gradle
implementation 'com.github.SouthEugeneRoboticsTeam:sertain:0.1.1'
```

## What's it look like?

Here's a simple program that runs a motor controller at full strength while the robot is in teleop mode:

```kotlin
suspend fun main() = Robot.start {
  val motor = MotorController(TalonId(1))
  
  onTeleop {
    periodic {
      motor.setPercentOutput(1.0)
    }
  }
}
```

Like what you see? Take a look at the [docs](https://github.com/sertainLib/sertain/wiki)!
