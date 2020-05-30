/*
 * Authors: Doran Kayoumi & Robin Cuénoud
 * File: GeoJsonParser.java
 * Date: 28.05.2020
 */

package ch.heigvd.ser.labo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class GeoJsonParser {
    private Reader file;
    private ArrayList<Country> result = new ArrayList<>();

    public GeoJsonParser(Reader data) {
        this.file = data;
    }

    /**
     * Parse the GEOJson file given to the class constructor and extract the countries found in it
     *
     * @return ArrayList of countries parsed from the GEOJson file
     */
    public ArrayList<Country> parse() {
        try {
            JSONParser parser = new JSONParser();
            // get the contents of the GeoJson file as a JSON object
            JSONObject contents = (JSONObject) parser.parse(file);

            // converte each feature (i.e. countries) into a country object
            ((JSONArray) contents.get("features")).forEach(jsonCountry->generateCountry((JSONObject) jsonCountry));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Convert a JSONObject into a `Country` object and add it to the result attribute
     *
     * @param jsonCountry - JSONObject to convert
     */
    private void generateCountry(JSONObject jsonCountry) {
        JSONObject properties = (JSONObject) jsonCountry.get("properties");
        JSONObject geometry = (JSONObject) jsonCountry.get("geometry");

        Country country = new Country(
            properties.get("ADMIN").toString(),
            properties.get("ISO_A3").toString(),
            geometry.get("type").toString(),
            (JSONArray) geometry.get("coordinates")
        );

        System.out.println(country);

        result.add(country);
    }
}
