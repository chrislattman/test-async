use std::time;

use async_std::task;
use futures::{executor, future};

struct Song {
    index: i32,
}

async fn learn_song() -> Song {
    // Asynchronously sleep for 1 second, letting dance() run
    // while waiting.
    let song_index = 2;
    task::sleep(time::Duration::from_secs(1)).await;
    println!("Learning song with index {song_index}");
    Song { index: song_index }
}

async fn sing_song(song: &Song) {
    println!("Singing song with index {}", song.index);
}

async fn dance() {
    println!("Dancing!");
}

async fn learn_and_sing() -> i32 {
    // Wait until the song has been learned before singing it.
    // We use `.await` here rather than `block_on` to prevent blocking the
    // thread, which makes it possible to `dance` at the same time.
    let song = learn_song().await;
    sing_song(&song).await;
    song.index
}

async fn async_main() -> i32 {
    // `join!` is like `.await` but can wait for multiple futures concurrently.
    // If we're temporarily blocked in the `learn_and_sing` future, the `dance`
    // future will take over the current thread. If `dance` becomes blocked,
    // `learn_and_sing` can take back over. If both futures are blocked, then
    // `async_main` is blocked and will yield to the executor.
    let results = futures::join!(learn_and_sing(), dance());
    results.0
}

async fn check_name(name: &str) -> bool {
    let invitees = ["Alice", "Bob", "Peggy", "Victor"]; // could use a HashSet
    task::sleep(time::Duration::from_secs(2)).await;
    invitees.contains(&name)
}

async fn async_num_intruders() -> usize {
    let names = ["Alice", "Bob", "Eve", "Mallory", "Peggy", "Victor"];
    println!("Checking in guests...");
    let start_time = time::SystemTime::now();
    let futures: Vec<_> = names.iter().map(|name| check_name(name)).collect();
    let results = future::join_all(futures).await;
    // No known equivalent to the line of code above due to borrow checker
    let end_time = time::SystemTime::now();
    let elapsed = end_time.duration_since(start_time).unwrap().as_secs_f64();
    println!("Checking in guests took {elapsed:.3} seconds");
    results.iter().filter(|result| !**result).count()
}

fn main() {
    let result = executor::block_on(async_main());
    println!("Got this index from async_main: {result}");
    let intruders = executor::block_on(async_num_intruders());
    println!("There are {intruders} intruders!");
}
