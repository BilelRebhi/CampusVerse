import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApplicationRequest, MunicipalService } from '../../models/municipal.model';
import { MunicipalServiceApi } from '../../services/municipal.service';

@Component({
  selector: 'app-application',
  imports: [FormsModule, RouterLink],
  templateUrl: './application.html',
  styleUrl: './application.css'
})
export class ApplicationComponent implements OnInit {
  private readonly api = inject(MunicipalServiceApi);
  private readonly route = inject(ActivatedRoute);

  services: MunicipalService[] = [];
  selectedServiceId = '';
  selectedService: MunicipalService | null = null;
  citizenId = '';
  submitting = false;
  errorMessage: string | null = null;
  submittedApplication: ApplicationRequest | null = null;

  ngOnInit(): void {
    const paramServiceId = this.route.snapshot.paramMap.get('serviceId');

    this.api.getServices().subscribe({
      next: (services) => {
        this.services = services;
        if (paramServiceId && this.services.some(s => s.id === paramServiceId)) {
          this.selectedServiceId = paramServiceId;
          this.onServiceChange();
        }
      },
      error: (err) => {
        this.errorMessage = err.message;
      }
    });

    this.generateCitizenId();
  }

  onServiceChange(): void {
    this.selectedService = this.services.find(s => s.id === this.selectedServiceId) || null;
  }

  generateCitizenId(): void {
    this.citizenId = crypto.randomUUID();
  }

  onSubmit(): void {
    if (!this.selectedServiceId) {
      this.errorMessage = 'Please select a service';
      return;
    }

    this.submitting = true;
    this.errorMessage = null;

    this.api.submitApplication({
      serviceId: this.selectedServiceId,
      citizenId: this.citizenId ? this.citizenId : undefined
    }).subscribe({
      next: (app) => {
        this.submitting = false;
        this.submittedApplication = app;
      },
      error: (err) => {
        this.submitting = false;
        this.errorMessage = err.message;
      }
    });
  }

  resetForm(): void {
    this.submittedApplication = null;
    this.selectedServiceId = '';
    this.selectedService = null;
    this.generateCitizenId();
  }
}
