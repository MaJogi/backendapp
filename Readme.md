Things I would add in real application
1. Checkstyle
2. PMD
3. Spotbugs
4. Jacoco
5. Perhaps generic repository for different entities
6. Integration tests
7. Different application.properties for testing & development environment.

How to start using IdCodeValidation application:
1. Run `docker-compose up` on project root directory.
   1. Linux users might need to add `sudo` at the start of sentence.
2. Start backend application by opening Gradle tab in intellij and executing Gradle bootRun task
3. Start frontend application by running `npm start`