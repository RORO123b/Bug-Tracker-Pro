# Barbulescu Robert Cristian - Bug Tracker Pro

## Project Structure

```
.
-- src/
    `-- main/
        `-- java/
            |-- commands/       --> commands needed for project execution
            |   |-- search/     --> strategies needed for the search command
            |-- enums/
            |-- fileio/         --> files used for input
            |-- main/           --> main application
            |-- milestones/ 
            |-- tickets/
            |   |-- action/
            `-- users/
```

## Design Patterns

### 1. Singleton
I used singleton to create the AppCenter, my idea was to use it as a kind of data center or operation center where different updates are made or different data is provided.

### 2. Commands
There is a "remote control" `Invoker`, which receives the command to be executed and calls its corresponding execute method. Each command has a separate file for their execution. Also, I implemented a `CommandHelper` that helps create different output nodes.

### 3. Strategy
For the search command I implemented this design pattern, each filter having its separate strategy. Developer and Manager are approached separately, each having an "engine" at their disposal which is used to follow the Open-Closed Principle. It should be mentioned that I used an unbounded wildcard ("?") within a list to receive either tickets or developers (depending on what is requested).

### 4. Factory
For users I decided to use factory since it doesn't have optional fields and this pattern made my work easier.

### 5. Builder
Last but not least. I wanted to improve my OOP abilities to the maximum with this and I think I did a pretty good job. For the ticket builder I first created a generic abstract class (`TicketBuilder<T extends Ticket, B extends TicketBuilder<T, B>>`) that uses self-bounded generics to allow correct and type-safe method chaining. This base class contains common methods for all ticket types (id, title, type, businessPriority, etc.) and an abstract `self()` method that returns the concrete builder instance. Then, each specific ticket type (Bug, FeatureRequest, UIFeedback) has its own concrete builder that extends `TicketBuilder` and adds methods specific to that type. For example, `BugBuilder` adds methods for `severity`, while `FeatureRequestBuilder` adds methods for `businessValue` and `customerDemand`. This approach allows code reuse and extensibility for new ticket types. Also, I implemented a similar `ActionBuilder` for creating `Action` objects that record the history of changes on tickets.

It should be mentioned that I also used a builder (simpler) for actions to facilitate the creation of actions within `viewTicketHistory`.

## Difficulties Encountered + How I Solved Them
- For search, I felt the need to deepen my knowledge of lambda expressions and LLMs gave me the idea to use streams to apply multiple operations to a list (I had to learn what they do as well)
- I considered that Comparators are an important tool that I should incorporate into the project, so I used them to sort developers, milestones, and tickets

## Error Handling Approach
I had the option to create separate exceptions for each type of error or use built-in ones. I opted for the latter because I could divide all errors into 2 categories: those for incorrect arguments and those for incorrect states of elements. Thus I was able to use `IllegalArgumentException` or `IllegalStateException`