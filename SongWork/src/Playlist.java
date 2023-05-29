import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Playlist implements Iterable<Song>, Serializable {
    private List<Song> songs ;
    private final int MAX_SONGS = 10;
    private Semaphore semaphore = new Semaphore(1);

    public Playlist() {
        songs = new ArrayList<>();
    }
    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) throws Exception {
        if (songs.size() >= MAX_SONGS) {
            throw new Exception("Playlist is full");
        }
        try {
            semaphore.acquire();
            songs.add(song);
        } finally {
            semaphore.release();
        }
    }
    public void removeSong(Song song) {
        try {
            semaphore.acquire();
            songs.remove(song);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void removeSongByName(String name) {
        try {
            semaphore.acquire();
            for (int i = 0; i < songs.size(); i++) {
                if (songs.get(i).getTitle().equals(name)) {
                    songs.remove(i);
                    return;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void removeSongByIndex(int index) {
        try {
            semaphore.acquire();
            if (index >= 0 && index < songs.size()) {
                songs.remove(index);
            }
        } catch (InterruptedException e) {
            e.printStackTrace()  ;
        } finally {
            semaphore.release();
        }
    }

    public void play() {
        try {
            semaphore.acquire();
            for (final Song song : songs) {
                System.out.println("Playing " + song.getTitle() + " by " + song.getArtist());

                // Create a new thread for each song
                Thread songThread = new Thread(new Runnable() {

                    public void run() {
                        try {
                            Thread.sleep(song.getLength() * 1000); // Sleep for song length in milliseconds
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // Start the song thread
                songThread.start();

                // Wait for the song thread to finish
                try {
                    songThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Pause for 2 seconds between songs
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void shuffle() {
        try {
            semaphore.acquire();
            Collections.shuffle(songs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public List<Song> search(String query) {
        try {
            semaphore.acquire();
            List<Song> results = new ArrayList<Song>();
            for (Song song : songs) {
                if (song.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        song.getArtist().toLowerCase().contains(query.toLowerCase())) {
                    results.add(song);
                }
            }
            return results;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            semaphore.release();
        }
    }
    // the total number of songs in the playlist

    public int getSongCount() {
        try {
            semaphore.acquire();
            return songs.size();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        } finally {
            semaphore.release();
        }
    }

    // retrieves a song from the playlist based on its index

    public Song getSongByIndex(int index) {
        try {
            semaphore.acquire();
            if (index >= 0 && index < songs.size()) {
                return songs.get(index);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
        return null;
    }
    public void changeSong(Song oldSong, Song newSong) {
        try {
            semaphore.acquire();
            int index = songs.indexOf(oldSong);
            if (index != -1) {
                songs.set(index, newSong);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    @Override
    public Iterator<Song> iterator() {
        return songs.iterator();
    }

}