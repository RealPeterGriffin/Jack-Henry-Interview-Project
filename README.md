# Jack Henry Interview Assignment

An HTTP Server that allows clients to send GET requests containing coordinates to get a snapshot of the current weather

Built in Scala

Written by Peter Carbone

## Program Info

- Runs on port 50000 on localhost
- Only supports `GET` requests
- Recommended to use with sbt (`build.sbt` included in project).
- Sends weather info and 200 code if successful, also writes API call results to separate `.json` files.
- Server will run until process is killed or `sbt` is killed .

## Dependencies Used

- OpenJDK               23.0.1   
- Scala                 3.5.2
- Scala code runner     1.4.3
- `sbt`                 1.10.5
- `sbt` script          1.10.5
- Scala toolkit         0.1.7
- Requests              0.8.0

## How to Run

1. Clone repository and navigate to top-level directory.
2. In a terminal, type `compile` to get a working build, then type `run` to start the server.
3. In a separate terminal, send a `GET` request using the following format: `curl "http://localhost:50000/?latitude=<latitude>&longitude=<longitude>"`.
4. Profit! You should receive a response like the following: `Clear 32 Cold`.

## Shortcuts Taken

- Not every algorithm/process is the most efficient it can be. Part of this is because this is my first Scala program.
- If more than one identical `GET` requests are sent, any request sent after the first will be met with `curl: (52) Empty reply from server`.
- There is almost no error handling for this program (i.e. checking to see if coordinates are valid before execution, these are met with errors or an empty reply).
- No unit testing has been done for this assignment