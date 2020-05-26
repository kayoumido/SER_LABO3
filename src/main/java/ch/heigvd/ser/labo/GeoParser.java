package ch.heigvd.ser.labo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class GeoParser {
    private Reader file;

    public GeoParser(Reader data) {
        this.file = data;
    }

    public ArrayList<Country> parse() {
        ArrayList<Country> result = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            // get the contents of the GeoJson file as a JSON array
            JSONObject contents = (JSONObject) parser.parse(file);

            JSONArray countries = (JSONArray) contents.get("features");
            for (Object c : countries) {
                JSONObject geoInfo = (JSONObject) c;
                JSONObject properties = (JSONObject) geoInfo.get("properties");
                JSONObject geometry = (JSONObject) geoInfo.get("geometry");

                Country country = new Country(
                    properties.get("ADMIN").toString(),
                    properties.get("ISO_A3").toString(),
                    geometry.get("type").toString(),
                    (JSONArray) geometry.get("coordinates")
                );

                System.out.println(country);

                result.add(country);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}
