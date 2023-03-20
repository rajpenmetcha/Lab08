import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Interactive menu where the user can use the functionalities of the MusicRecommender
 *
 * @version 2022-07-25
 * @author Purdue CS
 */
public class MusicRecommenderMenu {

    /**
     * Interactive menu where the user can use the functionalities of the MusicRecommender
     * @param args No argument is needed for this main method
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        MusicRecommender musicRecommender = null;
        while (true) {
            try {
                System.out.println("Welcome! What's the name of the file containing the music list?");
                String musicListFileName = scan.nextLine();
                musicRecommender = new MusicRecommender(musicListFileName);
                break;
            } catch (FileNotFoundException e) {
                System.out.println("The file does not exist! Please enter a valid file name!");
            } catch (MusicFileFormatException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        System.out.println("What's the name of the user?");
        String name = scan.nextLine();
        System.out.println("Who's your favorite artist?");
        String artist = scan.nextLine();
        System.out.println("What's your favorite genre?");
        String genre = scan.nextLine();
        System.out.println("What's your preferred BPM?");
        int BPM = scan.nextInt();
        System.out.println("Do you like popular music? (Y/N)");
        scan.nextLine();
        boolean likePopular = (scan.nextLine().equalsIgnoreCase("Y"));
        MusicProfile musicProfile = new MusicProfile(name, artist, genre, BPM, likePopular);

        System.out.printf("Hi %s!\n", musicProfile.getName());
        int option = -1;
        while (option != 5) {
            System.out.println("1. Find songs of my favorite artists\n" +
                    "2. Get a recommendation based on my preferred genre\n" +
                    "3. Get a recommendation based on my preferred BPM\n" +
                    "4. Show me the most popular song\n" +
                    "5. Exit");
            boolean wrongCondition;
            do {
                option = scan.nextInt();
                wrongCondition = ((option != 1) && (option != 2) && (option != 3) && (option != 4) && (option != 5));
                if (wrongCondition) System.out.println("Please enter a valid option!");
            } while (wrongCondition);
            switch (option) {
                case 1 -> {
                    try {
                        ArrayList<Music> searchResult = musicRecommender.searchArtists(musicProfile);
                        System.out.printf("Listing songs by %s:\n", musicProfile.getPreferredArtist());
                        for (Music music : searchResult) {
                            System.out.println(music.toString());
                        }
                    } catch (NoRecommendationException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    try {
                        System.out.println(musicRecommender.genreBasedRecommendation(musicProfile).toString());
                    } catch (NoRecommendationException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    try {
                        System.out.println(musicRecommender.BPMBasedRecommendation(musicProfile).toString());
                    } catch (NoRecommendationException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 4 -> System.out.println(musicRecommender.getMostPopularMusic().toString());
                case 5 -> { }
            }
        }
        musicRecommender.saveMusicList();
        System.out.println("Thanks for using the music recommender!");
    }
}
