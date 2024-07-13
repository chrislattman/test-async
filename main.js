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

async function checkName(name) {
    const invitees = ["Alice", "Bob", "Peggy", "Victor"]; // could use a Set
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve(invitees.includes(name));
        }, 2000);
    });
}

async function asyncNumIntruders() {
    const names = ["Alice", "Bob", "Eve", "Mallory", "Peggy", "Victor"];
    console.log("Checking in guests...");
    const startTime = Date.now();
    const promises = names.map(checkName); // Promises start executing here
    const results = await Promise.all(promises);
    // The line of code above is the same as running:
    // (each Promise must be defined in a variable beforehand)
    // const result0 = await promises[0];
    // const result1 = await promises[1];
    // const result2 = await promises[2];
    // const result3 = await promises[3];
    // const result4 = await promises[4];
    // const result5 = await promises[5];
    // const results = [result0, result1, result2, result3, result4, result5];
    const endTime = Date.now();
    const elapsed = (endTime - startTime) / 1000;
    console.log(`Checking in guests took ${elapsed} seconds`);
    return results.filter((val) => !val).length;
}

async function runner() {
    const result = await asyncMain();
    console.log(`Got this index from asyncMain: ${result}`);
    const intruders = await asyncNumIntruders();
    console.log(`There are ${intruders} intruders!`);
}

runner();
