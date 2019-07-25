package testing;

import com.lowlevelsubmarine.yt_music_api.SearchResults;
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
            System.out.println(result.getSongs().getFirst().getTitle());
        }
    }

}
