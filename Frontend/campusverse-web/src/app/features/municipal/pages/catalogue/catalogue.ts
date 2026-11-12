import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MunicipalService } from '../../models/municipal.model';
import { MunicipalServiceApi } from '../../services/municipal.service';

@Component({
  selector: 'app-catalogue',
  imports: [RouterLink],
  templateUrl: './catalogue.html',
  styleUrl: './catalogue.css'
})
export class CatalogueComponent implements OnInit {
  private readonly api = inject(MunicipalServiceApi);

  services: MunicipalService[] = [];
  loading = true;
  error: string | null = null;

  ngOnInit(): void {
    this.api.getServices().subscribe({
      next: (data) => {
        this.services = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }
}
