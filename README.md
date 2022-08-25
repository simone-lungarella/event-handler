<h1 align="center">EVENT HANDLER</h1>
<h3 align="center">Event Handler service with persistent memory</h3>

<p align="center" >
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/Heroku-430098?style=for-the-badge&logo=heroku&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white"/>
</p>

### Description
Application that consent to handle a series of events related to windfarms, prioritizing them on completion percentage. This application will expose only backend rest endpoints to perform all required actions. Each event is associated to specific steps and when each step is set as complete, the event will be also considered complete.
A background scheduler will make sure that each event is archived when complete for a customizable number of days and another scheduler will send e-mails to warn users about an incoming deadline.

The Api exposes services to handle user registration and authentication that requires to provide a valid jwt token to access any endpoint. Some specific operations also requires to be an authenticated Admin (eg. user registration).

The persistence layer is handled by a postgres server. The app is deployed to Heroku and is automated with github actions!

### Front end
The web-app that is allowed to use this api can be accessed from <a href="https://simone-lungarella.github.io/winforce/" > this page </a> and it requires to be authenticated to access every functionality!
