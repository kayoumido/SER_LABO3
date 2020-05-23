package ch.heigvd.ser.labo;

import org.json.simple.JSONArray;

public class Country {
    private String name;
    private String isoA3;
    private String geometry;
    private JSONArray coordinates;

    public Country(String name, String isoA3, String geometry, JSONArray coordinates) {
        this.name = name;
        this.isoA3 = isoA3;
        this.geometry = geometry;
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("(%s) %s\n", this.isoA3, this.name));

        for (Object coordinate : this.coordinates) {
            // `MultiPolygon` composed of an extra layer of JSONArray' compared to a `Polygon`,
            // so to get the number of coordinate for a "wall" we need to go down an extra layer
            // i.e we need to do a `.get(0)`.
            //
            // here's an example of MultiiPolygon structure:
            //  JSONArray [
            //      JSONArray [             <-- a `Polygon` doesn't have this layer
            //          JSONArray [
            //              JSONArray [],   <-- coordinates our found here
            //              JSONArray []
            //          ]
            //      ],
            //      JSONArray [
            //          JSONArray [
            //              ...
            //          ]
            //      ]
            //      ...
            //  ]
            JSONArray coord = this.geometry.equals("MultiPolygon") ?
                    ((JSONArray) ((JSONArray) coordinate).get(0)) : ((JSONArray) coordinate);

            sb.append(String.format("\t - %d coordinates\n", coord.size()));
        }

        return sb.toString();
    }
}
