package jenml2html;

import java.util.List;

import com.evernote.edam.type.Resource;

public interface ResourceConverter {

	public static class Attribute {
		private String name;
		private String value;

		public Attribute(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	public String convert(Resource resource, List<Attribute> attributes);
}
