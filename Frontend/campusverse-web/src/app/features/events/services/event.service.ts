import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiError, CampusEvent, EventFilters, EventRequest, Registration } from '../models/event.model';

/** Calls the MS4 Event service. In development, /api/events is proxied to http://localhost:8084 (proxy.conf.json). */
@Injectable({ providedIn: 'root' })
export class EventService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/events';

  // ---------------------------------------------------------------- events

  getAll(filters: EventFilters = {}): Observable<CampusEvent[]> {
    let params = new HttpParams();
    if (filters.category) params = params.set('category', filters.category);
    if (filters.status) params = params.set('status', filters.status);
    if (filters.clubId) params = params.set('clubId', filters.clubId);
    return this.http.get<CampusEvent[]>(this.baseUrl, { params });
  }

  getById(id: number): Observable<CampusEvent> {
    return this.http.get<CampusEvent>(`${this.baseUrl}/${id}`);
  }

  create(request: EventRequest): Observable<CampusEvent> {
    return this.http.post<CampusEvent>(this.baseUrl, request);
  }

  update(id: number, request: EventRequest): Observable<CampusEvent> {
    return this.http.put<CampusEvent>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  publish(id: number): Observable<CampusEvent> {
    return this.http.patch<CampusEvent>(`${this.baseUrl}/${id}/publish`, {});
  }

  cancel(id: number): Observable<CampusEvent> {
    return this.http.patch<CampusEvent>(`${this.baseUrl}/${id}/cancel`, {});
  }

  // ---------------------------------------------------------------- registrations

  getRegistrations(eventId: number): Observable<Registration[]> {
    return this.http.get<Registration[]>(`${this.baseUrl}/${eventId}/registrations`);
  }

  register(eventId: number, studentId: number): Observable<Registration> {
    return this.http.post<Registration>(`${this.baseUrl}/${eventId}/registrations`, { studentId });
  }

  cancelRegistration(eventId: number, studentId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${eventId}/registrations/${studentId}`);
  }

  markAttendance(eventId: number, studentId: number): Observable<Registration> {
    return this.http.patch<Registration>(`${this.baseUrl}/${eventId}/registrations/${studentId}/attendance`, {});
  }

  getStudentRegistrations(studentId: number): Observable<Registration[]> {
    return this.http.get<Registration[]>(`${this.baseUrl}/students/${studentId}/registrations`);
  }

  // ---------------------------------------------------------------- helpers

  /** Turns an HTTP error into a readable message (uses the backend ErrorResponse when present). */
  static errorMessage(err: unknown): string {
    if (err instanceof HttpErrorResponse) {
      const body = err.error as ApiError | null;
      if (err.status === 0 || err.status === 504) {
        return 'Cannot reach the Event service. Is it running on port 8084?';
      }
      if (body?.message) {
        const fields = body.fieldErrors
          ? ' — ' + Object.entries(body.fieldErrors).map(([f, m]) => `${f}: ${m}`).join(', ')
          : '';
        return `${body.message}${fields}`;
      }
      return `Error ${err.status}: ${err.statusText}`;
    }
    return 'Unexpected error';
  }
}
