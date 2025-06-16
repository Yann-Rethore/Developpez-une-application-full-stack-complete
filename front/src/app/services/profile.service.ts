import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserProfileDTO, UserProfileUpdateDTO } from '../interfaces/user-profile.dto';

@Injectable({ providedIn: 'root' })
export class ProfileService {
  private apiUrl = `${environment.apiUrl}/profile`;

  constructor(private http: HttpClient) {}

  getProfile(): Observable<UserProfileDTO> {
    return this.http.get<UserProfileDTO>(this.apiUrl);
  }

  updateProfile(data: UserProfileUpdateDTO): Observable<any> {
    return this.http.put(this.apiUrl, data);
  }
}

