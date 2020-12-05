package bg.sofia.uni.fmi.mjt.tagger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TaggerThirdTest {
    private final String cityCountryList =
            "Aabenraa,Denmark"
                    + System.lineSeparator()
                    + "Aachen,Germany"
                    + System.lineSeparator()
                    + "Aalborg,Denmark"
                    + System.lineSeparator()
                    + "Aalen,Germany"
                    + System.lineSeparator()
                    + "Plovdiv,Bulgaria";

    private final String text =
            "duma1 duma2 duma3 PloVdiv duma4 Plovdiv"
                    + System.lineSeparator()
                    + "duma1 Aalen assd asd Aabenraa duma1 duma2 asdaddasads Aalen duma2 sasdadsads";

    private final String dummyText = "duma duma asd asd"
            + System.lineSeparator()
            + "asd asd asd";

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
    public void testTagCities() {
        try (var textReader = new StringReader(this.text)) {
            try (var output = new StringWriter()) {
                this.tagger.tagCities(textReader, output);

                String expectedLineOne = "duma1 duma2 duma3 <city country=\"Bulgaria\">PloVdiv</city> "
                        + "duma4 <city country=\"Bulgaria\">Plovdiv</city>";

                String expectedLineTwo = "duma1 <city country=\"Germany\">Aalen</city> assd asd "
                        + "<city country=\"Denmark\">Aabenraa</city> duma1 duma2 asdaddasads "
                        + "<city country=\"Germany\">Aalen</city> duma2 sasdadsads";

                String[] lines = output.toString().split(System.lineSeparator());

                assertEquals("expecting exact line one", expectedLineOne, lines[0]);
                assertEquals("expecting exact line one", expectedLineTwo, lines[1]);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void testGetNMostTaggedCitiesTopOne() {
        try (var textReader = new StringReader(this.text)) {
            try (var output = new StringWriter()) {
                this.tagger.tagCities(textReader, output);

                String mostTaggedCity = "Plovdiv";
                Collection<String> topNTagged = this.tagger.getNMostTaggedCities(1);
                Iterator<String> iterator = topNTagged.iterator();

                assertEquals("Expecting to be Plovdiv", mostTaggedCity, iterator.next());
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void testGetNMostTaggedCitiesTopTwo() {
        try (var textReader = new StringReader(this.text)) {
            try (var output = new StringWriter()) {
                this.tagger.tagCities(textReader, output);

                String mostTaggedCity = "Plovdiv";
                String secondMostTaggedCity = "Aalen";

                Collection<String> topNTagged = this.tagger.getNMostTaggedCities(2);
                Iterator<String> iterator = topNTagged.iterator();

                assertEquals("Expecting to be Plovdiv", mostTaggedCity, iterator.next());
                assertEquals("Expecting to be Aalen", secondMostTaggedCity, iterator.next());

            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void testGetAllTaggedCitiesWithSome() {
        try (var textReader = new StringReader(this.text)) {
            try (var output = new StringWriter()) {
                this.tagger.tagCities(textReader, output);

                String expectedCityOne = "Plovdiv";
                String expectedCityTwo = "Aalen";
                String expectedCityThree = "Aabenraa";

                Collection<String> foundCities = this.tagger.getAllTaggedCities();
                assertTrue("Expect containing Plovdiv", foundCities.contains(expectedCityOne));
                assertTrue("Expect containing Aalen", foundCities.contains(expectedCityTwo));
                assertTrue("Expect containing Aabenraa", foundCities.contains(expectedCityThree));

            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void testGetAllTaggedCitiesWithSomeExpectedSize() {
        try (var textReader = new StringReader(this.text)) {
            try (var output = new StringWriter()) {
                this.tagger.tagCities(textReader, output);

                Collection<String> foundCities = this.tagger.getAllTaggedCities();
                assertEquals("Expecting 3 cities", 3, foundCities.size());

            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void testGetAllTaggedCitiesWithoutAnyExpectedSizeZero() {
        try (var textReader = new StringReader(this.dummyText)) {
            try (var output = new StringWriter()) {
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
        try (var textReader = new StringReader(this.text)) {
            try (var output = new StringWriter()) {
                this.tagger.tagCities(textReader, output);
                assertEquals("Expecting 5 cities", 5, this.tagger.getAllTagsCount());
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void testGetAllTagsCountWithoutAnyCities() {
        try (var textReader = new StringReader(this.dummyText)) {
            try (var output = new StringWriter()) {
                this.tagger.tagCities(textReader, output);
                assertEquals("Expecting 0 cities", 0, this.tagger.getAllTagsCount());
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }
}
