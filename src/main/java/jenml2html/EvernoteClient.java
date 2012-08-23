package jenml2html;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.THttpClient;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Resource;
import com.evernote.edam.userstore.Constants;
import com.evernote.edam.userstore.UserStore;

public class EvernoteClient {

	private static final String EVERNOTE_HOST = "www.evernote.com";
	private static final String USER_STORE_URL = "https://" + EVERNOTE_HOST
			+ "/edam/user";
	private static final String USER_AGENT = "Evernote/zsxwing (Java) "
			+ Constants.EDAM_VERSION_MAJOR + "." + Constants.EDAM_VERSION_MINOR;

	private String authToken;
	private NoteStore.Client noteStore;

	/**
	 * @param developerToken
	 *            Apple the <a
	 *            href="https://sandbox.evernote.com/api/DeveloperToken.action"
	 *            >developerToken</a> from <a
	 *            href="http://www.evernote.com">Evernote</a>.
	 * @throws TException
	 * @throws EDAMUserException
	 * @throws EDAMSystemException
	 */
	public EvernoteClient(String developerToken) throws TException,
			EDAMUserException, EDAMSystemException {
		this.authToken = developerToken;
		this.noteStore = createNoteStore(this.authToken, createUserStore());
	}

	NoteStore.Client getNoteStore() {
		return noteStore;
	}

	private UserStore.Client createUserStore() throws TException {
		THttpClient userStoreTrans = new THttpClient(USER_STORE_URL);
		userStoreTrans.setCustomHeader("User-Agent", USER_AGENT);
		TBinaryProtocol userStoreProt = new TBinaryProtocol(userStoreTrans);
		UserStore.Client userStore = new UserStore.Client(userStoreProt,
				userStoreProt);
		if (!userStore.checkVersion(USER_AGENT,
				com.evernote.edam.userstore.Constants.EDAM_VERSION_MAJOR,
				com.evernote.edam.userstore.Constants.EDAM_VERSION_MINOR)) {
			throw new TException("Incomatible Evernote client protocol version");
		}
		return userStore;
	}

	private NoteStore.Client createNoteStore(String autoToken,
			UserStore.Client userStore) throws EDAMUserException,
			EDAMSystemException, TException {
		String notestoreUrl = userStore.getNoteStoreUrl(autoToken);
		THttpClient noteStoreTrans = new THttpClient(notestoreUrl);
		noteStoreTrans.setCustomHeader("User-Agent", USER_AGENT);
		TBinaryProtocol noteStoreProt = new TBinaryProtocol(noteStoreTrans);
		return new NoteStore.Client(noteStoreProt, noteStoreProt);
	}

	public List<Note> listNotes() throws EDAMUserException,
			EDAMSystemException, EDAMNotFoundException, TException {
		List<Note> notes = new ArrayList<Note>();

		NoteFilter filter = new NoteFilter();
		filter.setOrder(NoteSortOrder.UPDATED.getValue());
		filter.setAscending(false);

		List<Note> partialNotes = null;
		int offset = 0;
		while (true) {
			NoteList noteList = noteStore.findNotes(authToken, filter, offset,
					com.evernote.edam.limits.Constants.EDAM_USER_NOTES_MAX);
			partialNotes = noteList.getNotes();
			if (partialNotes == null || partialNotes.isEmpty()) {
				break;
			}
			notes.addAll(partialNotes);
			offset = notes.size();
		}
		return notes;
	}

	public Note getNote(Note note) throws EDAMUserException,
			EDAMSystemException, EDAMNotFoundException, TException {
		return getNote(note.getGuid());
	}

	public Resource getResourceByHash(Note note, String hash)
			throws EDAMUserException, EDAMSystemException,
			EDAMNotFoundException, TException {
		return noteStore.getResourceByHash(authToken, note.getGuid(),
				hash.getBytes(), false, false, false);
	}

	public Note getNote(String guid) throws EDAMUserException,
			EDAMSystemException, EDAMNotFoundException, TException {
		return noteStore.getNote(authToken, guid, true, true, false, false);
	}
}
