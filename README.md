# AMAZING TOKEN GENERATOR


Progetto utilizzato per la scrittura della tesi triennale, contiene metodi per la generazione di token JWT, PASETO di tipo local e di token 
Macaroon.

Ogni token può essere creato, può essere validato il contenuto dei claim riservati dei token JWT e dei PASETO local.

A causa della mancanza di uno standard condiviso, non è possibile validare i token Macaroon, ma è possibile creare un token Macaroon con un determinato set di permessi e verificare se un token è sintatticamente valido.

# Installazione

Il progetto è installabile tramite Docker, di seguito i comandi necessari per lanciare il progetto in locale:

- docker build -t amazing-token-generator .
- docker run -p 8008:8080 amazing-token-generator

Il progetto sarà utilizzabile all'indirizzo http://localhost:8080

# Utilizzo
Consultare la documentazione nel file api.yaml per avere una panoramica delle API esposte dal progetto.