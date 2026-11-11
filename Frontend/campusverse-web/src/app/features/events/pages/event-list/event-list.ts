import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { EventService } from '../../services/event.service';
import { CampusEvent, EVENT_CATEGORIES, EVENT_STATUSES, EventFilters } from '../../models/event.model';

@Component({
  selector: 'app-event-list',
  imports: [RouterLink, FormsModule, DatePipe],
  templateUrl: './event-list.html',
  styleUrl: './event-list.css'
})
export class EventList implements OnInit {
  private readonly eventService = inject(EventService);

  readonly categories = EVENT_CATEGORIES;
  readonly statuses = EVENT_STATUSES;

  readonly events = signal<CampusEvent[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  filters: EventFilters = { category: '', status: '' };

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.eventService.getAll(this.filters).subscribe({
      next: events => {
        this.events.set(events);
        this.loading.set(false);
      },
      error: err => {
        this.error.set(EventService.errorMessage(err));
        this.loading.set(false);
      }
    });
  }

  resetFilters(): void {
    this.filters = { category: '', status: '' };
    this.load();
  }
}
