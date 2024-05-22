async function learnSong() {
    return new Promise((resolve) => {
        const songIndex = 2;
        setTimeout(() => {
            resolve({
                index: songIndex,
            });
            console.log(`Learning song with index ${songIndex}`);
        }, 1000);
    });
}

async function singSong(obj) {
    console.log(`Singing song with index ${obj.index}`);
}

async function dance() {
    console.log("Dancing!");
}

async function learnAndSing() {
    const song = await learnSong();
    await singSong(song);
    return song.index;
}

async function asyncMain() {
    const results = await Promise.all([learnAndSing(), dance()]);
    return results[0];
}

asyncMain().then((result) => {
    console.log(`Got this index from async main: ${result}`);
});
