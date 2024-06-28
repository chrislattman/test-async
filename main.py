import asyncio
import time


class Song:
    index: int

    def __init__(self, idx: int) -> None:
        self.index = idx


async def learn_song() -> Song:
    song_index = 2
    await asyncio.sleep(1)
    print(f"Learning song with index {song_index}")
    return Song(song_index)


async def sing_song(song: Song):
    print(f"Singing song with index {song.index}")


async def dance():
    print("Dancing!")


async def learn_and_sing() -> int:
    song = await learn_song()
    await sing_song(song)
    return song.index


async def async_main() -> int:
    results = await asyncio.gather(learn_and_sing(), dance())
    return results[0]


async def check_name(name: str) -> bool:
    invitees = ["Alice", "Bob", "Peggy", "Victor"] # could use a set
    await asyncio.sleep(2)
    return name in invitees


async def async_num_intruders() -> int:
    names = ["Alice", "Bob", "Eve", "Mallory", "Peggy", "Victor"]
    print("Checking in guests...")
    start_time = time.time()
    tasks = [asyncio.create_task(check_name(name)) for name in names] # Tasks start executing here
    results = await asyncio.gather(*tasks)
    # The line of code above is the same as running:
    # (each Task must be defined in a variable beforehand)
    # result0 = await tasks[0]
    # result1 = await tasks[1]
    # result2 = await tasks[2]
    # result3 = await tasks[3]
    # result4 = await tasks[4]
    # result5 = await tasks[5]
    # results = [result0, result1, result2, result3, result4, result5]
    end_time = time.time()
    elapsed = end_time - start_time
    print(f"Checking in guests took {elapsed} seconds")
    return results.count(False)


result = asyncio.run(async_main())
print(f"Got this index from async main: {result}")
intruders = asyncio.run(async_num_intruders())
print(f"There are {intruders} intruders!")
