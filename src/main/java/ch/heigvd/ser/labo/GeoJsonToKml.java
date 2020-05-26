package ch.heigvd.ser.labo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class GeoJsonToKml {

    final static String GEO_JSON = "src/main/resources/countries.geojson";

    public static void main(String[] args) {

        try {
            FileReader reader = new FileReader(GEO_JSON);

            new KmlGenerator().generate(new GeoParser(reader).parse());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
