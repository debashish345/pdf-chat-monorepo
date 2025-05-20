import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Clerk } from '@clerk/clerk-js';
import { environment } from '../../../environments/environment';
import { ClerkService } from '../../service/clerk.service';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent implements AfterViewInit {
  @ViewChild('signInContainer') signInContainer!: ElementRef;
  @ViewChild('userButtonContainer') userButtonContainer!: ElementRef;

  constructor(public clerkService: ClerkService) {
  }

  async ngAfterViewInit(): Promise<void> {
    if (!this.clerkService.clerk?.isSignedIn) {
      this.clerkService.clerk?.mountSignIn(this.signInContainer.nativeElement);
    }
    else {
      this.clerkService.clerk?.mountUserButton(this.userButtonContainer.nativeElement);
    }
  }

  // ngOnDestroy(): void {
  //   this.clerk?.unmountSignIn(this.signInContainer.nativeElement);
  //   this.clerk?.unmountUserButton(this.userButtonContainer.nativeElement);
  // }
}