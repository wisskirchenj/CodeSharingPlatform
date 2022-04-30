# IDEA EDU Course ...

Implemented in the Java <b>Backend Developer</b> Track of hyperskill.org's JetBrain Academy.<br>
https://hyperskill.org/projects/130

Project goal (or rather my interpretation of it) is to implement a Spring boot application with functional endpoints. 
The application will serve as a platform for code sharing between developers over the web.
The Code Sharing Platform will simultaneously provide a web interface delivering text/html and an API interface
providing application/json content-type. The Html pages are rendered by use of Thymeleaf template engine
together with templates and static Css and JS code. The web client posted code snippets are stored in
an H2 file database.

Purpose of doing this project, is getting more familiar with CSS / html frontends and practising Spring boot
applications - experimenting with the new functional Spring 5 routing and handling techniques.

As build tool gradle is used this time to get to know this tool better.

# Project was completed on 30.04.22.

## Repository Contents

Sources for all project tasks (5 stages) with tests, static contents HTML, JS, CSS and configurations.

## Progress

09.04.22 Project started. IDEA-setup and first repo.

11.04.22 Stage 1 completed with functional MVC endpoints, html and json type GET of a fixed code snippet.

18.04.22 Stage 2 completed with frontend using thymeleaf templates with RenderingResponse, JS, CSS and HTML forms.

19.04.22 Stage 3 completed, routing extended, thymeleaf with collections, more endpoints

22.04.22 Stage 4 completed, highlight.js added for html rendering and H2 database store implemented, with a Spring PagingAndSortingRepository

22.04.22 Final Stage 5 completed, store snippets by uuid. Secret snippets added with restriction on views
and/or time in seconds. Conditional thymeleaf rendering.
