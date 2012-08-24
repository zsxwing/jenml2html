package jenml2html;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.evernote.edam.type.Note;

public class ENMLToHTMLConverter {

	private ResourceConverter resourceConverter;

	private static String DOCTYPE_declaration = "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">";
	private static String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	public ENMLToHTMLConverter(ResourceConverter resourceConverter) {
		this.resourceConverter = resourceConverter;
	}

	/**
	 * Remove the doctype declaration for speeding up the parser.
	 * 
	 * @param content
	 * @return
	 */
	private String tidyNoteContent(String content) {
		return content.replace(DOCTYPE_declaration, "").replaceAll("&nbsp",
				"&#160");
	}

	/**
	 * Remove the xml declaration since we only need the html snippet.
	 * 
	 * @param content
	 * @return
	 */
	private String convertDocumentToHTMLSnippet(Document document) {
		String xmlString = new XMLOutputter(Format.getCompactFormat())
				.outputString(document);
		int beginIndex = XML_DECLARATION.length();
		if (xmlString.charAt(beginIndex) == '\r'
				|| xmlString.charAt(beginIndex) == '\n') {
			beginIndex++;
			if (xmlString.charAt(beginIndex) == '\r'
					|| xmlString.charAt(beginIndex) == '\n') {
				beginIndex++;
			}
		}
		int endIndex = xmlString.length() - 1;
		if (xmlString.charAt(endIndex) == '\r'
				|| xmlString.charAt(endIndex) == '\n') {
			endIndex--;
			if (xmlString.charAt(endIndex) == '\r'
					|| xmlString.charAt(endIndex) == '\n') {
				endIndex--;
			}
		}
		String htmlSnippet = xmlString.substring(beginIndex,
				Math.max(beginIndex, endIndex + 1));
		return htmlSnippet;
	}

	public String convert(Note note) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(tidyNoteContent(note
				.getContent())));
		doc.setDocType(null);
		doc.setBaseURI(null);
		doc.getRootElement().setName("div");

		convertElement(note, new ResourceManager(note), doc.getRootElement());
		return convertDocumentToHTMLSnippet(doc);
	}

	private void convertElement(Note note, ResourceManager resourceManager,
			Element element) {
		if (element.getName().startsWith("en-media")) {
			List<ResourceConverter.Attribute> convertedAttributes = elementToAttributeList(element);
			String tagName = resourceConverter.convert(resourceManager
					.getResourceByHash(element.getAttributeValue("hash")),
					convertedAttributes);
			if (tagName == null) {
				element.getParent().removeContent(element);
				return;
			}
			element.setName(tagName);
			element.removeContent();
			element.getAttributes().clear();
			for (ResourceConverter.Attribute attribute : convertedAttributes) {
				element.setAttribute(attribute.getName(), attribute.getValue());
			}
			return;
		}

		if (element.getName().startsWith("en-todo")) {
			// TODO en-todo
			return;
		}

		if (element.getName().startsWith("en-crypt")) {
			// TODO en-crypt
			return;
		}

		for (Element child : element.getChildren()) {
			convertElement(note, resourceManager, child);
		}
	}

	private List<ResourceConverter.Attribute> elementToAttributeList(
			Element element) {
		List<ResourceConverter.Attribute> convertedAttributes = new ArrayList<ResourceConverter.Attribute>();
		for (Attribute attribute : element.getAttributes()) {
			if ("hash".equals(attribute.getName())) {
				continue;
			}
			convertedAttributes.add(new ResourceConverter.Attribute(attribute
					.getName(), attribute.getValue()));
		}
		return convertedAttributes;
	}
}
