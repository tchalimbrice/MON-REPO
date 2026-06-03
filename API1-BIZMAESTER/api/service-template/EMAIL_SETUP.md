Configuration mail et test rapide

1) Variables d'environnement recommandées

- SPRING_MAIL_HOST (ex: smtp.mailtrap.io)
- SPRING_MAIL_PORT (ex: 587)
- SPRING_MAIL_USERNAME
- SPRING_MAIL_PASSWORD
- MAIL_FROM (ex: no-reply@yourdomain.com)
- APP_FRONTEND_URL (ex: http://localhost:4200)

3) Configurer Mailtrap (exemple)

 - Créez un inbox sur https://mailtrap.io et récupérez `username` et `password` (SMTP credentials).
 - Exportez les variables d'environnement avant de lancer l'application (Windows PowerShell):

```powershell
setx SPRING_MAIL_HOST "smtp.mailtrap.io"
setx SPRING_MAIL_PORT "2525"
setx SPRING_MAIL_USERNAME "<MAILTRAP_USERNAME>"
setx SPRING_MAIL_PASSWORD "<MAILTRAP_PASSWORD>"
setx MAIL_FROM "no-reply@yourdomain.com"
```

Remplacez `<MAILTRAP_USERNAME>` et `<MAILTRAP_PASSWORD>` par vos identifiants Mailtrap.

4) Test rapide avec curl (après démarrage du service-template)

```bash
curl -X POST "http://localhost:8094/api/invitations?companyId=1&createdByUserId=1" \
  -H "Content-Type: application/json" \
  -d '{"invitedEmail":"collab@example.com","firstName":"Jean","lastName":"Dupont","position":"Collaborateur"}'
```

Vérifiez l'inbox Mailtrap pour voir le message reçu.

2) Lancer l'application (depuis le module `service-template`)

mvn spring-boot:run

3) Exemple d'appel pour créer une invitation (et déclencher l'envoi)

curl -X POST "http://localhost:8094/api/invitations?companyId=1&createdByUserId=1" \
  -H "Content-Type: application/json" \
  -d '{"invitedEmail":"collab@example.com","firstName":"Jean","lastName":"Dupont","position":"Collaborateur"}'

Le service retournera "Invitation created and email queued" et un job programmé enverra l'email (toutes les 10s par défaut). Pour tester localement sans SMTP réel, utilisez un service comme Mailtrap ou configurez des variables SMTP pour votre provider.
