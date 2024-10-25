#include <algorithm>
#include <chrono>
#include <future>
#include <iostream>
#include <iterator>
#include <thread>
#include <vector>

using namespace std;

class Song {
    public:
        int index;

        Song(int idx) {
            this->index = idx;
        }
};

Song learn_song() {
    int song_index = 2;
    this_thread::sleep_for(chrono::seconds(1));
    cout << "Learning song with index " << song_index << endl;
    return Song(song_index);
}

void sing_song(Song song) {
    cout << "Singing song with index " << song.index << endl;
}

void dance() {
    cout << "Dancing!" << endl;
}

int learn_and_sing() {
    Song song = async(launch::async, learn_song).get();
    async(launch::async, sing_song, song).get();
    return song.index;
}

int async_main() {
    // Can't have vector of different data types using STL before C++17
    // otherwise: vector<int> results = {async(launch::async, learn_and_sing).get(), async(launch::async, dance).get()};
    async(launch::async, dance).get();
    int result = async(launch::async, learn_and_sing).get();
    return result;
}

bool check_name(string name) {
    string invitees[] = {"Alice", "Bob", "Peggy", "Victor"}; // could use an unordered_set
    this_thread::sleep_for(chrono::seconds(2));
    return find(begin(invitees), end(invitees), name) != end(invitees);
}

int async_num_intruders() {
    string names[] = {"Alice", "Bob", "Eve", "Mallory", "Peggy", "Victor"};
    cout << "Checking in guests..." << endl;
    long long start_time = chrono::duration_cast<chrono::milliseconds>(chrono::system_clock::now().time_since_epoch()).count();
    vector<future<bool>> futures;
    transform(begin(names), end(names), back_inserter(futures), [](string name) { return async(launch::async, check_name, name); });
    vector<bool> results;
    transform(futures.begin(), futures.end(), back_inserter(results), [](future<bool>& fut) { return fut.get(); });
    // The 2 lines of code above are the same as running:
    // bool result0 = futures[0].get();
    // bool result1 = futures[1].get();
    // bool result2 = futures[2].get();
    // bool result3 = futures[3].get();
    // bool result4 = futures[4].get();
    // bool result5 = futures[5].get();
    // vector<bool> results = {result0, result1, result2, result3, result4, result5};
    long long end_time = chrono::duration_cast<chrono::milliseconds>(chrono::system_clock::now().time_since_epoch()).count();
    double elapsed = (end_time - start_time) / 1000.0;
    cout << "Checking in guests took " << elapsed << " seconds" << endl;
    return count(results.begin(), results.end(), false);
}

int main(void) {
    int result = async_main();
    cout << "Got this index from async_main: " << result << endl;
    int intruders = async_num_intruders();
    cout << "There are " << intruders << " intruders!" << endl;
    return 0;
}
