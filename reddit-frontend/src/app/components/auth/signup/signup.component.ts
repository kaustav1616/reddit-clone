import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SignupRequestPayload } from 'src/app/dao/signup-request-payload';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
	selector: 'app-signup',
	templateUrl: './signup.component.html',
	styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit
{
	signupForm: FormGroup;
	signupRequestPayload: SignupRequestPayload;

	constructor(private authService: AuthService) 
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
	}

	signup()
	{
		this.signupRequestPayload.username = this.signupForm.get('username')!.value;
		this.signupRequestPayload.email = this.signupForm.get('email')!.value;
		this.signupRequestPayload.password = this.signupForm.get('password')!.value;

		this.authService.signup(this.signupRequestPayload)
							.subscribe(data => 
								{
									console.log(data);
								});
	}
}