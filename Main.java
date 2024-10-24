import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
        return Arrays.asList(invitees).contains(name);
    }

    private static long asyncNumIntruders() {
        String[] names = {"Alice", "Bob", "Eve", "Mallory", "Peggy", "Victor"};
        System.out.println("Checking in guests...");
        long startTime = System.currentTimeMillis();
        List<CompletableFuture<Boolean>> futures = Arrays.asList(names).stream()
            .map(name -> CompletableFuture.supplyAsync(() -> { return checkName(name); }))
            .collect(Collectors.toList());
        List<Boolean> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        // The line of code above is the same as running:
        // boolean result0 = futures.get(0).join();
        // boolean result1 = futures.get(1).join();
        // boolean result2 = futures.get(2).join();
        // boolean result3 = futures.get(3).join();
        // boolean result4 = futures.get(4).join();
        // boolean result5 = futures.get(5).join();
        // List<Boolean> results = Arrays.asList(result0, result1, result2, result3, result4, result5);
        long endTime = System.currentTimeMillis();
        double elapsed = (endTime - startTime) / 1000.0;
        System.out.println("Checking in guests took " + elapsed + " seconds");
        return results.stream().filter(e -> !e).count();
    }

    public static void main(String[] args) {
        Object result = asyncMain();
        System.out.println("Got this index from asyncMain: " + result);
        long intruders = asyncNumIntruders();
        System.out.println("There are " + intruders + " intruders!");
    }
}
