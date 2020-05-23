package ch.heigvd.ser.labo;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonToKml {

    final static String GEO_JSON = "src/main/resources/countries.geojson";

    public static void main(String[] args) {

        try {
            FileReader reader = new FileReader(GEO_JSON);

            GeoParser parser = new GeoParser(reader);

            System.out.println(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Hello SER");
    }
}
