/** Types matching the DTOs of the MS4 Event service (Backend/event-service). */

export type EventCategory =
  | 'ACADEMIC' | 'CULTURAL' | 'SPORTS' | 'CLUB'
  | 'CONFERENCE' | 'WORKSHOP' | 'SOCIAL' | 'OTHER';

export type EventStatus = 'DRAFT' | 'PENDING_ROOM' | 'PUBLISHED' | 'CANCELLED' | 'COMPLETED';

export type RegistrationStatus = 'CONFIRMED' | 'WAITLISTED' | 'CANCELLED' | 'ATTENDED';

export const EVENT_CATEGORIES: EventCategory[] =
  ['ACADEMIC', 'CULTURAL', 'SPORTS', 'CLUB', 'CONFERENCE', 'WORKSHOP', 'SOCIAL', 'OTHER'];

export const EVENT_STATUSES: EventStatus[] =
  ['DRAFT', 'PENDING_ROOM', 'PUBLISHED', 'CANCELLED', 'COMPLETED'];

export interface CampusEvent {
  id: number;
  title: string;
  description: string | null;
  category: EventCategory;
  status: EventStatus;
  startDateTime: string;
  endDateTime: string;
  registrationDeadline: string | null;
  maxCapacity: number;
  confirmedCount: number;
  availableSeats: number;
  organizerId: number;
  clubId: number | null;
  spaceId: number | null;
  reservationId: number | null;
  createdAt: string;
  updatedAt: string;
}

export interface EventRequest {
  title: string;
  description?: string | null;
  category: EventCategory;
  startDateTime: string;
  endDateTime: string;
  registrationDeadline?: string | null;
  maxCapacity: number;
  organizerId: number;
  clubId?: number | null;
  spaceId?: number | null;
}

export interface Registration {
  id: number;
  eventId: number;
  eventTitle: string;
  studentId: number;
  status: RegistrationStatus;
  registeredAt: string;
}

export interface EventFilters {
  category?: EventCategory | '';
  status?: EventStatus | '';
  clubId?: number | null;
}

/** Error body returned by the backend GlobalExceptionHandler. */
export interface ApiError {
  status: number;
  error: string;
  message: string;
  fieldErrors?: Record<string, string> | null;
}
