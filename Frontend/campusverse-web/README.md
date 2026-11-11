# CampusVerse Web (Angular 21)

**One** Angular website for the whole team. Each microservice owner builds their pages in their **own feature folder**.

## Run it

Requirements: Node.js 20.19+ / 22.12+ / 24+

```bash
cd Frontend/campusverse-web
npm install        # first time only
npm start          # = ng serve -> http://localhost:4200
```

Start the backend microservices you need first (e.g. Event service on port 8084).
Calls to `/api/...` are forwarded to the right backend by **`proxy.conf.json`**, so there is no CORS problem.

## Structure

```
src/app/
├── app.ts / app.html        top bar + navigation (add your link here)
├── app.routes.ts            add ONE line for your feature here
├── pages/home/              home page (add a tile for your feature)
└── features/
    └── events/              MS4 Events (isra)
        ├── models/          TypeScript types matching the backend DTOs
        ├── services/        HTTP calls to the backend
        ├── pages/           event-list, event-detail, event-form
        └── events.routes.ts
```

## Add your microservice (example: Lost & Found on port 8086)

1. Create `src/app/features/lost-found/` with `models/`, `services/`, `pages/` and `lost-found.routes.ts`
   (copy the `events` folder as a model).
2. In `app.routes.ts` add:
   ```ts
   { path: 'lost-found', loadChildren: () => import('./features/lost-found/lost-found.routes').then(m => m.LOST_FOUND_ROUTES) },
   ```
3. In `app.html` add a nav link: `<a routerLink="/lost-found" routerLinkActive="active">Lost &amp; Found</a>`
4. In `proxy.conf.json` add your backend:
   ```json
   "/api/lost-items": { "target": "http://localhost:8086", "secure": false, "changeOrigin": true }
   ```
5. Use the shared CSS classes from `src/styles.css` (`card`, `btn`, `badge`, `alert`, `form-grid`...) so all pages look the same.

**Only edit your own feature folder** + one line in each shared file above → no Git conflicts.

## Ports

| Microservice | Backend port | API prefix |
|---|---|---|
| MS1 Campus Spaces | 8081 | TBD |
| MS2 Reservations | 8082 | TBD |
| MS3 Student Clubs | 8083 | TBD |
| MS4 Events | 8084 | `/api/events` |
| MS5 Equipment Lending | 8085 | TBD |
| MS6 Lost & Found | 8086 | TBD |
| MS7 AI Brain | 8000 | TBD |
