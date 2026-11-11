import { Routes } from '@angular/router';

import { EventList } from './pages/event-list/event-list';
import { EventDetail } from './pages/event-detail/event-detail';
import { EventForm } from './pages/event-form/event-form';

/** Routes of the Events feature (MS4), mounted under /events in app.routes.ts */
export const EVENTS_ROUTES: Routes = [
  { path: '', component: EventList, title: 'Events | CampusVerse' },
  { path: 'new', component: EventForm, title: 'New event | CampusVerse' },
  { path: ':id', component: EventDetail, title: 'Event | CampusVerse' },
  { path: ':id/edit', component: EventForm, title: 'Edit event | CampusVerse' }
];
