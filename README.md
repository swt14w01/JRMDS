JRMDS
=====

jQAssistant Rule Management and Deployment Server




Installationsanleitung:
1) Neo4j Community herunterladen und installieren
2) nach dem Start Settings - Server Configuration, dort die folgende Zeile suchen:
#org.neo4j.server.webserver.address=
und folgendermaßen anpassen (also auskommentieren und binding auf localhost):
org.neo4j.server.webserver.address=[::1]

3) dieses Repo Clonen und als Maven Projekt importieren
4) unter test/jrmdsManagementTest ausführen, dies erstellt in der Datenbank als "Nebenprodukt" zwei Projekte mit vielerlei Gruppen und Regeln und Beziehungen
5) wenn die Application.java ausgeführt wird, kann unter localhost:8080 eine einfache klickbare Seite aufgerufen werden
