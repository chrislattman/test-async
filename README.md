# async/await

The results of each of the applications should be the following:

```
Dancing!
Learning song with index 2
Singing song with index 2
Got this index from async main: 2
Checking in guests...
Checking in guests took 2.0008609294891357 seconds
There are 2 intruders!
```

where `Dancing!` should appear about 1 second before the following lines through `Checking in guests...`, which itself should appear about 2 seconds before the remaining lines (the timestamp will vary every time but only by milliseconds).

While Java and Go don't have the `async` keyword, there are equivalent constructs for asynchronous programming provided as examples.
