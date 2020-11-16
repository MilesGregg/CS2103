package Project3;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class IMDBGraphImpl implements IMDBGraph {
	// Implement me
	public IMDBGraphImpl (String actorsFilename, String actressesFilename) throws IOException {
	}

	// Implement me
	public Collection<? extends Node> getActors () {
		return null;
	}

	// Implement me
	public Collection<? extends Node> getMovies () {
		return null;
	}

	// Implement me
	public Node getMovie (String name) {
		return null;
	}

	// Implement me
	public Node getActor (String name) {
		return null;
	}

	/**
	 * Parses the movie title from a line containing a movie
	 * @param str line containing a movie
	 * @return the movie title
	 */
	protected static String parseMovieName (String str) {
		int idx1 = str.indexOf("(");
		int idx2 = str.indexOf(")", idx1 + 1);
		return str.substring(0, idx2 + 1);
	}

	/**
	 * Scans an IMDB file for its actors/actresses and movies
	 * @param filename the movie file to parse
	 */
	protected static void processActors (String filename /* add other parameters as desired */) throws IOException {
		final Scanner s = new Scanner(new File(filename), "ISO-8859-1");

		// Skip until:  Name...Titles
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (line.startsWith("Name") && line.indexOf("Titles") >= 0) {
				break;
			}
		}
		s.nextLine();  // read one more

		String actorName = null;
		while (s.hasNextLine()) {
			final String line = s.nextLine();

			//System.out.println(line);
			if (line.indexOf("\t") >= 0) {  // new movie, either for an existing or a new actor
				int idxOfTab = line.indexOf("\t");
				if (idxOfTab > 0) {  // not at beginning of line => new actor
					actorName = line.substring(0, idxOfTab);

					// We have found a new actor...
					//System.out.println(actorName);
				}
				if (line.indexOf("(TV)") < 0 && line.indexOf("\"") < 0) {  // Only include bona-fide movies
					int lastIdxOfTab = line.lastIndexOf("\t");
					final String movieName = parseMovieName(line.substring(lastIdxOfTab + 1));

					// We have found a new movie
					//System.out.println(movieName);
				}
			}
		}
	}
}
