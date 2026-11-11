import { Component, OnInit, computed, inject, input, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';

import { EventService } from '../../services/event.service';
import { CampusEvent, Registration } from '../../models/event.model';

@Component({
  selector: 'app-event-detail',
  imports: [RouterLink, FormsModule, DatePipe],
  templateUrl: './event-detail.html',
  styleUrl: './event-detail.css'
})
export class EventDetail implements OnInit {
  private readonly eventService = inject(EventService);
  private readonly router = inject(Router);

  /** Route parameter :id (bound automatically thanks to withComponentInputBinding) */
  readonly id = input.required<string>();

  readonly event = signal<CampusEvent | null>(null);
  readonly registrations = signal<Registration[]>([]);
  readonly error = signal<string | null>(null);
  readonly message = signal<string | null>(null);
  readonly busy = signal(false);

  studentId: number | null = null;

  readonly isClosed = computed(() => {
    const s = this.event()?.status;
    return s === 'CANCELLED' || s === 'COMPLETED';
  });
  readonly canPublish = computed(() => {
    const s = this.event()?.status;
    return s === 'DRAFT' || s === 'PENDING_ROOM';
  });
  readonly activeRegistrations = computed(() =>
    this.registrations().filter(r => r.status !== 'CANCELLED'));

  ngOnInit(): void {
    this.reload();
  }

  reload(): void {
    const id = Number(this.id());
    this.eventService.getById(id).subscribe({
      next: e => this.event.set(e),
      error: err => this.error.set(EventService.errorMessage(err))
    });
    this.eventService.getRegistrations(id).subscribe({
      next: r => this.registrations.set(r),
      error: () => this.registrations.set([])
    });
  }

  publish(): void {
    this.run(this.eventService.publish(this.eventId), 'Event published');
  }

  cancelEvent(): void {
    if (confirm('Cancel this event? Registered students will no longer be able to attend.')) {
      this.run(this.eventService.cancel(this.eventId), 'Event cancelled');
    }
  }

  deleteEvent(): void {
    if (confirm('Delete this draft event permanently?')) {
      this.busy.set(true);
      this.eventService.delete(this.eventId).subscribe({
        next: () => this.router.navigate(['/events']),
        error: err => this.fail(err)
      });
    }
  }

  register(): void {
    if (!this.studentId) {
      this.error.set('Enter a student ID');
      return;
    }
    this.busy.set(true);
    this.clearAlerts();
    this.eventService.register(this.eventId, this.studentId).subscribe({
      next: r => {
        this.message.set(r.status === 'WAITLISTED'
          ? `Event is full: student ${r.studentId} was added to the waiting list`
          : `Student ${r.studentId} is registered`);
        this.studentId = null;
        this.busy.set(false);
        this.reload();
      },
      error: err => this.fail(err)
    });
  }

  cancelRegistration(r: Registration): void {
    this.run(this.eventService.cancelRegistration(this.eventId, r.studentId),
      `Registration of student ${r.studentId} cancelled`);
  }

  markAttendance(r: Registration): void {
    this.run(this.eventService.markAttendance(this.eventId, r.studentId),
      `Student ${r.studentId} marked as attended`);
  }

  // ---------------------------------------------------------------- helpers

  private get eventId(): number {
    return Number(this.id());
  }

  private run(action: Observable<unknown>, successMessage: string): void {
    this.busy.set(true);
    this.clearAlerts();
    action.subscribe({
      next: () => {
        this.message.set(successMessage);
        this.busy.set(false);
        this.reload();
      },
      error: err => {
        this.fail(err);
        this.reload(); // e.g. publish failed -> event is now PENDING_ROOM
      }
    });
  }

  private fail(err: unknown): void {
    this.error.set(EventService.errorMessage(err));
    this.busy.set(false);
  }

  private clearAlerts(): void {
    this.error.set(null);
    this.message.set(null);
  }
}
