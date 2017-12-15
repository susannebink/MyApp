Review by Sven Uitendaal
Names 3, het enige dat ik hierop heb aan te merken is dat je de vraagknoppen A, B, C en D noemt. Beter al noemde je het iets als answerA,
answerB enzovoort.
Headers 3, bovenaan de activity zou het fijn zijn als je uitlegt wat het is.
Comments 3, het aantal comments is wat aan de lage kant. Hierdoor wordt het iets moeilijker om je code te lezen.
Layout 45
Formatting 4
Flow 3, je functie om te checken of een user is ingelogd, de listener setten, is wat overbodig. Het is voldoende om te checken of user
niet null is.
Idiom 4
Expressions 4
Decomposition 3, je zou de functies in alfabetische volgorde kunnen zetten, zo zijn ze makkelijk terug te vinden.
Modularization 3, je hebt 3 functies om 1 ding te bereiken: het laden van de vragen. Is het niet makkelijker om bijvoorbeeld 
alle vragen in een lijst te zetten en van daaruit alle vragen laad?
Je hebt heel omslachtig je highscores erin gezet, dat is een verbeterpunt.
Aanvulling: Waarom update je na elke vraag de nieuwe highscore in de database? Is het niet beter om deze score lokaal op te slaan, daarna 
te vergelijken met de highscore in de database en bij een betere score deze te vervangen?
Het zou leuk zijn voor de gebruiker als hij zijn score kan zien nadat hij heeft geparticipeerd aan de quiz
Je hebt in thirdactivity wel erg veel globale variabelen. Dit is niet altijd nodig. 
Ook kan je een view een tag meegeven waarin bijvoorbeeld een object zoals een JSONArray staat, of zelf een class Question maken waarin 
je een vraag met zijn antwoorden zet. Dit scheelt geheugen en complexiteit.
replaceChar is een functie die al bestaat, alleen heet die anders.
