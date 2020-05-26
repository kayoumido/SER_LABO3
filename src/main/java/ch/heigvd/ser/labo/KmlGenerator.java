package ch.heigvd.ser.labo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public class KmlGenerator {

    public void generate(ArrayList<Country> countries) {
        // get an instance of the `DocumentBuilderFactory`
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            // create builder
            final DocumentBuilder builder = factory.newDocumentBuilder();
            // create document
            final Document document = builder.newDocument();

            // create root node and append it to the document
            final Element root = document.createElement("kml");
            root.setAttribute("xmlns", "http://www.opengis.net/kml/2.2");
            root.setAttribute("xmlns:gx", "http://www.google.com/kml/ext/2.2");
            document.appendChild(root);

            final Element kDocument = document.createElement("Document");
            root.appendChild(kDocument);

            // Set the styles
            final Element style = document.createElement("PolyStyle");
            style.setAttribute("id", "labStyle");
            kDocument.appendChild(style);
            final Element fill = document.createElement("fill");
            fill.appendChild(document.createTextNode("0"));
            style.appendChild(fill);


            for (Country country : countries) {
                // for the moment skip MultiPolygon
                final Element placemark = document.createElement("Placemark");
                kDocument.appendChild(placemark);

                final Element name = document.createElement("name");
                name.appendChild(document.createTextNode(country.getName()));
                placemark.appendChild(name);

                final Element styleUrl = document.createElement("styleUrl");
                styleUrl.appendChild(document.createTextNode("#labStyle"));
                placemark.appendChild(styleUrl);

                placemark.appendChild(
                    country.getGeometry().equals("MultiPolygon") ?
                        generateMultiPolygon(document, country) :
                        generatePolygon(document, country, 0)
                );
            }

            // Finalising document
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer               = transformerFactory.newTransformer();
            final DOMSource source                      = new DOMSource(document);
            final StreamResult result                   = new StreamResult(new File("countries.kml"));

            document.setXmlStandalone(false);
            document.setXmlVersion("1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public Element generateMultiPolygon(Document document, Country country) {
        Element multiGeometry = document.createElement("MultiGeometry");

        for (int i = 0; i < country.getPolygonCount(); ++i)
            multiGeometry.appendChild(generatePolygon(document, country, i));

        return multiGeometry;
    }

    public Element generatePolygon(Document document, Country country, int i) {
        Element polygon = document.createElement("Polygon");

        Element outerBoundary = document.createElement("outerBoundaryIs");
        polygon.appendChild(outerBoundary);

        Element linearRing = document.createElement("LinearRing");
        outerBoundary.appendChild(linearRing);

        Element coordinates = document.createElement("coordinates");
        coordinates.appendChild(document.createTextNode(country.getStringCoord(i)));
        linearRing.appendChild(coordinates);

        return polygon;
    }
}