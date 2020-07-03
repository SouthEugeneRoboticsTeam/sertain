<p align="center">
  <a href="#">
    <img alt="Botful" src="https://i.imgur.com/WAhi8NS.png" width="750px" height="auto"/>
  </a>
</p>

<p align="center">
  <a href="https://github.com/Botful/botful/actions">
    <img src="https://img.shields.io/github/workflow/status/Botful/botful/Java CI?style=flat-square" />
  </a>
  
  <a href="https://jitpack.io/#Botful/org.botful">
    <img src="https://img.shields.io/jitpack/v/github/SouthEugeneRoboticsTeam/sertain?style=flat-square" />
  </a>
</p>

Botful is a framework designed to make programming robots for the First Robotics Competition as easy as possible. It combines wrappers for WPILib, Pheonix motor controllers, and Spark motor controllers. The framework is written in Kotlin, a language meant to supplement and enhance Java.

### Minimizes mistakes

Botful is a framework for beginners and experienced coders alike, so it is designed to prevent as many mistakes as possible. It requires you to specify units so you never mix up meters and feet. It has network tables integration that allows for safe property access and reactive updating. Its clever take on subsystems prevents any subsystem from running two tasks at once. Botful is dedicated to preventing your robot from running into a wall.

### Cuts down on boilerplate code

Botful is straight to the point and doesn't require any excess setup. By utilizing Kotlin's syntax, DSL builders, callbacks, and coroutines, Botful allows you to focus less on boilerplate and more on coding your robot.

### Installation

It is recommended by FIRST that you use [Gradle](https://gradle.org/) to build your FRC projects, so that is the method that this guide will cover.

First, add this line to your `dependencies` block:

```gradle
maven { url 'https://jitpack.io' }
```

Then, add this line to your `repositories` block:

```gradle
implementation 'com.github.Botful:org.botful:0.1.1'
```

### Getting Started

To get started, take a look at the [docs](https://botlin.gitbook.io/botlin/)!
