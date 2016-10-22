package workshop.java.intermediate.collectionsprocessing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import workshop.java.intermediate.boilerplatefree.ExampleMovies;
import workshop.java.intermediate.boilerplatefree.Movie;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

public class ImmutableCollectorsTest {

    @Test
    void collectingAndThenExample() {
        Map<String, Movie> moviesById = ExampleMovies.allMovies()
                .stream()
                .map(Movie.MovieBuilder::build)
                .collect(collectingAndThen(
                        toMap(Movie::getImdbID, Function.identity()),
                        Collections::unmodifiableMap));

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                moviesById::clear
        );
    }

    @Test
    void toUnmodifiableMapExample() {
        Map<String, Movie> moviesById = ExampleMovies.allMovies()
                .stream()
                .map(Movie.MovieBuilder::build)
                .collect(toUnmodifiableMap(
                        Movie::getImdbID, Function.identity()));

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                moviesById::clear
        );
    }

    @Benchmark
    public void measureFactoryMethod() {
        List<String> list = List.of("1", "2", "3");
        Assertions.assertEquals(3, list.size());
    }

    @Benchmark
    public void measureUnmodifiable() {
        List<String> list = Arrays.asList("1", "2", "3");
        Assertions.assertEquals(3, list.size());
    }
}
