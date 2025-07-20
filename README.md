# Holiday Agency

A small app that quotes the lowest travel cost for holiday journeys.

## Quickstart

To run the application:

1. Ensure run.sh is executable `chmod +x run.sh`
2. Ensure `flights.csv` and `journeys.csv` exists in the current working directory
3. Run `./run.sh` to build and run the application in a single step
4. Journey suggestions are written to file `quotes.csv` in the current working directory

## Continuous Integration (CI)

This project uses GitHub Actions to perform the following checks on every commit or Pull Request:

- Build of the application
- Running of all unit tests
- Checking of code formatting, using Spotless Gradle Plugin
- Dependency graph submission, for Dependabot alerts for insecure dependencies

See `.github/workflows/gradle.yml` for the CI script in question.

## Design

The application is written using plain Java. For the sake of time constraints, I did not use Spring - I could have made `HolidayAgencyApplication` implement the `org.springframework.boot.CommandLineRunner` interface.

Still, the application is structured in a Spring-like way, with use of constructor-based dependency injection. See, for example, the four lines of the `HolidayAgencyApplication`'s constructor for the "hand crafting" that Spring's CDI would have done for us. 

I used A* (pronounced "A-star"), a graph traversal and pathfinding algorithm. Given a weighted graph, a source node and a goal node, the algorithm finds the shortest path (with respect to the given weights) from source to goal. Is guaranteed to find the shortest path.  

While A* is sufficient for the given inputs, real-world journey planners use more refined algorithms that preprocess the graph of connections between nodes (airports).

A* uses a scorer to estimate the distance from the current node to the end node, it is used as a heuristic to find the correct result more quickly. If the location of the nodes (airports) was known, the straight line distance would have made a good heuristic. 

## Building

To build the application: `./gradlew build` 

To build a jar with its dependencies: `./gradlew shadowJar`. To run this jar, run: `java -jar ./build/libs/holiday-agency-*-all.jar`

