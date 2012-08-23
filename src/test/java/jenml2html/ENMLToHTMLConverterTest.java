package jenml2html;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.evernote.edam.type.Data;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Resource;

public class ENMLToHTMLConverterTest {

	@Before
	public void setup() {

	}

	@Test
	public void testTidyNoteContent() throws IOException, JDOMException {
		Note note = mock(Note.class);
		when(note.getContent())
				.thenReturn(
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
								+ "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">\n"
								+ "<en-note><h1>Hello, world</h1></en-note>");
		ENMLToHTMLConverter enml2html = new ENMLToHTMLConverter(
				new ResourceConverterStub());
		Assert.assertEquals("<div><h1>Hello, world</h1></div>",
				enml2html.convert(note));
	}

	@Test
	public void testConvertEnMediaElement() throws IOException, JDOMException {
		Note note = mock(Note.class);
		when(note.getContent())
				.thenReturn(
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
								+ "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">\n"
								+ "<en-note><h1>Hello, world</h1><div>"
								+ "<en-media width=\"640\" height=\"480\" type=\"image/jpeg\" hash=\"f03c1c2d96bc67eda02968c8b5af9008\"/>"
								+ "</div></en-note>");

		List<Resource> resources = new ArrayList<Resource>();
		Resource resource = mock(Resource.class);
		resources.add(resource);
		when(note.getResources()).thenReturn(resources);

		Data data = mock(Data.class);
		when(resource.getData()).thenReturn(data);
		when(resource.getGuid()).thenReturn("f03c1c2d96bc67eda02968c8b5af9008");

		ENMLToHTMLConverter enml2html = new ENMLToHTMLConverter(
				new ImageConverterStub());
		Assert.assertEquals(
				"<div><h1>Hello, world</h1><div>"
						+ "<img width=\"640\" height=\"480\" type=\"image/jpeg\" src=\"stub?id=3\" />"
						+ "</div></div>", enml2html.convert(note));
	}
}
