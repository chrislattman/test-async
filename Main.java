import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static class Song {
        int index;

        public Song(int idx) {
            this.index = idx;
        }
    }

    private static Song learnSong() {
        int songIndex = 2;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Learning song with index " + songIndex);
        return new Song(songIndex);
    }

    private static void singSong(Song song) {
        System.out.println("Singing song with index " + song.index);
    }

    private static void dance() {
        System.out.println("Dancing!");
    }

    private static int learnAndSing() {
        Song song = CompletableFuture.supplyAsync(Main::learnSong).join();
        CompletableFuture.runAsync(() -> { singSong(song); }).join();
        return song.index;
    }

    private static Object asyncMain() {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(Main::learnAndSing);
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(Main::dance);
        ArrayList<Object> results = CompletableFuture.allOf(future1, future2)
            .thenApply(unused -> {
                ArrayList<Object> list = new ArrayList<>();
                list.add(future1.join());
                list.add(future2.join());
                return list;
            })
            .join();
        return results.get(0);
    }

    private static boolean checkName(String name) {
        String[] invitees = {"Alice", "Bob", "Peggy", "Victor"}; // could use a HashSet
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        for (String invitee : invitees) {
            if (name.equals(invitee)) {
                return true;
            }
        }
        return false;
    }

    private static int asyncNumIntruders() {
        String[] names = {"Alice", "Bob", "Eve", "Mallory", "Peggy", "Victor"};
        System.out.println("Checking in guests...");
        long startTime = System.currentTimeMillis();
        ArrayList<CompletableFuture<Boolean>> futures = new ArrayList<>(names.length);
        for (String name : names) {
            futures.add(CompletableFuture.supplyAsync(() -> { return checkName(name); }));
        }
        int count = CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]))
            .thenApply(unused -> {
                int localCount = 0;
                for (CompletableFuture<Boolean> future : futures) {
                    if (!future.join()) {
                        localCount++;
                    }
                }
                return localCount;
            })
            .join();
        long endTime = System.currentTimeMillis();
        double elapsed = (endTime - startTime) / 1000.0;
        System.out.println("Checking in guests took " + elapsed + " seconds");
        return count;
    }

    public static void main(String[] args) {
        Object result = asyncMain();
        System.out.println("Got this index from asyncMain: " + result);
        int intruders = asyncNumIntruders();
        System.out.println("There are " + intruders + " intruders!");
    }
}