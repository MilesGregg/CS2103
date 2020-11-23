import java.io.File;
import java.io.IOException;
import java.util.*;

public class IMDBGraphImpl implements IMDBGraph {
	private static Map<String, Actor> actors = new HashMap<>();
	private final static Map<String, Movie> movies = new HashMap<>();


	/**
	 * Stores the actors and actresses along with their movies into a graph data structure, and removes
	 * any actors or actresses who have 0 movies associated with them.
	 * @param actorsFilename the path to the file containing a list of actors (male) and their movies
	 * @param actressesFilename the path to the file containing a list of actresses (female) and their movies
	 */
	public IMDBGraphImpl (String actorsFilename, String actressesFilename) throws IOException {
		processActors(actorsFilename);
		processActors(actressesFilename);
		removeEmptyActors();
	}

	/**
	 * returns a collection of all the actors in the 2 files
	 * @return a colllection of actors
	 */
	@Override
	public Collection<? extends Node> getActors () {
		return actors.values();
	}

	/**
	 * returns a collection of all the movies in the 2 files
	 * @return a colllection of movies
	 */
	@Override
	public Collection<? extends Node> getMovies () {
		return movies.values();
	}

	/**
	 * returns the movie node given the movie's name
	 * @param name the name of the requested movie
	 * @return the movie node associated with the given name
	 */
	@Override
	public Node getMovie (String name) {
		return movies.get(name);
	}


	/**
	 * returns the actor node given the actor's name
	 * @param name the name of the requested actor
	 * @return the actor node associated with the given name
	 */
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
	protected static void processActors (String filename) throws IOException {
		final Scanner s = new Scanner(new File(filename), "ISO-8859-1");

		// Skip until:  Name...Titles
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (line.startsWith("Name") && line.contains("Titles")) {
				break;
			}
		}
		s.nextLine();  // read one more

		// Initializes an actor object
		String actorName = "";
		Actor actorNode = new Actor();

		while (s.hasNextLine()) {
			// Reads and stores the actor's name
			final String line = s.nextLine();
			actorNode.setName(actorName);

			if (line.contains("\t")) {  // new movie, either for an existing or a new actor

				int idxOfTab = line.indexOf("\t");
				if (idxOfTab > 0) {  // not at beginning of line => new actor
					actorName = line.substring(0, idxOfTab);

					// We have found an actor...
					// if the actor is new, create a new actor object and store it
					if(!actors.containsKey(actorName)) {
						actorNode = new Actor();
						actorNode.setName(actorName);
						actors.put(actorName, actorNode);
					}
					// If the actor is not new, set actorNode equal to the existing actor object
					else{
						actorNode = actors.get(actorName);
					}
				}
				if (!line.contains("(TV)") && !line.contains("\"")) {  // Only include bona-fide movies
					int lastIdxOfTab = line.lastIndexOf("\t");
					final String movieName = parseMovieName(line.substring(lastIdxOfTab + 1));
					// Get the existing movie with this name (or null if it does not exist)
					Movie movie = movies.get(movieName);
					// If the movie already exists, add the actor to the neighbors if it is not already there, and
					// add the movie as a neighbor for the actor, if it does not already exist
					if(movie != null){
						if(!movie.getNeighbors().contains(actorNode))
							movie.addNeighbor(actorNode);
						if(!actorNode.getNeighbors().contains(movie))
							actorNode.addNeighbor(movie);
					}
					// If the movie does not yet exist, make a new movie, add the actor as a neighbor, and make the
					// movie one of the actor's neighbors
					else{
						movie = new Movie();
						movie.name = movieName;
						if(!movie.getNeighbors().contains(actorNode))
							movie.addNeighbor(actorNode);
						if(!actorNode.getNeighbors().contains(movie))
							actorNode.addNeighbor(movie);
						movies.put(movieName, movie);
					}
				}
			}
		}
	}

	/**
	 * Removes all actors with 0 movies from the list of actors
	 */
	void removeEmptyActors(){
		// Initializes a new map of actors
		Map<String, Actor> newActors = new HashMap<>();
		// iterates through the existing actors, and only adds ones with at least 1 movie to the new map
		for(String a : actors.keySet())
			if(!actors.get(a).getNeighbors().isEmpty()) {
				newActors.put(a, actors.get(a));
			}
		// sets the actors map equal to the new map that was just created
		actors = newActors;
	}

	private static class Actor implements Node {
		List<Node> movies = new ArrayList<>();
		String name;

		public void setName(String name){
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Collection<? extends Node> getNeighbors() {
			return movies;
		}

		public void addNeighbor(Node movie){
			movies.add(movie);
		}
	}

	private static class Movie implements Node {
		List<Actor> actors = new ArrayList<>();
		String name;

		public void setName(String name){
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Collection<? extends Node> getNeighbors() {
			return actors;
		}

		public void addNeighbor(Actor actor){
			actors.add(actor);
		}
	}

}
