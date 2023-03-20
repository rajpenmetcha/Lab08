import java.io.*;
import java.util.*;
/**
 * Homework 8 -- Music Recommender
 *
 * This program takes user input such as fav artist
 * genre, bpm and popularity
 * The program gives a recommended song based off
 * those criteria.
 *
 * @author Raj Penmetcha, lab sec
 *
 * @version March 8, 2023
 *
 */
public class MusicRecommender {

    private final String musicListFileName;
    private final ArrayList<Music> music;


    public MusicRecommender(String musicListFileName) throws FileNotFoundException, MusicFileFormatException {
        this.musicListFileName = musicListFileName;
        this.music = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(musicListFileName))) {
            while (scanner.hasNextLine()) {
                String musicInfoLine = scanner.nextLine();
                try {
                    Music newMusic = parseMusic(musicInfoLine);
                    music.add(newMusic);
                } catch (MusicFileFormatException e) {
                    throw e;
                }

            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (MusicFileFormatException e) {
            e.printStackTrace();
        }

    }

    private static Music parseMusic(String musicInfoLine) throws MusicFileFormatException {
        String[] infoArray = musicInfoLine.split(" ");

        if (infoArray.length != 5) {
            throw new MusicFileFormatException("One of the lines of the music list file is malformed!");
        }

        String track = infoArray[0].replace("_", " ").trim();
        String artist = infoArray[1].replace("_", " ").trim();
        String genre = infoArray[2].replace("_", " ").trim();

        int bpm;
        int popularity;
        try {
            bpm = Integer.parseInt(infoArray[3].trim());
            popularity = Integer.parseInt(infoArray[4].trim());
        } catch (NumberFormatException e) {
            throw new MusicFileFormatException("One of the lines of the music list file is malformed!");
        }
        return new Music(track, artist, genre, bpm, popularity);

    }

    //fix this
    public ArrayList<Music> searchArtists(MusicProfile musicProfile) throws NoRecommendationException {
        ArrayList<Music> recommendedMusic = new ArrayList<>();
        String preferredArtist = musicProfile.getPreferredArtist();
        boolean foundMusicByPreferredArtist = false;


        for (Music m : music) {

            if (m.getArtist().toLowerCase().contains(preferredArtist)) {
                recommendedMusic.add(m);
                foundMusicByPreferredArtist = true;
            }
        }

        if (!foundMusicByPreferredArtist) {
            throw new NoRecommendationException("No music by your preferred artist is in the list!");
        }

        for (Music m : recommendedMusic) {
            m.setPopularity(m.getPopularity() + 1);
        }

        return recommendedMusic;

    }

    // genreBasedRecommendation works
    public Music genreBasedRecommendation(MusicProfile musicProfile) throws NoRecommendationException {

        String preferredGenre = musicProfile.getPreferredGenre();
        ArrayList<Music> genreMatchedMusic = new ArrayList<>();

        for (Music m : music) {
            if (m.getGenre().toLowerCase().contains(preferredGenre.toLowerCase())) {
                genreMatchedMusic.add(m);
            }
        }

        if (genreMatchedMusic.isEmpty()) {
            throw new NoRecommendationException("There was no music with your preferred genre!");
        }

        Music recommendedMusic;
        if (musicProfile.isLikePopular()) {
            recommendedMusic = Collections.max(genreMatchedMusic, Comparator.comparing(Music::getPopularity));
        } else {
            recommendedMusic = Collections.min(genreMatchedMusic, Comparator.comparing(Music::getPopularity));
        }

        recommendedMusic.setPopularity(recommendedMusic.getPopularity() + 1);
        return recommendedMusic;

    }

    //BPMBasedRecommendation works
    public Music BPMBasedRecommendation(MusicProfile musicProfile) throws NoRecommendationException {
        int preferredbpm = musicProfile.getPreferredBPM();
        boolean likePopular = musicProfile.isLikePopular();

        Music recommendedMusic = null;
        int minDelta = Integer.MAX_VALUE;

        // Iterate through the music list to find the closest song with the specified BPM
        for (Music m : music) {
            int delta = Math.abs(m.getBPM() - preferredbpm);
            if (delta <= 20 && delta < minDelta) {
                minDelta = delta;
                recommendedMusic = m;
            } else if (delta == minDelta && m.getPopularity() != recommendedMusic.getPopularity()) {
                if ((likePopular && m.getPopularity() > recommendedMusic.getPopularity()) ||
                        (!likePopular && m.getPopularity() < recommendedMusic.getPopularity())) {
                    recommendedMusic = m;
                }
            }
        }

        // Throw an exception if no song was found within 20 BPM
        if (recommendedMusic == null) {
            throw new NoRecommendationException("There was no music with your preferred BPM!");
        }

        // Increase the popularity of the recommended song
        recommendedMusic.setPopularity(recommendedMusic.getPopularity() + 1);

        return recommendedMusic;

    }

    //getMostPopularMusic works
    public Music getMostPopularMusic() {
        Music mostPopularMusic = null;
        int maxPopularity = Integer.MIN_VALUE;


        for (Music m : music) {
            if (m.getPopularity() > maxPopularity) {
                maxPopularity = m.getPopularity();
                mostPopularMusic = m;
            }
        }

        // Increase the popularity of the most popular song
        mostPopularMusic.setPopularity(mostPopularMusic.getPopularity() + 1);

        return mostPopularMusic;

    }

    public void saveMusicList() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(musicListFileName));
            for (Music m : music) {
                String track = m.getTrack().replaceAll(" ", "_");
                String artist = m.getArtist().replaceAll(" ", "_");
                String genre = m.getGenre().replaceAll(" ", "_");
                String line = track + " " + artist
                        + " " + genre + " "
                        + m.getBPM() + " " + m.getPopularity();
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
