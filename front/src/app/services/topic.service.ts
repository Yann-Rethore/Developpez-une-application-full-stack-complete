import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { TopicDto } from '../interfaces/topic.dto';

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  private apiUrl = `${environment.apiUrl}/themes`; // adapte l’URL si besoin

  constructor(private http: HttpClient) {}

  getAllTopics(): Observable<TopicDto[]> {
    return this.http.get<TopicDto[]>(this.apiUrl);
  }

  subscribeToTopic(topicId: number) {
  return this.http.get(`${this.apiUrl}/subscribe/${topicId}`);
  }

  unsubscribeFromTopic(topicId: number) {
  return this.http.get(`${this.apiUrl}/unsubscribe/${topicId}`);
  }

  getUserSubscriptions(): Observable<number[]> {
  return this.http.get<number[]>(`${this.apiUrl}/me/subscriptions`);
  }
}
