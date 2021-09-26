import { EventEmitter, Injectable, Output } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { SignupRequestPayload } from 'src/app/auth/signup/signup-request-payload';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from '../login/login-request';
import { LoginResponse } from '../login/login-response';
import { catchError, map, tap } from 'rxjs/operators';
import { LocalStorageService } from 'ngx-webstorage';

@Injectable({
	providedIn: 'root'
})
export class AuthService
{
	@Output() loggedIn: EventEmitter<boolean> = new EventEmitter();
  	@Output() username: EventEmitter<string> = new EventEmitter();
	  
	constructor(private http: HttpClient, private localStorage: LocalStorageService) { }

	signup(signupRequestPayload: SignupRequestPayload): Observable<any>
	{
		return this.http.post('http://localhost:8080/api/auth/signup', signupRequestPayload, { responseType: 'text' });
	}

	login(loginrequest: LoginRequest): Observable<boolean>
	{
		return this.http.post<LoginResponse>('http://localhost:8080/api/auth/login', loginrequest)
			.pipe(map(data =>
			{
				this.localStorage.store('authenticationToken', data.authenticationToken);
				this.localStorage.store('username', data.username);
				this.localStorage.store('refreshToken', data.refreshToken);
				this.localStorage.store('expiresAt', data.expiresAt);
				return true;
			}),
				catchError(error =>
				{
					console.log(error);
					return throwError(error);
				}));
	}

	refreshToken()
	{
		// step 1: create refresh token payload
		const refreshTokenPayload =
		{
			refreshToken: this.getRefreshToken(),
			username: this.getUserName()
		};

		// step 2: http request
		return this.http.post<LoginResponse>('http://localhost:8080/api/auth/refresh/token', refreshTokenPayload)
			.pipe(tap(response =>
			{
				this.localStorage.store("authenticationToken", response.authenticationToken);
				this.localStorage.store("expiresAt", response.expiresAt);
			}));
	}

	getJwtToken()
	{
		return this.localStorage.retrieve('authenticationToken');
	}

	getRefreshToken()
	{
		return this.localStorage.retrieve('refreshToken');
	}

	getUserName()
	{
		return this.localStorage.retrieve('username');
	}

	getExpirationTime()
	{
		return this.localStorage.retrieve('expiresAt');
	}

	isLoggedIn(): boolean
	{
		return this.getJwtToken() != null;
	}
}