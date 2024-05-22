import asyncio


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


result = asyncio.run(async_main())
print(f"Got this index from async main: {result}")
