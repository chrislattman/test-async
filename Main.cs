async Task<Song> learnSong() {
    int songIndex = 2;
    await Task.Delay(1000);
    Console.WriteLine("Learning song with index " + songIndex);
    return new Song(songIndex);
}

async Task singSong(Song song) {
    Console.WriteLine("Singing song with index " + song.index);
}

async Task dance() {
    Console.WriteLine("Dancing!");
}

async Task<int> learnAndSing() {
    Song song = await learnSong();
    await singSong(song);
    return song.index;
}

async Task<int> asyncMain() {
    var results = await Task.WhenAll(learnAndSing(), Task.Run(() => { dance(); return 0; }));
    return results[0];
}

async Task<bool> checkName(string name) {
    string[] invitees = ["Alice", "Bob", "Peggy", "Victor"]; // could use a HashSet
    await Task.Delay(2000);
    return invitees.Contains(name);
}

async Task<int> asyncNumIntruders() {
    string[] names = ["Alice", "Bob", "Eve", "Mallory", "Peggy", "Victor"];
    Console.WriteLine("Checking in guests...");
    long startTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
    var tasks = names.Select(checkName);
    bool[] results = await Task.WhenAll(tasks);
    // The line of code above is the same as running:
    // bool result0 = await tasks.ElementAt(0);
    // bool result1 = await tasks.ElementAt(1);
    // bool result2 = await tasks.ElementAt(2);
    // bool result3 = await tasks.ElementAt(3);
    // bool result4 = await tasks.ElementAt(4);
    // bool result5 = await tasks.ElementAt(5);
    // bool[] results = [result0, result1, result2, result3, result4, result5];
    long endTime = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
    double elapsed = (endTime - startTime) / 1000.0;
    Console.WriteLine("Checking in guests took " + elapsed + " seconds");
    return results.Where(val => !val).Count();
}

int result = await asyncMain();
Console.WriteLine("Got this index from asyncMain: " + result);
int intruders = await asyncNumIntruders();
Console.WriteLine("There are " + intruders + " intruders!");

class Song(int index)
{
    public int index = index;
}
