import java.io.*;
import java.net.*;

class Client {
    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;

    public Client() {
        try {
            socket = new Socket("localhost", 8000);
            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToServer(Song song) {
        try {
            toServer.writeObject(song);
            toServer.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Song readFromServer() {
        try {
            Object response = fromServer.readObject();
            if (response instanceof Song) {
                return (Song) response;
            } else {
                System.out.println("Unexpected response received from the server");
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Playlist getPlaylist() {
        try {
            toServer.writeObject("GET_PLAYLIST");
            Object response = fromServer.readObject();
            System.out.println(response);
            if (response instanceof Playlist) {
                return (Playlist) response;
            } else if (response instanceof String) {
                System.out.println("Received a playlist title: " + response);
            } else {
                System.out.println("Unexpected response received from the server");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
