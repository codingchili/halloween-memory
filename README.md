HalloweenMemory
===============

Memory game with a halloween theme from 2014 - rebooted as an Android app!

![alt text](https://raw.githubusercontent.com/codingchili/halloween-memory/master/halloween.png "Current snapshot version")

#### In progress
- [ ] port to android.
- [ ] google play store deployment.
- [ ] matchmaking / online play
- [ ] rewrite android parts in Kotlin

#### Port progress
- [ ] multi-project build to build artifacts for both mobile and desktop.
- [ ] wrap drawing APIs to reuse draw calls.
- [ ] use the gyroscope sensor for parallax effects.

## Building

To build the desktop jar run
```
gradle jar
```

To start the game,
```
java -jar halloween.jar
```

Android build TBD.

## Contributing
All contributions are welcome, submit a PR, issue or a review.

Contains Graphics by Arrioch, Milos Markovic. CC-BY-NC-ND (4.0)
