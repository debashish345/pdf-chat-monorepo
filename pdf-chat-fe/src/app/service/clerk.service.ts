import { Injectable, signal } from '@angular/core';
import { Clerk } from '@clerk/clerk-js';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClerkService {

  public clerk: Clerk | null = null;
  
  //create a signal for clerk user is signed in or not
  public isSignedIn = signal(this.clerk?.isSignedIn);

  async load(): Promise<void> {
    this.clerk = new Clerk(environment.clerkPublishableKey);
      await this.clerk.load();
      console.info('Before load:', this.isSignedIn());
      console.info('Clerk loaded:', this.clerk.loaded);
      console.info('Is signed in:', this.clerk.isSignedIn);
      if (this.clerk.loaded && !this.clerk.isSignedIn) {
    }
  }
}
