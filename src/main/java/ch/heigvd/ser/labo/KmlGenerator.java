package ch.heigvd.ser.labo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.*;
import org.jdom2.output.*;


public class KmlGenerator {
    private static final String xmlFilePath = "src/main/output/countries.kml";
    private static final String styleId = "labStyle";

    /**
     * Generate a kml file based on an ArrayList of countries
     * @param countries to convert to kml
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

            for (Country country : countries) {
                Element placemark = new Element("Placemark", kml.getNamespace());

                placemark.addContent(new Element("name", kml.getNamespace()).setText(country.getName()));
                placemark.addContent(new Element("styleUrl", kml.getNamespace()).setText("#" + styleId));

                placemark.addContent(
                    country.getGeometry().equals("MultiPolygon") ?
                        generateMultiPolygon(country, kml) :
                        generatePolygon(country, 0, kml)
                );

                kDocument.addContent(placemark);
        }

            document.getRootElement().addContent(kDocument);

            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.setFormat(Format.getPrettyFormat());
            xmlOutputter.output(document, new FileWriter(xmlFilePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Element generateMultiPolygon(Country country, Element kml) {
        Element multiGeometry = new Element("MultiGeometry", kml.getNamespace());

        for (int i = 0; i < country.getPolygonCount(); ++i)
            multiGeometry.addContent(generatePolygon(country, i, kml));

        return multiGeometry;
    }

    public Element generatePolygon(Country country, int i, Element kml) {
        return new Element("Polygon", kml.getNamespace()).addContent(
            new Element("outerBoundaryIs", kml.getNamespace()).addContent(
              new Element("LinearRing", kml.getNamespace()).addContent(
                  new Element("coordinates", kml.getNamespace()).setText(country.getStringCoord(i))
              )
            )
        );
    }
}