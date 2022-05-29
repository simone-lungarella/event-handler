<h1 align="center">Event Handler</h1>
<h3 align="center">Event Handler service with persistent memory</h3>

<p align="center" >
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/Heroku-430098?style=for-the-badge&logo=heroku&logoColor=white" />
</p>

<p> Application that consent to handle a series of events prioritizing them on completion percentage, this application will expose only backend rest endpoints
to perform all required actions. Each event is associated to specific steps and when each step is set as complete, the event will be considered also complete.
A background scheduler will make sure that each event is archived when complete for a customizable number of days. </p>

<p> The persistence layer is handled with h2db with in-file memory and the app is deployed to Heroku and is automated with github actions! </p>
