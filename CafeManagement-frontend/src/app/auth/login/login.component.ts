import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ShareddataService } from 'src/app/services/sharedData/shared-data.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string = '';
  userDetails: any = null;
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private sharedDataService: ShareddataService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  onSubmit() {
    const email = this.loginForm.value.email;
    const password = this.loginForm.value.password;
    this.authService.login(email, password).subscribe(
      (response: any) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('role', response.role);
        // this.sharedDataService.setUserRole(localStorage.getItem('role'));
        this.authService.getUserDetails().subscribe(
          (user: any) => {
            console.log(response.token);
            // console.log(user);

            this.sharedDataService.setUserDetails(user);
            this.userDetails = user;
          },
          (error) => {
            this.toastr.error('Something went wrong!');
          }
        );

        this.router.navigate(['/']);
        this.toastr.success('Yay! You are logged in.', 'Login Succesful');
      },
      (error) => {
        this.errorMessage = 'Wrong user credentials';
        console.log(this.errorMessage);
        this.toastr.error('Please enter correct login credentials.');
      }
    );
  }
  goToHome() {
    this.router.navigate(['/']);
  }
}
