Certification BretteSoft© Géronimo

Certains abonnés rendent les documents en retard au grand chef Géronimo (parfois avec
un retard de plusieurs lunes) ; d’autres dégradent les documents en laissant les papooses
jouer au frisbee^1 avec ; un abonné, suite à un retard de plus de 2 semaines ou à
dégradation de document constatée au retour, sera banni de la tribu pendant 1 mois.

(^1) Jeu traditionnel apache qui se joue à 2 avec un galet plat trouvé dans les collines

Certification BretteSoft© Grand chaman

Lors d'une demande de réservation d'un abonné A, si le document est déjà envouté par un abonné B
mais que cette envoûtement est assez faible – il ne reste que 60s, avant l'achèvement de la réservation -
plutôt que de répondre à l'abonné A « Impossible, le document est encore réservé 60s», et l'obliger à
refaire sa tentative à peine plus tard, on fera patienter l'abonné A avec une musique céleste et, dès que
les augures le permettront, on validera sa réservation. Bien sûr, si l'envoûtement est trop fort et que
l'abonné B passe emprunter le document dans la minute, on informera l'abonné A qu'il n'a pas de
chance pour ce document mais qu'il a bénéficié d'un concert céleste gratuit et qu'il aurait dû faire une
offrande plus importante au grand chaman

Certification BretteSoft© Sitting Bull

Lors d’une réservation, si le document n’est pas disponible, on pourra proposer à l’abonné
de placer une alerte nous avertissant par signaux de fumée lors du retour du document.
La certification suppose l’exploration des bibliothèques de signaux de fumée javax.mail et
l’envoi d’un nuage de test dans le contexte approprié au grand Wakan Tanka (jean -
francois.brette@u-paris.fr)

R4.01 Architecture logicielle 2025 - 26 Jean-François Brette
BUT2 FI
Projet ArchiLog
Réservation/emprunt/retour médiathèque
On désire gérer les réservations/emprunts/retours de documents pour une médiathèque. Le service ne
sera ouvert qu’aux abonnés référencés de la médiathèque (la création de nouveaux abonnés n’est pas
au programme de ce projet). Pour simplifier, on suppose que tout fonctionne 24h/24h. Les documents
concernés ici sont des livres et des DVDs mais cela doit pouvoir évoluer sans difficulté vers des CDs,
etc.
La réservation d’un document par un abonné se fait à distance (depuis chez l’abonné) si le DVD est
disponible en médiathèque ; celui-ci lui est alors réservé pendant 2 h (aucun abonné ne peut
l’emprunter ni le réserver) et l’abonné doit passer à la médiathèque l’emprunter dans ce délai (sinon
la réservation est annulée).
L’emprunt d’un document sur place est possible si le document est disponible ou réservé à l’abonné
qui vient l’emprunter.
Le retour d’un document se passe également sur place.
Chaque document a un identifiant et un titre ; les livres ont un nombre de pages et les dvds un
indicateur booléen adulte, signifiant, s’il est à true, réservé aux plus de 16 ans.
Chaque abonné a un numéro, un nom et une date de naissance.
Les autres informations sont laissées à votre discrétion.
Lors de la réservation ou de l’emprunt, on doit préciser numéro d’abonné et identifiant du document.
Lors du retour, l’identifiant de document suffit (vous pouvez ramener un DVD trouvé dans la rue par
exemple ou pour le compte de quelqu’un d’autre). Pour simplifier, on suppose que ces identifiants
sont connus de l’abonné qui réserve/emprunte/rend un document.
Clients et serveurs

Une application serveur générale est lancée en permanence sur un ordinateur de la médiathèque
(ou ailleurs) d’adresse IP connue. Cette application attend, via trois serveurs d’écoute :
les demandes de réservation sur le port 2000 ,
les demandes d’emprunt sur le port 2001.
les demandes de retour sur le port 2002.
Chacune de ces 3 fonctionnalités correspondra donc à un service déclenché par une application
cliente se connectant au port concerné, suivant le modèle logiciel vu en tps. Un client bttp2.0 avec
le numéro de port récupéré via les arguments du main sera bien sur apprécié, ainsi qu’une mise en
œuvre de la bibliothèque bserveur du tp 4.
Ces 3 serveurs d’écoute sur 3 ports différents sont contractuels : vous devez respecter ce choix
technique.
Un logiciel client de réservation est installé sur l’ordinateur des adhérents (à terme, une version
mobile sera envisagée). Lorsqu’un abonné lance ce client chez lui, le service de réservation lui
demande son numéro d’abonné et l’ identifiant de document qu’il souhaite réserver.
Les logiciels client d’emprunt et de retour sont installés sur des bornes dédiées à la médiathèque.
R4.01 Architecture logicielle 2025 - 26 Jean-François Brette
BUT2 FI
Coté serveur

Les documents et les abonnés sont tous matérialisés par des objets créés en dur au lancement du
programme. L’ajout d’abonnés ou de documents n’est pas à faire pour ce projet.
La tentative de réservation ou d’emprunt, si elle échoue, doit donner lieu à un message de refus
précis (« ce livre est déjà emprunté », « vous n’avez pas l’âge pour emprunter ce DVD », « ce
livre est réservé jusqu’à 12h25 », etc).
L ’interface Document que devra implémenter votre classe DVD
public interface Document {
String idDoc();
// exception si déjà réservé ou emprunté
void reservation (Abonne ab) throws ReservationException;
// exception si réservé pour une autre abonné ou déjà emprunté
void emprunt(Abonne ab) throws EmpruntException;
// sert au retour d’un document ou à l’annulation d‘une réservation
void retour() throws RetourException;
}
Considérez cette interface comme la clé de voute de votre projet : elle est contractuelle et
vous n’avez pas le droit de la modifier , (hormis pour les besoins de certains BretteSoft).
Certifications BretteSoft©

Les certifications BretteSoft© (décrites à part) permettent d’intégrer la tribu comme guerrier des
plaines ; ne faites ça qu’après avoir montré sur le sujet vos capacités à chasser le bison
proprement.
A rendre

Le projet est à réaliser par trinomes inter-groupes.
La remise est fixée au plus tard le dimanche 5 avril 23h59 sur le cours moodle : dépôt du dossier
sous forme d’UN fichier .zip nommé des noms des membres du groupe (mettez juste NOM_Prenom
de chaque membre, ni le groupe, ni ProjetJavaAppServeur, etc...) et contenant le code source
(Java source files) et un rapport de présentation pdf de quelques pages dont le contenu est précisé
à part. Un projet sans rapport de présentation vaut zéro. Un rapport bâclé peut difficilement avoir
la moyenne.
Aucun retard ne sera accepté (même avec points de pénalités), le dépôt moodle étant fermé à
échéance de la date/heure limite.