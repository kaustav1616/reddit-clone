import { Injectable } from '@angular/core';
import
{
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { AuthService } from './auth/shared/auth.service';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { LoginResponse } from './auth/login/login-response';

@Injectable()
export class TokenInterceptor implements HttpInterceptor
{
  isTokenRefreshing = false;
  refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);
  constructor(public authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>
  {
    if (this.authService.getJwtToken())
    {
      this.addToken(req, this.authService.getJwtToken());
    }

    return next.handle(req).pipe(catchError(error =>
    {
      if (error instanceof HttpErrorResponse && error.status === 403 && !this.isTokenRefreshing)
      {
        return this.handleAuthErrors(req, next);
      }
      else
      {
        return throwError(error);
      }
    }));
  }

  private handleAuthErrors(req: HttpRequest<any>, next: HttpHandler)
  {
    this.isTokenRefreshing = true;
    this.refreshTokenSubject.next(null);

    return this.authService.refreshToken().pipe(
      switchMap((refreshTokenResponse: LoginResponse) =>
      {
        this.isTokenRefreshing = false;
        this.refreshTokenSubject.next(refreshTokenResponse.authenticationToken);
        return next.handle(this.addToken(req, refreshTokenResponse.authenticationToken));
      })
    )
  }

  private addToken(req: HttpRequest<any>, jwtToken: string)
  {
    return req.clone({
      headers: req.headers.set('Authorization',
        'Bearer ' + jwtToken)
    });
  }
}