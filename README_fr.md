
#Introduction

Le but de ce TP est de r�aliser un plugin fonctionnant sous Lutece, en utilisant le plugin WIZARD permettant de cr�er facilement le squelette de notre projet.

Pour ce TP il est n�cessaire de disposer de l�environnement suivant :


 
* Maven 3.0.5
* Git
* SVN
* Ant
* MySQL
* Tomcat 7
* Tomcat admin (optionnel mais tr�s utile pour d�ployer les projets)


#�tape 0 � Clone depuis SVN et Configuration Git-Script

Le projet Lutece a �t� initialement versionn� avec SVN avant de passer sur Git et certains plugins n'ont pas encore �t� migr� vers Git.

Pour commencer, il faut r�cup�rer les sources depuis SVN avec la commande suivante :

 `$svn checkout http://dev.lutece.paris.fr/svn/lutece/portal/trunk lutece-dev` 

Une fois cette commande ex�cut�e, il faut r�cup�rer les scripts git permettant de cloner automatiquement toutes les sources de Github.

Dans un autre r�pertoire faites :

 `$git clone https://github.com/lutece-platform/tools-git-scripts` 

Ensuite copiez le contenu de ce dossier scripts dans le dossier lutece-dev :

 `$cp -r tools-git-scripts/* lutece-dev/` ( **note :** commande unix)

Puis fusionnez les deux sources en tapant dans le dossier lutece-dev :

 `$./lutece.sh config �d` 

