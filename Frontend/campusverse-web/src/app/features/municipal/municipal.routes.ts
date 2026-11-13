import { Routes } from '@angular/router';
import { CatalogueComponent } from './pages/catalogue/catalogue';
import { ApplicationComponent } from './pages/application/application';
import { TrackingComponent } from './pages/tracking/tracking';

export const MUNICIPAL_ROUTES: Routes = [
  { path: '', component: CatalogueComponent, title: 'Municipal Services | CampusVerse' },
  { path: 'apply', component: ApplicationComponent, title: 'Apply for Municipal Service | CampusVerse' },
  { path: 'apply/:serviceId', component: ApplicationComponent, title: 'Apply for Municipal Service | CampusVerse' },
  { path: 'track', component: TrackingComponent, title: 'Track Municipal Request | CampusVerse' },
  { path: 'track/:id', component: TrackingComponent, title: 'Track Municipal Request | CampusVerse' }
];
