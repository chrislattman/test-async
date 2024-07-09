package main

import (
	"fmt"
	"slices"
	"sync"
	"time"
)

type Song struct {
	Index int
}

var song chan Song
var doneSingSong chan bool
var resultsChan []chan any
var wg sync.WaitGroup
var containsChan []chan bool

func learnSong() {
	songIndex := 2
	time.Sleep(1 * time.Second)
	fmt.Println("Learning song with index", songIndex)
	song <- Song{Index: songIndex}
}

func singSong(song Song) {
	fmt.Println("Singing song with index", song.Index)
	doneSingSong <- true
}

func dance() {
	defer wg.Done()
	fmt.Println("Dancing!")
	resultsChan[1] <- nil
}

func learnAndSing() {
	defer wg.Done()
	go learnSong()
	recvSong := <-song
	go singSong(recvSong)
	<-doneSingSong
	resultsChan[0] <- recvSong.Index
}

func asyncMain() any {
	song = make(chan Song, 1)
	doneSingSong = make(chan bool, 1)
	resultsChan = make([]chan any, 2)
	for i := range [2]int{} {
		resultsChan[i] = make(chan any, 1)
	}
	wg.Add(2)
	go learnAndSing()
	go dance()
	wg.Wait()
	return <-resultsChan[0]
}

func checkName(index int, name string) {
	defer wg.Done()
	invitees := []string{"Alice", "Bob", "Peggy", "Victor"} // could use a map
	time.Sleep(2 * time.Second)
	containsChan[index] <- slices.Contains(invitees, name)
}

func asyncNumIntruders() int {
	names := []string{"Alice", "Bob", "Eve", "Mallory", "Peggy", "Victor"}
	namesLen := len(names)
	fmt.Println("Checking in guests...")
	startTime := time.Now().UnixMilli()
	containsChan = make([]chan bool, namesLen)
	wg.Add(namesLen)
	for i := 0; i < namesLen; i++ {
		containsChan[i] = make(chan bool, 1)
		go checkName(i, names[i])
	}
	wg.Wait()
	endTime := time.Now().UnixMilli()
	elapsed := float64(endTime - startTime) / 1000.0
	fmt.Printf("Checking in guests took %v seconds\n", elapsed)
	count := 0
	for _, channel  := range containsChan {
		if !<-channel {
			count++
		}
	}
	return count
}

func main() {
	result := asyncMain()
	fmt.Println("Got this index from asyncMain:", result)
	intruders := asyncNumIntruders()
	fmt.Printf("There are %v intruders!\n", intruders)
}
