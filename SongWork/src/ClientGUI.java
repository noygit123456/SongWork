import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

public class ClientGUI extends JFrame {
    private JTextField jtf = new JTextField();
    private JTextArea jta = new JTextArea();
    private Client client = new Client();

    private Song songToServer, songFromServer;

    public static void main(String[] args) {
        new ClientGUI();
    }

    public ClientGUI() {

        // create circle to read/write from/to server
        songToServer = new Song("", "", 0);
        songFromServer = new Song("", "", 0);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(new JLabel("Enter title of song"), BorderLayout.WEST);
        p.add(jtf, BorderLayout.CENTER);
        jtf.setHorizontalAlignment(JTextField.RIGHT);
        setLayout(new BorderLayout());
        add(p, BorderLayout.NORTH);
        add(new JScrollPane(jta), BorderLayout.CENTER);
        jtf.addActionListener(new ButtonListener());
        setTitle("Client");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String title = jtf.getText().trim();
            jtf.setText("");

            String artist = JOptionPane.showInputDialog("Enter artist:");
            String lengthStr = JOptionPane.showInputDialog("Enter length:");
            int length = Integer.parseInt(lengthStr);

            System.out.println("Client send title: " + title + ", artist: " + artist + ", length: " + length);
            songToServer = new Song(title, artist, length);
            client.writeToServer(songToServer);

            System.out.println("songToServer: " + songToServer);
            System.out.println("songie server: " + songToServer.getTitle());
            System.out.println("Artist sender: " + songToServer.getArtist());
            System.out.println("Length sender: " + songToServer.getLength());

            songFromServer = client.readFromServer();

            jta.append("Title is " + songToServer.getTitle() + "\n");
            jta.append("Artist is " + songToServer.getArtist() + "\n");
            jta.append("Length is " + songToServer.getLength() + "\n");
            jta.append("Title received from the server is " + songFromServer.getTitle() + '\n');
            jta.append("Artist received from the server is " + songFromServer.getArtist() + '\n');
            jta.append("Length received from the server is " + songFromServer.getLength() + '\n');

            // Retrieve the updated playlist from the server
            Playlist playlist = client.getPlaylist();
            jta.append("\nPlaylist:\n");
            for (Song song : playlist.getSongs()) {
                jta.append(song.getTitle() + " - " + song.getArtist() + " (" + song.getLength() + "s)\n");
            }



            // Play the playlist
            client.playPlaylist();
        }
    }
}