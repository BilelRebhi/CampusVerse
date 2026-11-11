import { Component, OnInit, inject, input, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { EventService } from '../../services/event.service';
import { EVENT_CATEGORIES, EventCategory, EventRequest } from '../../models/event.model';

/** Create (/events/new) or edit (/events/:id/edit) an event. */
@Component({
  selector: 'app-event-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './event-form.html'
})
export class EventForm implements OnInit {
  private readonly eventService = inject(EventService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);

  /** Route parameter :id — undefined when creating */
  readonly id = input<string>();

  readonly categories = EVENT_CATEGORIES;
  readonly error = signal<string | null>(null);
  readonly saving = signal(false);

  readonly form = this.fb.group({
    title: ['', [Validators.required, Validators.maxLength(150)]],
    description: ['', Validators.maxLength(2000)],
    category: ['WORKSHOP' as EventCategory, Validators.required],
    startDateTime: ['', Validators.required],
    endDateTime: ['', Validators.required],
    registrationDeadline: [''],
    maxCapacity: [30, [Validators.required, Validators.min(1)]],
    organizerId: [1, [Validators.required, Validators.min(1)]],
    clubId: [null as number | null],
    spaceId: [null as number | null]
  });

  get isEdit(): boolean {
    return !!this.id();
  }

  ngOnInit(): void {
    if (this.isEdit) {
      this.eventService.getById(Number(this.id())).subscribe({
        next: e => this.form.patchValue({
          title: e.title,
          description: e.description ?? '',
          category: e.category,
          startDateTime: e.startDateTime.substring(0, 16),
          endDateTime: e.endDateTime.substring(0, 16),
          registrationDeadline: e.registrationDeadline?.substring(0, 16) ?? '',
          maxCapacity: e.maxCapacity,
          organizerId: e.organizerId,
          clubId: e.clubId,
          spaceId: e.spaceId
        }),
        error: err => this.error.set(EventService.errorMessage(err))
      });
    }
  }

  invalid(name: string): boolean {
    const c = this.form.get(name);
    return !!c && c.invalid && (c.touched || c.dirty);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    const request: EventRequest = {
      title: v.title!.trim(),
      description: v.description || null,
      category: v.category!,
      startDateTime: v.startDateTime!,
      endDateTime: v.endDateTime!,
      registrationDeadline: v.registrationDeadline || null,
      maxCapacity: Number(v.maxCapacity),
      organizerId: Number(v.organizerId),
      clubId: v.clubId ? Number(v.clubId) : null,
      spaceId: v.spaceId ? Number(v.spaceId) : null
    };

    this.saving.set(true);
    this.error.set(null);
    const save$ = this.isEdit
      ? this.eventService.update(Number(this.id()), request)
      : this.eventService.create(request);

    save$.subscribe({
      next: saved => this.router.navigate(['/events', saved.id]),
      error: err => {
        this.error.set(EventService.errorMessage(err));
        this.saving.set(false);
      }
    });
  }
}
