# Introduktion #

CS-IFrame portlet är en iFrame portlet i grunden, som har utökats med möjligheten att spara inloggnings uppgifter för användare i en databas.

IFrame tekniken används för komma åt en existerande extern webbplats. Dessa externa webbtjänster kräver ofta en inloggning för att presentera en personaliserad vy av informationen.

CS-IFrame klarar basic-authentication och enklare formulär baserade inloggningar. Inloggnings informationen sparas undan efter första användningen så att man automatiskt blir inloggad nästa gång portleten presenteras.

# Användning #

Första gången man går in på portleten kommer man till "Credential" skärmen där man skriver in användarnamn och lösenord för den externa webplatsen.
Om det finns en sparad inloggningsuppgift så används dessa och en inloggning görs automatiskt.

Om inloggningen misslyckas tar den externa webbplatsens normala beteende över och man blir vanligtvis hänvisad till inloggningssidan igen. Dock, denna gång är det den externa webbplatsens egna inloggningssida och det går inte att spara undan inloggnings uppgifterna. Så om felaktiga inloggningsuppgift angivits, eller om inloggningsuppgifterna ändrats måste man själv gå in på "Credential" skärmen och ändra till korrekta uppgifter för att få automatiken att fungera.

# Portlet konfiguration #

## Formulärbaserad inloggning ##

CS-IFrame skapar ett eget inloggnings formulär, det är därför viktigt att efterlikna original formuläret tillräckligt mycket för att inloggningen skall lyckas.
Det enda dynamiska innehåll man kan påverka är användarnamn och lösenord så namnen på dessa formulär parametrar måste konfigureras.
Vart formuläret skall skickas behöver också konfigureras och om det skall skickas som ett GET eller POST anrop.

Inloggningsuppgifterna sparas per inloggad användare och en nyckel för typ av inloggnings uppgift (SiteKey). En siteKey kan återanvändas av flera CS-IFrame portlets om de externa webbplatserna använder samma inloggningsuppgifter (även om inloggningsformuläret ser olika ut).

## Lagring av inloggningsuppgifter ##

Inloggningsuppgifterna sparas i en databas, i tabellen usersitecredentail:

CREATE TABLE usersitecredential
(
> uid character varying(20) NOT NULL,
> sitekey character varying(50) NOT NULL,
> siteuser character varying(256),
> sitepassword character varying(20),
> CONSTRAINT vaultkey PRIMARY KEY (uid, sitekey)
);

Inloggningsuppgifterna till databasen sparas i Spring konfigurationsfilen "datasource.properties".

Lösenorden i usersitecredential krypteras med en reverserbar algoritm (AES). För att det skall fungera behöver man en nyckel - filen "cv.key".
Denna nyckelfil skall finnas på classpath för att krypteringen skall fungera.
Nyckelfilen kan skapas med CryptoUtilImpl som finns med i koden genom att köra CryptoUtilImpl som ett fristående program.

Filen cv.key MÅSTE hanteras konfidenciellt eftersom alla sparade lösenord kan återskapas om man har tillgång till den.

# Utvecklingsmiljö - Liferay & Eclipse #

Man behöver se till att filen `util-taglib.jar` ligger under `$LIFERAY_HOME\tomcat\lib\ext`