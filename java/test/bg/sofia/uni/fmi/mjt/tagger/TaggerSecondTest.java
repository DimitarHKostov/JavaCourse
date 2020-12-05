package bg.sofia.uni.fmi.mjt.tagger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TaggerSecondTest {
    private static final String inputCitiesMap = "world-cities.csv";
    private static final String inputText = "text.txt";
    private static final String outputText = "destination.txt";

    private static Path pathOfCities;
    private static Path pathOfInputText;
    private static Path pathOfOutputText;

    private Tagger tagger;

    private final String dummyText = "duma duma asd asd"
            + System.lineSeparator()
            + "asd asd asd";

    @BeforeClass
    public static void initialize() {
        pathOfCities = Path.of(inputCitiesMap);
        pathOfInputText = Path.of(inputText);
        pathOfOutputText = Path.of(outputText);
    }

    @Before
    public void setup() {
        try (var citiesReader = Files.newBufferedReader(pathOfCities)) {
            try (var textReader = Files.newBufferedReader(pathOfInputText)) {
                try (var output = Files.newBufferedWriter(pathOfOutputText)) {

                    this.tagger = new Tagger(citiesReader);
                    this.tagger.tagCities(textReader, output);

                } catch (IOException e) {
                    throw new RuntimeException();
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testTagCities() {
        String expectedLineOne = "duma1 duma2 duma3 <city country=\"Bulgaria\">Sofia</city>"
                + " asdasdasd <city country=\"Bulgaria\">PloVdiv</city> asd";

        String expectedLineTwo = "azadasdasd <city country=\"Bulgaria\">Sofia</city> asdjnasdnadsjk"
                + " <city country=\"Germany\">Aalen</city> <city country=\"Bulgaria\">Plovdiv</city> asdasd asd";

        String expectedLineThree = "adksdfmlkflk <city country=\"United Arab Emirates\">SharJAH</city>"
                + " ajkdjasdko joasdsd <city country=\"Bulgaria\">Plovdiv</city> joojasd j";

        try (var outputFileReader = Files.newBufferedReader(pathOfOutputText)) {
            String line = outputFileReader.readLine();
            assertEquals("expecting exact line one", expectedLineOne, line);

            line = outputFileReader.readLine();
            assertEquals("expecting exact line one", expectedLineTwo, line);

            line = outputFileReader.readLine();
            assertEquals("expecting exact line one", expectedLineThree, line);

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testGetNMostTaggedCitiesTopOne() {
        String mostTaggedCity = "Plovdiv";
        Collection<String> topNTagged = this.tagger.getNMostTaggedCities(1);
        Iterator<String> iterator = topNTagged.iterator();

        assertEquals("Expecting to be Plovdiv", mostTaggedCity, iterator.next());
    }

    @Test
    public void testGetNMostTaggedCitiesTopTwo() {
        String mostTaggedCity = "Plovdiv";
        String secondMostTaggedCity = "Sofia";

        Collection<String> topNTagged = this.tagger.getNMostTaggedCities(2);
        Iterator<String> iterator = topNTagged.iterator();

        assertEquals("Expecting to be Plovdiv", mostTaggedCity, iterator.next());
        assertEquals("Expecting to be Sofia", secondMostTaggedCity, iterator.next());
    }

    @Test
    public void testGetAllTaggedCitiesWithSome() {
        String expectedCityOne = "Plovdiv";
        String expectedCityTwo = "Sofia";
        String expectedCityThree = "Aalen";
        String expectedCityFour = "Sharjah";

        Collection<String> foundCities = this.tagger.getAllTaggedCities();
        assertTrue("Expect containing Plovdiv", foundCities.contains(expectedCityOne));
        assertTrue("Expect containing Sofia", foundCities.contains(expectedCityTwo));
        assertTrue("Expect containing Aalen", foundCities.contains(expectedCityThree));
        assertTrue("Expect containing Sharjah", foundCities.contains(expectedCityFour));
    }

    @Test
    public void testGetAllTaggedCitiesWithSomeExpectedSize() {
        Collection<String> foundCities = this.tagger.getAllTaggedCities();
        assertEquals("Expecting 4 cities", 4, foundCities.size());
    }

    @Test
    public void testGetAllTaggedCitiesWithoutAnyExpectedSizeZero() {
        try (var textReader = new StringReader(this.dummyText)) {
            try (var output = Files.newBufferedWriter(pathOfOutputText)) {
                this.tagger.tagCities(textReader, output);

                Collection<String> foundCities = this.tagger.getAllTaggedCities();
                assertEquals("Expecting size 0", 0, foundCities.size());

            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void testGetAllTagsCountWithSomeCities() {
        assertEquals("Expecting 7 cities", 7, this.tagger.getAllTagsCount());
    }

    @Test
    public void testGetAllTagsCountWithoutAnyCities() {
        try (var textReader = new StringReader(this.dummyText)) {
            try (var output = Files.newBufferedWriter(pathOfOutputText)) {
                this.tagger.tagCities(textReader, output);
                assertEquals("Expecting 0 cities", 0, this.tagger.getAllTagsCount());
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }
}
