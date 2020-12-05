package bg.sofia.uni.fmi.mjt.tagger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TaggerFourthTest {
    private final String cityCountryList =
            """
                    Aabenraa,Denmark
                    Aachen,Germany
                    Aalborg,Denmark
                    Aalen,Germany
                    Plovdiv,Bulgaria
                    """;

    private final String text =
            """
                    duma1 duma2 duma3 PloVdiv duma4 Plovdiv
                    duma1 Aalen assd asd Aabenraa duma1 duma2 asdaddasads Aalen duma2 sasdadsads Plovdiv
                    """;

    private final String dummyText =
            """
                    duma duma asd asd
                    asd asd asd
                    """;

    private Tagger tagger;
    private BufferedReader citiesMap;

    @Before
    public void initialize() {
        this.citiesMap = new BufferedReader(new StringReader(this.cityCountryList));
        this.tagger = new Tagger(citiesMap);
    }

    @After
    public void clear() {
        try {
            this.citiesMap.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testTagCities() throws IOException {
        try (var textReader = new StringReader(this.text); var output = new StringWriter();) {
            this.tagger.tagCities(textReader, output);

            String expectedLineOne = "duma1 duma2 duma3 <city country=\"Bulgaria\">PloVdiv</city> "
                    + "duma4 <city country=\"Bulgaria\">Plovdiv</city>";

            String expectedLineTwo = "duma1 <city country=\"Germany\">Aalen</city> assd asd "
                    + "<city country=\"Denmark\">Aabenraa</city> duma1 duma2 asdaddasads "
                    + "<city country=\"Germany\">Aalen</city> duma2 sasdadsads "
                    + "<city country=\"Bulgaria\">Plovdiv</city>";

            String[] lines = output.toString().split(System.lineSeparator());

            assertEquals("expecting exact line one", expectedLineOne, lines[0]);
            assertEquals("expecting exact line one", expectedLineTwo, lines[1]);
        }
    }

    @Test
    public void testGetNMostTaggedCitiesTopOne() throws IOException {
        try (var textReader = new StringReader(this.text); var output = new StringWriter();) {
            this.tagger.tagCities(textReader, output);

            Set<String> expected = Set.of("Plovdiv");

            Collection<String> topNTagged = this.tagger.getNMostTaggedCities(1);

            assertTrue(topNTagged.containsAll(expected) && expected.containsAll(topNTagged));
        }
    }

    @Test
    public void testGetNMostTaggedCitiesTopTwo() throws IOException {
        try (var textReader = new StringReader(this.text); var output = new StringWriter();) {
            this.tagger.tagCities(textReader, output);
            Set<String> expected = Set.of("Plovdiv", "Aalen");

            Collection<String> topNTagged = this.tagger.getNMostTaggedCities(2);

            assertTrue(topNTagged.containsAll(expected) && expected.containsAll(topNTagged));
        }
    }

    @Test
    public void testGetAllTaggedCitiesWithSome() throws IOException {
        try (var textReader = new StringReader(this.text); var output = new StringWriter();) {
            this.tagger.tagCities(textReader, output);
            Set<String> expected = Set.of("Plovdiv", "Aalen", "Aabenraa");

            Collection<String> foundCities = this.tagger.getAllTaggedCities();

            assertTrue(foundCities.containsAll(expected) && expected.containsAll(foundCities));
        }
    }

    @Test
    public void testGetAllTaggedCitiesWithSomeExpectedSize() throws IOException {
        try (var textReader = new StringReader(this.text); var output = new StringWriter();) {
            this.tagger.tagCities(textReader, output);

            Collection<String> foundCities = this.tagger.getAllTaggedCities();

            assertEquals("Expecting 3 cities", 3, foundCities.size());
        }
    }

    @Test
    public void testGetAllTaggedCitiesWithoutAnyExpectedSizeZero() throws IOException {
        try (var textReader = new StringReader(this.dummyText); var output = new StringWriter();) {
            this.tagger.tagCities(textReader, output);

            Collection<String> foundCities = this.tagger.getAllTaggedCities();

            assertEquals("Expecting size 0", 0, foundCities.size());
        }
    }

    @Test
    public void testGetAllTagsCountWithSomeCities() throws IOException {
        try (var textReader = new StringReader(this.text); var output = new StringWriter();) {
            this.tagger.tagCities(textReader, output);

            assertEquals("Expecting 6 cities", 6, this.tagger.getAllTagsCount());
        }
    }

    @Test
    public void testGetAllTagsCountWithoutAnyCities() throws IOException {
        try (var textReader = new StringReader(this.dummyText); var output = new StringWriter();) {
            this.tagger.tagCities(textReader, output);

            assertEquals("Expecting 0 cities", 0, this.tagger.getAllTagsCount());
        }
    }
}
