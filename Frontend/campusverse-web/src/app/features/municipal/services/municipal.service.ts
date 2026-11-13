import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import {
  ApplicationRequest,
  CreateApplicationDto,
  MunicipalService,
  RequestStatus,
  UpdateStatusDto
} from '../models/municipal.model';

@Injectable({ providedIn: 'root' })
export class MunicipalServiceApi {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/municipal';

  getServices(): Observable<MunicipalService[]> {
    return this.http.get<MunicipalService[]>(`${this.baseUrl}/services`).pipe(
      catchError(this.handleError)
    );
  }

  getServiceById(id: string): Observable<MunicipalService> {
    return this.http.get<MunicipalService>(`${this.baseUrl}/services/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  createService(service: Partial<MunicipalService>): Observable<MunicipalService> {
    return this.http.post<MunicipalService>(`${this.baseUrl}/services`, service).pipe(
      catchError(this.handleError)
    );
  }

  submitApplication(dto: CreateApplicationDto): Observable<ApplicationRequest> {
    return this.http.post<ApplicationRequest>(`${this.baseUrl}/applications`, dto).pipe(
      catchError(this.handleError)
    );
  }

  getApplicationById(id: string): Observable<ApplicationRequest> {
    return this.http.get<ApplicationRequest>(`${this.baseUrl}/applications/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  getApplicationsByCitizen(citizenId: string): Observable<ApplicationRequest[]> {
    return this.http.get<ApplicationRequest[]>(`${this.baseUrl}/applications/citizen/${citizenId}`).pipe(
      catchError(this.handleError)
    );
  }

  updateApplicationStatus(id: string, status: RequestStatus): Observable<ApplicationRequest> {
    const payload: UpdateStatusDto = { status };
    return this.http.patch<ApplicationRequest>(`${this.baseUrl}/applications/${id}/status`, payload).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(err: HttpErrorResponse): Observable<never> {
    let msg = 'An unexpected error occurred';
    if (err.status === 0) {
      msg = 'Cannot reach Municipal Service backend. Is it running on port 8085?';
    } else if (err.error?.message) {
      msg = err.error.message;
    } else if (err.statusText) {
      msg = `${err.status}: ${err.statusText}`;
    }
    return throwError(() => new Error(msg));
  }
}
