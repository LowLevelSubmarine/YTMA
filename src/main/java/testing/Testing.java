package testing;

import com.lowlevelsubmarine.yt_music_api.SearchResult;
import com.lowlevelsubmarine.yt_music_api.Song;
import com.lowlevelsubmarine.yt_music_api.YTMA;

import java.util.LinkedList;
import java.util.Scanner;

public class Testing {

    public static void main(String[] args) {
        YTMA ytma = new YTMA();
        ytma.prepare();
        while (true) {
            System.out.print("Request: ");
            Scanner scanner = new Scanner(System.in);
            String query = scanner.nextLine();
            System.out.println("Searching for: " + query + " ... ");
            SearchResult result = ytma.search(query);
            //System.out.println(result.getSongs().getFirst().getTitle() + " #" + result.getSongs().getFirst().getId());
            LinkedList<Song> songs = result.fetchSongResults().getSongs();
            for (Song song : songs) {
                System.out.println(song.getArtist() +  " / " + song.getTitle() + " #" + song.getId());
            }
        }
    }

}
