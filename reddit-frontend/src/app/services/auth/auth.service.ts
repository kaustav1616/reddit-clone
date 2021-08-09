import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SignupRequestPayload } from 'src/app/dao/signup-request-payload';
import { HttpClient } from '@angular/common/http';

@Injectable({
	providedIn: 'root'
})
export class AuthService
{

	constructor(private http: HttpClient) { }

	signup(signupRequestPayload: SignupRequestPayload): Observable<any>
	{
		return this.http.post('http://localhost:8080/api/auth/signup', signupRequestPayload, {responseType: 'text'});
	}
}