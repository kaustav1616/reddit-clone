import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SignupRequestPayload } from 'src/app/auth/signup/signup-request-payload';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
	selector: 'app-signup',
	templateUrl: './signup.component.html',
	styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit
{
	signupForm: FormGroup;
	signupRequestPayload: SignupRequestPayload;
	registrationMessage: string = "";

	constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) 
	{
		this.signupForm = new FormGroup({
			username: new FormControl('', Validators.required),
			email: new FormControl('', [Validators.required, Validators.email]),
			password: new FormControl('', Validators.required)
		});

		this.signupRequestPayload =
		{
			username: '',
			email: '',
			password: ''
		};
	}

	ngOnInit(): void
	{
		this.registrationMessage = "";
	}

	signup()
	{
		this.signupRequestPayload.username = this.signupForm.get('username')!.value;
		this.signupRequestPayload.email = this.signupForm.get('email')!.value;
		this.signupRequestPayload.password = this.signupForm.get('password')!.value;

		this.authService.signup(this.signupRequestPayload)
			.subscribe(
				() =>
				{
					this.router.navigate(["/login"], {queryParams: {registered: "true"}});
				},
				() =>
				{
					this.toastr.error("Registration failed. Try again!");
				}
			);
	};
}