package ch.heigvd.ser.labo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.*;
import org.jdom2.output.*;


public class KmlGenerator {

    private static final String outputFile = "src/main/output/countries.kml";
    private static final String styleId = "labStyle";

    /**
     * Generate a kml file based on an ArrayList of countries
     *
     * @param countries - ArrayList of countries containing the data to generate the KML
     */
    public void generate(ArrayList<Country> countries) {
        try {

            Element kml = new Element("kml", "http://www.opengis.net/kml/2.2");
            Document document = new Document(kml);

            Element kDocument = new Element("Document", kml.getNamespace());

            // Generate the style tags to respect the style
            // of the lab
            Element style = new Element("Style", kml.getNamespace());
            style.setAttribute(new Attribute("id", styleId));
            style.addContent(
                new Element("PolyStyle", kml.getNamespace()).addContent(
                    new Element("fill", kml.getNamespace()).setText("0")
                )
            );

            kDocument.addContent(style);

            // generate a Placemark for every country found in the given ArrayList
            // Note: the Placemark has a slightly different structure depending on if the
            //  country if a `Polygon` or a `MultiPolygon`. A `MultiPolygon` is a wrapper
            //  for multiple polygons (hence it's name).
            //  In KML terms, I `MultiPolygon` is actually a `MultiGeometry`
            for (Country country : countries) {
                Element placemark = new Element("Placemark", kml.getNamespace());

                placemark.addContent(new Element("name", kml.getNamespace()).setText(country.getName()));
                placemark.addContent(new Element("styleUrl", kml.getNamespace()).setText("#" + styleId));

                // check the geometry of the current country to generate the correct structure
                // and add it to the placemark
                placemark.addContent(
                    country.getGeometry().equals("MultiPolygon") ?
                        generateMultiPolygon(country, kml.getNamespace()) :
                        generatePolygon(country, 0, kml.getNamespace())
                );

                kDocument.addContent(placemark);
        }

            document.getRootElement().addContent(kDocument);

            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.setFormat(Format.getPrettyFormat());
            xmlOutputter.output(document, new FileWriter(outputFile));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate a `MultiPolygon` based on the data of a Country object
     *
     * @param country   - Country object to get the data from
     * @param namespace - namespace of the kml document
     *
     * @return generated MultiPolygon
     */
    private Element generateMultiPolygon(Country country, Namespace namespace) {
        Element multiGeometry = new Element("MultiGeometry", namespace);

        // loop through all the Polygons of the given country
        for (int i = 0; i < country.getPolygonCount(); ++i)
            multiGeometry.addContent(generatePolygon(country, i, namespace));

        return multiGeometry;
    }

    /**
     * Generate a `Polygon` based on the data of a Country object
     *
     * @param country   - Country object to get the data from
     * @param i         - index of the wanted polygon
     * @param namespace - namespace of the kml document
     *
     * @return generated Polgon
     */
    private Element generatePolygon(Country country, int i, Namespace namespace) {
        return new Element("Polygon", namespace).addContent(
            new Element("outerBoundaryIs", namespace).addContent(
              new Element("LinearRing", namespace).addContent(
                  new Element("coordinates", namespace).setText(country.getStringCoord(i))
              )
            )
        );
    }
}