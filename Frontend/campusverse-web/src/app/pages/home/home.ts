import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  template: `
    <h1>Welcome to CampusVerse</h1>
    <p class="muted">The smart university platform: rooms, clubs, events, equipment and lost &amp; found.</p>

    <div class="tiles">
      <!-- Each microservice owner adds a tile for their feature -->
      <a class="card tile" routerLink="/events">
        <h2>Events</h2>
        <p class="muted">Browse campus events, register, and manage your own events.</p>
      </a>
    </div>
  `,
  styles: `
    .tiles { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 1rem; margin-top: 1.5rem; }
    .tile { text-decoration: none; color: inherit; }
    .tile:hover { border-color: var(--primary); }
    .tile h2 { margin-top: 0; }
  `
})
export class Home {}
