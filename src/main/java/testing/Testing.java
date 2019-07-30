package testing;

import com.lowlevelsubmarine.yt_music_api.SearchResults;
import com.lowlevelsubmarine.yt_music_api.Song;
import com.lowlevelsubmarine.yt_music_api.VideoResults;
import com.lowlevelsubmarine.yt_music_api.YTMA;

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
            SearchResults result = ytma.search(query);
            for (Song song : result.fetchSongResults().getSongs()) {
                String feats = "";
                for (String string : song.getArtists()) {
                    feats += ", " + string;
                }
                String out = song.getMainArtist() + " / " + song.getTitle();
                if (!feats.isEmpty()) {
                    out += " [feat. " + feats.substring(2) + "]";
                }
                System.out.println(out);
            }
        }
    }

}
