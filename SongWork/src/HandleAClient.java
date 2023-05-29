import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class HandleAClient implements Runnable {
    private Playlist playlist;
    private Socket socket;
    private ObjectInputStream inputFromClient;
    private ObjectOutputStream outputToClient;
    private Song songFromClient, songToClient;

    public HandleAClient(Socket socket, Playlist playlist) {
        this.socket = socket;
        this.playlist = playlist;
        songFromClient = new Song("", "", 0);
        songToClient = new Song("", "", 0);
    }

    public void run() {
        try {
            // Create data input and output streams
            outputToClient = new ObjectOutputStream(socket.getOutputStream());
            inputFromClient = new ObjectInputStream(socket.getInputStream());

            // Continuously serve the client
            while (true) {
                // Receive song from the client
                try {
                    songFromClient = (Song) inputFromClient.readObject();

                    System.out.println("songie: " + songFromClient.getTitle());
                    System.out.println("Artist: " + songFromClient.getArtist());
                    System.out.println("Length: " + songFromClient.getLength());
                    System.out.println("songFromClient: " + songFromClient);

                    playlist.addSong(songFromClient);
                    System.out.println("Song added successfully!");
                    outputToClient.writeObject(songFromClient);
                    outputToClient.reset();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Failed to add the song: " + e.getMessage());
                }

                System.out.println("Server received song: " + songFromClient.getTitle());

                // Process the received song
                // Here  calculate the area of the song or perform any other desired operations

                // Send the modified song back to the client
                outputToClient.writeObject(songFromClient);

                // Handle additional requests
                Object request = inputFromClient.readObject();
                if (request instanceof String) {
                    String req = (String) request;
                    if (req.equals("GET_PLAYLIST")) {
                        // Send the playlist back to the client
                        outputToClient.writeObject(playlist);
                        outputToClient.reset();
                    } else if (req.equals("PLAY_PLAYLIST")) {
                        playPlaylist();
                    }
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void playPlaylist() {
        List<Song> songs = playlist.getSongs();
        for (Song song : songs) {
            System.out.println("Playing song: " + song.getTitle());
        }
    }
}
