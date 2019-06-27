package testing;

import com.lowlevelsubmarine.yt_music_api.SearchResult;
import com.lowlevelsubmarine.yt_music_api.Track;
import com.lowlevelsubmarine.yt_music_api.YTMA;

import java.util.Scanner;

public class Testing {

    public static void main(String[] args) {
        YTMA ytma = new YTMA();
        while (true) {
            System.out.print("Request: ");
            Scanner scanner = new Scanner(System.in);
            String query = scanner.nextLine();
            System.out.println("Searching for: " + query + " ... ");
            SearchResult result = ytma.search(query);
            if (result.getSongs() != null) {
                for (int i = 0; i < result.getSongs().size(); i++) {
                    Track item = result.getSongs().get(i);
                    System.out.println(i + 1 + ". \"" + item.getTitle() + "\" by \"" + item.getArtist() + "\" #" + item.getId() + " s" + item.getDuration());
                }
            } else {
                System.out.println("No track results!");
            }
            System.out.println();
        }
    }

}
