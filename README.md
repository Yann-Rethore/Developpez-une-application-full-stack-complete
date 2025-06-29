# Developpez-une-application-full-stack-complete - MDD

## Installation du projet

### Créer la BDD
- Utiliser un éditeur comme **MySQL WorkBench**.
- Créer une connexion avec :
  - **Hostname** : `localhost`
  - **Port** : `3306`
  - **Username** : `root`
  - **Password** : à définir
- Créer ensuite une BBD vide par exemple MDD 

---

## Front

### Cloner le projet
Dans l’IDE de votre choix, exécutez :
```sh
git clone https://github.com/Yann-Rethore/Developpez-une-application-full-stack-complete.git
```

### Installer les dépendances
```sh
cd /front
npm install
```



## Back-end

### Cloner et installer le projet
Dans le terminal de votre IDE
```sh
git clone https://github.com/Yann-Rethore/Developpez-une-application-full-stack-complete.git
cd /back
mvn install
```
### Properties et Back End

Le fichier properties a été sécurisé et les des données sensibles ne peuxvent être exportées .
Voici un exemple avec la base de donnée et les autres informations indispensables :
```sh
server.port=8080
spring.profiles.active=test
spring.jpa.open-in-view=false

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.show-sql=true
oc.app.jwtSecret=Votre_Token
oc.app.jwtExpirationMs=86400000

spring.datasource.url=jdbc:mysql://localhost:3306/MDD
spring.datasource.username=root
spring.datasource.password=Votre_Mot_De_Passe
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Le mot de passe de votre base de données et le token seront les votres.
Il existe des programmes simple en Java pour générer les tokens , en cas de soucis vous pouvez me contacter via openClassRoom

## Accès au projet

### Lancer le Front-end
A partir du terminal de votre IDE Front
```sh
npm run start
```

### Lancer la BDD
A Partir de votre gestionnaire de BDD lancer la base de données de MDD

### Lancer le Back-end
A partir de votre IDE Java, lancer le projet.

### Accès au site
Vous pouvez maintenant accèder au site en local à partir de l'adresse : http://localhost:4200/
L'accès se fera via le bouton s'inscrire ou vous pourrez créer votre utilisateur pour utiliser MDD

Actuelement , il n'y a pas de back office prévu il faudra donc créer dans votre Gestionnaire de BDD au minimum un topic dans la table topics afin de tester l'application jusqu'au bout.


