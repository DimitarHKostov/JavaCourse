package bg.sofia.uni.fmi.mjt.tagger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Tagger {
    private final Map<String, String> citiesMap;
    private final Set<String> cities;
    private final Map<String, Long> taggedCities;
    private final Set<String> lowerCaseCharacters;
    private final Set<String> upperCaseCharacters;
    private int totalTaggedCities;

    /**
     * Creates a new instance of Tagger for a given list of city/country pairs
     *
     * @param citiesReader a java.io.Reader input stream containing list of cities and countries
     *                     in the specified CSV format
     */
    public Tagger(Reader citiesReader) {
        this.citiesMap = new LinkedHashMap<>();
        this.taggedCities = new HashMap<>();
        this.lowerCaseCharacters = new HashSet<>();
        this.upperCaseCharacters = new HashSet<>();
        this.createCitiesMap(citiesReader);
        this.cities = this.citiesMap.keySet();
        this.totalTaggedCities = 0;
    }

    private void createCitiesMap(Reader reader) {
        try (var cities = new BufferedReader(reader)) {
            String currentLine;

            while ((currentLine = cities.readLine()) != null) {
                String[] currentPair = currentLine.split(",");
                citiesMap.put(currentPair[0], currentPair[1]);
                this.updateCharSet(currentPair[0]);
            }

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void updateCharSet(String city) {
        String currentLetter;

        for (int i = 0; i < city.length(); i++) {
            currentLetter = String.valueOf(city.charAt(i));
            this.lowerCaseCharacters.add(currentLetter.toLowerCase());
            this.upperCaseCharacters.add(currentLetter.toUpperCase());
        }
    }

    /**
     * Processes an input stream of a text file, tags any cities and outputs result
     * to a text output stream.
     *
     * @param text   a java.io.Reader input stream containing text to be processed
     * @param output a java.io.Writer output stream containing the result of tagging
     */

    private boolean isCharOfSet(char letter) {
        String letterAsString = String.valueOf(letter);
        return this.upperCaseCharacters.contains(letterAsString) || this.lowerCaseCharacters.contains(letterAsString);
    }

    private int findEndIndex(int startIndex, String line) {
        int endIndex = startIndex + 1;
        int lineLength = line.length();

        while (startIndex + 1 < lineLength && this.isCharOfSet(line.charAt(startIndex + 1))) {
            startIndex++;
            endIndex++;
        }

        return endIndex;
    }

    private String formatCity(String city) {
        int cityLength = city.length();

        if (cityLength == 1) {
            return city.toUpperCase();
        }

        String firstLetter = String.valueOf(city.charAt(0)).toUpperCase();
        StringBuilder otherLetters = new StringBuilder();

        for (int i = 1; i < city.length(); i++) {
            otherLetters.append(String.valueOf(city.charAt(i)).toLowerCase());
        }

        return firstLetter + otherLetters.toString();
    }

    private boolean isCity(String city) {
        return this.cities.contains(this.formatCity(city));
    }

    private void updateTaggedCities(String city) {
        if (!this.taggedCities.containsKey(city)) {
            this.taggedCities.put(city, (long) 1);
        } else {
            long previousTags = this.taggedCities.get(city);
            this.taggedCities.replace(city, previousTags + 1);
        }
    }

    private String reformLine(String line, Set<String> lineCities) {
        String replacement;

        for (String currentCity : lineCities) {
            String formattedCity = this.formatCity(currentCity);

            replacement = "<city country=\""
                    + this.citiesMap.get(formattedCity)
                    + "\">"
                    + currentCity + "</city>";

            line = line.replaceAll(currentCity, replacement);
            this.updateTaggedCities(formattedCity);
        }

        return line;
    }

    private String processLine(String line) {
        Set<String> lineCities = new HashSet<>();
        int lineLength = line.length();
        int increment;
        int lineIndex = 0;

        while (lineIndex < lineLength) {
            String city;
            int endIndex;
            increment = 1;

            if (this.isCharOfSet(line.charAt(lineIndex))) {
                endIndex = this.findEndIndex(lineIndex, line);
                city = line.substring(lineIndex, endIndex);
                increment = city.length();
                if (this.isCity(city)) {
                    lineCities.add(city);
                    this.totalTaggedCities++;
                }
            }

            lineIndex += increment;
        }

        return this.reformLine(line, lineCities);
    }

    public void tagCities(Reader text, Writer output) {
        try (var textReader = new BufferedReader(text)) {
            if (this.taggedCities.size() != 0) {
                this.taggedCities.clear();
                this.totalTaggedCities = 0;
            }

            String line;
            boolean firstLine = true;

            while (true) {
                line = textReader.readLine();
                if (line != null) {
                    if (firstLine) {
                        firstLine = false;
                    } else {
                        output.write(System.lineSeparator());
                    }
                    output.write(this.processLine(line));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Returns a collection the top @n most tagged cities' unique names
     * from the last tagCities() invocation. Note that if a particular city has been tagged
     * more than once in the text, just one occurrence of its name should appear in the result.
     * If @n exceeds the total number of cities tagged, return as many as available
     * If tagCities() has not been invoked at all, return an empty collection.
     *
     * @param n the maximum number of top tagged cities to return
     * @return a collection the top @n most tagged cities' unique names
     * from the last tagCities() invocation.
     */

    private int sortingCityFunction(Map.Entry<String, Long> objectOne, Map.Entry<String, Long> objectTwo) {
        return objectTwo.getValue().compareTo(objectOne.getValue());
    }

    private Collection<String> topNTaggedCities(int n) {
        Queue<Map.Entry<String, Long>> queue = new PriorityQueue<>(this::sortingCityFunction);
        queue.addAll(this.taggedCities.entrySet());

        List<String> topN = new ArrayList<>();
        Iterator<Map.Entry<String, Long>> iterator = queue.iterator();

        int addedCities = 0;
        while (addedCities++ < n && iterator.hasNext()) {
            topN.add(iterator.next().getKey());
        }
        return topN;
    }

    public Collection<String> getNMostTaggedCities(int n) {
        return n > this.taggedCities.size()
                ? this.topNTaggedCities(this.taggedCities.size()) : this.topNTaggedCities(n);
    }

    /**
     * Returns a collection of all tagged cities' unique names
     * from the last tagCities() invocation. Note that if a particular city has been tagged
     * more than once in the text, just one occurrence of its name should appear in the result.
     * If tagCities() has not been invoked at all, return an empty collection.
     *
     * @return a collection of all tagged cities' unique names
     * from the last tagCities() invocation.
     */
    public Collection<String> getAllTaggedCities() {
        return this.taggedCities.keySet();
    }

    /**
     * Returns the total number of tagged cities in the input text
     * from the last tagCities() invocation
     * In case a particular city has been taged in several occurences, all must be counted.
     * If tagCities() has not been invoked at all, return 0.
     *
     * @return the total number of tagged cities in the input text
     */
    public long getAllTagsCount() {
        return this.totalTaggedCities;
    }

}