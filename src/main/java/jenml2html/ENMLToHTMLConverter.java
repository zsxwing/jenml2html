package jenml2html;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ENMLToHTMLConverter {

	private ResourceConverter resourceConverter;

	public ENMLToHTMLConverter(ResourceConverter resourceConverter) {
		this.resourceConverter = resourceConverter;
	}

	private Document stringToDoc(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		return documentBuilder.parse(xml);
	}

	private String docToString(Document doc) {
		return "";
	}

	/**
	 * Convert a <a
	 * href="http://dev.evernote.com/documentation/cloud/chapters/ENML.php"
	 * >ENML</a>-format string to a HTML code.
	 * 
	 * @param enml
	 *            The ENML-format string.
	 * @return The corresponding HTML code.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public String convert(String enml) throws ParserConfigurationException,
			SAXException, IOException {
		return docToString(convert(stringToDoc(enml)));
	}

	private Document convert(Document enml) {
		Document html = null;
		return html;
	}

	public static String convert(String enml,
			ResourceConverter resourceConverter)
			throws ParserConfigurationException, SAXException, IOException {
		return new ENMLToHTMLConverter(resourceConverter).convert(enml);
	}
}
