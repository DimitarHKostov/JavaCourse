package bg.sofia.uni.fmi.mjt.spotify;

import org.junit.Test;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SpotifyExplorerTest {
    private final String fileSimulator =
            """
                    id,art,name,year,pop,dur_ms,tempo,loud,vale,acoust,dance,energy,live,speech,explicit
                    id0,['Pesho';'Ivan'],songName0,1921,4,831667,80.954,-20.096,0.0494,0.982,0.279,0.211,0.665,0.0366,0
                    id1,['Pesho],songName1,1981,5,831667,80.954,-20.096,0.0594,0.982,0.279,0.211,0.665,0.0366,1
                    id2,['Ivan'],songName2,1985,6,381667,80.954,-20.096,0.0694,0.982,0.279,0.211,0.665,0.0366,0
                    id3,['Gosho'],songName3,1991,6,251667,80.954,-30.096,0.0794,0.982,0.279,0.211,0.665,0.0366,1
                    id4,['Gosho';'Pesho'],songName4,1995,7,83167,80.954,-20.096,0.0894,0.982,0.279,0.211,0.665,0.0366,1
                    id5,['Gosho'],songName5,1991,8,31667,80.954,-20.096,0.0994,0.982,0.279,0.211,0.665,0.0366,0
                    """;

    private SpotifyExplorer explorer;

    private final String emptyFileSimulator = "";

    private final SpotifyTrack track0 = new SpotifyTrack("id0", Set.of("Pesho", "Ivan"), "songName0", 1921,
            4, 831667, 80.954, -20.096, 0.0494, 0.982,
            0.279, 0.211, 0.665, 0.0366, false);

    private final SpotifyTrack track1 = new SpotifyTrack("id1", Set.of("Pesho"), "songName1", 1981,
            5, 831667, 80.954, -20.096, 0.0594, 0.982,
            0.279, 0.211, 0.665, 0.0366, true);

    private final SpotifyTrack track2 = new SpotifyTrack("id2", Set.of("Ivan"), "songName2", 1985,
            6, 381667, 80.954, -20.096, 0.0694, 0.982,
            0.279, 0.211, 0.665, 0.0366, false);

    private final SpotifyTrack track3 = new SpotifyTrack("id3", Set.of("Gosho"), "songName3", 1991,
            6, 251667, 80.954, -30.096, 0.0794, 0.982,
            0.279, 0.211, 0.665, 0.0366, true);

    private final SpotifyTrack track4 = new SpotifyTrack("id4", Set.of("Gosho", "Pesho"), "songName4", 1995,
            7, 83167, 80.954, -20.096, 0.0894, 0.982,
            0.279, 0.211, 0.665, 0.0366, true);

    private final SpotifyTrack track5 = new SpotifyTrack("id5", Set.of("Gosho"), "songName5", 1991,
            8, 31667, 80.954, -20.096, 0.0994, 0.982,
            0.279, 0.211, 0.665, 0.0366, false);

    @Test
    public void testGetAllSpotifyTracksWhenThereAreSome() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            Collection<SpotifyTrack> actual = this.explorer.getAllSpotifyTracks();

            Set<SpotifyTrack> expected = Set.of(track0, track1, track2, track3, track4, track5);

            assertTrue(actual.containsAll(expected) && expected.containsAll(actual));
        }
    }

    @Test
    public void testGetAllSpotifyTracksWithoutAny() {
        try (var reader = new StringReader(emptyFileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            Collection<SpotifyTrack> actual = this.explorer.getAllSpotifyTracks();

            assertEquals(0, actual.size());
        }
    }

    @Test
    public void testGetExplicitSpotifyTracksExactTracks() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            Collection<SpotifyTrack> actual = this.explorer.getExplicitSpotifyTracks();

            Set<SpotifyTrack> expected = Set.of(track1, track3, track4);

            assertTrue(actual.containsAll(expected) && expected.containsAll(actual));
        }
    }

    @Test
    public void testGetExplicitSpotifyTracksWrongTracks() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            Collection<SpotifyTrack> actual = this.explorer.getExplicitSpotifyTracks();

            Set<SpotifyTrack> expected = Set.of(track0, track2, track5);

            assertFalse(actual.containsAll(expected) && expected.containsAll(actual));
        }
    }

    @Test
    public void testGroupSpotifyTracksByYearExactTracks() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            Map<Integer, Set<SpotifyTrack>> expected =
                    Map.of(1921, Set.of(track0),
                            1981, Set.of(track1),
                            1985, Set.of(track2),
                            1991, Set.of(track3, track5),
                            1995, Set.of(track4));

            Map<Integer, Set<SpotifyTrack>> actual = this.explorer.groupSpotifyTracksByYear();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGroupSpotifyTracksByYearDummyYears() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            Map<Integer, Set<SpotifyTrack>> expected =
                    Map.of(1, Set.of(track0),
                            2, Set.of(track1),
                            3, Set.of(track2),
                            4, Set.of(track3, track5),
                            5, Set.of(track4));

            Map<Integer, Set<SpotifyTrack>> actual = this.explorer.groupSpotifyTracksByYear();
            assertNotEquals(expected, actual);
        }
    }

    @Test
    public void testGetArtistActiveYearsRightOnes() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            int goshoActiveYears = this.explorer.getArtistActiveYears("Gosho");
            int peshoActiveYears = this.explorer.getArtistActiveYears("Pesho");
            int ivanActiveYears = this.explorer.getArtistActiveYears("Ivan");

            assertEquals(5, goshoActiveYears);
            assertEquals(75, peshoActiveYears);
            assertEquals(65, ivanActiveYears);
        }
    }

    @Test
    public void testGetArtistActiveYearsWrongOnes() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            int goshoActiveYears = this.explorer.getArtistActiveYears("Gosho");
            int peshoActiveYears = this.explorer.getArtistActiveYears("Pesho");
            int ivanActiveYears = this.explorer.getArtistActiveYears("Ivan");

            assertNotEquals(-5, goshoActiveYears);
            assertNotEquals(-75, peshoActiveYears);
            assertNotEquals(-31, ivanActiveYears);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTopNHighestValenceTracksFromThe80sNegativeNumber() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            this.explorer.getTopNHighestValenceTracksFromThe80s(-1);
        }
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sFirstOne() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            List<SpotifyTrack> actual = this.explorer.getTopNHighestValenceTracksFromThe80s(1);

            List<SpotifyTrack> expected = List.of(track2);

            assertTrue(actual.containsAll(expected) && expected.containsAll(actual));
        }
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sFirstTwo() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            List<SpotifyTrack> actual = this.explorer.getTopNHighestValenceTracksFromThe80s(2);

            List<SpotifyTrack> expected = List.of(track1, track2);

            assertTrue(actual.containsAll(expected) && expected.containsAll(actual));
        }
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sWrongOnes() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            List<SpotifyTrack> actual = this.explorer.getTopNHighestValenceTracksFromThe80s(2);

            List<SpotifyTrack> expected = List.of(track0, track3, track4, track5);

            assertFalse(actual.containsAll(expected) && expected.containsAll(actual));
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetMostPopularTrackFromThe90sWhenThereIsNone() {
        try (var reader = new StringReader(emptyFileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            this.explorer.getMostPopularTrackFromThe90s();
        }
    }

    @Test
    public void testGetMostPopularTrackFromThe90sExactOne() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            SpotifyTrack actual = this.explorer.getMostPopularTrackFromThe90s();

            assertEquals(actual, track5);
        }
    }

    @Test
    public void testGetMostPopularTrackFromThe90sWrongOne() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            SpotifyTrack actual = this.explorer.getMostPopularTrackFromThe90s();

            assertNotEquals(actual, track4);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNumberOfLongerTracksBeforeYearNegativeMinutes() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            this.explorer.getNumberOfLongerTracksBeforeYear(-1, 2020);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNumberOfLongerTracksBeforeYearNegativeYear() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            this.explorer.getNumberOfLongerTracksBeforeYear(100, -1);
        }
    }

    @Test()
    public void testGetNumberOfLongerTracksBeforeYearExactNumber() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            long numOfTracks = this.explorer.getNumberOfLongerTracksBeforeYear(2, 1970);

            assertEquals(1, numOfTracks);
        }
    }

    @Test()
    public void testGetNumberOfLongerTracksBeforeYearWrongNumber() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            long numOfTracks = this.explorer.getNumberOfLongerTracksBeforeYear(100, 1970);

            assertNotEquals(2, numOfTracks);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTheLoudestTrackInYearNegativeYear() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            this.explorer.getTheLoudestTrackInYear(-1);
        }
    }

    @Test()
    public void testGetTheLoudestTrackInYearExactTrack() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            SpotifyTrack loudestTrackInYear = this.explorer.getTheLoudestTrackInYear(1991).get();

            assertEquals(track5, loudestTrackInYear);
        }
    }

    @Test()
    public void testGetTheLoudestTrackInYearWrongTrack() {
        try (var reader = new StringReader(fileSimulator)) {
            this.explorer = new SpotifyExplorer(reader);

            SpotifyTrack loudestTrackInYear = this.explorer.getTheLoudestTrackInYear(1991).get();

            assertNotEquals(track1, loudestTrackInYear);
        }
    }
}
