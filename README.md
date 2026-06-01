# Shop-Flow 🛒

Un système de gestion complète pour une boutique en ligne moderne et performante.

**Shop-Flow** est une application full-stack construite avec **Java Spring Boot** pour le backend et **Next.js** pour le frontend, offrant une expérience d'achat fluide et une gestion boutique efficace.

---

## ✨ Caractéristiques

- 🔐 **Authentification sécurisée** avec JWT
- 🛍️ **Gestion complète des produits** - Ajouter, modifier, supprimer des articles
- 🛒 **Panier d'achat intuitif** - Gestion fluide des achats
- 📦 **Gestion des commandes** - Suivi et historique
- 👤 **Gestion des utilisateurs** - Profils et préférences
- 🔍 **Recherche et filtrage** - Trouvez facilement vos produits
- 📱 **Design responsive** - Fonctionne sur tous les appareils
- 📊 **Tableau de bord administrateur** - Statistiques et gestion
- 🚀 **Performance optimisée** - Chargement rapide et fluide

---

## 🏗️ Architecture

```
Shop-Flow/
├── backend/              # API REST Spring Boot
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
└── frontend/             # Application Next.js
    ├── app/
    ├── components/
    ├── public/
    ├── package.json
    └── README.md
```

### Stack Technologique

| Couche | Technologie | Version |
|--------|-------------|---------|
| **Backend** | Java Spring Boot | 3.2.5 |
| **Language** | Java | 21 |
| **Base de données** | PostgreSQL / H2 | Latest |
| **Frontend** | Next.js | 16.2.4 |
| **Framework UI** | React | 19.2.4 |
| **Langage Frontend** | TypeScript | 5 |
| **Styling** | Tailwind CSS | 4 |
| **UI Components** | Radix UI | Latest |
| **État Global** | Zustand | 5.0.12 |
| **HTTP Client** | Axios | 1.15.2 |
| **Notifications** | React Hot Toast | 2.6.0 |
| **Icônes** | Lucide React | 1.8.0 |

---

## 🚀 Démarrage Rapide

### Prérequis

- **Java 21+**
- **Node.js 18+** et **npm** (ou yarn)
- **PostgreSQL 12+** (ou H2 pour le développement)
- **Git**

### Installation Backend

```bash
cd backend

# Installer les dépendances Maven
mvn clean install

# Configurer la base de données
# Éditer src/main/resources/application.properties
# spring.datasource.url=jdbc:postgresql://localhost:5432/shopflow
# spring.datasource.username=your_username
# spring.datasource.password=your_password

# Lancer l'application
mvn spring-boot:run
```

L'API sera disponible sur `http://localhost:8080`

**Documentation API (Swagger):** http://localhost:8080/swagger-ui.html

### Installation Frontend

```bash
cd frontend

# Installer les dépendances
npm install
# ou
yarn install

# Lancer le serveur de développement
npm run dev
# ou
yarn dev
```

L'application sera disponible sur `http://localhost:3000`

### Lancer les deux en parallèle

```bash
# Terminal 1 - Backend
cd backend && mvn spring-boot:run

# Terminal 2 - Frontend
cd frontend && npm run dev
```

---

## 📚 Documentation

### Backend
Pour plus de détails sur l'API, les endpoints et la configuration, consultez [backend/README.md](./backend/README.md)

### Frontend
Pour plus d'informations sur l'interface utilisateur et les composants, consultez [frontend/README.md](./frontend/README.md)

---

## 🔧 Configuration

### Variables d'environnement Backend

Créer un fichier `.env` dans le répertoire `backend` :

```properties
# Base de données
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/shopflow
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password

# JWT
JWT_SECRET=your_super_secret_jwt_key_here
JWT_EXPIRATION=86400000

# Serveur
SERVER_PORT=8080
```

### Variables d'environnement Frontend

Créer un fichier `.env.local` dans le répertoire `frontend` :

```properties
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_APP_NAME=Shop-Flow
```

---

## 🐳 Docker (Optionnel)

### Déployer avec Docker Compose

```bash
docker-compose up -d
```

Voir `docker-compose.yml` pour plus de détails.

---

## 📦 Build & Déploiement

### Build Backend

```bash
cd backend
mvn clean package
java -jar target/shopflow-backend-1.0.0.jar
```

### Build Frontend

```bash
cd frontend
npm run build
npm start
```

### Déploiement Production

Consultez les guides de déploiement :
- [Backend Deployment](./backend/DEPLOYMENT.md)
- [Frontend Deployment](./frontend/DEPLOYMENT.md)

---

## 🧪 Tests

### Backend

```bash
cd backend
mvn test
```

### Frontend

```bash
cd frontend
npm test
```

---

## 📖 Structure des Données

### Modèles principaux

- **User** - Utilisateurs et authentification
- **Product** - Produits de la boutique
- **Cart** - Panier d'achat
- **Order** - Commandes
- **Category** - Catégories de produits

---

## 🔐 Sécurité

- ✅ Authentification JWT sécurisée
- ✅ Validation des entrées côté serveur
- ✅ Protection CORS configurée
- ✅ Hachage des mots de passe avec bcrypt
- ✅ Validation des requêtes

---

## 🤝 Contribution

Les contributions sont bienvenues ! Pour contribuer :

1. Fork le projet
2. Créer une branche (`git checkout -b feature/ma-feature`)
3. Commit vos changements (`git commit -m 'Add ma-feature'`)
4. Push vers la branche (`git push origin feature/ma-feature`)
5. Ouvrir une Pull Request

---

## 📝 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

## 👨‍💻 Auteur

**Fedi Sayadi** - [@fedisayadi29](https://github.com/fedisayadi29)

---

## 💬 Support

Pour toute question ou problème :

- 📧 Créer une [issue GitHub](https://github.com/fedisayadi29/Shop-Flow/issues)
- 📖 Consulter la [documentation](./docs)

---

## 🎯 Feuille de Route

- [ ] Intégration paiement (Stripe/PayPal)
- [ ] Système de recommandations
- [ ] Notifications email
- [ ] Système d'avis et commentaires
- [ ] Dashboard analytique avancé
- [ ] Optimisation mobile
- [ ] Internationalisation (i18n)

---

## ⭐ N'oublie pas de mettre une star si tu aimes le projet ! 🌟

