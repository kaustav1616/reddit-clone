import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { throwError } from 'rxjs';
import { AuthService } from '../shared/auth.service';
import { LoginRequest } from './login-request';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit
{
	isError: boolean = false;
	loginForm: FormGroup;
	loginRequest: LoginRequest;

	constructor(private authService: AuthService, private activatedRoute: ActivatedRoute, private router: Router, private toastr: ToastrService) 
	{
		this.loginForm = new FormGroup(
			{
				username: new FormControl('', Validators.required),
				password: new FormControl('', Validators.required)
			});

		this.loginRequest = 
		{
			username : "",
			password : ""
		};
	}

	ngOnInit(): void
	{
		this.isError = false;

		this.activatedRoute.queryParams
			.subscribe(params =>
				{
					if(params.registered != undefined && params.registered == "true")
						this.toastr.success("Signup successful");
				});
	}

	login()
	{
		this.loginRequest.username = this.loginForm.get('username')!.value;
    	this.loginRequest.password = this.loginForm.get('password')!.value;

		this.authService.login(this.loginRequest).subscribe(
			data =>
			{
				this.isError = false;
				this.toastr.success("Login successful.");
				this.router.navigateByUrl("");
				console.log(data);
			},
			error =>
			{
				this.isError = true;
				this.toastr.error("Login unsuccessful. Please check username / password.");
				console.log(error);
				throwError(error);
			}
		);
	}
}