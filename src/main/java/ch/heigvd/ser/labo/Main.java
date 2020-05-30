/*
 * Authors: Doran Kayoumi & Robin Cuénoud
 * File: Main.java
 * Date: 28.05.2020
 */

package ch.heigvd.ser.labo;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

    final static String GEO_JSON = "src/main/resources/countries.geojson";

    public static void main(String[] args) {

        try {
            FileReader reader = new FileReader(GEO_JSON);
            new KMLGenerator().generate(new GeoJsonParser(reader).parse());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
