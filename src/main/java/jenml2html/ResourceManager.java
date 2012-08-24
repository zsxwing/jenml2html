package jenml2html;

import java.util.HashMap;
import java.util.Map;

import com.evernote.edam.type.Note;
import com.evernote.edam.type.Resource;

public class ResourceManager {

	private Map<String, Resource> hashResourceMap;

	public ResourceManager(Note note) {
		hashResourceMap = new HashMap<String, Resource>();
		if (note.getResources() != null) {
			for (Resource resource : note.getResources()) {
				hashResourceMap.put(Digest.md5(resource.getData().getBody()),
						resource);
			}
		}
	}

	public Resource getResourceByHash(String hash) {
		return hashResourceMap.get(hash.toUpperCase());
	}

}
