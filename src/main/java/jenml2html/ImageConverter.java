package jenml2html;

import java.util.List;

import com.evernote.edam.type.Resource;

public abstract class ImageConverter implements ResourceConverter {

	@Override
	public String convert(Resource resource, List<Attribute> attributes) {
		String type = getAttributeValue(attributes, "type");
		if (type != null && type.startsWith("image/")) {
			String src = upload(resource);
			attributes.add(new Attribute("src", src));
			return "img";
		}
		return null;
	}

	protected abstract String upload(Resource resource);

	private String getAttributeValue(List<Attribute> attributes, String name) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals(name)) {
				return attribute.getValue();
			}
		}
		return null;
	}

}
