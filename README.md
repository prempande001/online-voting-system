# Online Voting Application

A simple online voting web app built with **Java (Servlets)** on the backend
and **JSP + HTML/CSS** on the frontend. This is the basic stage: no database
— all candidates and vote counts live in memory and reset when the app
restarts. It's meant as a clean starting point for practicing a DevOps
workflow (Git, Docker, CI/CD) on top of a real, working app.

## How it works

- `VoteStore` (in `com.voting.model`) is an in-memory singleton holding the
  candidate list and vote counts (`ConcurrentHashMap`-safe via `AtomicInteger`).
- `VoteServlet` (`/vote`) handles the POST from the voting form and uses the
  HTTP session (not a database) to stop a browser session from voting twice.
- `ResetServlet` (`/reset`) clears the votes for easy re-testing — remove or
  protect this before any real deployment.
- `index.jsp` renders the candidate list and voting form.
- `results.jsp` shows live vote counts and percentages, auto-refreshing every
  10 seconds.

## Project structure

```
online-voting-app/
├── pom.xml
├── .gitignore
├── README.md
└── src/main/
    ├── java/com/voting/
    │   ├── model/
    │   │   ├── Candidate.java
    │   │   └── VoteStore.java
    │   └── servlet/
    │       ├── VoteServlet.java
    │       └── ResetServlet.java
    └── webapp/
        ├── WEB-INF/web.xml
        ├── css/style.css
        ├── index.jsp
        └── results.jsp
```

## Running locally in VS Code

**Prerequisites:** JDK 11+ and Maven installed, plus the "Extension Pack for
Java" VS Code extension (not strictly required, but gives you code
completion/debugging).

1. Open the `online-voting-app` folder in VS Code.
2. Open the integrated terminal and run:

   ```bash
   mvn jetty:run
   ```

3. Visit `http://localhost:8080` in your browser.
4. Vote, then check `http://localhost:8080/results.jsp`.
5. To re-test without restarting the server, visit `http://localhost:8080/reset`.

No local Tomcat install is needed — the Jetty Maven plugin runs an embedded
server directly from `mvn jetty:run`.

To build a deployable WAR file instead:

```bash
mvn clean package
```

The WAR will be at `target/online-voting-app.war`.

## Pushing to GitHub

```bash
cd online-voting-app
git init
git add .
git commit -m "Initial commit: basic online voting app (no DB)"
git branch -M main
git remote add origin <your-repo-url>
git push -u origin main
```

## Suggested DevOps practice roadmap

This app is deliberately simple so you can layer DevOps practice on top of
it incrementally:

1. **Git/GitHub basics** — branches, PRs, commit hygiene.
2. **Continuous Integration** — add a GitHub Actions workflow that runs
   `mvn clean package` on every push/PR to catch build breaks.
3. **Containerization** — write a `Dockerfile` (e.g. `FROM jetty:9.4-jre11`
   or a multi-stage Maven build + Tomcat runtime image) and run the app in
   Docker.
4. **Container registry** — push the built image to Docker Hub or GitHub
   Container Registry from CI.
5. **Continuous Deployment** — deploy the container to a free-tier target
   (Render, Fly.io, a small VPS, or a local Kubernetes cluster like
   `kind`/`minikube`).
6. **Add a real database** — once the pipeline works end-to-end, swap
   `VoteStore` for a real datastore (PostgreSQL/MySQL) and practice managing
   schema migrations and secrets in CI/CD.
7. **Observability** — add basic health-check endpoints and logging, then
   wire up simple monitoring.

Happy to help scaffold any of these (Dockerfile, GitHub Actions workflow,
Kubernetes manifests, etc.) whenever you're ready for that step — just ask.