Donner votre nom, votre login et mot de passe (https://github.com)

 **Note :** si vous tapez ces commandes dans Git Bash un probl�me peut survenir avec le curl que Git Bash utilise par d�faut. Le prompt demandant le mot de passe ne s'affiche pas.

Ensuite tapez :

 `$./lutece.sh sync -t https` 

#�tape 1 - D�ploiement du plugin WIZARD 4.1.1

Dans cette �tape nous allons r�cup�rer sur GitHub un projet �site� contenant le plugin Wizard version 4.1.1 puis nous allons l'ex�cuter pour initier notre plugin �Budget Participatif�.

##1) R�cup�ration du POM de site sur GitHub :

Pour r�cup�rer le projet entrez la commande suivante :

 `$git clone https://github.com/lutece-platform/lutece-dev-TP.git MyNamePlugin-TP` 

Cette commande cr�� le dossier MyNamePlugin-TP contenant un fichier pom.xml. Celui-ci fait apparaitre les d�pendances suivantes :


```
<dependencies>
    <dependency>
        <groupId>fr.paris.lutece</groupId>
        <artifactId>lutece-core</artifactId>
        <version>5.0.1</version>
        <type>lutece-core</type>
    </dependency>
    <dependency>
        <groupId>fr.paris.lutece.plugins</groupId>
        <artifactId>plugin-pluginwizard</artifactId>
        <version>4.1.1</version>
        <type>lutece-plugin</type>
    </dependency>
</dependencies>
```


On a notamment :


 
* lutece-core : C'est le coeur du CMS Lutece. Cet �l�ment g�re entre autre la coordination de tous les autres plugins
* plugin-pluginwizard : C'est un plugin qui a pour r�le de faciliter la cr�ation d'un nouveau plugin lutece en g�n�rant le squelette d�une application JEE compatible lutece.


##2) Construction du projet :

Positionnez-vous dans le dossier MyNamePlugin-TP et ex�cutez la commande Maven :

 `$mvn lutece:site-assembly` 

Ceci va g�n�rer un dossier �target� contenant un livrable du site (dossier "lutece-TP-1.0.0-SNAPSHOT").

##3) Initialisation de la base de donn�es :

Avant de d�ployer notre version du site sur tomcat, il faut configurer et initialiser la base de donn�es (MySql) qui sera utilis�e par celui-ci.


 
* 
Dans le dossier du site �lutece-TP-1.0.0-SNAPSHOT�, allez dans /WEB-INF/conf/ et �ditez le fichier db.properties. Renommez la base de donn�es utilis�e par le site en �lutece_pwizard� en modifiant la propri�t� portal.url comme suit :
 `portal.url=jdbc:mysql://localhost/ **lutece_pwizard** ?autoReconnect=true&useUnicode=yes&characterEncoding=utf8` 
* 
Modifiez �galement la propri�t� portal.password par le mot de passe root de votre serveur mysql :
 `portal.user=root`  `portal.password=motdepasse` 
* 
Ex�cutez le script Ant d�initialisation de la base de donn�es :
 `ant [path]/MyNamePlugin-TP/target/lutece-TP-1.0.0-SNAPSHOT/WEB-INF/sql/build.xml` 


Une nouvelle base de donn�es nomm� �lutece_pwizard� est alors cr��e sur le serveur MySql local.

##4) D�ploiement du site sur Tomcat :

Nous pouvons � pr�sent d�ployer notre site sur Tomcat.

Pour cela, dans le dossier conf de votre application Tomcat, ajoutez la ligne suivante dans la balise `<HOST>` du fichier server.xml :

 `<Context docBase="[path]/MyNamePlugin-TP/target/lutece-TP-1.0.0-SNAPSHOT" path="/wizard" workDir="[path]/MyNamePlugin-TP/target/work" />` 

Vous pouvez ensuite v�rifier que l�application est bien lanc�e sur l�Url http://localhost:8080/wizard/ (si wizard est le chemin de contexte choisi pour l�application).

La page correspond � l'image picture/step1/first_page.gif s�affiche.

##5) Activation du plugin :

Pour pouvoir utiliser le plugin Wizard, il est d�abord n�cessaire de l�activer et d�affecter les droits � l�utilisateur.

Pour cela il faut aller dans la partie back office du site � l�adresse http://localhost:8080/wizard/jsp/admin/AdminLogin.jsp et s'authentifier en :


 
* login : admin
* mot de passe : adminadmin


Sur la page qui s�affiche, cliquez sur Syst�me > Gestion des Plugin.

La page correspondant picture/step1/plugin_management.gif s�affiche.

Activez le pluginwizard en cliquant sur le bouton vert.

Allez ensuite sur Gestionnaires > Gestion des utilisateurs. Sur l��cran qui s�affiche s�lectionnez le bouton Modifier du user admin.

Sur la page qui s�affiche, allez sur l�onglet �Droits� comme dans l'image picture/step1/user_rights.gif puis cochez les cases correspondant aux fonctionnalit�s du plugin wizard.

Le plugin Wizard est maintenant pr�t � �tre utilis� par le compte admin.

#�tape 2 - Cr�ation du plugin budget participatif

Nous allons dans cette �tape utiliser le plugin Wizard pour g�n�rer les sources du plugin Budget Participatif. Pour cela, nous utiliserons la partie front office du site pr�c�demment d�ploy�.

Allez � l�adresse http://localhost:8080/wizard/jsp/site/Portal.jsp?page=pluginwizard. La fen�tre correspondant � picture/step2/plugin_wizard.gif s�affiche.

Suivez les �tapes du formulaire en renseignant les informations suivantes :


 
* Description du plugin :

| Rubrique| Valeur|
|-----------------|-----------------|
| Nom du plugin :| bp|
| Auteur :| Mairie de Paris|


* Ajoutez une classe m�tier dans M�tier :

| Rubrique| Valeur|
|-----------------|-----------------|
| Classe m�tier :| Project|
| Nom de la table :| bp_project|


* Ajoutez les attributs suivants � la classe Project :

| Nom| Type|
|-----------------|-----------------|
| Name| texte court( 50 caract�res), obligatoire.|
| Description| texte moyen 255 caract�res, obligatoire.|
| image_url| URL, obligatoire.|


* Cr�ez une fonctionnalit� d�administration (adminfeature) dans Administration :

| Rubrique| Valeur|
|-----------------|-----------------|
| Titre :| Manage project|
| Description :| Project Management|
| Nom technique :| ManageBp|
| Droit :| BP_MANAGEMENT|
| Niveau droit :| Niveau 0|
| Classe m�tier :| Project|


* Ajoutez une XPage dans XPage :

| Rubrique| Valeur|
|-----------------|-----------------|
| Nom de la XPage :| project|
| Classe de XPage :| BpApp|
| Classe m�tier :| Project|




Une fois � l��tape �G�n�ration�, v�rifiez le r�capitulatif des sources � g�n�rer puis cliquez sur �G�n�rer l�archive des sources�.

Vous t�l�chargez ainsi un ZIP contenant le plugin bp.

#�tape 3 - D�ploiement du plugin Budget Participatif

Dans cette �tape nous d�ployons le plugin bp qui a �t� g�n�r�.

Commencez par extraire le fichier zip r�cup�r� dans l��tape pr�c�dente, et positionnez-vous dans le dossier extrait (au niveau du fichier pom.xml).

 **Note :** 


 
* Il est n�cessaire de changer dans le fichier pom.xml le num�ro de version du projet parent (balise `<version>` de `<parent>` par d�faut � 3.0 ; mettre 3.0.3)
* Vous devez mettre le dossier extrait dans le dossier lutece-dev (o� sont les sources de SVN et ceux de Git, nous allons utiliser le fichier pom.xml pour le mutil project)


Ex�cutez la commande Maven permettant de construire le projet :

 `$mvn lutece:exploded` 

Cette commande va g�n�rer un dossier �target� contenant le livrable de notre application plugin bp.

De la m�me fa�on que le site du plugin Wizard, nous devons initialiser une base de donn�es pour cette l�application.

Suivez donc les m�mes �tapes d�crites dans la partie �Initialisation de la base de donn�es� de l��tape 1, pour le plugin bp.

 **Note :** Le nom de la base de donn�es � cr�er doit �tre sp�cifique � chaque application pour �viter d'�craser des bases existantes. Pour l�application plugin bp nous la nommerons �lutece_bp�.

D�ployez maintenant l�application sur Tomcat en utilisant le gestionnaire d�applications Web Tomcat (voir partie �D�ploiement du site sur Tomcat� de l��tape 1).

Activez le Plugin bp et affectez les droits d�utilisation � admin (voir partie �Activation du plugin� de l��tape 1).

Un plugin Lutece se compose de deux parties: front office et back office.

Le front office est accessible � l�url : http://localhost:8080/bp/jsp/site/Portal.jsp. Vous trouverez une image correspondante dans picture/step3/front_office.gif.

Et le back office sur : http://localhost:8080/bp/jsp/admin/AdminMenu.jsp. Vous trouverez une image correspondante dans picture/step3/back_office.gif.

#�tape 4 - Nettoyage Front + Bootstrap

Dans cette partie nous allons ajouter 8 projets dans le plugin budget participatif, am�liorer l�affichage de l�interface front et cr�er une page HTML pour afficher les d�tails d�un projet (id, name, description).

 **Note :** 


 
* Le dossier plugin-bp/webapp/WEB-INF/templates/admin contient les mod�les HTML du Back.
* Le dossier plugin-bp/webapp/WEB-INF/templates/skin contient les mod�les HTML du Front.


Vous trouverez une image de l'arborescence du plugin dans picture/step4/plugin_folder_tree.gif.

Dans plugin-bp/webapp/WEB-INF/templates/skin/plugins/bp, supprimez les pages create_project.html et modify_project.html.

Pour l'ajout des projets, vous devez avoir une page similaire � l'image picture/step4/project_management.gif accessible � http://localhost:8080/bp/jsp/admin/plugins/bp/ManageProjects.jsp?view=manageProjects.

Pour am�liorer cette affichage nous allons ajouter du code Bootsrap dans la page manage_projets.html afin d�avoir un changement dynamique au moment de la r�duction de la fen�tre.

Pour l'affichage des projets, vous devez avoir une page similaire � l'image picture/step4/project_display.gif accessible � http://localhost:8080/bp/jsp/site/Portal.jsp?page=project.

##Cr�ation de la page details_project.html :

Nous allons ajouter une page details_project.html qui contient les informations d�un projet (id, name, description, imageUrl) cot� front.

Cette page sera affich�e lorsqu�un utilisateur cliquera sur un projet.

Vous devez avoir une page qui ressemble � picture/step4/detail_project.gif accessible � http://localhost:8080/bp/jsp/site/Portal.jsp?page=project&view=detailsProject&id=1.

Tapez la commande suivante pour obtenir les modifications � apporter par rapport � l'�tape pr�c�dente :

 `$git diff step2 step3` 

#�tape 5 - Ajouter un attribut co�t au Projet

Dans cette partie, nous allons ajouter un attribut co�t dans la table bp_projet de la base de donn�es.

Cet attribut a trois contraintes :


 
* La valeur du co�t doit �tre un nombre compris entre 5 et 25.
* La valeur doit �tre un multiple de 5.
* La valeur ne doit pas �tre nulle.


N�oubliez pas de modifier tous les fichiers n�cessaires.

Tapez la commande suivante pour obtenir les modifications � apporter par rapport � l'�tape pr�c�dente :

 `$git diff step3 step4` 

#�tape 6 - Int�gration du plugin extend (HIT, RATING, COMMENT)

Dans cette partie nous allons r�aliser un multi projet int�grant plusieurs plugins.

Si vous suivez bien ce guide, vous devriez avoir l'arborescence d�crite dans picture/step6/project-folder.gif.

Le dossier plugin-bp-TP (ou tout autre nom renseign� au moment de la cr�ation dans le plugin Wizard) est notre squelette de base pour notre plugin. On peut mettre ce dossier dans le dossier /plugins ou dans un r�pertoire fils de notre dossier lutece-dev. Dans la racine du dossier lutece-dev, se trouve un fichier pom.xml. Il s'agit du pom principal du multi projet. Ouvrez ce fichier et mettez les d�pendances suivantes :


```
<profile>
    <id>multi-project</id>
    <modules>
        <module>lutece-core</module>
        <module>plugin-bp-TP</module>
        <module>plugins/search/library-lucene</module>
        <module>plugins/auth/plugin-mylutece</module>
        <module>plugins/extends/plugin-extend</module>
        <module>plugins/extends/module-extend-rating</module>
        <module>plugins/extends/module-extend-comment</module>
        <module>plugins/auth/module-mylutece-database</module>
        <module>plugins/myapps/plugin-avatar</module>
        <module>plugins/myapps/plugin-mydashboard</module>
        <module>plugins/auth/plugin-mylutecetest</module>
        <module>plugins/tech/plugin-rest</module>
    </modules>
</profile>
```


Compilez le projet gr�ce � la commande suivante puis d�ployez-le sur Tomcat :

 `$mvn lutece:exploded -P multi-project` 

Activez les plugins sur le back office dans Syst�me/Gestion des plugins comme dans picture/step6/plugin_management_2.gif.

Impl�mentez le plugin extend dans le plugin bp. (Vous pouvez suivre ce tutoriel : http://fr.lutece.paris.fr/fr/wiki/howto-extend.html).

 **Note :** Au moment de modifier le template modify_project.html, ne pas ins�rer le markeur dans la balise `<form>` car celui-ci va lui-m�me g�n�rer des balises `<form>` 

 **Exercice :** 


 
* Ajouter l'extension nombre de vues (Hit) dans chaque projet et l�afficher dans la liste des projets sans l�incr�menter.
* Ajouter des commentaires dans la page d�tails de chaque projet.
* Ajouter le vote pour chaque projet dans la page d�tails du projet et l'afficher dans la liste de projets.


Pour l'affichage des projets, vous devez avoir une page similaire � picture/step6/project_display_2.gif.

Pour l'affichage de chaque projet, vous devez avoir une page similaire � picture/step6/detail_project_2.gif.

Tapez la commande suivante pour obtenir les modifications � apporter par rapport � l'�tape pr�c�dente :

 `$git diff step4 step5` 

#�tape 7 - Mise en place d�un Web service REST

Dans cette partie nous allons utiliser le plugin REST de lutece.

Nous allons chercher un ensemble de donn�es de notre base de donn�es et les afficher sous le format JSON.

Pour cela nous allons int�grer le plugin rest dans le multi projet.

Notre choix de technologies est d�utiliser Jersey JAX-RS et l�API Jackson.

Vous pouvez suivre ce tutoriel : http://fr.lutece.paris.fr/fr/wiki/howto-rest.html

 **Exercice :** 

R�alisez deux fonctions en Web Service REST :


 
* La premi�re fonction affiche par projet : le nombre total de vues, le nombre total de votes, le nombre total de commentaires publi�s et non publi�s.
* La deuxi�me affiche le nombre total de vue, le nombre total de commentaires publi�s et non publi�s, le nombre total de votes de tous les projets.


Vous devez avoir un affichage comme dans picture/step7/rest_request.gif.

Tapez la commande suivante pour obtenir les modifications � apporter par rapport � l'�tape pr�c�dente :

 `$git diff step5 step6` 

#�tape 8 - Gestion du cache

Dans cette partie nous allons sauvegarder la r�ponse du Web Service dans le cache.

Vous pouvez suivre ce tutoriel : http://fr.lutece.paris.fr/fr/wiki/cache-management.html

 **Note :** apr�s avoir suivi les instructions du wiki, activez votre cache en back office dans Syst�me/Gestion des caches.

Tapez la commande suivante pour obtenir les modifications � apporter par rapport � l'�tape pr�c�dente :

 `$git diff step6 step7` 

#R�cup�ration du projet final

Vous pouvez r�cup�rer le projet final de la mani�re suivanate :

Apr�s avoir appliqu� l'�tape 0, tapez :

 `$git checkout step7` 

D�ployez ensuite le projet comme � l'�tape 3.


[Maven documentation and reports](http://dev.lutece.paris.fr/plugins/lutece-TP/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*