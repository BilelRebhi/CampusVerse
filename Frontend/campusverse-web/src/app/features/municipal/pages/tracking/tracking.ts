import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ApplicationRequest, RequestStatus } from '../../models/municipal.model';
import { MunicipalServiceApi } from '../../services/municipal.service';

@Component({
  selector: 'app-tracking',
  imports: [FormsModule],
  templateUrl: './tracking.html',
  styleUrl: './tracking.css'
})
export class TrackingComponent implements OnInit {
  private readonly api = inject(MunicipalServiceApi);
  private readonly route = inject(ActivatedRoute);

  searchId = '';
  loading = false;
  errorMessage: string | null = null;
  application: ApplicationRequest | null = null;

  ngOnInit(): void {
    const paramId = this.route.snapshot.paramMap.get('id');
    if (paramId) {
      this.searchId = paramId;
      this.fetchApplication(paramId);
    }
  }

  onSearch(): void {
    if (!this.searchId.trim()) return;
    this.fetchApplication(this.searchId.trim());
  }

  fetchApplication(id: string): void {
    this.loading = true;
    this.errorMessage = null;

    this.api.getApplicationById(id).subscribe({
      next: (app) => {
        this.loading = false;
        this.application = app;
      },
      error: (err) => {
        this.loading = false;
        this.application = null;
        this.errorMessage = err.message || 'Application not found with that ID';
      }
    });
  }

  updateStatus(status: RequestStatus): void {
    if (!this.application) return;

    this.api.updateApplicationStatus(this.application.id, status).subscribe({
      next: (updated) => {
        this.application = updated;
      },
      error: (err) => {
        this.errorMessage = err.message;
      }
    });
  }

  isStepCompleted(step: number): boolean {
    if (!this.application) return false;
    const s = this.application.status;
    if (step === 1) return s !== 'PENDING';
    if (step === 2) return s === 'APPROVED' || s === 'REJECTED' || s === 'COMPLETED';
    if (step === 3) return s === 'APPROVED' || s === 'COMPLETED';
    return false;
  }

  isStepActive(step: number): boolean {
    if (!this.application) return false;
    const s = this.application.status;
    if (step === 1) return s === 'PENDING';
    if (step === 2) return s === 'IN_REVIEW';
    if (step === 3) return s === 'APPROVED' || s === 'REJECTED' || s === 'COMPLETED';
    return false;
  }
}
