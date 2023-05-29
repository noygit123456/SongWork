import java.io.*;
import java.net.*;

class HandleAClient extends Thread {
    private Playlist playlist;
    private Socket socket;
    private ObjectInputStream inputFromClient;
    private ObjectOutputStream outputToClient;
    private Song songFromClient;

    public HandleAClient(Socket socket, Playlist playlist) {
        super();
        this.socket = socket;
        this.playlist = playlist;
        songFromClient = null;
    }

    @Override
    public void run() {
        try {
            // Create data input and output streams
            outputToClient = new ObjectOutputStream(socket.getOutputStream());
            inputFromClient = new ObjectInputStream(socket.getInputStream());

            // Continuously serve the client
            while (true) {

                Object request = null;
                try {
                    request = inputFromClient.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    continue;
                }

                // Receive song from the client
                if (request instanceof Song) {
                    songFromClient = (Song) request;

                    System.out.println("songie: " + songFromClient.getTitle());
                    System.out.println("Artist: " + songFromClient.getArtist());
                    System.out.println("Length: " + songFromClient.getLength());
                    System.out.println("songFromClient: " + songFromClient);

                    try {
                        playlist.addSong(songFromClient);

                        System.out.println("Song added successfully!");

                    } catch (Exception e) {
                        System.out.println("Failed to add the song: " + e.getMessage());
                    }
                    System.out.println("Server received song: " + songFromClient.getTitle());
                    // Process the received song
                    // Here calculate the area of the song or perform any other desired operations

                    // Send the modified song back to the client
                    outputToClient.writeObject(songFromClient);
                } else if (request instanceof String) {
                    String req = (String) request;
                    if (req.equals("GET_PLAYLIST")) {
                        // Send the playlist back to the client
                        outputToClient.writeObject(playlist);
                        outputToClient.reset();
                    }
                }

                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
