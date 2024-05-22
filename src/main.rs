use std::time;

use async_std::task;
use futures::executor::block_on;

struct Song {
    index: u32
}

async fn learn_song() -> Song {
    // Asynchronously sleep for 1 second, letting dance() run
    // while waiting.
    let song_index = 2;
    task::sleep(time::Duration::from_secs(1)).await;
    println!("Learning song with index {}", song_index);
    return Song{index: song_index};
}

async fn sing_song(song: &Song) {
    println!("Singing song with index {}", song.index);
}

async fn dance() {
    println!("Dancing!");
}

async fn learn_and_sing() -> u32 {
    // Wait until the song has been learned before singing it.
    // We use `.await` here rather than `block_on` to prevent blocking the
    // thread, which makes it possible to `dance` at the same time.
    let song = learn_song().await;
    sing_song(&song).await;
    return song.index;
}

async fn async_main() -> u32 {
    // `join!` is like `.await` but can wait for multiple futures concurrently.
    // If we're temporarily blocked in the `learn_and_sing` future, the `dance`
    // future will take over the current thread. If `dance` becomes blocked,
    // `learn_and_sing` can take back over. If both futures are blocked, then
    // `async_main` is blocked and will yield to the executor.
    let results = futures::join!(learn_and_sing(), dance());
    return results.0;
}

fn main() {
    let result = block_on(async_main());
    println!("Got this index from async main: {}", result);
}
