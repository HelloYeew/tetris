# another tetris go bruh

just another tetris that can play with your friends but you don't have any friends so you need to pretend that you have friends.

This project is cover under [DON'T BE A DICK PUBLIC LICENSE WITH EXTRA LICENSE FOR SSD FINAL PROJECT](LICENSE.md) please read it before you start cloning and copy this to your own final project.

## Special requirements

- This project needs latest version of Java due to the lambda expression that's use a lot on codebase, and it's not supported by older version of Java.
- Don't forget to import the library file in `lib` folder before start build the project.

## Special features

- Since the playfield that I implement can attach on any new `JFrame`, add controller and play, so I add a local player mode that you can play with your keyboard with your friend.
- All client support debug window that's available when the `DEBUG` is set to `true` in the client class.
- Server can send some command to client like kick all player or pause the game like real-time game.
- The local client can pause, resume, and restart the game freely using keyboard. (But the multiplayer client can't do that)
    - Restart - Grave accent key (`~`)
    - Pause and resume - `SPACE` key
- Some special key when debug is on
    - Generate the permanent row on other playfield (available only on local client) - `O` and `P` key
    - Quit - `ESC` key

