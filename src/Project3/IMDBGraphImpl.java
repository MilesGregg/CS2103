import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class IMDBGraphImpl implements IMDBGraph {
	private static Map<String, Actor> actors = new HashMap<>();
	private final static Map<String, Movie> movies = new HashMap<>();
	// Implement me
	public IMDBGraphImpl (String actorsFilename, String actressesFilename) throws IOException {
		processActors(actorsFilename);
		processActors(actressesFilename);
		fix();
	}

	// Implement me
	@Override
	public Collection<? extends Node> getActors () {
		return actors.values();
	}

	// Implement me
	@Override
	public Collection<? extends Node> getMovies () {
		return movies.values();
	}

	// Implement me
	@Override
	public Node getMovie (String name) {
		return movies.get(name);
	}


	// Implement me
	@Override
	public Node getActor (String name) {
		return actors.get(name);
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
			if (line.startsWith("Name") && line.contains("Titles")) {
				break;
			}
		}
		s.nextLine();  // read one more

		String actorName = "";
		Actor actorNode = new Actor();

		while (s.hasNextLine()) {
			final String line = s.nextLine();

			actorNode.setName(actorName);

			//System.out.println(line);
			if (line.contains("\t")) {  // new movie, either for an existing or a new actor

				int idxOfTab = line.indexOf("\t");
				if (idxOfTab > 0) {  // not at beginning of line => new actor
					actorName = line.substring(0, idxOfTab);

					// We have found a new actor...
					//System.out.println(actorName);
					if(!actors.containsKey(actorName)) {
						actorNode = new Actor();
						actorNode.setName(actorName);
						actors.put(actorName, actorNode);
					}
					else{
						actorNode = actors.get(actorName);
					}
					//System.out.println(actorName);
				}
				if (!line.contains("(TV)") && !line.contains("\"")) {  // Only include bona-fide movies
					int lastIdxOfTab = line.lastIndexOf("\t");
					final String movieName = parseMovieName(line.substring(lastIdxOfTab + 1));
					Movie movie = movies.get(movieName);
					if(movie != null){
						if(!movie.getNeighbors().contains(actorNode))
							movie.addNeighbor(actorNode);
						if(!actorNode.getNeighbors().contains(movie))
							actorNode.addNeighbor(movie);
						//System.out.println(actorNode.getName());
					}
					else{
						movie = new Movie();
						movie.name = movieName;
						if(!movie.getNeighbors().contains(actorNode))
							movie.addNeighbor(actorNode);
						if(!actorNode.getNeighbors().contains(movie))
							actorNode.addNeighbor(movie);
						movies.put(movieName, movie);
					}
					// We have found a new movie
					//System.out.println(movieName);
				}
			}
		}
	}

	void fix(){
		Map<String, Actor> newActors = new HashMap<>();
		for(String a : actors.keySet())
			if(!actors.get(a).getNeighbors().isEmpty()) {
				newActors.put(a, actors.get(a));
			}
		actors = newActors;
	}
}
