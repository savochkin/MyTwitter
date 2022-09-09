# MyTwitter
Robert J. Martin (Clean Arhitecture, section 21): 

"Suppose you were looking at the architecture of a library. You would likely see a grand entrance, an area for check-in/out clerks, reading areas, small conference rooms, and gallery after gallery capable of holding bookshelves for all the books in the library. That architecture would scream: “LIBRARY.”

So what does the architecture of your application scream? When you look at the top-level directory structure, and the source files in the highest-level package, do they scream “Health Care System,” or “Accounting System,” or “Inventory Management System”? Or do they scream “Rails,” or “Spring/Hibernate,” or “ASP”?"

"If your system architecture is all about the use cases, and if you have kept your frameworks at arm’s length, then you should be able to unit-test all those use cases without any of the frameworks in place. You shouldn’t need the web server running to run your tests. You shouldn’t need the database connected to run your tests. Your Entity objects should be plain old objects that have no dependencies on frameworks or databases or other complications. Your use case objects should coordinate your Entity objects. Finally, all of them together should be testable in situ, without any of the complications of frameworks."

This is intended to be a training to try to do naive implementation of twitter with the following in mind:
1. Try a development approach when we implement business logic + its unit tests first and the rest follows.
2. Try to implement the core business logic such that the business logic "screams" its purpose.
3. Assess how separation of business logic and the underlying technology may be achieved in practice and in what extent we should take it into account.
(if we completely not taking into account the tech limitation than we may end up with implementation with poor performance, maintainability etc)

Thinks to look at (suggested check points):
- Business logic should be independent from all dependencies to technical frameworks (Spring, persistence, UI etc)
- Business classes should not share packages 
(not: org.company.controllers,  dto, model ... with all classes mixed in those tech packages, but: com.company.domain (for business logic), ui (for UI), persistence (for persistence)
- Interfaces (UI, persistence...) should be pluggable as much as possible

My try #1
1. Tried to implement twitter's logic first and made it as much more clear as possible. The objects should be as close to the vocabulary we use: Feed, User, etc.
2. Took into account the traffic load as: getNewsFeed is very often (O(1)), postTweet - moderate (O(F)), follow/unfollow - very rate (O(1) ). 
3. Implemented unit tests of the business logic right from the beggining and made it work before implementing tech stuff.
4. After that implemented the persistence.
5. After that implemented the REST interface.

Conclusions:
1. Business logic is in the twitter.domain package and has no dependecies from Spring Frameworkd, nor persisntence, nor UI. This is done via dependency inversion (see below) and because of the fact the Config bean in Spring Framework implements Factory pattern.
2. Persistence logic is in twitter.persistence and immplements UserRepository interface. Thus, we invert the dependency and ensure that neither business logic, nor UI has dependecies on the persistence implementation.
3. UI-logic is in twitter.web and depends only on business logic and Spring MVC.
4. Attention: see the implementation of adding new subscribers - we obtain the list of current subscribers and simply add a new one (which is how it is most naturally it was implemented in business logic alone). In order to make it work with DB we needed to make our implementation of FollowerList.
5. I found that this way working - implement business logic first and then persistence and UI - is very convenient as really the most difficulty lies (should lie) in the business logic. After I implementedd the business logic and had it unit tested - it would very easy to add persinstence and UI.
