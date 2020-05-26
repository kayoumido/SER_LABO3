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

        // `MultiPolygon` composed of an extra layer of JSONArray' compared to a `Polygon`,
        // so to get the number of coordinate for a "wall" we need to go down an extra layer
        // i.e to unify everything, we add the extra layer missing in a Polygon
        //
        // note: We've decided to put this here because it's an internal behaviour of this class
        //
        // here's an example of MultiPolygon structure:
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
        JSONArray tmp = new JSONArray();
        tmp.add(coordinates);
        this.coordinates = geometry.equals("MultiPolygon") ? coordinates : tmp;
    }

    public String getName() {
        return name;
    }

    public String getGeometry() {
        return geometry;
    }

    public int getPolygonCount() {
        return this.coordinates.size();
    }

    public String getStringCoord(int i) {
        StringBuilder sb = new StringBuilder();

        JSONArray coord = (JSONArray) ((JSONArray) this.coordinates.get(i)).get(0);

        for (Object c : coord) {
            double coord1 = (double) ((JSONArray) c).get(0);
            double coord2 = (double) ((JSONArray) c).get(1);

            sb.append(String.format("%f,%f ", coord1, coord2));
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("(%s) %s\n", this.isoA3, this.name));

        for (Object coordinate : this.coordinates) {
            JSONArray coord = (JSONArray) ((JSONArray) coordinate).get(0);
            sb.append(String.format("\t - %d coordinates\n", coord.size()));
        }

        return sb.toString();
    }
}
